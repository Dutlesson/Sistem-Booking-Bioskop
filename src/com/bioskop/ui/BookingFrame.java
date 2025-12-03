package com.bioskop.ui;

import javax.swing.*;
import java.awt.*;
import com.bioskop.model.Seat;
import java.util.List;

public class BookingFrame extends JFrame {
    private SeatSelectionPanel seatSelectionPanel;
    private int scheduleId;
    private String movieTitle;
    private String scheduleInfo;

    public BookingFrame(int scheduleId, String movieTitle, String scheduleInfo) {
        this.scheduleId = scheduleId;
        this.movieTitle = movieTitle;
        this.scheduleInfo = scheduleInfo;

        initializeFrame();
        initializeUI();
    }

    private void initializeFrame() {
        setTitle("Booking Tiket - " + movieTitle);
        setSize(950, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Seat Selection Panel
        seatSelectionPanel = new SeatSelectionPanel(scheduleId);
        add(seatSelectionPanel, BorderLayout.CENTER);

        // TODO: Load booked seats from database (integrate with DAO)
        // Example:
        // SeatDAO seatDAO = new SeatDAO();
        // List<Seat> bookedSeats = seatDAO.getBookedSeatsBySchedule(scheduleId);
        // seatSelectionPanel.loadSeatStatus(bookedSeats);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 33, 33));
        headerPanel.setPreferredSize(new Dimension(950, 90));
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Left side - Movie info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel movieLabel = new JLabel(movieTitle);
        movieLabel.setFont(new Font("Arial", Font.BOLD, 24));
        movieLabel.setForeground(Color.WHITE);
        movieLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel scheduleLabel = new JLabel(scheduleInfo);
        scheduleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        scheduleLabel.setForeground(new Color(200, 200, 200));
        scheduleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scheduleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        leftPanel.add(movieLabel);
        leftPanel.add(scheduleLabel);

        // Right side - Back button
        JButton backButton = new JButton("← Kembali");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(70, 70, 70));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin kembali?\nPilihan kursi akan dibatalkan.",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Load booked seats (call this after frame is created)
     */
    public void loadBookedSeats(List<Seat> bookedSeats) {
        seatSelectionPanel.loadSeatStatus(bookedSeats);
    }

    /**
     * Get selected seats from the panel
     */
    public java.util.Set<String> getSelectedSeats() {
        return seatSelectionPanel.getSelectedSeats();
    }

    // Main method untuk testing
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test frame
        SwingUtilities.invokeLater(() -> {
            BookingFrame frame = new BookingFrame(
                    1,
                    "Dune: Part Two",
                    "Studio 1 | 19:00 WIB | Kamis, 5 Desember 2024"
            );
            frame.setVisible(true);

            // Simulasi beberapa kursi sudah dipesan (untuk testing)
            // Uncomment baris di bawah untuk testing
            /*
            List<Seat> bookedSeats = new ArrayList<>();
            bookedSeats.add(new Seat(1, 1, "A1", true));
            bookedSeats.add(new Seat(2, 1, "A2", true));
            bookedSeats.add(new Seat(3, 1, "B5", true));
            frame.loadBookedSeats(bookedSeats);
            */
        });
    }
}