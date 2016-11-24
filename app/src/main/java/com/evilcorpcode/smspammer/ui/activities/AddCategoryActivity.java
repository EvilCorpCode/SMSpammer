package com.evilcorpcode.smspammer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.evilcorpcode.smspammer.AppUtils;
import com.evilcorpcode.smspammer.R;
import com.evilcorpcode.smspammer.models.MessagesWrapperWithCategory;
import com.evilcorpcode.smspammer.dao.MessagesWrapperWithCategoryDAO;
import com.evilcorpcode.smspammer.ui.widgets.UserInputMessagesWidget;
import com.evilcorpcode.smspammer.ui.widgets.UserInputMessagesWithNumberOfMsgsToSendWidget;
import com.evilcorpcode.smspammer.ui.snackbar.AppSnackbarImpl;

public class AddCategoryActivity extends NavDrawerBaseActivity {
    public static final String TAG = AddCategoryActivity.class.getName();
    private TextInputLayout mAddCategory;
    private FloatingActionButton mFab;
    private UserInputMessagesWidget mUserInputMessagesWidget;
    private MessagesWrapperWithCategoryDAO mMessagesWrapperWithCategoryDAO;

    @Override
    protected int getContentView() {
        return R.layout.add_or_edit_category_activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessagesWrapperWithCategoryDAO = new MessagesWrapperWithCategoryDAO(this);
        mUserInputMessagesWidget.addMsgInputView();
        mUserInputMessagesWidget.addMsgInputView();
    }

    @Override
    protected void initDrawer(Toolbar toolbar) {
        super.initDrawer(toolbar);
        if (mNavigationView != null) mNavigationView.setCheckedItem(R.id.nav_add_category);
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        mAddCategory = (TextInputLayout) findViewById(R.id.add_category_of_messages_input);
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
                        long id = mMessagesWrapperWithCategoryDAO.getMessagesWrapperWithCategoryList().size();

                        MessagesWrapperWithCategory messagesWrapperWithCategory =
                                new MessagesWrapperWithCategory(
                                        id,
                                        getCategoryText(),
                                        mUserInputMessagesWidget.getMsgsFromUserInput().getMsgs());

                        mMessagesWrapperWithCategoryDAO.addMessagesWrapperWithCategory(messagesWrapperWithCategory);

                        Intent intent = new Intent(AddCategoryActivity.this, SavedMessagesListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNavigationView != null) mNavigationView.setCheckedItem(R.id.nav_add_category);
    }

    private void checkState() {
        if (!getCategoryText().isEmpty()) {
            mUserInputMessagesWidget.checkState();
        } else {
            new AppSnackbarImpl(
                    mAddCategory,
                    R.string.category_is_empty,
                    R.string.insert,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            giveFocusToCategoryInput();
                        }

                        private void giveFocusToCategoryInput() {
                            if (mAddCategory.getEditText() != null) {
                                mAddCategory.getEditText().requestFocus();
                                AppUtils.showKeyboard(mAddCategory.getContext(), mAddCategory.getEditText());
                            }
                        }
                    }
            ).show();
        }
    }

    private String getCategoryText() {
        String category = "";
        if (mAddCategory.getEditText() != null) {
            category = mAddCategory.getEditText().getText().toString();
        }
        return category;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}