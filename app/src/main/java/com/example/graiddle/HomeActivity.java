package com.example.graiddle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.transition.Slide;
import android.widget.Button;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.graiddle.models.Recipe;
import com.example.graiddle.utils.HomeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvRecipes;
    private HomeAdapter adapter;
    //Button btnAdd;
    private ImageSlider ImagesSlider;

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRecipes = findViewById(R.id.menuRV);
        //btnAdd = findViewById(R.id.addBtn);
        ImagesSlider = findViewById(R.id.ImagesSlider);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rvRecipes.setLayoutManager(gridLayoutManager);
        rvRecipes.setHasFixedSize(true);

//        btnAdd.setOnClickListener(v -> {
//            startActivity(new Intent(HomeActivity.this, AddRecipeActivity.class));
//        });
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        database.collection("recipes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        String imgFileName = queryDocumentSnapshot.getString("imgID");
                        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/graiddle.appspot.com/o/" + imgFileName + "?alt=media";
                        slideModels.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                    }
                    ImagesSlider.setImageList(slideModels, ScaleTypes.FIT);
                }
                else{
                    Toast.makeText(HomeActivity.this, "Cannot Load Images", Toast.LENGTH_SHORT).show();
                }
            }
        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomeActivity.this, "Cannot Load Images", Toast.LENGTH_SHORT).show();
                            }
                        });

        Recipe.getCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Recipe> result = value.toObjects(Recipe.class);

                adapter = new HomeAdapter(HomeActivity.this, result);
                rvRecipes.setAdapter(adapter);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_nav);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.home_nav){
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