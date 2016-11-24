package com.evilcorpcode.smspammer.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.evilcorpcode.smspammer.models.ContactWithMessagesWrapper;

public class ContactWithMessagesWrapperDAO {
    public static final String TAG = ContactWithMessagesWrapperDAO.class.getName();
    private SharedPreferences mPrefs;

    public ContactWithMessagesWrapperDAO(Context context) {
        mPrefs = context.getSharedPreferences(ContactWithMessagesWrapperDAO.TAG, Context.MODE_PRIVATE);
    }

    public void saveContactWithMessages(ContactWithMessagesWrapper contactWithMessagesWrapper) {
        if (contactWithMessagesWrapper != null) {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(contactWithMessagesWrapper);
            prefsEditor.putString(ContactWithMessagesWrapper.TAG, json);
            prefsEditor.apply();
        }
    }

    public ContactWithMessagesWrapper getContactWithMessages() {
        Gson gson = new Gson();
        String json = mPrefs.getString(ContactWithMessagesWrapper.TAG, "");
        return gson.fromJson(json, ContactWithMessagesWrapper.class);
    }
}
