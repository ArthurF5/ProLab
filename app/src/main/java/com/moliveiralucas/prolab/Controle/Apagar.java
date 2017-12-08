package com.moliveiralucas.prolab.Controle;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.gms.vision.text.Line;
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

public class Apagar extends Fragment {
    private String[] tipoCadastro = {"Selecione o assunto", "Exame", "Laboratório", "Exame de Laboratório"};
    private Integer apagar;
    private String WS_URL;
    private String json;
    private Integer operacao;

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
                apagar = (Integer) spinner.getSelectedItemPosition();
                switch (apagar) {
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
        Button btnEnviar = v.findViewById(R.id.btnEnviarDelete);
        btnEnviar.setOnClickListener(enviar());
        Button btnCancelar = v.findViewById(R.id.btnCancelarDelete);
        btnCancelar.setOnClickListener(cancelar());
        return v;
    }

    private void alertBox(final String parametro, final Integer id) {
        AlertDialog.Builder mAlertBox = new AlertDialog.Builder(getActivity());
        mAlertBox.setMessage("Apagar")
                .setTitle("Deseja realmente apagar: " + parametro + "?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        operacao = 0;
                        switch (apagar) {
                            case 1:
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/excluirExame/" + id;
                                new AsyncWS().execute();
                                break;
                            case 2:
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/excluirLaboratorio/" + id;
                                new AsyncWS().execute();
                                break;
                            case 3:
                                WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/excluirExameLaboratorio/" + Integer.parseInt(parametro) + "_" + id;
                                new AsyncWS().execute();
                                break;
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

                    break;
                case 2:

                    break;
                case 3:

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

    private View.OnClickListener enviar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (apagar) {
                    case 1:
                        Spinner spinnerExame = getActivity().findViewById(R.id.spinnerDeleteExame);
                        Exame exame = (Exame) spinnerExame.getSelectedItem();
                        alertBox(exame.getExame(), exame.getExameID());
                        break;
                    case 2:
                        Spinner spinnerLaboratorio = getActivity().findViewById(R.id.spinnerDeleteLaboratorio);
                        Laboratorio lab = (Laboratorio) spinnerLaboratorio.getSelectedItem();
                        alertBox(lab.getLaboratorio(), lab.getLabID());
                        break;
                    case 3:
                        Spinner spinnerSelectLab = getActivity().findViewById(R.id.spinnerSelectDeleteLab);
                        Spinner spinnerSelectExame = getActivity().findViewById(R.id.spinnerSelectDeleteExame);
                        Laboratorio laboratorio = (Laboratorio) spinnerSelectLab.getSelectedItem();
                        Exame ex = (Exame) spinnerSelectExame.getSelectedItem();
                        alertBox("" + laboratorio.getLabID(), ex.getExameID());
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

    private void showFragment(Fragment fragment, String name) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.idContainer, fragment, name);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
