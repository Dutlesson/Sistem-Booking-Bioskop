package com.bioskop.ui;

import java.util.LinkedHashMap;
import java.util.Map;

public class FoodCart {

    private Map<FoodItem, Integer> items = new LinkedHashMap<>();

    public void addItem(FoodItem item, int qty) {
        items.put(item, items.getOrDefault(item, 0) + qty);
    }

    public Map<FoodItem, Integer> getItems() {
        return items;
    }

    public int getTotal() {
        int total = 0;
        for (var e : items.entrySet()) {
            total += e.getKey().price * e.getValue();
        }
        return total;
    }

    public void clear() {
        items.clear();
    }
}
