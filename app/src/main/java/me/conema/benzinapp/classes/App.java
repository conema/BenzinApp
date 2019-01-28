package me.conema.benzinapp.classes;

import java.util.ArrayList;
import java.util.Date;

public class App {
    private Car favCar;
    private Station favStation;
    private ArrayList<Station> lastStations;
    private Date lastSync;

    App(Car favCar, Station favStation, ArrayList<Station> lastStations, Date lastSync) {
        this.favCar = favCar;
        this.favStation = favStation;
        this.lastStations = lastStations;
        this.lastSync = lastSync;
    }

    public Car getFavCar() {
        return favCar;
    }

    public void setFavCar(Car favCar) {
        this.favCar = favCar;
    }

    public Station getFavStation() {
        return favStation;
    }

    public void setFavStation(Station favStation) {
            this.favStation = favStation;
    }

    public ArrayList<Station> getLastStations() {
        return lastStations;
    }

    public void setLastStations(ArrayList<Station> lastStations) {
        this.lastStations = lastStations;
    }

    public Date getLastSync() {
        return lastSync;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }
}
