package com.example.graiddle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String EMAIL = "testing@mail.com";
    private String PASSWORD = "123456";

    RecyclerView rvRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            Tasks.await(auth.signInWithEmailAndPassword(EMAIL, PASSWORD));
        }catch (Exception e){}
        rvRecipes = findViewById(R.id.recipesRV);

        Recipe.getCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Recipe> result = value.toObjects(Recipe.class);
                rvRecipes.setAdapter(new RecipeListAdapter(result));
                rvRecipes.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });
    }
}