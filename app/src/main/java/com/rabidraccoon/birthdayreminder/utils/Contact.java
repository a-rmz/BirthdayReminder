package com.rabidraccoon.birthdayreminder.utils;

import android.net.Uri;

/**
 * Created by alex on 5/29/16.
 */
public class Contact {

    private int ID;
    private String name;
    private String date;
    private String phone;
    private Uri photo;

    public Contact() {

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

    public void setDate(String date) {
        this.date = date;
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

}
