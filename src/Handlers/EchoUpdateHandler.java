package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Main;
import server.Usuario;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EchoUpdateHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            // Procesar la solicitud
            Map<String, Object> parameters = new HashMap<>();
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);

            // Obtener el índice del usuario a actualizar
            int index;
            try {
                index = Integer.parseInt((String) parameters.get("index"));
            } catch (NumberFormatException e) {
                sendResponse(exchange, "Invalid index format", 400);
                return;
            }
            // Validar el índice
            if (index < 0 || index >= Main.dataStore.size()) {
                sendResponse(exchange, "Index out of bounds", 400);
                return;
            }

            // Obtener nuevos valores y actualizar el usuario
            Usuario userToUpdate = Main.dataStore.get(index);
            String newName = (String) parameters.get("name");
            String newHobby = (String) parameters.get("hobby");
            if (newName != null) userToUpdate.setName(newName);
            if (newHobby != null) userToUpdate.setHobby(newHobby);


            // Enviar respuesta
            String response = "Updated user:\n";
            response += "Name = " + Main.dataStore.get(index).getName() + "\n";
            response += "Hobby = " + Main.dataStore.get(index).getHobby() + "\n";
            /*for (String key : parameters.keySet()) {
                response += key + " = " + parameters.get(key) + "\n";
            }*/

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            // Metodo no permitido
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
        }
    }
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
