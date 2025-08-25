package com.example.budgettracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginBtn, goToRegister;
    TextView forgotPassword;
    DBHelper db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        goToRegister = findViewById(R.id.goToRegister);
        forgotPassword = findViewById(R.id.forgotPassword);
        db = new DBHelper(this);

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);

        loginBtn.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.validateLogin(user, pass)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", user);
                editor.apply();

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        goToRegister.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });

        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
        });
    }
}
