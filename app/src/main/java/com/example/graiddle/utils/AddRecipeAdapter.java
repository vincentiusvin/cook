package com.example.graiddle.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graiddle.R;

import java.util.List;

public class AddRecipeAdapter extends RecyclerView.Adapter<AddRecipeAdapter.VH> {
    List<String> list;

    public AddRecipeAdapter(List<String> list){
        this.list = list;
    }

    @NonNull
    @Override
    public AddRecipeAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_add_recipe, parent, false);
        return new AddRecipeAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.etName.setText(list.get(position));
        holder.btnRemove.setOnClickListener(v -> {
            holder.etName.requestFocus();
            holder.etName.clearFocus();
            list.remove(position);
            AddRecipeAdapter.this.notifyItemRemoved(position);
            AddRecipeAdapter.this.notifyItemRangeChanged(position, list.size());
        });
        holder.etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String newData = String.valueOf(holder.etName.getText());
                list.set(position, newData);
                AddRecipeAdapter.this.notifyItemChanged(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends  RecyclerView.ViewHolder{
        Button btnRemove;
        EditText etName;

        public VH(@NonNull View itemView) {
            super(itemView);
            btnRemove = itemView.findViewById(R.id.addRecipeItemRemoveBtn);
            etName = itemView.findViewById(R.id.addRecipeItemNameET);
        }
    }
}
