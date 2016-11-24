package com.evilcorpcode.smspammer.ui.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;

import com.evilcorpcode.smspammer.R;

public class UserInputMessagesWidget extends UserInputMessagesBaseWidget {
    public static final String TAG = UserInputMessagesWidget.class.getName();

    public UserInputMessagesWidget(Context context) {
        this(context, null);
    }

    public UserInputMessagesWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserInputMessagesWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected
    @LayoutRes
    int getContentView() {
        return R.layout.user_input_messages_view;
    }
}
