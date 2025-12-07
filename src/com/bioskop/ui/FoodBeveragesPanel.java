package com.bioskop.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class FoodBeveragesPanel extends JPanel {

    private List<FoodItem> items = new ArrayList<>();
    private FoodCart cart = new FoodCart();
    private JTextArea cartArea;
    private JLabel totalLabel;

    public FoodBeveragesPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("🍿 Food & Beverage Menu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(new EmptyBorder(20, 20, 10, 20));
        add(title, BorderLayout.NORTH);

        // 1 COLUMN ONLY
        JPanel gridPanel = new JPanel(new GridLayout(0, 1, 20, 20));
        gridPanel.setBackground(new Color(245, 245, 245));
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel cartPanel = createCartPanel();

        loadFoodData();

        for (FoodItem item : items) {
            gridPanel.add(createFoodCard(item));
        }

        add(new JScrollPane(gridPanel), BorderLayout.CENTER);
        add(cartPanel, BorderLayout.EAST);
    }

    private JPanel createFoodCard(FoodItem item) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel img = new JLabel();
        img.setPreferredSize(new Dimension(150, 150));

        File f = new File("images/" + item.imageName);
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(f.getPath());
            Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(scaled));
        } else {
            img.setText("🍔");
            img.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));
            img.setHorizontalAlignment(SwingConstants.CENTER);
        }

        JLabel name = new JLabel(item.name);
        name.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel price = new JLabel("Rp " + item.price);
        price.setFont(new Font("Segoe UI", Font.BOLD, 16));
        price.setForeground(new Color(34, 139, 34));

        JTextArea desc = new JTextArea(item.desc);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);

        JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

        JButton btn = new JButton("Add to Cart");
        btn.setBackground(new Color(0, 122, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        btn.addActionListener(e -> {
            cart.addItem(item, (int) qty.getValue());
            updateCartUI();
        });

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(name, BorderLayout.NORTH);
        right.add(desc, BorderLayout.CENTER);
        right.add(price, BorderLayout.SOUTH);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(new JLabel("Qty:"));
        bottom.add(qty);
        bottom.add(btn);

        card.add(img, BorderLayout.WEST);
        card.add(right, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCartPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel("🛒 Cart");
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));

        cartArea = new JTextArea();
        cartArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cartArea.setEditable(false);

        totalLabel = new JLabel("Total: Rp 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton pay = new JButton("Pay Now");
        pay.setBackground(new Color(34, 139, 34));
        pay.setForeground(Color.WHITE);

        pay.addActionListener(e -> processPayment());

        panel.add(t, BorderLayout.NORTH);
        panel.add(new JScrollPane(cartArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(totalLabel, BorderLayout.NORTH);
        bottom.add(pay, BorderLayout.SOUTH);

        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void updateCartUI() {
        cartArea.setText("");
        for (var entry : cart.getItems().entrySet()) {
            FoodItem item = entry.getKey();
            int qty = entry.getValue();
            cartArea.append(item.name + " x" + qty + " = Rp " + (item.price * qty) + "\n");
        }
        totalLabel.setText("Total: Rp " + cart.getTotal());
    }

    private void processPayment() {

        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }

        try (FileWriter fw = new FileWriter("data/foodorderhistory.txt", true)) {

            for (var entry : cart.getItems().entrySet()) {
                FoodItem item = entry.getKey();
                int qty = entry.getValue();
                int subtotal = item.price * qty;

                // SAVE WITH IMAGE NAME
                fw.write(item.name + "|" + item.imageName + "|" + qty + "|" + subtotal + "\n");
            }

            fw.write("---\n");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving order!");
        }

        JOptionPane.showMessageDialog(this, "Payment successful!");

        cart.clear();
        updateCartUI();
    }

    private void loadFoodData() {

        File file = new File("data/foodbeverages.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");

                items.add(new FoodItem(
                        p[0],
                        Integer.parseInt(p[1]),
                        p[2],
                        p[3]
                ));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading F&B file!");
        }
    }
}
