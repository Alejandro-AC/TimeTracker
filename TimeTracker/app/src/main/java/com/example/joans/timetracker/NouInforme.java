package com.example.joans.timetracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Spinner;
import java.util.Calendar;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Classe que s'encarrega de la creació d'un Informe sobre un Projecte
 */
public class NouInforme extends AppCompatActivity {

    /**
     * Toolbar de l'Activity.
     */
    private Toolbar toolbar;

    /**
     * Nom del projecte del qual crearem l'Informe.
     */
    private String nomProjecte;

    /**
     * Contenen les dates per la creació de l'Informe.
     */
    public static EditText etDesde, etFins, aux;

    /**
     * Contenen els EditText de les dates.
     */
    public static TextInputLayout tilDesde, tilFins;

    /**
     * Spinners.
     */
    public static Spinner spinnerTipus, spinnerFormat;

    /**
     * Opcions dels Spinners.
     */
    private String[] tipus_array = new String[]{"Breu", "Detallat"};
    private String[] format_array = new String[]{"Text", "Web"};

    private final String tag = this.getClass().getSimpleName();

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag, "onCreate nova tasca");
        setContentView(R.layout.activity_nou_informe);

        // Obtenim el nom del Projecte del qual crearem l'Informe
        sendBroadcast(new Intent(LlistaActivitatsActivity.DONAM_NOM));

        // Inicialitzem la Toolbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar_new_activity);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tilDesde = findViewById(R.id.desde_til);

        etDesde = findViewById(R.id.desde_et);
        etDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.desde_et:
                        aux = etDesde;
                        showTimePickerDialog();
                        showDatePickerDialog();
                        break;
                }
            }
        });

        tilFins = findViewById(R.id.fins_til);

        etFins = findViewById(R.id.fins_et);
        etFins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.fins_et:
                        aux = etFins;
                        showTimePickerDialog();
                        showDatePickerDialog();
                        break;
                }
            }
        });

        spinnerTipus = (Spinner) findViewById(R.id.spinner_tipus);
        ArrayAdapter<String> tipusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, tipus_array);
        spinnerTipus.setAdapter(tipusAdapter);
        spinnerTipus.setSelection(0);
        spinnerTipus.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerTipus.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerFormat = (Spinner) findViewById(R.id.spinner_format);
        ArrayAdapter<String> formatAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, format_array);
        spinnerFormat.setAdapter(formatAdapter);
        spinnerFormat.setSelection(0);
        spinnerFormat.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerFormat.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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
            Log.d(tag, "onRecieve Receptor NouInforme");
            if (intent.getAction().equals(GestorArbreActivitats.TE_NOM)) {
                nomProjecte = (String) intent.getSerializableExtra("nom_activitat_pare_actual");
                if (nomProjecte.equals("ARREL")) {
                    toolbar.setTitle("Informe: Tots els Projectes");
                } else {
                    toolbar.setTitle("Informe: " + nomProjecte);
                }
            }
        }
    }

    private NouInforme.Receptor receptor;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.boto_desar:
                if (etDesde.getText().length() == 0 || etFins.getText().length() == 0) {
                    if (etDesde.getText().length() == 0) {
                        tilDesde.setError("Es necessita una data!");
                    } else {
                        tilDesde.setErrorEnabled(false);
                    }
                    if (etFins.getText().length() == 0) {
                        tilFins.setError("Es necessita una data!");
                    } else {
                        tilFins.setErrorEnabled(false);
                    }
                } else {
                    finish();
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
        Log.i(tag, "onResume NouInforme");

        IntentFilter filter;
        filter = new IntentFilter();
        filter.addAction(GestorArbreActivitats.TE_NOM);
        receptor = new NouInforme.Receptor();
        registerReceiver(receptor, filter);

        super.onResume();
    }

    @Override
    public final void onPause() {
        Log.i(tag, "onPause NouInforme");
        unregisterReceiver(receptor);
        super.onPause();
    }

    @Override
    public final void onDestroy() {
        Log.i(tag, "onDestroy NouInforme");
        super.onDestroy();
    }

    @Override
    public final void onStart() {
        Log.i(tag, "onStart NouInforme");
        super.onStart();
    }

    @Override
    public final void onStop() {
        Log.i(tag, "onStop NouInforme");
        super.onStop();
    }

    @Override
    public final void onRestart() {
        Log.i(tag, "onRestart NouInforme");
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

        public static NouInforme.DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            NouInforme.DatePickerFragment fragment = new NouInforme.DatePickerFragment();
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
                    + " " + aux.getText();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        private TimePickerDialog.OnTimeSetListener listener;

        public static NouInforme.TimePickerFragment newInstance(TimePickerDialog.OnTimeSetListener listener) {
            NouInforme.TimePickerFragment fragment = new NouInforme.TimePickerFragment();
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
            aux.setText(dataCompleta);
        }
    }

    private void showDatePickerDialog() {
        NouInforme.DatePickerFragment newFragment = NouInforme.DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePciker, int any, int mes, int dia) {
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        NouInforme.TimePickerFragment newFragment = NouInforme.TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hora, int min) {
            }
        });
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
}
