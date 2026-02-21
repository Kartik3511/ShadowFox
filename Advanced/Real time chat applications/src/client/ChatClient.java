package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Terminal-based JavaChat client.
 *
 * Usage: java client.ChatClient [host] [port]
 * Default: localhost:5000
 *
 * Two threads run concurrently:
 *   - Reader thread : receives messages from server and prints them
 *   - Main thread   : reads stdin and sends to server
 */
public class ChatClient {

    private static final String DEFAULT_HOST = "localhost";
    private static final int    DEFAULT_PORT = 5000;

    public static void main(String[] args) {
        String host = DEFAULT_HOST;
        int    port = DEFAULT_PORT;

        if (args.length >= 1) host = args[0];
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port. Using default: " + DEFAULT_PORT);
            }
        }

        System.out.println("Connecting to " + host + ":" + port + " ...");

        try (
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader serverIn =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected! Type /quit to exit.\n");

            // Reader thread: continuously print server messages
            Thread readerThread = new Thread(() -> {
                try {
                    String serverLine;
                    while ((serverLine = serverIn.readLine()) != null) {
                        System.out.println(serverLine);
                    }
                } catch (IOException e) {
                    // Server closed the connection
                }
                System.out.println("\n[Disconnected from server]");
                System.exit(0);
            });
            readerThread.setDaemon(true);
            readerThread.start();

            // Main thread: read stdin and send to server
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                if (userInput.trim().equalsIgnoreCase("/quit")) {
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + host);
        } catch (IOException e) {
            System.err.println("Could not connect to " + host + ":" + port);
            System.err.println("Make sure the server is running.");
        }
    }
}
