package com.example.graiddle;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.models.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FirebaseTests {
    private String EMAIL = "vincentiusvin1@gmail.com";
    private String PASSWORD = "abcdef";
    private User user;
    private Recipe recipe;

    public FirebaseTests(){
        user = new User(1, "TestUser", "1234");

        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("Ing 1");
        ingredients.add("Ing 2");

        ArrayList<String> steps = new ArrayList<String>();
        steps.add("Step 1");
        steps.add("Step 2");

        recipe = new Recipe(1,
                "TestRecipe",
                "Test Food",
                ingredients,
                steps,
                user.getReference()
        );
    }

    @Test
    @Before
    public void login() throws Exception{
        FirebaseAuth auth = FirebaseAuth.getInstance();
        AuthResult res = Tasks.await(auth.signInWithEmailAndPassword(EMAIL, PASSWORD));
        assertEquals(res.getUser().getEmail(), EMAIL);
    }

    @Test
    public void addUser() throws Exception{
        Tasks.await(user.push());
    }

    @Test
    public void addRecipe() throws Exception{
        Tasks.await(recipe.push());
    }

    @Test
    public void viewRecipe(){
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        Recipe.getCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc : value){
                    long id = doc.getLong("id");
                    String name = doc.getString("name");
                    String description = doc.getString("description");
                    List<String> ingredients = (List<String>) doc.get("ingredients");
                    List<String> steps = (List<String>) doc.get("steps");
                    DocumentReference creator = doc.getDocumentReference("creator"); // references users
                    recipes.add(new Recipe(id, name, description, ingredients, steps, creator));
                }
            }
        });
        while (recipes.size() == 0){
            try{
                wait(1000);
            }catch (Exception e){}
        }
        assertEquals(1, recipes.size());
    }
}
