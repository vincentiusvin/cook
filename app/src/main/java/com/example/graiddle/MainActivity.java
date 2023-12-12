package com.example.graiddle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.models.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String EMAIL = "testing@mail.com";
    private String PASSWORD = "123456";

    private RecyclerView mRecyclerView;
    private RecyclerView rvRecipes;
    private List<String> titles;
    private List<String> mImages;

    private HomeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        startActivity(new Intent(this, AddRecipeActivity.class));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.menuRV);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            Tasks.await(auth.signInWithEmailAndPassword(EMAIL, PASSWORD));
        }catch (Exception e){}

        Recipe.getCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Recipe> result = value.toObjects(Recipe.class);
                adapter = new HomeAdapter(MainActivity.this, result);
                mRecyclerView.setAdapter(adapter);
            }
        });
    }
}