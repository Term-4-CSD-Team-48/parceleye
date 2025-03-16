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
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView signUp;
    String signUpText;

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