package com.example.joans.timetracker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import nucli.Activitat;
import nucli.Interval;
import nucli.Periode;
import nucli.Projecte;
/*
import nucli.GeneradorTicks;
import nucli.Rellotge;
Veure comentari de la classe Actualitzador
*/
import nucli.Rellotge_Actualitzable;
import nucli.Tasca;
import nucli.Informacio;


/**
 * Llegeix, manté en memòria i desa l'arbre de projectes, tasques i intervals, i
 * en gestiona tant la navegació per ell com el cronometrat de tasques, quan
 * així es sol·licita des de les classes que capturen els events d'interacció
 * amb l'usuari. A més, envia a les Activity que ho demanen la llista de les
 * dades d'activitat o d'interval que s'han de mostrar.
 *
 * @author joans
 * @version 26 gener 2012
 */
public class GestorArbreActivitats extends Service implements Actualitzable {

    /**
     * Nom de la classe per fer aparèixer als missatges de logging del LogCat.
     *
     * @see Log
     */
    private final String tag = this.getClass().getSimpleName();

    /**
     * El node arrel de l'arbre de projectes, tasques i intervals. Aquest node
     * és doncs especial perquè no existeix per a l'usuari, només serveix per
     * contenir la llista d'activitats del primer nivell que si veu l'usuari.
     */
    private Projecte arrel;

    /**
     * Observable que actualitza les tasques que estan sent cronometrades en
     * invocar el seu mètode update().
     */
    private Rellotge_Actualitzable rellotge;
    /*
    private Rellotge rellotge;
    Veure comentari de la classe Actualitzador
    */

    /**
     * Arxiu on es desa tot l'arbre de projectes, tasques i intervals.
     */
    private final String nomArxiu = "timetracker2.dat";

    /**
     * Sort option
     */
    private String sortOptionIntervals = "recents";

    public void setSortOptionIntervals(String sortOption1){
        this.sortOptionIntervals = sortOption1;
    }

    public String getSortOptionIntervals(){
        return this.sortOptionIntervals;
    }

    /**
     * Sort option
     */
    private String sortOption = "alfabeticament";

    public void setSortOption(String sortOption1){
        this.sortOption = sortOption1;
    }

    public String getSortOption(){
        return this.sortOption;
    }

    /**
     * En navegar per l'arbre de projectes i tasques, és el projecte pare de la
     * llista d'activitats mostrada per últim cop gràcies a la classe
     * <code>LlistaActivitatsActivity</code>, o bé la tasca pare dels intervals
     * mostrats a la activitat <code>LlistaIntervalsActivity</code>. El seu
     * valor inicial és el projecte arrel de tot l'abre, {@link #arrel}
     */
    private Activitat activitatPareActual;

    /**
     * Retorna el nom de l'Activitat pare actual.
     */
    public static final String TE_NOM = "Te_nom";


    /**
     * Afegeix un nou Projecte a la llista d'Activitats del nivell actual.
     */
    public static final String AFEGIR_PROJECTE = "Afegir_projecte";

    /**
     * Afegeix una nova Tasca a la llista d'Activitats del nivell actual.
     */
    public static final String AFEGIR_TASCA = "Afegir_tasca";

    public static final String EDITAR_TASCA = "Editar_tasca";

    public static final String EDITAR_PROJECTE = "Editar_projecte";

    /**
     * El servei consisteix en processar, com en el cas d'engegar i parar
     * cronometre de tasca, o be processar i retornar unes dades, com en la
     * resta, ja que cal actualitzar el la activitat actual (tasca o projecte) i
     * enviar la llista d'activitats filles o intervals, segons el cas. Per tal
     * de retornar dades, dissenyem aquest intent.
     */
    public static final String TE_FILLS = "Te_fills";

    /**
     * Usada a {@link onCreate} i {@link carregaArbreActivitats} per crear un o
     * altre tipus d'arbre de projectes, tasques i intervals.
     */
    private final int llegirArbreArxiu = 0;

    /**
     * Usada a {@link onCreate} i {@link carregaArbreActivitats} per crear un o
     * altre tipus d'arbre de projectes, tasques i intervals.
     */
    private final int ferArbrePetitBuit = 1;

    /**
     * Usada a {@link onCreate} i {@link carregaArbreActivitats} per crear un o
     * altre tipus d'arbre de projectes, tasques i intervals.
     */
    private final int ferArbreGran = 2;

