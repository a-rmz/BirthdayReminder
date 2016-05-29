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

        String[] contactInfo = getIntent().getStringArrayExtra("contactInfo");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(contactInfo[0]);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        TextView nameCaption = (TextView) findViewById(R.id.contact_detail_name);
        TextView birthdayCaption = (TextView) findViewById(R.id.contact_detail_birthday);
        TextView ageCaption = (TextView) findViewById(R.id.contact_detail_age);
        nameCaption.setText(contactInfo[0]);
        birthdayCaption.setText(DateUtils.toHuman(contactInfo[1]));
        ageCaption.setText(getString(R.string.age, DateUtils.calculateAge(contactInfo[1])));

        CircleImageView contactImage = (CircleImageView) findViewById(R.id.contact_detail_image);
        if(contactInfo[2] != null) contactImage.setImageURI(Uri.parse(contactInfo[2]));

        sendMessage = (FloatingActionButton) findViewById(R.id.fab_send);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Happy Birthday!");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");

                startActivity(sendIntent);

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
