package com.example.budgettracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, phoneno, password, conpassword;
    Button registerBtn, backButton;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phoneno = findViewById(R.id.phoneno);
        password = findViewById(R.id.password);
        conpassword = findViewById(R.id.conpassword);
        registerBtn = findViewById(R.id.registerBtn);
        backButton = findViewById(R.id.backButton);
        db = new DBHelper(this);

        registerBtn.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String phone = phoneno.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String conPass = conpassword.getText().toString().trim();

            if (user.isEmpty() || mail.isEmpty() || phone.isEmpty() || pass.isEmpty() || conPass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phone.matches("[0-9]{10}")) {
                Toast.makeText(this, "Enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(conPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkUserExists(user)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = db.insertUser(user, pass); // You can modify DBHelper to store email & phone too
                if (inserted) {
                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }
}
