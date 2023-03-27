package com.example.abm_improved.Appointments;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class AppointmentsBaseFragment extends BaseFragment {

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
        insertNestedFragment(new AppointmentsMonthlyViewFragment());
    }

    // Embeds the child fragment dynamically
    private void insertNestedFragment(BaseFragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.child_fragment_container, fragment).addToBackStack(null).commit();
    }

    // Method to initialize the bottom navigation bar onclick listener (on item selected listener)
    public void onBottomNavBarClickListener(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menuItemMonthly) {
                insertNestedFragment(new AppointmentsMonthlyViewFragment());
            } else if (item.getItemId() == R.id.menuItemWeekly) {
                insertNestedFragment(new AppointmentsWeeklyViewFragment());
            } else if (item.getItemId() == R.id.menuItemList) {
                insertNestedFragment(new AppointmentsListViewFragment());
            }
            return true;
        });
    }
}