package com.example.artflow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MyArtworksActivity extends AppCompatActivity {

    private static final int ADD_ARTWORK_REQUEST = 1;

    private DrawerLayout drawerLayout;
    private Button addArtworkButton;
    private ImageView menuIcon;
    private TextView dashboardMenu, myArtworksMenu, ordersMenu, completedOrdersMenu, profileMenu, logoutMenu;
    private RecyclerView artworksRecyclerView;
    private ArtworkAdapter artworkAdapter;
    
    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myartworks);

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        addArtworkButton = findViewById(R.id.add_artwork_button);
        menuIcon = findViewById(R.id.menu_icon);
        dashboardMenu = findViewById(R.id.dashboard_menu);
        myArtworksMenu = findViewById(R.id.myartworks_menu);
        ordersMenu = findViewById(R.id.orders_menu);
        completedOrdersMenu = findViewById(R.id.completed_orders_menu);
        profileMenu = findViewById(R.id.profile_menu);
        logoutMenu = findViewById(R.id.logout_menu);

        // Initialize RecyclerView for artworks with GridLayoutManager
        artworksRecyclerView = findViewById(R.id.artworks_container);
        // Use 2 columns for grid layout
        artworksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        
        // Create sample artwork data
        // In a real app, this would come from a database or API
        artworkAdapter = new ArtworkAdapter(createSampleArtworks());
        artworksRecyclerView.setAdapter(artworkAdapter);

        // Highlight current page (My Artworks)
        setActiveMenuItem(myArtworksMenu);

        // Set click listener for menu icon to open drawer
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(findViewById(R.id.side_navigation));
            }
        });

        // Set click listener for add artwork button
        addArtworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddArtworkActivity
                Intent intent = new Intent(MyArtworksActivity.this, AddArtworkActivity.class);
                startActivityForResult(intent, ADD_ARTWORK_REQUEST);
            }
        });

        // Set click listeners for menu items
        dashboardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to dashboard
                setActiveMenuItem(dashboardMenu);
                startActivity(new Intent(MyArtworksActivity.this, ArtistDashboard.class));
                finish();
                drawerLayout.closeDrawers();
            }
        });

        myArtworksMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stay on the same page (already here)
                setActiveMenuItem(myArtworksMenu);
                drawerLayout.closeDrawers();
            }
        });

        ordersMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to orders page (you'll need to create this)
                // setActiveMenuItem(ordersMenu);
                // startActivity(new Intent(MyArtworksActivity.this, OrdersActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        completedOrdersMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Completed Orders page
                setActiveMenuItem(completedOrdersMenu);
                startActivity(new Intent(MyArtworksActivity.this, ArtistCompletedOrdersActivity.class));
                finish();
                drawerLayout.closeDrawers();
            }
        });

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Profile page (you'll need to create this)
                // setActiveMenuItem(profileMenu);
                // startActivity(new Intent(MyArtworksActivity.this, ProfileActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        logoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Select page
                startActivity(new Intent(MyArtworksActivity.this, Select.class));
                finish();
            }
        });

        // Set up chip click listeners
        setupChipListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ARTWORK_REQUEST && resultCode == RESULT_OK) {
            // Refresh the artwork list if needed
            // In a real app, you would add the newly created artwork to the list
        }
    }

    private void setupChipListeners() {
        TextView chipAll = findViewById(R.id.chip_all);
        TextView chipColoredPortraits = findViewById(R.id.chip_colored_portraits);
        TextView chipBwPortraits = findViewById(R.id.chip_bw_portraits);
        TextView chipWatercolor = findViewById(R.id.chip_watercolor);
        TextView chipDigital = findViewById(R.id.chip_digital);
        TextView chipAbstract = findViewById(R.id.chip_abstract);
        TextView chipLandscape = findViewById(R.id.chip_landscape);
        TextView chipLine = findViewById(R.id.chip_line);
        TextView chipAcrylic = findViewById(R.id.chip_acrylic);

        // Set initial selected state for "All" chip
        updateChipSelection(chipAll, true);

        // Set click listeners for all chips
        chipAll.setOnClickListener(v -> selectChip(chipAll));
        chipColoredPortraits.setOnClickListener(v -> selectChip(chipColoredPortraits));
        chipBwPortraits.setOnClickListener(v -> selectChip(chipBwPortraits));
        chipWatercolor.setOnClickListener(v -> selectChip(chipWatercolor));
        chipDigital.setOnClickListener(v -> selectChip(chipDigital));
        chipAbstract.setOnClickListener(v -> selectChip(chipAbstract));
        chipLandscape.setOnClickListener(v -> selectChip(chipLandscape));
        chipLine.setOnClickListener(v -> selectChip(chipLine));
        chipAcrylic.setOnClickListener(v -> selectChip(chipAcrylic));
    }

    private void selectChip(TextView selectedChip) {
        // Reset all chips to unselected state
        resetAllChips();

        // Update selected chip
        updateChipSelection(selectedChip, true);
        
        // Filter artworks based on selected chip
        String selectedCategory = selectedChip.getText().toString();
        artworkAdapter.filterByCategory(selectedCategory);
    }

    private void resetAllChips() {
        String[] chipIds = {
            "chip_all", "chip_colored_portraits", "chip_bw_portraits", 
            "chip_watercolor", "chip_digital", "chip_abstract", "chip_landscape", "chip_line", "chip_acrylic"
        };

        for (String chipId : chipIds) {
            int resourceId = getResources().getIdentifier(chipId, "id", getPackageName());
            TextView chip = findViewById(resourceId);
            if (chip != null) {
                updateChipSelection(chip, false);
            }
        }
    }

    private void updateChipSelection(TextView chip, boolean isSelected) {
        if (isSelected) {
            chip.setBackgroundResource(R.drawable.chip_background_selected);
            chip.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            chip.setBackgroundResource(R.drawable.chip_background);
            chip.setTextColor(0xFF6C179F); // Using the app's purple color
        }
    }

    // Sample data creation method
    private java.util.List<Artwork> createSampleArtworks() {
        java.util.List<Artwork> artworks = new java.util.ArrayList<>();
        
        artworks.add(new Artwork("Sunset Landscape", "Landscape Art", "Beautiful sunset over mountains", "$199.99", "landscape"));
        artworks.add(new Artwork("Portrait of Jane", "Colored Portraits", "Colorful portrait painting", "$149.99", "colored_portraits"));
        artworks.add(new Artwork("Digital Dreams", "Digital Art", "Modern digital art piece", "$89.99", "digital"));
        artworks.add(new Artwork("Black & White Portrait", "B&W Portraits", "Classic black and white portrait", "$129.99", "bw_portraits"));
        artworks.add(new Artwork("Ocean Watercolor", "Watercolor Art", "Peaceful ocean scene in watercolor", "$99.99", "watercolor"));
        artworks.add(new Artwork("Abstract Colors", "Abstract Art", "Vibrant abstract art piece", "$179.99", "abstract"));
        artworks.add(new Artwork("City Line Art", "Line Art", "Detailed cityscape in line art style", "$79.99", "line"));
        artworks.add(new Artwork("Floral Acrylic", "Acrylic Art", "Beautiful floral painting in acrylic", "$159.99", "acrylic"));
        
        return artworks;
    }
    
    private void setActiveMenuItem(TextView activeMenu) {
        // Reset all menu items to default color
        dashboardMenu.setTextColor(Color.WHITE);
        myArtworksMenu.setTextColor(Color.WHITE);
        ordersMenu.setTextColor(Color.WHITE);
        completedOrdersMenu.setTextColor(Color.WHITE);
        profileMenu.setTextColor(Color.WHITE);
        
        // Set active menu item to a different color (highlight it)
        activeMenu.setTextColor(Color.YELLOW); // Or any other color you prefer for highlighting
    }
}