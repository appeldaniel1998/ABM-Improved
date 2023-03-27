package com.example.abm_improved;

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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.abm_improved.AppointmentTypes.AppointmentTypesMainFragment;
import com.example.abm_improved.Appointments.AppointmentsBaseFragment;
import com.example.abm_improved.Cart.CartMainFragment;
import com.example.abm_improved.Clients.ClientsMainFragment;
import com.example.abm_improved.DataClasses.Client;
import com.example.abm_improved.HistoryAnalytics.HistoryFragment;
import com.example.abm_improved.LoginAndRegister.LoginFragment;
import com.example.abm_improved.Products.ProductsMainFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.example.abm_improved.Utils.Interfaces.*;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnChooseProfilePicListener {

    // For menu side bar
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initMenuSideBar();

        //load default fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new LoginFragment());
        fragmentTransaction.commit();
        setFilePicker();
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

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void initMenuSideBar() {
        toolbar = findViewById(R.id.toolbar); // init toolbar
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
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
                    MenuItem cart = menu.findItem(R.id.menuItemCart);
                    MenuItem clients = menu.findItem(R.id.menuItemClients);
                    MenuItem appointmentTypes = menu.findItem(R.id.menuItemAppointmentTypes);
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

                        menu.findItem(R.id.menuItemAnalytics).setTitle("History");
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
    // Menu selection
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers(); // close nav drawer
        if (item.getItemId() == R.id.menuItemAppointments) {
            Bundle bundle = new Bundle();
            AppointmentsBaseFragment appointmentsBaseFragment = new AppointmentsBaseFragment();
            appointmentsBaseFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, appointmentsBaseFragment).addToBackStack(null).commit();
            return true;
        } else if (item.getItemId() == R.id.menuItemAppointmentTypes) {
            Bundle bundle = new Bundle();
            AppointmentTypesMainFragment appointmentTypesMainFragment = new AppointmentTypesMainFragment();
            appointmentTypesMainFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, appointmentTypesMainFragment).addToBackStack(null).commit();
            return true;
        } else if (item.getItemId() == R.id.menuItemProducts) {
            Bundle bundle = new Bundle();
            ProductsMainFragment productsMainFragment = new ProductsMainFragment();
            productsMainFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, productsMainFragment).addToBackStack(null).commit();
            return true;
        } else if (item.getItemId() == R.id.menuItemClients) {
            Bundle bundle = new Bundle();
            ClientsMainFragment clientsMainFragment = new ClientsMainFragment();
            clientsMainFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, clientsMainFragment).addToBackStack(null).commit();
            return true;
        } else if (item.getItemId() == R.id.menuItemAnalytics) {
            Bundle bundle = new Bundle();
            HistoryFragment historyFragment = new HistoryFragment();
            historyFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, historyFragment).addToBackStack(null).commit();
            return true;
        } else if (item.getItemId() == R.id.menuItemCart) {
            Bundle bundle = new Bundle();
            CartMainFragment cartMainFragment = new CartMainFragment();
            cartMainFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cartMainFragment).addToBackStack(null).commit();
            return true;
        } else if (item.getItemId() == R.id.menuItemSignOut) {
            auth.signOut();
            if (auth.getCurrentUser() == null) {
                Toast.makeText(this, "User Signed Out!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User Signed Out Failed!", Toast.LENGTH_SHORT).show();
            }
            Bundle bundle = new Bundle();
            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).addToBackStack(null).commit();
            return true;
        } else return false;
    }

    @Override
    // For choosing media from gallery
    public void onImageClick(ImageView imageView) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // change to "*/*" to show all files
        selectedImageAddEditDataFragment = imageView;
        filePicker.launch(intent);
    }
}