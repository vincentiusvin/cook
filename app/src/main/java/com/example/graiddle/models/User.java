package com.example.graiddle.models;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class User extends FirebaseModel {
    public User(String id){
        super(id);
    }

    @Override
    public CollectionReference _getCollection() {
        return getCollection();
    }

    public static CollectionReference getCollection(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users");
    }

    public static Task<AuthResult> register(String username, String email, String password){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = authResult.getUser();
                user.updateProfile(
                    new UserProfileChangeRequest.Builder()
                        .setDisplayName(username).build()
                );
                new User(user.getUid()).push();
            }
        });
    }
}
