package com.example.graiddle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graiddle.models.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    private List<Recipe> items;

    public RecipeListAdapter(List<Recipe> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_recipecard, parent, false);
        return new RecipeListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        Recipe item = items.get(position);
        holder.tvName.setText(item.getDisplayName());
        holder.tvDesc.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvDesc;

        public RecipeListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.nameTV);
            tvDesc = itemView.findViewById(R.id.descTV);
        }
    }
}
