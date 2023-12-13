package com.example.graiddle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.models.User;
import com.example.graiddle.utils.AddRecipeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {
    Button btnAddIngs, btnAddSteps, btnResepPage;
    EditText etAddTitle, etAddDesc;
    ImageView ivAddImage;

    RecyclerView rvAddIngs, rvAddSteps;

    ArrayList<String> ings;
    ArrayList<String> steps;

    private byte[] imageViewToBytes(ImageView view){
        Bitmap bmImg = ((BitmapDrawable) ivAddImage.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmImg.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }

    private void clearFocus(){
        View v = getCurrentFocus();
        if(v != null){
            v.clearFocus();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnAddIngs = findViewById(R.id.addIngsBtn);
        btnAddSteps = findViewById(R.id.addStepsBtn);
        btnResepPage = findViewById(R.id.resepPageBtn);
        etAddTitle = findViewById(R.id.addTitleET);
        etAddDesc = findViewById(R.id.addDescET);
        rvAddIngs = findViewById(R.id.addIngsRV);
        rvAddSteps = findViewById(R.id.addStepsRV);
        ivAddImage = findViewById(R.id.addImageIV);

        ings = new ArrayList<String>();
        ings.add("");
        steps = new ArrayList<String>();
        steps.add("");

        rvAddIngs.setAdapter(new AddRecipeAdapter(this, ings));
        rvAddIngs.setLayoutManager(new LinearLayoutManager(this));

        rvAddSteps.setAdapter(new AddRecipeAdapter(this, steps));
        rvAddSteps.setLayoutManager(new LinearLayoutManager(this));

        btnAddIngs.setOnClickListener(v -> {
            ings.add("");
            rvAddIngs.getAdapter().notifyItemInserted(ings.size());
        });

        btnAddSteps.setOnClickListener(v -> {
            steps.add("");
            rvAddSteps.getAdapter().notifyItemInserted(steps.size());
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(),
            uri -> {ivAddImage.setImageURI(uri);}
        );

        ivAddImage.setOnClickListener(v -> {
            pickMedia
                .launch(new PickVisualMediaRequest.Builder() // errornya diignore aja
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
        });


        btnResepPage.setOnClickListener(v -> {
            clearFocus();

            String displayName = String.valueOf(etAddTitle.getText());
            String desc = String.valueOf(etAddDesc.getText());
            String imgName = UUID.randomUUID().toString() + ".jpeg";

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imageRef = storage.getReference().child(imgName);
            imageRef.putBytes(imageViewToBytes(ivAddImage))
                .addOnSuccessListener(t -> {
                    Toast.makeText(this, "Image added succesfully!", Toast.LENGTH_SHORT).show();
                    Recipe recipe = new Recipe(displayName, desc, ings, steps, auth.getUid(), imgName);
                    recipe.push().addOnSuccessListener(u -> {
                        Toast.makeText(AddRecipeActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                        Intent pindah = new Intent(AddRecipeActivity.this, RecipeDetailActivity.class);
                        pindah.putExtra("id", recipe.getId());
                        startActivity(pindah);
                    }).addOnFailureListener(u -> {
                        Toast.makeText(AddRecipeActivity.this, "Item was not added!", Toast.LENGTH_SHORT).show();
                        imageRef.delete();
                    });
                })
                .addOnFailureListener(t -> {
                    Toast.makeText(AddRecipeActivity.this, "Item was not added!", Toast.LENGTH_SHORT).show();
                });
        });

    }
}
