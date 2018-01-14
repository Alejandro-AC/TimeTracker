package com.example.joans.timetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.design.internal.NavigationMenu;
import android.widget.ImageView;
import android.support.design.widget.FloatingActionButton;
import android.graphics.Color;
import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;


/**
 * Mostra la llista de projectes i tasques filles del projecte pare actual.
 * Inicialment, en engegar la aplicació per primer cop (o quan s'ha engegat el
 * telèfon) mostra doncs les activitats del primer "nivell", considerant que hi
 * ha un projecte arrel invisible de cara als usuaris que és el seu projecte
 * pare.
 * <p>
 * Juntament amb el nom de projecte o tasca se'n mostra el temps total
 * cronometrat. I mentre que s'està cronometrant alguna tasca d'aquestes, o bé
 * descendent d'un dels projectes mostrats, el seu temps es veu que es va
 * actualitzant. Per tal de mostrar nom i durada mitjançant les
 * <code>ListView</code> d'Android, hem hagut de dotar la classe
 * <code>DadesActivitat</code> d'un mètode <code>toString</code> que és invocat
 * per un objecte de classe <code>Adapter</code>, que fa la connexió entre la
 * interfase i les dades que mostra.
 * <p>
 * També gestiona els events que permeten navegar per l'arbre de projectes i
 * tasques :
 * <ul>
 * <li>un click sobre un element de la llista baixa de nivell: passa a mostrar
 * els seus "fills", la siguin subprojectes i tasques (si era un projecte) o
 * intervals (si era tasca)</li>
 * <li>tecla o botó "back" puja de nivell: anem al projecte para del les
 * activitats de les quals mostrem les dades, o si ja són del primer nivell i no
 * podem pujar més, anem a la pantalla "Home"</li>
 * </ul>
 * I també events per tal de cronometrar una tasca p parar-ne el cronòmetre,
 * mitjançant un click llarg.
 * <p>
 * Totes dues funcions no són dutes a terme efectivament aquí sinó a
 * <code>GestorArbreActivitat</code>, que manté l'arbre de tasques, projectes i
 * intervals en memòria. Cal fer-ho així per que Android pot eliminar (
 * <code>destroy</code>) la instància d'aquesta classe quan no és visible per
 * que estem interactuant amb alguna altra aplicació, si necessita memòria. En
 * canvi, un servei com és <code>GestorArbreActivitats</code> només serà
 * destruït en circumstàncies extremes. La comunicació amb el servei es fa
 * mitjançant "intents", "broadcast" i una classe privada "receiver".
 *
 * @author joans
 * @version 6 febrer 2012
 */

public class LlistaActivitatsActivity extends AppCompatActivity {

    /**
     * Toolbar de l'Activity.
     */
    private Toolbar toolbar;

    /**
     * Menú de la Toolbar. Conté les diferents opcions de aquesta.
     */
    private Menu menuToolbar;

    /**
     * Floating Action Button que permet començar a cronometrar i parar una Tasca.
     */
    private FloatingActionButton fabPlay;

    /**
     * Floating Action Button amb l'efecte Speed Dial.
     * Mostra les opcions de creació d'una nova Activitat (Projecte o Tasca) i d'un Informe.
     */
    private FabSpeedDial fabSpeedDial;

    /**
     * View seleccionada
     */
    private View selectedView;

    /**
     * Nom de l'Activitat pare de les Activitats que s'estan mostrant a la llista.
     */
    private String nomActivitatPareActual = "";

    /**
     * Flag que indica si hi ha una Activitat de la Llista que està seleccionada (s'ha fet
     * LongClick sobre ella).
     */
    private boolean longClick = false;

    /**
     * Posició de l'Activitat que s'ha seleccionat.
     * Prendrà el valor -1 si no hi ha cap Activitat seleccionada.
     */
    public static int posicioItemLongClickat = -1;

    /**
     * Indica si hi ha tasques que s'estan cronometrant en algun lloc de l'arbre.
     */
    public static boolean tasquesCronometrant = false;



    /**
     * Nom de la classe per fer aparèixer als missatges de logging del LogCat.
     *
     * @see Log
     */
    private final String tag = this.getClass().getSimpleName();

