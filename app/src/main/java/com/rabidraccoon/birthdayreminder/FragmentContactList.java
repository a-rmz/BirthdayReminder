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

    // Contact
    ContactCursorAdapter mContactAdapter;
    boolean sortedByDate;


    public FragmentContactList() {
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
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Cursor cursor = (Cursor) getListAdapter().getItem(position);
        System.out.println("ID: " + cursor.getString(0));
        System.out.println("Name: " + cursor.getString(1));
        System.out.println("Date: " + cursor.getInt(2) + "/" + cursor.getInt(3) + "/" + cursor.getInt(4));
        System.out.println("Phone number: " + cursor.getString(5));
        System.out.println("PhotoURI: " + cursor.getString(6));
        /*Contact contact = new Contact();
        // Inflate contact
        contact.setID(cursor.getInt(0));
        contact.setName(cursor.getString(1));
        contact.setDate(cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
        contact.setPhoto(cursor.getString(5));
        contact.setPhone(cursor.getString(6));

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
        }*/

        FragmentContactDetail detail = (FragmentContactDetail) getFragmentManager().findFragmentByTag("detail");
        if (detail != null && detail.isInLayout()) {
            detail.setContact(cursor.getInt(0));
        }
        else {
            Intent intent = new Intent(
                    getActivity().getApplicationContext(),
                    ActivityDetail.class);
            intent.putExtra("contactID", cursor.getInt(0));
            intent.putExtra("contactName", cursor.getString(1));
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





}
