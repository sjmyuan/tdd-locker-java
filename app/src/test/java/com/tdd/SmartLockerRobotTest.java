package com.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SmartLockerRobotTest {

    @Test
    public void should_throw_exception_when_given_no_lockers() {
        // When creating a smart robot with no lockers, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new SmartLockerRobot(new Locker[0])
        );
        
        assertEquals("At least one locker is required", exception.getMessage());
    }
    
    @Test
    public void should_store_bag_in_locker_with_max_capacity_when_given_lockers_with_different_available_capacity() {
        // Given two lockers with different available capacity
        Locker smallerAvailCapacityLocker = new Locker(5);
        smallerAvailCapacityLocker.store_bag("some bag"); // 4 spaces left
        
        Locker largerAvailCapacityLocker = new Locker(10);
        largerAvailCapacityLocker.store_bag("another bag"); // 9 spaces left
        
        SmartLockerRobot robot = new SmartLockerRobot(new Locker[]{smallerAvailCapacityLocker, largerAvailCapacityLocker});
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = robot.store_bag(bagContent);
        
        // Then the bag should be stored in the locker with larger available capacity
        assertNotNull(ticket);
        
        // Verify by retrieving from the expected locker
        assertEquals(bagContent, largerAvailCapacityLocker.retrieve_bag(ticket));
    }
    
    @Test
    public void should_store_bag_in_first_locker_when_all_lockers_have_equal_available_capacity() {
        // Given two lockers with the same available capacity
        Locker firstLocker = new Locker(5);
        firstLocker.store_bag("a bag"); // 4 spaces left
        
        Locker secondLocker = new Locker(6);
        secondLocker.store_bag("a bag");
        secondLocker.store_bag("another bag"); // 4 spaces left
        
        SmartLockerRobot robot = new SmartLockerRobot(new Locker[]{firstLocker, secondLocker});
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = robot.store_bag(bagContent);
        
        // Then the bag should be stored in the first locker
        assertNotNull(ticket);
        
        // Attempting to retrieve from the first locker should succeed
        assertEquals(bagContent, firstLocker.retrieve_bag(ticket));
    }
    
    @Test
    public void should_throw_exception_when_all_lockers_are_full() {
        // Given all lockers are full
        Locker fullLocker = new Locker(1);
        fullLocker.store_bag("some bag");
        
        SmartLockerRobot robot = new SmartLockerRobot(new Locker[]{fullLocker});
        
        // When trying to store a bag, then an exception should be thrown
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> robot.store_bag("bag content")
        );
        
        assertEquals("All lockers are full", exception.getMessage());
    }
    
    @Test
    public void should_retrieve_bag_with_valid_ticket() {
        // Given a bag has been stored by the robot
        Locker locker = new Locker(1);
        SmartLockerRobot robot = new SmartLockerRobot(new Locker[]{locker});
        String bagContent = "bag content";
        String ticket = robot.store_bag(bagContent);
        
        // When retrieving with a valid ticket
        String retrievedContent = robot.retrieve_bag(ticket);
        
        // Then the correct bag content should be returned
        assertEquals(bagContent, retrievedContent);
    }
    
    @Test
    public void should_throw_exception_when_retrieving_with_invalid_ticket() {
        // Given a robot with lockers
        Locker locker = new Locker(1);
        SmartLockerRobot robot = new SmartLockerRobot(new Locker[]{locker});
        
        // When retrieving with an invalid ticket, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> robot.retrieve_bag("invalid-ticket")
        );
        
        assertEquals("Invalid ticket", exception.getMessage());
    }
}