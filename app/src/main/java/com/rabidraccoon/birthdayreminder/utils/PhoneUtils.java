package com.rabidraccoon.birthdayreminder.utils;

/**
 * Created by alex on 5/29/16.
 */
public class PhoneUtils {

    public static String formatPhone(String phone) {
        String formatted = phone;

        formatted = formatted.replace("-", "");
        formatted = formatted.replace(" ", "");

        return formatted;
    }

}
