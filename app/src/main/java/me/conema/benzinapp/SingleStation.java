package me.conema.benzinapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.util.ArrayList;

import me.conema.benzinapp.classes.App;
import me.conema.benzinapp.classes.AppFactory;
import me.conema.benzinapp.classes.Review;
import me.conema.benzinapp.classes.ReviewFactory;
import me.conema.benzinapp.classes.Station;
import me.conema.benzinapp.classes.StationFactory;
import timber.log.Timber;

public class SingleStation extends AppCompatActivity implements LocationListener {
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
    FloatingActionButton currentPositionButton;


    ImageView imgStazione;
    TextView viaStazione, prezzoStazione, votoStazione, addressView, gpsDistance;
    RelativeLayout addressLayout;
    int idStation = -1;

    TextView noRewiew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapQuest.start(this.getApplicationContext());

        setContentView(R.layout.activity_single_station);
        imgStazione = findViewById(R.id.imgStazione);
        viaStazione = findViewById(R.id.viaStatione);
        prezzoStazione = findViewById(R.id.prezzoStazione);
        votoStazione = findViewById(R.id.votoStazione);
        addressView = findViewById(R.id.addressView);
        addressLayout = findViewById(R.id.addressLayout);
        gpsDistance = findViewById(R.id.gpsDistance);
        noRewiew = findViewById(R.id.noReview);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        TextView mTitle = myToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(myToolbar);
        mTitle.setText(myToolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);

        //Serve per visualizzare il tasto indietro
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        StationFactory stationFactory = StationFactory.getInstance();

        Intent intent = getIntent();
        Bundle obj = intent.getExtras();

        if (obj != null)
            idStation = obj.getInt("stationId");

        Station station = stationFactory.getStationById(idStation);

        imgStazione.setImageResource(station.getImg());
        viaStazione.setText(station.getAddress());
        addressView.setText(station.getAddress());
        prezzoStazione.setText(Double.toString(station.getPrice()).substring(0, 5) + "€");
        votoStazione.setText(Double.toString(station.getMark()) + "/5");
        getSupportActionBar().setTitle(station.getName());
        mTitle.setText(station.getName());
        updateReviewList(idStation);


        checkLocationPermission();
        mapView = findViewById(R.id.mapquestMapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(parMapboxMap -> {
            mapboxMap = parMapboxMap;
            mapView.setStreetMode();
            initialize();
        });

        currentPositionButton = findViewById(R.id.currentPositionButton);
        currentPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNavigation();
            }
        });

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNavigation();
            }
        });

    }

    private void startNavigation() {
        LatLng pos = StationFactory.getInstance().getStationById(idStation).getPosition();
        double lat = pos.getLatitude();
        double longit = pos.getLongitude();
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + longit);

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
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

    @Override
    protected void onResume() {
        super.onResume();
        updateReviewList(idStation);
        mapView.onResume();
    }

    private void updateReviewList(int idStazione) {
        ReviewFactory reviewFactory = ReviewFactory.getInstance();
        ArrayList<Review> reviewArrayList = reviewFactory.getReviewByStation(idStazione);
        LinearLayout reviewContainer = findViewById(R.id.container);

        reviewContainer.removeAllViews();

        boolean thereAreReview = false;

        for (Review review : reviewArrayList) {
            View view = getLayoutInflater().from(this).inflate(R.layout.review_linear_layout, reviewContainer, false);
            TextView nomeReview = view.findViewById(R.id.nomeReview);
            nomeReview.setText(review.getName());
            TextView votoReview = view.findViewById(R.id.votoReview);
            votoReview.setText(Double.toString(review.getVote()) + "/5");
            TextView testoReview = view.findViewById(R.id.testoReview);
            testoReview.setText(review.getDescription());
            reviewContainer.addView(view);

            thereAreReview = true;
        }

        if (!thereAreReview) {
            noRewiew.setVisibility(View.VISIBLE);
        } else {
            noRewiew.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        MenuItem icon = menu.getItem(0);
        AppFactory appFactory = AppFactory.getInstance();
        App app = appFactory.getApp();
        if (app.getFavStation() != null && app.getFavStation().getId() == idStation) {
            icon.setIcon(R.drawable.ic_favorite_red_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favoriteStation:
                checkFavStation(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkFavStation(MenuItem item) {
        AppFactory appFactory = AppFactory.getInstance();
        App app = appFactory.getApp();
        if (app.getFavStation() == null) {
            StationFactory stationFactory = StationFactory.getInstance();
            Station station = stationFactory.getStationById(idStation);
            app.setFavStation(station);

            Toast.makeText(this, "Stazione aggiunta alle preferite", Toast.LENGTH_SHORT).show();
            item.setIcon(R.drawable.ic_favorite_red_24dp);
        } else {
            if (app.getFavStation().getId() == idStation) {
                StationFactory stationFactory = StationFactory.getInstance();
                Station station = stationFactory.getStationById(idStation);
                app.setFavStation(null);

                Toast.makeText(this, "Stazione rimossa dalle preferite", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleStation.this);
                builder.setTitle("Hai già una stazione preferita, vuoi sostituirla con questa?")
                        .setPositiveButton("sì", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                StationFactory stationFactory = StationFactory.getInstance();
                                Station station = stationFactory.getStationById(idStation);
                                app.setFavStation(station);
                                item.setIcon(R.drawable.ic_favorite_red_24dp);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }


    //Maps

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
    private void updateMapPosition() {
        LatLng currentLatLng;
        locationManager.getLastKnownLocation(locationProvider);
        locationManager.requestLocationUpdates(locationProvider, 5000, (float) 2.0, this);

        currentLocation = locationManager.getLastKnownLocation(locationProvider);

        if (currentLocation != null) {
            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            Timber.i("Location:" + String.valueOf(currentLocation.getLatitude()) + " " + String.valueOf(currentLocation.getLongitude()));
        } else {
            currentLatLng = new LatLng(39.222487, 9.114134);
        }

        //Create distance text
        int distance = (int) currentLatLng.distanceTo(StationFactory.getInstance().getStationById(idStation).getPosition());
        if (distance >= 1000) {
            gpsDistance.setText(distance / 1000 + " KM");
        } else {
            gpsDistance.setText(distance + " M");
        }

        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(StationFactory.getInstance().getStationById(idStation).getPosition()), 15));
        Icon pin;
        for (LatLng currentKey : StationFactory.getInstance().getStations().keySet()) {
            pin = drawableToIcon(SingleStation.this, StationFactory.getInstance().getStations().get(currentKey).getImg());
            mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(StationFactory.getInstance().getStations().get(currentKey).getPosition()));
        }

    }

    @SuppressLint("MissingPermission")
    private void initialize() {
        stationFactory = StationFactory.getInstance();
        locationManager = (LocationManager) SingleStation.this.getSystemService(Context.LOCATION_SERVICE);

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
                startNavigation();
                return true;
            }
        });

        // aggiunta stazioni di servizio
        /*Icon pin;
        for(LatLng currentKey : StationFactory.getInstance().getStations().keySet()) {
            pin = drawableToIcon(getActivity(), StationFactory.getInstance().getStations().get(currentKey).getImg());
            mapboxMap.addMarker(currentPositionMarker.icon(pin).setPosition(StationFactory.getInstance().getStations().get(currentKey).getPosition()));
        }*/
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


    // gestione permessi
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(SingleStation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(SingleStation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
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
                    if (ContextCompat.checkSelfPermission(SingleStation.this,
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
