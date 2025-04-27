package com.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrimaryLockerRobotTest {

    @Test
    public void should_throw_exception_when_given_no_lockers() {
        // When creating a robot with no lockers, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new PrimaryLockerRobot(new Locker[0])
        );
        
        assertEquals("At least one locker is required", exception.getMessage());
    }
    
    @Test
    public void should_store_bag_in_first_locker_when_all_lockers_have_capacity() {
        // Given two lockers both having available capacity
        Locker firstLocker = new Locker(1);
        Locker secondLocker = new Locker(1);
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{firstLocker, secondLocker});
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = robot.store_bag(bagContent);
        
        // Then the bag should be stored in the first locker
        assertNotNull(ticket);
        assertTrue(firstLocker.isFull());
        assertFalse(secondLocker.isFull());
        
        // And the bag should be retrievable from the first locker with the ticket
        assertEquals(bagContent, firstLocker.retrieve_bag(ticket));
    }
    
    @Test
    public void should_store_bag_in_second_locker_when_first_locker_is_full() {
        // Given first locker is full and second locker has capacity
        Locker firstLocker = new Locker(1);
        firstLocker.store_bag("some bag");
        Locker secondLocker = new Locker(1);
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{firstLocker, secondLocker});
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = robot.store_bag(bagContent);
        
        // Then the bag should be stored in the second locker
        assertNotNull(ticket);
        assertTrue(secondLocker.isFull());
        
        // And the bag should be retrievable from the second locker with the ticket
        assertEquals(bagContent, secondLocker.retrieve_bag(ticket));
    }
    
    @Test
    public void should_throw_exception_when_all_lockers_are_full() {
        // Given all lockers are full
        Locker firstLocker = new Locker(1);
        firstLocker.store_bag("some bag");
        Locker secondLocker = new Locker(1);
        secondLocker.store_bag("other bag");
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{firstLocker, secondLocker});
        
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
        Locker firstLocker = new Locker(1);
        Locker secondLocker = new Locker(1);
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{firstLocker, secondLocker});
        String bagContent = "bag content";
        String ticket = robot.store_bag(bagContent);
        
        // When retrieving with a valid ticket
        String retrievedContent = robot.retrieve_bag(ticket);
        
        // Then the correct bag content should be returned
        assertEquals(bagContent, retrievedContent);
    }
    
    @Test
    public void should_retrieve_bag_from_correct_locker_based_on_ticket() {
        // Given bags have been stored in different lockers
        Locker firstLocker = new Locker(1);
        Locker secondLocker = new Locker(1);
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{firstLocker, secondLocker});
        String firstBagContent = "first bag";
        String secondBagContent = "second bag";
        
        // Store in first locker
        String firstTicket = robot.store_bag(firstBagContent);
        
        // Make the first locker full to ensure second bag goes to second locker
        while (!firstLocker.isFull()) {
            firstLocker.store_bag("filler bag");
        }
        
        // Store in second locker
        String secondTicket = robot.store_bag(secondBagContent);
        
        // When retrieving with both tickets
        String firstRetrievedContent = robot.retrieve_bag(firstTicket);
        String secondRetrievedContent = robot.retrieve_bag(secondTicket);
        
        // Then the correct bag content should be returned for each
        assertEquals(firstBagContent, firstRetrievedContent);
        assertEquals(secondBagContent, secondRetrievedContent);
    }
    
    @Test
    public void should_throw_exception_when_retrieving_with_invalid_ticket() {
        // Given a robot with lockers
        Locker firstLocker = new Locker(1);
        Locker secondLocker = new Locker(1);
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{firstLocker, secondLocker});
        
        // When retrieving with an invalid ticket, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> robot.retrieve_bag("invalid-ticket")
        );
        
        assertEquals("Invalid ticket", exception.getMessage());
    }
    
    @Test
    public void should_not_be_able_to_reuse_ticket_after_retrieving_bag() {
        // Given a bag has been stored and retrieved
        Locker firstLocker = new Locker(1);
        Locker secondLocker = new Locker(1);
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{firstLocker, secondLocker});
        String ticket = robot.store_bag("bag content");
        robot.retrieve_bag(ticket);
        
        // When trying to retrieve with the same ticket again, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> robot.retrieve_bag(ticket)
        );
        
        assertEquals("Invalid ticket", exception.getMessage());
    }
}