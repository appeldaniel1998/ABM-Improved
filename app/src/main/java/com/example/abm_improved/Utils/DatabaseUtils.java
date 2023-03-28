package com.example.abm_improved.Utils;

import static com.example.abm_improved.BaseActivity.profilePicSelected;
import static com.example.abm_improved.BaseActivity.profilePicUri;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.abm_improved.BaseActivity;
import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.DataClasses.AppointmentType;
import com.example.abm_improved.DataClasses.Client;
import com.example.abm_improved.DataClasses.Product;
import com.example.abm_improved.LoginAndRegister.LoginFragment;
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
    private static ArrayList<AppointmentType> appointmentTypes = new ArrayList<>();
    private static ArrayList<Product> products = new ArrayList<>();
    private static ArrayList<Appointment> appointments = new ArrayList<>();
    private static boolean currentUserIsManager = false;

    public static ArrayList<Client> getClients() {
        return clients;
    }

    public static ArrayList<AppointmentType> getAppointmentTypes() {
        return appointmentTypes;
    }

    public static ArrayList<Product> getProducts() {
        return products;
    }

    public static ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public static boolean userLoggedIn() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = auth.getCurrentUser().getUid();
            database.collection("Clients").document(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Map<String, Object> user = task.getResult().getData();
                    assert user != null;
                    currentUserIsManager = (boolean) user.get("manager");
                }
            });
        }
        return currentUser != null;
    }

    public static void loginUser(String email, String password, LoginFragment currFragment, Interfaces.OnFinishQueryInterface onFinishQueryInterface) {
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

    public static void registerNewUser(FragmentActivity currActivity, String textFirstName, String textLastName, String textEmail, String textPhoneNumber, String textAddress, String textPassword, int textBirthdayDate) {
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(task -> {//ask firebase auth to create a new user
            if (task.isSuccessful()) { // if authenticator succeeded in creating a user
                FirebaseUser user = auth.getCurrentUser();//take that user

                assert user != null;
                String userUID = user.getUid();//get user ID

                Client userToAdd = new Client(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textBirthdayDate, userUID, false); //creating a new user
                uploadRelevantClientInfo(userToAdd, currActivity, storageReference.child("Clients").child(userToAdd.getUid()).child("profile.jpg")); //upload user to database

                // Upon success and finishing:
                // Init menu sidebar again (to show the correct menu items and user)
                BaseActivity baseActivity = (BaseActivity) currActivity;
                baseActivity.initMenuSideBar();

            } else {
                Toast.makeText(currActivity, "Registration failed! Unable to authenticate new user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void uploadRelevantClientInfo(Client currClient, FragmentActivity currActivity, StorageReference savedImagePath) {
        DatabaseUtils.addClientToFirebase(currClient, currActivity);//add the user to the database

        if (profilePicSelected) {
            DatabaseUtils.uploadImageToFirebase(savedImagePath, profilePicUri, currActivity);
            profilePicSelected = false;
        }
    }

    public static void addClientToFirebase(Client user, FragmentActivity currActivity) {
        database.collection("Clients").document(user.getUid()).set(user) //adding user data to database
                .addOnSuccessListener(unused -> Toast.makeText(currActivity, "Registration successful! You are logged in!", Toast.LENGTH_SHORT).show()) //Registration successful
                .addOnFailureListener(e -> Toast.makeText(currActivity, "Registration failed! Unable to add user to database.", Toast.LENGTH_SHORT).show()); //Registration failed
    }

    public static void uploadImageToFirebase(StorageReference fileRef, Uri profilePicUri, Context context) {
        // upload image to firebase storage
        fileRef.putFile(profilePicUri).addOnSuccessListener(taskSnapshot -> Toast.makeText(context, "Profile image uploaded successfully!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(context, "Image upload failed!", Toast.LENGTH_SHORT).show());
    }

    public static void getAllClientsFromDatabase(Interfaces.OnFinishQueryInterface onFinishQueryInterface) {
        clients.clear(); // reset clients array

        database.collection("Clients").orderBy("firstName").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                //save client data from database to clients array
                Map<String, Object> data = document.getData();
                String email = (String) data.get("email");
                String uid = document.getId();
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
        }).addOnFailureListener(e -> {
            Log.i(TAG, "Error getting documents: " + e.getMessage());
        });
    }

    public static void deleteClient(String uid) {
        // Delete client from database
        database.collection("Clients").document(uid).delete()
                .addOnSuccessListener(unused -> Log.i(TAG, "Client deleted successfully!"))
                .addOnFailureListener(e -> Log.i(TAG, "Error deleting client: " + e.getMessage()));

        // Delete client image from storage
        storageReference.child("Clients").child(uid).child("profile.jpg").delete()
                .addOnSuccessListener(unused -> Log.i(TAG, "Client image deleted successfully!"))
                .addOnFailureListener(e -> Log.i(TAG, "Error deleting client image: " + e.getMessage()));

        //TODO: Delete other client data from database
    }

    public static void getAllAppointmentTypesFromDatabase(Interfaces.OnFinishQueryInterface onFinishQueryInterface) {
        appointmentTypes.clear(); // reset appointment types array

        database.collection("Appointment Types").orderBy("typeName").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    //save appointment type data from database to appointment types arraylist
                    Map<String, Object> data = document.getData();
                    String typeName = (String) data.get("typeName");
                    String duration = (String) data.get("duration");
                    String price = (String) data.get("price");
                    String uid = document.getId();
                    AppointmentType currAppointmentType = new AppointmentType(typeName, price, duration, uid); //creating a new appointment type
                    appointmentTypes.add(currAppointmentType); //adding appointment type to list
                }
                onFinishQueryInterface.onFinishQuery();
            } else {
                Log.e(TAG, "Error getting documents: " + task.getException());
            }
        });
    }

    public static void addAppointmentTypeToDatabase(AppointmentType appointmentType) {
        database.collection("Appointment Types").document(appointmentType.getUid()).set(appointmentType) //adding appointment type data to database
                .addOnSuccessListener(unused -> Log.i(TAG, "Appointment type added successfully!"))
                .addOnFailureListener(e -> Log.i(TAG, "Error adding appointment type: " + e.getMessage()));
    }

    public static void deleteAppointmentTypeFromDatabase(AppointmentType currAppointmentType) {
        database.collection("Appointment Types").document(currAppointmentType.getUid()).delete()
                .addOnSuccessListener(unused -> Log.i(TAG, "Appointment type deleted successfully!"))
                .addOnFailureListener(e -> Log.i(TAG, "Error deleting appointment type: " + e.getMessage()));
    }

    public static void getAllProductsFromDatabase(Interfaces.OnFinishQueryInterface onFinishQueryInterface) {
        products.clear(); // reset products array

        database.collection("Products").orderBy("name").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    //save product data from database to products arraylist
                    Map<String, Object> data = document.getData();
                    String productName = (String) data.get("name");
                    String productPrice = data.get("price") + "";
                    String productDescription = (String) data.get("description");
                    String productCategory = (String) data.get("category");
                    String productQuantity = data.get("quantity") + "";
                    String uid = document.getId();
                    Product currProduct = new Product(uid, productName, productDescription, productCategory, Double.parseDouble(productPrice), Integer.parseInt(productQuantity)); //creating a new product
                    products.add(currProduct); //adding product to list
                }
                onFinishQueryInterface.onFinishQuery();
            } else {
                Log.e(TAG, "Error getting documents: " + task.getException());
            }
        });
    }

    public static void addProductToFirebase(Product product, FragmentActivity currActivity) {
        database.collection("Products").document(product.getUid()).set(product) //adding user data to database
                .addOnSuccessListener(unused -> Toast.makeText(currActivity, "Product added/updated successfully!", Toast.LENGTH_SHORT).show()) //Registration successful
                .addOnFailureListener(e -> Toast.makeText(currActivity, "Product adding/updating failed!", Toast.LENGTH_SHORT).show()); //Registration failed
    }

    public static void uploadRelevantProductInfo(Product currProduct, FragmentActivity currActivity, StorageReference savedImagePath) {
        DatabaseUtils.addProductToFirebase(currProduct, currActivity);//add the user to the database

        if (profilePicSelected) {
            DatabaseUtils.uploadImageToFirebase(savedImagePath, profilePicUri, currActivity);
            profilePicSelected = false;
        }
    }

    public static void deleteProductFromDatabase(String productUid) {
        database.collection("Products").document(productUid).delete()
                .addOnSuccessListener(unused -> Log.i(TAG, "Product deleted successfully!"))
                .addOnFailureListener(e -> Log.i(TAG, "Error deleting product: " + e.getMessage()));
        storageReference.child("Products").child(productUid).child("profile.jpg").delete()
                .addOnSuccessListener(unused -> Log.i(TAG, "Product image deleted successfully!"))
                .addOnFailureListener(e -> Log.i(TAG, "Error deleting product image: " + e.getMessage()));
    }

    public static void loadImageToImageView(StorageReference profilePicReference, ImageView imageView, FragmentActivity currActivity) {
        profilePicReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Toast.makeText(currActivity, "downloaded!", Toast.LENGTH_SHORT).show();
            Glide.with(currActivity).load(uri).into(imageView);
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to load image! " +e.getMessage());
            //failed probably due to the profile pic not existing (was not uploaded)
        });
    }

    public static void addAppointmentToDatabase(Appointment newAppointment) {
        database.collection("Appointments").document(newAppointment.getDate()).collection("AppointmentsPerDate").document(newAppointment.getUid()).set(newAppointment) //adding appointment data to database
                .addOnSuccessListener(unused -> Log.i(TAG, "Appointment added successfully!"))
                .addOnFailureListener(e -> Log.i(TAG, "Error adding appointment: " + e.getMessage()));
    }

    public static void deleteAppointmentFromDatabase(Appointment currAppointment) {
        database.collection("Appointments").document(currAppointment.getDate()).collection("AppointmentsPerDate").document(currAppointment.getUid()).delete()
                .addOnSuccessListener(unused -> Log.i(TAG, "Appointment deleted successfully!"))
                .addOnFailureListener(e -> Log.i(TAG, "Error deleting appointment: " + e.getMessage()));
    }

    public static void getAllAppointmentsFromDatabase(Interfaces.OnFinishQueryInterface onFinishQueryInterface) {
        appointments.clear(); // reset appointments array

        database.collectionGroup("AppointmentsPerDate").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    //save appointment data from database to appointments arraylist
                    Map<String, Object> data = document.getData();
                    String date = (String) data.get("date");
                    String time = (String) data.get("time");
                    String clientUid = (String) data.get("clientUid");
                    String appointmentTypeUid = (String) data.get("appointmentTypeUid");
                    String uid = document.getId();
                    Appointment currAppointment = new Appointment(uid, clientUid, appointmentTypeUid, date, time); //creating a new appointment
                    appointments.add(currAppointment); //adding appointment to list
                }
                onFinishQueryInterface.onFinishQuery();
            } else {
                Log.e(TAG, "Error getting documents: " + task.getException());
            }
        });
    }

    public static AppointmentType findAppointmentType(String appointmentTypeUid) {
        for (AppointmentType currAppointmentType : appointmentTypes) {
            if (currAppointmentType.getUid().equals(appointmentTypeUid)) {
                return currAppointmentType;
            }
        }
        return null;
    }

    public static Client findClient(String clientUid) {
        for (Client currClient : clients) {
            if (currClient.getUid().equals(clientUid)) {
                return currClient;
            }
        }
        return null;
    }
}
