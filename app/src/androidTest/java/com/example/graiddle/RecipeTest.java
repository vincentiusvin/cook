package com.example.graiddle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.models.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class RecipeTest {
    private String EMAIL = "testing@mail.com";
    private String PASSWORD = "123456";
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Before
    public void signIn() throws Exception{
        if(auth.getCurrentUser() != null){
            return;
        }
        else{
            Tasks.await(auth.signInWithEmailAndPassword(EMAIL, PASSWORD));
        }
    }

    @Test
    public void run() throws Exception {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("Ing 1");
        ingredients.add("Ing 2");

        ArrayList<String> steps = new ArrayList<String>();
        steps.add("Step 1");
        steps.add("Step 2");

        Recipe recipe = new Recipe(
                "TestRecipe",
                "Test Food",
                ingredients,
                steps,
                auth.getUid(),
                "bakso.jpg"
        );
        Tasks.await(recipe.push());

        DocumentSnapshot document = Tasks.await(recipe.getReference().get());
        assertTrue(document.exists());
        assertEquals(document.toObject(Recipe.class), recipe);

        Tasks.await(recipe.getReference().delete());
    }
}
