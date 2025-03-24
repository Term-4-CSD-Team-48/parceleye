package com.example.parceleyelogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private EditText usernameFill;
    private EditText emailFill;
    private EditText passwordFill;
    private EditText passwordConfirmFill;
    private Button signUpButton;

    private static final String TAG = "Register";

    private ApiClient.Calls calls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calls = ApiClient.getCalls();

        // Initialize registration fills
        usernameFill = findViewById(R.id.username);
        emailFill = findViewById(R.id.email);
        passwordFill = findViewById(R.id.password);
        passwordConfirmFill = findViewById(R.id.passwordConfirm);

        // Setting up onClick listener for sign up button
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String username = usernameFill.getText().toString();
        final String email = emailFill.getText().toString();
        final String password = passwordFill.getText().toString();
        final String passwordConfirm = passwordConfirmFill.getText().toString();

        // Validate input fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(Register.this, "Please fill in all fields.", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if passwords are matching
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(Register.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
            return;
        }

        // Show registration progress
        Toast.makeText(Register.this, "Registering...", Toast.LENGTH_SHORT).show();

        // Making API call
        Call<Void> call = calls.registerUser(username, email, password);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Registration Successful");
                    Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_LONG).show();

                    // Redirect to Login Page
                    Intent login = new Intent(Register.this, MainActivity.class);
                    startActivity(login);
                    finish();
                } else {
                    Log.e(TAG, "Registration Error: " + response.code());
                    if (response.code() == 400) {
                        Toast.makeText(Register.this, "User already exists.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Register.this, "Registration failed. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                Toast.makeText(Register.this, "Network error. Please check your connection.", Toast.LENGTH_LONG).show();
            }
        });
    }
}