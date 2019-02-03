package me.conema.benzinapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CarListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageButton refreshButton;
    Toolbar toolbar;

    private OnFragmentInteractionListener mListener;

    public CarListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarListFragment newInstance(String param1, String param2) {
        CarListFragment fragment = new CarListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Serve per visualizzare il tasto indietro
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_car_list, container, false);
        updateCarList(view);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshButton = getView().findViewById(R.id.refreshButton);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onStart() {
        super.onStart();
        updateCarList(getView());
        Log.i("controllo", "OnStart ");
    }

    //Aggiunge il bottone "+" alla toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void updateCarList(View viewParent) {
        CarFactory carFactory = CarFactory.getInstance();
        ArrayList<Car> carArrayList = carFactory.getCars();
        LinearLayout container = viewParent.findViewById(R.id.linearLayoutCarList_fragment);

        //Empty views (old cars)
        container.removeAllViews();

        //App app = AppFactory.getInstance().getApp();

        if (carArrayList.size() == 0) {
            View view = getLayoutInflater().from(getActivity()).inflate(R.layout.no_car_layout, container, false);
            container.addView(view);
        } else {
            for (Car car : carArrayList) {
                //View view = getLayoutInflater().from(this).inflate(R.layout.car_list_relative_layout, container, false);
                View view = getLayoutInflater().from(getActivity()).inflate(R.layout.car_list_relative_layout, container, false);
                ImageButton refreshButton = view.findViewById(R.id.refreshButton);
                TextView carNome = view.findViewById(R.id.carNome);
                carNome.setText(car.getName());
                ProgressBar progressBar = view.findViewById(R.id.progressBar);
                ImageView carImgView = view.findViewById(R.id.carImg);

                updateProgressBarColor(progressBar, car);

                TextView kmView = view.findViewById(R.id.kmTextView);
                kmView.setText("Km residui " + car.getKmDone());
                carImgView.setImageDrawable(car.getPhoto());
                Log.i("controllo", " Questo è l'id della car " + car.getId());

                /* Listener del refresh button */
                refreshButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int min = 0;
                        int max = 100;
                        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
                        ProgressBar progressBar = view.findViewById(R.id.progressBar);
                        progressBar.setProgress(randomNum, true);
                        car.setPercTank(randomNum);
                        updateProgressBarColor(progressBar, car);

                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleCar = new Intent(getActivity(), SingleCar.class);
                        singleCar.putExtra("carId", car.getId());
                        //TODO cambiare all'activity di Fra
                        startActivity(singleCar);
                    }
                });
                container.addView(view);

            }
        }
    }

    /* Questa funzione aggiorna il colore della progress bar dell'auto in base alla
    percentuale del carburante rimanente
     */
    public void updateProgressBarColor(ProgressBar progressBar, Car car) {

        if (car.getPercTank() >= 60) {
            Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
            progressBar.setProgressDrawable(progressDrawable);
            progressBar.setProgress(car.getPercTank(), true);
        } else if (car.getPercTank() >= 30) {
            Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.parseColor("#ffff4c"), android.graphics.PorterDuff.Mode.SRC_IN);
            progressBar.setProgressDrawable(progressDrawable);
            progressBar.setProgress(car.getPercTank(), true);
        } else {
            Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            progressBar.setProgressDrawable(progressDrawable);
            progressBar.setProgress(car.getPercTank(), true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addCar:
                Intent syncedCar = new Intent(getActivity(), SyncedCar.class);
                syncedCar.putExtra("sync", false);
                startActivity(syncedCar);
                /*Intent station = new Intent(getActivity(), SingleStation.class);
                station.putExtra("stationId", 0);
                startActivity(station);*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
