package com.bioskop.ui;

import com.bioskop.model.*;
import com.bioskop.factory.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * BookingHistoryPanel - Panel untuk menampilkan riwayat booking customer
 * Shows all bookings dengan detail tickets
 *
 * @author Fiandra (Member 3) - GUI Implementation
 * @version 3.0
 */
public class BookingHistoryPanel extends JPanel {

    private User currentUser;
    private JPanel bookingsContainer;
    private JLabel statsLabel;

    public BookingHistoryPanel(User user) {
        this.currentUser = user;

        initComponents();
        loadBookings();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("📋 My Booking History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 150, 243));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsLabel = new JLabel("Loading bookings...");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsLabel.setForeground(new Color(100, 100, 100));
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleContainer.add(titleLabel);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        titleContainer.add(statsLabel);

        // Refresh Button
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBackground(new Color(33, 150, 243));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadBookings());

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        // Bookings Container with ScrollPane
        bookingsContainer = new JPanel();
        bookingsContainer.setLayout(new BoxLayout(bookingsContainer, BoxLayout.Y_AXIS));
        bookingsContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(bookingsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Load bookings untuk current user
     */
    private void loadBookings() {
        bookingsContainer.removeAll();

        List<Booking> bookings = Booking.getBookingHistory(currentUser.getUserId());

        if (bookings.isEmpty()) {
            displayEmptyState();
        } else {
            updateStats(bookings);
            for (Booking booking : bookings) {
                bookingsContainer.add(createBookingCard(booking));
                bookingsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        bookingsContainer.revalidate();
        bookingsContainer.repaint();
    }

    /**
     * Display empty state ketika belum ada booking
     */
    private void displayEmptyState() {
        statsLabel.setText("No bookings yet");

        JPanel emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.setBackground(Color.WHITE);
        emptyPanel.setPreferredSize(new Dimension(700, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel iconLabel = new JLabel("📭", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        emptyPanel.add(iconLabel, gbc);

        gbc.gridy = 1;
        JLabel messageLabel = new JLabel("No bookings yet", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        messageLabel.setForeground(new Color(100, 100, 100));
        emptyPanel.add(messageLabel, gbc);

        gbc.gridy = 2;
        JLabel hintLabel = new JLabel("Start by browsing movies and booking tickets!", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hintLabel.setForeground(new Color(150, 150, 150));
        emptyPanel.add(hintLabel, gbc);

        bookingsContainer.add(emptyPanel);
    }

    /**
     * Update statistics label
     */
    private void updateStats(List<Booking> bookings) {
        int totalBookings = bookings.size();
        long confirmedBookings = bookings.stream()
                .filter(b -> b.getStatus().equals("confirmed"))
                .count();
        long cancelledBookings = bookings.stream()
                .filter(b -> b.getStatus().equals("cancelled"))
                .count();

        double totalSpent = bookings.stream()
                .filter(b -> b.getStatus().equals("confirmed"))
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        statsLabel.setText(String.format(
                "%d total bookings • %d confirmed • %d cancelled • Total spent: %s",
                totalBookings, confirmedBookings, cancelledBookings, formatPrice(totalSpent)
        ));
    }

    /**
     * Create booking card
     */
    private JPanel createBookingCard(Booking booking) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        // Left Panel - Booking Info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        // Booking ID & Status
        JPanel headerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerRow.setBackground(Color.WHITE);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bookingIdLabel = new JLabel("Booking #" + booking.getBookingId());
        bookingIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bookingIdLabel.setForeground(new Color(33, 33, 33));

        JLabel statusLabel = new JLabel(booking.getStatus().toUpperCase());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));

        if (booking.getStatus().equals("confirmed")) {
            statusLabel.setBackground(new Color(76, 175, 80));
        } else {
            statusLabel.setBackground(new Color(244, 67, 54));
        }

        headerRow.add(bookingIdLabel);
        headerRow.add(statusLabel);

        // Movie & Schedule Info
        Schedule schedule = Schedule.getScheduleById(booking.getScheduleId());
        Movie movie = Movie.getMovieById(schedule != null ? schedule.getMovieId() : 0);

        JLabel movieLabel = new JLabel("🎬 " + (movie != null ? movie.getTitle() : "Unknown Movie"));
        movieLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        movieLabel.setForeground(new Color(66, 66, 66));
        movieLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel scheduleLabel = new JLabel("📅 " + (schedule != null ? schedule.getInfo() : "N/A"));
        scheduleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        scheduleLabel.setForeground(new Color(100, 100, 100));
        scheduleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel("🕐 Booked on: " + booking.getBookingDate());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Total Price
        JLabel priceLabel = new JLabel("💰 " + formatPrice(booking.getTotalPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(new Color(76, 175, 80));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(headerRow);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(movieLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(scheduleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(dateLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(priceLabel);

        // Right Panel - Tickets Table
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JLabel ticketsLabel = new JLabel("🎟️ Tickets (" + booking.getTickets().size() + ")");
        ticketsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ticketsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create tickets table
        String[] columnNames = {"Type", "Seat", "Price"};
        Object[][] data = new Object[booking.getTickets().size()][3];

        int i = 0;
        for (Ticket ticket : booking.getTickets()) {
            data[i][0] = ticket.getTicketType();
            data[i][1] = ticket.getSeatNumber();
            data[i][2] = formatPrice(ticket.calculatePrice());
            i++;
        }

        JTable ticketsTable = new JTable(data, columnNames);
        ticketsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ticketsTable.setRowHeight(25);
        ticketsTable.setEnabled(false);
        ticketsTable.setFocusable(false);
        ticketsTable.setShowGrid(true);
        ticketsTable.setGridColor(new Color(224, 224, 224));

        // Table header styling
        JTableHeader header = ticketsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(245, 245, 245));
        header.setForeground(new Color(66, 66, 66));

        JScrollPane tableScrollPane = new JScrollPane(ticketsTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));
        tableScrollPane.setPreferredSize(new Dimension(300, 100));

        rightPanel.add(ticketsLabel, BorderLayout.NORTH);
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton detailsBtn = new JButton("View Receipt");
        detailsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsBtn.setForeground(new Color(33, 150, 243));
        detailsBtn.setBackground(Color.WHITE);
        detailsBtn.setFocusPainted(false);
        detailsBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        detailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailsBtn.addActionListener(e -> showReceipt(booking));

        buttonPanel.add(detailsBtn);

        // Cancel button (only if confirmed)
        if (booking.getStatus().equals("confirmed")) {
            JButton cancelBtn = new JButton("Cancel Booking");
            cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            cancelBtn.setForeground(new Color(244, 67, 54));
            cancelBtn.setBackground(Color.WHITE);
            cancelBtn.setFocusPainted(false);
            cancelBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(244, 67, 54), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
            cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cancelBtn.addActionListener(e -> cancelBooking(booking));

            buttonPanel.add(cancelBtn);
        }

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Assemble card
        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Show booking receipt
     */
    private void showReceipt(Booking booking) {
        String receipt = booking.getReceipt();

        JTextArea textArea = new JTextArea(receipt);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Booking Receipt #" + booking.getBookingId(),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Cancel booking
     */
    private void cancelBooking(Booking booking) {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this booking?\nBooking #" + booking.getBookingId(),
                "Cancel Booking",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            boolean success = booking.cancelBooking();

            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Booking cancelled successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadBookings(); // Refresh
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to cancel booking. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * Format price untuk display
     */
    private String formatPrice(double price) {
        return String.format("Rp %,.0f", price);
    }
}