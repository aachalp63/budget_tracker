package com.example.budgettracker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private static final String PREFS_NAME = "transactions";

    // Helper: Get current username from session
    private static String getCurrentUsername(Context context) {
        SharedPreferences userPrefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        return userPrefs.getString("username", null); // default null
    }

    // Helper: Get transaction key for current user
    private static String getUserTransactionKey(Context context) {
        String username = getCurrentUsername(context);
        return "transactions_" + username;
    }

    // Save the updated transaction list for the current user
    public static void saveTransactions(Context context, List<Transaction> transactions) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(transactions);
        String userKey = getUserTransactionKey(context);
        editor.putString(userKey, json);
        editor.apply();
    }

    // Get transactions for the current user
    public static List<Transaction> getTransactions(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userKey = getUserTransactionKey(context);
        String json = sharedPreferences.getString(userKey, null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Transaction>>() {}.getType();

        if (json == null) {
            return new ArrayList<>();
        } else {
            return gson.fromJson(json, type);
        }
    }

    // Add a new transaction
    public static void addTransaction(Context context, Transaction transaction) {
        List<Transaction> transactions = getTransactions(context);
        transactions.add(transaction);
        saveTransactions(context, transactions);
    }

    // Delete a transaction
    public static void deleteTransaction(Context context, Transaction transaction) {
        List<Transaction> transactions = getTransactions(context);
        transactions.remove(transaction);
        saveTransactions(context, transactions);

        // Recalculate totals after deletion
        recalculateAndUpdateTotals(context);
    }

    // Recalculate the total income and expense
    private static void recalculateAndUpdateTotals(Context context) {
        List<Transaction> transactions = getTransactions(context);

        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Income")) {
                totalIncome += transaction.getAmount();
            } else if (transaction.getType().equalsIgnoreCase("Expense")) {
                totalExpense += transaction.getAmount();
            }
        }

        updateUIWithTotals(context, totalIncome, totalExpense);
    }

    // Update totals in SharedPreferences (used for UI)
    private static void updateUIWithTotals(Context context, double totalIncome, double totalExpense) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save totals under the current user's scope
        String username = getCurrentUsername(context);
        editor.putFloat(username + "_totalIncome", (float) totalIncome);
        editor.putFloat(username + "_totalExpense", (float) totalExpense);

        editor.apply();
    }
}
