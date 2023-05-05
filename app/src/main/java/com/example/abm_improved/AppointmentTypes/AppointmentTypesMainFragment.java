package com.example.abm_improved.AppointmentTypes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.abm_improved.AppointmentTypes.Adapters.AppointmentTypesRecyclerAdapter;
import com.example.abm_improved.AppointmentTypes.AppointmentTypesMainFragmentDirections;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.LoginAndRegister.LoginFragment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.example.abm_improved.AppointmentTypes.AppointmentTypesMainFragmentDirections;

public class AppointmentTypesMainFragment extends BaseFragment {

    private Button addAppointmentTypeButton;

    private RecyclerView appointmentTypesRecyclerView;
    private AppointmentTypesRecyclerAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private ProgressBar progressBar;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_types_main, container, false);

        requireActivity().setTitle("Appointment Types");
        progressBar = requireActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        addAppointmentTypeButton = view.findViewById(R.id.addNewAppointmentTypeButton);

        recyclerViewLayoutManager = new LinearLayoutManager(requireActivity());
        appointmentTypesRecyclerView = view.findViewById(R.id.recyclerView);
        appointmentTypesRecyclerView.hasFixedSize();
        appointmentTypesRecyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        appointmentTypesRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        DatabaseUtils.getAllAppointmentTypesFromDatabase(new AppointmentTypesMainFragment.OnGetAllAppointmentTypes());

        navController = NavHostFragment.findNavController(AppointmentTypesMainFragment.this);

        addAppointmentTypeButton.setOnClickListener(v -> {
            navController.navigate(AppointmentTypesMainFragmentDirections.actionAppointmentsTypesMainFragmentToAddNewAppointmentTypeFragment()); // move to AddNewAppointmentType fragment
        });
        return view;
    }

    private class OnGetAllAppointmentTypes implements Interfaces.OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            recyclerViewAdapter = new AppointmentTypesRecyclerAdapter(DatabaseUtils.getAppointmentTypes());
            appointmentTypesRecyclerView.setAdapter(recyclerViewAdapter);

            progressBar.setVisibility(View.GONE); // disable loading screen

            //onclick of each item in the recycle view (client in the list)
            recyclerViewAdapter.setOnItemClickListener(position -> {
                navController.navigate(AppointmentTypesMainFragmentDirections.actionAppointmentsTypesMainFragmentToEditAppointmentTypeFragment(position)); // move to EditAppointmentType fragment
            });
        }
    }
}