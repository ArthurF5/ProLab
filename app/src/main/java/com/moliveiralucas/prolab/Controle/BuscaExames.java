package com.moliveiralucas.prolab.Controle;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.moliveiralucas.prolab.R;

public class BuscaExames extends Fragment {

    private EditText etExame;
    private Spinner spinnerCidade;
    private Button btnBuscar;
    private Button btnMostrarMapa;
    private ListView lvLaboratorios;

    DataPassListener mCallback;

    public interface DataPassListener{
        public void passAddress(String address);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (DataPassListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" precisa implementar BuscaExames.java");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.busca_exames,container,false);
        etExame = rootView.findViewById(R.id.searchExameBox);

        spinnerCidade = rootView.findViewById(R.id.spinnerCidade);

        lvLaboratorios = rootView.findViewById(R.id.listLaboratorio);

        btnBuscar = rootView.findViewById(R.id.btnBuscarLab);
        btnBuscar.setOnClickListener(buscarLaboratorios());

        btnMostrarMapa = rootView.findViewById(R.id.btnMostrarNoMapa);
        btnMostrarMapa.setOnClickListener(exibirNoMapa());
        return rootView;
    }

    private View.OnClickListener exibirNoMapa() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.passAddress("Goiânia - GO");
            }
        };
    }

    private View.OnClickListener buscarLaboratorios() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMostrarMapa.setEnabled(true);
            }
        };
    }
}
