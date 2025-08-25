package com.example.budgettracker;

public class Transaction {
    private String title;
    private double amount;
    private String category;
    private String type;
    private String date;
    private String time;
    public Transaction() {}
    // Modify constructor to accept date and time
    public Transaction(String title, double amount, String category, String type, String date, String time) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
        this.time = time;
    }

    // Getters and setters (if needed)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
