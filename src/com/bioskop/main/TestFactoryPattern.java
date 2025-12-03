package com.bioskop.main;

import com.bioskop.model.Booking;
import com.bioskop.util.FileManager;

/**
 * TestBookingDemo - Simple demo untuk test Booking class
 *
 * @author Nazriel (Member 1)
 */
public class TestFactoryPattern {

    public static void main(String[] args) {
        FileManager.ensureDataFolderExists();

        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║        BOOKING SYSTEM WITH FACTORY DEMO               ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");

        // Create booking
        int userId = 2; // customer1
        int scheduleId = 999; // dummy schedule

        Booking booking = new Booking(userId, scheduleId);

        System.out.println("📝 Booking Created!");
        System.out.println("   Booking ID: " + booking.getBookingId());
        System.out.println("   User ID: " + booking.getUserId());
        System.out.println("   Schedule ID: " + booking.getScheduleId());
        System.out.println();

        // Add tickets using Factory Method Pattern!
        System.out.println("🎫 Adding Tickets (Using Factory Method Pattern):");
        System.out.println("─────────────────────────────────────────────────");

        double basePrice = 50000;

        booking.addTicket("Regular", "A1", basePrice);
        booking.addTicket("VIP", "B5", basePrice);
        booking.addTicket("Student", "C10", basePrice);

        System.out.println("─────────────────────────────────────────────────");
        System.out.println();

        // Show receipt
        System.out.println(booking.getReceipt());

        System.out.println("✓ Demo completed!");
        System.out.println("\nNote: Booking not saved to file (this is demo only)");
    }
}