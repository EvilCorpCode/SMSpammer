package com.evilcorpcode.smspammer.models;

import java.io.Serializable;
import java.util.ArrayList;

public class MessagesWrapperWithCategory implements Serializable {
    public static final String TAG = MessagesWrapperWithCategory.class.getName();
    private long mId;
    private MessagesWrapper mMessagesWrapper;
    private String mCategory;

    public MessagesWrapperWithCategory(long id, String category, ArrayList<String> msgs) {
        mId = id;
        mMessagesWrapper = new MessagesWrapper(msgs);
        mCategory = category;
    }

    public MessagesWrapperWithCategory(long id, String category, String msg) {
        mId = id;
        mMessagesWrapper = new MessagesWrapper(msg);
        mCategory = category;
    }

    public long getId() {
        return mId;
    }

    public String getCategory() {
        return mCategory;
    }

    public MessagesWrapper getMessagesWrapper() {
        return mMessagesWrapper;
    }

    public void updateData(String category, ArrayList<String> msgs) {
        mMessagesWrapper = new MessagesWrapper(msgs);
        mCategory = category;
    }
}
