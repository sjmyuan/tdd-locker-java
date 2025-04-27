package com.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LockerRobotDirectorTest {

    @Test
    public void should_throw_exception_when_initializing_with_null_managers() {
        // When creating a director with null managers, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new LockerRobotDirector(null)
        );
        
        assertEquals("Managers cannot be null", exception.getMessage());
    }
    
    @Test
    public void should_throw_exception_when_initializing_with_empty_managers_array() {
        // When creating a director with empty managers array, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new LockerRobotDirector(new LockerRobotManager[0])
        );
        
        assertEquals("At least one manager is required", exception.getMessage());
    }
    
    @Test
    public void should_generate_report_for_single_manager_with_one_locker_and_no_robots() {
        // Given a director with a single manager that has one locker and no robots
        Locker locker = new Locker(5); // 5 capacity
        locker.store_bag("bag"); // Use 1 space, 4 left
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{locker},
            new LockerRobot[]{}
        );
        
        LockerRobotDirector director = new LockerRobotDirector(new LockerRobotManager[]{manager});
        
        // When generating a report
        String report = director.generateReport();
        
        // Then the report should show the manager and locker status
        String expectedReport = "M 4 5\n" +
                                "    L 4 5\n";
        assertEquals(expectedReport, report);
    }
    
    @Test
    public void should_generate_report_for_single_manager_with_no_lockers_and_one_robot() {
        // Given a director with a single manager that has no lockers and one robot
        Locker robotLocker = new Locker(10); // 10 capacity
        robotLocker.store_bag("bag1"); // Use 1 space, 9 left
        robotLocker.store_bag("bag2"); // Use 1 space, 8 left
        
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{robotLocker});
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{},
            new LockerRobot[]{robot}
        );
        
        LockerRobotDirector director = new LockerRobotDirector(new LockerRobotManager[]{manager});
        
        // When generating a report
        String report = director.generateReport();
        
        // Then the report should show the manager and robot status
        String expectedReport = "M 8 10\n" +
                                "    R 8 10\n";
        assertEquals(expectedReport, report);
    }
    
    @Test
    public void should_generate_report_for_single_manager_with_multiple_lockers_and_robots() {
        // Given a director with a single manager that has multiple lockers and robots
        Locker managerLocker1 = new Locker(5); // 5 capacity
        managerLocker1.store_bag("bag"); // Use 1 space, 4 left
        
        Locker managerLocker2 = new Locker(3); // 3 capacity
        managerLocker2.store_bag("bag"); // Use 1 space, 2 left
        
        Locker robot1Locker = new Locker(10); // 10 capacity
        robot1Locker.store_bag("bag1"); // Use 1 space, 9 left
        robot1Locker.store_bag("bag2"); // Use 1 space, 8 left
        
        PrimaryLockerRobot robot1 = new PrimaryLockerRobot(new Locker[]{robot1Locker});
        
        Locker robot2Locker = new Locker(8); // 8 capacity
        robot2Locker.store_bag("bag1"); // Use 1 space, 7 left
        
        SmartLockerRobot robot2 = new SmartLockerRobot(new Locker[]{robot2Locker});
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{managerLocker1, managerLocker2},
            new LockerRobot[]{robot1, robot2}
        );
        
        LockerRobotDirector director = new LockerRobotDirector(new LockerRobotManager[]{manager});
        
        // When generating a report
        String report = director.generateReport();
        
        // Then the report should show the manager, lockers, and robots status
        String expectedReport = "M 21 26\n" +
                                "    L 4 5\n" +
                                "    L 2 3\n" +
                                "    R 8 10\n" +
                                "    R 7 8\n";
        assertEquals(expectedReport, report);
    }
    
    @Test
    public void should_generate_report_for_multiple_managers() {
        // Given a director with multiple managers
        // First manager
        Locker manager1Locker = new Locker(5); // 5 capacity
        manager1Locker.store_bag("bag"); // Use 1 space, 4 left
        
        Locker robot1Locker = new Locker(10); // 10 capacity
        robot1Locker.store_bag("bag1"); // Use 1 space, 9 left
        
        PrimaryLockerRobot robot1 = new PrimaryLockerRobot(new Locker[]{robot1Locker});
        
        LockerRobotManager manager1 = new LockerRobotManager(
            new Locker[]{manager1Locker},
            new LockerRobot[]{robot1}
        );
        
        // Second manager
        Locker manager2Locker = new Locker(7); // 7 capacity
        manager2Locker.store_bag("bag1"); // Use 1 space, 6 left
        manager2Locker.store_bag("bag2"); // Use 1 space, 5 left
        
        Locker robot2Locker = new Locker(6); // 6 capacity
        robot2Locker.store_bag("bag1"); // Use 1 space, 5 left
        
        SmartLockerRobot robot2 = new SmartLockerRobot(new Locker[]{robot2Locker});
        
        LockerRobotManager manager2 = new LockerRobotManager(
            new Locker[]{manager2Locker},
            new LockerRobot[]{robot2}
        );
        
        LockerRobotDirector director = new LockerRobotDirector(
            new LockerRobotManager[]{manager1, manager2}
        );
        
        // When generating a report
        String report = director.generateReport();
        
        // Then the report should show both managers' status
        String expectedReport = "M 13 15\n" +
                                "    L 4 5\n" +
                                "    R 9 10\n" +
                                "M 10 13\n" +
                                "    L 5 7\n" +
                                "    R 5 6\n";
        assertEquals(expectedReport, report);
    }
    
    @Test
    public void should_calculate_manager_total_capacity_correctly_with_both_lockers_and_robots() {
        // Given a director with manager having both direct lockers and robots with their lockers
        Locker managerLocker = new Locker(5); // 5 capacity
        
        Locker robotLocker = new Locker(10); // 10 capacity
        PrimaryLockerRobot robot = new PrimaryLockerRobot(new Locker[]{robotLocker});
        
        LockerRobotManager manager = new LockerRobotManager(
            new Locker[]{managerLocker},
            new LockerRobot[]{robot}
        );
        
        LockerRobotDirector director = new LockerRobotDirector(new LockerRobotManager[]{manager});
        
        // When generating a report
        String report = director.generateReport();
        
        // Then the report should show the correct total capacity (5 + 10 = 15)
        String expectedReport = "M 15 15\n" +
                                "    L 5 5\n" +
                                "    R 10 10\n";
        assertEquals(expectedReport, report);
    }
}