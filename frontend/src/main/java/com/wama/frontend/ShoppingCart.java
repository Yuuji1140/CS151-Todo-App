package com.wama.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart {
    private final HashMap<HashMap<String, String>, Integer> items = new HashMap<>();

    public void addItem(HashMap<String, String> item) {
        // items.add(item);
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1);
        } else {
            items.put(item, 1);
        }
    }

    public void removeItem(HashMap<String, String> item) {
        // items.remove(item);
        if (items.containsKey(item)) {
            int quantity = items.get(item);
            if (quantity > 1)
                items.put(item, quantity - 1);
            else
                items.remove(item);
        }
    }

    public HashMap<HashMap<String, String>, Integer> getItems() {
        return items;
    }

    public double getTotal() {
        // return items.stream().mapToDouble(item -> Double.parseDouble(item.get("price"))).sum();
        double total = 0;
        for (HashMap<String, String> item : items.keySet()) {
            int quantity = items.get(item);
            double price = Double.parseDouble(item.get("price"));
            total += price * quantity;
        }

        return total;
    }
}