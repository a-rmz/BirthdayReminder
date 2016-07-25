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
        int month = getMonth(date);
        int day = getDay(date);

        return months_en[month-1] + ", " + day;
    }

    public static String toHuman(int day, int month) {
        return months_en[month-1] + ", " + day;
    }

    public static int calculateAge(String dateString) {
        int year = getYear(dateString);
        int month = getMonth(dateString);
        int day = getDay(dateString);


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

    public static int getYear(String date) {
        return Integer.parseInt(date.substring(0, 4));
    }

    public static int isSameMonth(int month1, int month2) {
        return month1 - month2;
    }

    public static boolean isItsBirthday(int day, int todayDay, int month, int todayMonth) {
        return (day == todayDay) && (month == todayMonth);
    }


}
