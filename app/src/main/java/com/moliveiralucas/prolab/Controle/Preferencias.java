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
import android.widget.Spinner;
import android.widget.Toast;

import com.moliveiralucas.prolab.R;
import com.moliveiralucas.prolab.model.Cidade;
import com.moliveiralucas.prolab.model.Estado;
import com.moliveiralucas.prolab.model.Exame;
import com.moliveiralucas.prolab.model.Laboratorio;
import com.moliveiralucas.prolab.util.PrefUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Preferencias extends Fragment {

    private String WS_URL;
    private String json;
    private Integer operacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preferencias, container, false);
        loadUF();

        return v;
    }

    private View.OnClickListener salvarPref() {
        final Spinner spinnerUF = getActivity().findViewById(R.id.spinnerPrefUF);
        final Estado uf = (Estado) spinnerUF.getSelectedItem();
        final Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerPrefCidade);
        final Cidade cidade = (Cidade) spinnerCidade.getSelectedItem();
        final EditText txtEndereco = getActivity().findViewById(R.id.txtPreferenciaEnd);
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefUtil.setString(getContext(), "UF.des", String.valueOf(uf.getUf()));
                PrefUtil.setInteger(getContext(), "UF.cod", spinnerUF.getSelectedItemPosition());

                PrefUtil.setString(getContext(), "Cidade.des", String.valueOf(cidade.getCidade()));
                PrefUtil.setInteger(getContext(), "Cidade.cod", spinnerCidade.getSelectedItemPosition());

                PrefUtil.setString(getContext(), "Endereco.des", String.valueOf(txtEndereco.getText()));
            }
        };
    }


    public void loadUF() {
        operacao = 0;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchAllUF/";
        new AsyncWS().execute();
    }

    public void loadCidade(Integer ufID) {
        operacao = 1;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchCidadePorEstado/" + ufID;
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
                case 0: //Load UF (Estado)
                    ArrayList<Estado> mArrayUF = new ArrayList<Estado>();
                    Estado uf = new Estado();
                    uf.setUf("Selecione o Estado");
                    mArrayUF.add(uf);
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            uf = new Estado();
                            uf.setUf(jsonArray.getJSONObject(i).getString("uf"));
                            uf.setUfID(jsonArray.getJSONObject(i).getInt("ufID"));
                            mArrayUF.add(uf);
                        }
                        final Spinner spinnerUF = getActivity().findViewById(R.id.spinnerPrefUF);
                        ArrayAdapter<Estado> mAdapter = new ArrayAdapter<Estado>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayUF);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerUF.setAdapter(mAdapter);
                        spinnerUF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                EditText txtEndereco = getActivity().findViewById(R.id.txtPreferenciaEnd);
                                Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerPrefCidade);
                                if (spinnerUF.getSelectedItemPosition() > 0) {
                                    spinnerCidade.setEnabled(true);

                                    Estado uf = (Estado) adapterView.getItemAtPosition(i);
                                    loadCidade(uf.getUfID());

                                    txtEndereco.setText("");
                                    txtEndereco.setEnabled(false);
                                } else {
                                    String vazia[] = {""};
                                    ArrayAdapter<String> adapterClear = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vazia);
                                    spinnerCidade.setAdapter(adapterClear);
                                    spinnerCidade.setEnabled(false);

                                    txtEndereco.setEnabled(false);
                                    txtEndereco.setText("");
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
                case 1: //Load Cidade
                    ArrayList<Cidade> mArrayCidade = new ArrayList<Cidade>();
                    Cidade cidade = new Cidade();
                    cidade.setCidade("Selecione a Cidade");
                    mArrayCidade.add(cidade);
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            cidade = new Cidade();
                            cidade.setCidade(jsonArray.getJSONObject(i).getString("cidade"));
                            cidade.setCidadeID(jsonArray.getJSONObject(i).getInt("cidadeID"));
                            mArrayCidade.add(cidade);
                        }
                        final Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerPrefCidade);
                        ArrayAdapter<Cidade> mAdapter = new ArrayAdapter<Cidade>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayCidade);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCidade.setAdapter(mAdapter);
                        spinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                EditText txtEndereco = getActivity().findViewById(R.id.txtPreferenciaEnd);
                                if (spinnerCidade.getSelectedItemPosition() > 0) {
                                    txtEndereco.setEnabled(true);

                                    Button btnSalvarPref = getActivity().findViewById(R.id.btnSalvar);

                                    btnSalvarPref.setOnClickListener(salvarPref());

                                } else {
                                    txtEndereco.setEnabled(false);
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
}
