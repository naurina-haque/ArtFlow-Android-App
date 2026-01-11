package com.example.artflow;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ArtistLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, backButton;
    private TextView signupTextView;
    
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artistlogin);
        
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.artistEmailEdit);
        passwordEditText = findViewById(R.id.artistPasswordEdit);
        loginButton = findViewById(R.id.artistLoginButton);
        backButton = findViewById(R.id.artistBackButton);
        signupTextView = findViewById(R.id.artistSignupText);

        // Set click listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
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
    
    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Validate input fields
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Sign in with email and password using Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(ArtistLogin.this, "Login successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ArtistLogin.this, ArtistDashboard.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(ArtistLogin.this, "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}