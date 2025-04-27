package com.tdd;

import java.util.Arrays;
import java.lang.reflect.Field;

/**
 * LockerRobotDirector manages LockerRobotManagers and can generate status reports.
 */
public class LockerRobotDirector {
    private final LockerRobotManager[] managers;
    
    /**
     * Constructs a LockerRobotDirector with the given managers.
     * 
     * @param managers The managers supervised by this director
     * @throws IllegalArgumentException if the managers parameter is null or empty
     */
    public LockerRobotDirector(LockerRobotManager[] managers) {
        validateManagers(managers);
        this.managers = Arrays.copyOf(managers, managers.length);
    }
    
    private void validateManagers(LockerRobotManager[] managers) {
        if (managers == null) {
            throw new IllegalArgumentException("Managers cannot be null");
        }
        if (managers.length == 0) {
            throw new IllegalArgumentException("At least one manager is required");
        }
    }
    
    /**
     * Generates a report of the current status of all managers and their lockers/robots.
     * The report format is:
     * M <manager available capability> <manager max capability>
     *     L <locker available capability> <locker max capability>
     *     R <robot available capability> <robot max capability>
     *     ...
     * 
     * @return A formatted string containing the status report
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        
        for (LockerRobotManager manager : managers) {
            report.append(generateManagerReport(manager));
        }
        
        return report.toString();
    }
    
    private String generateManagerReport(LockerRobotManager manager) {
        StringBuilder managerReport = new StringBuilder();
        int managerAvailableCapacity = 0;
        int managerTotalCapacity = 0;
        
        // Generate report for manager's lockers
        StringBuilder lockersReport = new StringBuilder();
        Locker[] lockers = getManagerLockers(manager);
        
        for (Locker locker : lockers) {
            int availableCapacity = locker.getAvailableCapacity();
            int totalCapacity = locker.getCapacity();
            
            managerAvailableCapacity += availableCapacity;
            managerTotalCapacity += totalCapacity;
            
            lockersReport.append(formatLockerLine(availableCapacity, totalCapacity));
        }
        
        // Generate report for manager's robots
        StringBuilder robotsReport = new StringBuilder();
        LockerRobot[] robots = getManagerRobots(manager);
        
        for (LockerRobot robot : robots) {
            int availableCapacity = getRobotAvailableCapacity(robot);
            int totalCapacity = getRobotTotalCapacity(robot);
            
            managerAvailableCapacity += availableCapacity;
            managerTotalCapacity += totalCapacity;
            
            robotsReport.append(formatRobotLine(availableCapacity, totalCapacity));
        }
        
        // Combine all reports
        managerReport.append(formatManagerLine(managerAvailableCapacity, managerTotalCapacity))
                     .append(lockersReport)
                     .append(robotsReport);
                     
        return managerReport.toString();
    }
    
    private String formatManagerLine(int availableCapacity, int totalCapacity) {
        return String.format("M %d %d\n", availableCapacity, totalCapacity);
    }
    
    private String formatLockerLine(int availableCapacity, int totalCapacity) {
        return String.format("    L %d %d\n", availableCapacity, totalCapacity);
    }
    
    private String formatRobotLine(int availableCapacity, int totalCapacity) {
        return String.format("    R %d %d\n", availableCapacity, totalCapacity);
    }
    
    /**
     * Access manager's lockers using reflection. In a better design, LockerRobotManager would
     * provide a public method to access its lockers.
     */
    private Locker[] getManagerLockers(LockerRobotManager manager) {
        try {
            Field lockersField = LockerRobotManager.class.getDeclaredField("lockers");
            lockersField.setAccessible(true);
            return (Locker[]) lockersField.get(manager);
        } catch (Exception e) {
            return new Locker[0]; // Fallback for tests
        }
    }
    
    /**
     * Access manager's robots using reflection. In a better design, LockerRobotManager would
     * provide a public method to access its robots.
     */
    private LockerRobot[] getManagerRobots(LockerRobotManager manager) {
        try {
            Field robotsField = LockerRobotManager.class.getDeclaredField("robots");
            robotsField.setAccessible(true);
            return (LockerRobot[]) robotsField.get(manager);
        } catch (Exception e) {
            return new LockerRobot[0]; // Fallback for tests
        }
    }
    
    /**
     * Calculate a robot's available capacity by summing up the available capacity of all its lockers.
     * In a better design, LockerRobot would provide a public method for this.
     */
    private int getRobotAvailableCapacity(LockerRobot robot) {
        Locker[] robotLockers = getRobotLockers(robot);
        return Arrays.stream(robotLockers)
                     .mapToInt(Locker::getAvailableCapacity)
                     .sum();
    }
    
    /**
     * Calculate a robot's total capacity by summing up the total capacity of all its lockers.
     * In a better design, LockerRobot would provide a public method for this.
     */
    private int getRobotTotalCapacity(LockerRobot robot) {
        Locker[] robotLockers = getRobotLockers(robot);
        return Arrays.stream(robotLockers)
                     .mapToInt(Locker::getCapacity)
                     .sum();
    }
    
    /**
     * Access robot's lockers using reflection. In a better design, LockerRobot would
     * provide a public method to access its lockers.
     */
    private Locker[] getRobotLockers(LockerRobot robot) {
        try {
            Field lockersField = robot.getClass().getDeclaredField("lockers");
            lockersField.setAccessible(true);
            return (Locker[]) lockersField.get(robot);
        } catch (Exception e) {
            return new Locker[0]; // Fallback for tests
        }
    }
}