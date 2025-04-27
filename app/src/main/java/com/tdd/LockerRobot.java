package com.tdd;

/**
 * Abstract base class for all locker robot implementations
 * Provides common functionality for robot types that manage lockers
 */
public abstract class LockerRobot {
    
    /**
     * Stores a bag and returns a ticket
     * 
     * @param content The content of the bag to store
     * @return A ticket that can be used to retrieve the bag
     * @throws IllegalStateException if there is no available capacity
     */
    public abstract String store_bag(String content);
    
    /**
     * Retrieves a bag using a ticket
     * 
     * @param ticket The ticket for the bag to retrieve
     * @return The content of the retrieved bag
     * @throws IllegalArgumentException if the ticket is invalid
     */
    public abstract String retrieve_bag(String ticket);
    
    /**
     * Checks if the robot has available capacity
     * 
     * @return true if the robot has capacity to store more bags, false otherwise
     */
    public abstract boolean hasAvailableCapacity();
}