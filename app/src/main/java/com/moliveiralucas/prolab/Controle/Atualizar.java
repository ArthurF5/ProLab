package com.moliveiralucas.prolab.Controle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.moliveiralucas.prolab.R;


public class Atualizar extends Fragment {
    private String[] tipoCadastro = {"Selecione o assunto", "Exame", "Laboratório"};

    public Atualizar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.atualizar, container, false);
        final Spinner spinner = v.findViewById(R.id.spinnerTipo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, tipoCadastro);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout updateExame = v.findViewById(R.id.groupUpdateExame);
                LinearLayout updateLaboratorio = v.findViewById(R.id.groupUpdateLaboratorio);
                Integer update = (Integer) spinner.getSelectedItemPosition();
                switch(update){
                    case 0:
                        updateExame.setVisibility(View.GONE);
                        updateLaboratorio.setVisibility(View.GONE);
                        break;
                    case 1:
                        updateExame.setVisibility(View.VISIBLE);
                        updateLaboratorio.setVisibility(View.GONE);
                        break;
                    case 2:
                        updateExame.setVisibility(View.GONE);
                        updateLaboratorio.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;
    }

}
