package com.example.abm_improved.Utils;

import android.app.TimePickerDialog;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.util.Calendar;

public class PopupTimePicker {

    private String hourString;
    private String minuteString;

    public PopupTimePicker(FragmentActivity activity, TextView appointmentTimeTextView) {
        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hourString = selectedHour < 10 ? "0" + selectedHour : "" + selectedHour;
            minuteString = selectedMinute < 10 ? "0" + selectedMinute : "" + selectedMinute;

            appointmentTimeTextView.setText(getTimeString());
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, timeSetListener, 0, 0, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public String getTimeString() {
        return hourString + ":" + minuteString;
    }

    public static String getTimeString(int time) {
        int hourInt = time / 100;
        int minuteInt = time % 100;
        String hourString = hourInt < 10 ? "0" + hourInt : "" + hourInt;
        String minuteString = minuteInt < 10 ? "0" + minuteInt : "" + minuteInt;

        return hourString + ":" + minuteString;
    }

    public static String getTimeString(int hour, int minute) {
        String hourString = hour < 10 ? "0" + hour : "" + hour;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;

        return hourString + ":" + minuteString;
    }

    public static void setCurrentTime(TextView appointmentTimeTextView) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        appointmentTimeTextView.setText(getTimeString((hour * 100) + minute));
    }

    public static String reformatToBasicString(String timeString) {
        String[] timeArray = timeString.split(":");
        return timeArray[0] + timeArray[1];
    }
}
