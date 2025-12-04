package com.bioskop.main;

import com.bioskop.model.User;
import com.bioskop.model.Movie;
import com.bioskop.model.Schedule;
import com.bioskop.model.Booking;
import com.bioskop.model.Seat;
import com.bioskop.util.FileManager;
import com.bioskop.manager.BookingManager;
import com.bioskop.observer.BookingObserver;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * MainApp - Console application entry point with full CLI implementation
 *
 * @author Nazriel (Member 1) - Skeleton
 * @author Fiandra (Member 3) - Full Implementation & Observer Pattern Integration
 * @version 3.0
 */
public class MainApp {

    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static BookingManager bookingManager = new BookingManager();

    public static void main(String[] args) {
        // Ensure data folder exists
        FileManager.ensureDataFolderExists();

        // Initialize global observer
        BookingObserver globalObserver = new BookingObserver("System-Observer", 0);
        bookingManager.registerGlobalObserver(globalObserver);

        displayWelcome();

        // Main application loop
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                // Show login menu
                running = showLoginMenu();
            } else {
                // Show main menu based on role
                if (currentUser.isAdmin()) {
                    showAdminMenu();
                } else {
                    showCustomerMenu();
                }
            }
        }

        System.out.println("\nTerima kasih telah menggunakan Sistem Booking Bioskop!");
        scanner.close();
    }

    /**
     * Display welcome screen dengan ASCII art
     */
    private static void displayWelcome() {
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                           ║");
        System.out.println("║     ██████╗ ██╗ ██████╗ ███████╗██╗  ██╗ ██████╗ ██████╗ ║");
        System.out.println("║     ██╔══██╗██║██╔═══██╗██╔════╝██║ ██╔╝██╔═══██╗██╔══██╗║");
        System.out.println("║     ██████╔╝██║██║   ██║███████╗█████╔╝ ██║   ██║██████╔╝║");
        System.out.println("║     ██╔══██╗██║██║   ██║╚════██║██╔═██╗ ██║   ██║██╔═══╝ ║");
        System.out.println("║     ██████╔╝██║╚██████╔╝███████║██║  ██╗╚██████╔╝██║     ║");
        System.out.println("║     ╚═════╝ ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝     ║");
        System.out.println("║                                                           ║");
        System.out.println("║            🎬 CINEMA BOOKING SYSTEM 🎬                    ║");
        System.out.println("║                                                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Show login menu
     *
     * @return true jika aplikasi masih running
     */
    private static boolean showLoginMenu() {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("║         MAIN MENU              ║");
        System.out.println("╠════════════════════════════════╣");
        System.out.println("║ 1. Login                       ║");
        System.out.println("║ 2. Register                    ║");
        System.out.println("║ 3. Exit                        ║");
        System.out.println("╚════════════════════════════════╝");
        System.out.print("Pilih menu: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    return false; // Exit application
                default:
                    System.out.println("❌ Pilihan tidak valid!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Input harus berupa angka!");
        }

        return true;
    }

    /**
     * Handle login process
     */
    private static void handleLogin() {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("║           LOGIN                ║");
        System.out.println("╚════════════════════════════════╝");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        currentUser = User.login(username, password);

        if (currentUser != null) {
            System.out.println("\n✓ Login berhasil!");
            System.out.println("Selamat datang, " + currentUser.getName() + "!");
            System.out.println("Role: " + currentUser.getRole().toUpperCase());
        } else {
            System.out.println("\n❌ Login gagal! Username atau password salah.");
        }
    }

    /**
     * Handle register process
     */
    private static void handleRegister() {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("║         REGISTER               ║");
        System.out.println("╚════════════════════════════════╝");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password (min 6 karakter): ");
        String password = scanner.nextLine().trim();

        System.out.print("Nama Lengkap: ");
        String name = scanner.nextLine().trim();

        boolean success = User.register(username, password, name);

        if (success) {
            System.out.println("\n✓ Registrasi berhasil! Silakan login.");
        }
    }

    /**
     * Show Admin Menu
     */
    private static void showAdminMenu() {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("║       ADMIN MENU               ║");
        System.out.println("╠════════════════════════════════╣");
        System.out.println("║ 1. Lihat Semua Film            ║");
        System.out.println("║ 2. Lihat Semua Jadwal          ║");
        System.out.println("║ 3. Lihat Semua Booking         ║");
        System.out.println("║ 4. Test Strategy Pattern       ║");
        System.out.println("║ 5. Lihat Booking Logs          ║");
        System.out.println("║ 6. Logout                      ║");
        System.out.println("╚════════════════════════════════╝");
        System.out.print("Pilih menu: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    viewAllMovies();
                    break;
                case 2:
                    viewAllSchedules();
                    break;
                case 3:
                    viewAllBookings();
                    break;
                case 4:
                    testStrategyPattern();
                    break;
                case 5:
                    viewBookingLogs();
                    break;
                case 6:
                    currentUser = null;
                    System.out.println("\n✓ Logout berhasil!");
                    break;
                default:
                    System.out.println("❌ Pilihan tidak valid!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Input harus berupa angka!");
        }
    }

    /**
     * Show Customer Menu
     */
    private static void showCustomerMenu() {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("║      CUSTOMER MENU             ║");
        System.out.println("╠════════════════════════════════╣");
        System.out.println("║ 1. Lihat Film                  ║");
        System.out.println("║ 2. Booking Tiket (CLI)         ║");
        System.out.println("║ 3. Riwayat Booking Saya        ║");
        System.out.println("║ 4. Logout                      ║");
        System.out.println("╚════════════════════════════════╝");
        System.out.print("Pilih menu: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    viewAllMovies();
                    break;
                case 2:
                    createBookingCLI();
                    break;
                case 3:
                    viewMyBookings();
                    break;
                case 4:
                    currentUser = null;
                    System.out.println("\n✓ Logout berhasil!");
                    break;
                default:
                    System.out.println("❌ Pilihan tidak valid!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Input harus berupa angka!");
        }
    }

    /**
     * View all movies
     */
    private static void viewAllMovies() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                   DAFTAR FILM                          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        List<Movie> movies = Movie.getAllMovies();

        if (movies.isEmpty()) {
            System.out.println("Tidak ada film tersedia.");
            return;
        }

        for (Movie movie : movies) {
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│ ID: " + movie.getMovieId());
            System.out.println("│ Judul: " + movie.getTitle());
            System.out.println("│ Genre: " + movie.getGenre());
            System.out.println("│ Harga Dasar: Rp " + String.format("%,.0f", movie.getBasePrice()));
            System.out.println("└─────────────────────────────────────────┘");
        }
    }

    /**
     * View all schedules
     */
    private static void viewAllSchedules() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                  DAFTAR JADWAL                         ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        List<Schedule> schedules = Schedule.getAllSchedules();

        if (schedules.isEmpty()) {
            System.out.println("Tidak ada jadwal tersedia.");
            return;
        }

        for (Schedule schedule : schedules) {
            Movie movie = Movie.getMovieById(schedule.getMovieId());
            String movieTitle = (movie != null) ? movie.getTitle() : "Unknown";

            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│ Schedule ID: " + schedule.getScheduleId());
            System.out.println("│ Film: " + movieTitle);
            System.out.println("│ " + schedule.getInfo());
            System.out.println("│ Final Price: Rp " + String.format("%,.0f", schedule.calculateFinalPrice()));
            System.out.println("└─────────────────────────────────────────┘");
        }
    }

    /**
     * View all bookings (Admin only)
     */
    private static void viewAllBookings() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                 SEMUA BOOKING                          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        List<Booking> bookings = Booking.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("Belum ada booking.");
            return;
        }

        for (Booking booking : bookings) {
            System.out.println("\n" + booking.getReceipt());
        }
    }

    /**
     * View my bookings (Customer)
     */
    private static void viewMyBookings() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║               RIWAYAT BOOKING SAYA                     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        List<Booking> userBookings = Booking.getBookingHistory(currentUser.getUserId());

        if (userBookings.isEmpty()) {
            System.out.println("Anda belum memiliki booking.");
            return;
        }

        for (Booking booking : userBookings) {
            System.out.println("\n" + booking.getReceipt());
        }
    }

    /**
     * View booking logs (Admin only)
     */
    private static void viewBookingLogs() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              BOOKING ACTIVITY LOGS                     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        BookingObserver.printLogs();
    }

    /**
     * ⭐ CREATE BOOKING - FULL CLI IMPLEMENTATION
     * Menggunakan Observer Pattern untuk tracking
     */
    private static void createBookingCLI() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              BOOKING TIKET - CLI                       ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        // ========== STEP 1: Pilih Film ==========
        List<Movie> movies = Movie.getAllMovies();

        if (movies.isEmpty()) {
            System.out.println("❌ Tidak ada film tersedia.");
            return;
        }

        System.out.println("\n=== DAFTAR FILM ===");
        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);
            System.out.println((i + 1) + ". " + m.getTitle() + " (" + m.getGenre() + ") - Base Price: Rp " +
                    String.format("%,.0f", m.getBasePrice()));
        }

        System.out.print("\nPilih film (nomor): ");
        int movieChoice;
        try {
            movieChoice = Integer.parseInt(scanner.nextLine().trim());
            if (movieChoice < 1 || movieChoice > movies.size()) {
                System.out.println("❌ Pilihan tidak valid!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Input harus berupa angka!");
            return;
        }

        Movie selectedMovie = movies.get(movieChoice - 1);
        System.out.println("\n✓ Film dipilih: " + selectedMovie.getTitle());

        // ========== STEP 2: Pilih Jadwal ==========
        List<Schedule> allSchedules = Schedule.getAllSchedules();
        List<Schedule> movieSchedules = new ArrayList<>();

        for (Schedule s : allSchedules) {
            if (s.getMovieId() == selectedMovie.getMovieId()) {
                movieSchedules.add(s);
            }
        }

        if (movieSchedules.isEmpty()) {
            System.out.println("❌ Tidak ada jadwal untuk film ini.");
            return;
        }

        System.out.println("\n=== JADWAL " + selectedMovie.getTitle() + " ===");
        for (int i = 0; i < movieSchedules.size(); i++) {
            Schedule s = movieSchedules.get(i);
            System.out.println((i + 1) + ". " + s.getInfo() +
                    " | Final Price: Rp " + String.format("%,.0f", s.calculateFinalPrice()));
        }

        System.out.print("\nPilih jadwal (nomor): ");
        int scheduleChoice;
        try {
            scheduleChoice = Integer.parseInt(scanner.nextLine().trim());
            if (scheduleChoice < 1 || scheduleChoice > movieSchedules.size()) {
                System.out.println("❌ Pilihan tidak valid!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Input harus berupa angka!");
            return;
        }

        Schedule selectedSchedule = movieSchedules.get(scheduleChoice - 1);
        System.out.println("\n✓ Jadwal dipilih: " + selectedSchedule.getInfo());

        // ========== STEP 3: Tampilkan Available Seats ==========
        List<Seat> allSeats = Seat.getSeatsBySchedule(selectedSchedule.getScheduleId());
        List<Seat> availableSeats = new ArrayList<>();

        for (Seat seat : allSeats) {
            if (!seat.isBooked()) {
                availableSeats.add(seat);
            }
        }

        if (availableSeats.isEmpty()) {
            System.out.println("❌ Maaf, semua kursi sudah penuh untuk jadwal ini.");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     KURSI TERSEDIA - Schedule " + selectedSchedule.getScheduleId() + "     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Total tersedia: " + availableSeats.size() + " kursi\n");

        // Display seats in grid format
        displaySeatsGrid(allSeats);

        // ========== STEP 4: Pilih Kursi ==========
        System.out.print("\nMasukkan nomor kursi (contoh: A1): ");
        String seatNumberInput = scanner.nextLine().trim().toUpperCase();

        // Find seat
        Seat selectedSeat = null;
        for (Seat seat : availableSeats) {
            if (seat.getSeatNumber().equals(seatNumberInput)) {
                selectedSeat = seat;
                break;
            }
        }

        if (selectedSeat == null) {
            System.out.println("❌ Kursi tidak tersedia atau tidak valid!");
            return;
        }

        System.out.println("✓ Kursi dipilih: " + selectedSeat.getSeatNumber());

        // ========== STEP 5: Pilih Tipe Tiket ==========
        System.out.println("\n=== TIPE TIKET ===");
        System.out.println("1. Regular (1.0x)");
        System.out.println("2. VIP (1.5x)");
        System.out.println("3. Student (0.8x)");
        System.out.print("\nPilih tipe tiket: ");

        String ticketType;
        try {
            int typeChoice = Integer.parseInt(scanner.nextLine().trim());
            switch (typeChoice) {
                case 1:
                    ticketType = "Regular";
                    break;
                case 2:
                    ticketType = "VIP";
                    break;
                case 3:
                    ticketType = "Student";
                    break;
                default:
                    System.out.println("❌ Pilihan tidak valid! Menggunakan Regular.");
                    ticketType = "Regular";
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Input tidak valid! Menggunakan Regular.");
            ticketType = "Regular";
        }

        // ========== STEP 6: Hitung Total & Konfirmasi ==========
        double finalPrice = selectedSchedule.calculateFinalPrice();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      KONFIRMASI BOOKING                ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Film      : " + selectedMovie.getTitle());
        System.out.println("║ Jadwal    : " + selectedSchedule.getInfo());
        System.out.println("║ Kursi     : " + selectedSeat.getSeatNumber());
        System.out.println("║ Tipe      : " + ticketType);
        System.out.println("║ Harga     : Rp " + String.format("%,.0f", finalPrice));
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("\nKonfirmasi booking? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println("❌ Booking dibatalkan.");
            return;
        }

        // ========== STEP 7: Process Booking dengan Observer Pattern ==========
        try {
            // Create user-specific observer
            BookingObserver userObserver = new BookingObserver(
                    "User-" + currentUser.getUserId() + "-Observer",
                    currentUser.getUserId()
            );

            // Attach observer to seat
            selectedSeat.addObserver(userObserver);

            // Book seat (akan trigger observer notifications)
            System.out.println("\n--- Processing Booking ---");
            boolean seatBooked = selectedSeat.bookSeat();

            if (!seatBooked) {
                System.out.println("❌ Gagal booking kursi!");
                return;
            }

            // Create booking record
            Booking booking = new Booking(currentUser.getUserId(), selectedSchedule.getScheduleId());
            booking.addTicket(ticketType, selectedSeat.getSeatNumber(), finalPrice);

            // Save booking
            boolean saved = booking.saveBooking();

            if (saved) {
                System.out.println("\n✅ BOOKING BERHASIL!");
                System.out.println(booking.getReceipt());
            } else {
                System.out.println("❌ Gagal menyimpan booking!");
                // Rollback seat
                selectedSeat.releaseSeat();
            }

        } catch (Exception e) {
            System.out.println("❌ Error saat booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Display seats in grid format
     */
    private static void displaySeatsGrid(List<Seat> seats) {
        // Group seats by row
        java.util.Map<String, List<Seat>> seatsByRow = new java.util.TreeMap<>();

        for (Seat seat : seats) {
            String row = seat.getSeatNumber().substring(0, 1); // Get first char (A, B, C, etc)
            seatsByRow.putIfAbsent(row, new ArrayList<>());
            seatsByRow.get(row).add(seat);
        }

        System.out.println("       [SCREEN]");
        System.out.println("   ================\n");

        for (String row : seatsByRow.keySet()) {
            System.out.print(row + " |");
            List<Seat> rowSeats = seatsByRow.get(row);

            for (Seat seat : rowSeats) {
                if (seat.isBooked()) {
                    System.out.print(" ❌");
                } else {
                    System.out.print(" ✓ ");
                }
            }
            System.out.println();
        }

        System.out.println("\nKeterangan: ✓ = Tersedia | ❌ = Sudah Dipesan");
    }

    /**
     * Test Strategy Pattern (from TestStrategyPattern.java)
     */
    private static void testStrategyPattern() {
        System.out.println("\n========================================");
        System.out.println("        STRATEGY PATTERN DEMO CLI       ");
        System.out.println("========================================\n");

        // Step 1: Tampilkan list film
        List<Movie> movies = Movie.getAllMovies();

        if (movies.isEmpty()) {
            System.out.println("Tidak ditemukan film di movies.txt");
            return;
        }

        System.out.println("=== LIST FILM ===");
        for (Movie m : movies) {
            System.out.println(m.getMovieId() + ". " + m.getTitle() +
                    " (Base Price: " + m.getBasePrice() + ")");
        }

        System.out.print("\nPilih Film (masukkan Movie ID): ");
        int pickedMovieId;
        try {
            pickedMovieId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Input tidak valid!");
            return;
        }

        Movie selectedMovie = Movie.getMovieById(pickedMovieId);

        if (selectedMovie == null) {
            System.out.println("Movie tidak ditemukan!");
            return;
        }

        System.out.println("\nAnda memilih: " + selectedMovie.getTitle());
        System.out.println("----------------------------------------");

        // Step 2: Tampilkan schedule film tersebut
        List<Schedule> schedules = Schedule.getAllSchedules();
        List<Schedule> movieSchedules = new ArrayList<>();

        System.out.println("\n=== JADWAL UNTUK: " + selectedMovie.getTitle() + " ===");

        int nomor = 1;
        for (Schedule s : schedules) {
            if (s.getMovieId() == selectedMovie.getMovieId()) {
                movieSchedules.add(s);
                System.out.println(nomor + ". " + s.getInfo());
                nomor++;
            }
        }

        if (movieSchedules.isEmpty()) {
            System.out.println("Tidak ada jadwal untuk film ini.");
            return;
        }

        System.out.print("\nPilih Jadwal (nomor): ");
        int schedulePick;
        try {
            schedulePick = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Input tidak valid!");
            return;
        }

        if (schedulePick < 1 || schedulePick > movieSchedules.size()) {
            System.out.println("Pilihan jadwal tidak valid!");
            return;
        }

        Schedule selectedSchedule = movieSchedules.get(schedulePick - 1);

        // Step 3: Hitung harga akhir (Strategy Pattern)
        double finalPrice = selectedSchedule.calculateFinalPrice();

        System.out.println("\n========================================");
        System.out.println("              HASIL PERHITUNGAN         ");
        System.out.println("========================================");
        System.out.println("Film      : " + selectedMovie.getTitle());
        System.out.println("Base Price: " + selectedMovie.getBasePrice());
        System.out.println("Strategy  : " + (selectedSchedule.getPricingStrategy() != null
                ? selectedSchedule.getPricingStrategy().getStrategyName() : "N/A"));
        System.out.println("Multiplier: " + (selectedSchedule.getPricingStrategy() != null
                ? selectedSchedule.getPricingStrategy().getMultiplier() : 1.0));
        System.out.println("----------------------------------------");
        System.out.println("Final Price (after Strategy): " + finalPrice);
        System.out.println("========================================\n");
    }
}