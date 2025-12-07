package com.bioskop.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class FoodOrderHistoryPanel extends JPanel {

    java.util.List<HistoryItem> history = new ArrayList<>();

    public FoodOrderHistoryPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("🧾 Food Order History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(new EmptyBorder(20, 20, 10, 20));
        add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel(new GridLayout(0, 1, 20, 20));
        listPanel.setBackground(new Color(245, 245, 245));
        listPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        loadHistory();

        for (HistoryItem item : history) {
            listPanel.add(createHistoryCard(item));
        }

        add(new JScrollPane(listPanel), BorderLayout.CENTER);
    }

    private JPanel createHistoryCard(HistoryItem item) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel img = new JLabel();
        img.setPreferredSize(new Dimension(120, 120));

        File f = new File("images/" + item.imageName);
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(f.getPath());
            Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(scaled));
        } else {
            img.setText("🍔");
            img.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
            img.setHorizontalAlignment(SwingConstants.CENTER);
        }

        JLabel name = new JLabel(item.name);
        name.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel qty = new JLabel("Qty: " + item.quantity);
        qty.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel price = new JLabel("Subtotal: Rp " + item.subtotal);
        price.setFont(new Font("Segoe UI", Font.BOLD, 16));
        price.setForeground(new Color(34, 139, 34));

        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setOpaque(false);
        info.add(name);
        info.add(qty);
        info.add(price);

        card.add(img, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    private void loadHistory() {
        File f = new File("data/foodorderhistory.txt");
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.equals("---")) continue;

                String[] p = line.split("\\|");

                if (p.length == 4) {
                    history.add(new HistoryItem(
                            p[0],
                            p[1],
                            Integer.parseInt(p[2]),
                            Integer.parseInt(p[3])
                    ));
                }
            }

        } catch (Exception ignored) {}
    }

    private static class HistoryItem {
        String name, imageName;
        int quantity, subtotal;

        public HistoryItem(String n, String img, int q, int s) {
            name = n;
            imageName = img;
            quantity = q;
            subtotal = s;
        }
    }
}
