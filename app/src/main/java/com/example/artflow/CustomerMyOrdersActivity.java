package com.example.artflow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
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

public class CustomerMyOrdersActivity extends AppCompatActivity {

    private static final String TAG = "CustomerMyOrdersActivity";
    
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private TextView dashboardMenu, myOrdersMenu, profileMenu, logoutMenu;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    
    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_my_orders); // We'll create this layout

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://artflow-55038-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        dashboardMenu = findViewById(R.id.dashboard_menu);
        myOrdersMenu = findViewById(R.id.my_orders_menu);

        profileMenu = findViewById(R.id.profile_menu);
        logoutMenu = findViewById(R.id.logout_menu);

        // Initialize RecyclerView for orders
        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with empty list
        orderAdapter = new OrderAdapter(new ArrayList<>());
        ordersRecyclerView.setAdapter(orderAdapter);

        // Load orders from Firebase
        loadOrdersFromFirebase();

        // Highlight current page (My Orders)
        setActiveMenuItem(myOrdersMenu);

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
                startActivity(new Intent(CustomerMyOrdersActivity.this, CustomerDashboard.class));
                finish();
                drawerLayout.closeDrawers();
            }
        });

        myOrdersMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stay on the same page (already here)
                setActiveMenuItem(myOrdersMenu);
                drawerLayout.closeDrawers();
            }
        });



        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Profile page
                setActiveMenuItem(profileMenu);
                startActivity(new Intent(CustomerMyOrdersActivity.this, ProfileActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        logoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Select page
                startActivity(new Intent(CustomerMyOrdersActivity.this, Select.class));
                finish();
            }
        });
    }

    private void loadOrdersFromFirebase() {
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        
        Log.d(TAG, "Loading orders for customer ID: " + currentUserId);
        
        if (currentUserId != null) {
            // Reference to orders for the current customer
            mDatabase.child("orders").orderByChild("customerId").equalTo(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Order> orders = new ArrayList<>();
                        
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            Order order = orderSnapshot.getValue(Order.class);
                            if (order != null) {
                                orders.add(order);
                            }
                        }
                        
                        Log.d(TAG, "Loaded " + orders.size() + " orders from Firebase");
                        
                        // Update the adapter with loaded orders
                        orderAdapter = new OrderAdapter(orders);
                        ordersRecyclerView.setAdapter(orderAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        Log.e(TAG, "loadOrders:onCancelled", databaseError.toException());
                    }
                });
        }
    }

    private void setActiveMenuItem(TextView activeMenu) {
        // Reset all menu items to default color
        dashboardMenu.setTextColor(Color.WHITE);
        myOrdersMenu.setTextColor(Color.WHITE);

        profileMenu.setTextColor(Color.WHITE);
        
        // Set active menu item to a different color (highlight it)
        activeMenu.setTextColor(Color.YELLOW); // Or any other color you prefer for highlighting
    }
}