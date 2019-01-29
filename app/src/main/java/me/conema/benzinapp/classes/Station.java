package me.conema.benzinapp.classes;

import android.graphics.drawable.Drawable;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.light.Position;

public class Station {
    /* toy data */
    static public String NAME_ENI = "ENI";
    static public String NAME_Q8 = "Q8";
    static public String NAME_TOTAL = "Total";
    static public String NAME_TAMOIL = "Tamoil";
    static public String NAME_ESSO = "ESSO";



    private int id;
    private String name;
    private String address;
    private double price;
    private int img;
    private LatLng position;

    public Station(int id, String name, String address, double price, int img, LatLng position) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.img = img;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public LatLng getPosition() { return position; }

    public void setPosition(LatLng position) { this.position = position; }
}
