package com.example.budgettracker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import android.graphics.Color;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Setup Toolbar as action bar with back button
        Toolbar toolbar = findViewById(R.id.statistics_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Statistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button

        pieChart = findViewById(R.id.pieChart);

        showStatistics();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Back to HomeActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showStatistics() {
        List<Transaction> transactions = TransactionManager.getTransactions(this);

        float totalIncome = 0f;
        float totalExpense = 0f;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Income")) {
                totalIncome += transaction.getAmount();
            } else if (transaction.getType().equalsIgnoreCase("Expense")) {
                totalExpense += transaction.getAmount();
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (totalIncome > 0) entries.add(new PieEntry(totalIncome, "Total Income ₹" + totalIncome));
        if (totalExpense > 0) entries.add(new PieEntry(totalExpense, "Total Expense ₹" + totalExpense));

        PieDataSet dataSet = new PieDataSet(entries, "Budget Overview");
        dataSet.setColors(Color.rgb(76, 175, 80), Color.rgb(244, 67, 54)); // Green and Red
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Income vs Expense");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}
