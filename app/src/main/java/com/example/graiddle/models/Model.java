package com.example.graiddle.models;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class Model {
    @DocumentId
    private String id;

    public Model(){}

    public Model(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Method ini jangan dipakai. Cuma buat ngakalin biar bisa abstract static doang.
    @Exclude
    abstract CollectionReference _getCollection();

    @Exclude
    public DocumentReference getReference(){
        return _getCollection().document(id);
    }

    public Task<Void> push() {
        return getReference().set(this);
    }
}
