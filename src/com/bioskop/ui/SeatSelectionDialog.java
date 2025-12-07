package com.bioskop.ui;

import com.bioskop.model.*;
import com.bioskop.manager.BookingManager;
import com.bioskop.observer.BookingObserver;
import com.bioskop.factory.TicketFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * SeatSelectionDialog - Visual seat map untuk booking (Multi-Select)
 * WITH AUTO SEAT GENERATION IF FILE IS EMPTY
 */
public class SeatSelectionDialog extends JDialog {

    private User currentUser;
    private int scheduleId;
    private Schedule schedule;
    private Movie movie;
    private BookingManager bookingManager;
    private List<Seat> allSeats;
    private Set<Seat> selectedSeats;
    private Map<Seat, JButton> seatButtonMap;
    private String selectedTicketType = "Regular";
    private boolean bookingComplete = false;

    // Colors
    private static final Color COLOR_AVAILABLE = new Color(76, 175, 80); // Green
    private static final Color COLOR_SELECTED = new Color(33, 150, 243); // Blue
    private static final Color COLOR_BOOKED = new Color(158, 158, 158); // Gray

    private JPanel seatMapPanel;
    private JLabel infoLabel;
    private JLabel priceLabel;
    private JLabel selectedLabel;
    private JComboBox<String> ticketTypeCombo;
    private JButton confirmButton;
    private JButton clearButton;

