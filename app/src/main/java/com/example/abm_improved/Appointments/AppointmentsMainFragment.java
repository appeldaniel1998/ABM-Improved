package com.example.abm_improved.Appointments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.R;

public class AppointmentsMainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_appointments_main, container, false);

        requireActivity().findViewById(R.id.AppBarLayout).setVisibility(View.VISIBLE);
        return view;
    }
}