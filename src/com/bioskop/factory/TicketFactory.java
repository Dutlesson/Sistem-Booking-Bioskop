package com.bioskop.factory;

import java.util.*;

/**
 * TicketFactory - Creator class untuk Factory Method Pattern
 * Centralized ticket creation dengan type selection
 *
 * Design Pattern: Factory Method Pattern (Creator)
 *
 * @author Nazriel (Member 1)
 * @version 1.0
 */
public class TicketFactory {

    /**
     * Factory Method - Membuat objek Ticket berdasarkan type
     *
     * @param type tipe tiket ("Regular", "VIP", "Student")
     * @param seatNumber nomor kursi
     * @param basePrice harga dasar
     * @return Ticket object (RegularTicket/VIPTicket/StudentTicket)
     * @throws IllegalArgumentException jika type tidak valid
     */
    public static Ticket createTicket(String type, String seatNumber, double basePrice) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket type cannot be null or empty");
        }

        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be null or empty");
        }

        if (basePrice <= 0) {
            throw new IllegalArgumentException("Base price must be greater than 0");
        }

        // Normalize type input (case-insensitive)
        String normalizedType = type.trim().toLowerCase();

        return switch (normalizedType) {
            case "regular" -> new RegularTicket(seatNumber, basePrice);
            case "vip" -> new VIPTicket(seatNumber, basePrice);
            case "student" -> new StudentTicket(seatNumber, basePrice);
            default -> throw new IllegalArgumentException(
                    "Invalid ticket type: " + type + ". Valid types: Regular, VIP, Student"
            );
        };
    }

    /**
     * Get list semua tipe tiket yang tersedia
     *
     * @return List of available ticket types
     */
    public static List<String> getAvailableTypes() {
        return Arrays.asList("Regular", "VIP", "Student");
    }

    /**
     * Cek apakah tipe tiket valid
     *
     * @param type tipe yang akan dicek
     * @return true jika valid
     */
    public static boolean isValidType(String type) {
        if (type == null) return false;

        String normalizedType = type.trim().toLowerCase();
        return normalizedType.equals("regular") ||
                normalizedType.equals("vip") ||
                normalizedType.equals("student");
    }

    /**
     * Get deskripsi untuk setiap tipe tiket
     *
     * @param type tipe tiket
     * @return deskripsi singkat
     */
    public static String getTypeDescription(String type) {
        if (type == null) return "Unknown type";

        String normalizedType = type.trim().toLowerCase();

        return switch (normalizedType) {
            case "regular" -> "Standard ticket with normal pricing (1.0x base price)";
            case "vip" -> "Premium ticket with extra benefits (2.0x base price)";
            case "student" -> "Discounted ticket for students (0.75x base price, 25% off)";
            default -> "Unknown ticket type";
        };
    }

    /**
     * Get price multiplier untuk setiap tipe
     *
     * @param type tipe tiket
     * @return multiplier value
     */
    public static double getTypeMultiplier(String type) {
        if (type == null) return 1.0;

        String normalizedType = type.trim().toLowerCase();

        return switch (normalizedType) {
            case "regular" -> 1.0;
            case "vip" -> 2.0;
            case "student" -> 0.75;
            default -> 1.0;
        };
    }

    /**
     * Display menu pilihan tipe tiket
     *
     * @return formatted menu string
     */
    public static String getTicketTypeMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append("╔═══════════════════════════════════════════════════╗\n");
        menu.append("║           SELECT TICKET TYPE                      ║\n");
        menu.append("╠═══════════════════════════════════════════════════╣\n");
        menu.append("║                                                   ║\n");
        menu.append("║  1. REGULAR TICKET                                ║\n");
        menu.append("║     • Standard seating                            ║\n");
        menu.append("║     • Price: 1.0x base price                      ║\n");
        menu.append("║                                                   ║\n");
        menu.append("║  2. VIP TICKET ⭐                                 ║\n");
        menu.append("║     • Premium reclining seats                     ║\n");
        menu.append("║     • Extra legroom & benefits                    ║\n");
        menu.append("║     • Price: 2.0x base price                      ║\n");
        menu.append("║                                                   ║\n");
        menu.append("║  3. STUDENT TICKET 🎓                            ║\n");
        menu.append("║     • 25% discount                                ║\n");
        menu.append("║     • Valid student ID required                   ║\n");
        menu.append("║     • Price: 0.75x base price                     ║\n");
        menu.append("║                                                   ║\n");
        menu.append("╚═══════════════════════════════════════════════════╝\n");

        return menu.toString();
    }

    /**
     * Convert menu choice ke ticket type string
     *
     * @param choice pilihan menu (1/2/3)
     * @return ticket type string
     */
    public static String choiceToType(int choice) {
        return switch (choice) {
            case 1 -> "Regular";
            case 2 -> "VIP";
            case 3 -> "Student";
            default -> null;
        };
    }

    /**
     * Helper method untuk testing factory
     * Membuat sample tickets untuk demo
     *
     * @return List of sample tickets
     */
    public static List<Ticket> createSampleTickets() {
        List<Ticket> tickets = new ArrayList<>();
        double basePrice = 50000;

        tickets.add(createTicket("Regular", "A1", basePrice));
        tickets.add(createTicket("VIP", "B5", basePrice));
        tickets.add(createTicket("Student", "C10", basePrice));

        return tickets;
    }
}