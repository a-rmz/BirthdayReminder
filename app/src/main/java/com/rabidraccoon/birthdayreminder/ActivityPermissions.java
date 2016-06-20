package com.rabidraccoon.birthdayreminder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityPermissions extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Button accept;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        count = 0;

        accept = (Button) findViewById(R.id.permissions_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Intent intent = new Intent(ActivityPermissions.this, ActivityMain.class);
                Snackbar.make(findViewById(R.id.permissions_view), getString(R.string.permission_yeap), Snackbar.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            } else {
                count++;
                Snackbar.make(findViewById(R.id.permissions_view), getString(R.string.permission_nope), Snackbar.LENGTH_SHORT).show();
            }
            // Change the description text
            if(count > 5) {
                ((TextView) findViewById(R.id.permissions_description)).setText(getString(R.string.permission_give));
            }
        }
    }
}
