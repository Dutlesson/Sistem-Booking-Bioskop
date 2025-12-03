package com.bioskop.ui;

import javax.swing.*;
import java.awt.*;
import com.bioskop.model.Seat;
import java.util.*;
import java.util.List;

public class SeatSelectionPanel extends JPanel {
    private static final int SEAT_SIZE = 50;
    private static final int SEAT_SPACING = 10;

    private Set<String> selectedSeats;
    private Map<String, JButton> seatButtons;
    private int scheduleId;
    private JLabel selectedCountLabel;

    // Colors
    private static final Color AVAILABLE_COLOR = new Color(76, 175, 80);
    private static final Color SELECTED_COLOR = new Color(33, 150, 243);
    private static final Color BOOKED_COLOR = new Color(158, 158, 158);

    public SeatSelectionPanel(int scheduleId) {
        this.scheduleId = scheduleId;
        this.selectedSeats = new HashSet<>();
        this.seatButtons = new HashMap<>();

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Pilih Kursi Anda", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Seat Grid Panel
        JPanel seatGridPanel = createSeatGridPanel();
        JScrollPane scrollPane = new JScrollPane(seatGridPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Legend and Info Panel
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSeatGridPanel() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        // Screen indicator
        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(new Color(50, 50, 50));
        screenPanel.setPreferredSize(new Dimension(700, 40));
        screenPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        JLabel screenLabel = new JLabel("=== LAYAR ===", SwingConstants.CENTER);
        screenLabel.setFont(new Font("Arial", Font.BOLD, 16));
        screenLabel.setForeground(Color.WHITE);
        screenPanel.add(screenLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 40, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gridPanel.add(screenPanel, gbc);

        // Generate seats (8 baris, 10 kolom)
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        int columns = 10;

        for (int i = 0; i < rows.length; i++) {
            // Row label (kiri)
            JLabel rowLabelLeft = new JLabel(String.valueOf(rows[i]));
            rowLabelLeft.setFont(new Font("Arial", Font.BOLD, 18));
            rowLabelLeft.setForeground(new Color(100, 100, 100));
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(5, 10, 5, 15);
            gbc.fill = GridBagConstraints.NONE;
            gridPanel.add(rowLabelLeft, gbc);

            // Seats
            for (int j = 1; j <= columns; j++) {
                String seatNumber = rows[i] + String.valueOf(j);
                JButton seatButton = createSeatButton(seatNumber);

                gbc.gridx = j;
                gbc.gridy = i + 1;
                gbc.gridwidth = 1;
                gbc.insets = new Insets(SEAT_SPACING/2, SEAT_SPACING/2,
                        SEAT_SPACING/2, SEAT_SPACING/2);
                gridPanel.add(seatButton, gbc);

                seatButtons.put(seatNumber, seatButton);
            }

            // Row label (kanan)
            JLabel rowLabelRight = new JLabel(String.valueOf(rows[i]));
            rowLabelRight.setFont(new Font("Arial", Font.BOLD, 18));
            rowLabelRight.setForeground(new Color(100, 100, 100));
            gbc.gridx = columns + 1;
            gbc.gridy = i + 1;
            gbc.insets = new Insets(5, 15, 5, 10);
            gridPanel.add(rowLabelRight, gbc);
        }

        // Add some padding at bottom
        gbc.gridy = rows.length + 1;
        gbc.weighty = 1.0;
        gridPanel.add(Box.createVerticalGlue(), gbc);

        return gridPanel;
    }

    private JButton createSeatButton(String seatNumber) {
        JButton button = new JButton(seatNumber);
        button.setPreferredSize(new Dimension(SEAT_SIZE, SEAT_SIZE));
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBackground(AVAILABLE_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.getBackground().equals(AVAILABLE_COLOR)) {
                    button.setBackground(AVAILABLE_COLOR.darker());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!selectedSeats.contains(seatNumber) &&
                        !button.getBackground().equals(BOOKED_COLOR)) {
                    button.setBackground(AVAILABLE_COLOR);
                }
            }
        });

        button.addActionListener(e -> toggleSeatSelection(seatNumber, button));

        return button;
    }

    private void toggleSeatSelection(String seatNumber, JButton button) {
        // Check if seat is already booked
        if (button.getBackground().equals(BOOKED_COLOR)) {
            JOptionPane.showMessageDialog(this,
                    "Kursi " + seatNumber + " sudah dipesan!",
                    "Kursi Tidak Tersedia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Toggle selection
        if (selectedSeats.contains(seatNumber)) {
            selectedSeats.remove(seatNumber);
            button.setBackground(AVAILABLE_COLOR);
        } else {
            selectedSeats.add(seatNumber);
            button.setBackground(SELECTED_COLOR);
        }

        // Update selected count
        updateSelectedCount();
    }

    private void updateSelectedCount() {
        if (selectedCountLabel != null) {
            selectedCountLabel.setText("Kursi dipilih: " + selectedSeats.size());
        }
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        panel.setBackground(Color.WHITE);

        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.add(createLegendItem("Tersedia", AVAILABLE_COLOR));
        legendPanel.add(createLegendItem("Dipilih", SELECTED_COLOR));
        legendPanel.add(createLegendItem("Terpesan", BOOKED_COLOR));

        panel.add(legendPanel, BorderLayout.WEST);

        // Selected info and button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightPanel.setBackground(Color.WHITE);

        selectedCountLabel = new JLabel("Kursi dipilih: 0");
        selectedCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        selectedCountLabel.setForeground(new Color(50, 50, 50));

        JButton confirmButton = new JButton("Konfirmasi Booking");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(33, 150, 243));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setPreferredSize(new Dimension(180, 40));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> confirmBooking());

        rightPanel.add(selectedCountLabel);
        rightPanel.add(confirmButton);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        item.setBackground(Color.WHITE);

        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(25, 25));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));

        item.add(colorBox);
        item.add(label);

        return item;
    }

    private void confirmBooking() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih minimal 1 kursi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Sort seats for better display
        List<String> sortedSeats = new ArrayList<>(selectedSeats);
        Collections.sort(sortedSeats);
        String seatList = String.join(", ", sortedSeats);

        int result = JOptionPane.showConfirmDialog(this,
                "Anda memilih kursi: " + seatList + "\n" +
                        "Total kursi: " + selectedSeats.size() + "\n\n" +
                        "Lanjutkan ke pembayaran?",
                "Konfirmasi Booking",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            // TODO: Proceed to payment (Commit 20)
            JOptionPane.showMessageDialog(this,
                    "Booking berhasil!\nKursi: " + seatList,
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Load seat status from database
     * Call this method after creating the panel to mark booked seats
     */
    public void loadSeatStatus(List<Seat> bookedSeats) {
        if (bookedSeats == null) return;

        for (Seat seat : bookedSeats) {
            if (seat.isBooked()) {
                String seatNumber = seat.getSeatNumber();
                JButton button = seatButtons.get(seatNumber);
                if (button != null) {
                    button.setBackground(BOOKED_COLOR);
                    button.setEnabled(false);
                    button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }

    /**
     * Get selected seats
     * @return Set of selected seat numbers
     */
    public Set<String> getSelectedSeats() {
        return new HashSet<>(selectedSeats);
    }

    /**
     * Get schedule ID
     * @return Schedule ID
     */
    public int getScheduleId() {
        return scheduleId;
    }
}