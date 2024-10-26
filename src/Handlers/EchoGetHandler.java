package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Main;
import server.Usuario;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EchoGetHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // parse request ~
        Map<String, Object> parameters = new HashMap<>();
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);

        // Obtener el índice del usuario
        int index;
        try {
            index = Integer.parseInt((String) parameters.get("index"));
        } catch (NumberFormatException | NullPointerException e) {
            sendResponse(exchange, "Invalid index format", 400);
            return;
        }

        // Validar el índice
        if (index < 0 || index >= Main.dataStore.size()) {
            sendResponse(exchange, "Index out of bounds", 400);
            return;
        }

        // Obtener el usuario correspondiente
        Usuario user = Main.dataStore.get(index);
        String response = "Usuario:\nNombre = " + user.getName() + "\nEmail = " + user.getHobby();

        // send response
        /*String response = "";
        for(String key : parameters.keySet())
            response += key +" = " + parameters.get(key) + "\n";*/
        exchange.sendResponseHeaders(200,response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    // Agregado
    public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
        if (query != null) {
            String[] pairs = query.split("[&]");

            for (String pair : pairs) {
                String[] param = pair.split("[=]");

                String key = null;
                String value = null;

                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
