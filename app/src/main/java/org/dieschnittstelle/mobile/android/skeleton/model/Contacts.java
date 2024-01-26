package org.dieschnittstelle.mobile.android.skeleton.model;

import android.database.Cursor;
import android.provider.ContactsContract;

public class Contacts {
    private String name;
    private String phoneNumber;
    private String emailaddress;

    public Contacts(String name, String phoneNumber, String emailaddress) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailaddress = emailaddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }
}
