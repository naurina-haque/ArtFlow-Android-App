package com.example.artflow;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;
    
    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        // Initialize with your specific Firebase Realtime Database URL
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://artflow-55038-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabase = database.getReference();
        mFirestore = FirebaseFirestore.getInstance();
    }
    
    // Method to register a new user
    public void registerUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }
    
    // Method to sign in a user
    public void signInUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }
    
    // Method to sign out the current user
    public void signOut() {
        mAuth.signOut();
    }
    
    // Method to get current user
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
    
    // Method to check if user is signed in
    public boolean isUserSignedIn() {
        return mAuth.getCurrentUser() != null;
    }
    
    // Method to send password reset email
    public void sendPasswordResetEmail(String email, OnCompleteListener<Void> onCompleteListener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(onCompleteListener);
    }
    
    // Method to get database reference
    public DatabaseReference getDatabaseReference() {
        return mDatabase;
    }
    
    // Method to get Firestore instance
    public FirebaseFirestore getFirestore() {
        return mFirestore;
    }
    
    // Method to save artist data to Realtime Database
    public void saveArtistData(String userId, String name, String email) {
        DatabaseReference artistsRef = mDatabase.child("artists").child(userId);
        
        // Create a map of the artist data
        Artist artist = new Artist(userId, name, email);
        artistsRef.setValue(artist);
    }
    
    // Method to save customer data to Realtime Database
    public void saveCustomerData(String userId, String name, String email) {
        DatabaseReference customersRef = mDatabase.child("customers").child(userId);
        
        // Create a map of the customer data
        Customer customer = new Customer(userId, name, email);
        customersRef.setValue(customer);
    }
    
    // Method to save artwork data to Realtime Database
    public void saveArtworkData(String artworkId, String artistId, String title, String description, String price) {
        DatabaseReference artworksRef = mDatabase.child("artworks").child(artworkId);

        Artwork artwork = new Artwork(artworkId, title, "", description, price, "", artistId, "");
        artworksRef.setValue(artwork);
    }
    
    // Method to save order data to Realtime Database
    public void saveOrderData(Order order) {
        DatabaseReference ordersRef = mDatabase.child("orders").child(order.getOrderId());
        ordersRef.setValue(order);
    }
    
    // Method to retrieve all orders for an artist
    public com.google.firebase.database.Query getArtistOrders(String artistId) {
        return mDatabase.child("orders").orderByChild("artistId").equalTo(artistId);
    }
    
    // Method to retrieve all artworks for an artist
    public com.google.firebase.database.Query getArtistArtworks(String artistId) {
        return mDatabase.child("artworks").orderByChild("artistId").equalTo(artistId);
    }
}