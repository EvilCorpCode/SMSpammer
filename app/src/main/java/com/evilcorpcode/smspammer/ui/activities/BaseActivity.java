package com.evilcorpcode.smspammer.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.evilcorpcode.smspammer.R;
import com.evilcorpcode.smspammer.ui.HasToolbar;
import com.evilcorpcode.smspammer.ui.snackbar.AppSnackbar;
import com.evilcorpcode.smspammer.ui.snackbar.AppSnackbarImpl;

public abstract class BaseActivity extends AppCompatActivity implements HasToolbar {
    private static final int PERMISSION_REQUEST_SMS_CODE = 0;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private Toolbar mToolbar;

    protected abstract
    @LayoutRes
    int getContentView();

    protected void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    protected abstract void setListeners();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        bindViews();
        setListeners();
        onToolbarInit(mToolbar);
        isSmsPermissionGranted();
    }

    @Override
    public void onToolbarInit(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    protected void requestContactPermission() {
        if (!isSmsPermissionGranted()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSION_REQUEST_SMS_CODE);
        }
    }

    protected boolean isSmsPermissionGranted() {
        int hasContactPermission = ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.RECEIVE_SMS);
        return (hasContactPermission == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("Permission", "Contact permission has now been granted. Showing result.");
//                    Toast.makeText(this, "Contact Permission is Granted", Toast.LENGTH_SHORT).show();
                } else {
                    AppSnackbar appSnackbar = new AppSnackbarImpl(
                            findViewById(android.R.id.content).getRootView(),
                            getString(R.string.permission_contact_not_granded),
                            Snackbar.LENGTH_INDEFINITE,
                            "Enable",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestContactPermission();
                                }
                            }
                    );
                    appSnackbar.show();

//                    Toast.makeText(this, "Contact permission was NOT granted", Toast.LENGTH_SHORT).show();
//                    Log.i("Permission", "Contact permission was NOT granted.");
                }
                break;
        }
    }
}