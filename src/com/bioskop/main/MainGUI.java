package com.bioskop.main;

import com.bioskop.ui.LoginFrame;
import com.bioskop.util.FileManager;
import javax.swing.*;
import java.awt.*;

/**
 * MainGUI - Entry point untuk GUI Application
 * Replaces CLI MainApp with modern Swing GUI
 *
 * @author Nazriel (Member 1) - Original CLI
 * @author Fiandra (Member 3) - GUI Implementation\
 * @version 3.0 GUI
 */
public class MainGUI {

    public static void main(String[] args) {
        // Ensure data folder exists
        FileManager.ensureDataFolderExists();

        // Set Look and Feel
        setLookAndFeel();

        // Show splash screen
        showSplashScreen();

        // Launch login frame
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    /**
     * Set native Look and Feel for better UI
     */
    private static void setLookAndFeel() {
        try {
            // Try to use system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Customize UI defaults
            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TextArea.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
            UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));

        } catch (Exception e) {
            System.err.println("Could not set Look and Feel: " + e.getMessage());
        }
    }

    /**
     * Show splash screen while loading
     */
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(500, 300);
        splash.setLocationRelativeTo(null);

        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Gradient background
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 30, 48),
                        0, getHeight(), new Color(36, 59, 85)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        content.setLayout(new BorderLayout());

        // Logo panel
        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Cinema icon
        JLabel iconLabel = new JLabel("🎬", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        logoPanel.add(iconLabel, gbc);

        // Title
        gbc.gridy = 1;
        JLabel titleLabel = new JLabel("CINEMA BOOKING SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        logoPanel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy = 2;
        JLabel subtitleLabel = new JLabel("Modern GUI Edition", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        logoPanel.add(subtitleLabel, gbc);

        // Version
        gbc.gridy = 3;
        JLabel versionLabel = new JLabel("Version 3.0", SwingConstants.CENTER);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        versionLabel.setForeground(new Color(150, 150, 150));
        logoPanel.add(versionLabel, gbc);

        content.add(logoPanel, BorderLayout.CENTER);

        // Loading label at bottom
        JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        content.add(loadingLabel, BorderLayout.SOUTH);

        splash.setContentPane(content);
        splash.setVisible(true);

        // Simulate loading
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splash.dispose();
    }

    /**
     * Display system information
     */
    public static void displaySystemInfo() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║         CINEMA BOOKING SYSTEM - GUI VERSION           ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("║ Features:                                              ║");
        System.out.println("║  • Visual Seat Selection                              ║");
        System.out.println("║  • Interactive Booking System                         ║");
        System.out.println("║  • Real-time Updates                                  ║");
        System.out.println("║  • Admin & Customer Dashboards                        ║");
        System.out.println("║  • Observer Pattern Integration                       ║");
        System.out.println("║  • Strategy Pattern for Pricing                       ║");
        System.out.println("║  • Factory Pattern for Tickets                        ║");
        System.out.println("║                                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
    }
}