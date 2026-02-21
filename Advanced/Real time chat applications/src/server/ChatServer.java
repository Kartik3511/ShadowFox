package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Entry point for the JavaChat server.
 *
 * Usage: java server.ChatServer [port]
 * Default port: 5000
 */
public class ChatServer {

    private static final int DEFAULT_PORT = 5000;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port. Using default: " + DEFAULT_PORT);
            }
        }

        RoomManager roomManager = new RoomManager();

        System.out.println("+--------------------------------+");
        System.out.println("|     JavaChat Server v1.0       |");
        System.out.println("+--------------------------------+");
        System.out.println("Listening on port " + port + " ...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] New connection from "
                        + clientSocket.getInetAddress().getHostAddress()
                        + ":" + clientSocket.getPort());

                ClientHandler handler = new ClientHandler(clientSocket, roomManager);
                Thread thread = new Thread(handler);
                thread.setDaemon(true);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("[Server] Fatal error: " + e.getMessage());
        }
    }
}
