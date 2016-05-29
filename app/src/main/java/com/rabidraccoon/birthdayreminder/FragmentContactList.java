package com.rabidraccoon.birthdayreminder;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.provider.ContactsContract.CommonDataKinds.*;
import android.widget.ListView;

import com.rabidraccoon.birthdayreminder.adapters.ContactListAdapter;

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
//            " AND " +
//            Phone.MIMETYPE + " = '" + Phone.CONTENT_ITEM_TYPE + "'" +
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
    ContactListAdapter mCursorAdapter;
    Cursor cursor;

    public FragmentContactList() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCursorAdapter = new ContactListAdapter(
                getActivity(),
                cursor
        );
        // Sets the adapter for the ListView
        setListAdapter(mCursorAdapter);
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Cursor contactCursor = (Cursor) getListAdapter().getItem(position);
        System.out.println( contactCursor.getString(FragmentContactList.PHONE_INDEX));
        FragmentContactDetail detail = (FragmentContactDetail) getFragmentManager().findFragmentByTag("detail");
        if (detail != null && detail.isInLayout()) {
            detail.setContact(contactCursor);
        }
      else {
            Intent intent = new Intent(
                    getActivity().getApplicationContext(),
                    ActivityDetail.class);
            intent.putExtra("contactInfo", new String[] {
                contactCursor.getString(FragmentContactList.DISPLAY_NAME_PRIMARY_INDEX),
                contactCursor.getString(FragmentContactList.START_DATE_INDEX),
                contactCursor.getString(FragmentContactList.PHOTO_URI_INDEX),
                contactCursor.getString(FragmentContactList.PHONE_INDEX)}
            );
            startActivity(intent);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        mSelectionArgs[0] = "%" + mSearchString + "%";

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
        mCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor
        mCursorAdapter.changeCursor(null);
    }
}