    public SeatSelectionDialog(Window parent, User user, int scheduleId, BookingManager manager) {
        super(parent, "Select Your Seat", ModalityType.APPLICATION_MODAL);
        this.currentUser = user;
        this.scheduleId = scheduleId;
        this.bookingManager = manager;
        this.selectedSeats = new HashSet<>();
        this.seatButtonMap = new HashMap<>();

        System.out.println("\n=== SEAT SELECTION DIALOG INIT ===");
        System.out.println("User: " + user.getName());
        System.out.println("Schedule ID: " + scheduleId);

        loadData();
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void loadData() {
        System.out.println("\n--- Loading Data ---");

        // Load schedule
        schedule = Schedule.getScheduleById(scheduleId);
        System.out.println("Schedule loaded: " + (schedule != null ? "YES" : "NO"));

        if (schedule != null) {
            movie = Movie.getMovieById(schedule.getMovieId());
            System.out.println("Movie loaded: " + (movie != null ? movie.getTitle() : "NO"));
        }

        // Try to load seats from file
        allSeats = loadSeatsFromFile(scheduleId);

        // If no seats found, generate default seats
        if (allSeats == null || allSeats.isEmpty()) {
            System.out.println("⚠️ No seats found in file, generating default seats...");
            allSeats = generateDefaultSeats(scheduleId);
        }

        System.out.println("Total seats available: " + allSeats.size());
        if (!allSeats.isEmpty()) {
            System.out.println("First seat: " + allSeats.get(0).getSeatNumber());
            System.out.println("Last seat: " + allSeats.get(allSeats.size()-1).getSeatNumber());
        }
        System.out.println("--- Data Loading Complete ---\n");
    }

    /**
     * Load seats from file
     */
    private List<Seat> loadSeatsFromFile(int scheduleId) {
        List<Seat> seats = new ArrayList<>();

        try {
            // Try using the Seat model method first
            List<Seat> modelSeats = Seat.getSeatsBySchedule(scheduleId);
            if (modelSeats != null && !modelSeats.isEmpty()) {
                System.out.println("✓ Loaded " + modelSeats.size() + " seats from Seat.getSeatsBySchedule()");
                return modelSeats;
            }

            // If that fails, try manual reading
            List<String> lines = com.bioskop.util.FileManager.readFile("seats.txt");
            System.out.println("Reading seats.txt manually...");
            System.out.println("Total lines in seats.txt: " + lines.size());

            boolean skipHeader = true;
            for (String line : lines) {
                line = line.trim();

                if (line.isEmpty()) continue;

                // Skip header
                if (skipHeader && line.toLowerCase().contains("seatid")) {
                    skipHeader = false;
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    try {
                        int seatId = Integer.parseInt(parts[0].trim());
                        int sId = Integer.parseInt(parts[1].trim());
                        String seatNumber = parts[2].trim();
                        boolean isBooked = Boolean.parseBoolean(parts[3].trim());

                        if (sId == scheduleId) {
                            seats.add(new Seat(seatId, sId, seatNumber, isBooked));
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line: " + line);
                    }
                }
            }

            System.out.println("✓ Manually loaded " + seats.size() + " seats for schedule " + scheduleId);

        } catch (Exception e) {
            System.err.println("✗ Error loading seats from file: " + e.getMessage());
            e.printStackTrace();
        }

        return seats;
    }

    /**
     * Generate default seats if file is empty
     */
    private List<Seat> generateDefaultSeats(int scheduleId) {
        List<Seat> seats = new ArrayList<>();
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        int columns = 10;

        int seatId = scheduleId * 1000; // Unique starting ID

        for (char row : rows) {
            for (int col = 1; col <= columns; col++) {
                String seatNumber = row + String.valueOf(col);

                // Randomly mark some as booked for demo (10% chance)
                boolean isBooked = Math.random() < 0.1;

                seats.add(new Seat(seatId++, scheduleId, seatNumber, isBooked));
            }
        }

        System.out.println("✓ Generated " + seats.size() + " default seats");
        return seats;
    }

    private void initComponents() {
        setSize(850, 750);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Movie Info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 33, 33));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        String movieInfo = movie != null ? movie.getTitle() : "Unknown Movie";
        String scheduleInfo = schedule != null ? schedule.getInfo() : "";

        JLabel titleLabel = new JLabel("<html><b style='font-size:16px'>" + movieInfo + "</b><br>" +
                "<span style='font-size:12px'>" + scheduleInfo + "</span></html>");
        titleLabel.setForeground(Color.WHITE);

        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Center Panel - Seat Map
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Screen
        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(new Color(50, 50, 50));
        screenPanel.setPreferredSize(new Dimension(700, 45));
        screenPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JLabel screenLabel = new JLabel("=== L A Y A R ===", SwingConstants.CENTER);
        screenLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        screenLabel.setForeground(Color.WHITE);
        screenPanel.add(screenLabel);

        // Seat Map
        seatMapPanel = createSeatMapPanel();

        JScrollPane scrollPane = new JScrollPane(seatMapPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        legendPanel.setBackground(Color.WHITE);

        legendPanel.add(createLegendItem("Tersedia", COLOR_AVAILABLE));
        legendPanel.add(createLegendItem("Dipilih", COLOR_SELECTED));
        legendPanel.add(createLegendItem("Terpesan", COLOR_BOOKED));

        centerPanel.add(screenPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(legendPanel, BorderLayout.SOUTH);

        // Bottom Panel
        JPanel bottomPanel = createBottomPanel();

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSeatMapPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (allSeats == null || allSeats.isEmpty()) {
            panel.setLayout(new BorderLayout());
            JLabel noSeatsLabel = new JLabel("Tidak ada kursi tersedia untuk jadwal ini");
            noSeatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noSeatsLabel.setForeground(Color.GRAY);
            noSeatsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noSeatsLabel, BorderLayout.CENTER);
            return panel;
        }

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        // Group seats by row
        Map<Character, List<Seat>> seatsByRow = new TreeMap<>();
        for (Seat seat : allSeats) {
            char row = seat.getSeatNumber().charAt(0);
            seatsByRow.computeIfAbsent(row, k -> new ArrayList<>()).add(seat);
        }

        int rowIndex = 0;
        for (Map.Entry<Character, List<Seat>> entry : seatsByRow.entrySet()) {
            // Sort seats by number
            List<Seat> seatsInRow = entry.getValue();
            seatsInRow.sort((s1, s2) -> {
                int num1 = Integer.parseInt(s1.getSeatNumber().substring(1));
                int num2 = Integer.parseInt(s2.getSeatNumber().substring(1));
                return Integer.compare(num1, num2);
            });

            // Row label (left)
            gbc.gridx = 0;
            gbc.gridy = rowIndex;
            JLabel rowLabelLeft = new JLabel(String.valueOf(entry.getKey()));
            rowLabelLeft.setFont(new Font("Segoe UI", Font.BOLD, 16));
            rowLabelLeft.setForeground(new Color(100, 100, 100));
            panel.add(rowLabelLeft, gbc);

            // Seats
            int colIndex = 1;
            for (Seat seat : seatsInRow) {
                gbc.gridx = colIndex;
                gbc.gridy = rowIndex;
                JButton seatBtn = createSeatButton(seat);
                panel.add(seatBtn, gbc);
                seatButtonMap.put(seat, seatBtn);
                colIndex++;
            }

            // Row label (right)
            gbc.gridx = colIndex;
            gbc.gridy = rowIndex;
            JLabel rowLabelRight = new JLabel(String.valueOf(entry.getKey()));
            rowLabelRight.setFont(new Font("Segoe UI", Font.BOLD, 16));
            rowLabelRight.setForeground(new Color(100, 100, 100));
            panel.add(rowLabelRight, gbc);

            rowIndex++;
        }

        return panel;
    }

    private JButton createSeatButton(Seat seat) {
        JButton btn = new JButton(seat.getSeatNumber());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setPreferredSize(new Dimension(55, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        if (seat.isBooked()) {
            btn.setBackground(COLOR_BOOKED);
            btn.setForeground(Color.WHITE);
            btn.setEnabled(false);
            btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            btn.setBackground(COLOR_AVAILABLE);
            btn.setForeground(Color.WHITE);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!selectedSeats.contains(seat)) {
                        btn.setBackground(COLOR_AVAILABLE.darker());
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!selectedSeats.contains(seat)) {
                        btn.setBackground(COLOR_AVAILABLE);
                    }
                }
            });

            btn.addActionListener(e -> toggleSeatSelection(seat, btn));
        }

