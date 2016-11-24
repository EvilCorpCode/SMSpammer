package com.evilcorpcode.smspammer.ui.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;

import com.evilcorpcode.smspammer.R;

public class UserInputMessagesWithNumberOfMsgsToSendWidget extends UserInputMessagesBaseWidget {
    public static final String TAG = UserInputMessagesWithNumberOfMsgsToSendWidget.class.getName();

    private AppCompatSpinner mNumberOfMsgsSpinner;

    public UserInputMessagesWithNumberOfMsgsToSendWidget(Context context) {
        this(context, null);
    }

    public UserInputMessagesWithNumberOfMsgsToSendWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserInputMessagesWithNumberOfMsgsToSendWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected
    @LayoutRes
    int getContentView() {
        return R.layout.user_input_messages_with_number_of_msgs_to_send_view;
    }

    @Override
    protected void bindViews(View view) {
        super.bindViews(view);
        mNumberOfMsgsSpinner = (AppCompatSpinner) findViewById(R.id.number_of_msgs_spinner);
    }

    public int getNumberOfMsgsToSend() {
        return Integer.valueOf((String) mNumberOfMsgsSpinner.getSelectedItem());
    }
}
