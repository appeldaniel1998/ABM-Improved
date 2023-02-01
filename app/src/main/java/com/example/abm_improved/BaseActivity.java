package com.example.abm_improved;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
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

import com.example.abm_improved.AppointmentTypes.AppointmentTypesMainFragment;
import com.example.abm_improved.Appointments.AppointmentsMainFragment;
import com.example.abm_improved.Cart.CartMainFragment;
import com.example.abm_improved.Clients.ClientsMainFragment;
import com.example.abm_improved.HistoryAnalytics.HistoryFragment;
import com.example.abm_improved.LoginAndRegister.LoginFragment;
import com.example.abm_improved.LoginAndRegister.RegisterFragment;
import com.example.abm_improved.Products.ProductsMainFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RegisterFragment.OnChooseProfilePicListener {
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ImageView selectedImageAddEditDataFragment;
    private ActivityResultLauncher<Intent> filePicker;

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initMenuSideBar();

        //load default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new LoginFragment());
        fragmentTransaction.commit();
        setFilePicker();

    }

    private void setFilePicker() {
        filePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                RegisterFragment.profilePicUri = result.getData().getData();
                if (RegisterFragment.profilePicUri != null) {
                    selectedImageAddEditDataFragment.setImageURI(RegisterFragment.profilePicUri);
                    RegisterFragment.profilePicSelected = true;
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
    }

    @Override
    // Menu selection
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers(); // close nav drawer
        if (item.getItemId() == R.id.menuItemAppointments) {
            Bundle bundle = new Bundle();
            AppointmentsMainFragment appointmentsMainFragment = new AppointmentsMainFragment();
            appointmentsMainFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, appointmentsMainFragment).addToBackStack(null).commit();
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
            FirebaseAuth auth = FirebaseAuth.getInstance();
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