package com.bioskop.ui;

import com.bioskop.model.*;
import com.bioskop.manager.BookingManager;
import com.bioskop.observer.BookingObserver;
import com.bioskop.factory.TicketFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * SeatSelectionDialog - Visual seat map untuk booking
 *
 * @author GUI Implementation
 * @version 1.0
 */
public class SeatSelectionDialog extends JDialog {

    private User currentUser;
    private int scheduleId;
    private Schedule schedule;
    private Movie movie;
    private BookingManager bookingManager;
    private List<Seat> allSeats;
    private Seat selectedSeat;
    private String selectedTicketType = "Regular";
    private boolean bookingComplete = false;

    private JPanel seatMapPanel;
    private JLabel infoLabel;
    private JLabel priceLabel;
    private JComboBox<String> ticketTypeCombo;
    private JButton confirmButton;

    public SeatSelectionDialog(Window parent, User user, int scheduleId, BookingManager manager) {
        super(parent, "Select Your Seat", ModalityType.APPLICATION_MODAL);
        this.currentUser = user;
        this.scheduleId = scheduleId;
        this.bookingManager = manager;

        loadData();
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void loadData() {
        schedule = Schedule.getScheduleById(scheduleId);
        if (schedule != null) {
            movie = Movie.getMovieById(schedule.getMovieId());
        }
        allSeats = Seat.getSeatsBySchedule(scheduleId);
    }

    private void initComponents() {
        setSize(700, 650);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Movie Info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 150, 243));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        String movieInfo = movie != null ? movie.getTitle() : "Unknown Movie";
        String scheduleInfo = schedule != null ? schedule.getInfo() : "";

        JLabel titleLabel = new JLabel("<html><b>" + movieInfo + "</b><br>" + scheduleInfo + "</html>");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.WHITE);

        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Center Panel - Seat Map
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Screen
        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(Color.WHITE);
        screenPanel.setPreferredSize(new Dimension(600, 40));

        JLabel screenLabel = new JLabel("[ S C R E E N ]", SwingConstants.CENTER);
        screenLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        screenLabel.setForeground(new Color(100, 100, 100));
        screenLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(200, 200, 200)));
        screenPanel.add(screenLabel);

        // Seat Map
        seatMapPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        seatMapPanel.setBackground(Color.WHITE);
        seatMapPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        for (Seat seat : allSeats) {
            JButton seatBtn = createSeatButton(seat);
            seatMapPanel.add(seatBtn);
        }

        JScrollPane scrollPane = new JScrollPane(seatMapPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        legendPanel.setBackground(Color.WHITE);

        legendPanel.add(createLegendItem("Available", new Color(76, 175, 80)));
        legendPanel.add(createLegendItem("Selected", new Color(33, 150, 243)));
        legendPanel.add(createLegendItem("Booked", new Color(189, 189, 189)));

        centerPanel.add(screenPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(legendPanel, BorderLayout.SOUTH);

        // Bottom Panel - Ticket Type & Confirm
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        infoLabel = new JLabel("Please select a seat");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Ticket Type Selection
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBackground(Color.WHITE);

        JLabel typeLabel = new JLabel("Ticket Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        ticketTypeCombo = new JComboBox<>(new String[]{"Regular", "VIP", "Student"});
        ticketTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ticketTypeCombo.addActionListener(e -> {
            selectedTicketType = (String) ticketTypeCombo.getSelectedItem();
            updatePrice();
        });

        typePanel.add(typeLabel);
        typePanel.add(ticketTypeCombo);

        priceLabel = new JLabel("Total: Rp 0");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(new Color(76, 175, 80));

        infoPanel.add(infoLabel);
        infoPanel.add(typePanel);
        infoPanel.add(priceLabel);

        // Confirm Button
        confirmButton = new JButton("Confirm Booking");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmButton.setBackground(new Color(76, 175, 80));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> confirmBooking());

        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        bottomPanel.add(confirmButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createSeatButton(Seat seat) {
        JButton btn = new JButton(seat.getSeatNumber());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(80, 50));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        if (seat.isBooked()) {
            btn.setBackground(new Color(189, 189, 189));
            btn.setForeground(Color.DARK_GRAY);
            btn.setEnabled(false);
            btn.setText(seat.getSeatNumber() + " ❌");
        } else {
            btn.setBackground(new Color(76, 175, 80));
            btn.setForeground(Color.WHITE);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> selectSeat(seat, btn));
        }

        return btn;
    }

    private void selectSeat(Seat seat, JButton btn) {
        // Deselect previous seat
        if (selectedSeat != null) {
            for (Component comp : seatMapPanel.getComponents()) {
                if (comp instanceof JButton) {
                    JButton b = (JButton) comp;
                    if (b.getText().startsWith(selectedSeat.getSeatNumber())) {
                        b.setBackground(new Color(76, 175, 80));
                    }
                }
            }
        }

        // Select new seat
        selectedSeat = seat;
        btn.setBackground(new Color(33, 150, 243));

        infoLabel.setText("Selected: Seat " + seat.getSeatNumber());
        confirmButton.setEnabled(true);

        updatePrice();
    }

    private void updatePrice() {
        if (selectedSeat != null && schedule != null) {
            double basePrice = schedule.calculateFinalPrice();
            double multiplier = TicketFactory.getTypeMultiplier(selectedTicketType);
            double finalPrice = basePrice * multiplier;

            priceLabel.setText(String.format("Total: Rp %,.0f", finalPrice));
        }
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);

        JLabel colorBox = new JLabel("   ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(colorBox);
        panel.add(label);

        return panel;
    }

    private void confirmBooking() {
        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(this, "Please select a seat!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm booking?\n\n" +
                        "Seat: " + selectedSeat.getSeatNumber() + "\n" +
                        "Ticket Type: " + selectedTicketType + "\n" +
                        "Total: " + priceLabel.getText(),
                "Confirm Booking",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            processBooking();
        }
    }

    private void processBooking() {
        try {
            // Create observer
            BookingObserver userObserver = new BookingObserver(
                    "User-" + currentUser.getUserId() + "-Observer",
                    currentUser.getUserId()
            );

            // Attach observer
            selectedSeat.addObserver(userObserver);

            // Book seat
            boolean seatBooked = selectedSeat.bookSeat();

            if (!seatBooked) {
                JOptionPane.showMessageDialog(this, "Failed to book seat!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create booking
            Booking booking = new Booking(currentUser.getUserId(), scheduleId);

            double basePrice = schedule.calculateFinalPrice();
            double multiplier = TicketFactory.getTypeMultiplier(selectedTicketType);
            double finalPrice = basePrice * multiplier;

            booking.addTicket(selectedTicketType, selectedSeat.getSeatNumber(), finalPrice);

            // Save booking
            boolean saved = booking.saveBooking();

            if (saved) {
                bookingComplete = true;

                JOptionPane.showMessageDialog(this,
                        "Booking successful!\n\n" + booking.getReceipt(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
            } else {
                // Rollback
                selectedSeat.releaseSeat();
                JOptionPane.showMessageDialog(this, "Failed to save booking!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean isBookingComplete() {
        return bookingComplete;
    }
}