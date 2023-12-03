package com.example.graiddle.models;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class Model {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Method ini jangan dipakai. Cuma buat ngakalin biar bisa abstract static doang.
    @Exclude
    abstract CollectionReference _getCollection();

    @Exclude
    public DocumentReference getReference(){
        return _getCollection().document(Long.toString(id));
    }

    public Task<Void> push() {
        return getReference().set(this);
    }
}
