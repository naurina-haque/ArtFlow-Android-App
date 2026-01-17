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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddArtworkActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = "AddArtworkActivity";

    private ImageView artworkImagePreview;
    private LinearLayout imageUploadArea;
    private Button btnSelectImage;
    private EditText etArtworkTitle, etArtworkDescription, etArtworkPrice;
    private Spinner spinnerArtworkCategory;
    private Button btnPublish;

    private Uri selectedImageUri;

    // Firebase variables private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.add_artwork);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://artflow-55038-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        initViews();
        setupCategoryDropdown();
        setupClickListeners();
    }

    private void initViews() {
        Log.d(TAG, "initViews called");
        artworkImagePreview= findViewById(R.id.artwork_image_preview);
        imageUploadArea = findViewById(R.id.image_upload_area);
        btnSelectImage = findViewById(R.id.btn_select_image);
        etArtworkTitle = findViewById(R.id.et_artwork_title);
        etArtworkDescription = findViewById(R.id.et_artwork_description);
        etArtworkPrice = findViewById(R.id.et_artwork_price);  // Fixed the ID
        spinnerArtworkCategory = findViewById(R.id.spinner_artwork_category);
        btnPublish = findViewById(R.id.btn_publish);

        Log.d(TAG, "Views initialized. artworkImagePreview: " + (artworkImagePreview != null) +", imageUploadArea: " + (imageUploadArea != null) +
              ", btnSelectImage: " + (btnSelectImage != null));
    }

    private void setupCategoryDropdown() {
        Log.d(TAG, "setupCategoryDropdown called");
        String[] categories = {
            "Select Category",
            "Colored Portraits",
            "B&W Portraits",
            "Watercolor Art",
            "Digital Art",
            "Abstract Art",
            "Landscape Art",
            "Line Art",
            "Acrylic Art"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        );

        spinnerArtworkCategory.setAdapter(adapter);
    }

    private void setupClickListeners() {
        Log.d(TAG, "setupClickListeners called");
        //Click listener for the upload area
        imageUploadArea.setOnClickListener(v -> checkPermissionAndOpenGallery());

        // Click listener for the select image button
        btnSelectImage.setOnClickListener(v -> checkPermissionAndOpenGallery());

        // Click listener for the publish button
        btnPublish.setOnClickListener(v -> publishArtwork());
    }

   private void checkPermissionAndOpenGallery() {
        Log.d(TAG, "checkPermissionAndOpenGallery called");

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

        if (ContextCompat.checkSelfPermission(this, permission)
           != PackageManager.PERMISSION_GRANTED) {
            // Check if we should show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // Show an explanation to the user
                new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("Thisapp needs access to your storage to select images for your artwork. Please grant the permission.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        ActivityCompat.requestPermissions(
                            AddArtworkActivity.this,
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
        Log.d(TAG, "openGallery called");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult called, requestCode: " + requestCode);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGallery();
            }else {
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
        Log.d(TAG, "onActivityResult called, requestCode: " + requestCode + ", resultCode: " + resultCode);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            artworkImagePreview.setImageURI(selectedImageUri);
            artworkImagePreview.setVisibility(View.VISIBLE);
            imageUploadArea.setVisibility(View.GONE);
        }
    }

    private void publishArtwork() {
        Log.d(TAG, "publishArtwork called");
        String title = etArtworkTitle.getText().toString().trim();
        String description = etArtworkDescription.getText().toString().trim();
        String price = etArtworkPrice.getText().toString().trim();
        String category = spinnerArtworkCategory.getSelectedItem().toString();

        // Validate inputs
        if (validateInputs(title, description, price, category)) {
            // Create new artwork object
            String categoryKey = convertCategoryToKey(category);

            // Get current user ID
            String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";

            if (userId.isEmpty()) {
                Log.e(TAG, "Useris not authenticated");
                Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_LONG).show();
                return;
            }

            Log.d(TAG, "Saving artwork for user ID: " + userId);

            // Create artwork object with user ID and image URI
            String imageUrl = selectedImageUri != null ? selectedImageUri.toString() : "";

            Artwork newArtwork = new Artwork(
                "", // id will be auto-generated by Firebase
                title,
                category,
                description,
                "Tk" + price,
                categoryKey,
                userId, // artistId
                imageUrl // image URL
            );

            // Save to Firebase
            saveArtworkToFirebase(newArtwork);
        }
    }

private void saveArtworkToFirebase(Artwork artwork) {
        // Generate a unique key for the artwork
        String artworkId = mDatabase.child("artworks").push().getKey();

        if (artworkId != null) {
            // Update the artwork with the generated ID
            artwork.setId(artworkId);

            Log.d(TAG, "Attempting to save artwork with ID: " + artworkId +
                      ", Title: " + artwork.getTitle() +
                      ", Artist ID: " + artwork.getArtistId());

            // Save to Firebase
            mDatabase.child("artworks").child(artworkId).setValue(artwork)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Artwork saved successfully with ID: " + artworkId);
                    Toast.makeText(this, "Artwork published successfully!", Toast.LENGTH_SHORT).show();

                    // Return to MyArtworks page
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving artwork: " + e.getMessage());
                    e.printStackTrace(); // Print full stack trace for debugging
                    Toast.makeText(this, "Failed to publish artwork: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
}
            else {
                Log.e(TAG, "Failed to generate artwork ID");
            Toast.makeText(this, "Failed to generate artwork ID", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String title, String description, String price, String category) {
        Log.d(TAG, "validateInputs called");
        boolean isValid = true;

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter artwork title", Toast.LENGTH_SHORT).show();
            etArtworkTitle.requestFocus(); // Request focus to highlight the field
            isValid = false;
        }

        if (isValid && description.isEmpty()) {
            Toast.makeText(this, "Please enter artwork description", Toast.LENGTH_SHORT).show();
            etArtworkDescription.requestFocus();
            isValid = false;
        }

        if (isValid && price.isEmpty()) {
            Toast.makeText(this, "Please enter artwork price", Toast.LENGTH_SHORT).show();
            etArtworkPrice.requestFocus();
            isValid = false;
        }

        if (isValid && category.equals("Select Category")) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            spinnerArtworkCategory.requestFocus();
            return false;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an artwork image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return isValid;
    }

    private String convertCategoryToKey(String category) {
        Log.d(TAG, "convertCategoryToKey called with: " + category);
        switch (category.toLowerCase()) {
            case "colored portraits":
                return "colored_portraits";
            case "b&w portraits":
                return "bw_portraits";
            case "watercolor art":
                return "watercolor";
            case "digital art":
                return "digital";
            case "abstract art":
                return "abstract";
            case "landscape art":
                return "landscape";
            case "line art":
                return "line";
            case "acrylic art":
                return "acrylic";
            default:
                return "other";
        }
    }
}