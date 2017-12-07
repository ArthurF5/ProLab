package com.moliveiralucas.prolab.Controle;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.moliveiralucas.prolab.R;
import com.moliveiralucas.prolab.model.Exame;
import com.moliveiralucas.prolab.model.Laboratorio;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class Atualizar extends Fragment {
    private String[] tipoCadastro = {"Selecione o assunto", "Exame", "Laboratório"};
    private String json;
    private String WS_URL = "";
    private Integer update;
    private Integer operacao;

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
                update = (Integer) spinner.getSelectedItemPosition();
                switch (update) {
                    case 0:
                        updateExame.setVisibility(View.GONE);
                        updateLaboratorio.setVisibility(View.GONE);
                        break;
                    case 1:
                        updateExame.setVisibility(View.VISIBLE);
                        updateLaboratorio.setVisibility(View.GONE);
                        loadExame();
                        break;
                    case 2:
                        updateExame.setVisibility(View.GONE);
                        updateLaboratorio.setVisibility(View.VISIBLE);
                        loadLaboratorio();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button btnEnviar = v.findViewById(R.id.btnEnviarAtualizacao);
        btnEnviar.setOnClickListener(enviar());
        Button btnCancelar = v.findViewById(R.id.btnCancelarAtualizacao);
        btnCancelar.setOnClickListener(cancelar());
        return v;
    }

    private View.OnClickListener enviar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (update) {
                    case 1:
                        EditText txtExame = getActivity().findViewById(R.id.txtExameAtt);
                        EditText txtExameID = getActivity().findViewById(R.id.txtExameIDAtt);
                        if (txtExame.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "Informe o Exame", Toast.LENGTH_SHORT).show();
                        } else {
                            WS_URL = "http://10.42.0.1:8080" + txtExame.getText().toString() + "_" + txtExameID.getText().toString();
                            operacao = 0;
                            new AsyncWS().execute();
                        }
                        break;
                    case 2:
                        EditText txtLab = getActivity().findViewById(R.id.txtLabAtt);
                        EditText txtLabID = getActivity().findViewById(R.id.txtLabIDAtt);
                        if (txtLab.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "Informe o Laboratorio", Toast.LENGTH_SHORT).show();
                        } else {
                            WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/attLaboratorio/" + txtLab.getText().toString() + "_" + txtLabID.getText().toString();
                            operacao = 0;
                            new AsyncWS().execute();
                        }
                        break;
                }
            }
        };
    }

    private View.OnClickListener cancelar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(new Empety(), "Empety");
            }
        };
    }

    public void loadExame() {
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchAllExames/";
        operacao = 1;
        new AsyncWS().execute();
    }

    public void loadLaboratorio() {
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchLabs/";
        operacao = 2;
        new AsyncWS().execute();
    }

    private class AsyncWS extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            try {
                json = getJSONObjectFromURL(WS_URL);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            switch (operacao) {
                case 0: //Saida
                    if (json != null) {
                        Integer retorno = Integer.parseInt(json);
                        switch (retorno) {
                            case 1:
                                Toast.makeText(getActivity(), "Sem Permissão", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getActivity(), "Operação realizada com Sucesso", Toast.LENGTH_SHORT).show();
                                showFragment(new Empety(), "Empety");
                                break;
                            case 3:
                                Toast.makeText(getActivity(), "Objeto Nulo", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(getActivity(), "Já possui Cadastro", Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
                                Toast.makeText(getActivity(), "Houve algum erro ao realizar a operação verificar log", Toast.LENGTH_SHORT).show();
                                break;
                            case 6:
                                Toast.makeText(getActivity(), "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    break;
                case 1:
                    ArrayList<Exame> mArraySelectExame = new ArrayList<Exame>();
                    Exame mExame = new Exame();
                    mExame.setExame("Selecione um Exame");
                    mArraySelectExame.add(mExame);
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            mExame = new Exame();
                            mExame.setExame(jsonArray.getJSONObject(i).getString("exame"));
                            mExame.setExameID(jsonArray.getJSONObject(i).getInt("exameID"));
                            mArraySelectExame.add(mExame);
                        }
                        final Spinner spinnerExame = getActivity().findViewById(R.id.spinnerAttExame);
                        ArrayAdapter<Exame> mAdapter = new ArrayAdapter<Exame>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArraySelectExame);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerExame.setAdapter(mAdapter);
                        spinnerExame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                EditText txtExameID = getActivity().findViewById(R.id.txtExameIDAtt);
                                EditText txtExame = getActivity().findViewById(R.id.txtExameAtt);
                                if (spinnerExame.getSelectedItemPosition() > 0) {
                                    Exame exame = (Exame) spinnerExame.getSelectedItem();
                                    txtExameID.setText(""+exame.getExameID());
                                    txtExameID.setEnabled(false);
                                    txtExame.setText(exame.getExame());
                                    txtExame.setEnabled(true);
                                } else {
                                    txtExame.setText("");
                                    txtExame.setEnabled(false);
                                    txtExameID.setText("");
                                    txtExameID.setEnabled(false);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } catch (JSONException e) {
                        Log.v("JSONArray ERROR: ", e.getMessage());
                    }
                    break;
                case 2:
                    ArrayList<Laboratorio> mArrayAttLaboratorio = new ArrayList<Laboratorio>();
                    Laboratorio mSelectLab = new Laboratorio();
                    mSelectLab.setLaboratorio("Selecione o Laboratório");
                    mArrayAttLaboratorio.add(mSelectLab);
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            mSelectLab = new Laboratorio();
                            mSelectLab.setLaboratorio(jsonArray.getJSONObject(i).getString("laboratorio"));
                            mSelectLab.setLabID(jsonArray.getJSONObject(i).getInt("labID"));
                            mArrayAttLaboratorio.add(mSelectLab);
                        }
                        final Spinner spinnerLaboratorio = getActivity().findViewById(R.id.spinnerAttLaboratorio);
                        ArrayAdapter<Laboratorio> mAdapter = new ArrayAdapter<Laboratorio>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayAttLaboratorio);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerLaboratorio.setAdapter(mAdapter);
                        spinnerLaboratorio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                EditText txtLabID = getActivity().findViewById(R.id.txtLabIDAtt);
                                EditText txtLab = getActivity().findViewById(R.id.txtLabAtt);
                                if (spinnerLaboratorio.getSelectedItemPosition() > 0) {
                                    Laboratorio lab = (Laboratorio) spinnerLaboratorio.getSelectedItem();
                                    txtLab.setText(lab.getLaboratorio());
                                    txtLab.setEnabled(true);
                                    txtLabID.setText(""+lab.getLabID());
                                    txtLabID.setEnabled(false);
                                } else {
                                    txtLab.setText("");
                                    txtLab.setEnabled(false);
                                    txtLabID.setText("");
                                    txtLabID.setEnabled(false);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } catch (JSONException e) {
                        Log.v("JSONArray ERROR: ", e.getMessage());
                    }
                    break;
                default:
                    Toast.makeText(getActivity(), "Operação Inválida", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        String line, newjson = "";
        URL urls = new URL(urlString);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urls.openStream(), "UTF-8"))) {
            while ((line = reader.readLine()) != null) {
                newjson += line;
            }
            Log.v("JSON: ", newjson);
            return newjson;
        }
    }

    private void showFragment(Fragment fragment, String name) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.idContainer, fragment, name);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
