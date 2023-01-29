package com.example.abm_improved;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.abm_improved.LoginAndRegister.LoginFragment;
import com.example.abm_improved.LoginAndRegister.RegisterFragment;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RegisterFragment.OnChooseProfilePicListener {
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ImageView selectedImageAddEditDataFragment;
    private ActivityResultLauncher<Intent> filePicker;


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
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
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
        return false;
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