        return btn;
    }

    private void toggleSeatSelection(Seat seat, JButton btn) {
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
            btn.setBackground(COLOR_AVAILABLE);
        } else {
            selectedSeats.add(seat);
            btn.setBackground(COLOR_SELECTED);
        }
        updateUI();
    }

    private void clearAllSeats() {
        for (Seat seat : selectedSeats) {
            JButton btn = seatButtonMap.get(seat);
            if (btn != null) {
                btn.setBackground(COLOR_AVAILABLE);
            }
        }
        selectedSeats.clear();
        updateUI();
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        infoLabel = new JLabel("Silakan pilih kursi (bisa lebih dari 1)");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(100, 100, 100));

        selectedLabel = new JLabel("Kursi dipilih: 0");
        selectedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        selectedLabel.setForeground(new Color(100, 100, 100));

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBackground(Color.WHITE);

        JLabel typeLabel = new JLabel("Tipe Tiket:");
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
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(new Color(33, 150, 243));

        infoPanel.add(infoLabel);
        infoPanel.add(selectedLabel);
        infoPanel.add(typePanel);
        infoPanel.add(priceLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        clearButton = new JButton("Batalkan Semua");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clearButton.setBackground(new Color(244, 67, 54));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setEnabled(false);
        clearButton.addActionListener(e -> clearAllSeats());

        confirmButton = new JButton("Konfirmasi Booking");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setBackground(new Color(76, 175, 80));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> confirmBooking());

        buttonPanel.add(clearButton);
        buttonPanel.add(confirmButton);

        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    private void updateUI() {
        boolean hasSelection = !selectedSeats.isEmpty();

        confirmButton.setEnabled(hasSelection);
        clearButton.setEnabled(hasSelection);

        selectedLabel.setText("Kursi dipilih: " + selectedSeats.size());

        if (hasSelection) {
            List<String> seatNumbers = new ArrayList<>();
            for (Seat seat : selectedSeats) {
                seatNumbers.add(seat.getSeatNumber());
            }
            Collections.sort(seatNumbers);
            infoLabel.setText("Kursi dipilih: " + String.join(", ", seatNumbers));
        } else {
            infoLabel.setText("Silakan pilih kursi (bisa lebih dari 1)");
        }

        updatePrice();
    }

    private void updatePrice() {
        if (!selectedSeats.isEmpty() && schedule != null) {
            double basePrice = schedule.calculateFinalPrice();
            double multiplier = TicketFactory.getTypeMultiplier(selectedTicketType);
            double pricePerSeat = basePrice * multiplier;
            double totalPrice = pricePerSeat * selectedSeats.size();

            priceLabel.setText(String.format("Total: Rp %,.0f (%d kursi)", totalPrice, selectedSeats.size()));
        } else {
            priceLabel.setText("Total: Rp 0");
        }
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);

        JLabel colorBox = new JLabel("     ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panel.add(colorBox);
        panel.add(label);

        return panel;
    }

    private void confirmBooking() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan pilih minimal 1 kursi!");
            return;
        }

        List<String> seatNumbers = new ArrayList<>();
        for (Seat seat : selectedSeats) {
            seatNumbers.add(seat.getSeatNumber());
        }
        Collections.sort(seatNumbers);

        double basePrice = schedule.calculateFinalPrice();
        double multiplier = TicketFactory.getTypeMultiplier(selectedTicketType);
        double pricePerSeat = basePrice * multiplier;
        double totalPrice = pricePerSeat * selectedSeats.size();

        String message = String.format(
                "Konfirmasi booking?\n\n" +
                        "Kursi: %s\n" +
                        "Jumlah: %d kursi\n" +
                        "Tipe Tiket: %s\n" +
                        "Harga per kursi: Rp %,.0f\n" +
                        "Total: Rp %,.0f",
                String.join(", ", seatNumbers),
                selectedSeats.size(),
                selectedTicketType,
                pricePerSeat,
                totalPrice
        );

        int confirm = JOptionPane.showConfirmDialog(this,
                message,
                "Konfirmasi Booking",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            processBooking();
        }
    }

    private void processBooking() {
        try {
            Booking booking = new Booking(currentUser.getUserId(), scheduleId);

            double basePrice = schedule.calculateFinalPrice();
            double multiplier = TicketFactory.getTypeMultiplier(selectedTicketType);
            double pricePerSeat = basePrice * multiplier;
            double totalPrice = pricePerSeat * selectedSeats.size();

            List<Seat> bookedSeats = new ArrayList<>();

            for (Seat seat : selectedSeats) {
                BookingObserver userObserver = new BookingObserver(
                        "User-" + currentUser.getUserId() + "-Observer",
                        currentUser.getUserId()
                );

                seat.addObserver(userObserver);
                boolean seatBooked = seat.bookSeat();

                if (!seatBooked) {
                    for (Seat booked : bookedSeats) {
                        booked.releaseSeat();
                    }

                    JOptionPane.showMessageDialog(this,
                            "Gagal booking kursi " + seat.getSeatNumber() + "!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                bookedSeats.add(seat);
                booking.addTicket(selectedTicketType, seat.getSeatNumber(), pricePerSeat);
            }

            // Open Payment Dialog
            PaymentDialog paymentDialog = new PaymentDialog(this, currentUser, booking, totalPrice);
            paymentDialog.setVisible(true);

            if (paymentDialog.isPaymentSuccess()) {
                bookingComplete = true;
                dispose();
            } else {
                for (Seat seat : bookedSeats) {
                    seat.releaseSeat();
                }
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