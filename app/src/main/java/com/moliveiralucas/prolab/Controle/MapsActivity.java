package com.moliveiralucas.prolab.Controle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends SupportMapFragment implements OnMapReadyCallback{
    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final int maxResult = 1;

    public final String DATA_RECEIVE = "data_receive";
    public String addressIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null){
            addressIntent = args.getString(DATA_RECEIVE);
        }
    }

    public static LatLng reverseGeocoding(Context context, String localAddress){
        if(!Geocoder.isPresent()){
            Log.w("LOG","MapsActivity Geocoder implementation not present!");
        }
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        try{
            List<Address> addresses = geoCoder.getFromLocationName(localAddress, maxResult);
            int tentatives = 0;
            while (addresses.size() == 0 && (tentatives < 10)){
                addresses = geoCoder.getFromLocationName("",1);
                tentatives++;
            }
            if (addresses.size()>0){
                Log.d("LOG","Reverse Geocoding : LocationName: "+localAddress+" Latitude: "+addresses.get(0).getLatitude()+" Longitude: "+addresses.get(0).getLongitude());
                return new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
            }else{
                Log.d("LOG","Algo deu errado");
            }
        }catch(IOException e){
            Log.d(MapsActivity.class.getName(),"Não foi possivel encontrar LatLng do endereço informado: "+ localAddress);

        }
        return null;
    }
/*
    public static String findAddress(Context context, LatLng latLng){
        StringBuilder address = new StringBuilder();
        final int maxResult = 10;
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,maxResult);
            if (addresses.size() > 0){
                Address first = addresses.get(0);
                for (int i=0; i<first.getMaxAddressLineIndex();i++){
                    address.append(first.getAddressLine(i));
                    address.append(" ");
                }
            }
        } catch (IOException e) {
            Log.d(MapsActivity.class.getName(),"Não foi possivel encontrar o endereço pelo LatLng: "+latLng.toString());
        }
        return address.toString();
    }
*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMinZoomPreference(15);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng local = reverseGeocoding(getContext(),addressIntent);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(local));
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
    }

    /*
    import android.location.Location;
    Location firstLocation = new Location("");
    firstLocation.setLatitude(Double.parseDouble(OrigemLatitude.getText().toString()));
    firstLocation.setLongitude(Double.parseDouble(OrigemLongitude.getText().toString()));
    float distanceMeters = firstLocation.distanceTo(mLastLocation);

     */
}
