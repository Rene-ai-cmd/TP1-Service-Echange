package main;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class ControleurDevices implements HttpHandler {

   private final List<Device> devices;

    public ControleurDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        String methode = exchange.getRequestMethod();
//
//        switch (methode) {
//            case "":
//        }
    }
}
