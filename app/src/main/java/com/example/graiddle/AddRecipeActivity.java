package com.example.graiddle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
        ings.add("Ingredient 1");
        ings.add("Ingredient 2");

        steps = new ArrayList<String>();
        steps.add("Step 1");
        steps.add("Step 2");

        rvAddIngs.setAdapter(new AddRecipeAdapter(ings));
        rvAddIngs.setLayoutManager(new LinearLayoutManager(this));

        rvAddSteps.setAdapter(new AddRecipeAdapter(steps));
        rvAddSteps.setLayoutManager(new LinearLayoutManager(this));

        btnAddIngs.setOnClickListener(v -> {
            ings.add("Placeholder");
            rvAddIngs.getAdapter().notifyItemInserted(ings.size() - 1);
        });

        btnAddSteps.setOnClickListener(v -> {
            steps.add("Placeholder");
            rvAddSteps.getAdapter().notifyItemInserted(steps.size() - 1);
        });

        btnResepPage.setOnClickListener(v -> {
            String displayName = String.valueOf(etAddTitle.getText());
            String desc = String.valueOf(etAddDesc.getText());
            String imgID = UUID.randomUUID().toString() + ".jpeg";

            DocumentReference user = User.refById(auth.getUid());
            new Recipe(displayName, desc, ings, steps, user, imgID)
                .push().addOnSuccessListener(u -> {
                    Toast.makeText(AddRecipeActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(u -> {
                    Toast.makeText(AddRecipeActivity.this, "Item was not added!", Toast.LENGTH_SHORT).show();
                });

            Bitmap bmImg = ((BitmapDrawable) ivAddImage.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmImg.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bytes = bos.toByteArray();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference image = storage.getReference().child(imgID);
            StorageTask storageTask = image.putBytes(bytes)
                .addOnSuccessListener(t -> {
                    Toast.makeText(this, "Image added succesfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(t -> {
                    Toast.makeText(this, "Image was not added!", Toast.LENGTH_SHORT).show();
                });
        });

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
        registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            ivAddImage.setImageURI(uri);
        });

    ivAddImage.setOnClickListener(v -> {
        pickMedia.launch(new PickVisualMediaRequest.Builder() // errornya diignore aja
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
            .build());
    });

    }
}