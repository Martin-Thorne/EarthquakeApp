package com.martint.earthquakeapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Earthquake> earthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Creates a set of fake earthquakes, stored in an array, to display on map
        earthquakes = new ArrayList<>();
        Earthquake earthquake1 = new Earthquake(-30.6004, -177.787);
        earthquakes.add(earthquake1);
        Earthquake earthquake2 = new Earthquake(38.646, 139.4723);
        earthquakes.add(earthquake2);
        Earthquake earthquake3 = new Earthquake(-30.9381, -177.5972);
        earthquakes.add(earthquake3);
        Earthquake earthquake4 = new Earthquake(-31.0615, -178.0232);
        earthquakes.add(earthquake4);
        Earthquake earthquake5 = new Earthquake(-30.8051, -178.0945);
        earthquakes.add(earthquake5);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * Adds markers to show recent earthquakes.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Goes through array of earthquakes adding markers to each earthquake position
        for (int i = 0; i < earthquakes.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(earthquakes.get(i).getmLatitude(), earthquakes.get(i).getmLongitude())));
        }

        mMap.moveCamera(CameraUpdateFactory.zoomTo(2.0f));
    }
}