    /**
     * Rep els "intents" que envien <code>LlistaActivitatsActivity</code> i
     * <code>LlistaIntervalsActivity</code>. El receptor els rep tots (no hi ha
     * cap filtre) i els diferència per la seva "action". Veure {@link Receptor}
     * per saber quins són.
     *
     */
    private Receptor receptor;

    /**
     * Handler que ens permet actualitzar la interfase d'usuari quan hi ha
     * alguna tasca que s'està cronometrant. Concretament, actualitza les
     * Activity que mostren activitats i intervals. S'engega i es para a
     * {@link Receptor#onReceive}.
     */
    private Actualitzador actualitzadorIU;

    /**
     * Llista de tasques que estan sent cronometrades en cada moment, que
     * mantenim per tal que en parar el servei les puguem deixar de cronometrar
     * i fer que no es desin com si ho fossin i llavors tenir dades
     * inconsistents en tornar a carregar l'arbre.
     */
    private ArrayList<Tasca> tasquesCronometrantse = new ArrayList<Tasca>();

    /**
     * Període de temps en segons dada quan s'actualitza la interfase d'usuari.
     * Es un paràmetre del constructor de {@link #actualitzadorIU}.
     */
    private final int periodeRefrescIU = 1000;

    /**
     * Llegeix l'abre de projectes, tasques i intervals desat en l'arxiu. Aquest
     * arxiu és propi i privat de la aplicació.
     *
     * @param opcio
     *            permet escollir entre 3 tipus d'arbres :
     *            <p>
     *            <ol>
     *            <li>Llegir l'arbre d'arxiu, si existeix, que vol dir si prové
     *            de desar l'arbre existent en una crida anterior al mètode
     *            <code>GestorArbreActivitats#desaArbre</code>. Si no existeix,
     *            fa un arbre que només te el node arrel.</li>
     *            <li>Fer un arbre petit, amb 3 tasques i 2 projectes a més de
     *            l'arrel, i sense intervals.</li>
     *            <li>Fer un arbre gran amb projectes, tasques i intervals, amb
     *            dades aleatòries però consistents. Cada vegada es fa el mateix
     *            arbre, però.</li>
     *            </ol>
     *
     * @see desaArbreActivitats
     */
    public final void carregaArbreActivitats(final int opcio) {
        // TODO : Això fora millor fer-ho en un altre thread per evitar que la
        // lectura d'un arxiu molt gran pugui trigui massa i provoqui que la
        // aplicació perdi responsiveness o pitjor, que aparegui el diàleg
        // ANR = application is not responding, demanant si volem forçar el
        // tancament de la aplicació o esperar.
        // La solució deu ser fer servir una AsyncTask, tal com s'explica a
        // l'article "Painless Threading" de la documentació del Android SDK.
        // Veure'l a la versió local o a
        // developer.android.com/resources/articles/painless-threading.html

        switch (opcio) {
            case llegirArbreArxiu:
                Log.d("TAG", "carrega arbre d'activitats");
                try {
                    FileInputStream fips = openFileInput(nomArxiu);
                    ObjectInputStream in = new ObjectInputStream(fips);
                    arrel = (Projecte) in.readObject();
                    in.close();
                    Log.d(tag, "Arbre llegit d'arxiu");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    Log.d(tag, "L'arxiu no es troba, fem un arbre buit");
                    arrel = new Projecte("ARREL", "arrel de projectes", null);
                    // e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ferArbrePetitBuit:
                // Crea un arbre de "mostra" petit, sense intervals. Per tant, cap
                // tasca ni projecte tenen data inicial, final ni durada.
                arrel = new Projecte("ARREL", "arrel de projectes", null);
                Projecte proj1 = new Projecte("Enginyeria del software 2",
                        "primer projecte", arrel);
                new Projecte("Visió artificial", "segon projecte", arrel);
                new Tasca("Anar a buscar carnet biblio", "tercera tasca", arrel);
                new Tasca("Instal·lar Eclipse", "primera tasca", proj1);
                new Tasca("Estudiar patrons", "segona tasca", proj1);
                Log.d(tag, "Arbre de mostra petit i sense intervals creat");
                break;
            case ferArbreGran:
                // Crea un arbre depenent dels paràmetres passats, i que pot ser
                // gran doncs.
                ArbreAleatori rt = new ArbreAleatori();
                final int nNivells = 3;
                final int nMaximActivitatsFilles = 10;
                final int nMaximIntervalsFills = 20;
                final double ratio = 0.5;
                // El constructor de Date demana any amb 0=1900, mes entre 0 i 11 i
                // dia entre 1 i 28...31.
                // TODO : canviar Date per GregorianCalendar per que és deprecated i
                // te un us més natural.
                final int anyBaseDate = 1900;
                final int anyInici = 2009;
                final int mesInici = 0; // gener, segons constructor de Date
                final int diaInici = 1;
                // 1 de gener de 2009
                final Date dataInici = new Date(anyInici - anyBaseDate, mesInici,
                        diaInici);
                final int anyFi = 2011;
                final int mesFi = 11; // desembre, segons constructor de Date
                final int diaFi = 31;
                // 31 desembre 2011
                final Date dataFi = new Date(anyFi - anyBaseDate, mesFi, diaFi);
                final long duradaMinimaInterval = 30; // en segons, mig minut
                final long duradaMaximaInterval = 48 * 3600; // en segons, 48 hores
                arrel = rt.nextArbre(nNivells, nMaximActivitatsFilles,
                        nMaximIntervalsFills, ratio, dataInici, dataFi,
                        duradaMinimaInterval, duradaMaximaInterval);
                Log.d(tag, "Arbre de mostra aleatori creat");
                break;
            default:
                // no hi ha més opcions possibles
                assert false : "opció de creació de l'arbre no existent";
        }
    }

    /**
     * Desa l'arbre de projectes, tasques i intervals en un arxiu propi de la
     * aplicació. El mecanisme emprat per desar és serialitzar l'arrel.
     */
    // TODO : Això fora millor fer-ho en un altre thread per evitar que
    // l'escriptura d'un arxiu molt gran pugui trigui massa i provoqui que la
    // aplicació perdi "responsiveness". veure el comentari ToDo de
    // carregaArbreActivitats.
    // TODO : si es demanés de desar l'arbre mentre alguna tasca s'estés
    // cronometrant, quedaria registrat així ? Seria un problema després,
    // quan es llegís l'arbre, per que aquest tasca no es podria cronometrar
    // altre cop. Comprovar-ho.
    public final void desaArbreActivitats() {
        Log.d("TAG", "desa arbre activitats");
        try {
            FileOutputStream fops = openFileOutput(nomArxiu,
                    Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fops);
            out.writeObject(arrel);
            out.close();
        } catch (FileNotFoundException e) {
            Log.d(tag, "L'arxiu no es troba");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * El nostre servei no està lligat a cap activitat per tal de compartir
     * dades. No és una bona opció per que el servei és destruït quan deixa
     * d'estar lligat a cap Activity. Tot i així cal sobrecarregar aquest mètode
     * per que retorni null.
     *
     * @param arg0
     *            argument no utilitzat
     * @return null
     */
    @Override
    public final IBinder onBind(final Intent arg0) {
        return null;
    }

    /**
     * Invocada quan es crea el servei per primer cop, fa una sèrie
     * d'inicialitzacions. Són les següents inicialitzacions
     * <ul>
     * <li>estableix els tipus d'intents als quals dona resposta (veure
     * {@link Receptor})</li>
     * <li>crea el "handler" {@link actualitzadorIU} que actualitzarà la
     * interfase d'usuari a mesura que vagi passant el temps</li>
     * <li>crea i posa en marxa el rellotge per cronometrar tasques</li>
     * </ul>
     */
    public final void onCreate() {
        Log.d(tag, "onCreate");

        IntentFilter filter;
        filter = new IntentFilter();
        filter.addAction(LlistaActivitatsActivity.DONAM_FILLS);
        filter.addAction(LlistaActivitatsActivity.PUJA_NIVELL);
        filter.addAction(LlistaActivitatsActivity.BAIXA_NIVELL);
        filter.addAction(LlistaActivitatsActivity.STOP_ALL);
        filter.addAction(LlistaActivitatsActivity.PAUSE_ALL);
        filter.addAction(LlistaActivitatsActivity.UNPAUSE_ALL);
        filter.addAction(LlistaActivitatsActivity.CAMBIA_ORDRE);
        filter.addAction(LlistaIntervalsActivity.CAMBIA_ORDRE_INTERVALS);
        filter.addAction(LlistaActivitatsActivity.ENGEGA_CRONOMETRE);
        filter.addAction(LlistaActivitatsActivity.PARA_CRONOMETRE);
        filter.addAction(LlistaActivitatsActivity.DESA_ARBRE);
        filter.addAction(LlistaActivitatsActivity.PARA_SERVEI);
        filter.addAction(LlistaIntervalsActivity.PUJA_NIVELL);
        filter.addAction(LlistaActivitatsActivity.DONAM_NOM);
        filter.addAction(LlistaActivitatsActivity.TASQUES_RUNNING);
        filter.addAction(NouProjecte.AFEGIR_PROJECTE);
        filter.addAction(NovaTasca.AFEGIR_TASCA);
        filter.addAction(NovaTasca.EDITAR_TASCA);
        filter.addAction(NouProjecte.EDITAR_PROJECTE);
        receptor = new Receptor();
        registerReceiver(receptor, filter);

        actualitzadorIU = new Actualitzador(this, periodeRefrescIU,
                "gestor_arbre_activitats");
        // Escollir la opció desitjada d'entre ferArbreGran, llegirArbreArxiu i
        // ferArbrePetitBuit. Podríem primer fer l'arbre gran i després, quan
        // ja s'hagi desat, escollir la opció de llegir d'arxiu.

        final int opcio = ferArbreGran;
        carregaArbreActivitats(opcio);

        if (arrel == null) {
            arrel = new Projecte("ARREL", "arrel de projectes", null);
        }

        activitatPareActual = arrel;

        Log.d(tag, "l'arrel te " + arrel.getActivitats().size() + " fills");

        rellotge = Rellotge_Actualitzable.Instance();
        rellotge.engega();

        /*
        rellotge = Rellotge.Instance();
        GeneradorTicks genTicks = new GeneradorTicks(rellotge);
        Veure comentari de la classe Actualitzador
        */

    }

    /**
     * En engegar el servei per primer cop, o després de ser parat i tornat a
     * engegar, enviem les dades de la llista de fills de la activitat actual.
     *
     * @param intent
     *               veure la documentació online
     * @param flags
     *               veure la documentació online
     * @param startId
     *               veure la documentació online
     * @return
     *               veure la documentació online
     */
    @Override
    public final int onStartCommand(final Intent intent, final int flags,
                                    final int startId) {
        if ((flags & Service.START_FLAG_RETRY) == 0) {
            // es un restart, després d'acabar de manera anormal
            Log.d(tag, "onStartCommand repetit");
        } else {
            Log.d(tag, "onStartCommand per primer cop");
        }
        enviaFills();
        // De l'exemple de la documentació de referència de la classe
        // Service de la web android developer: "We want this service
        // to continue running until it is explicitly stopped, so return
        // sticky".
        return Service.START_STICKY;
    }

    /**
     * Conté el mètode <code>onReceive</code> on es dona servei, o sigui, es
     * gestionen, les peticions provinents de les classes Activity que capturen
     * la interacció de l'usuari.
     * <p>
     * Concretament, de {@link LlistaActivitatsActivity} rebem les peticions de
     * <ul>
     * <li><code>ENGEGA_CRONOMETRE</code> i <code>PARA_CRONOMETRE</code> d'una
     * tasca clicada, la qual s'inidica com el número d'ordre en la llista
     * d'activitats mostrades. Si la tasca no està sent ja cronometrada i es
     * demana, se li engega el cronòmetre. Si ho està sent i es demana, se li
     * para.</li>
     * <li><code>PUJA_NIVELL</code> i <code>BAIXA_NIVELL</code>, que fan que
     * s'actualitzi la activitat pare actual. Es responsabilitat del
     * sol·licitant llavors demanar que se li enviïn les noves dades a mostrar.
     * </li>
     * <li><code>DONAM_FILLS</code> demana que es construeixi una llista de les
     * dades dels fills de la activitat pare actual, ja sigui projecte o tasca.
     * Ho farà el mètode {@link GestorArbreActivitats#enviaFills}, el qual
     * construeix aquesta llista i la posa com un "extra" a un Intent que té una
     * acció igual a TE_FILLS.</li>
     * <li><code>DESA_ARBRE</code> demana que s'escrigui l'arbre actual a
     * l'arxiu per defecte, privat de la aplicació, el nom del qual és af
     * {@link GestorArbreActivitats#nomArxiu}.</li>
     * <li>PARA_SERVEI</li> demana el que faríem si en Android es pogués
     * "sortir" de la aplicació: parar els handlers actualitzadors de la
     * interfase i el rellotge, parar el receptor d'intents, parar el cronòmetre
     * de les tasques que ho estan essent, i desar l'arbre a arxiu. Tot això ho
     * fa <code>paraServei</code>, que a més a més, fa un <code>stopSelf</code>
     * d'aquest servei.
     * </ul>
     * I de {@link LlistaintervalsActivity} rebem <code>PUJA_NIVELL</code> i
     * <code>DONAM_FILLS</code> que tenen el mateix tractament.
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
         * Per comptes de fer una classe receptora per a cada tipus d'accio o
         * intent, els tracto tots aquí mateix, distingit-los per la seva
         * "action".
         *
         * @param context
         *            sol·licitant
         * @param intent
         *            petició consistent en una acció (string) per identificar
         *            el tipus de petició, i paràmetres que l'acompanyen en
         *            forma d'extres de l'intent.
         */
        @Override
        public final void onReceive(final Context context,
                                    final Intent intent) {
            Log.d(tag, "onReceive");
            String accio = intent.getAction();
            Log.d(tag, "accio = " + accio);
            if ((accio.equals(LlistaActivitatsActivity.ENGEGA_CRONOMETRE))
                    || (accio.equals(LlistaActivitatsActivity.PARA_CRONOMETRE))) {
                int posicio = intent.getIntExtra("posicio", -1);
                //Tasca tascaClicada = (Tasca) ((Projecte) activitatPareActual).getActivitats().toArray()[posicio];

                List<Activitat> activityList = new ArrayList<Activitat>(((Projecte) activitatPareActual).getActivitats());

                sort(getSortOption(), activityList);
                //Collection<Activitat> childsActivitatPareActual3 = activityList;

                Tasca tascaClicada = (Tasca) activityList.toArray()[posicio];


                if (accio.equals(LlistaActivitatsActivity.ENGEGA_CRONOMETRE)) {
                    if (!tascaClicada.isCronometreEngegat()) {
                        // rellotge.engega();
                        tascaClicada.engegaCronometre(rellotge);
                        Log.d(tag, "engego cronometre de "
                                + tascaClicada.getNom());
                        tasquesCronometrantse.add(tascaClicada);
                        actualitzadorIU.engega(); // si ja ho esta no fa res
                    } else {
                        Log.w(tag, "intentat cronometrar la tasca "
                                + tascaClicada.getNom()
                                + " que ja ho esta sent");
                    }
                }
                if (accio.equals(LlistaActivitatsActivity.PARA_CRONOMETRE)) {
                    if (tascaClicada.isCronometreEngegat()) {
                        tascaClicada.paraCronometre(rellotge);
                        tasquesCronometrantse.remove(tascaClicada);
                        if (tasquesCronometrantse.size() == 0) {
                            enviaFills();
                            // es per dir a la IU (LlistaActivitatsActivity) que
                            // aquesta tasca ara ja te el cronometre parat.
                            // Sinó, si aquesta era la única tasca que es
                            // cronometrava, si directament paro
                            // l'actualitzador de la IU, quedara com que
                            // encara esta engegat per aquesta tasca i no es
                            // podrà tornar a engegar! I ara si que ja podem
                            // fer :
                            actualitzadorIU.para();
                        }
                    } else {
                        Log.w(tag, "intentat parar cronometrae de la tasca "
                                + tascaClicada.getNom()
                                + " que ja el te parat");
                    }
                }
                Log.d(tag, "Hi ha " + tasquesCronometrantse.size()
                        + " tasques cronometrant-se");
            } else if (accio.equals(LlistaActivitatsActivity.DESA_ARBRE)) {
                desaArbreActivitats();
            } else if(accio.equals(NovaTasca.AFEGIR_TASCA)){
                Date d = new Date();
                String nom = intent.getStringExtra("nomTasca");
                String descripcio = intent.getStringExtra("descripcioTasca");
                Tasca task1 = new Tasca(nom, descripcio, (Projecte) activitatPareActual);
                Periode p = new Periode(d,d,0);
                task1.setPeriode(p);
                task1.setDataFinal(d);
                task1.setDataInicial(d);
                task1.setDurada(0);
            } else if (accio.equals(NouProjecte.AFEGIR_PROJECTE)){
                Date d = new Date();
                Log.d(tag, "afegint projecte");
                String nom = intent.getStringExtra("nomProjecte");
                String descripcio = intent.getStringExtra("descripcioProjecte");
                Projecte proj1 = new Projecte(nom, descripcio, (Projecte) activitatPareActual);
                Periode p = new Periode(d,d,0);
                proj1.setPeriode(p);
                proj1.setDataFinal(d);
                proj1.setDataInicial(d);
                proj1.setDurada(0);
            } else if (accio.equals(NovaTasca.EDITAR_TASCA)){
                String nom = intent.getStringExtra("nomTasca");
                String descripcio = intent.getStringExtra("descripcioTasca");
                int id = intent.getIntExtra("id",0);
                Log.d(tag,"DENTRO DEL GESTOR DE ARBOLES");
                Log.d(tag,"NOM" + nom);
                Log.d(tag,"DESCRIPCIO" + descripcio);
                if (activitatPareActual.getClass().getName().endsWith("Projecte")) {
                    for (Activitat act : ((Projecte) activitatPareActual).getActivitats()) {
                        System.out.println(act.getNom());
                        if(act.getClass().getName().endsWith("Tasca")) {
                            if (act.getInfo().getId() == id) {
                                Log.d(tag, "DENTRO HE ENCONTRADO ALGO");
                                Informacio info = new Informacio(nom, descripcio);
                                act.setInfo(info);
                            }
                        }
                    }
                }
            }else if (accio.equals(NouProjecte.EDITAR_PROJECTE)){

                String nom = intent.getStringExtra("nomProjecte");
                String descripcio = intent.getStringExtra("descripcioProjecte");
                int id = intent.getIntExtra("id",0);
                Log.d(tag,"DENTRO DEL GESTOR DE ARBOLES");
                Log.d(tag,"NOM" + nom);
                Log.d(tag,"DESCRIPCIO" + descripcio);
                if (activitatPareActual.getClass().getName().endsWith("Projecte")) {
                    for (Activitat act : ((Projecte) activitatPareActual).getActivitats()) {
                        System.out.println(act.getNom());
                        if(act.getClass().getName().endsWith("Projecte")) {
                            if (act.getInfo().getId() == id) {
                                Log.d(tag, "DENTRO HE ENCONTRADO ALGO");
                                Informacio info = new Informacio(nom, descripcio);
                                act.setInfo(info);
                            }
                        }
                    }
                }


            }else if (accio.equals(LlistaActivitatsActivity.DONAM_FILLS)
                    || (accio.equals(LlistaIntervalsActivity.DONAM_FILLS))) {
                enviaFills();
            } else if (accio.equals(LlistaActivitatsActivity.PUJA_NIVELL)
                    || (accio.equals(LlistaIntervalsActivity.PUJA_NIVELL))) {
                activitatPareActual = activitatPareActual.getProjectePare();
            } else if (accio.equals(LlistaActivitatsActivity.BAIXA_NIVELL)) {
                // Anem a una les activitats filles => actualitzem
                // l'activitat actual i enviem la llista d'activitats filles si
                // és un projecte, o els intervals si és una tasca.
                int posicio = intent.getIntExtra("posicio", 0);
                // El pare d'una activitat clicada nomes pot ser un projecte
                // per que els intervals no son clicables (no gestionem aquest
                // event). Ara, la activitat clicada tant pot ser un projecte
                // com una tasca.


                //Activitat activitatClicada = (Activitat) ((Projecte) activitatPareActual).getActivitats().toArray()[posicio];

                List<Activitat> activityList = new ArrayList<Activitat>(((Projecte) activitatPareActual).getActivitats());

                sort(getSortOption(), activityList);
                //Collection<Activitat> childsActivitatPareActual3 = activityList;
                Activitat activitatClicada = (Activitat) activityList.toArray()[posicio];

                activitatPareActual = activitatClicada;

            } else if (accio.equals(LlistaActivitatsActivity.CAMBIA_ORDRE)) {
                Log.d(tag, "rebut intent CAMBIA_ORDRE");
                String sortOption = intent.getStringExtra("sortOption");
                setSortOption(sortOption);
                Log.d(tag, "procedim a actualitzar, sortOption rebut:"+sortOption);
                actualitza();

            }else if (accio.equals(LlistaIntervalsActivity.CAMBIA_ORDRE_INTERVALS)) {
                Log.d(tag, "rebut intent CAMBIA_ORDRE_INTERVALS");
                String sortOption = intent.getStringExtra("sortOption");
                setSortOptionIntervals(sortOption);
                Log.d(tag, "procedim a actualitzar, sortOptionIntervals rebut:"+sortOption);
                actualitza();

            }else if (accio.equals(LlistaActivitatsActivity.STOP_ALL)) {
                Log.d(tag, "rebut intent STOP_ALL");
                paraCronometreDeTasques();
                tasquesCronometrantse.clear() ;
                actualitza();

            }else if (accio.equals(LlistaActivitatsActivity.PAUSE_ALL)) {
                Log.d(tag, "rebut intent PAUSE_ALL");
                pausaCronometreDeTasques();
                actualitza();

            }else if (accio.equals(LlistaActivitatsActivity.UNPAUSE_ALL)) {
                Log.d(tag, "rebut intent UNPAUSE_ALL");
                unpauseCronometreDeTasques();
                actualitza();

            } else if (accio.equals(LlistaActivitatsActivity.PARA_SERVEI)) {
                paraServei();
            } else if (accio.equals(LlistaActivitatsActivity.DONAM_NOM)) {
                Log.d(tag, "rebuda intent DONAM_NOM");
                enviaNomActivitatPareActual();
            } else {
                Log.d(tag, "accio desconeguda!");
            }
            Log.d(tag, "final de onReceive");
        }
    }

    /**
     * Construeixi una llista de les dades dels fills de la activitat pare
     * actual, ja sigui projecte o tasca, per tal de ser mostrades (totes o
     * algunes d'aquestes dades) a la interfase d'usuari. Aquesta llista es posa
     * com a "extra" serialitzable d'un intent de nom TE_FILLS, del qual se'n fa
     * "broadcast".
     */
    private void enviaFills() {
        Intent resposta = new Intent(GestorArbreActivitats.TE_FILLS);
        resposta.putExtra("activitat_pare_actual_es_arrel",
                (activitatPareActual == arrel));
        if (activitatPareActual.getClass().getName().endsWith("Projecte")) {
            ArrayList<DadesActivitat> llistaDadesAct =
                    new ArrayList<DadesActivitat>();



            List<Activitat> activityList = new ArrayList<Activitat>(((Projecte) activitatPareActual).getActivitats());

            sort(getSortOption(), activityList);
            //Collection<Activitat> childsActivitatPareActual3 = activityList;

            for (Activitat act : activityList) {
                llistaDadesAct.add(new DadesActivitat(act));
            }


            resposta.putExtra("llista_dades_activitats", llistaDadesAct);
        } else { // es tasca
            ArrayList<DadesInterval> llistaDadesInter =
                    new ArrayList<DadesInterval>();



            List<Interval> intervalsList = new ArrayList<Interval> (((Tasca) activitatPareActual)
                    .getIntervals());

            sortIntervals(getSortOptionIntervals(), intervalsList);
            //Collection<Activitat> childsActivitatPareActual3 = activityList;

            for (Interval inter : intervalsList) {
                llistaDadesInter.add(new DadesInterval(inter));

            }


            resposta.putExtra("llista_dades_intervals", llistaDadesInter);
        }
        sendBroadcast(resposta);
        Log.d(tag, "enviat intent TE_FILLS d'activitat "
                + activitatPareActual.getClass().getName());
    }

    /**
     * Parar els handlers actualitzadors de la interfase i el rellotge, parar el
     * receptor d'intents, parar el cronòmetre de les tasques que ho estan
     * essent, desar l'arbre a arxiu i per últim fa un <code>stopSelf</code>
     * d'aquest servei, amb la qual cosa és semblant a tancar la aplicació.
     */
    private void paraServei() {
        actualitzadorIU.para();
        rellotge.para();
        // el garbage collector ja els el·liminarà quan sigui, després de veure
        // que no es "reachable", com a conseqüència de que el servei és
        // eliminat també (espero).
        unregisterReceiver(receptor);
        // Això cal fer-ho per evitar un error en fer el darrer 'back'
        paraCronometreDeTasques();
        // Cal parar totes les tasques que s'estiguin cronometrant, per tal que
        // no es desin a l'arxiu com que si que ho estan sent i després, en
        // llegir
        // apareguin com cronometrant-se i generin problemes.
        desaArbreActivitats();
        stopSelf();
        Log.d(tag, "servei desinstalat");
    }

    /**
     * Mètode de "forwarding" de <code>enviaFills</code> (per actualitzar
     * la interfase d'usuari) però només quan aquesta pot haver canviat,
     * és a dir, quan hi ha alguna tasca que s'està cronometrant.
     */
    public final void actualitza() {
        Log.d(tag, "entro a actualitza de GestorArbreActivitats");
        if (tasquesCronometrantse.size() > 0) {
            enviaFills();
        }
        enviaFills();
    }

    /**
     * Para el cronòmetre de totes les tasques que ho estiguin sent,
     * al nivell que sigui de l'arbre.
     */
    private void paraCronometreDeTasques() {
        for (Tasca t : tasquesCronometrantse) {
            t.paraCronometre(rellotge);
            t.setCronometreNoPausat();
        }
    }


    /**
     * Pausa el cronòmetre de totes les tasques que ho estiguin sent,
     * al nivell que sigui de l'arbre.
     */
    private void pausaCronometreDeTasques() {
        for (Tasca t : tasquesCronometrantse) {
            t.paraCronometre(rellotge);
            t.setCronometrePausat();
        }
    }


    /**
     * Unpause el cronòmetre de totes les tasques que son pausades,
     * al nivell que sigui de l'arbre.
     */
    private void unpauseCronometreDeTasques() {
        for (Tasca t : tasquesCronometrantse) {
            t.engegaCronometre(rellotge);
        }
    }

    private void enviaNomActivitatPareActual() {
        Log.d(tag, "enviant nom pare actual");
        Intent resposta = new Intent(GestorArbreActivitats.TE_NOM);
        resposta.putExtra("nom_activitat_pare_actual", getNomActivitatPareActual());
        sendBroadcast(resposta);

        Log.d(tag, "enviat intent TE_NOM  "
                + getNomActivitatPareActual());
    }

    private String getNomActivitatPareActual() {
        return activitatPareActual.getNom();
    }



    public void sort(final String field, List<Activitat> activitats) {
        Collections.sort(activitats, new Comparator<Activitat>() {
            @Override
            public int compare(Activitat o1, Activitat o2) {
                if(field.equals("alfabeticament")) {
                    return o1.getNom().compareTo(o2.getNom());

                } if(field.equals("recents")) {
                    if (o1.getDataFinal().after(o2.getDataFinal()) ) {
                        Log.i(tag, o1.getDataFinal()+" after "+o2.getDataFinal());
                        return -1;
                    }else if (o1.getDataFinal().before(o2.getDataFinal()) ) {
                        return 1;
                    }else{
                        return 0;
                    }

                } if(field.equals("tasques")) {
                    boolean b1 = o1.getClass().getName().endsWith("Tasca");
                    boolean b2 = o2.getClass().getName().endsWith("Tasca");
                    return (b1 != b2) ? (b1) ? -1 : 1 : 0;

                } else if(field.equals("projectes")) {
                    boolean b1 = o1.getClass().getName().endsWith("Projecte");
                    boolean b2 = o2.getClass().getName().endsWith("Projecte");
                    return (b1 != b2) ? (b1) ? -1 : 1 : 0;
                }

                return -1;
            }
        });
    }

    public void sortIntervals(final String field, List<Interval> intervals) {
        Collections.sort(intervals, new Comparator<Interval>() {
            @Override
            public int compare(Interval o1, Interval o2) {

                 if(field.equals("recents")) {
                    if (o1.getDataFinal().after(o2.getDataFinal()) ) {
                        return 1;
                    }else if (o1.getDataFinal().before(o2.getDataFinal()) ) {
                        return -1;
                    }else{
                        return 0;
                    }

                }

                return -1;
            }
        });
    }


}
