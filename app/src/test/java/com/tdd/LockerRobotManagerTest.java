package com.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LockerRobotManagerTest {

    @Test
    public void should_throw_exception_when_initializing_with_null_lockers_and_robots() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new LockerRobotManager(null, null)
        );
        assertEquals("Both lockers and robots cannot be null", exception.getMessage());
    }

    @Test
    public void should_store_bag_in_first_robot_when_all_robots_have_available_capacity() {
        // Given lockers and robots with available capacity
        PrimaryLockerRobot firstRobot = new PrimaryLockerRobot(new Locker[]{new Locker(1)});
        SmartLockerRobot secondRobot = new SmartLockerRobot(new Locker[]{new Locker(1)});
        Locker managerLocker = new Locker(1);
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{managerLocker},
            new LockerRobot[]{firstRobot, secondRobot}
        );
        
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = manager.store_bag(bagContent);
        
        // Then the bag should be stored in the first robot
        assertNotNull(ticket);
        
        // Verify by retrieving from the manager
        assertEquals(bagContent, manager.retrieve_bag(ticket));
    }
    
    @Test
    public void should_store_bag_in_next_robot_when_first_robot_is_full() {
        // Given first robot is full and second robot has capacity
        Locker fullLocker = new Locker(1);
        fullLocker.store_bag("some bag");
        PrimaryLockerRobot fullRobot = new PrimaryLockerRobot(new Locker[]{fullLocker});
        
        SmartLockerRobot availableRobot = new SmartLockerRobot(new Locker[]{new Locker(1)});
        Locker managerLocker = new Locker(1);
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{managerLocker},
            new LockerRobot[]{fullRobot, availableRobot}
        );
        
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = manager.store_bag(bagContent);
        
        // Then the bag should be stored in the second robot
        assertNotNull(ticket);
        
        // Verify by retrieving from the manager
        assertEquals(bagContent, manager.retrieve_bag(ticket));
    }
    
    @Test
    public void should_store_bag_in_own_locker_when_all_robots_are_full() {
        // Given all robots are full but manager locker has capacity
        Locker fullLocker1 = new Locker(1);
        fullLocker1.store_bag("some bag");
        PrimaryLockerRobot fullRobot1 = new PrimaryLockerRobot(new Locker[]{fullLocker1});
        
        Locker fullLocker2 = new Locker(1);
        fullLocker2.store_bag("another bag");
        SmartLockerRobot fullRobot2 = new SmartLockerRobot(new Locker[]{fullLocker2});
        
        Locker managerLocker = new Locker(1);
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{managerLocker},
            new LockerRobot[]{fullRobot1, fullRobot2}
        );
        
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = manager.store_bag(bagContent);
        
        // Then the bag should be stored in manager's own locker
        assertNotNull(ticket);
        
        // Verify by retrieving from the manager
        assertEquals(bagContent, manager.retrieve_bag(ticket));
    }
    
    @Test
    public void should_store_bag_in_first_own_locker_when_all_robots_are_full_and_multiple_manager_lockers_available() {
        // Given all robots are full but manager has multiple lockers with capacity
        Locker fullLocker = new Locker(1);
        fullLocker.store_bag("some bag");
        PrimaryLockerRobot fullRobot = new PrimaryLockerRobot(new Locker[]{fullLocker});
        
        Locker firstManagerLocker = new Locker(1);
        Locker secondManagerLocker = new Locker(1);
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{firstManagerLocker, secondManagerLocker},
            new LockerRobot[]{fullRobot}
        );
        
        String bagContent = "bag content";
        
        // When storing a bag
        String ticket = manager.store_bag(bagContent);
        
        // Then the bag should be stored in the first manager locker
        assertNotNull(ticket);
        
        // Verify the first locker is used by checking its capacity
        assertTrue(firstManagerLocker.isFull());
        assertFalse(secondManagerLocker.isFull());
        
        // Verify by retrieving from the manager
        assertEquals(bagContent, manager.retrieve_bag(ticket));
    }
    
    @Test
    public void should_throw_exception_when_all_robots_and_lockers_are_full() {
        // Given all robots and manager lockers are full
        Locker fullLocker1 = new Locker(1);
        fullLocker1.store_bag("some bag");
        PrimaryLockerRobot fullRobot = new PrimaryLockerRobot(new Locker[]{fullLocker1});
        
        Locker fullLocker2 = new Locker(1);
        fullLocker2.store_bag("another bag");
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{fullLocker2},
            new LockerRobot[]{fullRobot}
        );
        
        // When trying to store a bag, then an exception should be thrown
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> manager.store_bag("bag content")
        );
        
        assertEquals("No available capacity in any robot or locker", exception.getMessage());
    }
    
    @Test
    public void should_be_able_to_create_manager_with_only_robots_and_no_lockers() {
        // Given only robots and no lockers
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{new Locker(1)});
        
        // When creating a manager with only robots
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{},
            new LockerRobot[]{robot}
        );
        
        // Then the manager should be created successfully and able to store bags
        String bagContent = "bag content";
        String ticket = manager.store_bag(bagContent);
        
        assertNotNull(ticket);
        assertEquals(bagContent, manager.retrieve_bag(ticket));
    }
    
    @Test
    public void should_be_able_to_create_manager_with_only_lockers_and_no_robots() {
        // Given only lockers and no robots
        Locker locker = new Locker(1);
        
        // When creating a manager with only lockers
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{locker},
            new LockerRobot[]{}
        );
        
        // Then the manager should be created successfully and able to store bags
        String bagContent = "bag content";
        String ticket = manager.store_bag(bagContent);
        
        assertNotNull(ticket);
        assertEquals(bagContent, manager.retrieve_bag(ticket));
    }
    
    @Test
    public void should_retrieve_bag_from_robot_when_given_valid_ticket() {
        // Given a bag stored by a robot managed by the manager
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{new Locker(1)});
        Locker managerLocker = new Locker(1);
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{managerLocker},
            new LockerRobot[]{robot}
        );
        
        String bagContent = "bag content";
        String ticket = manager.store_bag(bagContent);
        
        // When retrieving with a valid ticket
        String retrievedContent = manager.retrieve_bag(ticket);
        
        // Then the correct bag content should be returned
        assertEquals(bagContent, retrievedContent);
    }
    
    @Test
    public void should_retrieve_bag_from_own_locker_when_given_valid_ticket() {
        // Given all robots are full and bag is stored in manager's locker
        Locker fullLocker = new Locker(1);
        fullLocker.store_bag("some bag");
        PrimaryLockerRobot fullRobot = new PrimaryLockerRobot(new Locker[]{fullLocker});
        
        Locker managerLocker = new Locker(1);
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{managerLocker},
            new LockerRobot[]{fullRobot}
        );
        
        String bagContent = "bag content";
        String ticket = manager.store_bag(bagContent);
        
        // When retrieving with a valid ticket
        String retrievedContent = manager.retrieve_bag(ticket);
        
        // Then the correct bag content should be returned
        assertEquals(bagContent, retrievedContent);
    }
    
    @Test
    public void should_throw_exception_when_retrieving_with_invalid_ticket() {
        // Given a manager with robots and lockers
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{new Locker(1)});
        Locker managerLocker = new Locker(1);
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{managerLocker},
            new LockerRobot[]{robot}
        );
        
        // When retrieving with an invalid ticket, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> manager.retrieve_bag("invalid-ticket")
        );
        
        assertEquals("Invalid ticket", exception.getMessage());
    }
}