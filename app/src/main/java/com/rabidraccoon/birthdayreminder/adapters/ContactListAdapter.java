package com.rabidraccoon.birthdayreminder.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.rabidraccoon.birthdayreminder.FragmentContactDetail;
import com.rabidraccoon.birthdayreminder.FragmentContactList;
import com.rabidraccoon.birthdayreminder.R;
import com.rabidraccoon.birthdayreminder.utils.DateUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alex on 5/22/16.
 */
public class ContactListAdapter extends CursorAdapter {

    TextView contactName, contactBirthday;
    CircleImageView contactThumb;

    public ContactListAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contact_adapter_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        contactName = (TextView) view.findViewById(R.id.list_item_contact_name);
        contactBirthday = (TextView) view.findViewById(R.id.list_item_contact_birthday);
        contactThumb = (CircleImageView) view.findViewById(R.id.contact_image_thumb);
        // Assign values from the cursor
        contactName.setText(cursor.getString(FragmentContactList.DISPLAY_NAME_PRIMARY_INDEX));
        contactBirthday.setText(DateUtils.toHuman(cursor.getString(FragmentContactList.START_DATE_INDEX)));

        String uri = cursor.getString(FragmentContactList.PHOTO_URI_INDEX);
        if(uri != null) contactThumb.setImageURI(Uri.parse(uri));
        else contactThumb.setImageDrawable(view.getResources().getDrawable(R.drawable.profile, null));
    }
}
