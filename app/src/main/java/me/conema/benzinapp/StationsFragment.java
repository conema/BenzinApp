package me.conema.benzinapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import android.support.v7.widget.GridLayout;
import android.util.Pair;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.support.v7.widget.GridLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.conema.benzinapp.classes.App;
import me.conema.benzinapp.classes.AppFactory;
import me.conema.benzinapp.classes.Station;
import me.conema.benzinapp.classes.StationFactory;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.conema.benzinapp.classes.Station;
import me.conema.benzinapp.classes.StationFactory;

public class StationsFragment extends Fragment implements LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    // gestione per trovare locazione corrente
    private LocationManager locationManager;
    private Location currentLocation;
    private String locationProvider;
    private Criteria criteria;
    private StationFactory stationFactory;


    // gestione mappa
    private MapView mapView;
    private MapboxMap mapboxMap;
    private MarkerOptions currentPositionMarker;

    // interfaccia sopra la mappa
    private LinearLayout stationsLinearLayout;

    // resto dell'interfaccia
    FloatingActionButton currentPositionButton;

    TextView toolbar_title;
    EditText toolbar_searchbox;

    @SuppressLint("MissingPermission")
    private void updateMapPosition() {
        LatLng currentLatLng;
        locationManager.getLastKnownLocation(locationProvider);
        locationManager.requestLocationUpdates(locationProvider, 5000, (float) 2.0, this);

        currentLocation = locationManager.getLastKnownLocation(locationProvider);

        if (currentLocation != null) {
            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        } else {
            currentLatLng = new LatLng(39.222487, 9.114134);
        }

        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));

        mapboxMap.removeAnnotations();

        mapboxMap.addMarker(currentPositionMarker
                .icon(drawableToIcon(getActivity(), R.drawable.ic_navigation_black_24dp))
                .setPosition(currentLatLng));

        Icon pin;
        for(LatLng currentKey : StationFactory.getInstance().getStations().keySet()) {
            pin = drawableToIcon(getActivity(), StationFactory.getInstance().getStations().get(currentKey).getImg());
            mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(StationFactory.getInstance().getStations().get(currentKey).getPosition()));
        }

        showStationsInfo(currentLatLng, 5000);
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

    /**
     * Mostra le stazioni
     * @param maxDistance massima distanza in metri.
     */
    private void showStationsInfo(LatLng position, double maxDistance) {
        HorizontalScrollView hsv = getView().findViewById(R.id.stationsScrollView);
        stationsLinearLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DecimalFormat df = new DecimalFormat("##.###");
        df.setRoundingMode(RoundingMode.DOWN);

        HashMap<LatLng, Station> stations = stationFactory.getStations();
        List<Pair<Station, Double>> stationDistancePairs = new ArrayList<>(stations.values().size());

        for(Station station : stations.values()) {
            stationDistancePairs.add(Pair.create(station, station.getPosition().distanceTo(position)));
        }
        //Collections.sort(stationDistancePairs, (stationDoublePair, t1) -> (int) (stationDoublePair.second - t1.second));
        Collections.sort(stationDistancePairs, Station.getComparator(Station.ComparationType.DISTANCE));

        for(Pair<Station, Double> stationDoublePair : stationDistancePairs) {
            if(stationDoublePair.second < maxDistance) {
                GridLayout gridLayout = (GridLayout) inflater.inflate(R.layout.station_grid_layout, stationsLinearLayout, false);
                ((ImageView)gridLayout.getChildAt(0)).setImageResource(stationDoublePair.first.getImg());

                LinearLayout currentLinearLayout = (LinearLayout) ((LinearLayout)gridLayout.getChildAt(1)).getChildAt(0);
                ((TextView)currentLinearLayout.getChildAt(1)).setText(stationDoublePair.first.getMark() + "/5");

                currentLinearLayout = (LinearLayout) ((LinearLayout)gridLayout.getChildAt(1)).getChildAt(1);
                if (stationDoublePair.second >= 1000) {
                    ((TextView)currentLinearLayout.getChildAt(1)).setText(df.format(stationDoublePair.second / 1000.0) + " Km");
                } else {
                    ((TextView)currentLinearLayout.getChildAt(1)).setText(df.format(stationDoublePair.second) + " m");
                }

                ((TextView) gridLayout.getChildAt(2)).setText(stationDoublePair.first.getName());

                ((TextView) gridLayout.getChildAt(3)).setText(df.format(stationDoublePair.first.getPrice()) + " €/L");

                gridLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent stationActivity = new Intent(getActivity(), SingleStation.class);
                        stationActivity.putExtra("stationId", stationDoublePair.first.getId());
                        startActivity(stationActivity);
                    }
                });
                stationsLinearLayout.addView(gridLayout);
            }
        }

        hsv.scrollTo(0, hsv.getBottom());
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
        mapboxMap.setOnMarkerClickListener(marker -> {
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), mapboxMap.getCameraPosition().zoom));
            showStationsInfo(marker.getPosition(), 5000);
            return true;
        });

        //Aggiunta stazioni
        Icon pin;
        for(LatLng currentKey : StationFactory.getInstance().getStations().keySet()) {
            pin = drawableToIcon(getActivity(), StationFactory.getInstance().getStations().get(currentKey).getImg());
            mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(StationFactory.getInstance().getStations().get(currentKey).getPosition()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stations, container, false);
        setHasOptionsMenu(true);

        toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_searchbox = getActivity().findViewById(R.id.toolbar_searchbox);
        return v;
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


        // gestione linear layout delle stazioni
        stationsLinearLayout = getView().findViewById(R.id.stationsLinearLayout);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuest.start(getActivity().getApplicationContext());

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
        toolbar_searchbox.setVisibility(View.GONE);
        toolbar_title.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

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


    //Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Drawable search = getResources().getDrawable(android.R.drawable.ic_menu_search).mutate();
        search.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);

        inflater.inflate(R.menu.menu_favorite, menu);
        MenuItem icon = menu.getItem(0);
        icon.setIcon(search);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favoriteStation:
                checkKey(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkKey(MenuItem item) {
        toolbar_searchbox.setVisibility(View.VISIBLE);
        toolbar_title.setVisibility(View.GONE);

        toolbar_searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Qua gestione ricerca
                Toast.makeText(getActivity(), toolbar_searchbox.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}