package com.example.abm_improved.LoginAndRegister;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.abm_improved.Appointments.AppointmentsMainFragment;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.R;

public class LoginFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        requireActivity().setTitle("Login");

        // basic XML fields
        Button loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);

        //Login button onclick listener
        loginButton.setOnClickListener(v -> {
            String emailStr = emailEditText.getText().toString();
            if (emailStr.isEmpty()) emailStr = "";
            String passwordStr = passwordEditText.getText().toString();
            if (passwordStr.isEmpty()) passwordStr = "";
            DatabaseUtils.loginUser(emailStr, passwordStr, LoginFragment.this);
        });

        //Register button onclick listener
        registerButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        return view;
    }

    public void userLoggedIn() {
        //upon success, move to appointments main activity
        Bundle bundle = new Bundle();
        AppointmentsMainFragment appointmentsMainFragment = new AppointmentsMainFragment();
        appointmentsMainFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, appointmentsMainFragment).addToBackStack(null).commit();
    }
}