package com.tdd;

import java.util.Arrays;

public class PrimaryLockerRobot extends LockerRobot {
    private final Locker[] lockers;
    
    public PrimaryLockerRobot(Locker[] lockers) {
        validateLockers(lockers);
        this.lockers = Arrays.copyOf(lockers, lockers.length);
    }
    
    private void validateLockers(Locker[] lockers) {
        if (lockers == null || lockers.length == 0) {
            throw new IllegalArgumentException("At least one locker is required");
        }
    }
    
    public String store_bag(String content) {
        Locker availableLocker = findFirstAvailableLocker();
        if (availableLocker == null) {
            throw new IllegalStateException("All lockers are full");
        }
        return availableLocker.store_bag(content);
    }
    
    private Locker findFirstAvailableLocker() {
        return Arrays.stream(lockers)
                .filter(locker -> !locker.isFull())
                .findFirst()
                .orElse(null);
    }
    
    public String retrieve_bag(String ticket) {
        for (Locker locker : lockers) {
            try {
                return locker.retrieve_bag(ticket);
            } catch (IllegalArgumentException e) {
                // Continue to next locker if ticket not found in current locker
            }
        }
        throw new IllegalArgumentException("Invalid ticket");
    }
    
    public boolean hasAvailableCapacity() {
        return findFirstAvailableLocker() != null;
    }
}