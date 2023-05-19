package com.example.abm_improved.Appointments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.BaseActivity;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class AppointmentsBaseFragment extends BaseFragment {

    private static final String TAG = "AppointmentsBaseFragment";
    private NavController nestedNavController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_base, container, false);
        requireActivity().findViewById(R.id.AppBarLayout).setVisibility(View.VISIBLE); //make toolbar visible
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavHostFragment nestedNavHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert nestedNavHostFragment != null;
        nestedNavController = nestedNavHostFragment.getNavController();
        BottomNavigationView navigationView = view.findViewById(R.id.bottom_navigation);
        navigationView.setItemIconTintList(null); //make icons in nav drawer colourful (not grayscale)
//
//        navigationView.setOnItemSelectedListener(item -> {
//            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(item.getItemId(), true).build();
//            nestedNavController.navigate(item.getItemId(), null, navOptions);
//            return true;
//        });

        NavigationUI.setupWithNavController(navigationView, nestedNavController);
        handleInnerFragmentsBackButton();
    }

    private void handleInnerFragmentsBackButton() {
        // Add the OnDestinationChangedListener in order to handle the back button on non-top-level destinations of the bottom navigation bar
        nestedNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            BaseActivity baseActivity = (BaseActivity) requireActivity();
            boolean isTopLevelDestination = destination.getId() == R.id.appointmentsListViewFragment || destination.getId() == R.id.appointmentsWeeklyViewFragment
                    || destination.getId() == R.id.appointmentsMonthlyViewFragment || destination.getId() == R.id.appointmentsThreeDaysViewFragment
                    || destination.getId() == R.id.appointmentsDailyViewFragment;
            if (isTopLevelDestination) {
                baseActivity.appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.appointmentsBaseFragment,
                        R.id.appointmentsTypesMainFragment,
                        R.id.productsMainFragment,
                        R.id.clientsMainFragment,
                        R.id.historyFragment,
                        R.id.cartMainFragment,
                        R.id.loginFragment,
                        R.id.appointmentsMonthlyViewFragment,
                        R.id.appointmentsWeeklyViewFragment,
                        R.id.appointmentsDailyViewFragment,
                        R.id.appointmentsThreeDaysViewFragment,
                        R.id.appointmentsListViewFragment)
                        .setOpenableLayout(baseActivity.drawerLayout).build();
                baseActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                baseActivity.appBarConfiguration = new AppBarConfiguration.Builder(destination.getId()).build();
                baseActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
            NavigationUI.setupActionBarWithNavController(baseActivity, nestedNavController, baseActivity.appBarConfiguration);
        });
    }
}