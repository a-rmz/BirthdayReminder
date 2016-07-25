package com.rabidraccoon.birthdayreminder;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.provider.ContactsContract.CommonDataKinds.*;
import android.widget.ListView;

import com.rabidraccoon.birthdayreminder.adapters.ContactCursorAdapter;
import com.rabidraccoon.birthdayreminder.adapters.ContactListAdapter;
import com.rabidraccoon.birthdayreminder.db.DBOperations;
import com.rabidraccoon.birthdayreminder.utils.Contact;
import com.rabidraccoon.birthdayreminder.utils.DateUtils;
import com.rabidraccoon.birthdayreminder.utils.NotifService;
import com.rabidraccoon.birthdayreminder.utils.PhoneUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 5/20/16.
 */
public class FragmentContactList extends ListFragment  {

    // CursorAdapter

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

    // Contact
//    ContactListAdapter mContactAdapter;
    ContactCursorAdapter mContactAdapter;
    ArrayList<Contact> contacts;
    boolean sortedByDate;


    public FragmentContactList() {
        contacts = new ArrayList<>();
        sortedByDate = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onStart() {
        super.onStart();
        new ContactLoader().execute();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Contact contact = (Contact) getListAdapter().getItem(position);
        FragmentContactDetail detail = (FragmentContactDetail) getFragmentManager().findFragmentByTag("detail");
        if (detail != null && detail.isInLayout()) {
            detail.setContact(contact);
        }
        else {
            Intent intent = new Intent(
                    getActivity().getApplicationContext(),
                    ActivityDetail.class);
            intent.putExtra("contactInfo", contact);
            startActivity(intent);
        }
    }

    public boolean sort() {
        DBOperations localdb = new DBOperations(getActivity());
        if(sortedByDate) {
            // Sort by name
            mContactAdapter.changeCursor(
                    localdb.getContactsCursor(DBOperations.SORT_BY_NAME)
            );
        } else {
                // Sort by date
                mContactAdapter.changeCursor(
                        localdb.getContactsCursor(DBOperations.SORT_BY_DATE)
            );
        }
            sortedByDate = !sortedByDate;
            return sortedByDate;
    }


    private class ContactLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            // Query device's db for contacts
            ContentResolver contentResolver = getActivity().getContentResolver();

            String PROJECTION_DEV[] = {
                    ContactsContract.Data._ID,                  // 0
                    ContactsContract.Data.CONTACT_ID,           // 1
                    ContactsContract.Data.LOOKUP_KEY,           // 2
                    ContactsContract.Data.DISPLAY_NAME_PRIMARY, // 3
                    ContactsContract.Data.PHOTO_URI,            // 4
                    Event.START_DATE,                           // 5
                    Phone.NUMBER                                // 6
            };

            String SELECTION_DEV =
                    Event.TYPE + " = " + Event.TYPE_BIRTHDAY +
                    " AND " +
                    Event.MIMETYPE + " = '" + Event.CONTENT_ITEM_TYPE + "'" +
                    " AND " +
                    ContactsContract.Data.CONTACT_ID + " = " + Event.CONTACT_ID +
                    " AND " +
                    ContactsContract.Data.CONTACT_ID + " = " + Phone.CONTACT_ID +
                    " AND " +
                    Phone.CONTACT_ID + " = " + Event.CONTACT_ID;

            Cursor deviceContacts = contentResolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    PROJECTION_DEV,
                    SELECTION_DEV,
                    null,
                    null
            );

            // Query local db for already stored contacts' IDs
            DBOperations localdb = new DBOperations(getActivity());
//            localdb.resetDB();

            List<Integer> localContacts = localdb.getContactIDs();

            // Compare existing vs local and add them to pending list
            List<Integer> pending = new ArrayList<>();
            while(deviceContacts != null && deviceContacts.moveToNext()) {

                System.out.println("ID: " + deviceContacts.getInt(CONTACT_ID_INDEX) +
                        " - " + deviceContacts.getString(DISPLAY_NAME_PRIMARY_INDEX) +
                        " || Exists: " + localContacts.contains(deviceContacts.getInt(CONTACT_ID_INDEX)));

                if(!localContacts.contains(deviceContacts.getInt(CONTACT_ID_INDEX))) {
                    pending.add(deviceContacts.getPosition());
                }
            }

