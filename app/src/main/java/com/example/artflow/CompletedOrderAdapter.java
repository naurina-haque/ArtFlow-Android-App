package com.example.artflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.CompletedOrderViewHolder> {

    private List<CompletedOrder> completedOrders;

    public CompletedOrderAdapter(List<CompletedOrder> completedOrders) {
        this.completedOrders = completedOrders;
    }

    @NonNull
    @Override
    public CompletedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_order_item, parent, false);
        return new CompletedOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderViewHolder holder, int position) {
        CompletedOrder completedOrder = completedOrders.get(position);

        holder.orderId.setText(completedOrder.getOrderId());
        holder.customerName.setText(completedOrder.getCustomerName());
        holder.artworkTitle.setText(completedOrder.getArtworkTitle());
        holder.orderedOn.setText(completedOrder.getOrderedOn());
        holder.status.setText(completedOrder.getStatus());
        // Set the status text color to green for completed orders
        holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
    }

    @Override
    public int getItemCount() {
        return completedOrders.size();
    }

    static class CompletedOrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView customerName;
        TextView artworkTitle;
        TextView orderedOn;
        TextView status;

        CompletedOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.order_id);
            customerName = itemView.findViewById(R.id.customer_name);
            artworkTitle = itemView.findViewById(R.id.artwork_title);
            orderedOn = itemView.findViewById(R.id.ordered_on);
            status = itemView.findViewById(R.id.status);
        }
    }
}