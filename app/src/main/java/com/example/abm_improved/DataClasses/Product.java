package com.example.abm_improved.DataClasses;

public class Product {

    private String uid;
    private String name;
    private String description;
    private String category;
    private double price;
    private int quantity;

    public Product(String uid, String name, String description, String category, double price, int quantity) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
