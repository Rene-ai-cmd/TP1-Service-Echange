package main;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ControleurDevices implements HttpHandler {

   private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String methode = exchange.getRequestMethod();

        switch (methode) {
            case "":
        }
    }
}
