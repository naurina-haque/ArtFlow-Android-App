package com.example.artflow;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    
    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    
    // Callback interfaces for accept and cancel actions
    private OnOrderAcceptedListener acceptCallback;
    private OnOrderCanceledListener cancelCallback;
    
    // Interface for accept callback
    public interface OnOrderAcceptedListener {
        void onOrderAccepted(Order order, int position);
    }
    
    // Interface for cancel callback
    public interface OnOrderCanceledListener {
        void onOrderCanceled(Order order, int position);
    }
    
    // Setter methods for callbacks
    public void setOnOrderAcceptedListener(OnOrderAcceptedListener listener) {
        this.acceptCallback = listener;
    }
    
    public void setOnOrderCanceledListener(OnOrderCanceledListener listener) {
        this.cancelCallback = listener;
    }

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://artflow-55038-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        
        // Set order details
        holder.orderId.setText("Order ID: " + order.getOrderId());
        holder.artworkTitle.setText(order.getArtworkTitle());
        holder.status.setText(order.getStatus());
        
        // Set artist name
        String artistName = order.getArtistName();
        holder.artistName.setText(artistName != null ? artistName : "Unknown Artist");
        
        // Set status color based on status value
        setStatusColor(holder.status, order.getStatus());
        
        // For artist view, show accept/cancel buttons; for customer view, hide them or show different options
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";
        boolean isArtistView = order.getArtistId().equals(currentUserId); // If current user is the artist
        
        if (isArtistView) {
            // For completed orders, don't show accept/reject buttons
            if ("Completed".equals(order.getStatus()) || "Rejected".equals(order.getStatus())) {
                // Hide buttons for completed/rejected orders
                holder.acceptButton.setVisibility(View.GONE);
                holder.cancelButton.setVisibility(View.GONE);
            } else {
                // Show accept/reject buttons for pending orders
                holder.acceptButton.setVisibility(View.VISIBLE);
                holder.cancelButton.setVisibility(View.VISIBLE);
                
                // Set up accept button click listener
                holder.acceptButton.setOnClickListener(v -> {
                    // Update order status to accepted in Firebase
                    String orderId = order.getOrderId();
                    mDatabase.child("orders").child(orderId).child("status").setValue("Accepted");
                    
                    // Update local order status
                    order.setStatus("Accepted");
                    holder.status.setText("Accepted");
                    setStatusColor(holder.status, "Accepted");
                    
                    // Call the callback to handle the acceptance
                    if (acceptCallback != null) {
                        acceptCallback.onOrderAccepted(order, position);
                    }
                });
                
                // Set up reject button click listener
                holder.cancelButton.setOnClickListener(v -> {
                    // Update order status to rejected in Firebase
                    String orderId = order.getOrderId();
                    mDatabase.child("orders").child(orderId).child("status").setValue("Rejected");
                    
                    // Update local order status
                    order.setStatus("Rejected");
                    holder.status.setText("Rejected");
                    setStatusColor(holder.status, "Rejected");
                    
                    // Call the callback to handle the rejection
                    if (cancelCallback != null) {
                        cancelCallback.onOrderCanceled(order, position);
                    }
                });
            }
        } else {
            // Hide buttons for customer view or show different options
            holder.acceptButton.setVisibility(View.GONE);
            holder.cancelButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // Helper method to set status color based on status value
    private void setStatusColor(TextView statusTextView, String status) {
        switch (status.toLowerCase()) {
            case "accepted":
            case "completed":
                statusTextView.setTextColor(Color.parseColor("#4CAF50")); // Green
                break;
            case "pending":
                statusTextView.setTextColor(Color.parseColor("#FF9800")); // Orange
                break;
            case "rejected":
                statusTextView.setTextColor(Color.parseColor("#F44336")); // Red
                break;
            default:
                statusTextView.setTextColor(Color.parseColor("#333333")); // Default gray
        }
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView artworkTitle;
        TextView status;
        TextView artistName;
        Button acceptButton;
        Button cancelButton;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            
            orderId = itemView.findViewById(R.id.order_id);
            artworkTitle = itemView.findViewById(R.id.artwork_title);
            status = itemView.findViewById(R.id.status);
            artistName = itemView.findViewById(R.id.artist_name);
            acceptButton = itemView.findViewById(R.id.accept_button);
            cancelButton = itemView.findViewById(R.id.cancel_button);
        }
    }
}