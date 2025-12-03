package com.bioskop.main;

import com.bioskop.model.User;
import com.bioskop.model.Schedule;
import com.bioskop.util.FileManager;

import java.util.Scanner;

/**
 * MainApp - Console application entry point
 * Skeleton untuk nanti dilengkapi oleh Member 3 (Fiandra)
 *
 * @author Nazriel (Member 1) - Skeleton
 * @author Fiandra (Member 3) - Full Implementation
 * @version 1.0
 */
public class MainApp {

    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        // Ensure data folder exists
        FileManager.ensureDataFolderExists();

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
                    // TODO: Member 3 akan implement admin menu
                    System.out.println("Admin menu - To be implemented by Member 3");
                    currentUser = null; // Temporary logout
                } else {
                    // TODO: Member 3 akan implement customer menu
                    System.out.println("Customer menu - To be implemented by Member 3");
                    currentUser = null; // Temporary logout
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
}