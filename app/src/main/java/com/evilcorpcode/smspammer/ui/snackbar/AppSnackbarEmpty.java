package com.evilcorpcode.smspammer.ui.snackbar;

import android.util.Log;

/**
 * To avoid null checks for when using the {@code AppSnackbarImpl} in case it is not ready yet.
 */
public class AppSnackbarEmpty implements AppSnackbar {
    private final String TAG = AppSnackbarEmpty.class.getName();

    @Override
    public void show() {
        Log.w(TAG, "Trying to show an empty Snackbar!!");
    }

    @Override
    public void dismissSnackbar() {
        Log.w(TAG, "Trying to dismiss an empty Snackbar!!");
    }
}
