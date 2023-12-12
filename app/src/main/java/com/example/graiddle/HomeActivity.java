package com.example.graiddle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.utils.HomeAdapter;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvRecipes;
    private HomeAdapter adapter;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRecipes = findViewById(R.id.menuRV);
        btnAdd = findViewById(R.id.addBtn);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rvRecipes.setLayoutManager(gridLayoutManager);
        rvRecipes.setHasFixedSize(true);

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AddRecipeActivity.class));
        });

        Recipe.getCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Recipe> result = value.toObjects(Recipe.class);
                adapter = new HomeAdapter(HomeActivity.this, result);
                rvRecipes.setAdapter(adapter);
            }
        });
    }
}