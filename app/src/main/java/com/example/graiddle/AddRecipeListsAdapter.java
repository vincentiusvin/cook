package com.example.graiddle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddRecipeListsAdapter extends RecyclerView.Adapter<AddRecipeListsAdapter.VH> {
    List<String> list;

    public AddRecipeListsAdapter(List<String> list){
        this.list = list;
    }

    @NonNull
    @Override
    public AddRecipeListsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_addrecipe, parent, false);
        return new AddRecipeListsAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.etName.setText(list.get(position));
        holder.btnRemove.setOnClickListener(v -> {
            list.remove(position);
            AddRecipeListsAdapter.this.notifyItemRemoved(position);
        });
        holder.etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String newData = String.valueOf(holder.etName.getText());
                list.set(position, newData);
                AddRecipeListsAdapter.this.notifyItemChanged(position);
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
