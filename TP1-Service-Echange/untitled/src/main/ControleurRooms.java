package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ControleurRooms implements HttpHandler {

    private final ObjectMapper mapper= new ObjectMapper();
    @Override
    public void handle(HttpExchange echange) throws IOException {
        // GET /rooms
        // POST /rooms
        // DELETE /rooms
        try {
        String methode = echange.getRequestMethod();
        switch (methode) {
            case "GET" -> listerPiece(echange);
            case "POST" -> ajouterPiece(echange);
            case "DELETE" -> supprimerPiece(echange);
            default -> repondre(echange , 405 , "Methode non autorise");
        }
    }catch (Exception e) {
            e.printStackTrace();  // ← va afficher l'erreur dans la console
            repondre(echange, 500, "Erreur: " + e.getMessage());
        }
    }
        // Sérialisation
    public void listerPiece(HttpExchange echange) throws IOException {
        String json = mapper.writeValueAsString(Device.listRooms());
        repondre(echange , 200, json);
    }

    public void ajouterPiece(HttpExchange echange) throws IOException {
        String corps = lireCorps(echange);
        Map<String, String> data = mapper.readValue(corps ,Map.class);
        String nom = data.get("nom");


    try {
        Device.addRoom(nom);
        repondre(echange, 200 , "Pièce ajouté");
    } catch (IllegalArgumentException e) {
        // "Pièce existante" → 409 Conflict
        repondre(echange, 400, e.getMessage());
    }
    }

    public void supprimerPiece(HttpExchange echange) throws IOException {
        String corps = lireCorps(echange);
        Map<String, String> data = mapper.readValue(corps, Map.class);
        String nom = data.get("nom");

        boolean supprime = Device.listRooms().remove(nom);
        if (supprime)
            repondre(echange , 200, "Pièce supprimé");
        else
            repondre(echange , 404, "Pièce introuvable");
    }
// Lire le corps de la requete
    private  String lireCorps(HttpExchange echange) throws IOException {
        InputStream flux = echange.getRequestBody();
        return  new String(flux.readAllBytes(), StandardCharsets.UTF_8);
    }

    // Envoyer une réponse texte ou JSON
    private void  repondre(HttpExchange echange, int code, String corps) throws IOException {
        byte[] octets = corps.getBytes(StandardCharsets.UTF_8);
        echange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        echange.sendResponseHeaders(code, octets.length);
        OutputStream os = echange.getResponseBody();
        os.write(octets);
        os.close();
    }

}
