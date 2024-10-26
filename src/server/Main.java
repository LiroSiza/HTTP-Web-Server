package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static int port = 8000; // Definir el puerto
    private static HttpServer server;
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

            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}