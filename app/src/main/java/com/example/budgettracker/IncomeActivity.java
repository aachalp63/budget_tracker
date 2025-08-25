package com.example.budgettracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class IncomeActivity extends AppCompatActivity {

    private EditText titleInput, amountInput;
    private Button saveBtn;
    private String selectedCategory = "";
    private TextView dateText, timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income);

        titleInput = findViewById(R.id.titleInput);
        amountInput = findViewById(R.id.amountInput);
        saveBtn = findViewById(R.id.saveBtn);
        ImageView backIcon = findViewById(R.id.backIcon);
        ImageView refreshIcon = findViewById(R.id.refreshIcon);

        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);

        // Select Date
        dateText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(IncomeActivity.this, (view, y, m, d) -> {
                dateText.setText(d + "/" + (m + 1) + "/" + y);
            }, year, month, day);
            dialog.show();
        });

        // Select Time
        timeText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog dialog = new TimePickerDialog(IncomeActivity.this, (view, h, m) -> {
                timeText.setText(String.format("%02d:%02d", h, m));
            }, hour, minute, true);
            dialog.show();
        });

        // Category Buttons
        findViewById(R.id.catSalary).setOnClickListener(v -> {
            selectedCategory = "Salary";
            Toast.makeText(this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.catTrading).setOnClickListener(v -> {
            selectedCategory = "Trading";
            Toast.makeText(this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.catBills).setOnClickListener(v -> {
            selectedCategory = "Business";
            Toast.makeText(this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.catOthers).setOnClickListener(v -> {
            selectedCategory = "Others";
            Toast.makeText(this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
        });

        // ✅ Save Button Logic
        saveBtn.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String amountStr = amountInput.getText().toString();
            String date = dateText.getText().toString();
            String time = timeText.getText().toString();

            if (title.isEmpty() || amountStr.isEmpty() || selectedCategory.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(IncomeActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double amount = Double.parseDouble(amountStr);
                    Transaction transaction = new Transaction(title, amount, selectedCategory, "Income", date, time);
                    TransactionManager.addTransaction(IncomeActivity.this, transaction);
                    Toast.makeText(IncomeActivity.this, "Income Saved!", Toast.LENGTH_SHORT).show();

                    // ✅ Redirect to HomeActivity after saving
                    Intent intent = new Intent(IncomeActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();

                } catch (NumberFormatException e) {
                    Toast.makeText(IncomeActivity.this, "Invalid amount entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backIcon.setOnClickListener(v -> {
            startActivity(new Intent(IncomeActivity.this, HomeActivity.class));
            finish();
        });

        refreshIcon.setOnClickListener(v -> {
            startActivity(new Intent(IncomeActivity.this, ExpenseActivity.class));
            finish();
        });
    }
}
