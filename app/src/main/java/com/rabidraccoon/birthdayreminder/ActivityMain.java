package com.rabidraccoon.birthdayreminder;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.rabidraccoon.birthdayreminder.adapters.ContactCursorAdapter;
import com.rabidraccoon.birthdayreminder.db.DBOperations;
import com.rabidraccoon.birthdayreminder.utils.NotifService;
import com.rabidraccoon.birthdayreminder.utils.PhoneUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityMain extends Activity {

    Toolbar toolbar;
    FragmentContactList contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            finish();
            Intent permissions = new Intent(ActivityMain.this, ActivityPermissions.class);
            startActivity(permissions);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        }
        setContentView(R.layout.activity_main);
        contactList = (FragmentContactList) getFragmentManager().findFragmentByTag("list");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.inflateMenu(R.menu.activity_main);
        setActionBar(toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new ContactLoader().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                if(contactList.sort()) {
                    item.setTitle(getString(R.string.sort_by_name));
                } else {
                    item.setTitle(getString(R.string.sort_by_date));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private class ContactLoader extends AsyncTask<Void, Void, Void> {

        // The column index for the _ID column
        public static final int CONTACT_ID_INDEX = 1;
        // The column index for the LOOKUP_KEY column
        public static final int LOOKUP_KEY_INDEX = 2;
        // The column index for the DISPLAY_NAME_PRIMARY_INDEX column
        public static final int DISPLAY_NAME_PRIMARY_INDEX = 3;
        // The column index for the DISPLAY_NAME_PRIMARY_INDEX column
        public static final int PHOTO_URI_INDEX = 4;
        // The column index for the DISPLAY_NAME_PRIMARY_INDEX column
        public static final int START_DATE_INDEX = 5;
        // The column index for the PHONE_INDEX column
        public static final int PHONE_INDEX = 6;


        @Override
        protected Void doInBackground(Void... voids) {

            // Query device's db for contacts
            ContentResolver contentResolver = getContentResolver();

            String PROJECTION_DEV[] = {
                    ContactsContract.Data._ID,                  // 0
                    ContactsContract.Data.CONTACT_ID,           // 1
                    ContactsContract.Data.LOOKUP_KEY,           // 2
                    ContactsContract.Data.DISPLAY_NAME_PRIMARY, // 3
                    ContactsContract.Data.PHOTO_URI,            // 4
                    ContactsContract.CommonDataKinds.Event.START_DATE,                           // 5
                    ContactsContract.CommonDataKinds.Phone.NUMBER                                // 6
            };

            String SELECTION_DEV =
                    ContactsContract.CommonDataKinds.Event.TYPE + " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                            " AND " +
                            ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "'" +
                            " AND " +
                            ContactsContract.Data.CONTACT_ID + " = " + ContactsContract.CommonDataKinds.Event.CONTACT_ID +
                            " AND " +
                            ContactsContract.Data.CONTACT_ID + " = " + ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                            " AND " +
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + ContactsContract.CommonDataKinds.Event.CONTACT_ID;

            Cursor deviceContacts = contentResolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    PROJECTION_DEV,
                    SELECTION_DEV,
                    null,
                    null
            );

            // Query local db for already stored contacts' IDs
            DBOperations localdb = new DBOperations(getApplicationContext());
            // For debug purposes only
//            localdb.resetDB();

            List<Integer> localContacts = localdb.getContactIDs();

            // Compare existing vs local and add them to pending list
            List<Integer> pending = new ArrayList<>();
            while(deviceContacts != null && deviceContacts.moveToNext()) {

                System.out.println("ID: " + deviceContacts.getInt(CONTACT_ID_INDEX) +
                        " - " + deviceContacts.getString(DISPLAY_NAME_PRIMARY_INDEX) +
                        " || Exists: " + localContacts.contains(deviceContacts.getInt(CONTACT_ID_INDEX)) +
                        " || Date: " + deviceContacts.getString(START_DATE_INDEX));

                if(!localContacts.contains(deviceContacts.getInt(CONTACT_ID_INDEX))) {
                    pending.add(deviceContacts.getPosition());
                }
            }

            // Query pending contacts and add them to local db as long as there are pending
            if(pending.size() >= 1) {
                String [] PROJECTION_PHONE = {
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                String WHERE_PHONE =
                        ContactsContract.CommonDataKinds.Phone.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'"
                                + " AND " +
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
                Cursor phoneCursor;


                for (int i = 0; deviceContacts!=null &&  deviceContacts.moveToFirst() && i < pending.size(); i++) {

                    deviceContacts.move(pending.get(i));
                    String [] SELECTION_PHONE = { String.valueOf(deviceContacts.getInt(1)) };

                    phoneCursor = contentResolver.query(
                            ContactsContract.Data.CONTENT_URI,
                            PROJECTION_PHONE,
                            WHERE_PHONE,
                            SELECTION_PHONE,
                            null
                    );

                    localdb.addContact(
                            deviceContacts.getInt(CONTACT_ID_INDEX),
                            deviceContacts.getString(DISPLAY_NAME_PRIMARY_INDEX),
                            deviceContacts.getString(START_DATE_INDEX),
                            (phoneCursor != null && phoneCursor.moveToFirst() && phoneCursor.getCount() > 0) ? PhoneUtils.formatPhone(phoneCursor.getString(1)) : null,
                            (deviceContacts.getString(PHOTO_URI_INDEX) != null) ? deviceContacts.getString(PHOTO_URI_INDEX) : ""
                    );
                    phoneCursor.close();
                }

                deviceContacts.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DBOperations localdb = new DBOperations(getApplicationContext());
            contactList.mContactAdapter = new ContactCursorAdapter(
                    getApplicationContext(),
                    localdb.getContactsCursor(DBOperations.SORT_BY_DATE),
                    false
            );
            contactList.setListAdapter(contactList.mContactAdapter);
        }
    }

}
