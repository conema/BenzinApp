package me.conema.benzinapp.classes;

import java.util.ArrayList;

public class StationFactory {
    private static StationFactory singleton;
    private ArrayList<Station> stationList = new ArrayList<>();

    static StationFactory getInstance() {
        if (singleton == null) {
            singleton = new StationFactory();
        }
        return singleton;
    }

    private StationFactory() {
        Station s1 = new Station(0, "ENI", "Via Roma 32 Cagliari", 1.437);
        Station s2 = new Station(1, "Q8", "Via Fiume 31 Cagliari", 1.543);
        Station s3 = new Station(2, "Total", "Via Genova 1 Cagliari", 1.520);

        stationList.add(s1);
        stationList.add(s2);
        stationList.add(s3);
    }

    public ArrayList<Station> getStations() {
        ArrayList<Station> stations = new ArrayList<>();

        for (Station station : stationList) {
            stations.add(station);
        }

        return stations;
    }

    public int addStation(Station station) {
        stationList.add(station);
        return station.getId();
    }

    public boolean removeStation(Station station) {
        return stationList.remove(station);
    }

    Station getStationById(int id) {
        for (Station station : stationList) {
            if (station.getId() == id) {
                return station;
            }
        }

        return null;
    }
}


