package com.example.abm_improved;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.abm_improved.DataClasses.Client;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.example.abm_improved.Utils.Interfaces.*;

import java.util.Objects;
import java.util.Set;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnChooseProfilePicListener {

    // For menu side bar
    private Toolbar toolbar;
    public DrawerLayout drawerLayout;
    // ------------------------

    // For Firebase Access
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    // ------------------------

    // For choosing profile pic
    private ImageView selectedImageAddEditDataFragment;
    private ActivityResultLauncher<Intent> filePicker;
    public static boolean profilePicSelected = false;
    public static Uri profilePicUri;
    // ------------------------

    // For Nav Controller
    public NavController navController;
    public AppBarConfiguration appBarConfiguration;
    // ------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        navController = NavHostFragment.findNavController(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)));
        initMenuSideBar();
    }

    private void setFilePicker() {
        filePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                assert result.getData() != null;
                profilePicUri = result.getData().getData();
                if (profilePicUri != null) {
                    selectedImageAddEditDataFragment.setImageURI(profilePicUri);
                    profilePicSelected = true;
                }
            }
        });
    }

    public void initMenuSideBar() {
        toolbar = findViewById(R.id.toolbar); // init toolbar
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null); //make icons in nav drawer colourful (not grayscale)

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.appointmentsBaseFragment,
                R.id.appointmentsTypesMainFragment,
                R.id.productsMainFragment,
                R.id.clientsMainFragment,
                R.id.historyFragment,
                R.id.cartMainFragment,
                R.id.loginFragment)
                .setOpenableLayout(drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        initMenuHeader(navigationView.getMenu());
    }

    //Initializing the header of the menu. Used once in the initMenuSideBar() method above
    private void initMenuHeader(Menu menu) {
        FirebaseUser user = this.auth.getCurrentUser();
        if (user != null) {
            String userUid = user.getUid();

            database.collection("Clients").document(userUid).get().addOnSuccessListener(documentSnapshot -> {
                Client currUser = documentSnapshot.toObject(Client.class);

                ImageView profilePicNavBar = findViewById(R.id.profileImageMenuHeader);
                StorageReference profilePicReference = storageReference.child("Clients").child(userUid).child("profile.jpg");
                //Connecting with Firebase storage and retrieving image
                profilePicReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(BaseActivity.this).load(uri).into(profilePicNavBar);
                });

                TextView name = findViewById(R.id.nameMenuHeader);
                if (currUser != null) {
                    //Toggle visibility for menu items in accordance to whether the user is a client or a manager
                    MenuItem cart = menu.findItem(R.id.cartMainFragment);
                    MenuItem clients = menu.findItem(R.id.clientsMainFragment);
                    MenuItem appointmentTypes = menu.findItem(R.id.appointmentsTypesMainFragment);
                    if (currUser.getManager()) { // A manager
                        // remove any page which a client can get no access to
                        cart.setVisible(false);

                        // show all pages which could have been hidden by a client
                        clients.setVisible(true);
                        appointmentTypes.setVisible(true);

                    } else { // A client
                        // remove any page which a manager can get no access to
                        clients.setVisible(false);
                        appointmentTypes.setVisible(false);

                        // show all pages which could have been hidden by a manager
                        cart.setVisible(true);

                        menu.findItem(R.id.historyFragment).setTitle("History");
                    }


                    // Set name and email in the menu screen header of each page
                    String fullName = currUser.getFirstName() + " " + currUser.getLastName();
                    name.setText(fullName);

                    TextView email = findViewById(R.id.emailMenuHeader);
                    email.setText(currUser.getEmail());

                    TextView isManager = findViewById(R.id.isManagerMenuHeader);
                    System.out.println("Is Manager: " + currUser.getManager());
                    if (currUser.getManager()) {
                        isManager.setText("Manager");
                    } else {
                        isManager.setText("Client");
                    }
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        if (item.getItemId() == R.id.loginFragment) { // if the "sign out" button is pressed
            auth.signOut();
            if (auth.getCurrentUser() == null) {
                Toast.makeText(this, "User Signed Out!", Toast.LENGTH_SHORT).show();
                navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.navigate(R.id.action_global_loginFragment); // Use the destination ID directly, not the global action
                return true;
            } else {
                Toast.makeText(this, "User Signed Out Failed!", Toast.LENGTH_SHORT).show();
            }
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    // For choosing media from gallery
    public void onImageClick(ImageView imageView) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // change to "*/*" to show all files
        selectedImageAddEditDataFragment = imageView;
        filePicker.launch(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (navController == null) {
            navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        }
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

}