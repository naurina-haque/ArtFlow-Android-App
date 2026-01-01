package com.example.artflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerLogin extends AppCompatActivity {

    private Button loginButton, backButton;
    private TextView signupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerlogin);

        // Initialize UI components
        loginButton = findViewById(R.id.customerLoginButton);
        backButton = findViewById(R.id.customerBackButton);
        signupTextView = findViewById(R.id.customerSignupText);

        // Set click listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For now, just navigate to the Select page
                // In a real app, you would implement actual login logic here
                startActivity(new Intent(CustomerLogin.this, Select.class));
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerLogin.this, Select.class));
                finish();
            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerLogin.this, CustomerSignup.class));
                finish();
            }
        });
    }
}