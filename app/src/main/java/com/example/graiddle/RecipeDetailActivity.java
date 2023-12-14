package com.example.graiddle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.models.User;
import com.example.graiddle.utils.RecipeDetailAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeDetailActivity extends AppCompatActivity {

    TextView tvRecipeTitle, tvRecipeDesc, tvRecipeAuthor;
    RecyclerView rvRecipeIngs, rvRecipeSteps;
    Button btnBack, btnEdit;
    ImageView ivRecipeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        tvRecipeTitle = findViewById(R.id.recipeTitleTV);
        tvRecipeDesc = findViewById(R.id.recipeDescTV);
        tvRecipeAuthor = findViewById(R.id.recipeAuthorTV);
        rvRecipeIngs = findViewById(R.id.recipeIngsRV);
        rvRecipeSteps = findViewById(R.id.recipeStepsRV);
        ivRecipeImage = findViewById(R.id.recipeImageIV);
        btnBack = findViewById(R.id.backButton);
        btnEdit = findViewById(R.id.editBtn);

        rvRecipeIngs.setLayoutManager(new LinearLayoutManager(this));
        rvRecipeSteps.setLayoutManager(new LinearLayoutManager(this));

        String recipeID = getIntent().getStringExtra("id");
        DocumentReference recipeDoc = Recipe.refById(recipeID);
        recipeDoc.get().addOnSuccessListener(documentSnapshot -> {
            Recipe recipe = documentSnapshot.toObject(Recipe.class);

            User.refById(recipe.getUserID()).get()
                .addOnSuccessListener(userDocument -> {
                    User user = userDocument.toObject(User.class);
                    tvRecipeAuthor.setText("by: " + user.getDisplayName());
                })
                .addOnFailureListener(u -> {
                    tvRecipeAuthor.setText("Unknown author");
                });

            tvRecipeTitle.setText(recipe.getDisplayName());
            tvRecipeDesc.setText(recipe.getDescription());
            rvRecipeIngs.setAdapter(new RecipeDetailAdapter(recipe.getIngredients()));
            rvRecipeSteps.setAdapter(new RecipeDetailAdapter(recipe.getSteps()));
            if(recipe.getUserID().equals(auth.getUid())){
                btnEdit.setVisibility(View.VISIBLE);
                btnEdit.setOnClickListener(v -> {
                    Intent edit = new Intent(RecipeDetailActivity.this, UpdateRecipeActivity.class);
                    edit.putExtra("id", recipe.getId());
                    startActivity(edit);
                });
            }

            recipe.getImageRef().getBytes(1000000000)
                .addOnSuccessListener(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ivRecipeImage.setImageBitmap(bmp);
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                });
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(RecipeDetailActivity.this, HomeActivity.class));
        });
    }
}