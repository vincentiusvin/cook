package com.example.graiddle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class UpdateRecipeActivity extends AppCompatActivity {

    Button btnUpdateIngs, btnUpdateSteps, btnUpdateSave, btnUpdateDelete, btnUpdateBack;
    EditText etUpdateTitle, etUpdateDesc;
    ImageView ivUpdateImage;

    RecyclerView rvUpdateIngs, rvUpdateSteps;

    Recipe curr;

    private void clearFocus(){
        View v = getCurrentFocus();
        if(v != null){
            v.clearFocus();
        }
    }


    private byte[] imageViewToBytes(ImageView view){
        Bitmap bmImg = ((BitmapDrawable) view.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmImg.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }

    private void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);


        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnUpdateIngs = findViewById(R.id.updateIngsBtn);
        btnUpdateSteps = findViewById(R.id.updateStepsBtn);
        btnUpdateSave = findViewById(R.id.updateSaveBtn);
        btnUpdateBack = findViewById(R.id.updateBackBtn);
        btnUpdateDelete = findViewById(R.id.updateDeleteBtn);
        etUpdateTitle = findViewById(R.id.updateTitleET);
        etUpdateDesc = findViewById(R.id.updateDescET);
        rvUpdateIngs = findViewById(R.id.updateIngsRV);
        rvUpdateSteps = findViewById(R.id.updateStepsRV);
        ivUpdateImage = findViewById(R.id.updateImageIV);

        btnUpdateBack.setOnClickListener(v -> {
            onBackPressed();
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {ivUpdateImage.setImageURI(uri);}
        );

        ivUpdateImage.setOnClickListener(v -> {
            pickMedia
                .launch(new PickVisualMediaRequest.Builder() // errornya diignore aja
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        String recipeID = getIntent().getStringExtra("id");
        DocumentReference recipeDoc = Recipe.refById(recipeID);
        recipeDoc.get().addOnSuccessListener(documentSnapshot -> {
            curr = documentSnapshot.toObject(Recipe.class);

            etUpdateTitle.setText(curr.getDisplayName());
            etUpdateDesc.setText(curr.getDescription());

            AddRecipeAdapter ingsAdapter = new AddRecipeAdapter(this, curr.getIngredients());
            AddRecipeAdapter stepsAdapter = new AddRecipeAdapter(this, curr.getSteps());

            rvUpdateIngs.setAdapter(ingsAdapter);
            rvUpdateIngs.setLayoutManager(new LinearLayoutManager(this));

            rvUpdateSteps.setAdapter(stepsAdapter);
            rvUpdateSteps.setLayoutManager(new LinearLayoutManager(this));

            btnUpdateIngs.setOnClickListener(v -> {
                ingsAdapter.add("");
            });

            btnUpdateSteps.setOnClickListener(v ->{
                stepsAdapter.add("");
            });

            curr.getImageRef().getBytes(1000000000)
                .addOnSuccessListener(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ivUpdateImage.setImageBitmap(bmp);
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                });

            btnUpdateSave.setOnClickListener(v -> {
                clearFocus();

                String displayName = String.valueOf(etUpdateTitle.getText());
                String desc = String.valueOf(etUpdateDesc.getText());
                String imgName = UUID.randomUUID().toString() + ".jpeg";

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference oldImageRef = storage.getReference().child(this.curr.getImgID());
                StorageReference imageRef = storage.getReference().child(imgName);

                imageRef.putBytes(imageViewToBytes(ivUpdateImage))
                    .addOnSuccessListener(t -> {
                        curr.setDisplayName(displayName);
                        curr.setDescription(desc);
                        curr.setImgID(imgName);
                        curr.push().addOnSuccessListener(u -> {
                            oldImageRef.delete();
                            toast("Item updated successfully!");
                            Intent pindah = new Intent(UpdateRecipeActivity.this, RecipeDetailActivity.class);
                            pindah.putExtra("id", curr.getId());
                            startActivity(pindah);
                        }).addOnFailureListener(u -> {
                            toast("Item was not updated!");
                            imageRef.delete();
                        });
                    })
                    .addOnFailureListener(t -> {
                        toast("Item was not updated!");
                    });
            });

            btnUpdateDelete.setOnClickListener(v -> {
                curr.delete()
                    .addOnSuccessListener(unused -> {
                        toast("Item deleted succesfully!");
                        startActivity(new Intent(UpdateRecipeActivity.this, HomeActivity.class));
                    })
                    .addOnFailureListener(unused -> {
                        toast("Item was not deleted!");
                    });
            });
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.home_nav){
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                return true;
            }
            else if(item.getItemId()==R.id.add_nav){
                startActivity(new Intent(getApplicationContext(), AddRecipeActivity.class));
                finish();
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