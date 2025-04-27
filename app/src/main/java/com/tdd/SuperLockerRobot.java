package com.tdd;

import java.util.Arrays;

public class SuperLockerRobot extends LockerRobot {
    private final Locker[] lockers;
    
    public SuperLockerRobot(Locker[] lockers) {
        validateLockers(lockers);
        this.lockers = Arrays.copyOf(lockers, lockers.length);
    }
    
    private void validateLockers(Locker[] lockers) {
        if (lockers == null || lockers.length == 0) {
            throw new IllegalArgumentException("At least one locker is required");
        }
    }
    
    public String store_bag(String content) {
        Locker lockerWithHighestVacancyRate = findLockerWithHighestVacancyRate();
        if (lockerWithHighestVacancyRate == null) {
            throw new IllegalStateException("All lockers are full");
        }
        return lockerWithHighestVacancyRate.store_bag(content);
    }
    
    private Locker findLockerWithHighestVacancyRate() {
        Locker selectedLocker = null;
        double highestVacancyRate = 0.0;
        
        for (Locker locker : lockers) {
            if (!locker.isFull()) {
                double vacancyRate = calculateVacancyRate(locker);
                
                // Select first locker or a locker with higher vacancy rate
                if (selectedLocker == null || vacancyRate > highestVacancyRate) {
                    selectedLocker = locker;
                    highestVacancyRate = vacancyRate;
                }
            }
        }
        
        return selectedLocker;
    }
    
    private double calculateVacancyRate(Locker locker) {
        return (double) locker.getAvailableCapacity() / locker.getCapacity();
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