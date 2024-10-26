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

public class EchoPostHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Served by /echoPost handler...");

        // parse request
        Map<String, Object> parameters = new HashMap<>();
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query, parameters);

        // storage
        Usuario newUser = new Usuario();
        newUser.setName((String) parameters.get("name"));
        newUser.setHobby((String) parameters.get("hobby"));
        Main.dataStore.add(newUser);

        // send response
        String response = "";
        response = "USER ADDED\n";
        response += "name = " + newUser.getName() + "\n";
        response += "hobby = " + newUser.getHobby() + "\n";
        /*for (String key : parameters.keySet()) {
            response += key + "=" + parameters.get(key) + "\n";
        }*/
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
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
}
