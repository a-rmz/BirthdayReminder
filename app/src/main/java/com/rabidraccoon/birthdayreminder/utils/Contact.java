package com.rabidraccoon.birthdayreminder.utils;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alex on 5/29/16.
 */
public class Contact implements Parcelable {

    private int ID;
    private String name;
    private String date;
    private String dateHuman;
    private String phone;
    private Uri photo;

    public Contact() { }

    public Contact(Parcel in) {
        String info[] = new String[5];
        in.readStringArray(info);
        setID(Integer.getInteger(info[0]));
        setName(info[1]);
        setDate(info[2]);
        setPhone(info[3]);
        setPhoto(Uri.parse(info[4]));
    }



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getDateHuman() {
        return dateHuman;
    }

    public void setDate(String date) {
        this.date = date;
        this.dateHuman = DateUtils.toHuman(date);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }


    // PARCELABLE

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                String.valueOf(getID()),
                getName(),
                getDate(),
                getPhone(),
                String.valueOf(getPhoto())
        });
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {

        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
