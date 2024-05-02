package com.wama.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart {
    private final List<HashMap<String, String>> items = new ArrayList<>();

    public void addItem(HashMap<String, String> item) {
        items.add(item);
    }

    public void removeItem(HashMap<String, String> item) {
        items.remove(item);
    }

    public List<HashMap<String, String>> getItems() {
        return items;
    }

    public double getTotal() {
        return items.stream().mapToDouble(item -> Double.parseDouble(item.get("price"))).sum();
    }
}