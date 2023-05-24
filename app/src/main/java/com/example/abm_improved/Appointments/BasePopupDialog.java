package com.example.abm_improved.Appointments;

import android.view.Window;

import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class BasePopupDialog extends DialogFragment {
    @Override
    public void onStart() {
        super.onStart();

        // Get the current window
        Window window = Objects.requireNonNull(getDialog()).getWindow();

        if (window != null) {
//            double scale = getResources().getDisplayMetrics().density; // Get the screen's density scale

            // The offset in pixels to dimension we want to get (width or height)
            int offsetWidth = getResources().getDisplayMetrics().widthPixels;
            int offsetHeight = getResources().getDisplayMetrics().heightPixels;

            // The new size in pixels
            int width = (int) (offsetWidth * 0.95);
            int height = (int) (offsetHeight * 0.6);

            window.setLayout(width, height); // Set the layout parameters of the window
        }
    }
}
