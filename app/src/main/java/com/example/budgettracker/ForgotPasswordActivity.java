package com.example.budgettracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText username, newPassword, confirmPassword;
    Button resetPasswordBtn, backButton;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        username = findViewById(R.id.username);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        backButton = findViewById(R.id.backButton);
        db = new DBHelper(this);

        resetPasswordBtn.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String newPass = newPassword.getText().toString().trim();
            String conPass = confirmPassword.getText().toString().trim();

            if (user.isEmpty() || newPass.isEmpty() || conPass.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(conPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(newPass)) {
                Toast.makeText(this, "Password must be at least 6 characters and include upper/lowercase, number, and special character", Toast.LENGTH_LONG).show();
                return;
            }

            boolean userExists = db.checkUserExists(user);
            if (!userExists) {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = db.updatePassword(user, newPass);
            if (updated) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            finish(); // Goes back to previous activity
        });
    }

    private boolean isValidPassword(String password) {
        // At least 6 characters, one upper, one lower, one digit, one special char
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$");
    }
}
