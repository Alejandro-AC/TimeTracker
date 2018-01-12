package com.example.joans.timetracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.IntentFilter;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.Dialog;
import android.app.DialogFragment;
import java.util.Calendar;
import android.widget.TimePicker;

/**
 * Classe que gestiona l'Activity per crear una Tasca nova.
 */
public class NovaTasca extends AppCompatActivity {

    /**
     * Toolbar de l'Activity.
     */
    private Toolbar toolbar;
    private int id;

    public void setId(int id){
        this.id = id;
    }

    /**
     * String que defineix l'acció de demanar a GestorActivitats que afegeixi una nova Tasca a la
     * llista de fills de l'Activitat pare actual.
     */
    public static final String AFEGIR_TASCA = "Afegir_tasca";
    public static final String EDITAR_TASCA = "Editar_tasca";

    private final String tag = this.getClass().getSimpleName();

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean edit = getIntent().getExtras().getBoolean("edit");
        int id = getIntent().getExtras().getInt("id");
        this.setId(id);
        Log.i(tag, "onCreate nova tasca");
        setContentView(R.layout.activity_nova_tasca);
        // Inicialitzem la Toolbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar_new_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Nova Tasca");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(edit){
            String nomIntent = getIntent().getExtras().getString("nom");
            String descripcioIntent = getIntent().getExtras().getString("descripcio");
            TextInputEditText nom = findViewById(R.id.nomTasca);
            TextInputEditText descripcio = findViewById(R.id.descripcioTasca);
            nom.setText(nomIntent);
            descripcio.setText(descripcioIntent);
        }
        etProgramada = findViewById(R.id.programada_et);
        etProgramada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.programada_et:
                        showTimePickerDialog();
                        showDatePickerDialog();
                        break;
                    case R.id.temps_limit_et:
                        break;
                }
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

    public static EditText etProgramada;
    Intent inte;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final TextInputEditText nom = findViewById(R.id.nomTasca);
        final TextInputEditText descripcio = findViewById(R.id.descripcioTasca);

        Boolean edit = getIntent().getExtras().getBoolean("edit");

        switch (item.getItemId()) {
            case R.id.boto_desar:
                if (nom.getText().length() == 0) {
                    nom.setError("La Tasca necessita un nom!");
                } else {
                    if(edit){
                        Log.d(tag,"Editant tasca");
                        inte = new Intent(GestorArbreActivitats.EDITAR_TASCA);
                        inte.putExtra("id", this.id);
                        inte.putExtra("nomTasca", nom.getText().toString());
                        inte.putExtra("descripcioTasca", descripcio.getText().toString());
                        sendBroadcast(inte);
                        finish();
                    }else{
                        inte = new Intent(GestorArbreActivitats.AFEGIR_TASCA);
                        inte.putExtra("nomTasca", nom.getText().toString());
                        inte.putExtra("descripcioTasca", descripcio.getText().toString());
                        sendBroadcast(inte);
                        finish();
                    }
                }
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

    private static String dataSeleccionada;

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private DatePickerDialog.OnDateSetListener listener;

        public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendari = Calendar.getInstance();
            int any = calendari.get(Calendar.YEAR);
            int mes = calendari.get(Calendar.MONTH);
            int dia = calendari.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, any, mes, dia);
        }

        public void onDateSet(DatePicker view, int any, int mes, int dia){
            dataSeleccionada = twoDigits(dia) + "/" + twoDigits((mes + 1)) + "/" + any
                    + " " + etProgramada.getText();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        private TimePickerDialog.OnTimeSetListener listener;

        public static TimePickerFragment newInstance(TimePickerDialog.OnTimeSetListener listener) {
            TimePickerFragment fragment = new TimePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(TimePickerDialog.OnTimeSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendari = Calendar.getInstance();
            int hora = calendari.get(Calendar.HOUR_OF_DAY);
            int min = calendari.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hora, min, true);
        }

        public void onTimeSet(TimePicker timePicker, int hora, int min) {
            final String dataCompleta = dataSeleccionada + " " + (hora) + ":" + twoDigits(min);
            etProgramada.setText(dataCompleta);
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePciker, int any, int mes, int dia) {
                final String dataSeleccionada = twoDigits(dia) + "/" + twoDigits((mes + 1)) + "/" + any;
                etProgramada.setText(dataSeleccionada);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hora, int min) {
                final String dataSeleccionada = etProgramada.getText() + " " + twoDigits(hora) + ":" + twoDigits(min);
                etProgramada.setText(dataSeleccionada);
            }
        });
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }


}




