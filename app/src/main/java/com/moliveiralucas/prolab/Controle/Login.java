package com.moliveiralucas.prolab.Controle;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moliveiralucas.prolab.MainActivity;
import com.moliveiralucas.prolab.R;
import com.moliveiralucas.prolab.model.Usuario;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class Login extends Fragment {

    private String json;
    private String WS_URL = "";

    EfetuarLogin listener;

    public interface EfetuarLogin {
        void efetuarLogin(Usuario usr);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EfetuarLogin) {
            listener = (EfetuarLogin) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(logar());

        Button btnCancelarLogin = v.findViewById(R.id.btnCancelarLogin);
        btnCancelarLogin.setOnClickListener(cancelar());
        return v;
    }

    public View.OnClickListener logar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtUsuario = getActivity().findViewById(R.id.txtUsuario);
                String usuario = txtUsuario.getText().toString();

                EditText txtPw = getActivity().findViewById(R.id.txtSenha);
                String senha = txtPw.getText().toString();

                if (usuario.equals("") && senha.equals("")) {
                    Toast.makeText(getActivity(), "Informe seu Usuário e Senha", Toast.LENGTH_SHORT).show();
                } else {
                    WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/login/" + usuario + "_" + senha;
                    new AsyncWS().execute();
                }
            }
        };
    }

    public View.OnClickListener cancelar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        protected void onPostExecute(String json) {
            if (json != null) {
                Usuario usr = (Usuario) getTranslation(json, Usuario.class);
                if(!usr.getUsuario().equals("")) {
                    listener.efetuarLogin(usr);
                    showFragment(new Cadastro(), "Cadastro");
                }else{
                    Toast.makeText(getActivity(), "Login ou senha incorretos, tente novamente!", Toast.LENGTH_SHORT).show();
                }
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

    private <T extends Object> T getTranslation(String json, Class<T> cl) {
        return new Gson().fromJson(json, cl);
    }
}
