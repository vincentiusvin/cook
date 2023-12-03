package com.example.graiddle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.models.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
}
