package com.example.joans.timetracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Adaptador per a les llistes d'Activities.
 * Permet mostrar una imatge que diferencia els tipus d'Activity de cada element (una carpeta per un
 * Project i un full per una Task).
 */
public class ActivityListAdapter extends ArrayAdapter<DadesActivitat> {

    private final Activity context;
    private final List<DadesActivitat> itemname;

    public ActivityListAdapter(Activity context, List<DadesActivitat> itemname) {
        super(context, R.layout.fila_llista_activitats, R.id.text_fila, itemname);
        this.context=context;
        this.itemname=itemname;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.fila_llista_activitats,null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.text_fila);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemname.get(posicion).toString());

        System.out.println("Posicion: " + posicion);

        if (itemname.get(posicion).isProjecte()) {
            imageView.setImageResource(R.drawable.ic_project);
        } else {
            imageView.setImageResource(R.drawable.ic_task);
        }

        return rowView;
    }


}