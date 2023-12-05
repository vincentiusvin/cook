package com.example.graiddle;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.models.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.util.Log;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class UserTest {
    private String EMAIL = "testing@mail.com";
    private String USERNAME = "Test User";
    private String PASSWORD = "123456";
    private Recipe recipe;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Test
    public void userTest() throws Exception{
        AuthResult register = Tasks.await(User.register(USERNAME, EMAIL, PASSWORD));
        AuthResult login = Tasks.await(auth.signInWithEmailAndPassword(EMAIL, PASSWORD));
        assertEquals(register.getUser().getEmail(), EMAIL);
        assertEquals(login.getUser().getEmail(), EMAIL);

        String uid = login.getUser().getUid();
        User user = new User(uid);

        DocumentSnapshot doc = Tasks.await(user.getReference().get());
        assertTrue(doc.exists());

        Tasks.await(user.getReference().delete());
        Tasks.await(login.getUser().delete());
    }

    @Test
    public void recipeTest(){
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("Ing 1");
        ingredients.add("Ing 2");

        ArrayList<String> steps = new ArrayList<String>();
        steps.add("Step 1");
        steps.add("Step 2");

        recipe = new Recipe(
                "TestRecipe",
                "Test Food",
                ingredients,
                steps,
                new User(auth.getCurrentUser().getUid()).getReference()
        );

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        Recipe.getCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc : value){
                    recipes.add(doc.toObject(Recipe.class));
                }
            }
        });
        while (recipes.size() == 0){
            try{
                wait(1000);
            }catch (Exception e){}
        }
        for (Recipe r: recipes){
            Log.d("halo", r.getDisplayName());
            Log.d("halo", r.getId());
            Log.d("halo", r.getDescription());
        }
    }
}
