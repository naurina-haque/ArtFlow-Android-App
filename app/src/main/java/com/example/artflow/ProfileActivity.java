package com.example.artflow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private ImageView profileImage;
    private TextInputEditText etName, etEmail, etPhone;
    private Button btnSaveProfile, btnChangePhoto, btnChangePassword;
    private LinearLayout imageUploadArea;

    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://artflow-55038-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        initViews();
        loadUserProfile();
        setupClickListeners();
    }

    private void initViews() {
        profileImage = findViewById(R.id.profile_image);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        btnSaveProfile = findViewById(R.id.btn_save_profile);
        btnChangePhoto = findViewById(R.id.btn_change_photo);
        btnChangePassword = findViewById(R.id.btn_change_password);
        imageUploadArea = findViewById(R.id.image_upload_area);
    }

    private void loadUserProfile() {
        // Get current user
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (currentUserId != null) {
            // Load user profile data from Firebase
            mDatabase.child("users").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            // Populate fields with user data
                            etName.setText(user.getName());
                            etEmail.setText(user.getEmail());
                            etPhone.setText(user.getPhone());

                            // Disable email editing since it's managed by Firebase Auth
                            etEmail.setEnabled(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Error loading user profile: " + databaseError.getMessage());
                    Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupClickListeners() {
        btnSaveProfile.setOnClickListener(v -> saveProfileChanges());

        btnChangePhoto.setOnClickListener(v -> checkPermissionAndOpenGallery());

        btnChangePassword.setOnClickListener(v -> {
            // Navigate to change password activity or show password change dialog
            startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
        });
    }

    private void saveProfileChanges() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        // Get current user
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (currentUserId != null) {
            // Update user profile in Firebase Database
            DatabaseReference userRef = mDatabase.child("users").child(currentUserId);

            // Create user object with updated data
            User updatedUser = new User(name, email, phone);

            userRef.setValue(updatedUser)
                    .addOnSuccessListener(aVoid -> {
                        // Update display name in Firebase Auth as well
                        updateAuthDisplayName(name);

                        Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error updating profile: " + e.getMessage());
                        Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateAuthDisplayName(String displayName) {
        if (mAuth.getCurrentUser() != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();

            mAuth.getCurrentUser().updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Display name updated in Firebase Auth");
                        } else {
                            Log.e(TAG, "Error updating display name in Firebase Auth: " + task.getException().getMessage());
                        }
                    });
        }
    }

    private void checkPermissionAndOpenGallery() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+, use READ_MEDIA_IMAGES
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11-12, READ_EXTERNAL_STORAGE is legacy
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Check if we should show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // Show an explanation to the user
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("This app needs access to your storage to select a profile picture. Please grant the permission.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            ActivityCompat.requestPermissions(
                                    ProfileActivity.this,
                                    new String[]{permission},
                                    PERMISSION_REQUEST_CODE
                            );
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                // Request the permission
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{permission},
                        PERMISSION_REQUEST_CODE
                );
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGallery();
            } else {
                // Permission denied permanently (user checked "Don't ask again")
                String permission = permissions[0];
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    // Show dialog to guide user to settings
                    new AlertDialog.Builder(this)
                            .setTitle("Permission Denied")
                            .setMessage("Storage permission is needed to select images. Please enable it in Settings.")
                            .setPositiveButton("Go to Settings", (dialog, which) -> {
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    // Permission denied but not permanently - inform user
                    Toast.makeText(this, "Permission denied to access gallery. Please allow to select images.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            profileImage.setImageURI(selectedImageUri);

            // Here you would typically upload the image to Firebase Storage
            // and update the user's profile picture URL in the database
            uploadProfilePicture(selectedImageUri);
        }
    }

    private void uploadProfilePicture(Uri imageUri) {
        // Get current user
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (currentUserId != null) {
            // In a real app, you would upload the image to Firebase Storage
            // and store the URL in the user's profile
            // For now, we'll just show a toast
            Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show();
        }
    }
}