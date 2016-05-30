package com.rabidraccoon.birthdayreminder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rabidraccoon.birthdayreminder.utils.Contact;
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

    public void setContact(Contact contact) {
        TextView nameCaption = (TextView) getActivity().findViewById(R.id.contact_detail_name);
        TextView dateCaption = (TextView) getActivity().findViewById(R.id.contact_detail_birthday);
        TextView ageCaption = (TextView) getActivity().findViewById(R.id.contact_detail_age);

        nameCaption.setText(contact.getName());
        dateCaption.setText(contact.getDateHuman());
        ageCaption.setText(getResources().getString(R.string.age, DateUtils.calculateAge(contact.getDate())));

        CircleImageView contactImage = (CircleImageView) getActivity().findViewById(R.id.contact_detail_image);
        if(contact.getPhoto() != null) contactImage.setImageURI(contact.getPhoto());
        else contactImage.setImageDrawable(getResources().getDrawable(R.drawable.profile, null));
    }
}
