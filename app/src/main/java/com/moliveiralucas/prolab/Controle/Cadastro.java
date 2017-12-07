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
import com.moliveiralucas.prolab.model.Cidade;
import com.moliveiralucas.prolab.model.Estado;
import com.moliveiralucas.prolab.model.Exame;
import com.moliveiralucas.prolab.model.Laboratorio;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Cadastro extends Fragment {
    private String[] tipoCadastro = {"Selecione um tipo de Cadastro", "Exame", "Laboratório", "Filial", "Atribuir Exame a Laboratório"};
    private String WS_URL = "";
    private String json;
    private Integer cadastro;
    /*
    Operações
        0: Saida
        1: Load Laboratorio
        2: Load Uf
        3: Load Cidade
        4: Load Select Laboratorio
        5: Load Select Exame
     */
    private Integer operacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.cadastro, container, false);
        final Spinner spinnerTipoCadastro = v.findViewById(R.id.spinnerTipoCadastro);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, tipoCadastro);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerTipoCadastro.setAdapter(adapter);

        spinnerTipoCadastro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cadastro = (Integer) spinnerTipoCadastro.getSelectedItemPosition();
                LinearLayout cadExame = v.findViewById(R.id.groupCadastroExame);
                LinearLayout cadLab = v.findViewById(R.id.groupCadastroLaboratorio);
                LinearLayout cadFilial = v.findViewById(R.id.groupCadastroFilial);
                LinearLayout cadExLab = v.findViewById(R.id.groupCadastroExameLab);
                switch (cadastro) {
                    case 0:
                        cadExame.setVisibility(View.GONE);
                        cadLab.setVisibility(View.GONE);
                        cadFilial.setVisibility(View.GONE);
                        cadExLab.setVisibility(View.GONE);
                        break;
                    case 1:
                        cadExame.setVisibility(View.VISIBLE);
                        cadLab.setVisibility(View.GONE);
                        cadFilial.setVisibility(View.GONE);
                        cadExLab.setVisibility(View.GONE);
                        break;
                    case 2:
                        cadExame.setVisibility(View.GONE);
                        cadLab.setVisibility(View.VISIBLE);
                        cadFilial.setVisibility(View.GONE);
                        cadExLab.setVisibility(View.GONE);
                        break;
                    case 3:
                        //Carregar Spinner de LABORATORIOS / CIDADE / ESTADO
                        loadLaboratorio();
                        cadExame.setVisibility(View.GONE);
                        cadLab.setVisibility(View.GONE);
                        cadFilial.setVisibility(View.VISIBLE);
                        cadExLab.setVisibility(View.GONE);
                        break;
                    case 4:
                        //Carregar Spinner de LABORATORIOS / EXAME
                        loadSelectLaboratorio();
                        cadExame.setVisibility(View.GONE);
                        cadLab.setVisibility(View.GONE);
                        cadFilial.setVisibility(View.GONE);
                        cadExLab.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnEnviar = v.findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(enviar());
        Button btnCancelar = v.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(cancelar());
        return v;
    }

    public View.OnClickListener cancelar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(new Empety(), "Empety");
            }
        };
    }

    public void loadLaboratorio() {
        operacao = 1;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchLabs/";
        new AsyncWS().execute();
    }

    public void loadEstado() {
        operacao = 2;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchAllUF/";
        new AsyncWS().execute();
    }

    public void loadCidade(Integer id) {
        operacao = 3;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchCidadePorEstado/" + id;
        new AsyncWS().execute();
    }

    public void loadSelectLaboratorio() {
        operacao = 4;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchLabs/";
        new AsyncWS().execute();
    }

    public void loadSelectExame() {
        operacao = 5;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchAllExames/";
        new AsyncWS().execute();
    }

    public View.OnClickListener enviar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cadastro != null) {
                    switch (cadastro) {
                        case 1:
                            EditText txtExame = getActivity().findViewById(R.id.txtExame);
                            if (txtExame.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Informe o nome do Exame", Toast.LENGTH_SHORT).show();
                            } else {
                                operacao = 0;
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/cadExame/" + txtExame.getText().toString();
                                new AsyncWS().execute();
                            }
                            break;
                        case 2:
                            EditText txtLaboratorio = getActivity().findViewById(R.id.txtLaboratorio);
                            if (txtLaboratorio.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Informe o nome do Laboratório", Toast.LENGTH_SHORT).show();
                            } else {
                                operacao = 0;
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/cadLabor/" + txtLaboratorio.getText().toString();
                                new AsyncWS().execute();
                            }
                            break;
                        case 3:
                            Spinner spinnerLaboratorio = getActivity().findViewById(R.id.spinnerLaboratorio);
                            Spinner spinnerUF = getActivity().findViewById(R.id.spinnerUF);
                            Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerCidade);
                            EditText txtLogradouro = getActivity().findViewById(R.id.txtLogradouro);
                            EditText txtNumero = getActivity().findViewById(R.id.txtNumero);
                            if (spinnerLaboratorio.getSelectedItemPosition() == 0 &&
                                    spinnerUF.getSelectedItemPosition() == 0 &&
                                    spinnerCidade.getSelectedItemPosition() == 0 &&
                                    txtLogradouro.getText().toString().equals("") &&
                                    txtNumero.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Informe todos os campos", Toast.LENGTH_SHORT).show();
                            } else {
                                //Verificar dados para entrar no WS_URL
                                Estado uf = (Estado) spinnerUF.getSelectedItem();
                                Cidade cidade = (Cidade) spinnerCidade.getSelectedItem();
                                Laboratorio lab = (Laboratorio) spinnerLaboratorio.getSelectedItem();
                                operacao = 0;
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/cadFilial/" + lab.getLabID() + "_" + txtLogradouro.getText().toString() + "_" + txtNumero.getText().toString() + "_" + cidade.getCidadeID() + "_" + uf.getUfID();
                                new AsyncWS().execute();
                            }
                            break;
                        case 4:
                            Spinner spinnerSelectLab = getActivity().findViewById(R.id.spinnerSelectLab);
                            Spinner spinnerSelectExame = getActivity().findViewById(R.id.spinnerSelectExame);
                            EditText txtValor = getActivity().findViewById(R.id.txtValor);
                            if (spinnerSelectLab.getSelectedItemPosition() == 0 && spinnerSelectExame.getSelectedItemPosition() == 0 && txtValor.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Informe todos os campos", Toast.LENGTH_SHORT).show();
                            } else {
                                Laboratorio lab = (Laboratorio) spinnerSelectLab.getSelectedItem();
                                Exame exame = (Exame) spinnerSelectExame.getSelectedItem();
                                exame.setValor(Double.parseDouble(txtValor.getText().toString()));
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/cadExameLab/" + lab.getLabID() + "_" + exame.getExameID() + "_" + exame.getValor();
                                operacao = 0;
                                new AsyncWS().execute();
                            }
                            break;
                    }
                } else {
                    Toast.makeText(getActivity(), "Selecione o tipo de cadastro", Toast.LENGTH_SHORT).show();
                }
            }
        };
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
                case 1: //Load Laboratorio
                    ArrayList<Laboratorio> mArrayLaboratorio = new ArrayList<Laboratorio>();
                    Laboratorio lab = new Laboratorio();
                    lab.setLaboratorio("Selecione um laboratorio");
                    mArrayLaboratorio.add(lab);
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            lab = new Laboratorio();
                            lab.setLaboratorio(jsonArray.getJSONObject(i).getString("laboratorio"));
                            lab.setLabID(jsonArray.getJSONObject(i).getInt("labID"));
                            mArrayLaboratorio.add(lab);
                        }
                        final Spinner spinnerLaboratorio = getActivity().findViewById(R.id.spinnerLaboratorio);
                        final ArrayAdapter<Laboratorio> mAdapter = new ArrayAdapter<Laboratorio>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayLaboratorio);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerLaboratorio.setAdapter(mAdapter);
                        spinnerLaboratorio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (spinnerLaboratorio.getSelectedItemPosition() > 0) {
                                    Spinner spinnerUF = getActivity().findViewById(R.id.spinnerUF);
                                    spinnerUF.setEnabled(true);
                                    loadEstado();
                                } else {
                                    String vazia[] = {""};
                                    ArrayAdapter<String> adapterClear = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vazia);
                                    Spinner spinnerUF = getActivity().findViewById(R.id.spinnerUF);
                                    spinnerUF.setAdapter(adapterClear);
                                    spinnerUF.setEnabled(false);
                                    Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerCidade);
                                    spinnerCidade.setAdapter(adapterClear);
                                    spinnerCidade.setEnabled(false);
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
                case 2: //Load UF (Estado)
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
                        final Spinner spinnerUF = getActivity().findViewById(R.id.spinnerUF);
                        ArrayAdapter<Estado> mAdapter = new ArrayAdapter<Estado>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayUF);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerUF.setAdapter(mAdapter);
                        spinnerUF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (spinnerUF.getSelectedItemPosition() > 0) {
                                    Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerCidade);
                                    spinnerCidade.setEnabled(true);

                                    Estado uf = (Estado) adapterView.getItemAtPosition(i);
                                    loadCidade(uf.getUfID());
                                } else {
                                    String vazia[] = {""};
                                    ArrayAdapter<String> adapterClear = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vazia);
                                    Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerCidade);
                                    spinnerCidade.setAdapter(adapterClear);
                                    spinnerCidade.setEnabled(false);
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
                case 3: //Load Cidade
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
                        Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerCidade);
                        ArrayAdapter<Cidade> mAdapter = new ArrayAdapter<Cidade>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayCidade);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCidade.setAdapter(mAdapter);

                    } catch (JSONException e) {
                        Log.v("JSONArray ERROR: ", e.getMessage());
                    }
                    break;
                case 4: //Load Select Laboratorio
                    ArrayList<Laboratorio> mArraySelectLaboratorio = new ArrayList<Laboratorio>();
                    Laboratorio mSelectLab = new Laboratorio();
                    mSelectLab.setLaboratorio("Selecione o Laboratório");
                    mArraySelectLaboratorio.add(mSelectLab);
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            mSelectLab = new Laboratorio();
                            mSelectLab.setLaboratorio(jsonArray.getJSONObject(i).getString("laboratorio"));
                            mSelectLab.setLabID(jsonArray.getJSONObject(i).getInt("labID"));
                            mArraySelectLaboratorio.add(mSelectLab);
                        }
                        final Spinner spinnerLaboratorio = getActivity().findViewById(R.id.spinnerSelectLab);
                        ArrayAdapter<Laboratorio> mAdapter = new ArrayAdapter<Laboratorio>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArraySelectLaboratorio);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerLaboratorio.setAdapter(mAdapter);
                        spinnerLaboratorio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Spinner spinnerExame = getActivity().findViewById(R.id.spinnerSelectExame);
                                if (spinnerLaboratorio.getSelectedItemPosition() > 0) {
                                    spinnerExame.setEnabled(true);
                                    loadSelectExame();
                                } else {
                                    String vazia[] = {""};
                                    ArrayAdapter<String> adapterClear = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vazia);
                                    spinnerExame.setAdapter(adapterClear);
                                    spinnerExame.setEnabled(false);
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
                case 5: //Load Select Exame
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
                        Spinner spinnerExame = getActivity().findViewById(R.id.spinnerSelectExame);
                        ArrayAdapter<Exame> mAdapter = new ArrayAdapter<Exame>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArraySelectExame);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerExame.setAdapter(mAdapter);
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

    private void showFragment(Fragment fragment, String name) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.idContainer, fragment, name);
        transaction.addToBackStack(null);
        transaction.commit();
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
