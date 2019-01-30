package me.conema.benzinapp.classes;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
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
    private float price;
    private double mark;
    private int img;
    private LatLng position;

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
}
