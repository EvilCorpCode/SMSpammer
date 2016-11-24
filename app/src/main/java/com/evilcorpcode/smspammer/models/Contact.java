package com.evilcorpcode.smspammer.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Contact implements Serializable {
    public static final String TAG = Contact.class.getName();
    private String mName;
    private String mPhoneNumber;

    public Contact(@NonNull String name, @NonNull String phoneNumber) {
        this.mName = name;
        this.mPhoneNumber = phoneNumber;
    }

    public Contact() {
        this.mName = "";
        this.mPhoneNumber = "";
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getName() {
        return mName;
    }

    public boolean isEmpty() {
        return mName.isEmpty() || mPhoneNumber.isEmpty();
    }
}

