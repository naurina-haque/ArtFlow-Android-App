package com.example.artflow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerDashboard extends AppCompatActivity {

    private static final String TAG = "CustomerDashboard";

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private TextView welcomeCustomerText;
    private TextView dashboardMenu, myOrdersMenu, profileMenu, logoutMenu;
    private RecyclerView artworksRecyclerView;
private ArtworkAdapter artworkAdapter;

    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerdashboard);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(
                        "https://artflow-55038-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        welcomeCustomerText = findViewById(R.id.welcome_customer_text);
        dashboardMenu = findViewById(R.id.dashboard_menu);
        myOrdersMenu = findViewById(R.id.my_orders_menu);

        profileMenu = findViewById(R.id.profile_menu);
        logoutMenu = findViewById(R.id.logout_menu);

        // Setwelcomemessage
        String customerName = getCurrentCustomerName();
        welcomeCustomerText.setText("Welcome, " + customerName);

        // Initialize RecyclerView with GridLayoutManager
        artworksRecyclerView = findViewById(R.id.artworks_container);
        artworksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        artworkAdapter = new ArtworkAdapter(new ArrayList<>(), true); // customer view
        artworksRecyclerView.setAdapter(artworkAdapter);

        // Load artworks from Firebase
        loadArtworksFromFirebase();

        // Highlight current page (Dashboard)
        setActiveMenuItem(dashboardMenu);

        // Menu icon click
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.LEFT));

// Menu items click
        dashboardMenu.setOnClickListener(v -> {
            setActiveMenuItem(dashboardMenu);
            drawerLayout.closeDrawers();
        });

        myOrdersMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to My Orders page
                setActiveMenuItem(myOrdersMenu);
                startActivity(new Intent(CustomerDashboard.this, CustomerMyOrdersActivity.class));
                drawerLayout.closeDrawers();
            }
        });



        profileMenu.setOnClickListener(v -> {
            // Navigate to ProfileActivity
            setActiveMenuItem(profileMenu);
            startActivity(new Intent(CustomerDashboard.this, ProfileActivity.class));
            drawerLayout.closeDrawers();
        });

        logoutMenu.setOnClickListener(v -> {
            // Logout and navigate to Select page
            startActivity(new Intent(CustomerDashboard.this, Select.class));
            finish();
        });

        // Set up chip click listeners
        setupChipListeners();
    }

   private void loadArtworksFromFirebase() {
        Log.d(TAG, "Loading artworks from Firebase");

        mDatabase.child("artworks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Artwork> artworks = new ArrayList<>();

                for (DataSnapshot artworkSnapshot: dataSnapshot.getChildren()) {
                    Artwork artwork = artworkSnapshot.getValue(Artwork.class);
                    if (artwork != null) {
                        artworks.add(artwork);
                    }
                }

                Log.d(TAG, "Loaded " + artworks.size() + " artworks from Firebase");

                runOnUiThread(() -> {
                    if (artworkAdapter != null) {
                        artworkAdapter.updateArtworksList(artworks);
                    } else {
                        artworkAdapter = new ArtworkAdapter(artworks, true);
                        artworksRecyclerView.setAdapter(artworkAdapter);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "loadArtworks:onCancelled", databaseError.toException());
            }
        });
    }

    private String getCurrentCustomerName() {
        // Get the current user from Firebase Auth
        com.google.firebase.auth.FirebaseUser currentUser = mAuth.getCurrentUser();
        
        if (currentUser != null) {
            // Try to get display name first
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                return displayName;
            }
            
            // If no display name, try to extract from email
            String email = currentUser.getEmail();
            if (email != null && !email.isEmpty()) {
                // Extract the part before @ symbol
                int atIndex = email.indexOf('@');
                if (atIndex > 0) {
                    return email.substring(0, atIndex);
                }
                return email; // fallback to full email
            }
            
            // If no email, use UID as last resort
            return currentUser.getUid().substring(0, Math.min(currentUser.getUid().length(), 8));
        }
        
        // Fallback if no user is logged in
        return "Customer";
    }

    private void setupChipListeners(){
        TextView chipAll = findViewById(R.id.chip_all);
        TextView chipColoredPortraits = findViewById(R.id.chip_colored_portraits);
        TextView chipBwPortraits = findViewById(R.id.chip_bw_portraits);
        TextView chipWatercolor = findViewById(R.id.chip_watercolor);
        TextView chipDigital = findViewById(R.id.chip_digital);
        TextView chipAbstract = findViewById(R.id.chip_abstract);
        TextView chipLandscape = findViewById(R.id.chip_landscape);
TextView chipLine = findViewById(R.id.chip_line);
        TextView chipAcrylic = findViewById(R.id.chip_acrylic);

        // Initial selection
        updateChipSelection(chipAll, true);

        // Click listeners
        chipAll.setOnClickListener(v -> selectChip(chipAll));
        chipColoredPortraits.setOnClickListener(v ->selectChip(chipColoredPortraits));
        chipBwPortraits.setOnClickListener(v -> selectChip(chipBwPortraits));
        chipWatercolor.setOnClickListener(v -> selectChip(chipWatercolor));
        chipDigital.setOnClickListener(v -> selectChip(chipDigital));
        chipAbstract.setOnClickListener(v -> selectChip(chipAbstract));
        chipLandscape.setOnClickListener(v ->selectChip(chipLandscape));
        chipLine.setOnClickListener(v -> selectChip(chipLine));
        chipAcrylic.setOnClickListener(v -> selectChip(chipAcrylic));
    }

    private void selectChip(TextView selectedChip) {
        resetAllChips();
        updateChipSelection(selectedChip, true);

        String selectedCategory =selectedChip.getText().toString();
        artworkAdapter.filterByCategory(selectedCategory);
    }

    private void resetAllChips() {
        String[] chipIds = {
                "chip_all", "chip_colored_portraits", "chip_bw_portraits",
                "chip_watercolor", "chip_digital", "chip_abstract",
               "chip_landscape", "chip_line", "chip_acrylic"
        };

        for (String chipId : chipIds) {
            int resourceId = getResources().getIdentifier(chipId, "id", getPackageName());
            TextView chip = findViewById(resourceId);
            if (chip != null) updateChipSelection(chip, false);
        }
    }

    private void updateChipSelection(TextView chip, boolean isSelected) {
        if (isSelected) {
            chip.setBackgroundResource(R.drawable.chip_background_selected);
            chip.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            chip.setBackgroundResource(R.drawable.chip_background);
            chip.setTextColor(0xFF6C179F); // app purple
        }
    }

    private void setActiveMenuItem(TextView activeMenu) {
        dashboardMenu.setTextColor(Color.WHITE);
        myOrdersMenu.setTextColor(Color.WHITE);
        profileMenu.setTextColor(Color.WHITE);

        activeMenu.setTextColor(Color.YELLOW); //Highlight active
    }
}