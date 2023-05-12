package com.example.abm_improved.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

/**
 * Class function code inspired by the tutorial and the accompanying github:
 * https://www.youtube.com/watch?v=qCoidM98zNk
 * https://github.com/codeWithCal/DatePickerTutorial/blob/master/app/src/main/java/codewithcal/au/datepickertutorial/MainActivity.java
 */
public class DateUtils {

    /**
     * Function to return the string representing today's date
     *
     * @return string representing a date (e.g. 1 January 2021)
     */
    public static String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // Jan = 0, so increment by 1 for the sake of understandability
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public static int[] getTodayDateAsInts() {
        int[] date = new int[3];
        Calendar cal = Calendar.getInstance();
        date[0] = cal.get(Calendar.YEAR);
        date[1] = cal.get(Calendar.MONTH) + 1; // Jan = 0, so increment by 1 for the sake of understandability
        date[2] = cal.get(Calendar.DAY_OF_MONTH);
        return date;
    }

    /**
     * Construct a string to present to the user after choosing a date. Given the day, month and year, create and return an appropriate string
     *
     * @param day   day of month
     * @param month month of year
     * @param year  year
     * @return constructed string
     */
    public static String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    /**
     * Given a month as a number, returns the name of the month as a relevant string
     */
    public static String getMonthFormat(int month) {
        if (month == 1) return "January";
        if (month == 2) return "February";
        if (month == 3) return "March";
        if (month == 4) return "April";
        if (month == 5) return "May";
        if (month == 6) return "June";
        if (month == 7) return "July";
        if (month == 8) return "August";
        if (month == 9) return "September";
        if (month == 10) return "October";
        if (month == 11) return "November";
        if (month == 12) return "December";

        //default should never happen
        return "Error";
    }

    //converting to and from database format (2 following functions)
    public static int stringToInt(String date) {
        String[] arr = date.split(" ");
        String day = formatDay(Integer.parseInt(arr[0]));
        String month = getMonthFormat(arr[1]);
        int year = Integer.parseInt(arr[2]);
        String tempDate = year + "" + month + "" + day;
        return Integer.parseInt(tempDate);
    }

    public static String formatDay(int day) {
        if (day >= 10) return day + "";
        else return "0" + day;
    }

    public static String intToString(int date) {
        int day = date % 100;
        int month = (date / 100) % 100;
        int year = date / (100 * 100);
        return makeDateString(day, month, year);
    }

    public static String getMonthFormat(String month) {
        if (month.equals("January")) return "01";
        if (month.equals("February")) return "02";
        if (month.equals("March")) return "03";
        if (month.equals("April")) return "04";
        if (month.equals("May")) return "05";
        if (month.equals("June")) return "06";
        if (month.equals("July")) return "07";
        if (month.equals("August")) return "08";
        if (month.equals("September")) return "09";
        if (month.equals("October")) return "10";
        if (month.equals("November")) return "11";
        if (month.equals("December")) return "12";

        return "00"; // default should never happen
    }

    public static int getTodayDateAsInt() {
        int[] date = getTodayDateAsInts();
        return date[0] * 10000 + date[1] * 100 + date[2];
    }

    /**
     * This method calculates the first day (Sunday) and the last day (Saturday) of the week,
     * respectively 3 weeks prior and 3 weeks after a given date.
     *
     * <p>
     * The input is an integer in the yyyymmdd format. The method returns an array of two integers,
     * where the first integer is the first day of the week from 3 weeks prior to the given date,
     * and the second integer is the last day of the week from 3 weeks after the given date.
     * Both the returned dates are also in the yyyymmdd format.
     * </p>
     *
     * <p>
     * Note: This method assumes that the first day of the week is Sunday and the last day is Saturday.
     * Adjustments may be needed based on the locale.
     * </p>
     *
     * @param date the date as an integer in the format of yyyymmdd
     * @return an array of two integers, where the first integer is the date of the first day
     * of the week from 3 weeks prior to the given date, and the second integer is
     * the date of the last day of the week from 3 weeks after the given date.
     * Both dates are in the yyyymmdd format.
     * @throws DateTimeParseException if the input date cannot be parsed
     */
    public static int[] get3WeeksBeforeAndAfterDates(int date) {
        // Convert the date into a string
        String dateString = Integer.toString(date);

        // Parse the date string into a LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        // Get the first day of the week, 3 weeks prior
        LocalDate startDate = localDate.minusWeeks(3)
                .with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));

        // Get the last day of the week, 3 weeks after
        LocalDate endDate = localDate.plusWeeks(3)
                .with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SATURDAY));

        // Convert the dates back to integers
        int startInt = Integer.parseInt(startDate.format(formatter));
        int endInt = Integer.parseInt(endDate.format(formatter));

        return new int[]{startInt, endInt};
    }

    public static int calendarToInt(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateString = sdf.format(calendar.getTime());
        return Integer.parseInt(dateString);
    }
}
