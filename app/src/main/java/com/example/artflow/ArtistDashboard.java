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

public class ArtistDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private TextView dashboardMenu, myArtworksMenu, ordersMenu, completedOrdersMenu, profileMenu, logoutMenu;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    
    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artistdashboard);
        
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
        
        // Initialize RecyclerView for orders
        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false; // Disable vertical scrolling to work with ScrollView
            }
        });
        ordersRecyclerView.setNestedScrollingEnabled(false); // Disable nested scrolling to work with ScrollView
        
        // Load orders from Firebase
        loadOrdersFromFirebase();

        // Highlight current page (Dashboard)
        setActiveMenuItem(dashboardMenu);

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
                // Stay on the same page (already here)
                setActiveMenuItem(dashboardMenu);
                drawerLayout.closeDrawers();
            }
        });

        myArtworksMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to My Artworks page (you'll need to create this)
                setActiveMenuItem(myArtworksMenu);
                startActivity(new Intent(ArtistDashboard.this, MyArtworksActivity.class));
                finish();
                drawerLayout.closeDrawers();
            }
        });

        ordersMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Orders page (you'll need to create this)
                // setActiveMenuItem(ordersMenu);
                // startActivity(new Intent(ArtistDashboard.this, OrdersActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        completedOrdersMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Completed Orders page
                setActiveMenuItem(completedOrdersMenu);
                startActivity(new Intent(ArtistDashboard.this, ArtistCompletedOrdersActivity.class));
                finish();
                drawerLayout.closeDrawers();
            }
        });

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Profile page (you'll need to create this)
                // setActiveMenuItem(profileMenu);
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
    
    private void loadOrdersFromFirebase() {
        // Get the current user's ID to load their specific orders
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        
        if (currentUserId != null) {
            // Reference to orders for the current artist
            DatabaseReference ordersRef = mDatabase.child("orders").orderByChild("artistId").equalTo(currentUserId);
            
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Order> ordersList = new ArrayList<>();
                    
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            ordersList.add(order);
                        }
                    }
                    
                    // Update the adapter with the loaded orders
                    if (orderAdapter == null) {
                        orderAdapter = new OrderAdapter(ordersList);
                        ordersRecyclerView.setAdapter(orderAdapter);
                    } else {
                        orderAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors
                    System.out.println("loadOrders:onCancelled " + databaseError.toException());
                }
            });
        } else {
            // If user is not authenticated, create an empty list
            List<Order> emptyOrdersList = new ArrayList<>();
            orderAdapter = new OrderAdapter(emptyOrdersList);
            ordersRecyclerView.setAdapter(orderAdapter);
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