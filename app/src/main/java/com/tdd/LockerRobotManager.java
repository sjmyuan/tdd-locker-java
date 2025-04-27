package com.tdd;

import java.util.Arrays;

/**
 * LockerRobotManager manages both lockers and robots.
 * It can store bags in robots first, and if all robots are full, it will store bags in its own lockers.
 */
public class LockerRobotManager {
    private final Locker[] lockers;
    private final LockerRobot[] robots;
    
    /**
     * Constructs a LockerRobotManager with the given lockers and robots.
     * 
     * @param lockers The lockers managed directly by this manager
     * @param robots The robots managed by this manager
     * @throws IllegalArgumentException if both lockers and robots are null or empty
     */
    public LockerRobotManager(Locker[] lockers, LockerRobot[] robots) {
        validateConstructorParams(lockers, robots);
        
        this.lockers = lockers != null ? Arrays.copyOf(lockers, lockers.length) : new Locker[0];
        this.robots = robots != null ? Arrays.copyOf(robots, robots.length) : new LockerRobot[0];
    }
    
    private void validateConstructorParams(Locker[] lockers, LockerRobot[] robots) {
        if (isEmpty(lockers) && isEmpty(robots)) {
            throw new IllegalArgumentException("Both lockers and robots cannot be null");
        }
    }
    
    private boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
    
    /**
     * Stores a bag and returns a ticket.
     * Tries to store in robots first, then in manager's own lockers if all robots are full.
     * 
     * @param content The bag content to store
     * @return A ticket that can be used to retrieve the bag
     * @throws IllegalStateException if there is no available capacity in any robot or locker
     */
    public String store_bag(String content) {
        String ticket = tryStoreInRobots(content);
        if (ticket != null) {
            return ticket;
        }
        
        ticket = tryStoreInLockers(content);
        if (ticket != null) {
            return ticket;
        }
        
        throw new IllegalStateException("No available capacity in any robot or locker");
    }
    
    private String tryStoreInRobots(String content) {
        for (LockerRobot robot : robots) {
            if (robot.hasAvailableCapacity()) {
                return robot.store_bag(content);
            }
        }
        return null;
    }
    
    private String tryStoreInLockers(String content) {
        for (Locker locker : lockers) {
            if (!locker.isFull()) {
                return locker.store_bag(content);
            }
        }
        return null;
    }
    
    /**
     * Retrieves a bag using a ticket.
     * Tries to retrieve from all robots and lockers until the bag is found.
     * 
     * @param ticket The ticket for the bag to retrieve
     * @return The content of the retrieved bag
     * @throws IllegalArgumentException if the ticket is invalid
     */
    public String retrieve_bag(String ticket) {
        String content = tryRetrieveFromRobots(ticket);
        if (content != null) {
            return content;
        }
        
        content = tryRetrieveFromLockers(ticket);
        if (content != null) {
            return content;
        }
        
        throw new IllegalArgumentException("Invalid ticket");
    }
    
    private String tryRetrieveFromRobots(String ticket) {
        for (LockerRobot robot : robots) {
            try {
                return robot.retrieve_bag(ticket);
            } catch (IllegalArgumentException e) {
                // Continue to next robot if ticket not found in current robot
            }
        }
        return null;
    }
    
    private String tryRetrieveFromLockers(String ticket) {
        for (Locker locker : lockers) {
            try {
                return locker.retrieve_bag(ticket);
            } catch (IllegalArgumentException e) {
                // Continue to next locker if ticket not found in current locker
            }
        }
        return null;
    }
    
    /**
     * Checks if the manager has available capacity either in robots or lockers.
     * 
     * @return true if there is available capacity, false otherwise
     */
    public boolean hasAvailableCapacity() {
        return hasAvailableRobotCapacity() || hasAvailableLockerCapacity();
    }
    
    private boolean hasAvailableRobotCapacity() {
        return Arrays.stream(robots).anyMatch(LockerRobot::hasAvailableCapacity);
    }
    
    private boolean hasAvailableLockerCapacity() {
        return Arrays.stream(lockers).anyMatch(locker -> !locker.isFull());
    }
}