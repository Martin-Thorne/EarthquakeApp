package com.martint.earthquakeapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap map;
    ArrayList<Earthquake> earthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Creates a set of fake earthquakes, stored in an array, to display on map
        earthquakes = new ArrayList<>();
        Earthquake earthquake1 = new Earthquake(-30.6004, -177.787, "140km NE of L'Esperance Rock, New Zealand", 6.4, "https://earthquake.usgs.gov/earthquakes/eventpage/us600042vn");
        earthquakes.add(earthquake1);
        Earthquake earthquake2 = new Earthquake(38.646, 139.4723, "31km WSW of Tsuruoka, Japan", 6.4, "https://earthquake.usgs.gov/earthquakes/eventpage/us600042fx");
        earthquakes.add(earthquake2);
        Earthquake earthquake3 = new Earthquake(-30.9381, -177.5972, "135km ENE of L'Esperance Rock, New Zealand", 6, "https://earthquake.usgs.gov/earthquakes/eventpage/us600041lv");
        earthquakes.add(earthquake3);
        Earthquake earthquake4 = new Earthquake(-31.0615, -178.0232, "93km ENE of L'Esperance Rock, New Zealand", 6.3, "https://earthquake.usgs.gov/earthquakes/eventpage/us600041b3");
        earthquakes.add(earthquake4);
        Earthquake earthquake5 = new Earthquake(-30.8051, -178.0945, "103km NE of L'Esperance Rock, New Zealand", 7.2, "https://earthquake.usgs.gov/earthquakes/eventpage/us6000417i");
        earthquakes.add(earthquake5);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * Adds markers and info windows to show recent earthquakes.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Goes through array of earthquakes adding markers and info windows to each earthquake position.
        for (int i = 0; i < earthquakes.size(); i++) {
            Earthquake earthquake = earthquakes.get(i);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(earthquake.getLatitude(), earthquake.getLongitude()))
                    .title(earthquake.getLocation())
                    .snippet("Magnitude:" + earthquake.getMagnitude()));
            // Adds the url of the earthquake webpage to the marker
            marker.setTag(earthquake.getUrl());
        }
        map.setOnInfoWindowClickListener(this);
        map.moveCamera(CameraUpdateFactory.zoomTo(2.0f));
    }

    /**
     * Handles user clicking on markers info window
     *
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        openWebPage(marker.getTag().toString());
    }

    /**
     * Intent to open web page
     *
     * @param url
     */
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
