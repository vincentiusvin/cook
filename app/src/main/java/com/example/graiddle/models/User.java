package com.example.graiddle.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class User {
    private long id;
    private String name;
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
        this.id = id;
        this.name = name;
        this.password = password;
    }

    @Exclude
    public DocumentReference getReference(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").document(Long.toString(id));
    }

    public Task<Void> push(){
        return getReference().set(this);
    }

    public Task<Void> push(OnCompleteListener<Void> callback){
        return getReference().set(this).addOnCompleteListener(callback);
    }

    // Callback dijalanin tiap ada perubahan di database.
    public static void bindToCollection(EventListener<QuerySnapshot> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").addSnapshotListener(callback);
    }
}
