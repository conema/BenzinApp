package me.conema.benzinapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;

public class SyncedCar extends AppCompatActivity {
    EditText carText, roadText, fuelText, fuelEditText;
    ImageButton imgPicker;
    Button saveButton;
    Button deleteButton;
    TextView syncText, verText;

    Drawable carImg;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int OPEN_DOCUMENT_CODE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synced_car);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        TextView mTitle = myToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Aggiungi auto");
        mTitle.setText(myToolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);

        //Serve per visualizzare il tasto indietro
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        carText = findViewById(R.id.carEditText);
        fuelText = findViewById(R.id.fuelEditText);
        roadText = findViewById(R.id.roadEditText);
        fuelEditText = findViewById(R.id.fuelEditText);
        imgPicker = findViewById(R.id.imgButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        syncText = findViewById(R.id.syncText);
        verText = findViewById(R.id.verText);

        boolean sync = true;

        final CarFactory factory = CarFactory.getInstance();

        //Controllo il parametro passato (se false è un auto nuova se true è un auto sincronizzata)
        Intent intent = getIntent();
        Bundle obj = intent.getExtras();

        if(obj != null)
            sync = obj.getBoolean("sync");


        if(sync) {
            Car car = new Car("Fiat punto", 45, 17, null, Color.parseColor("#121212"), 70, null, 56);
            carText.setText(car.getName());
            fuelText.setText("56");
            roadText.setText(Integer.toString(car.getKmDone()));
        }
        else{
            syncText.setText("Inserisci i dati della tua auto:");
            syncText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            syncText.setTextSize(20);
            verText.setVisibility(View.GONE);
            carText.setText("");
            fuelText.setText("");
            roadText.setText("");
        }

        imgPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[] {"Scatta foto", "Scegli dalla galleria"};

                AlertDialog.Builder builder = new AlertDialog.Builder(SyncedCar.this);
                builder.setCancelable(false);
                builder.setTitle("Seleziona un'opzione:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                chooseFromGallery();
                                break;

                        }

                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the user clicked on Cancel
                    }
                });
                builder.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carText.getText().toString().equals(""))
                    carText.setError("Il campo non può essere vuoto");
                if (roadText.getText().toString().equals(""))
                    roadText.setError("Il campo non può essere vuoto");
                if (fuelEditText.getText().toString().equals(""))
                    fuelEditText.setError("Il campo non può essere vuoto");
                else {
                    String carName = carText.getText().toString();
                    int kmDone = Integer.parseInt(roadText.getText().toString());
                    int tankCapacity = Integer.parseInt(fuelEditText.getText().toString());
                    Car car = new Car(carName, kmDone, 23, null, Color.parseColor("#c6b6b6"), 80, carImg, tankCapacity);

                    if (carImg != null) {
                        factory.addCar(car);
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), "Auto aggiunta con successo!", Toast.LENGTH_SHORT).show();

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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //New bitmapdrawable
            carImg = new BitmapDrawable(getResources(), imageBitmap);

            //Setting that bitmapdrawable as the icon in the xml
            imgPicker.setImageDrawable(carImg);
        }

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

    public void chooseFromGallery(){
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

    public void takePhoto(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(SyncedCar.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SyncedCar.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Context context = getApplicationContext();
                CharSequence text = "L'app ha bisogno di questo permesso per poter funzionare";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                ActivityCompat.requestPermissions(SyncedCar.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_IMAGE_CAPTURE);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(SyncedCar.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_IMAGE_CAPTURE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
