package com.rabidraccoon.birthdayreminder.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.rabidraccoon.birthdayreminder.FragmentContactDetail;
import com.rabidraccoon.birthdayreminder.FragmentContactList;
import com.rabidraccoon.birthdayreminder.R;
import com.rabidraccoon.birthdayreminder.utils.Contact;
import com.rabidraccoon.birthdayreminder.utils.DateUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alex on 5/22/16.
 */
public class ContactListAdapter extends BaseAdapter {

    ArrayList<Contact> contacts;
    LayoutInflater inflater;

    public ContactListAdapter(Context context, ArrayList<Contact> contacts) {
        super();
        this.contacts = contacts;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) contacts.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        Contact contact = contacts.get(position);

        if(view == null) {
            view = inflater.inflate(R.layout.contact_adapter_view, parent, false);
        } else {
            if( view.getTag() != null && view.getTag().equals(contact.getID()) ) {
                return view;
            }
        }

        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.contactName.setText(contact.getName());
        viewHolder.contactBirthday.setText(contact.getDateHuman());
        if(contact.getPhoto() != null) viewHolder.contactThumb.setImageURI(contact.getPhoto());
        else viewHolder.contactThumb.setImageDrawable(parent.getResources().getDrawable(R.drawable.profile, null));

        view.setTag(contact.getID());

        return view;
    }

    static class ViewHolder {

        TextView contactName, contactBirthday;
        CircleImageView contactThumb;

        public ViewHolder (View view) {
            contactName = (TextView) view.findViewById(R.id.list_item_contact_name);
            contactBirthday = (TextView) view.findViewById(R.id.list_item_contact_birthday);
            contactThumb = (CircleImageView) view.findViewById(R.id.contact_image_thumb);
        }
    }

}