            // Query pending contacts and add them to local db as long as there are pending
            if(pending.size() >= 1) {
                String [] PROJECTION_PHONE = {
                        Phone.CONTACT_ID,
                        Phone.NUMBER
                };
                String WHERE_PHONE =
                        Phone.MIMETYPE + " = '" + Phone.CONTENT_ITEM_TYPE + "'"
                                + " AND " +
                                Phone.CONTACT_ID + " = ?";
                Cursor phoneCursor;


                for (int i = 0; deviceContacts!=null &&  deviceContacts.moveToFirst() && i < pending.size(); i++) {

                    deviceContacts.move(pending.get(i));
                    System.out.println("ID selected: " + deviceContacts.getInt(0));
                    String [] SELECTION_PHONE = { String.valueOf(deviceContacts.getInt(0)) };

                    phoneCursor = contentResolver.query(
                            ContactsContract.Data.CONTENT_URI,
                            PROJECTION_PHONE,
                            WHERE_PHONE,
                            SELECTION_PHONE,
                            null
                    );

                    if (phoneCursor.getCount() > 0) {
                        phoneCursor.moveToNext();
                        System.out.println("Columns in phoneCursor: " + phoneCursor.getString(1));
                    }

                    localdb.addContact(
                            deviceContacts.getInt(FragmentContactList.CONTACT_ID_INDEX),
                            deviceContacts.getString(FragmentContactList.DISPLAY_NAME_PRIMARY_INDEX),
                            deviceContacts.getString(FragmentContactList.START_DATE_INDEX),
                            (phoneCursor.getCount() > 0) ? PhoneUtils.formatPhone(phoneCursor.getString(1)) : null,
                            deviceContacts.getString(FragmentContactList.PHOTO_URI_INDEX)
                    );
                    phoneCursor.close();
                }

                deviceContacts.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DBOperations localdb = new DBOperations(getActivity());
            mContactAdapter = new ContactCursorAdapter(
                    getActivity(),
                    localdb.getContactsCursor(DBOperations.SORT_BY_DATE),
                    false
                );
            setListAdapter(mContactAdapter);
        }
    }

    /*

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                SELECTION,
                null,//mSelectionArgs,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Put the result Cursor in the adapter for the ListView

        ContentResolver cr = getActivity().getContentResolver(); //getContentResolver()

        String [] PROJECTION_PHONE = {
                Phone.CONTACT_ID,
                Phone.NUMBER
        };
        String WHERE =
                Phone.MIMETYPE + " = '" + Phone.CONTENT_ITEM_TYPE + "'"
                + " AND " +
                Phone.CONTACT_ID + " = ?";
        Cursor phoneCursor;

        while (data.moveToNext()) {
            Contact contact = new Contact();

            contact.setID(data.getInt(FragmentContactList.CONTACT_ID_INDEX));
            contact.setDate(data.getString(FragmentContactList.START_DATE_INDEX));
            contact.setName(data.getString(FragmentContactList.DISPLAY_NAME_PRIMARY_INDEX));
            if(data.getString(FragmentContactList.PHOTO_URI_INDEX) != null)
                contact.setPhoto(Uri.parse(data.getString(FragmentContactList.PHOTO_URI_INDEX)));



            String [] SELECTION = { String.valueOf(contact.getID()) };

            phoneCursor = cr.query(
                    ContactsContract.Data.CONTENT_URI,
                    PROJECTION_PHONE,
                    WHERE,
                    SELECTION,
                    null
            );

            if (phoneCursor.getCount() > 0) {
                phoneCursor.moveToNext();
                contact.setPhone(PhoneUtils.formatPhone(phoneCursor.getString(1)));
            }
            phoneCursor.close();
            contacts.add(contact);
        }
        sortByDate();
        // Sets the adapter for the ListView
        setListAdapter(mContactAdapter);

        // Notifications
        Intent myIntent = new Intent(getActivity() , NotifService.class);
        myIntent.putExtra("contacts", contacts);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Activity.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, myIntent, 0);
        // Calendar instance to define alarm hour
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 9);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        // Repeating alarm for each day
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
        // Start the Service
        getActivity().startService(myIntent);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor
//        mContactAdapter.changeCursor(null);
    }

    public boolean sort() {
        System.out.println("Sorting... byDate: " + sortedByDate);
        if(sortedByDate) {
            sortByName();
            System.out.println("By name");
        } else {
            sortByDate();
            System.out.println("By date");
        }
        sortedByDate = !sortedByDate;
        System.out.println("Sorted byDate: " + sortedByDate);
        return sortedByDate;
    }

    public void sortByName() {
        // Sort the ArrayList
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        mContactAdapter.notifyDataSetChanged();
    }

    public void sortByDate() {
        // Sort the ArrayList
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                int monthComparison = DateUtils.isSameMonth(
                        DateUtils.getMonth(lhs.getDate()),
                        DateUtils.getMonth(rhs.getDate())
                );
                if(monthComparison != 0) return monthComparison;
                else
                    return ( DateUtils.getDay(lhs.getDate()) < DateUtils.getDay(rhs.getDate()) ) ? -1 :
                            ( DateUtils.getDay(lhs.getDate()) == DateUtils.getDay(rhs.getDate()) ) ? 0 : 1;
            }
        });
        mContactAdapter.notifyDataSetChanged();
    }
*/
    public ArrayList<Contact> getContacts() {
        return this.contacts;
    }
}
