package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * Handles a single connected client on its own thread.
 *
 * Supported commands (client → server):
 *   /nick <name>    – set or change display name
 *   /join <room>    – join (or create) a chat room
 *   /leave          – leave the current room
 *   /rooms          – list all active rooms with member counts
 *   /who            – list members of the current room
 *   /quit           – disconnect
 *   <anything else> – send as a chat message to the current room
 */
public class ClientHandler implements Runnable {

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Socket socket;
    private final RoomManager roomManager;
    private PrintWriter out;

    private String username = "Guest_" + (int)(Math.random() * 9000 + 1000);
    private ChatRoom currentRoom = null;

    public ClientHandler(Socket socket, RoomManager roomManager) {
        this.socket = socket;
        this.roomManager = roomManager;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            this.out = writer;
            sendMessage("SERVER: Welcome to JavaChat! Commands:");
            sendMessage("SERVER:   /nick <name>  – set your display name");
            sendMessage("SERVER:   /join <room>  – join or create a room");
            sendMessage("SERVER:   /leave        – leave current room");
            sendMessage("SERVER:   /rooms        – list all rooms");
            sendMessage("SERVER:   /who          – list room members");
            sendMessage("SERVER:   /quit         – disconnect");
            sendMessage("SERVER: Your temporary name is: " + username);

            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("/nick ")) {
                    handleNick(line.substring(6).trim());
                } else if (line.startsWith("/join ")) {
                    handleJoin(line.substring(6).trim());
                } else if (line.equals("/leave")) {
                    handleLeave();
                } else if (line.equals("/rooms")) {
                    handleRooms();
                } else if (line.equals("/who")) {
                    handleWho();
                } else if (line.equals("/quit")) {
                    sendMessage("SERVER: Goodbye, " + username + "!");
                    break;
                } else if (line.startsWith("/")) {
                    sendMessage("SERVER: Unknown command. Type /quit to exit.");
                } else {
                    handleMessage(line);
                }
            }
        } catch (IOException e) {
            // Client disconnected unexpectedly – handled below
        } finally {
            disconnect();
        }
    }

    // ── Command handlers ────────────────────────────────────────────────────

    private void handleNick(String newName) {
        if (newName.isEmpty()) {
            sendMessage("SERVER: Usage: /nick <name>");
            return;
        }
        if (newName.length() > 20) {
            sendMessage("SERVER: Name too long (max 20 chars).");
            return;
        }
        String oldName = username;
        username = newName;
        sendMessage("SERVER: Name changed to " + username);
        if (currentRoom != null) {
            currentRoom.broadcast(
                "SERVER: " + oldName + " is now known as " + username, this);
        }
    }

    private void handleJoin(String roomName) {
        if (roomName.isEmpty()) {
            sendMessage("SERVER: Usage: /join <room>");
            return;
        }
        if (roomName.length() > 30) {
            sendMessage("SERVER: Room name too long (max 30 chars).");
            return;
        }
        // Leave current room first
        if (currentRoom != null) {
            leaveRoom();
        }
        currentRoom = roomManager.getOrCreate(roomName);
        currentRoom.addMember(this);
        currentRoom.broadcast(
            "SERVER: " + username + " has joined #" + roomName, this);
        sendMessage("SERVER: Joined #" + roomName
                + " (" + currentRoom.getMemberCount() + " member(s)).");
    }

    private void handleLeave() {
        if (currentRoom == null) {
            sendMessage("SERVER: You are not in any room.");
            return;
        }
        String roomName = currentRoom.getName();
        leaveRoom();
        sendMessage("SERVER: You left #" + roomName + ".");
    }

    private void handleRooms() {
        Collection<ChatRoom> allRooms = roomManager.getAllRooms();
        if (allRooms.isEmpty()) {
            sendMessage("SERVER: No active rooms. Create one with /join <room>.");
            return;
        }
        sendMessage("SERVER: Active rooms:");
        for (ChatRoom room : allRooms) {
            String marker = (room == currentRoom) ? " <- (you are here)" : "";
            sendMessage("  #" + room.getName()
                    + " [" + room.getMemberCount() + " member(s)]" + marker);
        }
    }

    private void handleWho() {
        if (currentRoom == null) {
            sendMessage("SERVER: You are not in any room.");
            return;
        }
        sendMessage("SERVER: Members of #" + currentRoom.getName() + ":");
        for (ClientHandler member : currentRoom.getMembers()) {
            String tag = (member == this) ? " (you)" : "";
            sendMessage("  - " + member.username + tag);
        }
    }

    private void handleMessage(String text) {
        if (currentRoom == null) {
            sendMessage("SERVER: Join a room first with /join <room>.");
            return;
        }
        String timestamp = LocalTime.now().format(TIME_FMT);
        String formatted = "[" + timestamp + "] " + username + ": " + text;
        currentRoom.broadcast(formatted, this);
        // Echo back to sender with same format
        sendMessage(formatted);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    /** Remove from current room and clean up empty rooms. */
    private void leaveRoom() {
        if (currentRoom == null) return;
        String roomName = currentRoom.getName();
        currentRoom.broadcast(
            "SERVER: " + username + " has left #" + roomName, this);
        currentRoom.removeMember(this);
        roomManager.removeIfEmpty(roomName);
        currentRoom = null;
    }

    private void disconnect() {
        leaveRoom();
        try { socket.close(); } catch (IOException ignored) {}
        System.out.println("[Server] " + username + " disconnected.");
    }

    /** Thread-safe send: called by other ClientHandlers' threads. */
    public synchronized void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
}
