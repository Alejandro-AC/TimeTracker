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


public class NouProjecte extends AppCompatActivity {

    private final String tag = this.getClass().getSimpleName();

    private Toolbar toolbar;

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nou_projecte);
        Log.i(tag, "onCreate nou projecte");

        toolbar = (Toolbar) findViewById(R.id.my_toolbar_new_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Nou Projecte");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
        });

        final TextInputLayout nom = (TextInputLayout) findViewById(R.id.nomWrapper);
        final TextInputLayout descripcio = (TextInputLayout) findViewById(R.id.descripcioWrapper);

        nom.setHint("Nom");
        descripcio.setHint("Descripcio");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_new_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.boto_desar:
                //
                break;
            default:
                break;
        }
        return true;
    }

    public class Receptor extends BroadcastReceiver {
        private final String tag = this.getClass().getCanonicalName();

        @Override
        public final void onReceive(final Context context,
                                    final Intent intent) {
            Log.d(tag, "onRecieve Receptor NouProjecte");
        }
    }

    private Receptor receptor;

    @Override
    public final void onBackPressed() {
        Log.i(tag, "onBackPressed");
        startActivity(new Intent(NouProjecte.this, LlistaActivitatsActivity.class));
    }

    @Override
    public final void onResume() {
        Log.i(tag, "onREsume NouProjecte");

        IntentFilter filter;
        filter = new IntentFilter();
        filter.addAction(GestorArbreActivitats.AFEGIR_PROJECTE);
        receptor = new Receptor();
        registerReceiver(receptor, filter);

        super.onResume();
    }

    @Override
    public final void onPause() {
        Log.i(tag, "onPause NouProjecte");
        unregisterReceiver(receptor);
        super.onPause();
    }

    @Override
    public final void onDestroy() {
        Log.i(tag, "onDestroy NouProjecte");
        super.onDestroy();
    }

    @Override
    public final void onStart() {
        Log.i(tag, "onStart NouProjecte");
        super.onStart();
    }

    @Override
    public final void onStop() {
        Log.i(tag, "onStop NouProjecte");
        super.onStop();
    }

    @Override
    public final void onRestart() {
        Log.i(tag, "onRestart NouProjecte");
        super.onRestart();
    }

    @Override
    public final void onSaveInstanceState(final Bundle savedInstanceState) {
        Log.i(tag, "onSaveInstanceState");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public final void onRestoreInstanceState(final Bundle savedInstanceState) {
        Log.i(tag, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public final void onConfigurationChanged(final Configuration newConfig) {
        Log.i(tag, "onConfigurationChanged");
        if (Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, newConfig.toString());
        }
    }


}
