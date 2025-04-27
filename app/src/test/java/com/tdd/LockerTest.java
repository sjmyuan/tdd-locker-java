package com.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class LockerTest {

    @Test
    public void should_create_locker_successfully_when_capacity_is_valid() {
        // Given a valid capacity
        int validCapacity = 10;
        
        // When creating a locker with valid capacity
        Locker locker = new Locker(validCapacity);
        
        // Then the locker should be created successfully with the correct capacity
        assertEquals(validCapacity, locker.getCapacity());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    public void should_throw_exception_when_capacity_is_invalid(int invalidCapacity) {
        // When creating a locker with invalid capacity, then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> new Locker(invalidCapacity)
        );
        
        assertEquals("Locker capacity must be greater than 0", exception.getMessage());
    }

    @Test
    public void should_return_ticket_when_store_bag_given_locker_has_capacity() {
        // Given a locker with capacity
        Locker locker = new Locker(1);
        String bagContent = "a bag";
        
        // When storing a bag
        String ticket = locker.store_bag(bagContent);
        
        // Then a ticket should be returned and it should not be null or empty
        assertNotNull(ticket);
        assertFalse(ticket.isEmpty());
    }
    
    @Test
    public void should_return_different_tickets_when_store_different_bags() {
        // Given a locker with enough capacity
        Locker locker = new Locker(2);
        
        // When storing two different bags
        String ticket1 = locker.store_bag("bag 1");
        String ticket2 = locker.store_bag("bag 2");
        
        // Then should get different tickets
        assertNotEquals(ticket1, ticket2);
    }
    
    @Test
    public void should_throw_exception_when_store_bag_given_locker_is_full() {
        // Given a locker with capacity 1 that is already full
        Locker locker = new Locker(1);
        locker.store_bag("a bag");
        
        // When trying to store another bag, then an exception should be thrown
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> locker.store_bag("another bag")
        );
        
        assertEquals("Locker is full", exception.getMessage());
    }

    @Test
    public void should_retrieve_bag_when_given_valid_ticket() {
        // Given a locker with a stored bag
        Locker locker = new Locker(1);
        String bagContent = "a bag content";
        String validTicket = locker.store_bag(bagContent);
        
        // When retrieving bag with valid ticket
        String retrievedContent = locker.retrieve_bag(validTicket);
        
        // Then the correct bag content should be returned
        assertEquals(bagContent, retrievedContent);
    }
    
    @Test
    public void should_throw_exception_when_retrieving_with_invalid_ticket() {
        // Given a locker with a stored bag
        Locker locker = new Locker(1);
        locker.store_bag("a bag");
        
        // When retrieving with an invalid ticket
        String invalidTicket = "invalid-ticket";
        
        // Then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> locker.retrieve_bag(invalidTicket)
        );
        
        assertEquals("Invalid ticket", exception.getMessage());
    }
    
    @Test
    public void should_restore_capacity_after_retrieving_bag() {
        // Given a filled locker
        Locker locker = new Locker(1);
        String ticket = locker.store_bag("a bag");
        assertTrue(locker.isFull());
        
        // When retrieving the bag
        locker.retrieve_bag(ticket);
        
        // Then the capacity should be restored
        assertFalse(locker.isFull());
    }
    
    @Test
    public void should_not_be_able_to_use_same_ticket_twice() {
        // Given a locker from which a bag has been retrieved
        Locker locker = new Locker(1);
        String ticket = locker.store_bag("a bag");
        locker.retrieve_bag(ticket);
        
        // When trying to retrieve with the same ticket again
        // Then an exception should be thrown
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> locker.retrieve_bag(ticket)
        );
        
        assertEquals("Invalid ticket", exception.getMessage());
    }
}