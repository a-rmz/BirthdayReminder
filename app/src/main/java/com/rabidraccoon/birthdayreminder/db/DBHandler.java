package com.rabidraccoon.birthdayreminder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by a-rmz on 7/22/16.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Contacts.db";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called to create the DB
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_DB =
                "CREATE TABLE " + ContactContract.ContactEntry.TABLE_CONTACT + " (" +
                    ContactContract.ContactEntry._ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ContactContract.ContactEntry.KEY_NAME    + " TEXT NOT NULL, " +
                    ContactContract.ContactEntry.KEY_DAY     + " INTEGER NOT NULL, " +
                    ContactContract.ContactEntry.KEY_MONTH   + " INTEGER NOT NULL, " +
                    ContactContract.ContactEntry.KEY_YEAR    + " INTEGER, " +
                    ContactContract.ContactEntry.KEY_PHONE   + " TEXT, " +
                    ContactContract.ContactEntry.KEY_PHOTO   + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_DB);
    }

    // Called when the DB version in the device needs to be updated
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +
                oldVersion + " to " + newVersion + ", which will destroy all old data");
        // Upgrade the existing database to conform to the new
        // version. Multiple previous versions can be handled
        // by comparing oldVersion and newVersion values.
        // The simplest case is to drop the old table and
        // create a new one.
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + ContactContract.ContactEntry.TABLE_CONTACT);
        // Create a new one.
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
