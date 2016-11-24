package com.evilcorpcode.smspammer.ui.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evilcorpcode.smspammer.R;
import com.evilcorpcode.smspammer.models.Contact;

import java.util.ArrayList;

public class ContactsSelectionActivity extends AppCompatActivity {
    public static final String TAG = ContactsSelectionActivity.class.getName();
    public static final String SELECTED_CONTACT_EXTRAS = "selected_contact_extras";
    ArrayList<Contact> mContacts = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_selection_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindViews();
        setListeners();

        mContacts = getAllContacts(this.getContentResolver());
        initRecyclerView();
    }

    private void initRecyclerView() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter adapter = new ContactsSelectionAdapter();
        mRecyclerView.setAdapter(adapter);
    }

    private void bindViews() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.contacts_selection_recycler_view);
    }

    private void setListeners() {
    }

    public ArrayList<Contact> getAllContacts(ContentResolver cr) {
        ArrayList<Contact> contacts = new ArrayList<>();

        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Contact contact = new Contact(name, phoneNumber);
            contacts.add(contact);
        }
        phones.close();

        return contacts;
    }

    public class ContactsSelectionAdapter extends RecyclerView.Adapter<ContactsSelectionViewHolder> {
        // Create new views (invoked by the layout manager)
        @Override
        public ContactsSelectionViewHolder onCreateViewHolder(
                ViewGroup parent,
                int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contacts_selection_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            return new ContactsSelectionViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ContactsSelectionViewHolder holder, final int position) {
            final Contact contact = mContacts.get(position);

            holder.contactName.setText(contact.getName());
            holder.phoneNumber.setText(contact.getPhoneNumber());

            setListeners(holder, position);
        }

        private void setListeners(ContactsSelectionViewHolder holder, final int position) {
            holder.cardViewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onContactSelected(position);
                }
            });
        }

        private void onContactSelected(int position) {
            Contact contact = mContacts.get(position);
            openMainActivityWithResult(contact);
        }

        private void openMainActivityWithResult(Contact contact) {
            Intent returnIntent = getIntent();
            returnIntent.putExtra(ContactsSelectionActivity.SELECTED_CONTACT_EXTRAS, contact);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mContacts.size();
        }
    }


    public class ContactsSelectionViewHolder extends RecyclerView.ViewHolder {
        public CardView cardViewContainer;
        public TextView contactName;
        public TextView phoneNumber;

        public ContactsSelectionViewHolder(View itemView) {
            super(itemView);
            cardViewContainer = (CardView) itemView.findViewById(R.id.card_view);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            phoneNumber = (TextView) itemView.findViewById(R.id.phone_number);
        }
    }
}