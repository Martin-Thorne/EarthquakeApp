package com.martint.earthquakeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

    private String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
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

        volley();
        requestQueue = Volley.newRequestQueue(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        requestQueue.add(stringRequest);
        map.setOnInfoWindowClickListener(this);
        map.moveCamera(CameraUpdateFactory.zoomTo(2.0f));
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
//                        TODO re-load to selected magnitudes
                        .setTitle(R.string.mag_dialog_title)
                        .setItems(R.array.mag_dialog_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        volleyRequest(1);
                                        break;
                                    case 1:
                                        volleyRequest(2);
                                        break;
                                    case 2:
                                        volleyRequest(3);
                                        break;
                                    case 3:
                                        volleyRequest(4);
                                        break;
                                    case 4:
                                        volleyRequest(5);
                                        break;
                                }
                            }
                        })
                        .setPositiveButton("OK", null)
                        .show();
                return true;
            case R.id.reload_markers_menu:

                // clears existing markers then reloads
                map.clear();
                requestQueue.add(stringRequest);
                return true;
            case R.id.about_menu:

                // Create about dialog with clickable links
                MaterialAlertDialogBuilder aboutDialog = new MaterialAlertDialogBuilder(this);
                aboutDialog.setTitle(R.string.about_dialog_title);
                aboutDialog.setMessage(Html.fromHtml(getString(R.string.about_dialog_main)));
                aboutDialog.setPositiveButton("OK", null);
                AlertDialog builder = aboutDialog.create();
                builder.show();
                ((TextView) builder.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
        }

        return super.onOptionsItemSelected(item);
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

    /**
     * Creates a URL used to request the data from USGS
     *
     * @param magnitude the minimum earthquake magnitude requested
     * @return
     */
    private String createUrl(String magnitude) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("earthquake.usgs.gov");
        builder.appendPath("fdsnws");
        builder.appendPath("event");
        builder.appendPath("1");
        builder.appendPath("query");
        builder.appendQueryParameter("format", "geojson");
        builder.appendQueryParameter("orderby", "time");
        builder.appendQueryParameter("minmag", magnitude);
        builder.appendQueryParameter("limit", "10");
        String myUrl = builder.build().toString();
        return myUrl;
    }

    /**
     * Updates the map to the user requested magnitude
     *
     * @param magnitude the minimum earthquake magnitude requested
     */
    private void volleyRequest(int magnitude) {

        // If the current magnitude does not match the requested magnitude then the URL
        // is updated and a new instance of stringRequest is created
        if (!url.contains("minmag=" + magnitude)) {
            url = createUrl(Integer.toString(magnitude));
            volley();
        }
        map.clear();
        requestQueue.add(stringRequest);
    }

    /**
     * When the URL changes a new instance of stringRequest is created.
     */
    private void volley() {
        // Requests a string response from USGS website
        stringRequest = new StringRequest(Request.Method.GET, url,
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
}
