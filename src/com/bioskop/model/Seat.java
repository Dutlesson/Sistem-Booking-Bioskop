package com.bioskop.model;

import com.bioskop.observer.SeatObserver;
import com.bioskop.util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class untuk Seat (Kursi bioskop)
 * Implements Observer Pattern sebagai Subject/Observable
 *
 * Design Pattern: Observer Pattern
 * Role: Subject (Observable)
 *
 * Seat akan notify semua registered observers ketika status booking berubah
 *
 * @author Fiandra
 * @version 1.0
 */
public class Seat {

    // ========== ATTRIBUTES ==========

    private int seatId;
    private int scheduleId;
    private String seatNumber;  // Format: A1, A2, B1, dst
    private boolean isBooked;

    // Observer Pattern: Collection of observers
    private List<SeatObserver> observers;

    // ========== CONSTRUCTOR ==========

    /**
     * Constructor untuk Seat
     *
     * @param seatId ID unik seat
     * @param scheduleId ID schedule terkait
     * @param seatNumber Nomor kursi (e.g., "A1")
     * @param isBooked Status booking
     */
    public Seat(int seatId, int scheduleId, String seatNumber, boolean isBooked) {
        this.seatId = seatId;
        this.scheduleId = scheduleId;
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
        this.observers = new ArrayList<>();
    }

    // ========== OBSERVER PATTERN METHODS ==========

