package com.bioskop.ui;

import com.bioskop.model.*;
import com.bioskop.factory.Ticket;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * BookingConfirmationDialog - Dialog untuk menampilkan detail booking
 *
 * @author GUI Implementation
 * @version 1.0
 */
public class BookingConfirmationDialog extends JDialog {

    private Booking booking;
    private Schedule schedule;
    private Movie movie;

    public BookingConfirmationDialog(Window parent, Booking booking) {
        super(parent, "Booking Confirmation", ModalityType.APPLICATION_MODAL);
        this.booking = booking;

        loadData();
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void loadData() {
        if (booking != null) {
            schedule = Schedule.getScheduleById(booking.getScheduleId());
            if (schedule != null) {
                movie = Movie.getMovieById(schedule.getMovieId());
            }
        }
    }

    private void initComponents() {
        setSize(600, 700);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(76, 175, 80));
        headerPanel.setPreferredSize(new Dimension(600, 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel headerTitle = new JLabel("✓ Booking Confirmed");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerTitle.setForeground(Color.WHITE);

        JLabel headerSubtitle = new JLabel("Booking ID: #" + booking.getBookingId());
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        headerSubtitle.setForeground(new Color(240, 255, 240));

        JPanel headerTextPanel = new JPanel();
        headerTextPanel.setLayout(new BoxLayout(headerTextPanel, BoxLayout.Y_AXIS));
        headerTextPanel.setBackground(new Color(76, 175, 80));

        headerTextPanel.add(headerTitle);
        headerTextPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerTextPanel.add(headerSubtitle);

        headerPanel.add(headerTextPanel, BorderLayout.WEST);

        // Main Content Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Movie Information Section
        JPanel movieSection = createSection("🎬 Movie Information");
        addInfoRow(movieSection, "Title:", movie != null ? movie.getTitle() : "Unknown");
        addInfoRow(movieSection, "Genre:", movie != null ? movie.getGenre() : "Unknown");
        addInfoRow(movieSection, "Duration:", movie != null ? movie.getDurationMinutes() + " minutes" : "Unknown");
        addInfoRow(movieSection, "Rating:", movie != null ? String.format("%.1f / 10", movie.getRating()) : "Unknown");

        mainPanel.add(movieSection);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Schedule Information Section
        JPanel scheduleSection = createSection("📅 Schedule Information");
        if (schedule != null) {
            String scheduleInfo = schedule.getInfo();
            String[] parts = scheduleInfo.split("\\|");

            if (parts.length >= 4) {
                addInfoRow(scheduleSection, "Studio:", parts[2].trim());
                addInfoRow(scheduleSection, "Date:", parts[3].trim().split(" ")[0]);
                addInfoRow(scheduleSection, "Time:", parts[3].trim().split(" ")[1]);
                addInfoRow(scheduleSection, "Pricing:", schedule.getPricingStrategy().getStrategyName());
            }
        }

        mainPanel.add(scheduleSection);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Ticket Information Section
        JPanel ticketSection = createSection("🎟️ Ticket Information");
        List<Ticket> tickets = booking.getTickets();

        if (!tickets.isEmpty()) {
            for (int i = 0; i < tickets.size(); i++) {
                Ticket ticket = tickets.get(i);

                JPanel ticketPanel = new JPanel(new BorderLayout(10, 5));
                ticketPanel.setBackground(new Color(250, 250, 250));
                ticketPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                ticketPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                // Ticket icon and type
                JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                leftPanel.setBackground(new Color(250, 250, 250));

                JLabel ticketIcon = new JLabel(getTicketIcon(ticket.getTicketType()));
                ticketIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

                JLabel ticketType = new JLabel(ticket.getTicketType());
                ticketType.setFont(new Font("Segoe UI", Font.BOLD, 16));

                leftPanel.add(ticketIcon);
                leftPanel.add(ticketType);

                // Ticket details
                JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
                centerPanel.setBackground(new Color(250, 250, 250));

                JLabel seatLabel = new JLabel("Seat: " + ticket.getSeatNumber());
                seatLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                JLabel priceLabel = new JLabel(String.format("Price: Rp %,d", (int) ticket.calculatePrice()));
                priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                priceLabel.setForeground(new Color(76, 175, 80));

                centerPanel.add(seatLabel);
                centerPanel.add(priceLabel);

                ticketPanel.add(leftPanel, BorderLayout.WEST);
                ticketPanel.add(centerPanel, BorderLayout.CENTER);

                ticketSection.add(ticketPanel);
                ticketSection.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        mainPanel.add(ticketSection);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Payment Summary Section
        JPanel paymentSection = createSection("💰 Payment Summary");
        paymentSection.setBackground(new Color(245, 245, 245));

        addInfoRow(paymentSection, "Booking Date:", booking.getBookingDate());
        addInfoRow(paymentSection, "Status:", booking.getStatus().toUpperCase());

        // Total with highlight
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(245, 245, 245));
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(224, 224, 224)),
                BorderFactory.createEmptyBorder(15, 0, 5, 0)
        ));

        JLabel totalLabel = new JLabel("Total Amount:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel totalValue = new JLabel(String.format("Rp %,d", (int) booking.getTotalPrice()));
        totalValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalValue.setForeground(new Color(76, 175, 80));

        totalPanel.add(totalLabel, BorderLayout.WEST);
        totalPanel.add(totalValue, BorderLayout.EAST);

        paymentSection.add(totalPanel);

        mainPanel.add(paymentSection);

        // Scroll pane for main content
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Footer Panel with buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)));

        JButton printButton = new JButton("🖨️ Print Receipt");
        printButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        printButton.setBackground(new Color(33, 150, 243));
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.setBorderPainted(false);
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.addActionListener(e -> printReceipt());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        closeButton.setBackground(new Color(158, 158, 158));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        footerPanel.add(printButton);
        footerPanel.add(closeButton);

        // Add components to dialog
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createSection(String title) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 33, 33));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(titleLabel);
        section.add(Box.createRigidArea(new Dimension(0, 15)));

        return section;
    }

    private void addInfoRow(JPanel parent, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(parent.getBackground());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelComponent.setForeground(new Color(100, 100, 100));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueComponent.setForeground(new Color(33, 33, 33));

        row.add(labelComponent, BorderLayout.WEST);
        row.add(valueComponent, BorderLayout.EAST);

        parent.add(row);
        parent.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private String getTicketIcon(String ticketType) {
        return switch (ticketType.toLowerCase()) {
            case "vip" -> "⭐";
            case "student" -> "🎓";
            default -> "🎫";
        };
    }

    private void printReceipt() {
        // Create a print-friendly dialog
        JDialog printDialog = new JDialog(this, "Print Receipt", true);
        printDialog.setSize(400, 500);
        printDialog.setLocationRelativeTo(this);

        JTextArea receiptArea = new JTextArea(booking.getReceipt());
        receiptArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        receiptArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(receiptArea);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton copyButton = new JButton("Copy to Clipboard");
        copyButton.addActionListener(e -> {
            receiptArea.selectAll();
            receiptArea.copy();
            JOptionPane.showMessageDialog(printDialog,
                    "Receipt copied to clipboard!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> printDialog.dispose());

        buttonPanel.add(copyButton);
        buttonPanel.add(closeBtn);

        printDialog.add(scrollPane, BorderLayout.CENTER);
        printDialog.add(buttonPanel, BorderLayout.SOUTH);
        printDialog.setVisible(true);
    }
}