package com.example.graiddle.models;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Recipe extends FirebaseModel {
    private String displayName;
    private String description;
    private List<String> ingredients;
    private List<String> steps;
    private String userID;
    private String imgID;

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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    @Exclude
    public StorageReference getImageRef(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference image = storage.getReference().child(getImgID());
        return image;
    }


    public Recipe(String id, String name, String description, List<String> ingredients, List<String> steps, String userID, String imgID) {
        super(id);
        this.displayName = name;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.userID = userID;
        this.imgID = imgID;
    }

    public Recipe(String name, String description, List<String> ingredients, List<String> steps, String userID, String imgID) {
        super(UUID.randomUUID().toString());
        this.displayName = name;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.userID = userID;
        this.imgID = imgID;
    }

    @Override
    public CollectionReference _getCollection(){
        return  getCollection();
    }

    public static CollectionReference getCollection(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("recipes");
    }

    public static DocumentReference refById(String id){
        return getCollection().document(id);
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
                && Objects.equals(userID, recipe.userID)
                && Objects.equals(imgID, recipe.imgID)
                && Objects.equals(getId(), recipe.getId());
    }

    @Override
    public Task<Void> delete() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Task imgDel = storage.getReference().child(this.imgID).delete();
        Task itemDel = super.delete();
        return Tasks.whenAll(imgDel, itemDel);
    }
}
