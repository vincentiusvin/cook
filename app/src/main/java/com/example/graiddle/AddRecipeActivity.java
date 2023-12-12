package com.example.graiddle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {
    Button btnAddIngs, btnAddSteps, btnResepPage;
    EditText etAddTitle, etAddDesc;

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

        ings = new ArrayList<String>();
        ings.add("Ingredient 1");
        ings.add("Ingredient 2");

        steps = new ArrayList<String>();
        steps.add("Step 1");
        steps.add("Step 2");

        rvAddIngs.setAdapter(new AddRecipeListsAdapter(ings));
        rvAddIngs.setLayoutManager(new LinearLayoutManager(this));

        rvAddSteps.setAdapter(new AddRecipeListsAdapter(steps));
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
            DocumentReference user = User.refById(auth.getUid());
            new Recipe(displayName, desc, ings, steps, user)
                .push().addOnSuccessListener(u -> {
                    Toast.makeText(AddRecipeActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(u -> {
                    Toast.makeText(AddRecipeActivity.this, "Item was not added!", Toast.LENGTH_SHORT).show();
                });
        });
    }
}