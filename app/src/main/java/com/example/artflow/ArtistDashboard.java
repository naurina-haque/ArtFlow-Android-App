package com.example.artflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ArtistDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private TextView dashboardMenu, myArtworksMenu, ordersMenu, profileMenu, logoutMenu;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artistdashboard);

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        dashboardMenu = findViewById(R.id.dashboard_menu);
        myArtworksMenu = findViewById(R.id.myartworks_menu);
        ordersMenu = findViewById(R.id.orders_menu);
        profileMenu = findViewById(R.id.profile_menu);
        logoutMenu = findViewById(R.id.logout_menu);
        
        // Initialize RecyclerView for orders
        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        
        // Use horizontal LinearLayoutManager to work with the horizontal scroll
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false; // Disable vertical scrolling to work with ScrollView
            }
        };
        ordersRecyclerView.setLayoutManager(layoutManager);
        ordersRecyclerView.setNestedScrollingEnabled(false); // Disable nested scrolling to work with ScrollView
        
        // Create sample order data
        List<Order> sampleOrders = new ArrayList<>();
        sampleOrders.add(new Order("ORD001", "John Smith", "Abstract Painting", "2023-01-15", "Completed"));
        sampleOrders.add(new Order("ORD002", "Emma Johnson", "Landscape Art", "2023-01-18", "Pending"));
        sampleOrders.add(new Order("ORD003", "Michael Brown", "Modern Sculpture", "2023-01-20", "Shipped"));
        sampleOrders.add(new Order("ORD004", "Sarah Davis", "Watercolor Sunset", "2023-01-22", "Processing"));
        sampleOrders.add(new Order("ORD005", "Robert Wilson", "Oil Portrait", "2023-01-25", "Cancelled"));
        sampleOrders.add(new Order("ORD006", "Jennifer Taylor", "Digital Art", "2023-01-28", "Completed"));
        
        // Set up adapter
        orderAdapter = new OrderAdapter(sampleOrders);
        ordersRecyclerView.setAdapter(orderAdapter);

        // Set click listener for menu icon to open drawer
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(findViewById(R.id.dashboard_layout))) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.dashboard_layout));
                }
            }
        });

        // Set click listeners for menu items
        dashboardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stay on the same page or refresh
                drawerLayout.closeDrawers();
            }
        });

        myArtworksMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to My Artworks page
                startActivity(new Intent(ArtistDashboard.this, MyArtworksActivity.class));
                finish();
                drawerLayout.closeDrawers();
            }
        });

        ordersMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Orders page (you'll need to create this)
                // startActivity(new Intent(ArtistDashboard.this, OrdersActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Profile page (you'll need to create this)
                // startActivity(new Intent(ArtistDashboard.this, ProfileActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        logoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Select page
                startActivity(new Intent(ArtistDashboard.this, Select.class));
                finish();
            }
        });
    }
}