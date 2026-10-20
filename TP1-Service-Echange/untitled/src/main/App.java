package main;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        // Création de la liste des appareils en mémoire
        List<Device> devices = new ArrayList<>();

        // Créer le serveur sur le port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);

        // Enregistrer les routes
        server.createContext("/rooms", new ControleurRooms() {};
        server.createContext("/devices", new ControleurDevices(devices) {});

        // Démarrer
        server.setExecutor(null);
        server.start();
        System.out.println("Serveur démarré sur http://localhost:8080");
    }

}
