package me.conema.benzinapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;

import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;

public class SyncedCar extends AppCompatActivity {
    EditText carText, roadText, fuelText;
    ImageButton imgPicker;
    Button saveButton;
    Button deleteButton;

    Drawable carImg;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int OPEN_DOCUMENT_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synced_car);
        carText = (EditText) findViewById(R.id.carEditText);
        fuelText = (EditText) findViewById(R.id.fuelEditText);
        roadText = (EditText) findViewById(R.id.roadEditText);
        imgPicker = findViewById(R.id.imgButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        final CarFactory factory = CarFactory.getInstance();


        Car car = new Car("Fiat punto", 55000, 17, null, Color.parseColor("#121212"), 70, null);
        carText.setText(car.getName());
        fuelText.setText("5");
        roadText.setText(Integer.toString(car.getKmDone()));

        imgPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(SyncedCar.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SyncedCar.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Context context = getApplicationContext();
                        CharSequence text = "L'app ha bisogno di questo permesso per poter funzionare";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        ActivityCompat.requestPermissions(SyncedCar.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PICK_FROM_GALLERY);
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(SyncedCar.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PICK_FROM_GALLERY);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, OPEN_DOCUMENT_CODE);

                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carName = carText.getText().toString();
                int kmDone = Integer.parseInt(roadText.getText().toString());
                Car car = new Car(carName, kmDone, 23, null, 123, 80, carImg);
                if (carImg != null) {
                    factory.addCar(car);
                    Intent home = new Intent(SyncedCar.this, Home.class);
                    startActivity(home);


                }
                //If the user doesn't select an img for the car show an error msg
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Seleziona un'immagine per l'auto";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(SyncedCar.this, Home.class);
                startActivity(home);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == OPEN_DOCUMENT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a photo.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
                try {
                    Uri photoUri = data.getData();
                    Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

                    //Resize of the image to better fit the height and width

                    //New bitmapdrawable
                    carImg = new BitmapDrawable(getResources(), bitmapImg);

                    //Setting that bitmapdrawable as the icon in the xml
                    imgPicker.setImageDrawable(carImg);
                } catch (Exception e) {
                    Log.i("controllo", "( ͡° ͜ʖ ͡°) ");
                }

            }
        }
    }
}
