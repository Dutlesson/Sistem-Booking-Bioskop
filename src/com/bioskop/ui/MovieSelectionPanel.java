package com.bioskop.ui;

import com.bioskop.model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

/**
 * MovieSelectionPanel - Panel untuk menampilkan dan memilih movie
 * Displays movies in card layout dengan detail lengkap
 *
 * @author Fiandra (Member 3) - GUI Implementation
 * @version 3.0
 */
public class MovieSelectionPanel extends JPanel {

    private CustomerDashboard parentDashboard;
    private User currentUser;
    private JPanel moviesContainer;

    public MovieSelectionPanel(CustomerDashboard parent, User user) {
        this.parentDashboard = parent;
        this.currentUser = user;

        initComponents();
        loadMovies();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("🎬 Now Showing");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 150, 243));

        JLabel subtitleLabel = new JLabel("Select a movie to book tickets");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setBackground(Color.WHITE);
        titleContainer.add(titleLabel);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        titleContainer.add(subtitleLabel);

        headerPanel.add(titleContainer, BorderLayout.WEST);

        // Refresh Button
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBackground(new Color(33, 150, 243));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadMovies());

        headerPanel.add(refreshBtn, BorderLayout.EAST);

        // Movies Container with ScrollPane
        moviesContainer = new JPanel();
        moviesContainer.setLayout(new GridLayout(0, 2, 20, 20)); // 2 columns
        moviesContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(moviesContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Load movies dari database dan display sebagai cards
     */
    private void loadMovies() {
        moviesContainer.removeAll();

        List<Movie> movies = Movie.getAllMovies();

        if (movies.isEmpty()) {
            JLabel noMoviesLabel = new JLabel("No movies available at the moment");
            noMoviesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noMoviesLabel.setForeground(Color.GRAY);
            noMoviesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            moviesContainer.setLayout(new BorderLayout());
            moviesContainer.add(noMoviesLabel, BorderLayout.CENTER);
        } else {
            moviesContainer.setLayout(new GridLayout(0, 2, 20, 20));
            for (Movie movie : movies) {
                moviesContainer.add(createMovieCard(movie));
            }
        }

        moviesContainer.revalidate();
        moviesContainer.repaint();
    }

    /**
     * Create movie card untuk setiap movie
     */
    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Movie Icon/Poster (simplified dengan emoji)
        JLabel iconLabel = new JLabel("🎬", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setPreferredSize(new Dimension(80, 80));
        iconLabel.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));

        // Movie Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 33, 33));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Genre & Duration
        JLabel genreLabel = new JLabel("🎭 " + movie.getGenre() + " • " + movie.getDurationMinutes() + " mins");
        genreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        genreLabel.setForeground(new Color(100, 100, 100));
        genreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Rating
        JLabel ratingLabel = new JLabel("⭐ " + movie.getRating() + " / 10");
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ratingLabel.setForeground(new Color(255, 152, 0));
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Base Price
        JLabel priceLabel = new JLabel("💰 Base Price: " + formatPrice(movie.getBasePrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(76, 175, 80));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(genreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(ratingLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(priceLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton detailsBtn = new JButton("View Details");
        detailsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsBtn.setForeground(new Color(33, 150, 243));
        detailsBtn.setBackground(Color.WHITE);
        detailsBtn.setFocusPainted(false);
        detailsBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        detailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailsBtn.addActionListener(e -> showMovieDetails(movie));

        JButton bookBtn = new JButton("Book Now");
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setBackground(new Color(33, 150, 243));
        bookBtn.setFocusPainted(false);
        bookBtn.setBorderPainted(false);
        bookBtn.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        bookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookBtn.addActionListener(e -> proceedToBooking(movie));

        buttonPanel.add(detailsBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(bookBtn);

        // Assemble card
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(infoPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 245, 245));
                centerPanel.setBackground(new Color(245, 245, 245));
                infoPanel.setBackground(new Color(245, 245, 245));
                buttonPanel.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
                centerPanel.setBackground(Color.WHITE);
                infoPanel.setBackground(Color.WHITE);
                buttonPanel.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    /**
     * Show movie details dialog
     */
    private void showMovieDetails(Movie movie) {
        String details = String.format("""
            🎬 %s
            
            📋 Genre: %s
            ⏱️ Duration: %d minutes
            ⭐ Rating: %.1f / 10
            💰 Base Price: %s
            
            Note: Final ticket price will be calculated based on:
            • Ticket type (Regular/VIP/Student)
            • Day type (Weekday/Weekend/Holiday)
            
            Ready to book?
            """,
                movie.getTitle(),
                movie.getGenre(),
                movie.getDurationMinutes(),
                movie.getRating(),
                formatPrice(movie.getBasePrice())
        );

        int choice = JOptionPane.showConfirmDialog(
                this,
                details,
                "Movie Details",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (choice == JOptionPane.OK_OPTION) {
            proceedToBooking(movie);
        }
    }

    /**
     * Proceed to booking untuk movie ini
     */
    private void proceedToBooking(Movie movie) {
        // Get schedules untuk movie ini
        List<Schedule> schedules = Schedule.getAllSchedules();
        List<Schedule> movieSchedules = schedules.stream()
                .filter(s -> s.getMovieId() == movie.getMovieId())
                .toList();

        if (movieSchedules.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No schedules available for this movie yet.",
                    "No Schedules",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Show schedule selection dialog
        String[] scheduleOptions = movieSchedules.stream()
                .map(s -> s.getScheduleId() + " - " + s.getInfo())
                .toArray(String[]::new);

        String selectedSchedule = (String) JOptionPane.showInputDialog(
                this,
                "Select a schedule:",
                "Schedule Selection - " + movie.getTitle(),
                JOptionPane.QUESTION_MESSAGE,
                null,
                scheduleOptions,
                scheduleOptions[0]
        );

        if (selectedSchedule != null) {
            int scheduleId = Integer.parseInt(selectedSchedule.split(" - ")[0]);
            openSeatSelection(scheduleId);
        }
    }

    /**
     * Open seat selection dialog
     */
    private void openSeatSelection(int scheduleId) {
        // Delegate to parent dashboard
        SwingUtilities.invokeLater(() -> {
            // Get BookingManager from parent (you might need to add getter)
            // For now, create inline
            SeatSelectionDialog dialog = new SeatSelectionDialog(
                    SwingUtilities.getWindowAncestor(this),
                    currentUser,
                    scheduleId,
                    new com.bioskop.manager.BookingManager()
            );
            dialog.setVisible(true);

            // Refresh if booking complete
            if (dialog.isBookingComplete()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Booking completed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }

    /**
     * Format price untuk display
     */
    private String formatPrice(double price) {
        return String.format("Rp %,.0f", price);
    }
}