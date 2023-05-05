package com.example.abm_improved.Appointments;

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

import com.example.abm_improved.Appointments.Adapters.AppointmentsRecyclerAdapter;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Clients.Adapters.ClientsRecyclerAdapter;
import com.example.abm_improved.Clients.AddNewClientFragment;
import com.example.abm_improved.Clients.ClientsMainFragment;
import com.example.abm_improved.Clients.EditClientFragment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.example.abm_improved.Utils.PopupDatePicker;

public class AppointmentsListViewFragment extends BaseFragment {

    private Button addAppointmentButton;

    private RecyclerView appointmentsRecyclerView;
    private AppointmentsRecyclerAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ProgressBar progressBar;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_appointments_list_view, container, false);

        navController = NavHostFragment.findNavController(AppointmentsListViewFragment.this);

        progressBar = requireActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        addAppointmentButton = view.findViewById(R.id.addNewAppointmentButton);

        recyclerViewLayoutManager = new LinearLayoutManager(requireActivity());
        appointmentsRecyclerView = view.findViewById(R.id.recyclerViewAppointments);
        appointmentsRecyclerView.hasFixedSize();
        appointmentsRecyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        appointmentsRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        DatabaseUtils.getAllAppointmentTypesFromDatabase(new Interfaces.DoNothing());
        DatabaseUtils.getAllAppointmentsFromDatabase(new OnGetAllAppointments());

        addAppointmentButton.setOnClickListener(v -> {
            int[] todayDate = PopupDatePicker.getTodayDateAsInts(); // get today's date as ints (3 ints in the array)
            int year = todayDate[0];
            int month = todayDate[1];
            int day = todayDate[2];
            navController.navigate(AppointmentsMonthlyViewFragmentDirections.actionAppointmentsMonthlyViewFragmentToAddNewAppointmentFragment(year, month, day)); // navigate to add new appointment fragment
        });
        return view;
    }

    private class OnGetAllAppointments implements Interfaces.OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            recyclerViewAdapter = new AppointmentsRecyclerAdapter(DatabaseUtils.getAppointments());
            appointmentsRecyclerView.setAdapter(recyclerViewAdapter);

            progressBar.setVisibility(View.GONE); // disable loading screen

            //onclick of each item in the recycle view (appointment in the list)
            recyclerViewAdapter.setOnItemClickListener(position -> {
                navController.navigate(AppointmentsListViewFragmentDirections.actionAppointmentsListViewFragmentToEditAppointmentFragment(position));
            });
        }
    }
}