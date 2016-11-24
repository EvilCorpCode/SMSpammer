package com.evilcorpcode.smspammer.ui.activities;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.evilcorpcode.smspammer.R;
import com.evilcorpcode.smspammer.SendReceiveSmsController;
import com.evilcorpcode.smspammer.dao.ContactWithMessagesWrapperDAO;
import com.evilcorpcode.smspammer.models.Contact;
import com.evilcorpcode.smspammer.models.ContactWithMessagesWrapper;
import com.evilcorpcode.smspammer.models.MessagesWrapper;
import com.evilcorpcode.smspammer.ui.snackbar.AppSnackbarImpl;
import com.evilcorpcode.smspammer.ui.widgets.ContactDetailsWidget;
import com.evilcorpcode.smspammer.ui.widgets.UserInputMessagesWithNumberOfMsgsToSendWidget;

public class MainActivity extends NavDrawerBaseActivity
        implements SendReceiveSmsController.SendReceiveSmsControllerListener, UserInputMessagesWithNumberOfMsgsToSendWidget.UserInputMessagesWidgetCallback {
    private static final int REQUEST_CONTACT_INTENT = 1;

    private FloatingActionButton mFab;
    private ContactDetailsWidget mContactDetailsWidget;
    private UserInputMessagesWithNumberOfMsgsToSendWidget mUserInputMsgsWidget;

    private SendReceiveSmsController mSendReceiveSmsController;
    private ContactWithMessagesWrapperDAO mContactWithMessagesWrapperDAO;
    private ContactWithMessagesWrapper mContactWithMessagesWrapper;

    @Override
    public int getContentView() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactWithMessagesWrapperDAO = new ContactWithMessagesWrapperDAO(this);
        mSendReceiveSmsController = new SendReceiveSmsController(this);
        mUserInputMsgsWidget.setCallback(this);
        loadDataFromDAO();
    }

    @Override
    protected void initDrawer(Toolbar toolbar) {
        super.initDrawer(toolbar);
        if (mNavigationView != null) mNavigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mSendReceiveSmsController.receiver, new IntentFilter(SendReceiveSmsController.SMS_SENT));
        if (mNavigationView != null) mNavigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mSendReceiveSmsController.receiver);
    }

    private void loadDataFromDAO() {
        ContactWithMessagesWrapper contactWithMessagesWrapper = mContactWithMessagesWrapperDAO.getContactWithMessages();
        updateViews(contactWithMessagesWrapper);
    }

    private void updateViews(@Nullable ContactWithMessagesWrapper contactWithMessagesWrapper) {
        if (contactWithMessagesWrapper != null) {
            updateContactDetailsViews(contactWithMessagesWrapper.getContact());
            updateMessagesViewsWithNewData(contactWithMessagesWrapper.getMessages());
        }
    }

    private void updateContactDetailsViews(@Nullable Contact contact) {
        if (contact != null) {
            mContactDetailsWidget.setContact(contact);
        }
    }

    private void updateMessagesViewsWithNewData(@Nullable MessagesWrapper messagesWrapper) {
        if (messagesWrapper != null) {
            mUserInputMsgsWidget.clearInputsMsgs();
            mUserInputMsgsWidget.addMsgsInputsViews(messagesWrapper);
        }
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mContactDetailsWidget = (ContactDetailsWidget) findViewById(R.id.contact_details_widget);
        mUserInputMsgsWidget = (UserInputMessagesWithNumberOfMsgsToSendWidget) findViewById(R.id.user_input_messages_widget);
    }

    private void goToContactSelection() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CONTACT_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CONTACT_INTENT:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    Cursor c = getContentResolver().query(uri, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        c.close();
                        mContactDetailsWidget.setContact(new Contact(name, phone));
                    }
                } else {
                    Log.i("TAG", "REQUEST_A_CONTACT_INTENT_FAILED");
                }
                break;

            case SavedMessagesListActivity.REQUEST_SAVED_MESSAGES:
                if (resultCode == RESULT_OK) {
                    //TODO maybe this is not needed as the onNewIntent will be called when we come from the saved messages selection
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        MessagesWrapper messagesWrapper = (MessagesWrapper) bundle.getSerializable(SavedMessagesListActivity.TAG);
                        updateMessagesViewsWithNewData(messagesWrapper);
                    }
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            MessagesWrapper messagesWrapper = (MessagesWrapper) bundle.getSerializable(SavedMessagesListActivity.TAG);
            updateMessagesViewsWithNewData(messagesWrapper);
        }
    }

    @Override
    protected void setListeners() {
        mContactDetailsWidget.setOnIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToContactSelection();
            }
        });

        mFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mContactDetailsWidget.getState() == ContactDetailsWidget.State.CONTACT_NOT_SET) {

                            new AppSnackbarImpl(
                                    view, R.string.no_contact_selected,
                                    R.string.choose,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            goToContactSelection();
                                        }
                                    }
                            ).show();

                        } else {
                            mUserInputMsgsWidget.checkState();
                        }
                    }
                }
        );
    }

    private void showSendConfirmationDialog(final ContactWithMessagesWrapper contactWithMessagesWrapper) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
        builder.setTitle(getString(R.string.confirmation_before_sending_sms_title));
        builder.setMessage(getString(R.string.confirmation_before_sending_sms));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mContactWithMessagesWrapperDAO.saveContactWithMessages(mContactWithMessagesWrapper);
                mSendReceiveSmsController.setContactWithMessagesWrapper(contactWithMessagesWrapper, mUserInputMsgsWidget.getNumberOfMsgsToSend());
                mSendReceiveSmsController.initiateSmsSending(getBaseContext());
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);

        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_contact) {
            mContactWithMessagesWrapper = new ContactWithMessagesWrapper(
                    mContactDetailsWidget.getContact(),
                    mUserInputMsgsWidget.getMsgsFromUserInput()
            );

            mContactWithMessagesWrapperDAO.saveContactWithMessages(mContactWithMessagesWrapper);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onControllerBroadcastReceive(BroadcastReceiver broadcastReceiver) {
        switch (broadcastReceiver.getResultCode()) {
            case RESULT_OK:
                mSendReceiveSmsController.initiateSmsSending(getBaseContext());
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                break;
        }
    }

    @Override
    public void onStateReady() {
        mContactWithMessagesWrapper = new ContactWithMessagesWrapper(
                mContactDetailsWidget.getContact(),
                mUserInputMsgsWidget.getMsgsFromUserInput());

        showSendConfirmationDialog(mContactWithMessagesWrapper);
    }
}