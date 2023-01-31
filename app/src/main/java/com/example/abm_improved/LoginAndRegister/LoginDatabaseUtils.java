package com.example.abm_improved.LoginAndRegister;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.abm_improved.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class LoginDatabaseUtils {
    private static final String TAG = "LoginDatabaseUtils";

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static void loginUser(String email, String password, LoginFragment currFragment) {
        //send to firebase auth - upon success logs in automatically
        currFragment.getAuth().signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Toast.makeText(currFragment.requireContext(), "Login successful!", Toast.LENGTH_SHORT).show();
            currFragment.userLoggedIn(); // transfer to appointments main fragment
        }).addOnFailureListener(e -> {
            String errorMsg = "Error logging in! " + e.getMessage();
            Toast.makeText(currFragment.requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
            Log.i(TAG, errorMsg);
        });
    }

    public static void addClientToFirebase(Client user, FragmentActivity currActivity) {
        database.collection("Clients").document(user.getUid()).set(user) //adding user data to database
                .addOnSuccessListener(unused -> Toast.makeText(currActivity, "Registration successful! You are logged in!", Toast.LENGTH_SHORT).show()) //Registration successful
                .addOnFailureListener(e -> Toast.makeText(currActivity, "Registration failed! Unable to add user to database.", Toast.LENGTH_SHORT).show()); //Registration failed
    }

    public static void uploadImageToFirebase(StorageReference storageReference, String clientUID, Uri profilePicUri, Context context) {
        // upload image to firebase storage
        StorageReference fileRef = storageReference.child("Clients").child(clientUID).child("profile.jpg");
        fileRef.putFile(profilePicUri)
                .addOnSuccessListener(taskSnapshot -> Toast.makeText(context, "Profile image uploaded successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Image upload failed!", Toast.LENGTH_SHORT).show());
    }
}
