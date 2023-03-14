package com.example.abm_improved;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BaseFragment extends Fragment {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public FirebaseAuth getAuth() {
        return auth;
    }

}