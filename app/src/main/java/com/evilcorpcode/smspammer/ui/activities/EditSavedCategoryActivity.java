package com.evilcorpcode.smspammer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.View;

import com.evilcorpcode.smspammer.AppUtils;
import com.evilcorpcode.smspammer.R;
import com.evilcorpcode.smspammer.models.MessagesWrapper;
import com.evilcorpcode.smspammer.models.MessagesWrapperWithCategory;
import com.evilcorpcode.smspammer.dao.MessagesWrapperWithCategoryDAO;
import com.evilcorpcode.smspammer.ui.widgets.UserInputMessagesWidget;
import com.evilcorpcode.smspammer.ui.widgets.UserInputMessagesWithNumberOfMsgsToSendWidget;
import com.evilcorpcode.smspammer.ui.snackbar.AppSnackbarImpl;

public class EditSavedCategoryActivity extends BaseActivity {
    public static final String TAG = EditSavedCategoryActivity.class.getName();
    public static final String EXTRAS_SELECTED_CATEGORY = "extras_selected_category";
    private TextInputLayout mAddNewCategoryOfMessages;
    private FloatingActionButton mFab;
    private UserInputMessagesWidget mUserInputMessagesWidget;
    private MessagesWrapperWithCategoryDAO mMessagesWrapperWithCategoryDAO;
    private MessagesWrapperWithCategory mMessagesWrapperWithCategory;

    @Override
    protected int getContentView() {
        return R.layout.add_or_edit_category_activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessagesWrapperWithCategoryDAO = new MessagesWrapperWithCategoryDAO(this);

        Bundle bundle = getIntent().getExtras();
        mMessagesWrapperWithCategory = (MessagesWrapperWithCategory) bundle.getSerializable(EXTRAS_SELECTED_CATEGORY);

        mAddNewCategoryOfMessages.getEditText().setText(mMessagesWrapperWithCategory.getCategory());
        for (int i = 0; i < mMessagesWrapperWithCategory.getMessagesWrapper().getSize(); i++) {
            MessagesWrapper messagesWrapper = mMessagesWrapperWithCategory.getMessagesWrapper();
            mUserInputMessagesWidget.addMsgInputView(messagesWrapper.getMsgs().get(i));
        }
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        mAddNewCategoryOfMessages = (TextInputLayout) findViewById(R.id.add_category_of_messages_input);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mUserInputMessagesWidget = (UserInputMessagesWidget) findViewById(R.id.user_input_messages__with_category_widget);
    }

    @Override
    protected void setListeners() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkState();
            }
        });

        mUserInputMessagesWidget.setCallback(
                new UserInputMessagesWithNumberOfMsgsToSendWidget.UserInputMessagesWidgetCallback() {
                    @Override
                    public void onStateReady() {
                        long id = mMessagesWrapperWithCategory.getId();

                        MessagesWrapperWithCategory messagesWrapperWithCategory =
                                new MessagesWrapperWithCategory(
                                        id,
                                        getCategoryText(),
                                        mUserInputMessagesWidget.getMsgsFromUserInput().getMsgs());

                        mMessagesWrapperWithCategoryDAO.updateExistingMessagesWrapperWithCategory(messagesWrapperWithCategory);

                        Intent intent = new Intent(EditSavedCategoryActivity.this, SavedMessagesListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    private void checkState() {
        if (!getCategoryText().isEmpty()) {
            mUserInputMessagesWidget.checkState();
        } else {
            new AppSnackbarImpl(
                    mAddNewCategoryOfMessages,
                    R.string.category_is_empty,
                    R.string.insert,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            giveFocusToCategoryInput();
                        }

                        private void giveFocusToCategoryInput() {
                            if (mAddNewCategoryOfMessages.getEditText() != null) {
                                mAddNewCategoryOfMessages.getEditText().requestFocus();
                                AppUtils.showKeyboard(mAddNewCategoryOfMessages.getContext(), mAddNewCategoryOfMessages.getEditText());
                            }
                        }
                    }
            ).show();
        }
    }

    private String getCategoryText() {
        String category = "";
        if (mAddNewCategoryOfMessages.getEditText() != null) {
            category = mAddNewCategoryOfMessages.getEditText().getText().toString();
        }
        return category;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}