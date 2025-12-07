package com.bioskop.ui;

import com.bioskop.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame - GUI untuk Login dan Register
 *
 * @author GUI Implementation
 * @version 1.0
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton switchModeButton;
    private JLabel titleLabel;
    private JPanel mainPanel;
    private boolean isLoginMode = true;

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("🎬 Cinema Booking System - Login");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main Panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(20, 30, 48);
                Color color2 = new Color(36, 59, 85);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);

        // Logo/Icon Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBounds(125, 30, 200, 80);
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BorderLayout());

        JLabel logoLabel = new JLabel("🎬", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        logoPanel.add(logoLabel, BorderLayout.CENTER);

        // Title Label
        titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setBounds(0, 120, 450, 40);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBounds(50, 180, 350, 250);
        formPanel.setOpaque(false);
        formPanel.setLayout(null);

        // Username Label & Field
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(0, 0, 350, 25);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        usernameField = new JTextField();
        usernameField.setBounds(0, 30, 350, 40);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Password Label & Field
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(0, 80, 350, 25);
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setForeground(Color.WHITE);

        passwordField = new JPasswordField();
        passwordField.setBounds(0, 110, 350, 40);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Name Field (hidden by default)
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setBounds(0, 160, 350, 25);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setVisible(false);

        nameField = new JTextField();
        nameField.setBounds(0, 190, 350, 40);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        nameField.setVisible(false);

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(50, 440, 350, 50);
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 0));

        // Login Button
        loginButton = createStyledButton("LOGIN", new Color(46, 125, 50));
        loginButton.addActionListener(e -> handleLogin());

        // Register Button
        registerButton = createStyledButton("REGISTER", new Color(46, 125, 50));
        registerButton.addActionListener(e -> handleRegister());
        registerButton.setVisible(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Switch Mode Button
        switchModeButton = new JButton("Don't have an account? Register");
        switchModeButton.setBounds(50, 500, 350, 30);
        switchModeButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        switchModeButton.setForeground(new Color(100, 181, 246));
        switchModeButton.setContentAreaFilled(false);
        switchModeButton.setBorderPainted(false);
        switchModeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchModeButton.addActionListener(e -> toggleMode(nameLabel));

        // Add all components to main panel
        mainPanel.add(logoPanel);
        mainPanel.add(titleLabel);
        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(switchModeButton);

        add(mainPanel);

        // Enter key listeners
        ActionListener enterAction = e -> {
            if (isLoginMode) handleLogin();
            else handleRegister();
        };
        usernameField.addActionListener(enterAction);
        passwordField.addActionListener(enterAction);
        nameField.addActionListener(enterAction);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void toggleMode(JLabel nameLabel) {
        isLoginMode = !isLoginMode;

        if (isLoginMode) {
            titleLabel.setText("LOGIN");
            nameField.setVisible(false);
            nameLabel.setVisible(false);
            loginButton.setVisible(true);
            registerButton.setVisible(false);
            switchModeButton.setText("Don't have an account? Register");
        } else {
            titleLabel.setText("REGISTER");
            nameField.setVisible(true);
            nameLabel.setVisible(true);
            loginButton.setVisible(false);
            registerButton.setVisible(true);
            switchModeButton.setText("Already have an account? Login");
        }

        clearFields();
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields!");
            return;
        }

        User user = User.login(username, password);

        if (user != null) {
            showSuccess("Login successful! Welcome, " + user.getName());

            // Open appropriate dashboard
            SwingUtilities.invokeLater(() -> {
                if (user.isAdmin()) {
                    new AdminDashboard(user).setVisible(true);
                } else {
                    new CustomerDashboard(user).setVisible(true);
                }
                dispose();
            });
        } else {
            showError("Invalid username or password!");
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String name = nameField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
            showError("Please fill in all fields!");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters!");
            return;
        }

        boolean success = User.register(username, password, name);

        if (success) {
            showSuccess("Registration successful! Please login.");
            toggleMode(null);
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        nameField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}