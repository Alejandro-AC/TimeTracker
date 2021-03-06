package com.example.joans.timetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nucli.Projecte;

/**
 * Gestiona l'Activity que mostra els detalls de l'Activitat solicitada.
 */
public class InformacioActivity extends AppCompatActivity {

    /**
     * Toolbar de l'Activity.
     */
    private Toolbar toolbar;

    private final String tag = this.getClass().getSimpleName();

    private String type;

    private int id;

    public void setId(int id) { this.id = id; }

    public void setType(String type){
        this.type = type;
    }

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        Log.i(tag, "onCreate InformacioActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacio_activitat);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // En aquesta Activity no cal mostrar cap títol en la Toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Demanem al GestorArbreActivitats la llista de fills, ja que és on es troba
        // l'Activitat de la qual es volen veure els Detalls
        sendBroadcast(new Intent(LlistaActivitatsActivity.DONAM_FILLS));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_edit, menu);
        /*MenuItem item = menu.findItem(R.id.boto_detalls);
        item.setVisible(false);
        item = menu.findItem(R.id.boto_informes);
        item.setVisible(false);*/

        return super.onCreateOptionsMenu(menu);
    }


    public class Receptor extends BroadcastReceiver {
        private final String tag = this.getClass().getCanonicalName();

        @Override
        public final void onReceive(final Context context,
                                    final Intent intent) {
            Log.d(tag, "onRecieve Receptor InformacioActivity");

            if (intent.getAction().equals(GestorArbreActivitats.TE_FILLS)) {
                ArrayList<DadesActivitat> llistaDadesAct =
                        (ArrayList<DadesActivitat>) intent
                                .getSerializableExtra("llista_dades_activitats");

                // L'Activitat que busquem es la mateixa que hem seleccionat amb un longClick
                // (utilitzem la seva posició per obtenir-la de la llista)
                DadesActivitat activitat = llistaDadesAct.get(LlistaActivitatsActivity.posicioItemLongClickat);
                setId(activitat.getId());
                if(activitat.isProjecte()){
                    setType("projecte");
                }
                if(activitat.isTasca()){
                    setType("tasca");
                }
                TextView txtNom = findViewById(R.id.text_nom),
                        txtDescripcio = findViewById(R.id.text_descripcio),
                        txtDataInici = findViewById(R.id.text_data_inici),
                        txtDataFinal = findViewById(R.id.text_data_final),
                        txtTempsTotal = findViewById(R.id.text_temps_total);
                txtNom.setText(activitat.getNom());
                txtDescripcio.setText(activitat.getDescripcio());
                txtDataInici.setText(activitat.toStringInicial());
                txtDataFinal.setText(activitat.toStringFinal());
                txtTempsTotal.setText(activitat.toStringTemps());

                if (activitat.isCronometreEngegat()) {
                    txtNom.setTextColor(getResources().getColor(R.color.colorRunning));
                    txtDescripcio.setTextColor(getResources().getColor(R.color.colorRunning));
                    txtDataInici.setTextColor(getResources().getColor(R.color.colorRunning));
                    txtDataFinal.setTextColor(getResources().getColor(R.color.colorRunning));
                    txtTempsTotal.setTextColor(getResources().getColor(R.color.colorRunning));
                }

                ImageView imgView = (ImageView)findViewById(R.id.activity_icon);
                if (activitat.isProjecte()) {
                    imgView.setImageResource(R.drawable.ic_project_white);
                } else {
                    imgView.setImageResource(R.drawable.ic_task_white);
                }

                toolbar.setTitle(activitat.getNom());
            }
        }
    }

    private InformacioActivity.Receptor receptorInfo;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView nom = findViewById(R.id.text_nom);
        String nomStr = nom.getText().toString();
        TextView descripcio = findViewById(R.id.text_descripcio);
        String descripcioStr = descripcio.getText().toString();

        switch (item.getItemId()) {
            case R.id.boto_opcions:
                break;
            case R.id.boto_editar:
                System.out.println(nomStr);
                System.out.println(descripcioStr);
                if(this.type.equals("tasca")){
                    Intent editTask = new Intent(InformacioActivity.this, NovaTasca.class);
                    editTask.putExtra("edit", true);
                    editTask.putExtra("id", this.id);
                    editTask.putExtra("nom", nomStr);
                    editTask.putExtra("descripcio", descripcioStr);
                    startActivity(editTask);
                    finish();
                }
                if(this.type.equals("projecte")){
                    Intent editProject = new Intent(InformacioActivity.this, NouProjecte.class);
                    editProject.putExtra("edit", true);
                    editProject.putExtra("id", this.id);
                    editProject.putExtra("nom", nomStr);
                    editProject.putExtra("descripcio", descripcioStr);
                    startActivity(editProject);
                    finish();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private InformacioActivity.Receptor receptor;

    @Override
    public final void onBackPressed() {
        Log.i(tag, "onBackPressed");
        startActivity(new Intent(InformacioActivity.this, LlistaActivitatsActivity.class));
    }

    @Override
    public final void onResume() {
        Log.i(tag, "onResume NouProjecte");

        IntentFilter filter;
        filter = new IntentFilter();
        filter.addAction(GestorArbreActivitats.TE_FILLS);
        receptor = new InformacioActivity.Receptor();
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
