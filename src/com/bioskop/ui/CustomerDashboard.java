package com.bioskop.ui;

import com.bioskop.model.*;
import com.bioskop.manager.BookingManager;
import com.bioskop.observer.BookingObserver;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * CustomerDashboard - Main GUI untuk Customer
 *
 * @author GUI Implementation
 * @version 1.0
 */
public class CustomerDashboard extends JFrame {

    private User currentUser;
    private BookingManager bookingManager;
    private JPanel contentPanel;
    private JLabel welcomeLabel;

    public CustomerDashboard(User user) {
        this.currentUser = user;
        this.bookingManager = new BookingManager();

        // Register global observer
        BookingObserver globalObserver = new BookingObserver("System-Observer", 0);
        bookingManager.registerGlobalObserver(globalObserver);

        initComponents();
        showMoviePanel();
    }

    private void initComponents() {
        setTitle("🎬 Cinema Booking System - Customer Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Layout
        setLayout(new BorderLayout(0, 0));

        // Top Panel (Header)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 150, 243));
        topPanel.setPreferredSize(new Dimension(1000, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(244, 67, 54));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());

        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);

        // Left Panel (Navigation)
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(245, 245, 245));
        navPanel.setPreferredSize(new Dimension(220, 620));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel navTitle = new JLabel("  MENU");
        navTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        navTitle.setForeground(new Color(66, 66, 66));
        navTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        navPanel.add(navTitle);
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Navigation Buttons
        navPanel.add(createNavButton("🎬 Browse Movies", e -> showMoviePanel()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("🎟️ Book Ticket", e -> showBookingPanel()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("📋 My Bookings", e -> showMyBookings()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("🍿 Food & Beverages", e -> showFoodBeverages()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("🧾 Food Order History", e -> showFoodOrderHistory()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("ℹ️ About", e -> showAbout()));

        // Content Panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(navPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(listener);
        return btn;
    }

    private void showMoviePanel() {
        contentPanel.removeAll();
        MovieSelectionPanel moviePanel = new MovieSelectionPanel(this, currentUser);
        contentPanel.add(moviePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showBookingPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🎟️ Book Your Ticket");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // Booking Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Movie Selection
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel movieLabel = new JLabel("Select Movie:");
        movieLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(movieLabel, gbc);

        JComboBox<String> movieCombo = new JComboBox<>();
        movieCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        List<Movie> movies = Movie.getAllMovies();
        for (Movie m : movies) {
            movieCombo.addItem(m.getMovieId() + " - " + m.getTitle());
        }
        gbc.gridx = 1;
        formPanel.add(movieCombo, gbc);

        // Schedule Selection
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel scheduleLabel = new JLabel("Select Schedule:");
        scheduleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(scheduleLabel, gbc);

        JComboBox<String> scheduleCombo = new JComboBox<>();
        scheduleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(scheduleCombo, gbc);

        // Update schedules when movie changes
        movieCombo.addActionListener(e -> {
            scheduleCombo.removeAllItems();
            String selected = (String) movieCombo.getSelectedItem();
            if (selected != null) {
                int movieId = Integer.parseInt(selected.split(" - ")[0]);
                List<Schedule> schedules = Schedule.getAllSchedules();
                for (Schedule s : schedules) {
                    if (s.getMovieId() == movieId) {
                        scheduleCombo.addItem(s.getScheduleId() + " - " + s.getInfo());
                    }
                }
            }
        });

        // Trigger initial load
        if (movieCombo.getItemCount() > 0) {
            movieCombo.setSelectedIndex(0);
        }

        // Book Button
        JButton bookButton = new JButton("Proceed to Seat Selection");
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bookButton.setBackground(new Color(76, 175, 80));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookButton.addActionListener(e -> {
            if (scheduleCombo.getSelectedItem() != null) {
                String selected = (String) scheduleCombo.getSelectedItem();
                int scheduleId = Integer.parseInt(selected.split(" - ")[0]);
                openSeatSelection(scheduleId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a schedule!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(bookButton, gbc);

        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void openSeatSelection(int scheduleId) {
        SeatSelectionDialog dialog = new SeatSelectionDialog(this, currentUser, scheduleId, bookingManager);
        dialog.setVisible(true);

        // Refresh bookings after dialog closes
        if (dialog.isBookingComplete()) {
            showMyBookings();
        }
    }

    private void showMyBookings() {
        contentPanel.removeAll();
        BookingHistoryPanel historyPanel = new BookingHistoryPanel(currentUser);
        contentPanel.add(historyPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showFoodBeverages() {
        contentPanel.removeAll();
        contentPanel.add(new FoodBeveragesPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showFoodOrderHistory() {
        contentPanel.removeAll();
        contentPanel.add(new FoodOrderHistoryPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAbout() {
        String message = """
            🎬 Cinema Booking System
            Version 3.0
            
            Features:
            • Browse Movies
            • Book Tickets with Visual Seat Selection
            • View Booking History
            • Multiple Ticket Types (Regular, VIP, Student)
            • Dynamic Pricing (Weekday/Weekend/Holiday)
            
            Design Patterns Used:
            • Observer Pattern
            • Strategy Pattern
            • Factory Method Pattern
            
            Developed with Java Swing GUI
            """;

        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}