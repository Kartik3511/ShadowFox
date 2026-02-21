package server;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a single chat room.
 * Thread-safe: multiple ClientHandlers can call broadcast() concurrently.
 */
public class ChatRoom {

    private final String name;
    private final Set<ClientHandler> members = ConcurrentHashMap.newKeySet();

    public ChatRoom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addMember(ClientHandler client) {
        members.add(client);
    }

    public void removeMember(ClientHandler client) {
        members.remove(client);
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public int getMemberCount() {
        return members.size();
    }

    public Set<ClientHandler> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    /** Broadcasts a message to every member except the sender. */
    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler member : members) {
            if (member != sender) {
                member.sendMessage(message);
            }
        }
    }

    /** Broadcasts a message to ALL members including the sender. */
    public void broadcastAll(String message) {
        for (ClientHandler member : members) {
            member.sendMessage(message);
        }
    }
}
