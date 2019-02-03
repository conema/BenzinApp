package me.conema.benzinapp;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import me.conema.benzinapp.classes.CityData;

public class CitySearchProvider extends ContentProvider {
    public CitySearchProvider() {
    }

    private static final String STORES = "stores/" + SearchManager.SUGGEST_URI_PATH_QUERY + "/*";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI("me.conema.benzinapp.search", STORES, 1);
    }

    private static String[] matrixCursorColumns = {"_id",
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA};

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        String queryType = "";
        switch (uriMatcher.match(uri)) {
            case 1:
                String query = uri.getLastPathSegment().toLowerCase();
                return getSearchResultsCursor(query);
            default:
                return null;
        }
    }

    private MatrixCursor getSearchResultsCursor(String searchString) {
        MatrixCursor searchResults = new MatrixCursor(matrixCursorColumns);
        Object[] mRow = new Object[3];
        int counterId = 0;
        if (searchString != null) {
            searchString = searchString.toLowerCase();

            for (String rec : CityData.getCity()) {
                if (rec.toLowerCase().contains(searchString)) {
                    mRow[0] = "" + counterId++;
                    mRow[1] = getLoc(rec, getContext()).getAddressLine(0).replace("[", "").replace("]", "");
                    mRow[2] = "" + counterId++;

                    searchResults.addRow(mRow);
                }
            }
        }
        return searchResults;
    }

    public static Address getLoc(String city, Context context) {
        Geocoder gc = new Geocoder(context);
        ArrayList<String> cities = new ArrayList<>();
        List<Address> list = null;

        try {
            list = gc.getFromLocationName(city, 7);
        } catch (Exception ex) {

        }

        return list.get(0);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
