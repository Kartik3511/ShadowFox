package server;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages all active chat rooms.
 * Creates a room on first join, removes it when it becomes empty.
 */
public class RoomManager {

    private final ConcurrentHashMap<String, ChatRoom> rooms = new ConcurrentHashMap<>();

    /** Returns (or creates) the room with the given name. */
    public ChatRoom getOrCreate(String roomName) {
        return rooms.computeIfAbsent(roomName, ChatRoom::new);
    }

    /** Removes the room if it has no members. */
    public void removeIfEmpty(String roomName) {
        rooms.computeIfPresent(roomName, (name, room) -> room.isEmpty() ? null : room);
    }

    public Collection<ChatRoom> getAllRooms() {
        return Collections.unmodifiableCollection(rooms.values());
    }

    public boolean roomExists(String roomName) {
        return rooms.containsKey(roomName);
    }
}
