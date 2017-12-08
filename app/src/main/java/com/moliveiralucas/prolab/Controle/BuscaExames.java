package com.moliveiralucas.prolab.Controle;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.moliveiralucas.prolab.R;
import com.moliveiralucas.prolab.model.Cidade;
import com.moliveiralucas.prolab.model.Estado;
import com.moliveiralucas.prolab.model.Exame;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class BuscaExames extends Fragment {

    private Button btnBuscar;
    private Button btnMostrarMapa;
    private ListView lvLaboratorios;
    private String WS_URL;
    private String json;
    private Integer operacao;

    DataPassListener mCallback;

    public interface DataPassListener {
        public void passAddress(String address);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (DataPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " precisa implementar BuscaExames.java");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.busca_exames, container, false);

        loadExame();

        btnBuscar = v.findViewById(R.id.btnBuscarLab);
        btnBuscar.setOnClickListener(buscarLaboratorios());

        btnMostrarMapa = v.findViewById(R.id.btnMostrarNoMapa);
        btnMostrarMapa.setOnClickListener(exibirNoMapa());
        return v;
    }

    private void loadExame() {
        operacao = 1;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchAllExames/";
        new AsyncWS().execute();
    }

    private void loadCidade(Integer id) {
        operacao = 2;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchCidadePorEstado/" + id;
        new AsyncWS().execute();
    }

    private void loadUF() {
        operacao = 3;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchAllUF/";
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
                case 0: //Popular ListView

                    break;
                case 1: //Load Exame
                    final Spinner spinnerExame = getActivity().findViewById(R.id.spinnerSearchExame);
                    ArrayList<Exame> mArrayExame = new ArrayList<Exame>();
                    Exame exame = new Exame();
                    exame.setExame("Selecione um Exame");
                    mArrayExame.add(exame);
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (Integer i = 0; i < jsonArray.length(); i++) {
                            exame = new Exame();
                            exame.setExame(jsonArray.getJSONObject(i).getString("exame"));
                            exame.setExameID(jsonArray.getJSONObject(i).getInt("exameID"));
                            mArrayExame.add(exame);
                        }
                        ArrayAdapter<Exame> mAdapterExame = new ArrayAdapter<Exame>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayExame);
                        mAdapterExame.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerExame.setAdapter(mAdapterExame);
                        spinnerExame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerSearchCidade);
                                Spinner spinnerUF = getActivity().findViewById(R.id.spinnerSearchCidade);
                                if (spinnerExame.getSelectedItemPosition() > 0) {
                                    spinnerUF.setEnabled(true);
                                    loadUF();
                                } else {
                                    String[] vazio = {""};
                                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vazio);
                                    spinnerCidade.setEnabled(false);
                                    spinnerUF.setEnabled(false);
                                    spinnerCidade.setAdapter(mAdapter);
                                    spinnerUF.setAdapter(mAdapter);
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
                case 2: //Load Cidades
                    final Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerSearchCidade);
                    ArrayList<Cidade> mArrayCidade = new ArrayList<Cidade>();
                    Cidade cidade = new Cidade();
                    cidade.setCidade("Selecione uma Cidade");
                    mArrayCidade.add(cidade);
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (Integer i = 0; i < jsonArray.length(); i++) {
                            cidade = new Cidade();
                            cidade.setCidade(jsonArray.getJSONObject(i).getString("cidade"));
                            cidade.setCidadeID(jsonArray.getJSONObject(i).getInt("cidadeID"));
                            mArrayCidade.add(cidade);
                        }
                        ArrayAdapter<Cidade> mAdapterCidade = new ArrayAdapter<Cidade>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayCidade);
                        mAdapterCidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCidade.setAdapter(mAdapterCidade);
                        spinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (spinnerCidade.getSelectedItemPosition() > 0) {

                                } else {

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
                case 3:
                    final Spinner spinnerUF = getActivity().findViewById(R.id.spinnerSearchCidade);
                    ArrayList<Estado> mArrayUF = new ArrayList<Estado>();
                    Estado uf = new Estado();
                    uf.setUf("Selecione um Estado");
                    mArrayUF.add(uf);
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (Integer i = 0; i < jsonArray.length(); i++) {
                            uf = new Estado();
                            uf.setUf(jsonArray.getJSONObject(i).getString("uf"));
                            uf.setUfID(jsonArray.getJSONObject(i).getInt("ufID"));
                            mArrayUF.add(uf);
                        }
                        ArrayAdapter<Estado> mAdapterUF = new ArrayAdapter<Estado>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayUF);
                        mAdapterUF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerUF.setAdapter(mAdapterUF);
                        spinnerUF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerSearchCidade);
                                if (spinnerUF.getSelectedItemPosition() > 0) {
                                    spinnerCidade.setEnabled(true);
                                    Estado estado = (Estado) spinnerUF.getSelectedItem();
                                    loadCidade(estado.getUfID());
                                } else {
                                    String[] vazio = {""};
                                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vazio);
                                    spinnerCidade.setEnabled(false);
                                    spinnerCidade.setAdapter(mAdapter);
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
