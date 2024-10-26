package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static int port = 8000; // Definir el puerto
    private static HttpServer server;

    // Lista dinámica para almacenar datos
    public static List<Usuario> dataStore = new ArrayList<>();

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        try {
            //this.port = port;
            server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("server started at " + port);

            server.createContext("/", new Handlers.RootHandler());
            server.createContext("/echoHeader", new Handlers.EchoHeaderHandler());
            server.createContext("/echoGet", new Handlers.EchoGetHandler());
            server.createContext("/echoPost", new Handlers.EchoPostHandler());
            server.createContext("/echoUpdate", new Handlers.EchoUpdateHandler());

            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}