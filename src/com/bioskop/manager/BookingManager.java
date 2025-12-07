package com.bioskop.manager;

import com.bioskop.model.Booking;
import com.bioskop.model.Seat;
import com.bioskop.observer.BookingObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class untuk handle booking operations
 * Simplified version - langsung menggunakan Booking model
 *
 * @author Fiandra
 * @version 1.0
 */
public class BookingManager {

    // List of active observers
    private List<BookingObserver> globalObservers;

    // ========== CONSTRUCTOR ==========

    public BookingManager() {
        this.globalObservers = new ArrayList<>();
        System.out.println("✓ BookingManager initialized");
    }

    // ========== OBSERVER MANAGEMENT ==========

    /**
     * Register global observer yang akan di-attach ke semua seats
     *
     * @param observer Observer yang akan di-register
     */
    public void registerGlobalObserver(BookingObserver observer) {
        if (!globalObservers.contains(observer)) {
            globalObservers.add(observer);
            System.out.println("✓ Global observer registered: " + observer.getObserverName());
        } else {
            System.out.println("⚠️ Observer already registered: " + observer.getObserverName());
        }
    }

    /**
     * Remove global observer
     *
     * @param observer Observer yang akan di-remove
     */
    public void removeGlobalObserver(BookingObserver observer) {
        if (globalObservers.remove(observer)) {
            System.out.println("✓ Global observer removed: " + observer.getObserverName());
        } else {
            System.out.println("⚠️ Observer not found: " + observer.getObserverName());
        }
    }

    /**
     * Attach all global observers to a seat
     *
     * @param seat Seat yang akan di-attach observers
     */
    public void attachObserversToSeat(Seat seat) {
        for (BookingObserver observer : globalObservers) {
            seat.addObserver(observer);
        }
    }

    // ========== UTILITY METHODS ==========

    /**
     * Get available seats for a schedule
     *
     * @param scheduleId ID schedule
     * @return List of available seats
     */
    public List<Seat> getAvailableSeats(int scheduleId) {
        List<Seat> allSeats = Seat.loadSeats(scheduleId);
        List<Seat> availableSeats = new ArrayList<>();

        for (Seat seat : allSeats) {
            if (!seat.isBooked()) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }

    /**
     * Display available seats untuk schedule
     *
     * @param scheduleId ID schedule
     */
    public void displayAvailableSeats(int scheduleId) {
        List<Seat> availableSeats = getAvailableSeats(scheduleId);

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    AVAILABLE SEATS - Schedule " + scheduleId + "       ║");
        System.out.println("╚════════════════════════════════════════╝");

        if (availableSeats.isEmpty()) {
            System.out.println("No available seats for this schedule.");
        } else {
            System.out.println("Total available: " + availableSeats.size() + " seats\n");
            for (Seat seat : availableSeats) {
                System.out.println("  • " + seat.getSeatInfo());
            }
        }
        System.out.println();
    }

    /**
     * Get statistics
     */
    public void displayStatistics() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      BOOKING STATISTICS                ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Total Bookings: " + Booking.getAllBookings().size());
        System.out.println("Global Observers: " + globalObservers.size());
        System.out.println();
    }

    // ========== GETTERS ==========

    public int getGlobalObserverCount() {
        return globalObservers.size();
    }
}