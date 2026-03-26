package com.example.appcodetest.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;

public class MainActivity extends AppCompatActivity {

    Button btnLogout, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "MAIN OK", Toast.LENGTH_SHORT).show(); // 🔥 test

        btnLogout = findViewById(R.id.btnLogout);
        btnProfile = findViewById(R.id.btnProfile);

        // 🔥 FIX CRASH (check null)
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {

                SharedPreferences prefs = getSharedPreferences("APP", MODE_PRIVATE);
                prefs.edit().remove("TOKEN").apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }
    }
}