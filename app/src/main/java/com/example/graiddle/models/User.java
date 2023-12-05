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
import java.util.UUID;

public class User extends Model{
    private String name;
    private String password;

    public User(){}

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

    public User(String id, String name, String password) {
        super(id);
        this.name = name;
        this.password = password;
    }

    public User(String name, String password) {
        super(UUID.randomUUID().toString());
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
