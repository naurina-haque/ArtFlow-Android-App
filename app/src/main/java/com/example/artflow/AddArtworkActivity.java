package com.example.artflow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.textfield.TextInputEditText;

public class AddArtworkActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private ImageView artworkImagePreview;
    private FrameLayout imageUploadArea;
    private Button btnSelectImage;
    private TextInputEditText etArtworkTitle, etArtworkDescription, etArtworkPrice;
    private AutoCompleteTextView etArtworkCategory;
    private Button btnPublish;
    private LinearLayout imageUploadContainer;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_artwork);

        initViews();
        setupCategoryDropdown();
        setupClickListeners();
    }

    private void initViews() {
        artworkImagePreview = findViewById(R.id.artwork_image_preview);
        imageUploadArea = findViewById(R.id.image_upload_area);
        btnSelectImage = findViewById(R.id.btn_select_image);
        etArtworkTitle = findViewById(R.id.et_artwork_title);
        etArtworkDescription = findViewById(R.id.et_artwork_description);
        etArtworkPrice = findViewById(R.id.et_artwork_price);
        etArtworkCategory = findViewById(R.id.et_artwork_category);
        btnPublish = findViewById(R.id.btn_publish);
        imageUploadContainer = findViewById(R.id.image_upload_area);
    }

    private void setupCategoryDropdown() {
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
            android.R.layout.simple_dropdown_item_1line, 
            categories
        );
        
        etArtworkCategory.setAdapter(adapter);
        etArtworkCategory.setText(categories[0], false); // Set default selection
    }

    private void setupClickListeners() {
        // Click listener for the upload area
        imageUploadArea.setOnClickListener(v -> checkPermissionAndOpenGallery());

        // Click listener for the select image button
        btnSelectImage.setOnClickListener(v -> checkPermissionAndOpenGallery());

        // Click listener for the publish button
        btnPublish.setOnClickListener(v -> publishArtwork());
    }

    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE
            );
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
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied to access gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            artworkImagePreview.setImageURI(selectedImageUri);
            artworkImagePreview.setVisibility(View.VISIBLE);
            imageUploadArea.setVisibility(View.GONE);
        }
    }

    private void publishArtwork() {
        String title = etArtworkTitle.getText().toString().trim();
        String description = etArtworkDescription.getText().toString().trim();
        String price = etArtworkPrice.getText().toString().trim();
        String category = etArtworkCategory.getText().toString();

        // Validate inputs
        if (validateInputs(title, description, price, category)) {
            // Create new artwork object
            String categoryKey = convertCategoryToKey(category);
            Artwork newArtwork = new Artwork(
                title, 
                category, 
                description, 
                "$" + price, 
                categoryKey
            );

            // In a real app, you would save this to a database
            // For now, we'll simulate adding it to a shared list
            // and return to the MyArtworksActivity
            
            Toast.makeText(this, "Artwork published successfully!", Toast.LENGTH_SHORT).show();
            
            // Return to MyArtworks page
            setResult(RESULT_OK);
            finish();
        }
    }

    private boolean validateInputs(String title, String description, String price, String category) {
        if (title.isEmpty()) {
            etArtworkTitle.setError("Please enter artwork title");
            return false;
        }

        if (description.isEmpty()) {
            etArtworkDescription.setError("Please enter artwork description");
            return false;
        }

        if (price.isEmpty()) {
            etArtworkPrice.setError("Please enter artwork price");
            return false;
        }

        if (category.equals("Select Category")) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an artwork image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String convertCategoryToKey(String category) {
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