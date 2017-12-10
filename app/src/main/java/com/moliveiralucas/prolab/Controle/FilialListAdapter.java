package com.moliveiralucas.prolab.Controle;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moliveiralucas.prolab.Manifest;
import com.moliveiralucas.prolab.R;
import com.moliveiralucas.prolab.model.Filial;

import java.util.ArrayList;
import java.util.jar.Pack200;

/**
 * Created by moliveiralucas on 10/12/17.
 */

public class FilialListAdapter extends ArrayAdapter<Filial> {
    private static final String TAG = "FilialListAdapter";
    private Context mContext;
    private Integer mResource;

    public FilialListAdapter(Context context, int resource, ArrayList<Filial> objects){
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String laboratorio = getItem(position).getmLab();
        String endereco = getItem(position).getmEnd();
        Double distancia = getItem(position).getmDistancia();

        Filial filial = new Filial(laboratorio, endereco, distancia);

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
        return super.getView(position, convertView, parent);
    }
    private void pedirPermissoes(){
        if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

        }
    }
}
