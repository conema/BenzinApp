package me.conema.benzinapp.classes;

public class Review {
    private int id;
    private int stationId;
    private String name;
    private double vote;
    private String description;

    Review(int id, int stationId, String name, double vote, String description) {
        this.id = id;
        this.stationId = stationId;
        this.name = name;
        this.vote = vote;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
