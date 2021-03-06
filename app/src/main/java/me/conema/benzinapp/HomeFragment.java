package me.conema.benzinapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Stack;

import me.conema.benzinapp.classes.App;
import me.conema.benzinapp.classes.AppFactory;
import me.conema.benzinapp.classes.Car;
import me.conema.benzinapp.classes.CarFactory;
import me.conema.benzinapp.classes.Station;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private App app = AppFactory.getInstance().getApp();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        //Back button
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView statsFromDate = view.findViewById(R.id.stats_from_date);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        statsFromDate.setText(df.format(app.getLastSync()));


        Button addCar = view.findViewById(R.id.add_car);
        addCar.setOnClickListener(v -> {
            Intent addCar1 = new Intent(getActivity(), CarSync.class);
            startActivity(addCar1);
        });

        Button favCar = view.findViewById(R.id.fav_car);
        Button favStation = view.findViewById(R.id.fav_station);

        //Favourites
        favCar.setOnClickListener(v -> {
            Car car = AppFactory.getInstance().getApp().getFavCar();

            if (car == null) {
                Toast.makeText(getActivity(), "Non è presente un'auto preferita.", Toast.LENGTH_SHORT).show();
            } else {
                Intent carActivity = new Intent(getActivity(), SingleCar.class);
                carActivity.putExtra("carId", car.getId());
                startActivity(carActivity);
            }
        });

        favStation.setOnClickListener(v -> {
            Station station = AppFactory.getInstance().getApp().getFavStation();

            if (station == null) {
                Toast.makeText(getActivity(), "Non è presente una stazione preferita.", Toast.LENGTH_SHORT).show();
            } else {
                Intent stationActivity = new Intent(getActivity(), SingleStation.class);
                stationActivity.putExtra("stationId", station.getId());
                startActivity(stationActivity);
            }
        });

        updateLastStations(view);
        updateCarStats(view);

        return view;
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

    private void updateLastStations(View viewParent) {
        //Last stations
        GridLayout lastStations = viewParent.findViewById(R.id.last_stations_grid);
        DecimalFormat df = new DecimalFormat("##.###");
        df.setRoundingMode(RoundingMode.DOWN);

        //Delete child view (old station)
        lastStations.removeAllViews();

        App app = AppFactory.getInstance().getApp();

        Stack<Station> stations = new Stack<>();
        stations.addAll(app.getLastStations());

        while (!stations.empty()) {
            View view = getLayoutInflater().from(getActivity()).inflate(R.layout.last_station, lastStations, false);

            Station station = stations.pop();

            ImageView stationImg = view.findViewById(R.id.last_station_img);
            stationImg.setImageResource(station.getImg());

            TextView stationName = view.findViewById(R.id.last_station_name);
            stationName.setText(station.getName());

            TextView stationPrice = view.findViewById(R.id.last_station_price);
            stationPrice.setText(df.format(station.getPrice()) + "");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent singleStation = new Intent(getActivity(), SingleStation.class);
                    singleStation.putExtra("stationId", station.getId());
                    startActivity(singleStation);
                }
            });

            lastStations.addView(view);
        }
    }

    private void updateCarStats(View viewParent) {
        ArrayList<Car> cars = CarFactory.getInstance().getCars();

        LinearLayout statsList = viewParent.findViewById(R.id.stats_list);
        com.nex3z.flowlayout.FlowLayout statsCircles = viewParent.findViewById(R.id.stats_circles);

        //Delete child view (old line and circle stats)
        statsList.removeAllViews();
        statsCircles.removeAllViews();

        //Circle total
        View view = getLayoutInflater().from(getActivity()).inflate(R.layout.car_circle, statsCircles, false);

        TextView circleName = view.findViewById(R.id.stats_circle_name);
        circleName.setText("Totale");

        ImageView circle = view.findViewById(R.id.stats_circle);
        circle.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);

        statsCircles.addView(view);

        //Line total
        view = getLayoutInflater().from(getActivity()).inflate(R.layout.car_stats, statsList, false);
        ProgressBar progressBar = view.findViewById(R.id.stats_progressbar);
        progressBar.setProgress(100);           //total is at 100%
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLACK));

        int totalL = CarFactory.getInstance().getTotalL();

        TextView statsL = view.findViewById(R.id.stats_l);
        statsL.setText(totalL + "L");

        statsList.addView(view);

        for (Car car : cars) {
            //Circle
            view = getLayoutInflater().from(getActivity()).inflate(R.layout.car_circle, statsCircles, false);

            circleName = view.findViewById(R.id.stats_circle_name);
            circleName.setText(car.getName());

            circle = view.findViewById(R.id.stats_circle);
            circle.setColorFilter(car.getColor(), PorterDuff.Mode.SRC_ATOP);

            statsCircles.addView(view);

            //Line
            view = getLayoutInflater().from(getActivity()).inflate(R.layout.car_stats, statsList, false);

            progressBar = view.findViewById(R.id.stats_progressbar);
            progressBar.setProgress((int) (100 * car.getKmDone() * car.getKml() / totalL));

            progressBar.setProgressTintList(ColorStateList.valueOf(car.getColor()));

            statsL = view.findViewById(R.id.stats_l);
            statsL.setText(car.getKmDone() * car.getKml() + "L");

            statsList.addView(view);
        }
    }
}
