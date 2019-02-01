package me.conema.benzinapp.classes;

import android.graphics.drawable.Drawable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.light.Position;
public class Station {
    /* toy data */
    public static String NAME_ENI = "ENI";
    public static String NAME_Q8 = "Q8";
    public static String NAME_TOTAL = "Total";
    public static String NAME_TAMOIL = "Tamoil";
    public static String NAME_ESSO = "ESSO";

    public enum ComparationType {DISTANCE, PRICE, MARK}


    private int id;
    private String name;
    private String address;
    private float price;
    private double mark;
    private int img;
    private LatLng position;


    private static class distanceComparator implements Comparator<Pair<Station, Double>> {
        @Override
        public int compare(Pair<Station, Double> stationDoublePair, Pair<Station, Double> t1) {
            return stationDoublePair.second.compareTo(t1.second);
        }
    }

    private static class priceComparator implements Comparator<Pair<Station, Double>> {
        @Override
        public int compare(Pair<Station, Double> stationDoublePair, Pair<Station, Double> t1) {
            return Float.compare(stationDoublePair.first.getPrice(), t1.first.getPrice());
        }
    }

    private static class markComparator implements Comparator<Pair<Station, Double>> {
        @Override
        public int compare(Pair<Station, Double> stationDoublePair, Pair<Station, Double> t1) {
            return Double.compare(stationDoublePair.first.getMark(), t1.first.getMark());
        }
    }

    public static Comparator<Pair<Station, Double>> getComparator(ComparationType type) {
        switch (type) {
            case MARK:
                return new markComparator();
            case PRICE:
                return new priceComparator();
            case DISTANCE:
                return new distanceComparator();
        }

        return null;
    }

    Station(int id, String name, String address, float price, int img, LatLng position, double mark) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.img = img;
        this.mark = mark;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
    public double getMark() {
        ArrayList<Review> reviews = ReviewFactory.getInstance().getReviewByStation(id);
        double mark = 0;

        for (Review review : reviews) {
            mark += review.getVote();
        }
        if (reviews.size() > 0) {
            return mark / reviews.size();
        } else {
            return 0;
        }
    }

    public void setMark(double mark) {
        this.mark = mark;
    }
    public LatLng getPosition() { return position; }
    public void setPosition(LatLng position) { this.position = position; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return getId() == station.getId() &&
                Float.compare(station.getPrice(), getPrice()) == 0 &&
                Double.compare(station.getMark(), getMark()) == 0 &&
                getImg() == station.getImg() &&
                Objects.equals(getName(), station.getName()) &&
                Objects.equals(getAddress(), station.getAddress()) &&
                Objects.equals(getPosition(), station.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAddress(), getPrice(), getMark(), getImg(), getPosition());
    }
}
