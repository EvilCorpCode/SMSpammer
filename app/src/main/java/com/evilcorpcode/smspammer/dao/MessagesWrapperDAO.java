package com.evilcorpcode.smspammer.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.evilcorpcode.smspammer.models.MessagesWrapper;

public class MessagesWrapperDAO {
    public static final String TAG = MessagesWrapperDAO.class.getName();
    private SharedPreferences mPrefs;

    public MessagesWrapperDAO(Context context) {
        mPrefs = context.getSharedPreferences(MessagesWrapperDAO.TAG, Context.MODE_PRIVATE);
    }

    public void saveMessages(MessagesWrapper messagesWrapper) {
        if (messagesWrapper != null) {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(messagesWrapper);
            prefsEditor.putString(MessagesWrapper.TAG, json);
            prefsEditor.apply();
        }
    }

    public MessagesWrapper getMessages() {
        Gson gson = new Gson();
        String json = mPrefs.getString(MessagesWrapper.TAG, "");
        return gson.fromJson(json, MessagesWrapper.class);
    }
}
