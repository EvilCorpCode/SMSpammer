package com.evilcorpcode.smspammer.ui.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.evilcorpcode.smspammer.AppUtils;
import com.evilcorpcode.smspammer.R;
import com.evilcorpcode.smspammer.models.MessagesWrapper;
import com.evilcorpcode.smspammer.ui.snackbar.AppSnackbarImpl;

import java.util.ArrayList;

public abstract class UserInputMessagesBaseWidget extends LinearLayout {
    public static final String TAG = UserInputMessagesBaseWidget.class.getName();
    private View mAddMsgInputViewGroupIcon;
    private LinearLayout mMsgInputViewGroup;
    //keep track of the input messages views. we then read the content of the edittext to send the SMS
    private ArrayList<ViewGroup> mMsgInputViewGroupList = new ArrayList<>();

    private UserInputMessagesWidgetCallback mUserInputMessagesWidgetCallback;


    public UserInputMessagesBaseWidget(Context context) {
        this(context, null);
    }

    public UserInputMessagesBaseWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserInputMessagesBaseWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected abstract
    @LayoutRes
    int getContentView();

    private void init() {
        View view = inflate(getContext(), getContentView(), this);
        bindViews(view);
        setListeners();
    }

    protected void bindViews(View view) {
        mAddMsgInputViewGroupIcon = findViewById(R.id.add_msg_icon);
        mMsgInputViewGroup = (LinearLayout) findViewById(R.id.msg_input_view_group);
    }

    protected void setListeners() {
        mAddMsgInputViewGroupIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addMsgInputView();
            }
        });
    }

    public void setCallback(UserInputMessagesWidgetCallback userInputMessagesWidgetCallback) {
        this.mUserInputMessagesWidgetCallback = userInputMessagesWidgetCallback;
    }

    public void addMsgInputView() {
        final ViewGroup msgInputViewGroup = createMsgInputViewGroup();

        mMsgInputViewGroup.addView(msgInputViewGroup);
        mMsgInputViewGroupList.add(msgInputViewGroup);
    }

    public void addMsgInputView(@NonNull String predefinedText) {
        final ViewGroup msgInputViewGroup = createMsgInputViewGroup();
        TextInputLayout msgInput = (TextInputLayout) msgInputViewGroup.findViewById(R.id.msg_input);

        mMsgInputViewGroup.addView(msgInputViewGroup);
        mMsgInputViewGroupList.add(msgInputViewGroup);

        msgInput.getEditText().setText(predefinedText);
    }

    public void addMsgsInputsViews(@NonNull MessagesWrapper messagesWrapper) {
        for (String message : messagesWrapper.getMsgs()) {
            addMsgInputView(message);
        }
    }

    private ViewGroup createMsgInputViewGroup() {
        final ViewGroup msgInputViewGroup = (ViewGroup) inflate(getContext(), R.layout.user_input_messages_inflate, null);
        View msgInputRemoveIcon = msgInputViewGroup.findViewById(R.id.msg_input_remove_icon);

        msgInputRemoveIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mMsgInputViewGroup.removeView(msgInputViewGroup);

                if (mMsgInputViewGroupList.contains(msgInputViewGroup)) {
                    mMsgInputViewGroupList.remove(msgInputViewGroup);
                }
            }
        });

        return msgInputViewGroup;
    }

    public void giveFocusToFirstEmptyMsgInput() {
        for (int i = 0; i < mMsgInputViewGroupList.size(); i++) {
            ViewGroup msgInputContainer = mMsgInputViewGroupList.get(i);
            TextInputLayout textInputLayout = (TextInputLayout) msgInputContainer.getChildAt(0);
            TextInputEditText textInputEditText = (TextInputEditText) textInputLayout.getEditText();

            if (textInputEditText != null && textInputEditText.getText().toString().isEmpty()) {
                textInputEditText.requestFocus();
                AppUtils.showKeyboard(textInputLayout.getContext(), textInputEditText);
                return;
            }
        }
    }

    public MessagesWrapper getMsgsFromUserInput() {
        MessagesWrapper messagesWrapper = new MessagesWrapper();

        for (ViewGroup msgInputContainer : mMsgInputViewGroupList) {
            TextInputLayout textInputLayout = (TextInputLayout) msgInputContainer.getChildAt(0);
            String msg = textInputLayout.getEditText().getText().toString();
            messagesWrapper.addMsg(msg);
        }
        return messagesWrapper;
    }

    public enum State {
        INPUT_VIEWS_DO_NOT_EXIST, INPUT_VIEWS_EXIST_BUT_NO_TEXT, READY
    }

    public void checkState() {
        showStateUI();

        switch (getState()) {
            case READY:
                mUserInputMessagesWidgetCallback.onStateReady();
                break;
        }
    }

    private boolean hasInputViews() {
        return !mMsgInputViewGroupList.isEmpty();
    }

    private void showStateUI() {
        switch (getState()) {
            case INPUT_VIEWS_DO_NOT_EXIST:
                new AppSnackbarImpl(
                        this,
                        R.string.no_msgs_to_send,
                        R.string.add,
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addMsgInputView();
                            }
                        }
                ).show();


                break;
            case INPUT_VIEWS_EXIST_BUT_NO_TEXT:

                new AppSnackbarImpl(
                        this, R.string.no_content_in_the_msg,
                        R.string.insert,
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                giveFocusToFirstEmptyMsgInput();
                            }
                        }
                ).show();

                break;
        }
    }

    private State getState() {
        State state;

        if (hasInputViews()) {
            if (getMsgsFromUserInput().hasMsgs()) {
                state = State.READY;
            } else {
                state = State.INPUT_VIEWS_EXIST_BUT_NO_TEXT;
            }
        } else {
            state = State.INPUT_VIEWS_DO_NOT_EXIST;
        }

        return state;
    }

    public void clearInputsMsgs() {
        if (mMsgInputViewGroupList.size() > 0) {
            mMsgInputViewGroupList.clear();
            mMsgInputViewGroup.removeAllViews();
        }
    }

    public interface UserInputMessagesWidgetCallback {
        void onStateReady();
    }
}
