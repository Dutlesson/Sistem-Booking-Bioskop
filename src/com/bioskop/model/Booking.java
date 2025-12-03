package com.bioskop.model;

import com.bioskop.factory.*;
import com.bioskop.util.FileManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Booking - Model class untuk transaksi booking tiket
 * Integrates dengan TicketFactory untuk membuat tickets
 *
 * @author Nazriel (Member 1)
 * @version 1.0
 */
public class Booking {

    private int bookingId;
    private int userId;
    private int scheduleId;
    private String bookingDate;
    private List<Ticket> tickets;
    private double totalPrice;
    private String status; // "confirmed", "cancelled"

    private static final String BOOKINGS_FILE = "bookings.txt";
    private static final String TICKETS_FILE = "tickets.txt";

    /**
     * Constructor
     */
    public Booking(int bookingId, int userId, int scheduleId, String bookingDate) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.bookingDate = bookingDate;
        this.tickets = new ArrayList<>();
        this.totalPrice = 0.0;
        this.status = "confirmed";
    }

    /**
     * Constructor untuk create booking baru (auto-generate ID dan date)
     */
    public Booking(int userId, int scheduleId) {
        this.bookingId = FileManager.getNextId(BOOKINGS_FILE);
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.bookingDate = getCurrentDateTime();
        this.tickets = new ArrayList<>();
        this.totalPrice = 0.0;
        this.status = "confirmed";
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Add ticket menggunakan TicketFactory (Factory Method Pattern!)
     *
     * @param type tipe tiket (Regular/VIP/Student)
     * @param seatNumber nomor kursi
     * @param basePrice harga dasar
     */
    public void addTicket(String type, String seatNumber, double basePrice) {
        try {
            // Menggunakan Factory untuk create ticket
            Ticket ticket = TicketFactory.createTicket(type, seatNumber, basePrice);
            tickets.add(ticket);

            // Update total price
            calculateTotal();

            System.out.println("✓ Ticket added: " + ticket.getTicketType() +
                    " - Seat " + seatNumber);
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding ticket: " + e.getMessage());
        }
    }

    /**
     * Remove ticket berdasarkan seat number
     *
     * @param seatNumber nomor kursi yang akan dihapus
     * @return true jika berhasil remove
     */
    public boolean removeTicket(String seatNumber) {
        boolean removed = tickets.removeIf(ticket ->
                ticket.getSeatNumber().equals(seatNumber)
        );

        if (removed) {
            calculateTotal();
            System.out.println("✓ Ticket removed: Seat " + seatNumber);
        }

        return removed;
    }

    /**
     * Calculate total price dari semua tickets
     */
    public void calculateTotal() {
        totalPrice = 0.0;

        for (Ticket ticket : tickets) {
            totalPrice += ticket.calculatePrice();
        }
    }

    /**
     * Get receipt string untuk print
     *
     * @return formatted receipt
     */
    public String getReceipt() {
        StringBuilder receipt = new StringBuilder();

        receipt.append("\n");
        receipt.append("╔════════════════════════════════════════════════════╗\n");
        receipt.append("║              BOOKING CONFIRMATION                  ║\n");
        receipt.append("╠════════════════════════════════════════════════════╣\n");
        receipt.append(String.format("║ Booking ID    : %-35d║\n", bookingId));
        receipt.append(String.format("║ User ID       : %-35d║\n", userId));
        receipt.append(String.format("║ Schedule ID   : %-35d║\n", scheduleId));
        receipt.append(String.format("║ Booking Date  : %-35s║\n", bookingDate));
        receipt.append(String.format("║ Status        : %-35s║\n", status.toUpperCase()));
        receipt.append("╠════════════════════════════════════════════════════╣\n");
        receipt.append("║ TICKETS:                                           ║\n");
        receipt.append("╠════════════════════════════════════════════════════╣\n");

        int ticketNum = 1;
        for (Ticket ticket : tickets) {
            receipt.append(String.format("║ %d. %-47s║\n",
                    ticketNum++, ticket.toString()));
        }

        receipt.append("╠════════════════════════════════════════════════════╣\n");
        receipt.append(String.format("║ TOTAL PRICE   : %-35s║\n",
                formatPrice(totalPrice)));
        receipt.append("╚════════════════════════════════════════════════════╝\n");

        return receipt.toString();
    }

    /**
     * Save booking ke file
     *
     * @return true jika berhasil save
     */
    public boolean saveBooking() {
        try {
            // Save booking info
            String bookingLine = String.format("%d|%d|%d|%s|%.2f|%s",
                    bookingId, userId, scheduleId, bookingDate, totalPrice, status);
            FileManager.appendFile(BOOKINGS_FILE, bookingLine);

            // Save tickets
            for (Ticket ticket : tickets) {
                String ticketLine = String.format("%d|%d|%s|%s|%.2f|%.2f",
                        FileManager.getNextId(TICKETS_FILE),
                        bookingId,
                        ticket.getTicketType(),
                        ticket.getSeatNumber(),
                        ticket.getBasePrice(),
                        ticket.calculatePrice());
                FileManager.appendFile(TICKETS_FILE, ticketLine);
            }

            System.out.println("✓ Booking saved successfully! ID: " + bookingId);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get booking history untuk user tertentu
     *
     * @param userId ID user
     * @return List of bookings
     */
    public static List<Booking> getBookingHistory(int userId) {
        List<Booking> userBookings = new ArrayList<>();
        List<String> lines = FileManager.readFile(BOOKINGS_FILE);

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");
            if (parts.length < 6) continue;

            try {
                int bookingId = Integer.parseInt(parts[0]);
                int bookingUserId = Integer.parseInt(parts[1]);

                if (bookingUserId == userId) {
                    int scheduleId = Integer.parseInt(parts[2]);
                    String bookingDate = parts[3];

                    Booking booking = new Booking(bookingId, bookingUserId,
                            scheduleId, bookingDate);
                    booking.totalPrice = Double.parseDouble(parts[4]);
                    booking.status = parts[5];

                    // Load tickets untuk booking ini
                    booking.loadTickets();

                    userBookings.add(booking);
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }

        return userBookings;
    }

    /**
     * Load tickets untuk booking ini dari file
     */
    private void loadTickets() {
        List<String> lines = FileManager.readFile(TICKETS_FILE);

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");
            if (parts.length < 6) continue;

            try {
                int ticketBookingId = Integer.parseInt(parts[1]);

                if (ticketBookingId == this.bookingId) {
                    String type = parts[2];
                    String seatNumber = parts[3];
                    double basePrice = Double.parseDouble(parts[4]);

                    // Recreate ticket menggunakan Factory
                    Ticket ticket = TicketFactory.createTicket(type, seatNumber, basePrice);
                    this.tickets.add(ticket);
                }
            } catch (IllegalArgumentException e) {
                // Catches both NumberFormatException and TicketFactory exceptions
                continue;
            }
        }
    }

    /**
     * Get all bookings (untuk admin)
     *
     * @return List of all bookings
     */
    public static List<Booking> getAllBookings() {
        List<Booking> allBookings = new ArrayList<>();
        List<String> lines = FileManager.readFile(BOOKINGS_FILE);

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");
            if (parts.length < 6) continue;

            try {
                int bookingId = Integer.parseInt(parts[0]);
                int userId = Integer.parseInt(parts[1]);
                int scheduleId = Integer.parseInt(parts[2]);
                String bookingDate = parts[3];

                Booking booking = new Booking(bookingId, userId, scheduleId, bookingDate);
                booking.totalPrice = Double.parseDouble(parts[4]);
                booking.status = parts[5];
                booking.loadTickets();

                allBookings.add(booking);
            } catch (NumberFormatException e) {
                continue;
            }
        }

        return allBookings;
    }

    /**
     * Cancel booking
     *
     * @return true jika berhasil cancel
     */
    public boolean cancelBooking() {
        this.status = "cancelled";
        return updateBookingStatus();
    }

    /**
     * Update booking status di file
     *
     * @return true jika berhasil update
     */
    private boolean updateBookingStatus() {
        List<String> lines = FileManager.readFile(BOOKINGS_FILE);
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                updatedLines.add(line);
                continue;
            }

            String[] parts = line.split("\\|");
            if (parts.length < 6) {
                updatedLines.add(line);
                continue;
            }

            try {
                int lineBookingId = Integer.parseInt(parts[0]);

                if (lineBookingId == this.bookingId) {
                    String newLine = String.format("%d|%d|%d|%s|%.2f|%s",
                            bookingId, userId, scheduleId, bookingDate, totalPrice, status);
                    updatedLines.add(newLine);
                } else {
                    updatedLines.add(line);
                }
            } catch (NumberFormatException e) {
                updatedLines.add(line);
            }
        }

        FileManager.writeFile(BOOKINGS_FILE, updatedLines);
        return true;
    }

    /**
     * Get current date time formatted
     *
     * @return formatted date time string
     */
    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    /**
     * Format price untuk display
     *
     * @param price harga
     * @return formatted price
     */
    private String formatPrice(double price) {
        return String.format("Rp %,.0f", price);
    }

    /**
     * Get booking summary
     *
     * @return summary string
     */
    public String getSummary() {
        return String.format("Booking #%d - %d ticket(s) - %s - %s",
                bookingId, tickets.size(), formatPrice(totalPrice), status.toUpperCase());
    }

    @Override
    public String toString() {
        return getSummary();
    }
}