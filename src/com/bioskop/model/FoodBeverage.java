package com.bioskop.model;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class: FoodBeverage
 *
 * Represents an F&B item. File is "foodbeverages.txt" in project root.
 * Format: fnbId|name|price|quantity
 *
 */
public class FoodBeverage {
    private int fnbId;
    private String name;
    private double price;
    private int quantity; // default available quantity (optional)

    private static final String FILE = "../data/foodbeverages.txt";
    private static final String SEP = "\\|";
    private static final String JOIN = "|";

    public FoodBeverage(int fnbId, String name, double price, int quantity) {
        this.fnbId = fnbId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters / Setters
    public int getFnbId() { return fnbId; }
    public void setFnbId(int fnbId) { this.fnbId = fnbId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getFnBInfo() {
        return String.format("FnBID:%d | %s | Price: %.0f | Qty: %d",
                fnbId, name, price, quantity);
    }

    // ---- File operations ----
    public static List<FoodBeverage> loadFromFile() {
        List<FoodBeverage> list = new ArrayList<>();
        Path path = Paths.get(FILE);
        if (!Files.exists(path)) return list;
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(SEP, -1);
                // support both 3 and 4 fields for backward compatibility
                if (p.length < 3) continue;
                int id = Integer.parseInt(p[0]);
                String name = p[1];
                double price = Double.parseDouble(p[2]);
                int qty = 0;
                if (p.length >= 4 && !p[3].isEmpty()) qty = Integer.parseInt(p[3]);
                list.add(new FoodBeverage(id, name, price, qty));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<FoodBeverage> getAllFnB() {
        return loadFromFile();
    }

    public static FoodBeverage getFnBById(int id) {
        return loadFromFile().stream()
                .filter(f -> f.getFnbId() == id)
                .findFirst().orElse(null);
    }

    public static boolean addFnB(FoodBeverage fnb) {
        List<FoodBeverage> list = loadFromFile();
        int maxId = list.stream().mapToInt(FoodBeverage::getFnbId).max().orElse(0);
        if (fnb.getFnbId() <= 0) fnb.setFnbId(maxId + 1);
        else {
            boolean exists = list.stream().anyMatch(x -> x.getFnbId() == fnb.getFnbId());
            if (exists) return false;
        }
        list.add(fnb);
        return writeAll(list);
    }

    public static boolean updateFnB(FoodBeverage updated) {
        List<FoodBeverage> list = loadFromFile();
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFnbId() == updated.getFnbId()) {
                list.set(i, updated);
                found = true;
                break;
            }
        }
        if (!found) return false;
        return writeAll(list);
    }

    public static boolean deleteFnB(int id) {
        List<FoodBeverage> list = loadFromFile();
        List<FoodBeverage> filtered = list.stream()
                .filter(f -> f.getFnbId() != id)
                .collect(Collectors.toList());
        if (filtered.size() == list.size()) return false;
        return writeAll(filtered);
    }

    private static boolean writeAll(List<FoodBeverage> list) {
        List<String> lines = list.stream()
                .map(f -> String.join(JOIN,
                        String.valueOf(f.getFnbId()),
                        f.getName(),
                        String.valueOf((long) f.getPrice()),
                        String.valueOf(f.getQuantity())))
                .collect(Collectors.toList());
        try {
            Files.write(Paths.get(FILE), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
