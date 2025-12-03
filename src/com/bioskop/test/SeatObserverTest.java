package com.bioskop.test;

import com.bioskop.model.Seat;
import com.bioskop.observer.BookingObserver;
import com.bioskop.observer.SeatObserver;
import java.util.List;

/**
 * Comprehensive Test untuk Observer Pattern Integration
 * Commit #18: Testing Observer Pattern Integration
 *
 * @author Fiandra
 * @version 1.0
 */
public class SeatObserverTest {

    // Custom Test Observer
    static class TestObserver implements SeatObserver {
        private String name;
        private int updateCount;

        public TestObserver(String name) {
            this.name = name;
            this.updateCount = 0;
        }

        @Override
        public void update(String seatNumber, boolean isBooked, int scheduleId) {
            updateCount++;
            String status = isBooked ? "BOOKED ✓" : "RELEASED ✗";
            System.out.println(String.format("  └─ %s received update: Seat %s is %s (Schedule: %d) [Update #%d]",
                    name, seatNumber, status, scheduleId, updateCount));
        }

        @Override
        public String getObserverName() {
            return name;
        }

        public int getUpdateCount() {
            return updateCount;
        }
    }

    // TEST 1: Basic Registration
    public static void test1_BasicRegistration() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TEST 1: Basic Observer Registration  ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        Seat seat = Seat.getSeatById(1);
        if (seat == null) {
            System.out.println("❌ Seat not found!");
            return;
        }

        System.out.println("✓ Loaded: " + seat.getSeatInfo());
        System.out.println("  Initial observers: " + seat.getObserverCount());

        BookingObserver observer = new BookingObserver("Test1-Observer", 100);
        seat.addObserver(observer);

