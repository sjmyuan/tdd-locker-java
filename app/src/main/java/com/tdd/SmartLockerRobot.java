package com.tdd;

import java.util.Arrays;

public class SmartLockerRobot extends LockerRobot {
    private final Locker[] lockers;
    
    public SmartLockerRobot(Locker[] lockers) {
        validateLockers(lockers);
        this.lockers = Arrays.copyOf(lockers, lockers.length);
    }
    
    private void validateLockers(Locker[] lockers) {
        if (lockers == null || lockers.length == 0) {
            throw new IllegalArgumentException("At least one locker is required");
        }
    }
    
    public String store_bag(String content) {
        Locker lockerWithMaxCapacity = findLockerWithMaxAvailableCapacity();
        if (lockerWithMaxCapacity == null) {
            throw new IllegalStateException("All lockers are full");
        }
        return lockerWithMaxCapacity.store_bag(content);
    }
    
    private Locker findLockerWithMaxAvailableCapacity() {
        Locker selectedLocker = null;
        int maxAvailableCapacity = 0;
        
        for (Locker locker : lockers) {
            if (!locker.isFull()) {
                int availableCapacity = locker.getAvailableCapacity();
                
                // Select the first locker encountered or a locker with greater capacity
                if (selectedLocker == null || availableCapacity > maxAvailableCapacity) {
                    selectedLocker = locker;
                    maxAvailableCapacity = availableCapacity;
                }
            }
        }
        
        return selectedLocker;
    }
    
    public String retrieve_bag(String ticket) {
        return findAndRetrieveBag(ticket);
    }
    
    private String findAndRetrieveBag(String ticket) {
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
        return Arrays.stream(lockers).anyMatch(locker -> !locker.isFull());
    }
}