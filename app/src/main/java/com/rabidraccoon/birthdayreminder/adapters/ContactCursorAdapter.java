package com.rabidraccoon.birthdayreminder.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.rabidraccoon.birthdayreminder.R;
import com.rabidraccoon.birthdayreminder.utils.DateUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by a-rmz on 7/24/16.
 */
public class ContactCursorAdapter extends CursorAdapter {

    public ContactCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.contact_adapter_view, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView contactName, contactBirthday;
        CircleImageView contactThumb;

        contactName = (TextView) view.findViewById(R.id.list_item_contact_name);
        contactBirthday = (TextView) view.findViewById(R.id.list_item_contact_birthday);
        contactThumb = (CircleImageView) view.findViewById(R.id.contact_image_thumb);

        contactName.setText(cursor.getString(1));
        contactBirthday.setText(DateUtils.toHuman(cursor.getInt(2), cursor.getInt(3)));
        if(!cursor.getString(6).equals("")) {
            contactThumb.setImageURI(Uri.parse(cursor.getString(6)));
        } else {
            contactThumb.setImageDrawable(context.getResources().getDrawable(R.drawable.profile, null));
        }

    }
}
