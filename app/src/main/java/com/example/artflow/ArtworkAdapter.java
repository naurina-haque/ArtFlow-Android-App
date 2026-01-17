package com.example.artflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ArtworkViewHolder> {

    private List<Artwork> allArtworks; // Store all artworks for filtering
    private List<Artwork> artworks; // Current list to display
    private boolean isCustomerView; // Flag to determine if showing for customer or artist

    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // Constructor for artist view (with edit/delete buttons)
    public ArtworkAdapter(List<Artwork> artworks) {
        this(artworks, false);
    }

    // Constructor for both views
    public ArtworkAdapter(List<Artwork> artworks, boolean isCustomerView) {
        this.allArtworks = new ArrayList<>(artworks);
        this.artworks = artworks;
        this.isCustomerView = isCustomerView;

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://artflow-55038-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artwork_item, parent, false);
        return new ArtworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder holder, int position) {
        Artwork artwork = artworks.get(position);

        // Safely set the values to avoid null pointer exceptions
        if (artwork.getTitle() != null) {
            holder.title.setText(artwork.getTitle());
        } else {
            holder.title.setText("Untitled");
        }

        if (artwork.getCategory() != null) {
            holder.category.setText(artwork.getCategory());
        } else {
            holder.category.setText("Uncategorized");
        }

        if (artwork.getDescription() != null) {
            holder.description.setText(artwork.getDescription());
        } else {
            holder.description.setText("No description");
        }

        if (artwork.getPrice() != null) {
            holder.price.setText(artwork.getPrice());
        } else {
            holder.price.setText("Price not set");
        }

        if (isCustomerView) {
            // Show buy button, hide edit/delete buttons
            holder.buyButton.setVisibility(View.VISIBLE);
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);

            // Set click listener for buy button
            holder.buyButton.setOnClickListener(v -> {
                // Handle buy action - create an order
                placeOrder(artwork);
            });
        } else {
            // Show edit/delete buttons, hide buy button
            holder.buyButton.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);

            // Set click listeners for edit and delete buttons
            holder.editButton.setOnClickListener(v -> {
                // Handle edit action
            });

            holder.deleteButton.setOnClickListener(v -> {
                // Handle delete action
                // Remove artwork from list and notify adapter
                int positionToRemove = holder.getAdapterPosition();
                if (positionToRemove != RecyclerView.NO_POSITION) {
                    artworks.remove(positionToRemove);
                    notifyItemRemoved(positionToRemove);
                }
            });
        }
    }

    private void placeOrder(Artwork artwork) {
        String customerId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";
        if (customerId.isEmpty()) {
            // Handle case where user is not authenticated
            return;
        }

        mDatabase.child("artists").child(artwork.getArtistId()).get()
            .addOnSuccessListener(artistSnapshot -> {
                Artist artist = artistSnapshot.getValue(Artist.class);
                String artistName = (artist != null && artist.getName() != null) ? artist.getName() : "Unknown Artist";

                mDatabase.child("customers").child(customerId).get()
                    .addOnSuccessListener(customerSnapshot -> {
                        Customer customer = customerSnapshot.getValue(Customer.class);
                        String customerName = (customer != null && customer.getName() != null) ? customer.getName() : "Unknown Customer";
                        String orderId = mDatabase.child("orders").push().getKey();
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        if (orderId != null) {
                            Order order = new Order(orderId, customerId, customerName, artwork.getArtistId(), artistName, artwork.getId(), artwork.getTitle(), artwork.getImageUrl(), "Pending", timestamp);
                            mDatabase.child("orders").child(orderId).setValue(order)
                                .addOnSuccessListener(aVoid -> android.widget.Toast.makeText(itemView.getContext(), "Order placed successfully", android.widget.Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> android.widget.Toast.makeText(itemView.getContext(), "Failed to place order", android.widget.Toast.LENGTH_SHORT).show());
                        }
                    })
                    .addOnFailureListener(e -> {
                        String customerName = "Unknown Customer";
                        String orderId = mDatabase.child("orders").push().getKey();
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        if (orderId != null) {
                            Order order = new Order(orderId, customerId, customerName, artwork.getArtistId(), artistName, artwork.getId(), artwork.getTitle(), artwork.getImageUrl(), "Pending", timestamp);
                            mDatabase.child("orders").child(orderId).setValue(order)
                                .addOnSuccessListener(aVoid -> android.widget.Toast.makeText(itemView.getContext(), "Order placed successfully", android.widget.Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e2 -> android.widget.Toast.makeText(itemView.getContext(), "Failed to place order", android.widget.Toast.LENGTH_SHORT).show());
                        }
                    });
            })
            .addOnFailureListener(e -> {
                String artistName = "Unknown Artist";
                mDatabase.child("customers").child(customerId).get()
                    .addOnSuccessListener(customerSnapshot -> {
                        Customer customer = customerSnapshot.getValue(Customer.class);
                        String customerName = (customer != null && customer.getName() != null) ? customer.getName() : "Unknown Customer";
                        String orderId = mDatabase.child("orders").push().getKey();
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        if (orderId != null) {
                            Order order = new Order(orderId, customerId, customerName, artwork.getArtistId(), artistName, artwork.getId(), artwork.getTitle(), artwork.getImageUrl(), "Pending", timestamp);
                            mDatabase.child("orders").child(orderId).setValue(order)
                                .addOnSuccessListener(aVoid -> android.widget.Toast.makeText(itemView.getContext(), "Order placed successfully", android.widget.Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e2 -> android.widget.Toast.makeText(itemView.getContext(), "Failed to place order", android.widget.Toast.LENGTH_SHORT).show());
                        }
                    })
                    .addOnFailureListener(e2 -> {
                        String customerName = "Unknown Customer";
                        String orderId = mDatabase.child("orders").push().getKey();
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        if (orderId != null) {
                            Order order = new Order(orderId, customerId, customerName, artwork.getArtistId(), artistName, artwork.getId(), artwork.getTitle(), artwork.getImageUrl(), "Pending", timestamp);
                            mDatabase.child("orders").child(orderId).setValue(order)
                                .addOnSuccessListener(aVoid -> android.widget.Toast.makeText(itemView.getContext(), "Order placed successfully", android.widget.Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e3 -> android.widget.Toast.makeText(itemView.getContext(), "Failed to place order", android.widget.Toast.LENGTH_SHORT).show());
                        }
                    });
            });
    }

    @Override
    public int getItemCount() {
        return artworks.size();
    }

    // Filter method to update displayed artworks based on category
    public void filterByCategory(String category) {
        artworks.clear();

        if (category.equals("All")) {
            artworks.addAll(allArtworks);
        } else {
            String categoryKey = convertCategoryToKey(category);
            for (Artwork artwork : allArtworks) {
                if (artwork.getCategoryKey() != null && artwork.getCategoryKey().equals(categoryKey)) {
                    artworks.add(artwork);
                }
            }
        }

        notifyDataSetChanged();
    }

    // Add a new artwork to the list
    public void addArtwork(Artwork artwork) {
        allArtworks.add(0, artwork); // Add to the beginning
        artworks.add(0, artwork); // If not filtered, also add to current list
        notifyItemInserted(0);
    }

    // Update an existing artwork in the list
    public void updateArtwork(int position, Artwork artwork) {
        if (position >= 0 && position < allArtworks.size()) {
            allArtworks.set(position, artwork);
            if (position < artworks.size()) {
                artworks.set(position, artwork);
                notifyItemChanged(position);
            }
        }
    }

    // Remove an artwork from the list
    public void removeArtwork(int position) {
        if (position >= 0 && position < allArtworks.size()) {
            Artwork artworkToRemove = allArtworks.get(position);
            allArtworks.remove(position);

            // Also remove from filtered list if it's currently displayed
            if (artworks.contains(artworkToRemove)) {
                artworks.remove(artworkToRemove);
                notifyItemRemoved(position);
            }
        }
    }

    private String convertCategoryToKey(String category) {
        switch (category.toLowerCase()) {
            case "colored portraits":
                return "colored_portraits";
            case "b&w portraits":
                return "bw_portraits";
            case "watercolor art":
                return "watercolor";
            case "digital art":
                return "digital";
            case "abstract art":
                return "abstract";
            case "landscape art":
                return "landscape";
            case "line art":
                return "line";
            case "acrylic art":
                return "acrylic";
            default:
                return "all";
        }
    }

    static class ArtworkViewHolder extends RecyclerView.ViewHolder {
        ImageView artworkImage;
        TextView title;
        TextView category;
        TextView description;
        TextView price;
        Button editButton;
        Button deleteButton;
        Button buyButton;  // New buy button for customer view

        ArtworkViewHolder(@NonNull View itemView) {
            super(itemView);

            artworkImage = itemView.findViewById(R.id.artwork_image);
            title = itemView.findViewById(R.id.artwork_title);
            category = itemView.findViewById(R.id.artwork_category);
            description = itemView.findViewById(R.id.artwork_description);
            price = itemView.findViewById(R.id.artwork_price);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            buyButton = itemView.findViewById(R.id.buy_button);  // Initialize buy button
        }
    }
}