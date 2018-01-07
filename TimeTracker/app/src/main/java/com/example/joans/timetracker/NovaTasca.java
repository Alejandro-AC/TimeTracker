package com.example.joans.timetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.IntentFilter;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.EditText;

/**
 * Created by alexg on 07/01/2018.
 */

public class NovaTasca extends AppCompatActivity{
    private Toolbar toolbar;
    private final String tag = this.getClass().getSimpleName();

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag, "onCreate nova tasca");
        setContentView(R.layout.activity_nova_tasca);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar_new_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Nova Tasca");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_new_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
