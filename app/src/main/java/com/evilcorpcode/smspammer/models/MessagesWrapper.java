package com.evilcorpcode.smspammer.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class MessagesWrapper implements Serializable {
    public static final String TAG = MessagesWrapper.class.getName();
    private ArrayList<String> mMessages = new ArrayList<>();
    private Random mRandom = new Random();

    public MessagesWrapper() {
    }

    public MessagesWrapper(ArrayList<String> msgs) {
        mMessages = msgs;
    }

    public MessagesWrapper(String msg) {
        mMessages.add(msg);
    }

    public void addMsg(String msgs) {
        mMessages.add(msgs);
    }

    public ArrayList<String> getMsgs() {
        return mMessages;
    }

    public String getRandomMsg() {
        int randomIndex = mRandom.nextInt(mMessages.size());
        return mMessages.get(randomIndex);
    }

    public boolean hasMsgs() {
        boolean valid = !mMessages.isEmpty() && !mMessages.get(0).isEmpty();

        for (String message : mMessages) {
            if (message.isEmpty()) {
                valid = false;
            }
        }

        return valid;
    }

    public int getSize() {
        return mMessages.size();
    }
}
