package com.evilcorpcode.smspammer.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.evilcorpcode.smspammer.R;

public abstract class NavDrawerBaseActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    protected NavigationView mNavigationView;

    @Override
    protected void bindViews() {
        super.bindViews();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public void onToolbarInit(Toolbar toolbar) {
        super.onToolbarInit(toolbar);
        initDrawer(toolbar);
    }

    protected void initDrawer(Toolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.nav_home:
                intent = new Intent(NavDrawerBaseActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_saved_messages:
                intent = new Intent(NavDrawerBaseActivity.this, SavedMessagesListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_add_category:
                intent = new Intent(NavDrawerBaseActivity.this, AddCategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawerBaseActivity.this, R.style.MyAlertDialogStyle);
                builder.setTitle(R.string.about);
                builder.setView(R.layout.about_dialog);
                builder.setNegativeButton(getString(R.string.close), null);
                builder.show();

                break;
            case R.id.nav_share:
                String shareBody = getString(R.string.share_msg_content);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                break;

            case R.id.nav_open_github_page:
                openGithubPage();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openGithubPage() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KyriakosAlexandrou/SMSpammer")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
