package com.example.budgettracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExpenseActivity extends AppCompatActivity {

    private EditText titleInput, amountInput;
    private Button saveBtn;
    private String selectedCategory = "None";
    private TextView dateText, timeText;
    private String selectedDate = "", selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);

        titleInput = findViewById(R.id.titleInput);
        amountInput = findViewById(R.id.amountInput);
        saveBtn = findViewById(R.id.saveBtn);
        ImageView backIcon = findViewById(R.id.backIcon);
        ImageView refreshIcon = findViewById(R.id.refreshIcon);
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);

        // Date Picker
        dateText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(ExpenseActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                        dateText.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Time Picker
        timeText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(ExpenseActivity.this,
                    (view, hourOfDay, minute1) -> {
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                        timeText.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        // Category selection
        findViewById(R.id.catShopping).setOnClickListener(v -> selectCategory("Shopping"));
        findViewById(R.id.catFood).setOnClickListener(v -> selectCategory("Food"));
        findViewById(R.id.catTransport).setOnClickListener(v -> selectCategory("Transport"));
        findViewById(R.id.catSalary).setOnClickListener(v -> selectCategory("Salary"));
        findViewById(R.id.catTrading).setOnClickListener(v -> selectCategory("Trading"));
        findViewById(R.id.catFun).setOnClickListener(v -> selectCategory("Fun"));
        findViewById(R.id.catEducation).setOnClickListener(v -> selectCategory("Education"));
        findViewById(R.id.catBills).setOnClickListener(v -> selectCategory("Bills"));
        findViewById(R.id.catOthers).setOnClickListener(v -> selectCategory("Others"));

        // Save Button Logic
        saveBtn.setOnClickListener(v -> saveExpense());

        // Navigate back to HomeActivity
        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        // Switch to IncomeActivity
        refreshIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseActivity.this, IncomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void selectCategory(String category) {
        selectedCategory = category;
        Toast.makeText(this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
    }

    private void saveExpense() {
        String title = titleInput.getText().toString().trim();
        String amountStr = amountInput.getText().toString().trim();

        // Use selected date/time or fallback to current
        String currentDate = selectedDate.isEmpty() ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : selectedDate;
        String currentTime = selectedTime.isEmpty() ? new SimpleDateFormat("HH:mm:ss").format(new Date()) : selectedTime;

        if (title.isEmpty() || amountStr.isEmpty() || selectedCategory.equals("None")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            Transaction transaction = new Transaction(title, amount, selectedCategory, "Expense", currentDate, currentTime);
            TransactionManager.addTransaction(this, transaction);

            Toast.makeText(this, "Expense Saved!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity after saving
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount entered", Toast.LENGTH_SHORT).show();
        }
    }
}
