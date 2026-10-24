package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ControleurDevices implements HttpHandler {

   private final List<Device> devices;
   private  final ObjectMapper mapper = new ObjectMapper();

    public ControleurDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public void handle(HttpExchange echange) throws IOException {
        try {
        String methode = echange.getRequestMethod();
        String query = echange.getRequestURI().getQuery();
        switch (methode) {
            case "GET" -> handleGet(echange, query);
            case "POST" -> ajouterDevice(echange);
            case "DELETE" -> supprimerDevice(echange, query);
            case "PATCH" -> modifierDevice(echange, query);
            default -> repondre(echange, 405, "Méthode non authorisée");
        }
    } catch (Exception e) {
            e.printStackTrace();
            repondre(echange, 500, "Erreur: " + e.getMessage());
        }

    }
// GET
    private void handleGet(HttpExchange echange, String query) throws IOException {
        List<Device> resultat;

        if (query != null && query.startsWith("room=")){
            String room = query.split("=")[1];
            resultat = devices.stream()
                    .filter(d -> room.equalsIgnoreCase(d.getRoom()))
                    .toList();
        } else if (query != null && query.startsWith("subtype=")){
            String subtype = query.split("=")[1];
            resultat = devices.stream()
                    .filter(d -> subtype.equalsIgnoreCase(d.getClass().getSimpleName()))
                    .toList();
        } else {
            resultat = devices;
        }
        repondre(echange, 200 , mapper.writeValueAsString(resultat));
    }
    // POST
    private void ajouterDevice(HttpExchange echange) throws  IOException {
        String corps = lireCorps(echange);

        Device device = mapper.readValue(corps, Device.class);
        devices.add(device);
        repondre(echange, 200, String.valueOf(device.getId()));
    }

    // DELETE
    private  void supprimerDevice(HttpExchange echange, String query) throws IOException {
        if (query == null || !query.startsWith("id=")) {
            repondre(echange, 400, "Paramètre id requis");
            return;
        }
        int id = Integer.parseInt(query.split("=")[1]);
        boolean supprime = devices.removeIf(d -> d.getId() == id);

        if (supprime)
            repondre(echange, 200, "Appareil supprimé");
        else
            repondre(echange, 404, "Appareil introuvable");
    }

    // PATCH  modifier un appareil ou une pièce
    //   ?id=1 → modifier un appareil spécifique
    //?room=Salon → allumer/éteindre tous les appareils d’une pièce
    private void modifierDevice(HttpExchange echange, String query) throws  IOException {
        String corps = lireCorps(echange);
        Map<String, Object> data = mapper.readValue(corps, Map.class);

        // Cas 1: modifier un appareil spécifique
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.split("=")[1]);
            Device device = devices.stream()
                    .filter(d -> d.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (device == null) {
                repondre(echange, 404, "Appareil introuvable");
                return;

            }
            appliquerModifications(device, data);
            repondre(echange, 200, "Appareil modifié");

            // Cas 2 : allumer/éteindre toute une pièce
        } else if (query != null && query.startsWith("room=")) {
            String room = query.split("=")[1];
            devices.stream()
                    .filter(d -> room.equalsIgnoreCase(d.getRoom()))
                    .forEach(d -> appliquerModifications(d, data));
            repondre(echange, 200, "Pi'ece modifiée");
        } else {
            repondre(echange, 400, "Paramètre id ou room requis");
        }
    }
        private void appliquerModifications (Device device, Map<String, Object> data) {
            // Champs communs à tous les appareils
            if (data.containsKey("on")) {
                boolean on = (boolean) data.get("on");
                if (on) device.turnOn();
                else device.turnOff();
            }
            if (data.containsKey("room")){
                device.setRoom((String) data.get("room"));
            }

            // Champs spécifiques à Light
            if (device instanceof Light light){
                if (data.containsKey("intensity"))
                    light.setIntensity((int) data.get("intensity"));
                if (data.containsKey("colorTemp"))
                    light.setColorTemp((int) data.get("colorTemp"));
            }

            // Champs spécifiques à Vacuum
            if (device instanceof Vacuum vacuum) {
                if (data.containsKey("task")) {
                    String task = (String) data.get("task");
                    switch (task) {
                        case "Nettoyage d'une pièce" -> vacuum.clean((String) data.get("room"));
                        case "Nettoyage de la maison" -> vacuum.cleanAll();
                        case  "stop"                  -> vacuum.stop();
                    }
                }
            }
        }


    private String lireCorps(HttpExchange echange) throws IOException {
        InputStream flux = echange.getRequestBody();
        return  new String(flux.readAllBytes(), StandardCharsets.UTF_8);

    }

    private void repondre(HttpExchange echange, int code, String corps) throws IOException {
        byte[] octes = corps.getBytes(StandardCharsets.UTF_8);
        echange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        echange.sendResponseHeaders(code, octes.length);
        OutputStream os = echange.getResponseBody();
        os.write(octes);
        os.close();
    }

}
