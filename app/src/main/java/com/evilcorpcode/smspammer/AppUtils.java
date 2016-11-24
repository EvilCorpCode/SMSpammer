package com.evilcorpcode.smspammer;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AppUtils {
    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
