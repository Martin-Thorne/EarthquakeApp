package com.martint.earthquakeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String EARTHQUAKE_URL_USGS = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    private GoogleMap map;
    private ArrayList<Earthquake> earthquakes;

    // Used to create a latitude and longitude bound that includes markers
    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Set up toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Create options menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Handles options menu selection
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_menu:
                new MaterialAlertDialogBuilder(this)
//                        TODO reload to selected magnitudes
                        .setTitle("Title")
                        .setItems(R.array.mag_array_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        break;
                                    case 3:
                                        break;
                                    case 4:
                                        break;
                                }
                            }
                        })
                        .setPositiveButton("Ok", null)
                        .show();
                return true;
            case R.id.refresh_menu:
//            TODO reload markers
                return true;
            case R.id.about_menu:
//             TODO create about page
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        map.setOnInfoWindowClickListener(this);
        map.moveCamera(CameraUpdateFactory.zoomTo(2.0f));
    }

    /**
     * Adds markers and info windows to show recent earthquakes.
     */
    private void addMarkers() {

        // If there are no earthquakes, parsing failed, then display toast
        if (earthquakes == null) {
            Toast.makeText(getApplicationContext(), "Error parsing earthquake data", Toast.LENGTH_LONG).show();
            return;
        }
        // Goes through array of earthquakes adding markers and info windows to each earthquake position.
        for (int i = 0; i < earthquakes.size(); i++) {
            Earthquake earthquake = earthquakes.get(i);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(earthquake.getLatitude(), earthquake.getLongitude()))
                    .title(earthquake.getLocation())
                    .snippet("Magnitude:" + earthquake.getMagnitude()));

            // Adds the url of the earthquake webpage to the marker
            marker.setTag(earthquake.getUrl());

            // Adds the latitude and longitude of the marker to the builder
            builder.include(marker.getPosition());
        }
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
    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    // Requests a string response from USGS website
    StringRequest stringRequest = new StringRequest(Request.Method.GET, EARTHQUAKE_URL_USGS,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    // If request successful parse string, add markers and move the camera to show markers
                    earthquakes = ParseEarthquakes.parseEarthquakes(response);
                    addMarkers();

                    // If parsing successful creates latitude and longitude bounds that is used to move the map camera when the markers have been placed
                    if (earthquakes != null) {
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                        map.animateCamera(cu);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // If request unsuccessful display toast
                    Toast.makeText(getApplicationContext(), "Error obtaining earthquake data", Toast.LENGTH_LONG).show();
                }
            });
}
