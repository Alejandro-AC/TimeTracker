package com.example.joans.timetracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import android.support.v4.content.ContextCompat;

/**
 * Adaptador per les llistes d'Intervals.
 */
public class IntervalListAdapter extends ArrayAdapter<DadesInterval> {

    private final Activity context;
    private final List<DadesInterval> dadesIntervals;

    public IntervalListAdapter(Activity context, List<DadesInterval> llistaDadesInterval) {
        super(context, R.layout.fila_llista_intervals, llistaDadesInterval);
        this.context = context;
        this.dadesIntervals = llistaDadesInterval;
    }

    @Override
    public View getView(int posicio, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.fila_llista_intervals,null,true);

        TextView txtInicial = (TextView) rowView.findViewById(R.id.inicial_fila);
        TextView txtFinal = (TextView) rowView.findViewById(R.id.final_fila);
        TextView txtTemps = (TextView) rowView.findViewById(R.id.temps_fila_intervals);

        txtInicial.setText(dadesIntervals.get(posicio).toStringInicial());
        txtFinal.setText(dadesIntervals.get(posicio).toStringFinal());
        txtTemps.setText(dadesIntervals.get(posicio).toStringTemps());

        if (dadesIntervals.get(posicio).getPare().isCronometreEngegat() && posicio == 0) {
            txtInicial.setTextColor(ContextCompat.getColor(context, R.color.colorRunning));
            txtFinal.setTextColor(ContextCompat.getColor(context, R.color.colorRunning));
            txtTemps.setTextColor(ContextCompat.getColor(context, R.color.colorRunning));
        }

        return rowView;
    }
}
