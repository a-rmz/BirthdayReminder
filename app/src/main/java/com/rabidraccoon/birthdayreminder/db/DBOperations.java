package com.rabidraccoon.birthdayreminder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.rabidraccoon.birthdayreminder.utils.Contact;
import com.rabidraccoon.birthdayreminder.utils.DateUtils;

/**
 * Created by a-rmz on 7/23/16.
 */
public class DBOperations {

    DBHandler dbHandler;

    public DBOperations(Context context) {
        dbHandler = new DBHandler(context);
    }

    public long addContact(String name, String date, String phone, String photo) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues newContact = new ContentValues();

        newContact.put(ContactContract.ContactEntry.KEY_NAME, name);
        newContact.put(ContactContract.ContactEntry.KEY_DAY, DateUtils.getDay(date));
        newContact.put(ContactContract.ContactEntry.KEY_MONTH, DateUtils.getMonth(date));
        newContact.put(ContactContract.ContactEntry.KEY_YEAR, DateUtils.getYear(date));
        newContact.put(ContactContract.ContactEntry.KEY_PHONE, phone);
        newContact.put(ContactContract.ContactEntry.KEY_PHOTO, photo);

        long newRowID = db.insert(
                ContactContract.ContactEntry.TABLE_CONTACT,
                null,
                newContact);

        return newRowID;

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

        int count = db.update(
                ContactContract.ContactEntry.TABLE_CONTACT,
                newContact,
                SELECTION,
                SELECTION_ARGS);

        return count;

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

        contact.setID(ID);
        contact.setName(contactCursor.getString(0));
        contact.setDate(contactCursor.getInt(1), contactCursor.getInt(2), contactCursor.getInt(3));
        contact.setPhone(contactCursor.getString(4));
        contact.setPhoto(Uri.parse(contactCursor.getString(5)));

        return contact;
    }

}
