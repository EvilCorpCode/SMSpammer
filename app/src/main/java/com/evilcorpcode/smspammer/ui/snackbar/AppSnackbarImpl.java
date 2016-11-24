package com.evilcorpcode.smspammer.ui.snackbar;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class AppSnackbarImpl implements AppSnackbar {
    private Snackbar mSnackbar;

    /**
     * Make a Snackbar to display a message
     * <p/>
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     * <p/>
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param view        The view to find a parent from.
     * @param description The text to show.  Can be formatted text.
     * @param duration    How long to display the message.  Either {@link Snackbar#LENGTH_SHORT} or {@link
     *                    Snackbar#LENGTH_LONG}
     */
    public AppSnackbarImpl(
            @NonNull View view, @NonNull CharSequence description, @Snackbar.Duration int duration) {

        mSnackbar = Snackbar
                .make(view, description, duration);

        configureSnackbarStyle(mSnackbar);
    }

    /**
     * Make a Snackbar to display a message.
     * <p/>
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     * <p/>
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param view             The view to find a parent from.
     * @param descriptionResId The resource id of the string resource to use. Can be formatted text.
     * @param duration         How long to display the message.  Either {@link Snackbar#LENGTH_SHORT} or {@link
     *                         Snackbar#LENGTH_LONG}
     */
    public AppSnackbarImpl(@NonNull View view, @StringRes int descriptionResId, @Snackbar.Duration int duration) {
        mSnackbar = Snackbar
                .make(view, descriptionResId, duration);

        configureSnackbarStyle(mSnackbar);
    }


    /**
     * Make a Snackbar to display a message
     * <p/>
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     * <p/>
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param view            The view to find a parent from.
     * @param description     The text to show.  Can be formatted text.
     * @param duration        How long to display the message.  Either {@link Snackbar#LENGTH_SHORT} or {@link
     *                        Snackbar#LENGTH_LONG}
     * @param actionText      The text to show as an action. Can be formatted text.
     * @param onClickListener callback to be invoked when the action is clicked
     */
    public AppSnackbarImpl(
            @NonNull View view, @NonNull CharSequence description,
            @Snackbar.Duration int duration, @NonNull CharSequence actionText,
            View.OnClickListener onClickListener) {

        mSnackbar = Snackbar
                .make(view, description, duration)
                .setAction(actionText, onClickListener);

        configureSnackbarStyle(mSnackbar);
    }


    /**
     * Make a Snackbar to display a message.
     * <p/>
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     * <p/>
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *  @param view             The view to find a parent from.
     * @param descriptionResId The resource id of the string resource to use. Can be formatted text.
     * @param actionTextResId  The resource id of the string resource to use as an action. Can be formatted text.
     * @param onClickListener  callback to be invoked when the action is clicked
     */
    public AppSnackbarImpl(
            @NonNull View view, @StringRes int descriptionResId,
            @StringRes int actionTextResId,
            View.OnClickListener onClickListener) {

        mSnackbar = Snackbar
                .make(view, descriptionResId, Snackbar.LENGTH_INDEFINITE)
                .setAction(actionTextResId, onClickListener);

        configureSnackbarStyle(mSnackbar);
    }


    private void configureSnackbarStyle(Snackbar snackbar) {
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.DKGRAY);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
    }

    @Override
    public void show() {
        mSnackbar.dismiss();
        mSnackbar.show();
    }

    @Override
    public void dismissSnackbar() {
        mSnackbar.dismiss();
    }
}