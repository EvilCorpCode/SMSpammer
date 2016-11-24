package com.evilcorpcode.smspammer.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evilcorpcode.smspammer.R;
import com.evilcorpcode.smspammer.models.MessagesWrapper;
import com.evilcorpcode.smspammer.models.MessagesWrapperWithCategory;
import com.evilcorpcode.smspammer.dao.MessagesWrapperWithCategoryDAO;

import java.util.List;

public class SavedMessagesListActivity extends NavDrawerBaseActivity {
    public static final String TAG = SavedMessagesListActivity.class.getName();
    public static final int REQUEST_SAVED_MESSAGES = 69;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private MessagesWrapperWithCategoryDAO mMessagesWrapperWithCategoryDAO;
    private List<MessagesWrapperWithCategory> mMessagesWrapperWithCategoryList;

    @Override
    public int getContentView() {
        return R.layout.saved_messages_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessagesWrapperWithCategoryDAO = new MessagesWrapperWithCategoryDAO(this);
        mMessagesWrapperWithCategoryList = mMessagesWrapperWithCategoryDAO.getMessagesWrapperWithCategoryList();
        initRecyclerView();
    }

    @Override
    protected void initDrawer(Toolbar toolbar) {
        super.initDrawer(toolbar);
        if (mNavigationView != null) mNavigationView.setCheckedItem(R.id.nav_saved_messages);
    }

    private void initRecyclerView() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SavedMessagesAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.saved_messages_categories);
    }

    @Override
    protected void setListeners() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SavedMessagesListActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMessagesWrapperWithCategoryList = mMessagesWrapperWithCategoryDAO.getMessagesWrapperWithCategoryList();
        if (mNavigationView != null) mNavigationView.setCheckedItem(R.id.nav_saved_messages);

        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    public class SavedMessagesAdapter extends RecyclerView.Adapter<SavedMessagesAdapter.SavedMessagesViewHolder> {

        // Create new views (invoked by the layout manager)
        @Override
        public SavedMessagesViewHolder onCreateViewHolder(
                ViewGroup parent,
                int viewType) {


            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.saved_messages_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            return new SavedMessagesViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final SavedMessagesViewHolder holder, final int position) {
            if (mMessagesWrapperWithCategoryList != null) {
                final MessagesWrapperWithCategory messagesWrapperWithCategory = mMessagesWrapperWithCategoryList.get(position);
                String category = messagesWrapperWithCategory.getCategory();
                holder.categoryValue.setText(category);

                holder.categoryEditImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SavedMessagesListActivity.this, EditSavedCategoryActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(EditSavedCategoryActivity.EXTRAS_SELECTED_CATEGORY, messagesWrapperWithCategory);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                holder.cardViewContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SavedMessagesListActivity.this, MainActivity.class);

                        //convert MessagesWrapperWithCategory to just Messages as we don't need the category field
                        MessagesWrapper messagesWrapper = new MessagesWrapper(messagesWrapperWithCategory.getMessagesWrapper().getMsgs());

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(TAG, messagesWrapper);
                        intent.putExtras(bundle);

                        startActivity(intent);
                        finish();
                    }
                });

                holder.cardViewContainer.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SavedMessagesListActivity.this, R.style.MyAlertDialogStyle);
                        builder.setTitle("Are you sure??");
                        builder.setMessage("Delete this category permanently?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMessagesWrapperWithCategoryList = mMessagesWrapperWithCategoryDAO.deleteCategory(messagesWrapperWithCategory);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.show();

                        return true;
                    }
                });

            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mMessagesWrapperWithCategoryList.size();
        }

        public class SavedMessagesViewHolder extends RecyclerView.ViewHolder {
            public ImageView categoryEditImg;
            public CardView cardViewContainer;
            public TextView categoryValue;

            public SavedMessagesViewHolder(View itemView) {
                super(itemView);
                cardViewContainer = (CardView) itemView.findViewById(R.id.card_view);
                categoryValue = (TextView) itemView.findViewById(R.id.category_value);
                categoryEditImg = (ImageView) itemView.findViewById(R.id.category_edit_img);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }
}