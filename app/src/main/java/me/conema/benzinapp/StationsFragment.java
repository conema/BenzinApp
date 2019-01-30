package me.conema.benzinapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import me.conema.benzinapp.classes.Station;
import me.conema.benzinapp.classes.StationFactory;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;

import timber.log.Timber;

public class StationsFragment extends Fragment implements LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    // gestione per trovare locazione corrente
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    private String locationProvider;
    private Criteria criteria;
    private StationFactory stationFactory;


    // gestione mappa
    private MapView mapView;
    private MapboxMap mapboxMap;
    private LatLng currentMapLocation;
    private MarkerOptions currentPositionMarker;

    // interfaccia sopra la mappa
    //private LinearLayout prova;

    // resto dell'interfaccia
    FloatingActionButton currentPositionButton;

    @SuppressLint("MissingPermission")
    private void updateMapPosition() {
        locationManager.getLastKnownLocation(locationProvider);
        locationManager.requestLocationUpdates(locationProvider, 5000, (float) 2.0, this);

        currentLocation = locationManager.getLastKnownLocation(locationProvider);
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Timber.i("Location:" + String.valueOf(currentLocation.getLatitude()) + " " + String.valueOf(currentLocation.getLongitude()));
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));


        //currentPositionMarker.setPosition(currentLatLng);
        mapboxMap.removeAnnotations();

        //mapboxMap.removeMarker(currentPositionMarker.getMarker());
        mapboxMap.addMarker(currentPositionMarker
                .icon(drawableToIcon(getActivity(), R.drawable.ic_navigation_black_24dp))
                .setPosition(currentLatLng));

    }


    public static Icon drawableToIcon(@NonNull Context context, @DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, context.getTheme());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

        if (canvas.getWidth() > 200 && canvas.getHeight() > 200) {
            canvas.scale(0.3f, 0.3f, canvas.getHeight() / 2, canvas.getWidth() / 2);
        }

        vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }

    @SuppressLint("MissingPermission")
    private void initialize() {
        stationFactory = StationFactory.getInstance();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        locationProvider = locationManager.getBestProvider(criteria, true);

        currentPositionMarker = new MarkerOptions();

        updateMapPosition();


        // si setta listener sui marker
        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(getActivity(), "Il mio creatore deve ancora mettere le info sulla stazione di servizio, accontentati: " +
                        marker.getPosition(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        // aggiunta stazioni di servizio
        Icon pin;
        for(LatLng currentKey : StationFactory.getInstance().getStations().keySet()) {
            pin = drawableToIcon(getActivity(), StationFactory.getInstance().getStations().get(currentKey).getImg());
            mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(StationFactory.getInstance().getStations().get(currentKey).getPosition()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkLocationPermission();
        mapView = getView().findViewById(R.id.mapquestMapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(parMapboxMap -> {
            mapboxMap = parMapboxMap;
            mapView.setStreetMode();
            initialize();
        });

        currentPositionButton = getView().findViewById(R.id.currentPositionButton);
        currentPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMapPosition();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuest.start(getActivity().getApplicationContext());
        //setContentView(R.layout.fragment_stations);

        // innanzitutto si verificano i permessi

        //Serve per visualizzare il tasto indietro
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        mapView.onSaveInstanceState(outState);
    }*/

    @Override
    public void onLocationChanged(Location location) {

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


    // gestione permessi
    public boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    //initialize();
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        updateMapPosition();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}