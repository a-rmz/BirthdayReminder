package com.rabidraccoon.birthdayreminder;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rabidraccoon.birthdayreminder.utils.DateUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alex on 5/22/16.
 */
public class FragmentContactDetail extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_detail, container);
    }

    public void setContact(Cursor contactCursor) {
        TextView nameCaption = (TextView) getActivity().findViewById(R.id.contact_detail_name);
        TextView dateCaption = (TextView) getActivity().findViewById(R.id.contact_detail_birthday);
        TextView ageCaption = (TextView) getActivity().findViewById(R.id.contact_detail_age);
        nameCaption.setText(contactCursor.getString(FragmentContactList.DISPLAY_NAME_PRIMARY_INDEX));
        dateCaption.setText(DateUtils.toHuman(contactCursor.getString(FragmentContactList.START_DATE_INDEX)));
        ageCaption.setText(getResources().getString(R.string.age, DateUtils.calculateAge(contactCursor.getString(FragmentContactList.START_DATE_INDEX))));

        CircleImageView contactImage = (CircleImageView) getActivity().findViewById(R.id.contact_detail_image);
        contactImage.setImageURI(Uri.parse(contactCursor.getString(FragmentContactList.PHOTO_URI_INDEX)));
    }
}
