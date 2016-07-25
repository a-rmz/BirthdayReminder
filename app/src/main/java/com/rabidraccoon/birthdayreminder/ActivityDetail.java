package com.rabidraccoon.birthdayreminder;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import android.support.design.widget.FloatingActionButton;

import com.rabidraccoon.birthdayreminder.utils.Contact;
import com.rabidraccoon.birthdayreminder.utils.DateUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alex on 5/22/16.
 */
public class ActivityDetail extends Activity {

    Toolbar toolbar;
    FloatingActionButton sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Validate if the device is on landscape mode
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        setContentView(R.layout.activity_contact_detail);

        int ID = getIntent().getIntExtra("contactID", 0);
        String name = getIntent().getStringExtra("contactName");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        FragmentContactDetail fragment = (FragmentContactDetail) getFragmentManager().findFragmentByTag("detail");
        fragment.setContact(ID);

        sendMessage = (FloatingActionButton) findViewById(R.id.fab_send);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }
}
