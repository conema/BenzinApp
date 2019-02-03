package me.conema.benzinapp.classes;

import java.util.ArrayList;
import java.util.List;

public class CityData {

    private static List<String> cities;

    static {
        cities = new ArrayList<String>();
        cities.add("cagliari, ca");
        cities.add("quartu Sant'Elena, ca");
        cities.add("quartucciu, ca");
        cities.add("selargius, ca");
        cities.add("pula, ca");
        cities.add("monserrato, ca");
        cities.add("pirri, ca");
        cities.add("catania, ct");
        cities.add("assemini, ca");
        cities.add("sestu, ca");
        cities.add("capoterra, ca");
    }

    public static List<String> getCity() {
        return cities;
    }
}
