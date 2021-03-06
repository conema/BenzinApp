package me.conema.benzinapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.conema.benzinapp.classes.AppFactory;
import me.conema.benzinapp.classes.Station;
import me.conema.benzinapp.classes.StationFactory;

public class StationsFragment extends Fragment implements LocationListener, SearchView.OnQueryTextListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String CIRCLE_LAYER_ID = "circle_layer";
    private static int CIRCLE_MUL = 500;
    private static double METERS_CONV = 1000.00;

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
    private LatLng currentSelectedPosition;
    private LatLng currentUserLatLng;

    // interfaccia sopra la mappa
    private LinearLayout stationsLinearLayout;
    private Station.ComparationType selectedType;
    private double gridLayoutWidth;

    // resto dell'interfaccia
    FloatingActionButton currentPositionButton;
    FloatingActionButton closeInfoButton;
    Spinner orderBySpinner;
    HorizontalScrollView hsv;

    TextView toolbar_title;
    SearchView searchView;
    MenuItem searchMenuItem;

    LinearLayout infoMaps;
    SeekBar circleDim;
    TextView seekKm;
    LinearLayout notFoundStations;
    LatLng realUserPosition;

    //Variabile statica per memorizzare LatLong ultimo click
    private static LatLng lastClick;

    private HashMap<LatLng, Short> stationNumberHashMap;


    @SuppressLint("MissingPermission")
    private void updateMapPosition() {

        locationManager.getLastKnownLocation(locationProvider);
        locationManager.requestLocationUpdates(locationProvider, 5000, (float) 2.0, this);

        currentLocation = locationManager.getLastKnownLocation(locationProvider);

        if (currentLocation != null) {
            currentUserLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        } else {
            currentUserLatLng = new LatLng(39.222487, 9.114134);
        }

        realUserPosition = new LatLng(currentUserLatLng);

        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLng, 14));

        printStations();
    }

    public void printStations() {
        short markerCounter = 0;
        stationNumberHashMap.clear();
        mapboxMap.removeAnnotations();

        mapboxMap.addMarker(currentPositionMarker
                .icon(drawableToIcon(getActivity(), R.drawable.ic_navigation_black_24dp))
                .setPosition(realUserPosition));

        currentSelectedPosition = currentUserLatLng;

        hsv = getView().findViewById(R.id.stationsScrollView);
        stationsLinearLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Icon pin;
        boolean thereAreStations = false;
        LinearLayout notFoundStations = getView().findViewById(R.id.notFoundStations);

        Station station;
        double distance;

        HashMap<LatLng, Station> stations = stationFactory.getStations();
        List<Pair<Station, Double>> stationDistancePairs = new ArrayList<>(stations.values().size());

        for (Station s : stations.values()) {
            stationDistancePairs.add(Pair.create(s, s.getPosition().distanceTo(currentSelectedPosition)));
        }

        Collections.sort(stationDistancePairs, Station.getComparator(selectedType));

        for (LatLng currentKey : StationFactory.getInstance().getStations().keySet()) {
            station = stationFactory.getStations().get(currentKey);
            distance = station.getPosition().distanceTo(currentUserLatLng);
            if (distance <= getPerimeterSeekBar() * METERS_CONV) {
                markerCounter++;
                stationNumberHashMap.put(station.getPosition(), markerCounter);
                // TODO lavorare sul pin che segue
                View view = inflater.inflate(R.layout.station_pin, null, false);
                ((ImageView)view.findViewById(R.id.station_pin_logo)).setImageResource(stationFactory.getStations().get(currentKey).getImg());
                ((TextView)view.findViewById(R.id.station_number)).setText(Integer.toString(markerCounter));
                ((TextView) view.findViewById(R.id.station_number)).setShadowLayer(1.5f, -1, 1, Color.WHITE);
                ((TextView) view.findViewById(R.id.station_number)).setTextColor(Color.DKGRAY);
                pin = bitmapToIcon(getActivity(), createBitmapFromView(view));

                mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(stationFactory.getStations().get(currentKey).getPosition()));
                thereAreStations = true;
            }
        }

        if (!thereAreStations) {
            notFoundStations.setVisibility(View.VISIBLE);
        } else if (notFoundStations.getVisibility() == View.VISIBLE) {
            notFoundStations.setVisibility(View.GONE);
        }

        if (lastClick == null) {
            generateCircle(currentSelectedPosition, currentUserLatLng);
        }

        // si crea la roba che sta sotto
        hsv = getView().findViewById(R.id.stationsScrollView);
        stationsLinearLayout.removeAllViews();

        DecimalFormat df = new DecimalFormat("##.###");
        DecimalFormat df_km = new DecimalFormat("##.#");
        DecimalFormat df_m = new DecimalFormat("##");
        df.setRoundingMode(RoundingMode.DOWN);
        df_km.setRoundingMode(RoundingMode.DOWN);
        df_m.setRoundingMode(RoundingMode.DOWN);



        for(Pair<Station, Double> stationDoublePair : stationDistancePairs) {
            if (stationDoublePair.second < (getPerimeterSeekBar() * METERS_CONV)) {
                GridLayout gridLayout = (GridLayout) inflater.inflate(R.layout.station_grid_layout, stationsLinearLayout, false);
                ((ImageView) gridLayout.getChildAt(0)).setImageResource(stationDoublePair.first.getImg());

                LinearLayout currentLinearLayout = (LinearLayout) ((LinearLayout) gridLayout.getChildAt(1)).getChildAt(0);
                ((TextView) currentLinearLayout.getChildAt(1)).setText(stationDoublePair.first.getMark() + "/5");

                currentLinearLayout = (LinearLayout) ((LinearLayout) gridLayout.getChildAt(1)).getChildAt(1);
                if (stationDoublePair.second >= 1000) {
                    ((TextView) currentLinearLayout.getChildAt(1)).setText(df_km.format(stationDoublePair.second / 1000.0) + " Km");
                } else {
                    ((TextView) currentLinearLayout.getChildAt(1)).setText(df_m.format(stationDoublePair.second) + " m");
                }

                ((TextView) gridLayout.getChildAt(2)).setText(Short.toString(stationNumberHashMap.get(stationDoublePair.first.getPosition())));

                ((TextView) gridLayout.getChildAt(4)).setText(df.format(stationDoublePair.first.getPrice()) + " €/L");

                gridLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent stationActivity = new Intent(getActivity(), SingleStation.class);
                        stationActivity.putExtra("stationId", stationDoublePair.first.getId());
                        AppFactory.getInstance().getApp().pushLastStation(StationFactory.getInstance().getStationById(stationDoublePair.first.getId()));
                        startActivity(stationActivity);
                    }
                });
                stationsLinearLayout.addView(gridLayout);
                gridLayoutWidth = gridLayout.getWidth();
            }
        }

        hsv.smoothScrollTo(0, hsv.getBottom());
    }


    public Bitmap createBitmapFromView(View v) {
        v.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return bitmap;
    }

    public static Icon drawableToIcon(@NonNull Context context, @DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, context.getTheme());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

        vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }

    public static Icon bitmapToIcon(@NonNull Context context, Bitmap bmp) {
        Canvas canvas = new Canvas(bmp);
        //vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

        //vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bmp);
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

        stationNumberHashMap = new HashMap<>();

        updateMapPosition();


        // si aggiunge un livello per il disegno dei cerchi
        mapboxMap.addSource(new GeoJsonSource("near_stations"));
        CircleLayer layer = new CircleLayer(CIRCLE_LAYER_ID, "near_stations");
        layer.withProperties(
                PropertyFactory.circleRadius(10f),
                PropertyFactory.circleOpacity(.4f),
                PropertyFactory.circleColor(Color.CYAN)
        );

        mapboxMap.addLayer(layer);


        // si setta listener sui marker
        mapboxMap.setOnMarkerClickListener(marker -> {
            Intent stationActivity = new Intent(getActivity(), SingleStation.class);
            stationActivity.putExtra("stationId", stationFactory.getStations().get(marker.getPosition()).getId());
            AppFactory.getInstance().getApp().pushLastStation(StationFactory.getInstance().getStationById(stationFactory.getStations().get(marker.getPosition()).getId()));
            startActivity(stationActivity);
            return true;
        });

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                Log.d("PUNTO CLICCATO: ", point.toString());
                Log.d("Posizione selezionata: ", currentSelectedPosition.toString());
                generateCircle(currentSelectedPosition, point);
            }
        });
    }

    private void generateCircle(LatLng currentSelectedPosition, LatLng point) {
        currentSelectedPosition.setLatitude(point.getLatitude());
        currentSelectedPosition.setLongitude(point.getLongitude());

        lastClick = new LatLng(point.getLatitude(), point.getLongitude());

        printStations();

        /* hardcoded da far schifo
        mapboxMap.removeLayer(CIRCLE_LAYER_ID);
        if (mapboxMap.getPolygons().size() != 0) {
            mapboxMap.removePolygon(mapboxMap.getPolygons().get(0));
        }*/
        mapboxMap.addPolygon(generatePerimeter(currentSelectedPosition, getPerimeterSeekBar(), 64));

    }

    private double getPerimeterSeekBar() {
        //(0+1*400) 400 min distance
        return ((circleDim.getProgress() + 1) * CIRCLE_MUL) / METERS_CONV;
    }

    private PolygonOptions generatePerimeter(LatLng centerCoordinates, double radiusInKilometers, int numberOfSides) {
        List<LatLng> positions = new ArrayList<>();
        double distanceX = radiusInKilometers / (111.319 * Math.cos(centerCoordinates.getLatitude() * Math.PI / 180));
        double distanceY = radiusInKilometers / 110.574;

        double slice = (2 * Math.PI) / numberOfSides;

        double theta;
        double x;
        double y;
        LatLng position;
        for (int i = 0; i < numberOfSides; ++i) {
            theta = i * slice;
            x = distanceX * Math.cos(theta);
            y = distanceY * Math.sin(theta);

            position = new LatLng(centerCoordinates.getLatitude() + y,
                    centerCoordinates.getLongitude() + x);
            positions.add(position);
        }
        return new PolygonOptions()
                .addAll(positions)
                .fillColor(Color.parseColor("#0094CF"))
                .alpha(0.4f);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stations, container, false);
        setHasOptionsMenu(true);

        toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkLocationPermission();
        circleDim = getView().findViewById(R.id.circleDim);

        mapView = getView().findViewById(R.id.mapquestMapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(parMapboxMap -> {
            mapboxMap = parMapboxMap;
            mapView.setStreetMode();
            initialize();
        });

        currentPositionButton = getView().findViewById(R.id.currentPositionButton);
        currentPositionButton.setOnClickListener(view1 -> {
            updateMapPosition();
            generateCircle(currentSelectedPosition, currentUserLatLng);
        });

        // gestione linear layout delle stazioni
        stationsLinearLayout = getView().findViewById(R.id.stationsLinearLayout);

        orderBySpinner = getView().findViewById(R.id.orderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_order, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        orderBySpinner.setPrompt("Ordina per");
        orderBySpinner.setAdapter(adapter);

        orderBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType = Station.ComparationType.values()[i];
                if (mapboxMap != null) {
                    //showStationsInfo(currentSelectedPosition, getPerimeterSeekBar());
                    printStations();
                    generateCircle(currentSelectedPosition, lastClick);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Close info maps button
        hsv = getView().findViewById(R.id.stationsScrollView);
        infoMaps = getView().findViewById(R.id.infoMaps);

        closeInfoButton = getView().findViewById(R.id.closeInfoButton);
        closeInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoMaps.getVisibility() == View.GONE) {
                    infoMaps.setVisibility(View.VISIBLE);
                    hsv.setVisibility(View.VISIBLE);
                    closeInfoButton.setImageResource(android.R.drawable.arrow_down_float);
                } else {
                    infoMaps.setVisibility(View.GONE);
                    hsv.setVisibility(View.GONE);
                    closeInfoButton.setImageResource(android.R.drawable.arrow_up_float);
                }
            }
        });

        seekKm = view.findViewById(R.id.seekKm);
        circleDim.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    generateCircle(currentSelectedPosition, lastClick);

                    if (getPerimeterSeekBar() * METERS_CONV <= METERS_CONV) {
                        seekKm.setText(getPerimeterSeekBar() * METERS_CONV + "M");
                    } else {
                        seekKm.setText(getPerimeterSeekBar() + "KM");
                    }

                } catch (NullPointerException ex) {
                    //lastClick non dovrebbe mai essere null
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION))
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


        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Cerca città...");

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                Cursor cursor = searchView.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(i);

                Address address = CitySearchProvider.getLoc((cursor.getString(1)), getContext());

                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 12));


                generateCircle(currentSelectedPosition, new LatLng(address.getLatitude(), address.getLongitude()));
                searchMenuItem.collapseActionView();
                searchView.setQuery("", false);
                return false;
            }
        });

        MenuItem icon = menu.getItem(0);
        icon.setIcon(search);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchMenuItem.collapseActionView();
        searchView.setQuery("", false);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}