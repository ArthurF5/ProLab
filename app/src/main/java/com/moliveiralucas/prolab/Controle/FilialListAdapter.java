package com.moliveiralucas.prolab.Controle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moliveiralucas.prolab.R;
import com.moliveiralucas.prolab.model.Filial;

import java.util.ArrayList;

public class FilialListAdapter extends ArrayAdapter<Filial> {
    private static final String TAG = "FilialListAdapter";
    private Context mContext;
    private Integer mResource;

    public FilialListAdapter(Context context, int resource, ArrayList<Filial> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String laboratorio = getItem(position).getmLab();
        String endereco = getItem(position).getmEnd();
        Float distancia = getItem(position).getmDistancia();

        Filial filial = new Filial();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView txtLaboratorio = convertView.findViewById(R.id.txtViewLaboratorio);
        TextView txtEndereco = convertView.findViewById(R.id.txtViewEndereco);
        TextView txtDistancia = convertView.findViewById(R.id.txtViewDistancia);

        txtLaboratorio.setText(laboratorio);
        txtEndereco.setText(endereco);
        String unidade;
        if (distancia > 1000) {
            unidade = " km";
        } else {
            unidade = " mt";
        }
        txtDistancia.setText(String.format("%.2f ",distancia)+ unidade);
        return convertView;
    }

}
