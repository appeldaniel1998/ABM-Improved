package com.example.abm_improved.Appointments;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class AppointmentsBaseFragment extends BaseFragment {

    private static final String TAG = "AppointmentsBaseFragment";
    private static final int LAST_FRAGMENT_SELECTED_MONTHLY = 0;
    private static final int LAST_FRAGMENT_SELECTED_WEEKLY = 1;
    private static final int LAST_FRAGMENT_SELECTED_LIST = 2;

    private int lastFragmentSelected = LAST_FRAGMENT_SELECTED_MONTHLY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_base, container, false);
        requireActivity().findViewById(R.id.AppBarLayout).setVisibility(View.VISIBLE); //make toolbar visible

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        onBottomNavBarClickListener(bottomNavigationView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if (lastFragmentSelected == LAST_FRAGMENT_SELECTED_MONTHLY) insertNestedFragment(new AppointmentsMonthlyViewFragment());
        else if (lastFragmentSelected == LAST_FRAGMENT_SELECTED_WEEKLY) insertNestedFragment(new AppointmentsWeeklyViewFragment());
        else if (lastFragmentSelected == LAST_FRAGMENT_SELECTED_LIST) insertNestedFragment(new AppointmentsListViewFragment());
    }

    // Embeds the child fragment dynamically
    private void insertNestedFragment(BaseFragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.child_fragment_container, fragment).addToBackStack(null).commit();
    }

    // Method to initialize the bottom navigation bar onclick listener (on item selected listener)
    public void onBottomNavBarClickListener(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menuItemMonthly) {
                lastFragmentSelected = LAST_FRAGMENT_SELECTED_MONTHLY;
                insertNestedFragment(new AppointmentsMonthlyViewFragment());
            } else if (item.getItemId() == R.id.menuItemWeekly) {
                lastFragmentSelected = LAST_FRAGMENT_SELECTED_WEEKLY;
                insertNestedFragment(new AppointmentsWeeklyViewFragment());
            } else if (item.getItemId() == R.id.menuItemList) {
                lastFragmentSelected = LAST_FRAGMENT_SELECTED_LIST;
                insertNestedFragment(new AppointmentsListViewFragment());
            }
            return true;
        });
    }
}