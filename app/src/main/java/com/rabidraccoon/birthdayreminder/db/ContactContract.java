package com.rabidraccoon.birthdayreminder.db;

import android.provider.BaseColumns;

/**
 * Created by a-rmz on 7/22/16.
 */
public final class ContactContract {

    public ContactContract() {}

    public static abstract class ContactEntry implements BaseColumns {

        // Table
        protected static final String TABLE_CONTACT = "t_contact";

        // Rows
        protected static String KEY_NAME = "name";
        protected static String KEY_DAY = "bd_day";
        protected static String KEY_MONTH = "bd_month";
        protected static String KEY_YEAR = "bd_year";
        protected static String KEY_PHONE = "phone";
        protected static String KEY_PHOTO = "photo";
    }

}
