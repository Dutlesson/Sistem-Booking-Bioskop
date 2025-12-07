package com.bioskop.ui;

import com.bioskop.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PaymentDialog extends JDialog {

    private User currentUser;
    private Booking booking;
    private double totalPrice;
    private boolean paymentSuccess = false;

    private JComboBox<String> paymentMethodCombo;
    private JTextArea summaryArea;

    public PaymentDialog(Window parent, User user, Booking booking, double totalPrice) {
        super(parent, "Pembayaran", ModalityType.APPLICATION_MODAL);
        this.currentUser = user;
        this.booking = booking;
        this.totalPrice = totalPrice;

        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 600);
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Pembayaran Booking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new BorderLayout(10, 10));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel summaryLabel = new JLabel("Ringkasan Booking:");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        summaryArea = new JTextArea(booking.getReceipt());
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryArea.setBackground(new Color(245, 245, 245));
        summaryArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        scrollPane.setPreferredSize(new Dimension(450, 200));

        summaryPanel.add(summaryLabel, BorderLayout.NORTH);
        summaryPanel.add(scrollPane, BorderLayout.CENTER);

        // Payment Method Panel
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel methodLabel = new JLabel("Metode Pembayaran:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        methodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] paymentMethods = {
                "Cash",
                "Debit Card (BCA, Mandiri, BNI)",
                "Credit Card (Visa, Mastercard)",
                "E-Wallet (GoPay, OVO, Dana, ShopeePay)",
                "QRIS",
                "Transfer Bank"
        };

        paymentMethodCombo = new JComboBox<>(paymentMethods);
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        paymentMethodCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        paymentMethodCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel totalLabel = new JLabel(String.format("Total Pembayaran: Rp %,.0f", totalPrice));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(new Color(33, 150, 243));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        paymentPanel.add(methodLabel);
        paymentPanel.add(Box.createVerticalStrut(10));
        paymentPanel.add(paymentMethodCombo);
        paymentPanel.add(Box.createVerticalStrut(20));
        paymentPanel.add(totalLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton cancelButton = new JButton("Batal");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Batalkan pembayaran?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        JButton payButton = new JButton("Bayar Sekarang");
        payButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        payButton.setBackground(new Color(76, 175, 80));
        payButton.setForeground(Color.WHITE);
        payButton.setFocusPainted(false);
        payButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        payButton.addActionListener(e -> processPayment());

        buttonPanel.add(cancelButton);
        buttonPanel.add(payButton);

        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(summaryPanel, BorderLayout.NORTH);
        centerPanel.add(paymentPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void processPayment() {
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();

        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Konfirmasi pembayaran?\n\nMetode: %s\nTotal: Rp %,.0f",
                        paymentMethod, totalPrice),
                "Konfirmasi Pembayaran",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {


            // Simulate delay
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    SwingUtilities.invokeLater(() -> {

                        // Save booking with payment info
                        boolean saved = saveBookingToFile(paymentMethod);

                        if (saved) {
                            paymentSuccess = true;

                            JOptionPane.showMessageDialog(this,
                                    "Pembayaran Berhasil!\n\n" +
                                            "Metode: " + paymentMethod + "\n" +
                                            "Total: Rp " + String.format("%,.0f", totalPrice) + "\n\n" +
                                            "Booking telah disimpan.\nTerima kasih!",
                                    "Sukses",
                                    JOptionPane.INFORMATION_MESSAGE);

                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Gagal menyimpan booking!",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }


    private boolean saveBookingToFile(String paymentMethod) {
        try {
            System.out.println("\n=== SAVING BOOKING ===");
            System.out.println("User ID: " + currentUser.getUserId());
            System.out.println("Schedule ID: " + booking.getScheduleId());
            System.out.println("Total Price: " + totalPrice);
            System.out.println("Payment Method: " + paymentMethod);
            System.out.println("Tickets count: " + booking.getTickets().size());

            // ⭐ LANGSUNG GUNAKAN METHOD saveBooking() YANG SUDAH ADA!
            boolean saved = booking.saveBooking();

            if (saved) {
                System.out.println("✅ Booking saved successfully!");
                System.out.println("Booking ID: " + booking.getBookingId());
                System.out.println("======================\n");
                return true;
            } else {
                System.err.println("❌ Failed to save booking!");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error saving booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Generate unique ticket ID
     */
    private int generateUniqueTicketId() {
        try {
            List<String> lines = com.bioskop.util.FileManager.readFile("tickets.txt");

            int maxId = 0;
            for (String line : lines) {
                if (line.contains("ticketId|")) continue;

                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException ignored) {}
                }
            }

            return maxId + 1;

        } catch (Exception e) {
            return (int)(System.currentTimeMillis() % 100000);
        }
    }

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }
}