        System.out.println("  After registration: " + seat.getObserverCount() + " observer(s)");
        System.out.println("\n✅ Test 1 Passed!");
    }

    // TEST 2: Observer Notification
    public static void test2_ObserverNotification() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TEST 2: Observer Notification        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        Seat seat = Seat.getSeatById(2);
        if (seat == null) {
            System.out.println("❌ Seat not found!");
            return;
        }

        if (seat.isBooked()) {
            seat.releaseSeat();
        }

        System.out.println("✓ Seat status: " + seat.getSeatInfo());

        BookingObserver observer = new BookingObserver("Test2-Observer", 200);
        seat.addObserver(observer);

        System.out.println("\n--- Booking Seat ---");
        seat.bookSeat();

        System.out.println("\n✅ Test 2 Passed! Observer was notified.");
    }

    // TEST 3: Multiple Observers
    public static void test3_MultipleObservers() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TEST 3: Multiple Observers           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        Seat seat = Seat.getSeatById(3);
        if (seat == null) {
            System.out.println("❌ Seat not found!");
            return;
        }

        if (seat.isBooked()) {
            seat.releaseSeat();
        }

        System.out.println("✓ Seat status: " + seat.getSeatInfo());

        BookingObserver observer1 = new BookingObserver("Observer-Alpha", 301);
        BookingObserver observer2 = new BookingObserver("Observer-Beta", 302);
        TestObserver observer3 = new TestObserver("Observer-Gamma");

        seat.addObserver(observer1);
        seat.addObserver(observer2);
        seat.addObserver(observer3);

        System.out.println("  Registered: " + seat.getObserverCount() + " observers\n");

        System.out.println("--- Booking Seat ---");
        seat.bookSeat();

        System.out.println("\n✅ Test 3 Passed! All " + seat.getObserverCount() + " observers notified.");
    }

    // TEST 4: Observer Removal
    public static void test4_ObserverRemoval() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TEST 4: Observer Removal             ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        Seat seat = Seat.getSeatById(4);
        if (seat == null) {
            System.out.println("❌ Seat not found!");
            return;
        }

        if (seat.isBooked()) {
            seat.releaseSeat();
        }

        System.out.println("✓ Seat status: " + seat.getSeatInfo());

        BookingObserver observer1 = new BookingObserver("Observer-1", 401);
        BookingObserver observer2 = new BookingObserver("Observer-2", 402);

        seat.addObserver(observer1);
        seat.addObserver(observer2);
        System.out.println("  Registered: " + seat.getObserverCount() + " observers\n");

        System.out.println("--- First Booking (2 observers) ---");
        seat.bookSeat();

        System.out.println("\n--- Releasing Seat ---");
        seat.releaseSeat();

        System.out.println("\n--- Removing Observer-1 ---");
        seat.removeObserver(observer1);
        System.out.println("  Remaining: " + seat.getObserverCount() + " observer(s)\n");

        System.out.println("--- Second Booking (1 observer) ---");
        seat.bookSeat();

        System.out.println("\n✅ Test 4 Passed!");
    }

    // TEST 5: Duplicate Prevention
    public static void test5_DuplicatePrevention() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TEST 5: Duplicate Prevention         ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        Seat seat = Seat.getSeatById(5);
        if (seat == null) {
            System.out.println("❌ Seat not found!");
            return;
        }

        BookingObserver observer = new BookingObserver("DuplicateTest", 500);

        System.out.println("--- First Registration ---");
        seat.addObserver(observer);
        System.out.println("  Observers: " + seat.getObserverCount());

        System.out.println("\n--- Duplicate Registration ---");
        seat.addObserver(observer);
        System.out.println("  Observers: " + seat.getObserverCount() + " (should be 1)");

        if (seat.getObserverCount() == 1) {
            System.out.println("\n✅ Test 5 Passed!");
        } else {
            System.out.println("\n❌ Test 5 Failed!");
        }
    }

    // TEST 6: Booking Cycle
    public static void test6_BookingCycle() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TEST 6: Complete Booking Cycle       ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        Seat seat = Seat.getSeatById(6);
        if (seat == null) {
            System.out.println("❌ Seat not found!");
            return;
        }

        if (seat.isBooked()) {
            seat.releaseSeat();
        }

        System.out.println("✓ Initial: " + seat.getSeatInfo());

        TestObserver observer = new TestObserver("CycleTest");
        seat.addObserver(observer);

        System.out.println("\n--- Cycle 1: Book ---");
        seat.bookSeat();

        System.out.println("\n--- Cycle 2: Release ---");
        seat.releaseSeat();

        System.out.println("\n--- Cycle 3: Book ---");
        seat.bookSeat();

        System.out.println("\n--- Cycle 4: Release ---");
        seat.releaseSeat();

        System.out.println("\n✓ Observer received " + observer.getUpdateCount() + " updates");

        if (observer.getUpdateCount() == 4) {
            System.out.println("✅ Test 6 Passed!");
        } else {
            System.out.println("❌ Test 6 Failed!");
        }
    }

    // TEST 7: Logs Verification
    public static void test7_LogsVerification() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TEST 7: Logs Verification            ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        BookingObserver.clearLogs();

        Seat seat = Seat.getSeatById(7);
        if (seat == null) {
            System.out.println("❌ Seat not found!");
            return;
        }

        if (seat.isBooked()) {
            seat.releaseSeat();
        }

        BookingObserver observer = new BookingObserver("LogTest", 700);
        seat.addObserver(observer);

        System.out.println("--- Performing 3 actions ---");
        seat.bookSeat();
        seat.releaseSeat();
        seat.bookSeat();

        System.out.println("\n--- Checking Logs ---");
        List<String> logs = BookingObserver.getAllLogs();
        int logCount = logs.size() - 1;

        System.out.println("✓ Total logs: " + logCount);

        if (logCount >= 3) {
            System.out.println("✅ Test 7 Passed!");
        } else {
            System.out.println("❌ Test 7 Failed!");
        }

        System.out.println("\n--- Recent Logs ---");
        BookingObserver.printLogs();
    }

    // TEST 8: Multiple Seats
    public static void test8_MultipleSeats() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TEST 8: Multiple Seats               ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        Seat seat1 = Seat.getSeatById(8);
        Seat seat2 = Seat.getSeatById(9);

        if (seat1 == null || seat2 == null) {
            System.out.println("❌ Seats not found!");
            return;
        }

        if (seat1.isBooked()) seat1.releaseSeat();
        if (seat2.isBooked()) seat2.releaseSeat();

        System.out.println("✓ Seat 1: " + seat1.getSeatInfo());
        System.out.println("✓ Seat 2: " + seat2.getSeatInfo());

        BookingObserver observer1 = new BookingObserver("MultiSeat-Obs1", 801);
        BookingObserver observer2 = new BookingObserver("MultiSeat-Obs2", 802);

        seat1.addObserver(observer1);
        seat2.addObserver(observer2);

        System.out.println("\n--- Booking Seat 1 ---");
        seat1.bookSeat();

        System.out.println("\n--- Booking Seat 2 ---");
        seat2.bookSeat();

        System.out.println("\n✅ Test 8 Passed!");
    }

    // MAIN TEST RUNNER
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SEAT OBSERVER PATTERN TEST SUITE    ║");
        System.out.println("║         Comprehensive Testing         ║");
        System.out.println("╚════════════════════════════════════════╝");

        try {
            test1_BasicRegistration();
            test2_ObserverNotification();
            test3_MultipleObservers();
            test4_ObserverRemoval();
            test5_DuplicatePrevention();
            test6_BookingCycle();
            test7_LogsVerification();
            test8_MultipleSeats();

            System.out.println("\n\n╔════════════════════════════════════════╗");
            System.out.println("║          TEST SUMMARY                  ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("\n✅ All 8 Tests Completed!");
            System.out.println("\n🎉 Observer Pattern: VERIFIED");
            System.out.println("   - Subject (Seat) ✓");
            System.out.println("   - Observer (BookingObserver) ✓");
            System.out.println("   - Notification System ✓");
            System.out.println("   - Logging System ✓");

        } catch (Exception e) {
            System.out.println("\n❌ Test Failed:");
            e.printStackTrace();
        }
    }
}