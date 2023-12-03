package com.example.graiddle.models;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Recipe {
    private long id;
    private String name;
    private String description;
    private List<String> ingredients;
    private List<String> steps;
    DocumentReference creator; // references users

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public DocumentReference getCreator() {
        return creator;
    }

    public void setCreator(DocumentReference creator) {
        this.creator = creator;
    }

    public Recipe(long id, String name, String description, List<String> ingredients, List<String> steps, DocumentReference creator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.creator = creator;
    }

    @Exclude
    public DocumentReference getReference(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("recipes").document(Long.toString(id));
    }

    public Task<Void> push() {
        return getReference().set(this);
    }

    public Task<Void> push(OnCompleteListener<Void> callback){
        return getReference().set(this).addOnCompleteListener(callback);
    }

    // Callback dijalanin tiap ada perubahan di database.
    public static void bindToCollection(EventListener<QuerySnapshot> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("recipes").addSnapshotListener(callback);
    }
}
