package com.bioskop.model;

import com.bioskop.util.FileManager;
import java.util.*;

/**
 * User - Model class untuk representasi user (Admin/Customer)
 * Handles authentication dan user management
 *
 * @author Nazriel (Member 1)
 * @version 1.0
 */
public class User {

    private int userId;
    private String username;
    private String password;
    private String name;
    private String role; // "admin" atau "customer"

    private static final String USERS_FILE = "users.txt";

    /**
     * Constructor
     */
    public User(int userId, String username, String password, String name, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role.toLowerCase();
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.toLowerCase();
    }

    /**
     * Cek apakah user adalah admin
     *
     * @return true jika user adalah admin
     */
    public boolean isAdmin() {
        return this.role.equalsIgnoreCase("admin");
    }

    /**
     * Login method - authenticate user
     *
     * @param username username input
     * @param password password input
     * @return User object jika berhasil, null jika gagal
     */
    public static User login(String username, String password) {
        List<User> users = loadFromFile();

        for (User user : users) {
            if (user.getUsername().equals(username) &&
                    user.getPassword().equals(password)) {
                return user;
            }
        }

        return null; // Login gagal
    }

    /**
     * Register user baru
     *
     * @param username username baru
     * @param password password baru
     * @param name nama lengkap
     * @return true jika berhasil register
     */
    public static boolean register(String username, String password, String name) {
        // Validasi input
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                name == null || name.trim().isEmpty()) {
            System.out.println("Error: Semua field harus diisi!");
            return false;
        }

        // Cek apakah username sudah ada
        if (isUsernameExists(username)) {
            System.out.println("Error: Username sudah digunakan!");
            return false;
        }

        // Validasi panjang password
        if (password.length() < 6) {
            System.out.println("Error: Password minimal 6 karakter!");
            return false;
        }

        // Generate user ID
        int newUserId = FileManager.getNextId(USERS_FILE);

        // Buat user baru (default role: customer)
        User newUser = new User(newUserId, username, password, name, "customer");

        // Save ke file
        newUser.saveToFile();

        System.out.println("Registrasi berhasil! Silakan login.");
        return true;
    }

    /**
     * Cek apakah username sudah ada
     *
     * @param username username yang dicek
     * @return true jika username sudah ada
     */
    private static boolean isUsernameExists(String username) {
        List<User> users = loadFromFile();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Load semua user dari file
     *
     * @return List of User objects
     */
    private static List<User> loadFromFile() {
        List<User> users = new ArrayList<>();
        List<String> lines = FileManager.readFile(USERS_FILE);

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");

            // Skip header atau baris invalid
            if (parts.length < 5) continue;

            try {
                int userId = Integer.parseInt(parts[0]);
                String username = parts[1];
                String password = parts[2];
                String name = parts[3];
                String role = parts[4];

                users.add(new User(userId, username, password, name, role));
            } catch (NumberFormatException e) {
                // Skip baris header atau invalid
                continue;
            }
        }

        return users;
    }

    /**
     * Save user ini ke file
     */
    public void saveToFile() {
        String line = String.format("%d|%s|%s|%s|%s",
                userId, username, password, name, role);
        FileManager.appendFile(USERS_FILE, line);
    }

    /**
     * Get semua users (untuk admin)
     *
     * @return List of all users
     */
    public static List<User> getAllUsers() {
        return loadFromFile();
    }

    /**
     * Get user by ID
     *
     * @param userId user ID
     * @return User object atau null jika tidak ditemukan
     */
    public static User getUserById(int userId) {
        List<User> users = loadFromFile();

        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }

        return null;
    }

    /**
     * Update user data
     *
     * @return true jika berhasil update
     */
    public boolean updateUser() {
        List<String> lines = FileManager.readFile(USERS_FILE);
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                updatedLines.add(line);
                continue;
            }

            String[] parts = line.split("\\|");

            if (parts.length < 5) {
                updatedLines.add(line);
                continue;
            }

            try {
                int lineUserId = Integer.parseInt(parts[0]);

                if (lineUserId == this.userId) {
                    // Update baris ini
                    String newLine = String.format("%d|%s|%s|%s|%s",
                            userId, username, password, name, role);
                    updatedLines.add(newLine);
                } else {
                    updatedLines.add(line);
                }
            } catch (NumberFormatException e) {
                updatedLines.add(line);
            }
        }

        FileManager.writeFile(USERS_FILE, updatedLines);
        return true;
    }

    /**
     * Delete user
     *
     * @param userId ID user yang akan dihapus
     * @return true jika berhasil delete
     */
    public static boolean deleteUser(int userId) {
        List<String> lines = FileManager.readFile(USERS_FILE);
        List<String> updatedLines = new ArrayList<>();
        boolean deleted = false;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                updatedLines.add(line);
                continue;
            }

            String[] parts = line.split("\\|");

            if (parts.length < 5) {
                updatedLines.add(line);
                continue;
            }

            try {
                int lineUserId = Integer.parseInt(parts[0]);

                if (lineUserId == userId) {
                    deleted = true;
                    // Skip baris ini (tidak ditambahkan ke updatedLines)
                } else {
                    updatedLines.add(line);
                }
            } catch (NumberFormatException e) {
                updatedLines.add(line);
            }
        }

        if (deleted) {
            FileManager.writeFile(USERS_FILE, updatedLines);
        }

        return deleted;
    }

    /**
     * Display user info
     *
     * @return String representasi user
     */
    @Override
    public String toString() {
        return String.format("User[ID=%d, Username=%s, Name=%s, Role=%s]",
                userId, username, name, role);
    }

    /**
     * Get user info formatted
     *
     * @return formatted user info
     */
    public String getUserInfo() {
        return String.format("""
                ┌─────────────────────────────────┐
                │ USER INFORMATION                │
                ├─────────────────────────────────┤
                │ ID       : %-20d │
                │ Username : %-20s │
                │ Name     : %-20s │
                │ Role     : %-20s │
                └─────────────────────────────────┘
                """, userId, username, name, role.toUpperCase());
    }
}