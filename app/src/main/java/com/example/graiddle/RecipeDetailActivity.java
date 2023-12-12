package com.example.graiddle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.utils.RecipeDetailAdapter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeDetailActivity extends AppCompatActivity {

    TextView tvRecipeTitle, tvRecipeDesc;
    RecyclerView rvRecipeIngs, rvRecipeSteps;
    Button btnBack;
    ImageView ivRecipeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvRecipeTitle = findViewById(R.id.recipeTitleTV);
        tvRecipeDesc = findViewById(R.id.recipeDescTV);
        rvRecipeIngs = findViewById(R.id.recipeIngsRV);
        rvRecipeSteps = findViewById(R.id.recipeStepsRV);
        ivRecipeImage = findViewById(R.id.recipeImageIV);
        btnBack = findViewById(R.id.backButton);

        rvRecipeIngs.setLayoutManager(new LinearLayoutManager(this));
        rvRecipeSteps.setLayoutManager(new LinearLayoutManager(this));

        String recipeID = getIntent().getStringExtra("id");
        DocumentReference recipeDoc = Recipe.refById(recipeID);
        recipeDoc.get().addOnSuccessListener(documentSnapshot -> {
            Recipe recipe = documentSnapshot.toObject(Recipe.class);

            tvRecipeTitle.setText(recipe.getDisplayName());
            tvRecipeDesc.setText(recipe.getDescription());
            rvRecipeIngs.setAdapter(new RecipeDetailAdapter(recipe.getIngredients()));
            rvRecipeSteps.setAdapter(new RecipeDetailAdapter(recipe.getSteps()));

            recipe.getImageRef().getBytes(1000000000)
                .addOnSuccessListener(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ivRecipeImage.setImageBitmap(bmp);
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                });
            });

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}