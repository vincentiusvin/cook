package com.example.graiddle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.graiddle.models.Recipe;
import com.example.graiddle.models.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        try{
            Tasks.await(auth.signInWithEmailAndPassword("vincentiusvin1@gmail.com", "abcdef"));
        }catch (Exception e){
            e.printStackTrace();
        }

        User newUser = new User(1, "Udin", "123");
        newUser.push(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
            } else {
                Exception e = task.getException();
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}