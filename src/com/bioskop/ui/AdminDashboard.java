package com.bioskop.ui;

import com.bioskop.model.*;
import com.bioskop.observer.BookingObserver;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * AdminDashboard - Main GUI untuk Admin
 *
 * @author GUI Implementation
 * @version 1.0
 */
public class AdminDashboard extends JFrame {

    private User currentUser;
    private JPanel contentPanel;
    private JLabel welcomeLabel;

    public AdminDashboard(User user) {
        this.currentUser = user;
        initComponents();
        showDashboardPanel();
    }

    private void initComponents() {
        setTitle("🎬 Cinema Booking System - Admin Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Layout
        setLayout(new BorderLayout(0, 0));

        // Top Panel (Header)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(156, 39, 176));
        topPanel.setPreferredSize(new Dimension(1100, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        welcomeLabel = new JLabel("Admin Dashboard - " + currentUser.getName());
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
        navPanel.setPreferredSize(new Dimension(240, 670));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel navTitle = new JLabel("  ADMIN MENU");
        navTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        navTitle.setForeground(new Color(66, 66, 66));
        navTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        navPanel.add(navTitle);
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Navigation Buttons
        navPanel.add(createNavButton("📊 Dashboard", e -> showDashboardPanel()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("🎬 Manage Movies", e -> showMoviesPanel()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("📅 Manage Schedules", e -> showSchedulesPanel()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("🎟️ All Bookings", e -> showAllBookings()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("📋 Booking Logs", e -> showBookingLogs()));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("👥 Manage Users", e -> showUsersPanel()));

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
        btn.setMaximumSize(new Dimension(220, 40));
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

    private void showDashboardPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📊 System Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        int totalMovies = Movie.getAllMovies().size();
        int totalSchedules = Schedule.getAllSchedules().size();
        int totalBookings = Booking.getAllBookings().size();
        int totalUsers = User.getAllUsers().size();

        statsPanel.add(createStatCard("🎬 Total Movies", String.valueOf(totalMovies), new Color(33, 150, 243)));
        statsPanel.add(createStatCard("📅 Total Schedules", String.valueOf(totalSchedules), new Color(76, 175, 80)));
        statsPanel.add(createStatCard("🎟️ Total Bookings", String.valueOf(totalBookings), new Color(255, 152, 0)));
        statsPanel.add(createStatCard("👥 Total Users", String.valueOf(totalUsers), new Color(156, 39, 176)));

        // Calculate revenue
        double totalRevenue = 0;
        for (Booking b : Booking.getAllBookings()) {
            if (b.getStatus().equals("confirmed")) {
                totalRevenue += b.getTotalPrice();
            }
        }

        statsPanel.add(createStatCard("💰 Total Revenue", String.format("Rp %,.0f", totalRevenue), new Color(244, 67, 54)));
        statsPanel.add(createStatCard("✅ Active System", "Running", new Color(0, 150, 136)));

        panel.add(title, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void showMoviesPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🎬 Movie Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // Movie Table
        String[] columns = {"ID", "Title", "Genre", "Duration (min)", "Rating", "Base Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Movie> movies = Movie.getAllMovies();
        for (Movie m : movies) {
            model.addRow(new Object[]{
                    m.getMovieId(),
                    m.getTitle(),
                    m.getGenre(),
                    m.getDurationMinutes(),
                    m.getRating(),
                    String.format("Rp %,.0f", m.getBasePrice())
            });
        }

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showSchedulesPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📅 Schedule Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // Schedule Table
        String[] columns = {"Schedule ID", "Movie", "Studio", "Date", "Time", "Seats", "Strategy", "Final Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Schedule> schedules = Schedule.getAllSchedules();
        for (Schedule s : schedules) {
            Movie m = Movie.getMovieById(s.getMovieId());
            String movieTitle = m != null ? m.getTitle() : "Unknown";

            model.addRow(new Object[]{
                    s.getScheduleId(),
                    movieTitle,
                    s.getInfo().split("\\|")[2].trim(),
                    s.getInfo().split("\\|")[3].trim().split(" ")[0],
                    s.getInfo().split("\\|")[3].trim().split(" ")[1],
                    s.getInfo().split("\\|")[4].trim(),
                    s.getPricingStrategy().getStrategyName(),
                    String.format("Rp %,.0f", s.calculateFinalPrice())
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAllBookings() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🎟️ All Bookings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        List<Booking> bookings = Booking.getAllBookings();

        if (bookings.isEmpty()) {
            JLabel emptyLabel = new JLabel("No bookings yet.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            panel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            JPanel bookingsPanel = new JPanel();
            bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));
            bookingsPanel.setBackground(Color.WHITE);

            for (Booking booking : bookings) {
                JTextArea receiptArea = new JTextArea(booking.getReceipt());
                receiptArea.setFont(new Font("Courier New", Font.PLAIN, 11));
                receiptArea.setEditable(false);
                receiptArea.setBackground(new Color(250, 250, 250));
                receiptArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

                bookingsPanel.add(receiptArea);
                bookingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            JScrollPane scrollPane = new JScrollPane(bookingsPanel);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        panel.add(title, BorderLayout.NORTH);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showBookingLogs() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📋 Booking Activity Logs");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JTextArea logsArea = new JTextArea();
        logsArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        logsArea.setEditable(false);

        List<String> logs = BookingObserver.getAllLogs();

        if (logs.isEmpty() || logs.size() == 1) {
            logsArea.setText("No logs available.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("BOOKING ACTIVITY LOGS\n");
            sb.append("=" .repeat(80)).append("\n\n");

            for (int i = 1; i < logs.size(); i++) {
                String[] parts = logs.get(i).split("\\|");
                if (parts.length >= 6) {
                    sb.append(String.format("[%s] Observer: %s | User: %s | Seat: %s | Action: %s | Schedule: %s\n",
                            parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }

            sb.append("\n").append("=" .repeat(80));
            sb.append(String.format("\nTotal logs: %d", logs.size() - 1));

            logsArea.setText(sb.toString());
        }

        JScrollPane scrollPane = new JScrollPane(logsArea);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showUsersPanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("👥 User Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // User Table
        String[] columns = {"User ID", "Username", "Name", "Role"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<User> users = User.getAllUsers();
        for (User u : users) {
            model.addRow(new Object[]{
                    u.getUserId(),
                    u.getUsername(),
                    u.getName(),
                    u.getRole().toUpperCase()
            });
        }

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(156, 39, 176));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(200, 200, 255));
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