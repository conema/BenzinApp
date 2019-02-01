package me.conema.benzinapp.classes;

import java.util.Date;
import java.util.Stack;

public class App {
    private Car favCar;
    private Station favStation;
    private Stack<Station> lastStations;
    private Date lastSync;

    App(Car favCar, Station favStation, Stack<Station> lastStations, Date lastSync) {
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

    public Stack<Station> getLastStations() {
        return lastStations;
    }

    public void setLastStations(Stack<Station> lastStations) {
        this.lastStations = lastStations;
    }

    public void pushLastStation(Station station) {

        if (lastStations.search(station) > -1) {
            lastStations.remove(station);
        }

        lastStations.push(station);

    }

    public Date getLastSync() {
        return lastSync;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }
}