    /**
     * Grup de vistes (controls de la interfase gràfica) que consisteix en un
     * <code>TextView</code> per a cada activitat a mostrar.
     */
    private ListView arrelListView;

    /**
     * Adaptador necessari per connectar les dades de la llista de projectes i
     * tasques filles del projecte pare actual, amb la interfase, segons el
     * mecanisme estàndard d'Android.
     * <p>
     * Per tal de fer-lo servir, cal que la classe <code>DadesActivitat</code>
     * tingui un mètode <code>toString</code> que retornarà l'string a mostrar
     * en els TextView (controls de text) de la llista ListView.
     */
    private ActivityListAdapter aaAct;

    /**
     * Llista de dades de les activitats (projectes i tasques) mostrades
     * actualment sense ordenar, filles del (sub)projecte on estem posicionats actualment.
     */
    private List<DadesActivitat> llistaDadesActivitatsAux;

    /**
     * Llista de dades de les activitats (projectes i tasques) mostrades
     * actualment, filles del (sub)projecte on estem posicionats actualment.
     */
    private List<DadesActivitat> llistaDadesActivitats;

    /**
     * Flag que ens servirà per decidir fer que si premem el botó/tecla "back"
     * quan estem a l'arrel de l'arbre de projectes, tasques i intervals : si és
     * que si, desem l'arbre i tornem a la pantalla "Home", sinó hem d'anar al
     * projecte pare del pare actual (pujar de nivell).
     */
    private boolean activitatPareActualEsArrel;

    /**
     * Rep els "intents" que envia <code>GestorArbreActivitats</code> amb les
     * dades de les activitats a mostrar. El receptor els rep tots (no hi ha cap
     * filtre) per que només se'n n'hi envia un, el "TE_FILLS".
     *
     * @author joans
     * @version 6 febrer 2012
     */
    private class Receptor extends BroadcastReceiver {
        /**
         * Nom de la classe per fer aparèixer als missatges de logging del
         * LogCat.
         *
         * @see Log
         */
        private final String tag = this.getClass().getCanonicalName();

        /**
         * Gestiona tots els intents enviats, de moment només el de la
         * acció TE_FILLS. La gestió consisteix en actualitzar la llista
         * de dades que s'està mostrant mitjançant el seu adaptador.
         *
         * @param context
         * @param intent
         * objecte Intent que arriba per "broadcast" i del qual en fem
         * servir l'atribut "action" per saber quina mena de intent és
         * i els extres per obtenir les dades a mostrar i si el projecte
         * actual és l'arrel de tot l'arbre o no
         *
         */
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.i(tag, "onReceive");
            if (intent.getAction().equals(GestorArbreActivitats.TE_FILLS)) {
                activitatPareActualEsArrel = intent.getBooleanExtra(
                        "activitat_pare_actual_es_arrel", false);
                // Obtenim la nova llista de dades d'activitat que ve amb
                // l'intent
                @SuppressWarnings("unchecked")
                ArrayList<DadesActivitat> llistaDadesAct =
                        (ArrayList<DadesActivitat>) intent
                                .getSerializableExtra("llista_dades_activitats");
                aaAct.clear();

                if(!llistaDadesAct.isEmpty()) {
                    for (DadesActivitat dadesAct : llistaDadesAct) {
                        aaAct.add(dadesAct);
                    }
                }
                // Això farà redibuixar el ListView
                aaAct.notifyDataSetChanged();
                Log.d(tag, "mostro els fills actualitzats");

                // Mirem si hi ha alguna tasca que s'està cronometrant
                tasquesCronometrant = isTasquesCronometrant(llistaDadesAct);

                if (activitatPareActualEsArrel) {
                    // Si ens trobem en els fills de l'arrel de l'arbre d'Activitats, no cal
                    // mostrar el botó de Back a la Toolbar. En el seu lloc posarem l'icona de l'APP
                    toolbar.setNavigationIcon(R.drawable.ic_timetracker);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });

                    if (menuToolbar != null) {
                        // Si estem de tornada, cal tornar a mostrar el botó per veure els informes
                        // de tot el programa (Informes generals)
                        MenuItem itemInformesGenerals = menuToolbar.findItem(R.id.boto_informes_arrel);
                        itemInformesGenerals.setVisible(true);
                    }

