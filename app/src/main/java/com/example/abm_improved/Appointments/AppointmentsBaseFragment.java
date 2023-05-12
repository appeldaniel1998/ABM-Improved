package com.example.abm_improved.Appointments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.BaseActivity;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppointmentsBaseFragment extends BaseFragment {

    private static final String TAG = "AppointmentsBaseFragment";
    private NavController nestedNavController;

//    BaseActivity baseActivity = (BaseActivity) requireActivity();

    protected static final int APPOINTMENTS_LIST_VIEW = R.id.appointmentsListViewFragment;
    protected static final int APPOINTMENTS_WEEKLY_VIEW = R.id.appointmentsWeeklyViewFragment;
    protected static final int APPOINTMENTS_MONTHLY_VIEW = R.id.appointmentsMonthlyViewFragment;
//    protected static int lastFragmentId = APPOINTMENTS_MONTHLY_VIEW; //default fragment to show (updated in nav-graph)

    public NavController getNavController() {
        return nestedNavController;
    }

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
        NavigationUI.setupWithNavController(navigationView, nestedNavController);
        handleInnerFragmentsBackButton();
    }

    private void handleInnerFragmentsBackButton() {
        // Add the OnDestinationChangedListener in order to handle the back button on non-top-level destinations of the bottom navigation bar
        nestedNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            BaseActivity baseActivity = (BaseActivity) requireActivity();
            boolean isTopLevelDestination = destination.getId() == APPOINTMENTS_LIST_VIEW || destination.getId() == APPOINTMENTS_WEEKLY_VIEW
                    || destination.getId() == APPOINTMENTS_MONTHLY_VIEW;
            if (isTopLevelDestination) {
                baseActivity.appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.appointmentsBaseFragment,
                        R.id.appointmentsTypesMainFragment,
                        R.id.productsMainFragment,
                        R.id.clientsMainFragment,
                        R.id.historyFragment,
                        R.id.cartMainFragment,
                        R.id.loginFragment,
                        APPOINTMENTS_LIST_VIEW,
                        APPOINTMENTS_WEEKLY_VIEW,
                        APPOINTMENTS_MONTHLY_VIEW)
                        .setOpenableLayout(baseActivity.drawerLayout)
                        .build();
                baseActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                baseActivity.appBarConfiguration = new AppBarConfiguration.Builder(destination.getId()).build();
                baseActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
            NavigationUI.setupActionBarWithNavController(baseActivity, nestedNavController, baseActivity.appBarConfiguration);
        });
    }

}