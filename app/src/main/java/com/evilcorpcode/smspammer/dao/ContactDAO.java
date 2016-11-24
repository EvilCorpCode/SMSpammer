package com.evilcorpcode.smspammer.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.evilcorpcode.smspammer.models.Contact;

public class ContactDAO {
    public static final String TAG = ContactDAO.class.getName();
    private SharedPreferences mPrefs;

    public ContactDAO(Context context) {
        mPrefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public void saveContact(Contact contact) {
        if (contact != null) {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(contact);
            prefsEditor.putString(Contact.TAG, json);
            prefsEditor.apply();
        }
    }

    public Contact getContact() {
        Gson gson = new Gson();
        String json = mPrefs.getString(Contact.TAG, "");
        return gson.fromJson(json, Contact.class);
    }
}
