package me.conema.benzinapp.classes;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.HashMap;
import java.util.Random;

import me.conema.benzinapp.R;

public class StationFactory {
    private static StationFactory singleton;
    private HashMap<LatLng, Station> stationHashMap = new HashMap<>(70);

    public static StationFactory getInstance() {
        if (singleton == null) {
            singleton = new StationFactory();
        }
        return singleton;
    }

    private double generateMark() {
        Random r = new Random();
        return 5 * r.nextDouble();
    }

    private float generatePrice() {
        Random r = new Random();
        return 1.0f + (1.6f - 1.0f) * r.nextFloat();

    }

    private StationFactory() {
        stationHashMap.put(new LatLng(39.2325218, 9.0953491), new Station(0, Station.NAME_TAMOIL, "Via Adige, San Paolo, Sant'Avendrace, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(39.2325218, 9.0953491), generateMark()));
        stationHashMap.put(new LatLng(39.2329010, 9.1029220), new Station(1, Station.NAME_TAMOIL, "Via Is Maglias, San Paolo, Tuvixeddu-Tuvumannu, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(39.2329010, 9.1029220), generateMark()));
        stationHashMap.put(new LatLng(39.2216692, 9.1062200), new Station(2, Station.NAME_TAMOIL, "Piazza dell'Annunziata, San Paolo, Tuvixeddu-Tuvumannu, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(39.2216692, 9.1062200), generateMark()));
        stationHashMap.put(new LatLng(39.2284012, 9.1320594), new Station(3, Station.NAME_TAMOIL, "Viale Guglielmo Marconi, Genneruxi, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(39.2284012, 9.1320594), generateMark()));
        stationHashMap.put(new LatLng(39.2968868, 9.0009544), new Station(9, Station.NAME_TAMOIL, "Via Piave, Piri Piri, Assemini, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(39.2968868, 9.0009544), generateMark()));
        stationHashMap.put(new LatLng(39.1694859, 8.9862899), new Station(8, Station.NAME_TAMOIL, "SP91, Capoterra, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(39.1694859, 8.9862899), generateMark()));
        stationHashMap.put(new LatLng(38.9797039, 8.9799854), new Station(7, Station.NAME_TAMOIL, "SS195, Pula, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(38.9797039, 8.9799854), generateMark()));
        stationHashMap.put(new LatLng(39.2141564, 9.1075633), new Station(17, Station.NAME_ENI, "Viale La Plaia, San Paolo, Stampace, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2141564, 9.1075633), generateMark()));
        stationHashMap.put(new LatLng(39.2332010, 9.1864990), new Station(19, Station.NAME_ENI, "Viale Cristoforo Colombo, Quartu Sant'Elena, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2332010, 9.1864990), generateMark()));
        stationHashMap.put(new LatLng(39.2068343, 9.1331229), new Station(23, Station.NAME_ENI, "Piazza Firenze, Bonaria, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2068343, 9.1331229), generateMark()));
        stationHashMap.put(new LatLng(39.2424670, 9.0908840), new Station(24, Station.NAME_ENI, "Viale Elmas, San Paolo, San Michele, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2424670, 9.0908840), generateMark()));
        stationHashMap.put(new LatLng(39.2528680, 9.1136360), new Station(25, Station.NAME_ENI, "Via del Timo, Barracca Manna, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2528680, 9.1136360), generateMark()));
        stationHashMap.put(new LatLng(39.2080880, 9.1337280), new Station(26, Station.NAME_ENI, "Via della Pineta, Bonaria, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2080880, 9.1337280), generateMark()));
        stationHashMap.put(new LatLng(39.2254425, 9.1298667), new Station(27, Station.NAME_ENI, "Viale Guglielmo Marconi, Genneruxi, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2254425, 9.1298667), generateMark()));
        stationHashMap.put(new LatLng(39.2714910, 9.1372190), new Station(30, Station.NAME_ENI, "SS387, Monserrato, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2714910, 9.1372190), generateMark()));
        stationHashMap.put(new LatLng(39.2817382, 9.0319635), new Station(32, Station.NAME_ESSO, "SS130, Giliacquas, Elmas, Cagliari", generatePrice(), R.drawable.ic_esso_logo, new LatLng(39.2817382, 9.0319635), generateMark()));
        stationHashMap.put(new LatLng(39.2357140, 9.1126930), new Station(28, Station.NAME_ENI, "Piazza De Esquivel Arcivescovo, San Paolo, La Vega, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2357140, 9.1126930), generateMark()));
        stationHashMap.put(new LatLng(39.2516385, 9.1034724), new Station(33, Station.NAME_ESSO, "Via Giuseppe Peretti, Selargius, Cagliari", generatePrice(), R.drawable.ic_esso_logo, new LatLng(39.2516385, 9.1034724), generateMark()));
        stationHashMap.put(new LatLng(39.2308900, 9.0949150), new Station(29, Station.NAME_ENI, "Via Santa Gilla, San Paolo, Sant'Avendrace, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2308900, 9.0949150), generateMark()));
        stationHashMap.put(new LatLng(38.9694533, 8.9705068), new Station(31, Station.NAME_ENI, "SS195, Forte Village, Domus De Maria, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(38.9694533, 8.9705068), generateMark()));
        stationHashMap.put(new LatLng(39.2422696, 9.1714671), new Station(34, Station.NAME_ESSO, "Via Guglielmo Marconi, Quartu Sant'Elena, Cagliari", generatePrice(), R.drawable.ic_esso_logo, new LatLng(39.2422696, 9.1714671), generateMark()));
        stationHashMap.put(new LatLng(39.2370500, 9.1249250), new Station(38, Station.NAME_ESSO, "Via Riva Villasanta, Monteleone, Cagliari", generatePrice(), R.drawable.ic_esso_logo, new LatLng(39.2370500, 9.1249250), generateMark()));
        stationHashMap.put(new LatLng(39.2168302, 9.1254600), new Station(37, Station.NAME_ESSO, "Piazza della Repubblica, Monte Urpinu, Cagliari", generatePrice(), R.drawable.ic_esso_logo, new LatLng(39.2168302, 9.1254600), generateMark()));
        stationHashMap.put(new LatLng(39.2335950, 9.1113870), new Station(35, Station.NAME_ESSO, "Via Capitanata, San Paolo, La Vega, Cagliari", generatePrice(), R.drawable.ic_esso_logo, new LatLng(39.2335950, 9.1113870), generateMark()));
        stationHashMap.put(new LatLng(39.2329010, 9.1029220), new Station(36, Station.NAME_ESSO, "Via Riva Villasanta, Pirri, Cagliari", generatePrice(), R.drawable.ic_esso_logo, new LatLng(39.2329010, 9.1029220), generateMark()));
        stationHashMap.put(new LatLng(39.2982480, 9.0936330), new Station(10, Station.NAME_TAMOIL, "Via Monserrato, Sestu, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(39.2982480, 9.0936330), generateMark()));
        stationHashMap.put(new LatLng(39.2863720, 9.1856290), new Station(11, Station.NAME_TAMOIL, "Via Roma, Settimo San Pietro, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(39.2863720, 9.1856290), generateMark()));
        stationHashMap.put(new LatLng(39.0088955, 8.9958697), new Station(13, Station.NAME_ENI, "SS195, Pula, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.0088955, 8.9958697), generateMark()));
        stationHashMap.put(new LatLng(39.2239334, 9.1155413), new Station(15, Station.NAME_ENI, "Via Belvedere, San Paolo, Castello, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2239334, 9.1155413), generateMark()));
        stationHashMap.put(new LatLng(39.2513209, 9.1339677), new Station(16, Station.NAME_ENI, "Via Italia, Terramaini, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2513209, 9.1339677), generateMark()));
        stationHashMap.put(new LatLng(39.2415420, 9.1509550), new Station(18, Station.NAME_ENI, "Viale Guglielmo Marconi, Quartiere Europeo, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2415420, 9.1509550), generateMark()));
        stationHashMap.put(new LatLng(38.9934133, 8.9925959), new Station(21, Station.NAME_ENI, "SS195, Pula, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(38.9934133, 8.9925959), generateMark()));
        stationHashMap.put(new LatLng(39.2474346, 9.1959459), new Station(20, Station.NAME_ENI, "Via Guglielmo Marconi, Quartu Sant'Elena, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2474346, 9.1959459), generateMark()));
        stationHashMap.put(new LatLng(39.2341300, 9.0974580), new Station(22, Station.NAME_ENI, "Piazza Sant'Avendrace, San Paolo, Sant'Avendrace, Cagliari", generatePrice(), R.drawable.ic_eni_logo, new LatLng(39.2341300, 9.0974580), generateMark()));
        stationHashMap.put(new LatLng(39.2798682, 9.1815180), new Station(12, Station.NAME_TAMOIL, "Via San Salvatore, Settimo San Pietro, Cagliari", generatePrice(), R.drawable.ic_tamoil_logo, new LatLng(39.2798682, 9.1815180), generateMark()));
    }


    public HashMap<LatLng, Station> getStations() {
        HashMap<LatLng, Station> stations = new HashMap<>();

        for (LatLng currentKey : stationHashMap.keySet()) {
            stations.put(currentKey, stationHashMap.get(currentKey));
        }

        return stations;
    }

    public int addStation(Station station) {
        //stationList.add(station);
        stationHashMap.put(station.getPosition(), station);
        return station.getId();
    }

    public Station removeStation(LatLng position) {
        return stationHashMap.remove(position);
    }

    public Station getStationById(int id) {
        for (LatLng currentKey : stationHashMap.keySet()) {
            if (stationHashMap.get(currentKey).getId() == id) {
                return stationHashMap.get(currentKey);
            }
        }

        return null;
    }
}


