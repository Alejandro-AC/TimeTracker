package com.example.joans.timetracker;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import android.support.v4.content.ContextCompat;
import android.graphics.Color;

/**
 * Adaptador per a les llistes d'Activities.
 * Permet mostrar una imatge que diferencia els tipus d'Activity de cada element (una carpeta per un
 * Project i un full per una Task).
 * Quan s'està cronometrant una Activity, aquesta mostra el seu temps total de color VERMELL.
 */
public class ActivityListAdapter extends ArrayAdapter<DadesActivitat> {

    private final Activity context;
    private final List<DadesActivitat> dadesActivitats;

    public ActivityListAdapter(Activity context, List<DadesActivitat> llistaDadesActivitats) {
        super(context, R.layout.fila_llista_activitats, R.id.text_fila, llistaDadesActivitats);
        this.context=context;

        this.dadesActivitats=llistaDadesActivitats;
    }

    @Override
    public View getView(int posicio, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.fila_llista_activitats,null,true);

        TextView txtNom = (TextView) rowView.findViewById(R.id.text_fila);
        TextView txtTemps = (TextView) rowView.findViewById(R.id.temps_fila_activitats);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtNom.setText(dadesActivitats.get(posicio).getNom());

        // Canvia el color del temps si l'Activity està sent cronometrada
        if (dadesActivitats.get(posicio).isCronometreEngegat()) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorRunning));
            txtNom.setTextColor(ContextCompat.getColor(context, R.color.colorRunning));
            txtTemps.setTextColor(ContextCompat.getColor(context, R.color.colorRunning));
        }else if
        //Canvia el color del temps si l'Activity està pausada
                (dadesActivitats.get(posicio).isCronometrePausat()){

            imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorPaused));
            txtNom.setTextColor(ContextCompat.getColor(context, R.color.colorPaused));
            txtTemps.setTextColor(ContextCompat.getColor(context, R.color.colorPaused));
            rowView.setBackgroundColor(Color.parseColor("#5da39f"));
        }

        // Manté pintat l'item si està seleccionat
        if (posicio == LlistaActivitatsActivity.posicioItemLongClickat) {
            rowView.setBackgroundColor(Color.GRAY);

        }


        txtTemps.setText(dadesActivitats.get(posicio).toStringTemps());

        // Mostra la imatge corresponent
        if (dadesActivitats.get(posicio).isProjecte()) {
            imageView.setImageResource(R.drawable.ic_project);
        } else {
            imageView.setImageResource(R.drawable.ic_task);
        }

        return rowView;
    }


}