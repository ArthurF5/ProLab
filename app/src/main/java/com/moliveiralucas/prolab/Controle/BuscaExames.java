package com.moliveiralucas.prolab.Controle;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.maps.model.LatLng;
import com.moliveiralucas.prolab.R;
import com.moliveiralucas.prolab.model.Cidade;
import com.moliveiralucas.prolab.model.Estado;
import com.moliveiralucas.prolab.model.Exame;
import com.moliveiralucas.prolab.model.Filial;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuscaExames extends Fragment {

    private Button btnBuscar;
    private Button btnMostrarMapa;
    private String WS_URL;
    private String json;
    private Integer operacao;
    private Double latPoint, lngPoint;
    private static final int maxResult = 1;

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

    private void loadListaLaboratorios(Integer cidadeId) {
        operacao = 0;
        WS_URL = "http://10.42.0.1:8080/ProLabWEBApp/service/SearchLabPorCidade/" + cidadeId;
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
                    ListView mListView = getActivity().findViewById(R.id.listLaboratorio);

                    pedirPermissoes();

                    Spinner spinnerCity = getActivity().findViewById(R.id.spinnerSearchCidade);
                    Spinner spinnerEstado = getActivity().findViewById(R.id.spinnerSearchCidade);
                    Cidade mCidade = (Cidade) spinnerCity.getSelectedItem();
                    Estado mUF = (Estado) spinnerEstado.getSelectedItem();

                    ArrayList<Filial> mArrayFilial = new ArrayList<Filial>();
                    Filial mFilial;

                    Location firstLocation = new Location("");
                    firstLocation.setLatitude(latPoint);
                    firstLocation.setLongitude(lngPoint);

                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (Integer i = 0; i < jsonArray.length(); i++) {
                            mFilial = new Filial();
                            mFilial.setmLab(jsonArray.getJSONObject(i).getString("laboratorio"));
                            mFilial.setmEnd(jsonArray.getJSONObject(i).getString("rua") + " " + mCidade.getCidade() + "-" + mUF.getUf());

                            //Calcula a distancia entre o ponto atual e o endereço
                            Location lastlocation = new Location("");
                            lastlocation.setLatitude(reverseGeocoding(getActivity(), mFilial.getmEnd()).latitude);
                            lastlocation.setLongitude(reverseGeocoding(getActivity(), mFilial.getmEnd()).longitude);
                            float distanceMeters = firstLocation.distanceTo(lastlocation) / 1000;
                            mFilial.setmDistancia(distanceMeters);

                            mArrayFilial.add(mFilial);
                        }
                        FilialListAdapter adapter = new FilialListAdapter(getActivity(), R.layout.adapter_view_layout, mArrayFilial);
                        mListView.setAdapter(adapter);
                    } catch (JSONException e) {
                        Log.v("JSONArray ERROR: ", e.getMessage());
                    }
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
                        final Spinner spinnerCidade = getActivity().findViewById(R.id.spinnerSearchCidade);
                        ArrayAdapter<Cidade> mAdapterCidade = new ArrayAdapter<Cidade>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayCidade);
                        mAdapterCidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCidade.setAdapter(mAdapterCidade);
                        spinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (spinnerCidade.getSelectedItemPosition() > 0) {
                                    Cidade cid = (Cidade) spinnerCidade.getSelectedItem();
                                    loadListaLaboratorios(cid.getCidadeID());
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
                    final Spinner spinnerUF = getActivity().findViewById(R.id.spinnerSearchUF);
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

    private void pedirPermissoes() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            getLocation();
        }
    }

    public void getLocation() {
        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latPoint = location.getLatitude();
                    lngPoint = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            Log.v("Att Location ERROR:", e.getMessage());
            Toast.makeText(getActivity(), "Erro ao atualizar a Localização", Toast.LENGTH_SHORT).show();
        }
    }

    public static LatLng reverseGeocoding(Context context, String localAddress) {
        if (!Geocoder.isPresent()) {
            Log.w("LOG", "MapsActivity Geocoder implementation not present!");
        }
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(localAddress, maxResult);
            int tentatives = 0;
            while (addresses.size() == 0 && (tentatives < 10)) {
                addresses = geoCoder.getFromLocationName("", 1);
                tentatives++;
            }
            if (addresses.size() > 0) {
                Log.d("LOG", "Reverse Geocoding : LocationName: " + localAddress + " Latitude: " + addresses.get(0).getLatitude() + " Longitude: " + addresses.get(0).getLongitude());
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            } else {
                Log.d("LOG", "Algo deu errado");
            }
        } catch (IOException e) {
            Log.d(BuscaExames.class.getName(), "Não foi possivel encontrar LatLng do endereço informado: " + localAddress);

        }
        return null;
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
