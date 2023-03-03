package com.example.abm_improved.LoginAndRegister;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.abm_improved.Appointments.AppointmentsMainFragment;
import com.example.abm_improved.BaseActivity;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.OnFinishQueryInterface;

public class LoginFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        if (DatabaseUtils.userLoggedIn()) { // if user is already logged in, move to appointments main activity
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new AppointmentsMainFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else { // if user is not logged in, show login screen
            requireActivity().setTitle("Login");

            // Basic XML fields
            Button loginButton = view.findViewById(R.id.loginButton);
            Button registerButton = view.findViewById(R.id.registerButton);
            EditText emailEditText = view.findViewById(R.id.emailEditText);
            EditText passwordEditText = view.findViewById(R.id.passwordEditText);
            requireActivity().findViewById(R.id.AppBarLayout).setVisibility(View.GONE);

            //Login button onclick listener
            loginButton.setOnClickListener(v -> {
                String emailStr = emailEditText.getText().toString();
                String passwordStr = passwordEditText.getText().toString();
                if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                    Toast.makeText(requireActivity(), "Fill in all both email and password fields", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseUtils.loginUser(emailStr, passwordStr, LoginFragment.this, new onUserLoggedIn());
                }
            });

            //Register button onclick listener
            registerButton.setOnClickListener(v -> {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        //hide keyboard -->
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        // <-- hide keyboard
    }

    private class onUserLoggedIn implements OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            // Upon success,
            // Init menu sidebar again (to show the correct menu items and user)
            BaseActivity baseActivity = (BaseActivity) requireActivity();
            baseActivity.initMenuSideBar();

            // move to appointments main activity
            Bundle bundle = new Bundle();
            AppointmentsMainFragment appointmentsMainFragment = new AppointmentsMainFragment();
            appointmentsMainFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, appointmentsMainFragment).addToBackStack(null).commit();
        }
    }
}