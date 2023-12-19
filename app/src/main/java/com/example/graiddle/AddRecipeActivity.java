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
import com.example.graiddle.utils.AddRecipeAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {
    Button btnAddIngs, btnAddSteps, btnAddPage, btnBack;
    EditText etAddTitle, etAddDesc;
    ImageView ivAddImage;

    RecyclerView rvAddIngs, rvAddSteps;

    ArrayList<String> ings;
    ArrayList<String> steps;

    private byte[] imageViewToBytes(ImageView view){
        Bitmap bmImg = ((BitmapDrawable) view.getDrawable()).getBitmap();
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

    private void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnAddIngs = findViewById(R.id.addIngsBtn);
        btnAddSteps = findViewById(R.id.addStepsBtn);
        btnAddPage = findViewById(R.id.addPageBtn);
        etAddTitle = findViewById(R.id.addTitleET);
        etAddDesc = findViewById(R.id.addDescET);
        rvAddIngs = findViewById(R.id.addIngsRV);
        rvAddSteps = findViewById(R.id.addStepsRV);
        ivAddImage = findViewById(R.id.addImageIV);
        btnBack = findViewById(R.id.backButton);

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        });

        ings = new ArrayList<String>();
        ings.add("");
        steps = new ArrayList<String>();
        steps.add("");

        AddRecipeAdapter ingsAdapter = new AddRecipeAdapter(this, ings);
        AddRecipeAdapter stepsAdapter = new AddRecipeAdapter(this, steps);

        rvAddIngs.setAdapter(ingsAdapter);
        rvAddIngs.setLayoutManager(new LinearLayoutManager(this));

        rvAddSteps.setAdapter(stepsAdapter);
        rvAddSteps.setLayoutManager(new LinearLayoutManager(this));

        btnAddIngs.setOnClickListener(v -> {
            ingsAdapter.add("");
        });

        btnAddSteps.setOnClickListener(v -> {
            stepsAdapter.add("");
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


        btnAddPage.setOnClickListener(v -> {
            clearFocus();

            String displayName = String.valueOf(etAddTitle.getText());
            String desc = String.valueOf(etAddDesc.getText());
            String imgName = UUID.randomUUID().toString() + ".jpeg";

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imageRef = storage.getReference().child(imgName);
            imageRef.putBytes(imageViewToBytes(ivAddImage))
                .addOnSuccessListener(t -> {
                    Recipe recipe = new Recipe(displayName, desc, ings, steps, auth.getUid(), imgName);
                    recipe.push().addOnSuccessListener(u -> {
                        toast("Item added successfully!");
                        Intent pindah = new Intent(AddRecipeActivity.this, RecipeDetailActivity.class);
                        pindah.putExtra("id", recipe.getId());
                        startActivity(pindah);
                    }).addOnFailureListener(u -> {
                        toast("Item was not added!");
                        imageRef.delete();
                    });
                })
                .addOnFailureListener(t -> {
                    toast("Item was not added!");
                });
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.add_nav);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.home_nav){
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                return true;
            }
            else if(item.getItemId()==R.id.add_nav){
                return true;
            }
            else if(item.getItemId()==R.id.logout_nav){
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Toast.makeText(getApplicationContext(), "Log out account", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            }
            return false;
        });

    }
}
