package com.example.budgettracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView transactionRecyclerView;

    TextView totalBalanceText, incomeAmountText, expenseAmountText, usernameText;
    Button logoutBtn;

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize views
        totalBalanceText = findViewById(R.id.totalBalance);
        incomeAmountText = findViewById(R.id.incomeAmount);
        expenseAmountText = findViewById(R.id.expenseAmount);
        usernameText = findViewById(R.id.usernameText);
        logoutBtn = findViewById(R.id.logoutBtn);

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);

        // Protect this page - redirect if not logged in
        if (!sharedPreferences.contains("username")) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }

        // Show username
        String username = sharedPreferences.getString("username", "User");
        usernameText.setText(username);

        // Handle logout
        logoutBtn.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // FAB to add income
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, IncomeActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_statistics) {
                startActivity(new Intent(this, StatisticsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return true;
        });
    }

    private void updateBalanceSummary(List<Transaction> transactionList) {
        double income = 0;
        double expense = 0;

        // Calculate income and expense totals
        for (Transaction transaction : transactionList) {
            if (transaction.getType().equalsIgnoreCase("Income")) {
                income += transaction.getAmount();
            } else if (transaction.getType().equalsIgnoreCase("Expense")) {
                expense += transaction.getAmount();
            }
        }

        double total = income - expense;

        // Update UI with new totals
        incomeAmountText.setText(String.format("₹%.1f", income));
        expenseAmountText.setText(String.format("₹%.1f", expense));
        totalBalanceText.setText(String.format("₹%.1f", total));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load transactions from SharedPreferences
        List<Transaction> transactionList = TransactionManager.getTransactions(this);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.transactionRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with the listener for transaction deletion
        adapter = new TransactionAdapter(this, transactionList, new TransactionAdapter.OnTransactionDeleteListener() {
            @Override
            public void onTransactionDelete(Transaction transaction) {
                // Get the updated transaction list
                List<Transaction> updatedTransactions = TransactionManager.getTransactions(HomeActivity.this);

                // Update balance summary
                updateBalanceSummary(updatedTransactions);
            }
        });
        recyclerView.setAdapter(adapter);

        // Update balance summary
        updateBalanceSummary(transactionList);
    }
}
