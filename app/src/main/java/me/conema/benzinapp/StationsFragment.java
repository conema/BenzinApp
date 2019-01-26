package me.conema.benzinapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.PersistableBundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;
import com.mapquest.mapping.utils.PoiOnMapData;

import timber.log.Timber;

public class StationsFragment extends Fragment implements LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    // gestione per trovare locazione corrente
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    private String locationProvider;
    private Criteria criteria;


    // gestione mappa
    private MapView mapView;
    private MapboxMap mapboxMap;
    private LatLng currentMapLocation;
    private MarkerOptions currentPositionMarker;

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
            canvas.scale(0.1f, 0.1f, canvas.getHeight() / 2, canvas.getWidth() / 2);
        }

        vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }

    @SuppressLint("MissingPermission")
    private void initialize() {
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
        Icon pin = drawableToIcon(getActivity(), R.drawable.ic_total_logo);

        // TAMOIL
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2325218, 9.0953491)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2329010, 9.1029220)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2216692, 9.1062200)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2284012, 9.1320594)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(38.9797039, 8.9799854)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.1694859, 8.9862899)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2968868, 9.0009544)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2982480, 9.0936330)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2863720, 9.1856290)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2798682, 9.1815180)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.0088955, 8.9958697)));

        pin = drawableToIcon(getActivity(), R.drawable.ic_eni_logo);
        // AGIP
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.1720020, 8.5224240)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2239334, 9.1155413)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2513209, 9.1339677)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2141564, 9.1075633)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2415420, 9.1509550)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2332010, 9.1864990)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2474346, 9.1959459)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(38.9934133, 8.9925959)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2341300, 9.0974580)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2068343, 9.1331229)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2424670, 9.0908840)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2528680, 9.1136360)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2080880, 9.1337280)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2254425, 9.1298667)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2357140, 9.1126930)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2308900, 9.0949150)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2714910, 9.1372190)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(38.9694533, 8.9705068)));

        // ESSO
        pin = drawableToIcon(getActivity(), R.drawable.ic_q8_logo);
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2817382, 9.0319635)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2516385, 9.1034724)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2422696, 9.1714671)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2335950, 9.1113870)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2421905, 9.1269659)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2168302, 9.1254600)));
        mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(new LatLng(39.2370500, 9.1249250)));



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
            //mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 11));
        });

        currentPositionButton = getView().findViewById(R.id.currentPositionButton);
        currentPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMapPosition();
            }
        });

        // inizializzazione marker
        //currentPositionMarker = mapboxMap.addMarker(new MarkerOptions());
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