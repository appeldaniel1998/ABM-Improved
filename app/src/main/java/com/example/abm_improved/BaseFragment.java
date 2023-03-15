package com.example.abm_improved;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.abm_improved.Utils.Interfaces;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BaseFragment extends Fragment {
    public Interfaces.OnChooseProfilePicListener onChooseProfilePicListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Interfaces.OnChooseProfilePicListener) {
            onChooseProfilePicListener = (Interfaces.OnChooseProfilePicListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnChooseMediaListener");
        }
    }

}