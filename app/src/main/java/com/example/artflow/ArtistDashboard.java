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
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ArtistDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private TextView welcomeArtistText;
    private TextView totalArtworksValue, totalOrdersValue, earnedMoneyValue;
    private TextView dashboardMenu, myArtworksMenu, completedOrdersMenu,profileMenu, logoutMenu;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
   private List<Order> ordersList;

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
        welcomeArtistText = findViewById(R.id.welcome_artist_text);
        totalArtworksValue = findViewById(R.id.total_artworks_value);
        totalOrdersValue = findViewById(R.id.total_orders_value);
        earnedMoneyValue = findViewById(R.id.earned_money_value);
        dashboardMenu = findViewById(R.id.dashboard_menu);
myArtworksMenu = findViewById(R.id.myartworks_menu);

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
        ordersRecyclerView.setNestedScrollingEnabled(false);// Disable nested scrolling to work with ScrollView// Initialize orders list
        ordersList = new ArrayList<>();

        // Load orders from Firebase
        loadOrdersFromFirebase();

        // Load and update dashboard statistics
        updateDashboardStats();

        // Set welcome message
        String artistName = getCurrentArtistName();
        welcomeArtistText.setText("Welcome, " +artistName);

        // Highlight current page (Dashboard)
        setActiveMenuItem(dashboardMenu);

        // Set click listener for menu icon to open drawer
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //Set click listeners for menu items
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
                // Navigate to Profile page
                setActiveMenuItem(profileMenu);
                startActivity(new Intent(ArtistDashboard.this, ProfileActivity.class));
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

    private void updateDashboardStats() {
        // Load total artworks count
        loadTotalArtworksCount();

        // Load total orders count
        loadTotalOrdersCount();

        // Load earned money
        loadEarnedMoney();
    }

    private void loadTotalArtworksCount() {
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (currentUserId != null) {
            mDatabase.child("artworks").orderByChild("artistId").equalTo(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        totalArtworksValue.setText(String.valueOf(count));
}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        System.out.println("loadTotalArtworksCount:onCancelled " + databaseError.toException());
                    }
                });
        }
    }

    private void loadTotalOrdersCount() {
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (currentUserId != null) {
            mDatabase.child("orders").orderByChild("artistId").equalTo(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        totalOrdersValue.setText(String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        System.out.println("loadTotalOrdersCount:onCancelled " + databaseError.toException());
                    }
                });
        }
    }

    private void loadEarnedMoney() {
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (currentUserId != null) {
            mDatabase.child("orders").orderByChild("artistId").equalTo(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> completedOrderArtworkIds = new ArrayList<>();

                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            Order order = orderSnapshot.getValue(Order.class);
                            if (order != null && "Completed".equals(order.getStatus())) {
                                String artworkId = order.getArtworkId();
                                if (artworkId!= null) {
                                    completedOrderArtworkIds.add(artworkId);
                                }
                            }
                        }

                        // Calculate total earnings by fetching each artwork's price
                        calculateTotalEarnings(completedOrderArtworkIds, 0, 0.0);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        System.out.println("loadEarnedMoney:onCancelled " + databaseError.toException());
                    }
                });
        }
    }

    private void calculateTotalEarnings(List<String> artworkIds, int index, double totalEarned) {
        if (index >= artworkIds.size()) {
            // All artwork prices have been processed
            earnedMoneyValue.setText("$" + String.format("%.2f", totalEarned));
            return;
        }

        String artworkId = artworkIds.get(index);
        mDatabase.child("artworks").child(artworkId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot artworkSnapshot) {
                double newTotalEarned = totalEarned; // Create a local copy
                Artwork artwork = artworkSnapshot.getValue(Artwork.class);
                if (artwork != null && artwork.getPrice() != null) {
                    String priceStr = artwork.getPrice();
                    try {
                        // Remove currency symbols and parse the number
                        String cleanPrice = priceStr.replaceAll("[^0-9.]+", "");
                        double price = Double.parseDouble(cleanPrice);
                        newTotalEarned += price; // Modify the local copy
                    } catch (NumberFormatException e) {
                        // Handle case where price cannot be parsed
                        System.out.println("Error parsing price: " + priceStr);
                    }
                }

                // Process next artwork
                calculateTotalEarnings(artworkIds, index+ 1, newTotalEarned);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error loading artwork: " + databaseError.getMessage());
                // Continue with next artwork even if one fails
                calculateTotalEarnings(artworkIds, index + 1, totalEarned);
}
});
    }

    private void loadOrdersFromFirebase() {
        // Get the current user's ID to load their specific orders
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (currentUserId != null) {
            // Reference to orders for the current artist(pending orders)
            mDatabase.child("orders").orderByChild("artistId").equalTo(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ordersList.clear();

                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            Order order =orderSnapshot.getValue(Order.class);
                            if (order != null && !"Completed".equals(order.getStatus()) && !"Rejected".equals(order.getStatus())) { // Only load non-completed and non-rejected orders
                                ordersList.add(order);
                            }
                        }

                        // Update the adapter with the loaded orders
                        if (orderAdapter== null) {
                            orderAdapter = new OrderAdapter(ordersList);

                            // Set up callbacks for accept and reject actions
                            orderAdapter.setOnOrderAcceptedListener(new OrderAdapter.OnOrderAcceptedListener() {
                                @Override
                                public void onOrderAccepted(Order order, int position) {
                                    // Updateorder status toaccepted in Firebase
                                    updateOrderStatusInFirebase(order, "Accepted");
                                }
                            });

                            orderAdapter.setOnOrderCanceledListener(new OrderAdapter.OnOrderCanceledListener() {
                                @Override
                                public void onOrderCanceled(Order order, int position) {
                                    // Update order status to rejected in Firebase
                                    updateOrderStatusInFirebase(order, "Rejected");
                                }
                            });

                            ordersRecyclerView.setAdapter(orderAdapter);
                        } else {
                            orderAdapter.notifyDataSetChanged();
                        }

                        // Update the total orders count
                        totalOrdersValue.setText(String.valueOf(ordersList.size()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){
                        // Handle possible errors
                        System.out.println("loadOrders:onCancelled " + databaseError.toException());
                    }
                });
        } else {
            // If user is not authenticated, clear the list
            ordersList.clear();
            if (orderAdapter == null) {
                orderAdapter = new OrderAdapter(ordersList);
                ordersRecyclerView.setAdapter(orderAdapter);
            } else {
                orderAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateOrderStatusInFirebase(Order order, String newStatus) {
        // Update the order status in Firebase
        String orderId = order.getOrderId();
        DatabaseReference orderRef = mDatabase.child("orders").child(orderId);

        // When artist accepts an order, it should be marked as Completed
        // This will make it move from recent orders to completed orders
        if ("Accepted".equals(newStatus)) {
            newStatus = "Completed";
        }

        orderRef.child("status").setValue(newStatus);
    }

    private void removeOrderFromFirebase(Order order) {
        // Remove the order from Firebase
        String orderId= order.getOrderId();
        DatabaseReference orderRef = mDatabase.child("orders").child(orderId);
        orderRef.removeValue();
    }

    private String getCurrentArtistName() {
        // Getthe current user from Firebase Auth
        com.google.firebase.auth.FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Try to get display name first
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                return displayName;
            }
            
            //If no display name, try to extract from email
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
        return"Artist";
    }
    
    private void setActiveMenuItem(TextView activeMenu) {
        // Reset all menu items to default color
        dashboardMenu.setTextColor(Color.WHITE);
        myArtworksMenu.setTextColor(Color.WHITE);
        completedOrdersMenu.setTextColor(Color.WHITE);
        profileMenu.setTextColor(Color.WHITE);
        
        // Set active menu itemto a different color (highlight it)
        activeMenu.setTextColor(Color.YELLOW); // Or any other color you prefer for highlighting
    }
}
