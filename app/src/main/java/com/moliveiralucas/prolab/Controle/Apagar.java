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

import com.google.android.gms.vision.text.Line;
import com.moliveiralucas.prolab.R;

public class Apagar extends Fragment {
    private String[] tipoCadastro = {"Selecione o assunto", "Exame", "Laboratório", "Exame de Laboratório"};

    public Apagar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.apagar, container, false);
        final Spinner spinner = v.findViewById(R.id.spinnerApagar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, tipoCadastro);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout apagarExame = v.findViewById(R.id.groupExcluirExame);
                LinearLayout apagarLaboratorio = v.findViewById(R.id.groupExcluirLaboratorio);
                LinearLayout apagarExameLab = v.findViewById(R.id.groupExcluirExameLab);
                Integer apagar = (Integer) spinner.getSelectedItemPosition();
                switch(apagar){
                    case 0:
                        apagarExame.setVisibility(View.GONE);
                        apagarExameLab.setVisibility(View.GONE);
                        apagarLaboratorio.setVisibility(View.GONE);
                        break;
                    case 1:
                        apagarExame.setVisibility(View.VISIBLE);
                        apagarExameLab.setVisibility(View.GONE);
                        apagarLaboratorio.setVisibility(View.GONE);
                        break;
                    case 2:
                        apagarExame.setVisibility(View.GONE);
                        apagarExameLab.setVisibility(View.GONE);
                        apagarLaboratorio.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        apagarExame.setVisibility(View.GONE);
                        apagarExameLab.setVisibility(View.VISIBLE);
                        apagarLaboratorio.setVisibility(View.GONE);
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
