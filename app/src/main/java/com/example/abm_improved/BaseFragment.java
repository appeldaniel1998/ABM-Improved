package com.example.abm_improved;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BaseFragment extends Fragment {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getDatabase() {
        return database;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}