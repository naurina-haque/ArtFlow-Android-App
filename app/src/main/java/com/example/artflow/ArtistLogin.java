package com.example.artflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ArtistLogin extends AppCompatActivity {

    private Button loginButton, backButton;
    private TextView signupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artistlogin);

        // Initialize UI components
        loginButton = findViewById(R.id.artistLoginButton);
        backButton = findViewById(R.id.artistBackButton);
        signupTextView = findViewById(R.id.artistSignupText);

        // Set click listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Artist Dashboard
                startActivity(new Intent(ArtistLogin.this, ArtistDashboard.class));
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArtistLogin.this, Select.class));
                finish();
            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArtistLogin.this, ArtistSignup.class));
                finish();
            }
        });
    }
}