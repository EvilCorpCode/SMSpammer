package com.evilcorpcode.smspammer.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evilcorpcode.smspammer.R;
import com.evilcorpcode.smspammer.models.Contact;

public class ContactDetailsWidget extends LinearLayout {
    private TextView mName;
    private TextView mPhoneNumber;
    private View mChooseContactBtn;
    private Contact mContact = new Contact();

    public ContactDetailsWidget(Context context) {
        this(context, null);
    }

    public ContactDetailsWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContactDetailsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.contact_details_view, this);
        bindViews(view);
    }

    private void bindViews(View view) {
        mChooseContactBtn = view.findViewById(R.id.choose_contact_btn);
        mName = (TextView) view.findViewById(R.id.contact_name_value);
        mPhoneNumber = (TextView) view.findViewById(R.id.contact_phone_number_value);
    }

    public void setOnIconListener(OnClickListener onClickListener) {
        mChooseContactBtn.setOnClickListener(onClickListener);
    }

    public void setContact(Contact mContact) {
        this.mContact = mContact;
        mName.setText(mContact.getName());
        mPhoneNumber.setText(mContact.getPhoneNumber());
    }

    public Contact getContact() {
        return mContact;
    }

    public enum State {
        CONTACT_NOT_SET, CONTACT_SET
    }

    public State getState() {
        return mContact.isEmpty() ? State.CONTACT_NOT_SET : State.CONTACT_SET;
    }
}
