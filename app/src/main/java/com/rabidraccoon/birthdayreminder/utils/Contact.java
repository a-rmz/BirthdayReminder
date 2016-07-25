package com.rabidraccoon.birthdayreminder.utils;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

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
    // Birthday
    private int day;
    private int month;
    private int year;

    public Contact() {
        photo = null;
    }

    public Contact(Parcel in) {
        String info[] = new String[5];
        in.readStringArray(info);
        setID(Integer.parseInt(info[0]));
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
        this.day = DateUtils.getDay(date);
        this.month = DateUtils.getMonth(date);
        this.year = DateUtils.getYear(date);
    }

    public void setDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.date = day + "-" + month + "-" + ((this.year != 0) ? year : "-");
        this.dateHuman = DateUtils.toHuman(day, month);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = PhoneUtils.formatPhone(phone);
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = (!photo.equals("")) ? photo : null;
    }

    public void setPhoto(String photo) {
        setPhoto((photo != null) ? Uri.parse(photo) : Uri.parse(""));
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
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
                (getPhoto() != null) ? getPhoto().toString() : ""
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
