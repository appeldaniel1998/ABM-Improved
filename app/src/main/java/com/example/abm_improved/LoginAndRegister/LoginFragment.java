package com.example.abm_improved.LoginAndRegister;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.abm_improved.BaseActivity;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.Interfaces;

public class LoginFragment extends BaseFragment {

    BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        baseActivity = (BaseActivity) requireActivity();
        if (DatabaseUtils.userLoggedIn()) { // if user is already logged in, move to appointments main fragment
            baseActivity.navController.navigate(R.id.action_loginFragment_to_appointmentsBaseFragment);
        } else { // if user is not logged in, show login screen

            // Basic XML fields
            Button loginButton = view.findViewById(R.id.loginButton);
            Button registerButton = view.findViewById(R.id.doneButton);
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
                baseActivity.navController.navigate(R.id.action_loginFragment_to_registerFragment);
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

    private class onUserLoggedIn implements Interfaces.OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            // Upon success, Init menu sidebar again (to show the correct menu items and user)
            baseActivity.initMenuSideBar();

            // move to appointments main activity
            NavController navController = NavHostFragment.findNavController(LoginFragment.this);
            navController.navigate(R.id.action_loginFragment_to_appointmentsBaseFragment);
        }
    }
}