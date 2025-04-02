package com.example.parceleyelogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView signUp;
    private Button loginButton;
    private String signUpText;
    private EditText email;
    private EditText password;

    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUp = findViewById(R.id.signUp);
        loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // Set login button to be clickable
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(MainActivity.this, Home.class);
                String emailIn = email.getText().toString();
                String passwordIn = password.getText().toString();
                Log.v("LoginButton", "We are under the login button");
                Log.v("Email", emailIn);
                Log.v("Password", passwordIn);
                if (emailIn.equals("john@email.com") && passwordIn.equals("password123")) {
                    startActivity(login);
                } else {
                    // Making API calls
                    ApiClient.login(emailIn, passwordIn, new ApiClient.CallbackParts() {
                        @Override
                        public void onResponse(int code) {
                            if (code == 200) {
                                Log.i(TAG, "Login Successful");
                                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                                startActivity(login);
                            } else {
                                Toast.makeText(MainActivity.this, "Unknown error occured.", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Login Error: " + code);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(MainActivity.this, "Network error. Please check your connection.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        // Setting custom colour for "Sign Up"
        signUpText = "Don't have an account? Sign Up";
        SpannableString spannableString = new SpannableString(signUpText);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF000000"));
        spannableString.setSpan(colorSpan, 23, signUpText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Setting Sign Up to be clickable
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent register = new Intent(MainActivity.this, Register.class);
                startActivity(register);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#3685cd")); // Keep custom color
                ds.setUnderlineText(false); // Remove underline
            }
        };

        spannableString.setSpan(clickableSpan, 23, signUpText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUp.setText(spannableString);
        signUp.setMovementMethod(LinkMovementMethod.getInstance());
    }
}