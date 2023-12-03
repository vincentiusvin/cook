package com.example.graiddle.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class User extends Model{
    private String name;
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(long id, String name, String password) {
        setId(id);
        this.name = name;
        this.password = password;
    }

    @Override
    public CollectionReference _getCollection() {
        return getCollection();
    }

    public static CollectionReference getCollection(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users");
    }
}
