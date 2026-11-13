package com.example.fooddelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// RecyclerView er jonno adapter: list er prottek Food ke item_food card e dekhay
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    public interface OnFoodClickListener {
        void onFoodClick(Food food);
    }

    private final Context context;
    private final DatabaseHelper db;
    private final OnFoodClickListener listener;
    private List<Food> foods;

    public FoodAdapter(Context context, List<Food> foods, OnFoodClickListener listener) {
        this.context = context;
        this.foods = foods;
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    // Search/filter korle notun list set kore refresh kore
    public void setData(List<Food> newList) {
        this.foods = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Food f = foods.get(position);

        h.tvName.setText(f.getName());
        h.tvSubtitle.setText(f.getSubtitle());
        h.tvRating.setText(String.valueOf(f.getRating()));
        h.imgFood.setImageResource(getImageRes(f.getImage()));
        h.imgFav.setColorFilter(f.isFavorite() ? 0xFFF02D3A : 0xFF555555);

        h.itemView.setOnClickListener(v -> listener.onFoodClick(f));

        // Heart click: DB te favourite update + icon color change
        h.imgFav.setOnClickListener(v -> {
            f.setFavorite(!f.isFavorite());
            db.setFavorite(f.getId(), f.isFavorite());
            notifyItemChanged(h.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() { return foods.size(); }

    // "burger1" naam diye drawable khuje. Na pele placeholder
    private int getImageRes(String name) {
        int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return id != 0 ? id : R.drawable.ic_burger;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood, imgFav;
        TextView tvName, tvSubtitle, tvRating;

        ViewHolder(View v) {
            super(v);
            imgFood = v.findViewById(R.id.imgFood);
            imgFav = v.findViewById(R.id.imgFav);
            tvName = v.findViewById(R.id.tvName);
            tvSubtitle = v.findViewById(R.id.tvSubtitle);
            tvRating = v.findViewById(R.id.tvRating);
        }
    }
}
