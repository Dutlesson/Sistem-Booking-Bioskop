package com.bioskop.observer;

import com.bioskop.util.FileManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Observer untuk tracking booking activities
 * Implements Observer Pattern - Concrete Observer role
 *
 * Design Pattern: Observer Pattern
 * Role: Concrete Observer
 *
 * @author Fiandra
 * @version 1.0
 */
public class BookingObserver implements SeatObserver {

    // ========== ATTRIBUTES ==========

    private String observerName;
    private int userId;
    private boolean enableLogging;

    private static final String LOG_FILE = "booking_logs.txt";

    // ========== CONSTRUCTOR ==========

    public BookingObserver(String observerName) {
        this.observerName = observerName;
        this.userId = -1;
        this.enableLogging = true;
        initializeLogFile();
    }

    public BookingObserver(String observerName, int userId) {
        this.observerName = observerName;
        this.userId = userId;
        this.enableLogging = true;
        initializeLogFile();
    }

    public BookingObserver(String observerName, int userId, boolean enableLogging) {
        this.observerName = observerName;
        this.userId = userId;
        this.enableLogging = enableLogging;
        initializeLogFile();
    }

    // ========== OBSERVER PATTERN IMPLEMENTATION ==========

    @Override
    public void update(String seatNumber, boolean isBooked, int scheduleId) {
        String action = isBooked ? "BOOKED" : "RELEASED";
        String logMessage = String.format(
                "[%s] Seat %s %s for Schedule %d",
                observerName, seatNumber, action, scheduleId
        );

        System.out.println("🔔 " + logMessage);

        if (enableLogging) {
            logToFile(seatNumber, isBooked, scheduleId);
        }

        performAdditionalActions(seatNumber, isBooked, scheduleId);
    }

    @Override
    public String getObserverName() {
        return observerName;
    }

    // ========== LOGGING METHODS ==========

    private void initializeLogFile() {
        if (!FileManager.fileExists(LOG_FILE)) {
            String header = "timestamp|observerName|userId|seatNumber|action|scheduleId";
            FileManager.appendFile(LOG_FILE, header);
            System.out.println("✓ Booking log file initialized");
        }
    }

    private void logToFile(String seatNumber, boolean isBooked, int scheduleId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            String action = isBooked ? "BOOKED" : "RELEASED";

            String logEntry = String.format("%s|%s|%d|%s|%s|%d",
                    timestamp, observerName, userId, seatNumber, action, scheduleId);

            FileManager.appendFile(LOG_FILE, logEntry);

        } catch (Exception e) {
            System.out.println("❌ Error logging to file: " + e.getMessage());
        }
    }

    protected void performAdditionalActions(String seatNumber, boolean isBooked, int scheduleId) {
        // Default: no additional actions
    }

    // ========== UTILITY METHODS ==========

    public void enableLogging() {
        this.enableLogging = true;
        System.out.println("✓ Logging enabled for " + observerName);
    }

    public void disableLogging() {
        this.enableLogging = false;
        System.out.println("✓ Logging disabled for " + observerName);
    }

    public boolean isLoggingEnabled() {
        return enableLogging;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    // ========== STATIC METHODS ==========

    public static java.util.List<String> getAllLogs() {
        return FileManager.readFile(LOG_FILE);
    }

    public static java.util.List<String> getLogsBySchedule(int scheduleId) {
        java.util.List<String> allLogs = getAllLogs();
        java.util.List<String> filteredLogs = new java.util.ArrayList<>();

        for (String log : allLogs) {
            if (log.contains("|" + scheduleId)) {
                filteredLogs.add(log);
            }
        }

        return filteredLogs;
    }

    public static void clearLogs() {
        java.util.List<String> header = java.util.Arrays.asList(
                "timestamp|observerName|userId|seatNumber|action|scheduleId"
        );
        FileManager.writeFile(LOG_FILE, header);
        System.out.println("✓ All logs cleared");
    }

    public static void printLogs() {
        java.util.List<String> logs = getAllLogs();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         BOOKING LOGS                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        if (logs.isEmpty() || logs.size() == 1) {
            System.out.println("No logs available.");
            return;
        }

        for (int i = 1; i < logs.size(); i++) {
            String[] parts = logs.get(i).split("\\|");
            if (parts.length >= 6) {
                System.out.printf("[%s] Observer: %s | User: %s | Seat: %s | Action: %s | Schedule: %s\n",
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
            }
        }

        System.out.println("\n✓ Total logs: " + (logs.size() - 1));
    }

    // ========== TEST METHOD ==========

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   TESTING BOOKINGOBSERVER CLASS        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        clearLogs();

        System.out.println("=== Test 1: Create BookingObserver ===");
        BookingObserver observer1 = new BookingObserver("TestObserver-1", 101);
        System.out.println("Observer created: " + observer1.getObserverName());
        System.out.println("User ID: " + observer1.getUserId() + "\n");

        System.out.println("=== Test 2: Simulate Booking ===");
        observer1.update("A1", true, 1);
        System.out.println();

        System.out.println("=== Test 3: Simulate Release ===");
        observer1.update("A1", false, 1);
        System.out.println();

        System.out.println("=== Test 4: Multiple Observers ===");
        BookingObserver observer2 = new BookingObserver("TestObserver-2", 102);
        observer1.update("B3", true, 1);
        observer2.update("B4", true, 1);
        System.out.println();

        System.out.println("=== Test 5: Disable Logging ===");
        observer1.disableLogging();
        observer1.update("C1", true, 2);
        System.out.println("(Should not log to file)\n");

        System.out.println("=== Test 6: Print All Logs ===");
        printLogs();
        System.out.println();

        System.out.println("=== Test 7: Filter Logs by Schedule ===");
        java.util.List<String> schedule1Logs = getLogsBySchedule(1);
        System.out.println("Logs for Schedule 1: " + (schedule1Logs.size() - 1) + " entries\n");

        System.out.println("✅ All tests completed!");
    }
}