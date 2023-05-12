package com.example.abm_improved.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.TextView;

import java.util.Calendar;

public class PopupDatePicker {
    public static DatePickerDialog initDatePicker(TextView dateTextView, Activity activity) {
        // when ok is clicked inside the date picker
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = DateUtils.makeDateString(day, month, year);
            dateTextView.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.R.style.Theme_DeviceDefault_Light_Dialog; //Can change style at will

        return new DatePickerDialog(activity, style, dateSetListener, year, month, day);
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());  // Can set max/min date with this line of code
    }
}
