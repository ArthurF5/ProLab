package com.moliveiralucas.prolab.Controle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moliveiralucas.prolab.R;
import com.moliveiralucas.prolab.model.Laboratorio;

import java.util.ArrayList;

public class LaboratorioListAdapter extends ArrayAdapter<Laboratorio>{
    private static final String TAG = "LaboratorioListAdapter";
    private Context mContext;
    private Integer mResource;

    public LaboratorioListAdapter(Context context, int resource, ArrayList<Laboratorio> objects){
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Pega dados do laboratorio (Nome, Endereco)
        String laboratorio = getItem(position).getLaboratorio();
        String endereco = getItem(position).getmEndereco();

        Laboratorio lab = new Laboratorio(laboratorio, endereco);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView txtLaboratorio = convertView.findViewById(R.id.txtViewLaboratorio);
        TextView txtEndereco = convertView.findViewById(R.id.txtViewEndereco);
        TextView txtDistancia = convertView.findViewById(R.id.txtViewDistancia);

        txtLaboratorio.setText(laboratorio);
        txtEndereco.setText(endereco);

        //Calcular a distancia entre o endereco e a localização atual do usuario usando o GEOCODE
        //Setar no txtDistancia a distancia calculada
        txtDistancia.setText("");
        return convertView;
    }
}
