package com.rabidraccoon.birthdayreminder;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.provider.ContactsContract.CommonDataKinds.*;
import android.widget.ListView;

import com.rabidraccoon.birthdayreminder.adapters.ContactListAdapter;
import com.rabidraccoon.birthdayreminder.utils.Contact;
import com.rabidraccoon.birthdayreminder.utils.PhoneUtils;

import java.util.ArrayList;

/**
 * Created by alex on 5/20/16.
 */
public class FragmentContactList extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // CursorAdapter
    private static final String PROJECTION[] = {
            ContactsContract.Data._ID,                  // 0
            ContactsContract.Data.CONTACT_ID,           // 1
            ContactsContract.Data.LOOKUP_KEY,           // 2
            ContactsContract.Data.DISPLAY_NAME_PRIMARY, // 3
            ContactsContract.Data.PHOTO_URI,            // 4
            Event.START_DATE,                           // 5
            Phone.NUMBER                                // 6
    };
    private static final String SELECTION =
            Event.TYPE + " = " + Event.TYPE_BIRTHDAY +
            " AND " +
            Event.MIMETYPE + " = '" + Event.CONTENT_ITEM_TYPE + "'" +
            " AND " +
            ContactsContract.Data.CONTACT_ID + " = " + Event.CONTACT_ID +
            " AND " +
            ContactsContract.Data.CONTACT_ID + " = " + Phone.CONTACT_ID +
            " AND " +
            Phone.CONTACT_ID + " = " + Event.CONTACT_ID
            ;

    private String mSearchString = null;
    private String mSelectionArgs[] = { mSearchString };

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
    ContactListAdapter mContactAdapter;
    Cursor cursor;
    ArrayList<Contact> contacts;

    public FragmentContactList() {
        contacts = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);

        mContactAdapter = new ContactListAdapter(
                this.getActivity(),
                contacts
        );

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

        // Sets the adapter for the ListView
        setListAdapter(mContactAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor
//        mContactAdapter.changeCursor(null);
    }
}
