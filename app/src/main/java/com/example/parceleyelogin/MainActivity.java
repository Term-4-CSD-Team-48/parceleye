package com.example.parceleyelogin;

import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavView;

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

        if (savedInstanceState == null)
            replaceFragment(new HomeFragment());
      
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
        bottomNavView = findViewById(R.id.bottomNavigationView);
        bottomNavView.setBackground(null);
        bottomNavView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.nav_history:
                    replaceFragment(new HistoryFragment());
                    break;

                case R.id.nav_recording:
                    replaceFragment(new RecordingFragment());
                    break;

                case R.id.nav_profile:
                    replaceFragment(new ProfileFragment());
                    break;

            }
            return true;
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new CameraFragment());
                bottomNavView.setSelectedItemId(R.id.fab);
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_frame, fragment);
        fragmentTransaction.commit();
    }
}