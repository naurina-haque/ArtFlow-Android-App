package com.example.artflow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistCompletedOrdersActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private TextView dashboardMenu, myArtworksMenu, ordersMenu, completedOrdersMenu, profileMenu, logoutMenu;
    private RecyclerView completedOrdersRecyclerView;
    private OrderAdapter completedOrderAdapter; // Changed to use OrderAdapter
    private List<Order> completedOrdersList; // Changed to use Order instead of CompletedOrder
    
    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artistcompletedorders);
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://artflow-55038-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

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
        completedOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false; // Disable vertical scrolling to work with ScrollView
            }
        });
        completedOrdersRecyclerView.setNestedScrollingEnabled(false); // Disable nested scrolling to work with ScrollView
        
        // Initialize completed orders list
        final List<Order> completedOrdersList = new ArrayList<>();
        this.completedOrdersList = completedOrdersList;

        // Load completed orders from Firebase
        loadCompletedOrdersFromFirebase();

        // Highlight current page (Completed Orders)
        setActiveMenuItem(completedOrdersMenu);

        // Set click listener for menu icon to open drawer
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
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
                setActiveMenuItem(profileMenu);
                startActivity(new Intent(ArtistCompletedOrdersActivity.this, ProfileActivity.class));
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
    
    private void loadCompletedOrdersFromFirebase() {
        // Get the current user's ID to load their specific orders
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        
        if (currentUserId != null) {
            // Reference to completed orders for the current artist
            mDatabase.child("orders").orderByChild("artistId").equalTo(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        completedOrdersList.clear();
                        
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            Order order = orderSnapshot.getValue(Order.class);
                            if (order != null && ("Completed".equals(order.getStatus()) || "Rejected".equals(order.getStatus()))) { // Only load completed or rejected orders
                                completedOrdersList.add(order);
                            }
                        }
                        
                        // Update the adapter with the loaded completed orders
                        if (completedOrderAdapter == null) {
                            completedOrderAdapter = new OrderAdapter(completedOrdersList);
                            completedOrdersRecyclerView.setAdapter(completedOrderAdapter);
                        } else {
                            completedOrderAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        System.out.println("loadCompletedOrders:onCancelled " + databaseError.toException());
                    }
                });
        } else {
            // If user is not authenticated, clear the list
            completedOrdersList.clear();
            if (completedOrderAdapter == null) {
                completedOrderAdapter = new OrderAdapter(completedOrdersList);
                completedOrdersRecyclerView.setAdapter(completedOrderAdapter);
            } else {
                completedOrderAdapter.notifyDataSetChanged();
            }
        }
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