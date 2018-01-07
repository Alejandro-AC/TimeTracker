package com.example.joans.timetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
    public static final String AFEGIR_TASCA = "Afegir_tasca";
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


    public class Receptor extends BroadcastReceiver {
        private final String tag = this.getClass().getCanonicalName();

        @Override
        public final void onReceive(final Context context,
                                    final Intent intent) {
            Log.d(tag, "onRecieve Receptor NovaTasca");
        }
    }

    private Receptor receptor;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent inte;
        final TextInputEditText nom = findViewById(R.id.nomTasca);
        final TextInputEditText descripcio = findViewById(R.id.descripcioTasca);
        switch (item.getItemId()) {
            case R.id.boto_desar:
                System.out.println("nombre recogido tasca:"+nom.getText().toString());
                System.out.println("descripcion recogida tasca:"+descripcio.getText().toString());
                inte = new Intent(GestorArbreActivitats.AFEGIR_TASCA);
                inte.putExtra("nomTasca", nom.getText().toString());
                inte.putExtra("descripcioTasca", descripcio.getText().toString());
                sendBroadcast(inte);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public final void onBackPressed() {
        Log.i(tag, "onBackPressed");
        finish();
    }

    @Override
    public final void onResume() {
        Log.i(tag, "onREsume NovaTasca");

        IntentFilter filter;
        filter = new IntentFilter();
        filter.addAction(GestorArbreActivitats.AFEGIR_PROJECTE);
        receptor = new NovaTasca.Receptor();
        registerReceiver(receptor, filter);

        super.onResume();
    }

    @Override
    public final void onPause() {
        Log.i(tag, "onPause NovaTasca");
        unregisterReceiver(receptor);
        super.onPause();
    }

    @Override
    public final void onDestroy() {
        Log.i(tag, "onDestroy NovaTasca");
        super.onDestroy();
    }

    @Override
    public final void onStart() {
        Log.i(tag, "onStart NovaTasca");
        super.onStart();
    }

    @Override
    public final void onStop() {
        Log.i(tag, "onStop NovaTasca");
        super.onStop();
    }

    @Override
    public final void onRestart() {
        Log.i(tag, "onRestart NovaTasca");
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
