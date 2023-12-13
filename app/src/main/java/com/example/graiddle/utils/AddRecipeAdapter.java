package com.example.graiddle.utils;

import android.app.Activity;
import android.content.Context;
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
    Activity activity;

    public AddRecipeAdapter(Activity activity, List<String> list){
        this.list = list;
        this.activity = activity;
    }

    private void clearFocus(){
        View v = activity.getCurrentFocus();
        if(v != null){
            v.clearFocus();
        }
    }

    @NonNull
    @Override
    public AddRecipeAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_add_recipe, parent, false);
        return new AddRecipeAdapter.VH(v);
    }

    public void remove(int position){
        clearFocus();
        list.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, list.size());
    }

    public void change(int position, String newValue){
        list.set(position, newValue);
        AddRecipeAdapter.this.notifyItemChanged(position);
    }

    public void add(String newValue){
        list.add(newValue);
        notifyItemInserted(list.size());
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.etName.setText(list.get(position));
        holder.btnRemove.setOnClickListener(v -> {
            remove(position);
        });
        holder.etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String string = String.valueOf(holder.etName.getText());
                change(position, string);
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
