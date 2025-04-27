package com.tdd;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Locker {
    private final int capacity;
    private final Map<String, String> storage = new HashMap<>();
    
    public Locker(int capacity) {
        validateCapacity(capacity);
        this.capacity = capacity;
    }
    
    private void validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Locker capacity must be greater than 0");
        }
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public String store_bag(String content) {
        validateLockerHasSpace();
        
        String ticket = generateTicket();
        storage.put(ticket, content);
        return ticket;
    }
    
    private void validateLockerHasSpace() {
        if (isFull()) {
            throw new IllegalStateException("Locker is full");
        }
    }
    
    public boolean isFull() {
        return storage.size() >= capacity;
    }
    
    public int getAvailableCapacity() {
        return capacity - storage.size();
    }
    
    private String generateTicket() {
        return UUID.randomUUID().toString();
    }
    
    public String retrieve_bag(String ticket) {
        validateTicket(ticket);
        
        String content = storage.get(ticket);
        storage.remove(ticket);
        return content;
    }
    
    private void validateTicket(String ticket) {
        if (!storage.containsKey(ticket)) {
            throw new IllegalArgumentException("Invalid ticket");
        }
    }
}