    /**
     * Register observer untuk menerima notifikasi
     * Observer akan di-notify setiap kali seat status berubah
     *
     * @param observer Observer yang akan di-register
     */
    public void addObserver(SeatObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("✓ Observer registered: " + observer.getObserverName());
        } else {
            System.out.println("⚠️ Observer already registered: " + observer.getObserverName());
        }
    }

    /**
     * Remove observer dari notification list
     *
     * @param observer Observer yang akan di-remove
     */
    public void removeObserver(SeatObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("✓ Observer removed: " + observer.getObserverName());
        } else {
            System.out.println("⚠️ Observer not found: " + observer.getObserverName());
        }
    }

    /**
     * Notify semua registered observers tentang perubahan status
     * Private method - hanya dipanggil internal saat status berubah
     *
     * Ini adalah CORE dari Observer Pattern:
     * - Subject (Seat) notify semua Observers
     * - Observers akan menerima update() call
     */
    private void notifyObservers() {
        if (observers.isEmpty()) {
            System.out.println("ℹ️ No observers registered for seat " + seatNumber);
            return;
        }

        System.out.println("🔔 Notifying " + observers.size() + " observer(s)...");

        for (SeatObserver observer : observers) {
            try {
                // Call update() method pada setiap observer
                observer.update(seatNumber, isBooked, scheduleId);
            } catch (Exception e) {
                System.out.println("❌ Error notifying observer: " + observer.getObserverName());
                e.printStackTrace();
            }
        }
    }

    // ========== BUSINESS METHODS ==========

    /**
     * Book seat dan notify observers
     *
     * @return true jika berhasil, false jika sudah booked
     */
    public boolean bookSeat() {
        if (isBooked) {
            System.out.println("✗ Seat " + seatNumber + " is already booked!");
            return false;
        }

        // Change status
        this.isBooked = true;
        System.out.println("✓ Seat " + seatNumber + " booked successfully!");

        // Update file
        updateSeatInFile();

        // ⭐ OBSERVER PATTERN: Notify observers
        notifyObservers();

        return true;
    }

    /**
     * Release seat booking dan notify observers
     *
     * @return true jika berhasil, false jika memang belum booked
     */
    public boolean releaseSeat() {
        if (!isBooked) {
            System.out.println("✗ Seat " + seatNumber + " is not booked!");
            return false;
        }

        // Change status
        this.isBooked = false;
        System.out.println("✓ Seat " + seatNumber + " released!");

        // Update file
        updateSeatInFile();

        // ⭐ OBSERVER PATTERN: Notify observers
        notifyObservers();

        return true;
    }

    /**
     * Get seat information as string
     *
     * @return String representation of seat
     */
    public String getSeatInfo() {
        String status = isBooked ? "BOOKED ❌" : "AVAILABLE ✓";
        return String.format("Seat %s [Schedule: %d] - %s",
                seatNumber, scheduleId, status);
    }

    // ========== FILE OPERATIONS ==========

    /**
     * Update seat status di file seats.txt
     * Synchronize memory state dengan file storage
     */
    private void updateSeatInFile() {
        try {
            List<String> lines = FileManager.readFile("seats.txt");
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                // Skip jika header
                if (line.startsWith("seatId|")) {
                    updatedLines.add(line);
                    continue;
                }

                // Parse line
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);

                    if (id == this.seatId) {
                        // Update this seat
                        String newLine = String.format("%d|%d|%s|%s",
                                seatId, scheduleId, seatNumber, isBooked);
                        updatedLines.add(newLine);
                    } else {
                        // Keep other seats unchanged
                        updatedLines.add(line);
                    }
                }
            }

            // Write back to file
            FileManager.writeFile("seats.txt", updatedLines);

        } catch (Exception e) {
            System.out.println("❌ Error updating seat in file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load all seats dari file untuk schedule tertentu
     *
     * @param scheduleId ID schedule
     * @return List of Seat objects
     */
    public static List<Seat> loadSeats(int scheduleId) {
        List<Seat> seats = new ArrayList<>();

        try {
            List<String> lines = FileManager.readFile("seats.txt");

            // Skip header (line 0)
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split("\\|");

                if (parts.length >= 4) {
                    int seatId = Integer.parseInt(parts[0]);
                    int seatScheduleId = Integer.parseInt(parts[1]);
                    String seatNumber = parts[2];
                    boolean isBooked = Boolean.parseBoolean(parts[3]);

                    // Filter by scheduleId
                    if (seatScheduleId == scheduleId) {
                        Seat seat = new Seat(seatId, seatScheduleId, seatNumber, isBooked);
                        seats.add(seat);
                    }
                }
            }

            System.out.println("✓ Loaded " + seats.size() + " seats for Schedule " + scheduleId);

        } catch (Exception e) {
            System.out.println("❌ Error loading seats: " + e.getMessage());
            e.printStackTrace();
        }

        return seats;
    }

    /**
     * Get available seats count untuk schedule
     *
     * @param scheduleId ID schedule
     * @return Jumlah kursi available
     */
    public static int getAvailableSeatsCount(int scheduleId) {
        List<Seat> seats = loadSeats(scheduleId);
        int count = 0;

        for (Seat seat : seats) {
            if (!seat.isBooked) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get seat by ID
     *
     * @param seatId ID seat yang dicari
     * @return Seat object atau null jika tidak ditemukan
     */
    public static Seat getSeatById(int seatId) {
        try {
            List<String> lines = FileManager.readFile("seats.txt");

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split("\\|");

                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);

                    if (id == seatId) {
                        int scheduleId = Integer.parseInt(parts[1]);
                        String seatNumber = parts[2];
                        boolean isBooked = Boolean.parseBoolean(parts[3]);

                        return new Seat(id, scheduleId, seatNumber, isBooked);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error getting seat: " + e.getMessage());
        }

        return null;
    }

    public static List<Seat> getSeatsBySchedule(int scheduleId) {
        List<Seat> seats = new ArrayList<>();
        List<String> lines = FileManager.readFile("seats.txt");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                try {
                    int seatId = Integer.parseInt(parts[0]);
                    int sId = Integer.parseInt(parts[1]);
                    String seatNumber = parts[2];
                    boolean isBooked = Boolean.parseBoolean(parts[3]);

                    if (sId == scheduleId) {
                        seats.add(new Seat(seatId, sId, seatNumber, isBooked));
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid lines
                }
            }
        }

        return seats;
    }



    // ========== GETTERS ==========

    public int getSeatId() {
        return seatId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public int getObserverCount() {
        return observers.size();
    }
}