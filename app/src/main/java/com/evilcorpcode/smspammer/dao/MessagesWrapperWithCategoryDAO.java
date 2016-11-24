package com.evilcorpcode.smspammer.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.evilcorpcode.smspammer.models.MessagesWrapperWithCategory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessagesWrapperWithCategoryDAO {
    public static final String TAG = MessagesWrapperWithCategoryDAO.class.getName();
    private SharedPreferences mPrefs;

    public MessagesWrapperWithCategoryDAO(Context context) {
        mPrefs = context.getSharedPreferences(MessagesWrapperWithCategoryDAO.TAG, Context.MODE_PRIVATE);
    }

    public void addMessagesWrapperWithCategory(MessagesWrapperWithCategory messagesWrapperWithCategory) {
        if (messagesWrapperWithCategory != null) {
            //get previous local msgs if any to append the new data
            List<MessagesWrapperWithCategory> messagesWrapperWithCategoryList = getMessagesWrapperWithCategoryList();
            messagesWrapperWithCategoryList.add(messagesWrapperWithCategory);

            storeLocally(messagesWrapperWithCategoryList);
        }
    }

    public void updateExistingMessagesWrapperWithCategory(MessagesWrapperWithCategory messagesWrapperWithCategoryToUpdate) {
        if (messagesWrapperWithCategoryToUpdate != null) {
            //get all local msgs and then we update the local json excluding the one we want to remove
            List<MessagesWrapperWithCategory> messagesWrapperWithCategoryList = getMessagesWrapperWithCategoryList();
            MessagesWrapperWithCategory messagesWrapperWithCategory;

            for (int i = 0; i < messagesWrapperWithCategoryList.size(); i++) {
                messagesWrapperWithCategory = messagesWrapperWithCategoryList.get(i);
                if (messagesWrapperWithCategory.getId() == messagesWrapperWithCategoryToUpdate.getId()) {
                    messagesWrapperWithCategory.updateData(
                            messagesWrapperWithCategoryToUpdate.getCategory(),
                            messagesWrapperWithCategoryToUpdate.getMessagesWrapper().getMsgs());
                }
            }
            storeLocally(messagesWrapperWithCategoryList);
        }
    }

    private void storeLocally(List<MessagesWrapperWithCategory> messagesWrapperWithCategoryList) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(messagesWrapperWithCategoryList);
        prefsEditor.putString(MessagesWrapperWithCategory.TAG, json);
        prefsEditor.apply();
    }

    public List<MessagesWrapperWithCategory> getMessagesWrapperWithCategoryList() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<MessagesWrapperWithCategory>>() {
        }.getType();

        List<MessagesWrapperWithCategory> defaultValue = new ArrayList<>();

        String json = mPrefs.getString(MessagesWrapperWithCategory.TAG, defaultValue.toString());
        return gson.fromJson(json, type);
    }

    public List<MessagesWrapperWithCategory> deleteCategory(MessagesWrapperWithCategory messagesWrapperWithCategoryToDelete) {
        List<MessagesWrapperWithCategory> messagesWrapperWithCategoryListNew = new ArrayList<>();

        if (messagesWrapperWithCategoryToDelete != null) {
            //get all local msgs and then we update the local json excluding the one we want to remove
            List<MessagesWrapperWithCategory> messagesWrapperWithCategoryList = getMessagesWrapperWithCategoryList();
            MessagesWrapperWithCategory messagesWrapperWithCategory;

            for (int i = 0; i < messagesWrapperWithCategoryList.size(); i++) {
                messagesWrapperWithCategory = messagesWrapperWithCategoryList.get(i);
                // comparing ids to make sure we are removing the correct category
                if (!(messagesWrapperWithCategory.getId() == messagesWrapperWithCategoryToDelete.getId())) {
                    //add to new array
                    messagesWrapperWithCategoryListNew.add(messagesWrapperWithCategory);
                }
            }
            storeLocally(messagesWrapperWithCategoryListNew);
        }
        return messagesWrapperWithCategoryListNew;
    }
}
