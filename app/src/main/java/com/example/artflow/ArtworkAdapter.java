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

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ArtworkViewHolder> {

    private List<Artwork> allArtworks; // Store all artworks for filtering
    private List<Artwork> artworks; // Current list to display

    public ArtworkAdapter(List<Artwork> artworks) {
        this.allArtworks = new ArrayList<>(artworks);
        this.artworks = artworks;
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
        
        holder.title.setText(artwork.getTitle());
        holder.category.setText(artwork.getCategory());
        holder.description.setText(artwork.getDescription());
        holder.price.setText(artwork.getPrice());
        
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
                if (artwork.getCategoryKey().equals(categoryKey)) {
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

        ArtworkViewHolder(@NonNull View itemView) {
            super(itemView);
            
            artworkImage = itemView.findViewById(R.id.artwork_image);
            title = itemView.findViewById(R.id.artwork_title);
            category = itemView.findViewById(R.id.artwork_category);
            description = itemView.findViewById(R.id.artwork_description);
            price = itemView.findViewById(R.id.artwork_price);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}