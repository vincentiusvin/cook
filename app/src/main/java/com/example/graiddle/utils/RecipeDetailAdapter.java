package com.example.graiddle.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graiddle.R;

import java.util.List;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.VH> {
    List<String> list;
    public RecipeDetailAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_recipe_detail, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tv.setText((position + 1) + ". " + list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends  RecyclerView.ViewHolder {
        TextView tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            this.tv = itemView.findViewById(R.id.itemRecipeDetailTV);
        }
    }
}
