package com.example.abm_improved.Utils;

import static com.example.abm_improved.BaseActivity.profilePicSelected;
import static com.example.abm_improved.BaseActivity.profilePicUri;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.abm_improved.Appointments.AppointmentsMainFragment;
import com.example.abm_improved.BaseActivity;
import com.example.abm_improved.DataClasses.Client;
import com.example.abm_improved.LoginAndRegister.LoginFragment;
import com.example.abm_improved.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class DatabaseUtils {
    private static final String TAG = "DatabaseUtils";

    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private static final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private static ArrayList<Client> clients = new ArrayList<>();

    public static ArrayList<Client> getClients() {
        return clients;
    }


    public static boolean userLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public static void loginUser(String email, String password, LoginFragment currFragment, OnFinishQueryInterface onFinishQueryInterface) {
        //send to firebase auth - upon success logs in automatically
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Toast.makeText(currFragment.requireContext(), "Login successful!", Toast.LENGTH_SHORT).show();
            onFinishQueryInterface.onFinishQuery(); // transfer to appointments main fragment
        }).addOnFailureListener(e -> {
            String errorMsg = "Error logging in! " + e.getMessage();
            Toast.makeText(currFragment.requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
            Log.i(TAG, errorMsg);
        });
    }

    public static void registerNewUser(FragmentActivity activity, String textFirstName, String textLastName, String textEmail, String textPhoneNumber, String textAddress, String textPassword, int textBirthdayDate) {
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(task -> {//ask firebase auth to create a new user
            if (task.isSuccessful()) { // if authenticator succeeded in creating a user
                FirebaseUser user = auth.getCurrentUser();//take that user

                assert user != null;
                String userUID = user.getUid();//get user ID

                Client userToAdd = new Client(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textBirthdayDate, userUID, false); //creating a new user
                DatabaseUtils.addClientToFirebase(userToAdd, activity);//add the user to the database

                if (profilePicSelected) {
                    DatabaseUtils.uploadImageToFirebase(storageReference.child("Clients").child(userUID).child("profile.jpg"), profilePicUri, activity);
                }

                // Upon success and finishing:
                // Init menu sidebar again (to show the correct menu items and user)
                BaseActivity baseActivity = (BaseActivity) activity;
                baseActivity.initMenuSideBar();

            } else {
                Toast.makeText(activity, "Registration failed! Unable to authenticate new user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void addClientToFirebase(Client user, FragmentActivity currActivity) {
        database.collection("Clients").document(user.getUid()).set(user) //adding user data to database
                .addOnSuccessListener(unused -> Toast.makeText(currActivity, "Registration successful! You are logged in!", Toast.LENGTH_SHORT).show()) //Registration successful
                .addOnFailureListener(e -> Toast.makeText(currActivity, "Registration failed! Unable to add user to database.", Toast.LENGTH_SHORT).show()); //Registration failed
    }

    public static void uploadImageToFirebase(StorageReference fileRef, Uri profilePicUri, Context context) {
        // upload image to firebase storage
        fileRef.putFile(profilePicUri)
                .addOnSuccessListener(taskSnapshot -> Toast.makeText(context, "Profile image uploaded successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Image upload failed!", Toast.LENGTH_SHORT).show());
    }

    public static void getAllClientsFromDatabase(OnFinishQueryInterface onFinishQueryInterface) {
        clients.clear(); // reset clients array
        //accessing database
        database.collection("Clients").orderBy("firstName")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        //save client data from database to clients array
                        Map<String, Object> data = document.getData();
                        String email = (String) data.get("email");
                        String uid = (String) data.get("uid");
                        String firstName = (String) data.get("firstName");
                        String lastName = (String) data.get("lastName");
                        String phoneNumber = (String) data.get("phoneNumber");
                        String address = (String) data.get("address");
                        int birthdayDate = Integer.parseInt(data.get("birthdayDate") + "");
                        boolean isManager = Boolean.parseBoolean(data.get("manager") + "");
                        Client currClient = new Client(firstName, lastName, email, phoneNumber, address, birthdayDate, uid, isManager); //creating a new user
                        clients.add(currClient); //adding user to list
                    }
                    onFinishQueryInterface.onFinishQuery();
                })
                .addOnFailureListener(e -> {
                    Log.i(TAG, "Error getting documents: " + e.getMessage());
                });
    }
}
