package com.example.graiddle.models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Recipe extends FirebaseModel {
    private String displayName;
    private String description;
    private List<String> ingredients;
    private List<String> steps;
    DocumentReference creator; // references users

    public Recipe(){

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public Recipe(String id, String name, String description, List<String> ingredients, List<String> steps, DocumentReference creator) {
        super(id);
        this.displayName = name;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.creator = creator;
    }

    public Recipe(String name, String description, List<String> ingredients, List<String> steps, DocumentReference creator) {
        super(UUID.randomUUID().toString());
        this.displayName = name;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.creator = creator;
    }

    @Override
    public CollectionReference _getCollection(){
        return  getCollection();
    }

    public static CollectionReference getCollection(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("recipes");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(displayName, recipe.displayName)
                && Objects.equals(description, recipe.description)
                && Objects.equals(ingredients, recipe.ingredients)
                && Objects.equals(steps, recipe.steps)
                && Objects.equals(creator.getPath(), recipe.creator.getPath())
                && Objects.equals(getId(), recipe.getId());
    }
}
