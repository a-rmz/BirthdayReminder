package com.rabidraccoon.birthdayreminder;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rabidraccoon.birthdayreminder.db.DBOperations;
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


    public void setContact(int ID) {
        TextView nameCaption = (TextView) getActivity().findViewById(R.id.contact_detail_name);
        TextView dateCaption = (TextView) getActivity().findViewById(R.id.contact_detail_birthday);
        TextView ageCaption = (TextView) getActivity().findViewById(R.id.contact_detail_age);

        DBOperations localdb = new DBOperations(getActivity());
        Contact contact = localdb.getContact(ID);

        nameCaption.setText(contact.getName());
        dateCaption.setText(contact.getDateHuman());
        ageCaption.setText(
                (contact.getYear() != 0) ?
                        getResources().getString(R.string.age, DateUtils.calculateAge(contact)) :
                        getResources().getString(R.string.no_year_prov)
        );

        CircleImageView contactImage = (CircleImageView) getActivity().findViewById(R.id.contact_detail_image);
        if(!contact.getPhoto().toString().equals("")) {
            contactImage.setImageURI(contact.getPhoto());
        } else {
            contactImage.setImageDrawable(getResources().getDrawable(R.drawable.profile, null));
        }
    }
}
