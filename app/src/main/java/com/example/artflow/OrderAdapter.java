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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        
        holder.orderId.setText(order.getOrderId());
        holder.customerName.setText(order.getCustomerName());
        holder.artworkTitle.setText(order.getArtworkTitle());
        holder.orderedOn.setText(order.getOrderedOn());
        holder.status.setText(order.getStatus());
        
        // Set status color based on status value
        setStatusColor(holder.status, order.getStatus());
        
        // Set button click listener
        holder.actionButton.setText("View");
        holder.actionButton.setOnClickListener(v -> {
            // Handle action button click - could show more details or navigate to another screen
            // For now, just print to log or handle as needed
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // Helper method to set status color based on status value
    private void setStatusColor(TextView statusTextView, String status) {
        switch (status.toLowerCase()) {
            case "completed":
                statusTextView.setTextColor(Color.parseColor("#4CAF50")); // Green
                break;
            case "pending":
                statusTextView.setTextColor(Color.parseColor("#FF9800")); // Orange
                break;
            case "processing":
                statusTextView.setTextColor(Color.parseColor("#2196F3")); // Blue
                break;
            case "shipped":
                statusTextView.setTextColor(Color.parseColor("#9C27B0")); // Purple
                break;
            case "cancelled":
                statusTextView.setTextColor(Color.parseColor("#F44336")); // Red
                break;
            default:
                statusTextView.setTextColor(Color.parseColor("#333333")); // Default gray
        }
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView customerName;
        TextView artworkTitle;
        TextView orderedOn;
        TextView status;
        Button actionButton;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            
            orderId = itemView.findViewById(R.id.order_id);
            customerName = itemView.findViewById(R.id.customer_name);
            artworkTitle = itemView.findViewById(R.id.artwork_title);
            orderedOn = itemView.findViewById(R.id.ordered_on);
            status = itemView.findViewById(R.id.status);
            actionButton = itemView.findViewById(R.id.action_button);
        }
    }
}