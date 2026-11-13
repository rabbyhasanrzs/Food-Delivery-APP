package com.example.fooddelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.models.Product;
import com.example.fooddelivery.utils.CurrencyFormatter;
import com.example.fooddelivery.utils.ProductImages;

import java.util.List;

/**
 * Minimal Home grid adapter. Binds one Product to one card, handles
 * the favourite heart, and reports card taps back to MainActivity
 * through a listener.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    /** Called when a product card is tapped. */
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    /** Called when the favourite heart is tapped. */
    public interface OnFavouriteClickListener {
        void onFavouriteClick(Product product);
    }

    private final List<Product> products;
    private final OnProductClickListener clickListener;
    private final OnFavouriteClickListener favouriteListener;

    public ProductAdapter(List<Product> products,
                          OnProductClickListener clickListener,
                          OnFavouriteClickListener favouriteListener) {
        this.products = products;
        this.clickListener = clickListener;
        this.favouriteListener = favouriteListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameText.setText(product.getName());
        holder.priceText.setText(CurrencyFormatter.format(product.getPriceBdt()));
        holder.ratingText.setText("★ " + product.getRating());
        holder.deliveryTimeText.setText(product.getDeliveryTime());
        // Real product photo when the JPG is present, placeholder otherwise.
        ProductImages.setProductImage(holder.imageView, product.getImageName());
        holder.favouriteText.setText(product.isFavourite() ? "♥" : "♡");
        holder.favouriteText.setTextColor(product.isFavourite()
                ? holder.itemView.getContext().getColor(R.color.colorPrimary)
                : holder.itemView.getContext().getColor(R.color.colorTextSecondary));

        holder.itemView.setOnClickListener(v -> clickListener.onProductClick(product));
        holder.favouriteText.setOnClickListener(v -> favouriteListener.onFavouriteClick(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    /** Replace the list (used by search) and redraw the grid. */
    public void updateProducts(List<Product> newProducts) {
        products.clear();
        products.addAll(newProducts);
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameText;
        final TextView priceText;
        final TextView ratingText;
        final TextView deliveryTimeText;
        final TextView favouriteText;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            nameText = itemView.findViewById(R.id.product_name);
            priceText = itemView.findViewById(R.id.product_price);
            ratingText = itemView.findViewById(R.id.product_rating);
            deliveryTimeText = itemView.findViewById(R.id.product_delivery_time);
            favouriteText = itemView.findViewById(R.id.product_favourite);
        }
    }
}
