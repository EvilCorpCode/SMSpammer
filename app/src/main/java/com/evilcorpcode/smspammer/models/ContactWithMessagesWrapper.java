package com.evilcorpcode.smspammer.models;


public class ContactWithMessagesWrapper {
    public static final String TAG = ContactWithMessagesWrapper.class.getName();
    private Contact mContact;
    private MessagesWrapper mMessagesWrapper;

    public ContactWithMessagesWrapper(Contact contact, MessagesWrapper messagesWrapper) {
        this.mMessagesWrapper = messagesWrapper;
        this.mContact = contact;
    }

    public Contact getContact() {
        return mContact;
    }

    public String getRandomMsg() {
        return mMessagesWrapper.getRandomMsg();
    }

    public MessagesWrapper getMessages() {
        return mMessagesWrapper;
    }
}
