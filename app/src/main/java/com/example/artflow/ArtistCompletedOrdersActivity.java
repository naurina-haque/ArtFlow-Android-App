package com.example.artflow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ArtistCompletedOrdersActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private TextView dashboardMenu, myArtworksMenu, ordersMenu, completedOrdersMenu, profileMenu, logoutMenu;
    private RecyclerView completedOrdersRecyclerView;
    private CompletedOrderAdapter completedOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artistcompletedorders);

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        dashboardMenu = findViewById(R.id.dashboard_menu);
        myArtworksMenu = findViewById(R.id.myartworks_menu);
        ordersMenu = findViewById(R.id.orders_menu);
        completedOrdersMenu = findViewById(R.id.completed_orders_menu);
        profileMenu = findViewById(R.id.profile_menu);
        logoutMenu = findViewById(R.id.logout_menu);

        // Initialize RecyclerView for completed orders
        completedOrdersRecyclerView = findViewById(R.id.completed_orders_recycler_view);
        completedOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create sample completed order data
        List<CompletedOrder> sampleCompletedOrders = new ArrayList<>();
        sampleCompletedOrders.add(new CompletedOrder("ORD001", "John Smith", "Abstract Painting", "2023-01-15", "Completed"));
        sampleCompletedOrders.add(new CompletedOrder("ORD003", "Michael Brown", "Modern Sculpture", "2023-01-20", "Completed"));
        sampleCompletedOrders.add(new CompletedOrder("ORD006", "Jennifer Taylor", "Digital Art", "2023-01-28", "Completed"));
        sampleCompletedOrders.add(new CompletedOrder("ORD008", "David Wilson", "Watercolor Landscape", "2023-02-05", "Completed"));
        sampleCompletedOrders.add(new CompletedOrder("ORD010", "Lisa Anderson", "Portrait Art", "2023-02-10", "Completed"));

        // Set up adapter
        completedOrderAdapter = new CompletedOrderAdapter(sampleCompletedOrders);
        completedOrdersRecyclerView.setAdapter(completedOrderAdapter);

        // Highlight current page (Completed Orders)
        setActiveMenuItem(completedOrdersMenu);

        // Set click listener for menu icon to open drawer
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(findViewById(R.id.side_navigation));
            }
        });

        // Set click listeners for menu items
        dashboardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to dashboard
                setActiveMenuItem(dashboardMenu);
                startActivity(new Intent(ArtistCompletedOrdersActivity.this, ArtistDashboard.class));
                finish();
                drawerLayout.closeDrawers();
            }
        });

        myArtworksMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to My Artworks page
                setActiveMenuItem(myArtworksMenu);
                startActivity(new Intent(ArtistCompletedOrdersActivity.this, MyArtworksActivity.class));
                finish();
                drawerLayout.closeDrawers();
            }
        });

        ordersMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Orders page
                // For now, we'll stay on the current page since OrdersActivity is not implemented
                // In the future, uncomment when OrdersActivity is created
                // setActiveMenuItem(ordersMenu);
                // startActivity(new Intent(ArtistCompletedOrdersActivity.this, OrdersActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        completedOrdersMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stay on the same page (already here)
                setActiveMenuItem(completedOrdersMenu);
                drawerLayout.closeDrawers();
            }
        });

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Profile page
                // setActiveMenuItem(profileMenu);
                // startActivity(new Intent(ArtistCompletedOrdersActivity.this, ProfileActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        logoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Select page
                startActivity(new Intent(ArtistCompletedOrdersActivity.this, Select.class));
                finish();
            }
        });
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