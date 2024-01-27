package org.dieschnittstelle.mobile.android.skeleton.model;

import android.database.Cursor;
import android.provider.ContactsContract;

public class Contacts {
    private String name;
    private String phoneNumber;
    private String emailaddress;

    private String contactId;
    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }


    public Contacts(String name, String phoneNumber, String emailaddress, String contactId) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailaddress = emailaddress;
        this.contactId = contactId;
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
