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

import com.google.gson.Gson;
import com.moliveiralucas.prolab.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Cadastro extends Fragment {
    private String[] tipoCadastro = {"Selecione um tipo de Cadastro", "Exame", "Laboratório", "Filial", "Atribuir Exame a Laboratório"};
    private String WS_URL = "";
    private String json;
    private String loadJsonSpinner;
    private Integer cadastro;

    public Cadastro() {
        // Required empty public constructor
    }


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
                        loadEstado();
                        loadCidade();
                        cadExame.setVisibility(View.GONE);
                        cadLab.setVisibility(View.GONE);
                        cadFilial.setVisibility(View.VISIBLE);
                        cadExLab.setVisibility(View.GONE);
                        break;
                    case 4:
                        //Carregar Spinner de LABORATORIOS / EXAME
                        loadLaboratorio();
                        loadExame();
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

        return v;
    }

    public void loadLaboratorio() {
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchLabs";
    }

    public void loadEstado() {
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchAllUF";
    }

    public void loadCidade() {
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchCidadePorEstado/";
    }

    public void loadExame() {
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/searchAllExames";
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
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/cadExame/" + txtExame.getText().toString();
                                new AsyncWS().execute();
                            }
                            break;
                        case 2:
                            EditText txtLaboratorio = getActivity().findViewById(R.id.txtLaboratorio);
                            if (txtLaboratorio.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Informe o nome do Laboratório", Toast.LENGTH_SHORT).show();
                            } else {
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
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/cadFilial/" + spinnerLaboratorio.getSelectedItem() + "_" + txtLogradouro.getText().toString() + "_" + txtNumero.getText().toString() + "_" + spinnerCidade.getSelectedItem() + "_" + spinnerUF.getSelectedItem();
                                new AsyncWS().execute();
                            }
                            break;
                        case 4:
                            Spinner spinnerSelectLab = getActivity().findViewById(R.id.spinnerSelectLab);
                            Spinner spinnerSelectExame = getActivity().findViewById(R.id.spinnerSelectExame);
                            EditText txtValor = getActivity().findViewById(R.id.txtValor);
                            if (spinnerSelectLab.getSelectedItemPosition() > 0 && spinnerSelectExame.getSelectedItemPosition() > 0 && txtValor.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Informe todos os campos", Toast.LENGTH_SHORT).show();
                            } else {
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/atrExameLaboratorio/" + spinnerSelectLab.getSelectedItem() + "_" + spinnerSelectExame.getSelectedItem() + "_" + txtValor.getText().toString();
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
        }
    }

    private class AsyncWSSpinner extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            try {
                loadJsonSpinner = getJSONObjectFromURL(WS_URL);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return loadJsonSpinner;
        }

        @Override
        protected void onPostExecute(String s) {

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

    private <T extends Object> T getTranslation(String json, Class<T> cl) {
        return new Gson().fromJson(json, cl);
    }
}
