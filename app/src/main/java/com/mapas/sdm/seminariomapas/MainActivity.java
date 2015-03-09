package com.mapas.sdm.seminariomapas;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private ArrayList<City> cities;

    private LocationManager manager;
    private MyListener listener;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest request;

    private HashMap<Marker,City> marker_cities =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("tab1");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("tab2");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        /** Using Location Manager**/

        setLocationManager();

        /** Using Location Services**/
        //createLocationRequest();
        //setLocationServices();

        setCities();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check the availability of the Google Play Services

        /** Using Location Services**/
        /*int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (available != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(available, this, 0);
            if (dialog != null) {
                 MyErrorDialog errorDialog = new MyErrorDialog();
                 errorDialog.setDialog(dialog);
                 errorDialog.show(getSupportFragmentManager(), "errorDialog");
            }
        }*/


        /** Instanciamos el Mapa **/
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                // El mapa está instanciado correctamente
                // ahora podemos empezar a modificarlo




            }
        }
    }


    private void setCities(){
        if(cities==null)
            cities = new ArrayList<City>();
        else
            cities.clear();

        cities.add(new City("Barcelona",41.38792,2.169919,R.mipmap.ic_bcn));
        cities.add(new City("Madrid",40.41669,-3.700346,R.mipmap.ic_madrid));
        cities.add(new City("Valencia", 39.47024, -0.3768049, R.mipmap.ic_vlc));
        cities.add(new City("Granada",37.17649,-3.597929,R.mipmap.ic_granada));

    }

    public void setLocationManager(){
        listener = new MyListener(this);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ToggleButton toggleLocation = (ToggleButton) findViewById(R.id.tbGeolocation);
        toggleLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (((ToggleButton) v).isChecked()) {
                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                    } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                    }
                } else {
                    manager.removeUpdates(listener);
                }
            }
        });
    }

    protected void createLocationRequest() {
        request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void setLocationServices(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        ToggleButton toggleLocation = (ToggleButton) findViewById(R.id.tbGeolocation);
        toggleLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (((ToggleButton) v).isChecked()) {
                   mGoogleApiClient.connect();
                } else {
                   mGoogleApiClient.disconnect();
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, request, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Location changed", Toast.LENGTH_SHORT).show();
        ((EditText) findViewById(R.id.etLongitude)).setText(Double.toString(location.getLongitude()));
        ((EditText) findViewById(R.id.etLatitude)).setText(Double.toString(location.getLatitude()));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    public HashMap<Marker, City> getMarkerCities(){
        return marker_cities;
    }

    private void setUpMarkers(){
        if(marker_cities==null){
            marker_cities = new HashMap<Marker,City>();
        }
        for(City c: cities) {

            Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(c.toLatLng())
                            .title(c.getName())
            );

            marker_cities.put(marker,c);
        }
    }

}
