package com.example.graiddle.models;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User extends FirebaseModel {
    private String displayName;
    User(){

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public User(String id, String displayName) {
        super(id);
        this.displayName = displayName;
    }

    @Override
    public CollectionReference _getCollection() {
        return getCollection();
    }

    public static CollectionReference getCollection() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users");
    }

    // Callback hell.
    // Buat user, terus update profilenya, terus masukkin displayname.
    public static Task<AuthResult> register(String username, String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            FirebaseUser user = authResult.getUser();
            user.updateProfile(
                    new UserProfileChangeRequest.Builder()
                            .setDisplayName(username).build()
            ).addOnSuccessListener(unused -> {
                new User(user.getUid(), user.getDisplayName()).push();
            });
        });
    }

    public static User findByID(String id) throws Exception{
        try{
            DocumentSnapshot doc = Tasks.await(getCollection().document(id).get());
            return doc.toObject(User.class);
        }
        catch(Exception e){
            return null;
        }
    }

    public static DocumentReference refById(String id) {
        return getCollection().document(id);
    }
}
