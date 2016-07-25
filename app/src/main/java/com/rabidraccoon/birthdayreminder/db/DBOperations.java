package com.rabidraccoon.birthdayreminder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.rabidraccoon.birthdayreminder.utils.Contact;
import com.rabidraccoon.birthdayreminder.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a-rmz on 7/23/16.
 */
public class DBOperations {

    public static final int SORT_BY_NAME = 0;
    public static final int SORT_BY_DATE = 1;

    DBHandler dbHandler;

    public DBOperations(Context context) {
        dbHandler = new DBHandler(context);
    }

    public long addContact(int ID, String name, String date, String phone, String photo) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues newContact = new ContentValues();

        newContact.put(ContactContract.ContactEntry._ID, ID);
        newContact.put(ContactContract.ContactEntry.KEY_NAME, name);
        newContact.put(ContactContract.ContactEntry.KEY_DAY, DateUtils.getDay(date));
        newContact.put(ContactContract.ContactEntry.KEY_MONTH, DateUtils.getMonth(date));
        newContact.put(ContactContract.ContactEntry.KEY_YEAR, DateUtils.getYear(date));
        newContact.put(ContactContract.ContactEntry.KEY_PHONE, phone);
        newContact.put(ContactContract.ContactEntry.KEY_PHOTO, photo);

        return db.insert(
                ContactContract.ContactEntry.TABLE_CONTACT,
                null,
                newContact);

    }

    public int removeContact(int ID) {
        String SELECTION = ContactContract.ContactEntry._ID + " LIKE ?";
        String SELECTION_ARGS[] = {String.valueOf(ID)};

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        return db.delete(
                ContactContract.ContactEntry.TABLE_CONTACT,
                SELECTION,
                SELECTION_ARGS);
    }

    public int updateContact(int ID, String name, String date, String phone, String photo) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues newContact = new ContentValues();

        newContact.put(ContactContract.ContactEntry.KEY_NAME, name);
        newContact.put(ContactContract.ContactEntry.KEY_DAY, DateUtils.getDay(date));
        newContact.put(ContactContract.ContactEntry.KEY_MONTH, DateUtils.getMonth(date));
        newContact.put(ContactContract.ContactEntry.KEY_YEAR, DateUtils.getYear(date));
        newContact.put(ContactContract.ContactEntry.KEY_PHONE, phone);
        newContact.put(ContactContract.ContactEntry.KEY_PHOTO, photo);

        // Which row to update, based on the ID
        String SELECTION = ContactContract.ContactEntry._ID + " LIKE ?";
        String SELECTION_ARGS[] = {String.valueOf(ID)};

        return db.update(
                ContactContract.ContactEntry.TABLE_CONTACT,
                newContact,
                SELECTION,
                SELECTION_ARGS);

    }

    public Contact getContact(int ID) {
        Contact contact = new Contact();
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String PROJECTION[] = {
                ContactContract.ContactEntry.KEY_NAME,  // 0
                ContactContract.ContactEntry.KEY_DAY,   // 1
                ContactContract.ContactEntry.KEY_MONTH, // 2
                ContactContract.ContactEntry.KEY_YEAR,  // 3
                ContactContract.ContactEntry.KEY_PHONE, // 4
                ContactContract.ContactEntry.KEY_PHOTO  // 5
        };
        String SELECTION = ContactContract.ContactEntry._ID + " LIKE ?";
        String SELECTION_ARGS[] = { String.valueOf(ID) };

        Cursor contactCursor = db.query(
                ContactContract.ContactEntry.TABLE_CONTACT,
                PROJECTION,
                SELECTION,
                SELECTION_ARGS,
                null,
                null,
                null
        );

        contactCursor.close();

        contact.setID(ID);
        contact.setName(contactCursor.getString(0));
        contact.setDate(contactCursor.getInt(1), contactCursor.getInt(2), contactCursor.getInt(3));
        contact.setPhone(contactCursor.getString(4));
        contact.setPhoto(Uri.parse(contactCursor.getString(5)));

        return contact;
    }

    public Cursor getContactsCursor(int SORT_ORDER) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String PROJECTION[] = {
                ContactContract.ContactEntry._ID,       // 0
                ContactContract.ContactEntry.KEY_NAME,  // 1
                ContactContract.ContactEntry.KEY_DAY,   // 2
                ContactContract.ContactEntry.KEY_MONTH, // 3
                ContactContract.ContactEntry.KEY_YEAR,  // 4
                ContactContract.ContactEntry.KEY_PHONE, // 5
                ContactContract.ContactEntry.KEY_PHOTO  // 6
        };

        String ORDER = (SORT_ORDER == SORT_BY_NAME) ?
                ContactContract.ContactEntry.KEY_NAME + " ASC" : // SORT_BY_NAME
                // Sorts first by month, then by day
                ContactContract.ContactEntry.KEY_MONTH + " ASC, " + ContactContract.ContactEntry.KEY_DAY + " ASC"; // SORT_BY_DATE


        return db.query(
                ContactContract.ContactEntry.TABLE_CONTACT,
                PROJECTION,
                null,
                null,
                null,
                null,
                ORDER
        );

    }

    public List<Integer> getContactIDs() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        List<Integer> ids = new ArrayList<>();

        String PROJECTION[] = {
                ContactContract.ContactEntry._ID
        };

        Cursor cursor = db.query(
                ContactContract.ContactEntry.TABLE_CONTACT,
                PROJECTION,
                null,
                null,
                null,
                null,
                null
        );


        if (cursor != null && cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        return ids;

    }

    public void resetDB() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        dbHandler.onReset(
                db
        );
    }

}