                    ImageView imgView = (ImageView)findViewById(R.id.activity_icon);
                    imgView.setVisibility(View.GONE);

                    // A la Toolbar apareix el nom de l'app
                    toolbar.setTitle(R.string.app_name);
                    nomActivitatPareActual = "";
                } else {
                    // Si ens trobem a l'interior de l'arbre, mostrarem a la Toolbar el botó de Back
                    toolbar.setNavigationIcon(R.drawable.ic_back);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });

                    MenuItem itemInformesGenerals = menuToolbar.findItem(R.id.boto_informes_arrel);
                    itemInformesGenerals.setVisible(false);

                    // Mostrem a la Toolbar el nom de l'Activitat pare
                    sendBroadcast(new Intent(LlistaActivitatsActivity.DONAM_NOM));
                    toolbar.setTitle(nomActivitatPareActual);

                    // Mostrem a la Toolbar l'icona corresponent a la classe de l'Activitat pare
                    // (en aquesta Activity, sempre serà un Projecte)
                    ImageView imgView = (ImageView)findViewById(R.id.activity_icon);
                    imgView.setImageResource(R.drawable.ic_project_white);
                    imgView.setVisibility(View.VISIBLE);
                }

            } else if (intent.getAction().equals(GestorArbreActivitats.TE_NOM)) {
                Log.d(tag, "rebent nom activitat pare actual");
                // Obtenim el nom de l'Activitat pare actual
                nomActivitatPareActual = (String) intent.getSerializableExtra("nom_activitat_pare_actual");
                if (!activitatPareActualEsArrel && !nomActivitatPareActual.equals("")) {
                    toolbar.setTitle(nomActivitatPareActual);
                }
            } else {
                // no pot ser
                assert false : "intent d'acció no prevista";
            }
        }
    }

    /**
     * Objecte únic de la classe {@link Receptor}.
     */
    private Receptor receptor;

    // Aquests són els "serveis", identificats per un string, que demana
    // aquesta classe a la classe Service GestorArbreActivitats, en funció
    // de la interacció de l'usuari:

    /**
     * String que defineix l'acció de demanar a GestorActivitats el nom de l'Activitat pare
     * del nivell on ens trobem actualment.
     */
    public static final String DONAM_NOM = "Donam_nom";

    /**
     * String que defineix l'acció de demanar a <code>GestorActivitats</code> la
     * llista de les dades dels fills de l'activitat actual, que és un projecte.
     * Aquesta llista arribarà com a dades extres d'un Intent amb la "acció"
     * TE_FILLS.
     *
     * @see GestorArbreActivitats.Receptor
     */
    public static final String DONAM_FILLS = "Donam_fills";

    /**
     * String que defineix l'acció de demanar a <code>GestorActivitats</code>
     * que engegui el cronòmetre de la tasca clicada.
     */
    public static final String ENGEGA_CRONOMETRE = "Engega_cronometre";

    /**
     * String que defineix l'acció de demanar a <code>GestorActivitats</code>
     * que pari el cronòmetre de la tasca clicada.
     */
    public static final String PARA_CRONOMETRE = "Para_cronometre";

    /**
     * String que defineix l'acció de demanar a <code>GestorActivitats</code>
     * que escrigui al disc l'arbre actual.
     */
    public static final String DESA_ARBRE = "Desa_arbre";

    /**
     * String que defineix l'acció de demanar a <code>GestorActivitats</code>
     * que el projecte pare de les activitats actuals sigui el projecte que
     * l'usuari ha clicat.
     */
    public static final String BAIXA_NIVELL = "Baixa_nivell";

    /**
     * String que defineix l'acció de demanar a <code>GestorActivitats</code>
     * que sha de cambiar el ordre que mostra les activitats.
     */
    public static final String CAMBIA_ORDRE = "Cambia_ordre";

    /**
     * String que defineix l'acció de demanar a <code>GestorActivitats</code>
     * que el projecte pare passi a ser el seu pare, o sigui, pujar de nivell.
     */
    public static final String PUJA_NIVELL = "Puja_nivell";

    /**
     * En voler pujar de nivell quan ja som a dalt de tot vol dir que l'usuari
     * desitja "deixar de treballar del tot" amb la aplicació, així que "parem"
     * el servei <code>GestorActivitats</code>, que vol dir parar el cronòmetre
     * de les tasques engegades, si n'hi ha alguna, desar l'arbre i parar
     * (invocant <code>stopSelf</code>) el servei. Tot això es fa a
     * {@link GestorArbreActivitats#paraServei}.
     */
    public static final String PARA_SERVEI = "Para_servei";

    /**
     * Quan aquesta Activity es mostra després d'haver estat ocultada per alguna
     * altra Activity cal tornar a fer receptor i el seu filtre per que atengui
     * als intents que es redifonen (broadcast). I també engegar el servei
     * <code>GestorArbreActivitats</code>, si és la primera vegada que es mostra
     * aquesta Activity. En fer-ho, el servei enviarà la llista de dades de les
     * activitats filles del projecte arrel actual.
     */
    @Override
    public final void onResume() {
        Log.i(tag, "onResume");

        sendBroadcast(new Intent(LlistaActivitatsActivity.DONAM_NOM));
        toolbar.setTitle(nomActivitatPareActual);

        IntentFilter filter;
        filter = new IntentFilter();
        filter.addAction(GestorArbreActivitats.TE_FILLS);
        filter.addAction(GestorArbreActivitats.TE_NOM);
        receptor = new Receptor();
        registerReceiver(receptor, filter);

        // al tornar a mostrar aquesta activitat, cal inicialitzar una altra vegada els atributs
        // que serveixen per controlar els longClicks
        posicioItemLongClickat = -1;
        longClick = false;

        // Crea el servei GestorArbreActivitats, si no existia ja. A més,
        // executa el mètode onStartCommand del servei, de manera que
        // *un cop creat el servei* = havent llegit ja l'arbre si es el
        // primer cop, ens enviarà un Intent amb acció TE_FILLS amb les
        // dades de les activitats de primer nivell per que les mostrem.
        // El que no funcionava era crear el servei (aquí mateix o
        // a onCreate) i després demanar la llista d'activiats a mostrar
        // per que startService s'executa asíncronament = retorna de seguida,
        // i la petició no trobava el servei creat encara.
        startService(new Intent(this, GestorArbreActivitats.class));

        super.onResume();
        Log.i(tag, "final de onResume");
    }

    /**
     * Just abans de quedar "oculta" aquesta Activity per una altra, anul·lem el
     * receptor de intents.
     */
    @Override
    public final void onPause() {
        Log.i(tag, "onPause");
        unregisterReceiver(receptor);

        // Deixem la Toolbar, el fabPlay i el fabSpeedDial en el seu estat original (quan no hi ha
        // cap Activitat seleccionada)
        fabPlay.setImageResource(R.drawable.ic_play);
        fabPlay.setVisibility(View.GONE);


        MenuItem itemDetalls = menuToolbar.findItem(R.id.boto_detalls);
        itemDetalls.setVisible(false);
        MenuItem itemDelete = menuToolbar.findItem(R.id.boto_delete);
        itemDelete.setVisible(false);
        MenuItem itemInformes = menuToolbar.findItem(R.id.boto_informes);
        itemInformes.setVisible(false);

        fabSpeedDial.setVisibility(View.VISIBLE);


        super.onPause();
    }

    /**
     * Estableix com a activitats a visualitzar les filles del projecte
     * arrel, així com els dos listeners que gestionen els
     * events de un click normal i un click llarg. El primer serveix per navegar
     * "cap avall" per l'arbre, o sigui, veure els fills d'un projecte o els
     * intervals d'una tasca. El segon per cronometrar, en cas que haguem clicat
     * sobre una tasca.
     *
     * @param savedInstanceState
     *            de tipus Bundle, però no el fem servir ja que el pas de
     *            paràmetres es fa via l'objecte aplicació
     *            <code>TimeTrackerApplication</code>.
     */
    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_activitats);
        Log.i(tag, "onCreate");

        // Quan es crea aquesta Activity, ens trobem en els fills de l'Activitat arrel
        // per tant, hem de mostrar només el nom de l'APP
        toolbar = (Toolbar) this.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ImageView imgView = (ImageView)findViewById(R.id.activity_icon);
        imgView.setVisibility(View.GONE);


        arrelListView = (ListView) this.findViewById(R.id.listViewActivitats);

        llistaDadesActivitats = new ArrayList<DadesActivitat>();


        // Ara utilitza el nou Adaptador per poder mostrar imatges en la Llista.
        aaAct = new ActivityListAdapter(this, llistaDadesActivitats);

        arrelListView.setAdapter(aaAct);

        // Un click serveix per navegar per l'arbre de projectes, tasques
        // i intervals. Un long click es per cronometrar una tasca, si és que
        // l'item clicat es una tasca (sinó, no es fa res).

        arrelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> arg0, final View view,
                                    final int pos, final long id) {
                Log.i(tag, "onItemClick");
                Log.d(tag, "pos = " + pos + ", id = " + id);

                if (!longClick) {

                    // Si no hi ha un element de la llista que està seleccionat, baixem un nivell
                    // de l'arbre d'Activitats
                    Intent inte = new Intent(LlistaActivitatsActivity.BAIXA_NIVELL);
                    inte.putExtra("posicio", pos);
                    sendBroadcast(inte);
                    if (llistaDadesActivitats.get(pos).isProjecte()) {
                        sendBroadcast(new Intent(
                                LlistaActivitatsActivity.DONAM_FILLS));
                        Log.d(tag, "enviat intent DONAM_FILLS");
                        Log.d(tag, "name"+llistaDadesActivitats.get(pos).getNom());

                        // Cal mostrar l'icona dels Projectes juntament amb el nom del Projecte pare
                        ImageView imgView = (ImageView)findViewById(R.id.activity_icon);
                        imgView.setVisibility(View.VISIBLE);
                    } else if (llistaDadesActivitats.get(pos).isTasca()) {
                        startActivity(new Intent(LlistaActivitatsActivity.this,
                                LlistaIntervalsActivity.class));
                        // en aquesta classe ja es demanara la llista de fills
                    } else {
                        // no pot ser!
                        assert false : "activitat que no es projecte ni tasca";
                    }

                    // Intent per obtenir el nom de l'activitat pare de l'actual
                    sendBroadcast(new Intent(LlistaActivitatsActivity.DONAM_NOM));

                } else {
                    // Si a la llista d'Activitats hi ha un element seleccionat (s'ha fet un
                    // longClick)
                    if (pos == posicioItemLongClickat) {
                        // Si l'item que hem clickat és el mateix que tenim seleccionat, treïem
                        // el longClick que hi ha sobre l'item
                        longClick = false;
                        posicioItemLongClickat = -1;



                        view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        fabPlay.setVisibility(View.GONE);
                        fabSpeedDial.setVisibility(View.VISIBLE);

                        MenuItem itemDetalls = menuToolbar.findItem(R.id.boto_detalls);
                        itemDetalls.setVisible(false);
                        MenuItem itemDelete = menuToolbar.findItem(R.id.boto_delete);
                        itemDelete.setVisible(false);
                        MenuItem itemInformes = menuToolbar.findItem(R.id.boto_informes);
                        itemInformes.setVisible(false);
                    }
                }
            }
        });

        // Un "long click" serveix per seleccionar una Activitat amb la qual volem interactuar,
        // tant per encendre el seu cronómetre com per parar-lo, o per veure els seus Detalls.
        arrelListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> arg0,
                                           final View view, final int pos, final long id) {
                Log.i(tag, "onItemLongClick");
                Log.d(tag, "pos = " + pos + ", id = " + id);

                // Mostrem el botó dels Detalls
                MenuItem item = menuToolbar.findItem(R.id.boto_detalls);
                item.setVisible(true);
                MenuItem itemDelete = menuToolbar.findItem(R.id.boto_delete);
                itemDelete.setVisible(true);


                // Guardem la View seleccionada (per si la necessitem en un futur per canviar el
                // color del fons.
                selectedView = view;

                if (llistaDadesActivitats.get(pos).isTasca()) {
                    if (!longClick) {
                        // Si no tenim cap altre element seleccionat, podem seleccionar la Tasca
                        view.setBackgroundColor(Color.GRAY);
                        fabSpeedDial.setVisibility(View.GONE);

                        // Mostrem el fabPlay amb la imatge que toca
                        if (llistaDadesActivitats.get(pos).isCronometreEngegat()) {
                            fabPlay.setImageResource(R.drawable.ic_stop);
                        } else {
                            fabPlay.setImageResource(R.drawable.ic_play);
                        }

                        fabPlay.setVisibility(View.VISIBLE);

                        // Guardem els atributs relacionats amb el longClick realitzat
                        longClick = true;
                        posicioItemLongClickat = pos;
                    }
                } else {
                    if (!longClick) {
                        // Si no tenim cap altre element seleccionat, podem seleccionar el Projecte
                        view.setBackgroundColor(Color.GRAY);


                        fabSpeedDial.setVisibility(View.GONE);

                        MenuItem itemInformes = menuToolbar.findItem(R.id.boto_informes);
                        itemInformes.setVisible(true);

                        // Guardem els atributs relacionats amb el longClick realitzat
                        longClick = true;
                        posicioItemLongClickat = pos;
                    }

                }

                // si es un projecte, no fem res

                // Important :
                // "Programming Android", Z. Mednieks, L. Dornin,
                // G. Meike, M. Nakamura, O'Reilly 2011, pag. 187:
                //
                // If the listener returns false, the event is dispatched
                // to the View methods as though the handler did not exist.
                // If, on the other hand, a listener returns true, the event
                // is said to have been consumed. The View aborts any further
                // processing for it.
                //
                // Si retornem false, l'event long click es tornat a processar
                // pel listener de click "normal", fent que seguidament a
                // ordenar el cronometrat passem a veure la llista d'intervals.
                return true;
            }
        });

        fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.fab_project:
                        Intent nouProjecte = new Intent(LlistaActivitatsActivity.this, NouProjecte.class);
                        nouProjecte.putExtra("edit",false);
                        startActivity(nouProjecte);
                        break;
                    case R.id.fab_task:
                        Intent novaTasca = new Intent(LlistaActivitatsActivity.this, NovaTasca.class);
                        novaTasca.putExtra("edit",false);
                        startActivity(novaTasca);
                        break;
                    case R.id.fab_report:
                        startActivity( new Intent(LlistaActivitatsActivity.this, NouInforme.class));
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        fabPlay = (FloatingActionButton) findViewById(R.id.fab_play);
        fabPlay.setVisibility(View.GONE);
        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte;
                if (!llistaDadesActivitats.get(posicioItemLongClickat).isCronometreEngegat()) {
                    // Si no s'està cronometrant la Tasca, la comencem a cronometrar
                    inte = new Intent(
                            LlistaActivitatsActivity.ENGEGA_CRONOMETRE);
                    Log.d(tag, "enviat intent ENGEGA_CRONOMETRE de "
                            + llistaDadesActivitats.get(posicioItemLongClickat).getNom());
                } else {
                    // Si s'està cronometrant la Tasca, la parem
                    inte = new Intent(
                            LlistaActivitatsActivity.PARA_CRONOMETRE);
                    Log.d(tag, "enviat intent PARA_CRONOMETRE de "
                            + llistaDadesActivitats.get(posicioItemLongClickat).getNom());
                }
                inte.putExtra("posicio", posicioItemLongClickat);
                sendBroadcast(inte);

                // Treiem la selecció de l'Activitat en qüestió
                MenuItem itemDetalls = menuToolbar.findItem(R.id.boto_detalls);
                itemDetalls.setVisible(false);
                MenuItem itemDelete = menuToolbar.findItem(R.id.boto_delete);
                itemDelete.setVisible(false);
                MenuItem itemInformes = menuToolbar.findItem(R.id.boto_informes);
                itemInformes.setVisible(false);

                selectedView.setBackgroundColor(Color.parseColor("#EEEEEE"));

                fabPlay.setVisibility(View.GONE);

                fabSpeedDial.setVisibility(View.VISIBLE);

                longClick = false;
                posicioItemLongClickat = -1;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuToolbar = menu;
        getMenuInflater().inflate(R.menu.menu_toolbar_activitat, menu);
        MenuItem itemDetalls = menuToolbar.findItem(R.id.boto_detalls);
        itemDetalls.setVisible(false);
        MenuItem itemDelete = menuToolbar.findItem(R.id.boto_delete);
        itemDelete.setVisible(false);
        MenuItem itemInformes = menuToolbar.findItem(R.id.boto_informes);
        itemInformes.setVisible(false);
        MenuItem itemInformesGenerals = menuToolbar.findItem(R.id.boto_informes_arrel);
        itemInformesGenerals.setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.boto_detalls:
                DadesActivitat activitat = llistaDadesActivitats.get(posicioItemLongClickat);
                Intent showInfo = new Intent(LlistaActivitatsActivity.this, InformacioActivity.class);
                showInfo.putExtra("idActivitat", activitat.getId());
                startActivity(showInfo);
                break;
            case R.id.boto_opcions:
                break;
            case R.id.boto_delete:
                // Treiem la selecció de l'Activitat en qüestió
                MenuItem itemDetalls = menuToolbar.findItem(R.id.boto_detalls);
                itemDetalls.setVisible(false);
                MenuItem itemDelete = menuToolbar.findItem(R.id.boto_delete);
                itemDelete.setVisible(false);
                MenuItem itemInformes = menuToolbar.findItem(R.id.boto_informes);
                itemInformes.setVisible(false);

                selectedView.setBackgroundColor(Color.parseColor("#EEEEEE"));

                fabPlay.setVisibility(View.GONE);

                fabSpeedDial.setVisibility(View.VISIBLE);

                longClick = false;
                posicioItemLongClickat = -1;
                break;
            case R.id.fab_alfabeticament:
                Intent inte = new Intent(LlistaActivitatsActivity.CAMBIA_ORDRE);
                inte.putExtra("sortOption", "alfabeticament");
                sendBroadcast(inte);
                break;
            case R.id.fab_recents:
                Intent inte2 = new Intent(LlistaActivitatsActivity.CAMBIA_ORDRE);
                inte2.putExtra("sortOption", "recents");
                sendBroadcast(inte2);
                break;
            case R.id.fab_tasques:
                Intent inte3 = new Intent(LlistaActivitatsActivity.CAMBIA_ORDRE);
                inte3.putExtra("sortOption", "tasques");
                sendBroadcast(inte3);
                break;
            case R.id.fab_projectes:
                Intent inte4 = new Intent(LlistaActivitatsActivity.CAMBIA_ORDRE);
                inte4.putExtra("sortOption", "projectes");
                sendBroadcast(inte4);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Missatge que apareix al intentar sortir de l'aplicació si encara hi ha tasques cronometrant-se.
     */
    @SuppressLint("validFragment")
    public class PopUpMessage extends DialogFragment {

        private int message;
        private int possitiveButton;
        private int negativeButton;

        public void setMessages(int message, int possitiveButton, int negativeButton) {
            this.message = message;
            this.possitiveButton = possitiveButton;
            this.negativeButton = negativeButton;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message)
                    .setPositiveButton(possitiveButton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            opcioEscollida = id;
                            onBackPressed();
                        }
                    })
                    .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    private boolean isTasquesCronometrant(ArrayList<DadesActivitat> dadesActivitat) {
        for(DadesActivitat activitat : dadesActivitat) {
            if (activitat.isCronometreEngegat()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Opció escollida en el PopUpMessage
     */
    private int opcioEscollida = 0;

    /**
     * Gestor de l'event de prémer la tecla 'enrera' del D-pad. El que fem es
     * anar "cap amunt" en l'arbre de tasques i projectes. Si el projecte pare
     * de les activitats que es mostren ara no és nul (n'hi ha), 'pugem' per
     * mostrar-lo a ell i les seves activitats germanes. Si no n'hi ha, paro el
     * servei, deso l'arbre (equivalent a parar totes les tasques que s'estiguin
     * cronometrant) i pleguem de la aplicació.
     */
    @Override
    public final void onBackPressed() {
        Log.i(tag, "onBackPressed");

        // Quan s'ha acceptat que es surti de l'APP encara que hi hagi Tasques running
        if (opcioEscollida == -1) {
            Log.d(tag, "parem servei");
            sendBroadcast(new Intent(LlistaActivitatsActivity.PARA_SERVEI));
            super.onBackPressed();
        }

        if (activitatPareActualEsArrel) {
            if (tasquesCronometrant) {
                PopUpMessage missatgeConfirmacio = new PopUpMessage();
                missatgeConfirmacio.setMessages(R.string.missatgeConfirmacioBack,
                        R.string.aceptar, R.string.cancelar);
                missatgeConfirmacio.show(getFragmentManager(), "missatge_back");
            } else {
                Log.d(tag, "parem servei");
                sendBroadcast(new Intent(LlistaActivitatsActivity.PARA_SERVEI));
                super.onBackPressed();
            }
        } else {
            sendBroadcast(new Intent(LlistaActivitatsActivity.PUJA_NIVELL));
            Log.d(tag, "enviat intent PUJA_NIVELL");
            sendBroadcast(new Intent(LlistaActivitatsActivity.DONAM_FILLS));
            Log.d(tag, "enviat intent DONAM_FILLS");

            // Al cambiar de nivell de l'arbre, cal reinicialtizar els atributs del longClick
            longClick = false;
            posicioItemLongClickat = -1;

            // Tornem els elements de la Activity al seu estat inicial
            fabPlay.setImageResource(R.drawable.ic_play);
            fabPlay.setVisibility(View.GONE);

            fabSpeedDial.setVisibility(View.VISIBLE);

            MenuItem itemDetalls = menuToolbar.findItem(R.id.boto_detalls);
            itemDetalls.setVisible(false);
            MenuItem itemInformes = menuToolbar.findItem(R.id.boto_informes);
            itemInformes.setVisible(false);
        }
    }

    // D'aqui en avall els mètodes que apareixen són simplement sobrecàrregues
    // de mètodes de Activity per tal que es mostri un missatge de logging i
    // d'aquesta manera puguem entendre el cicle de vida d'un objecte d'aquesta
    // classe i depurar errors de funcionament de la interfase (on posar què).

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     *
     * @param savedInstanceState
     *            objecte de classe Bundle, que no fem servir.
     */
    @Override
    public final void onSaveInstanceState(final Bundle savedInstanceState) {
        Log.i(tag, "onSaveInstanceState");
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Aquesta funció es crida després de <code>onCreate</code> quan hi ha un
     * canvi de configuració = rotar el mòbil 90 graus, passant de "portrait" a
     * apaisat o al revés.
     *
     * @param savedInstanceState
     *            Bundle que de fet no es fa servir.
     *
     * @see onConfigurationChanged
     */
    @Override
    public final void onRestoreInstanceState(final Bundle savedInstanceState) {
        Log.i(tag, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     */
    @Override
    public final void onStop() {
        Log.i(tag, "onStop");
        super.onStop();
    }

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     */
    @Override
    public final void onDestroy() {
        Log.i(tag, "onDestroy");
        super.onDestroy();
    }

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     */
    @Override
    public final void onStart() {
        Log.i(tag, "onStart");
        super.onStart();
    }

    /**
     * Mostra un missatge de log per entendre millor el cicle de vida d'una
     * Activity.
     */
    @Override
    public final void onRestart() {
        Log.i(tag, "onRestart");
        super.onRestart();
    }

    /**
     * Mostra un missatge de logging en rotar 90 graus el dispositiu (o
     * simular-ho en l'emulador). L'event <code>configChanged</code> passa quan
     * girem el dispositiu 90 graus i passem de portrait a landscape (apaisat) o
     * al revés. Això fa que les activitats siguin destruïdes (
     * <code>onDestroy</code>) i tornades a crear (<code>onCreate</code>). En
     * l'emulador del dispositiu, això es simula fent Ctrl-F11.
     *
     * @param newConfig
     *            nova configuració {@link Configuration}
     */
    @Override
    public final void onConfigurationChanged(final Configuration newConfig) {
        Log.i(tag, "onConfigurationChanged");
        if (Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, newConfig.toString());
        }
    }


}
