package com.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SuperLockerRobotTest {

    @Test
    public void should_throw_exception_when_given_no_lockers() {
        // When creating a super robot with no lockers, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new SuperLockerRobot(new Locker[0])
        );
        
        assertEquals("At least one locker is required", exception.getMessage());
    }
    
    @Test
    public void should_store_bag_in_locker_with_highest_vacancy_rate() {
        // Given two lockers with different vacancy rates
        // First locker: 3/5 available = 60% vacancy rate
        Locker firstLocker = new Locker(5);
        firstLocker.store_bag("bag 1");
        firstLocker.store_bag("bag 2");
        
        // Second locker: 2/5 available = 40% vacancy rate
        Locker secondLocker = new Locker(5);
        secondLocker.store_bag("bag 3");
        secondLocker.store_bag("bag 4");
        secondLocker.store_bag("bag 5");
        
        SuperLockerRobot robot = new SuperLockerRobot(new Locker[]{firstLocker, secondLocker});
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = robot.store_bag(bagContent);
        
        // Then the bag should be stored in the locker with higher vacancy rate (first locker)
        assertNotNull(ticket);
        
        // Verify by retrieving from the expected locker
        assertEquals(bagContent, firstLocker.retrieve_bag(ticket));
    }
    
    @Test
    public void should_store_bag_in_locker_with_highest_vacancy_rate_regardless_of_absolute_capacity() {
        // Given two lockers with different vacancy rates
        // First locker: 1/2 available = 50% vacancy rate
        Locker smallerLocker = new Locker(2);
        smallerLocker.store_bag("a bag");
        
        // Second locker: 3/10 available = 30% vacancy rate (but more absolute capacity)
        Locker largerLocker = new Locker(10);
        for (int i = 0; i < 7; i++) {
            largerLocker.store_bag("bag " + i);
        }
        
        SuperLockerRobot robot = new SuperLockerRobot(new Locker[]{largerLocker, smallerLocker});
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = robot.store_bag(bagContent);
        
        // Then the bag should be stored in the locker with higher vacancy rate (smaller locker)
        // despite having less absolute available capacity
        assertNotNull(ticket);
        
        // Verify by retrieving from the expected locker
        assertEquals(bagContent, smallerLocker.retrieve_bag(ticket));
    }
    
    @Test
    public void should_store_bag_in_first_locker_when_all_lockers_have_equal_vacancy_rate() {
        // Given two lockers with the same vacancy rate
        // First locker: 1/2 available = 50% vacancy rate
        Locker firstLocker = new Locker(2);
        firstLocker.store_bag("a bag");
        
        // Second locker: 2/4 available = 50% vacancy rate
        Locker secondLocker = new Locker(4);
        secondLocker.store_bag("a bag");
        secondLocker.store_bag("another bag");
        
        SuperLockerRobot robot = new SuperLockerRobot(new Locker[]{firstLocker, secondLocker});
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = robot.store_bag(bagContent);
        
        // Then the bag should be stored in the first locker
        assertNotNull(ticket);
        
        // Verify by retrieving from the expected locker
        assertEquals(bagContent, firstLocker.retrieve_bag(ticket));
    }
    
    @Test
    public void should_throw_exception_when_all_lockers_are_full() {
        // Given all lockers are full
        Locker fullLocker = new Locker(1);
        fullLocker.store_bag("some bag");
        
        SuperLockerRobot robot = new SuperLockerRobot(new Locker[]{fullLocker});
        
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
        SuperLockerRobot robot = new SuperLockerRobot(new Locker[]{locker});
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
        SuperLockerRobot robot = new SuperLockerRobot(new Locker[]{locker});
        
        // When retrieving with an invalid ticket, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> robot.retrieve_bag("invalid-ticket")
        );
        
        assertEquals("Invalid ticket", exception.getMessage());
    }
}