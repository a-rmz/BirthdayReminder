package com.rabidraccoon.birthdayreminder.utils;

import java.util.Calendar;

/**
 * Created by alex on 5/22/16.
 */
public class DateUtils {

    private static String months_en[] = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    public static String toHuman(String date) {
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));

        return months_en[month-1] + ", " + day;
    }

    public static int calculateAge(String dateString) {
        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(5, 7));
        int day = Integer.parseInt(dateString.substring(8, 10));


        Calendar dob = Calendar.getInstance();
        dob.set(year, month-1, day);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age;
    }

    public static int getDay(String date) {
        return Integer.parseInt(date.substring(8, 10));
    }

    public static int getMonth(String date) {
        return Integer.parseInt(date.substring(5, 7));
    }

    public static int isSameMonth(int month1, int month2) {
        return month1 - month2;
    }


}
