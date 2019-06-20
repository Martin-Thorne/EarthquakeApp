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
    // Temporary earthquake data used to test parsing
    private static final String EARTHQUAKE_DATA = "{\"type\":\"FeatureCollection\",\"metadata\":{\"generated\":1560948066000,\"url\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=5\",\"title\":\"USGS Earthquakes\",\"status\":200,\"api\":\"1.8.1\",\"limit\":5,\"offset\":1,\"count\":5},\"features\":[{\"type\":\"Feature\",\"properties\":{\"mag\":6.4,\"place\":\"140km NE of L'Esperance Rock, New Zealand\",\"time\":1560927702801,\"updated\":1560935158945,\"tz\":-720,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us600042vn\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us600042vn&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":0,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":630,\"net\":\"us\",\"code\":\"600042vn\",\"ids\":\",pt19170000,at00ptc3is,us600042vn,\",\"sources\":\",pt,at,us,\",\"types\":\",geoserve,ground-failure,impact-link,losspager,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":1.356,\"rms\":1.51,\"gap\":51,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.4 - 140km NE of L'Esperance Rock, New Zealand\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-177.787,-30.6004,10]},\"id\":\"us600042vn\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":6.4,\"place\":\"31km WSW of Tsuruoka, Japan\",\"time\":1560864139013,\"updated\":1560940556302,\"tz\":540,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us600042fx\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us600042fx&format=geojson\",\"felt\":131,\"cdi\":9.1,\"mmi\":6.871,\"alert\":\"yellow\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":769,\"net\":\"us\",\"code\":\"600042fx\",\"ids\":\",us600042fx,pt19169001,at00ptaqha,\",\"sources\":\",us,pt,at,\",\"types\":\",dyfi,geoserve,ground-failure,impact-link,losspager,moment-tensor,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":1.295,\"rms\":0.83,\"gap\":33,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.4 - 31km WSW of Tsuruoka, Japan\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[139.4723,38.646,12]},\"id\":\"us600042fx\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":6,\"place\":\"135km ENE of L'Esperance Rock, New Zealand\",\"time\":1560751325750,\"updated\":1560837961875,\"tz\":-720,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us600041lv\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us600041lv&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":0,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":554,\"net\":\"us\",\"code\":\"600041lv\",\"ids\":\",us600041lv,\",\"sources\":\",us,\",\"types\":\",geoserve,ground-failure,losspager,moment-tensor,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":1.711,\"rms\":1.15,\"gap\":49,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.0 - 135km ENE of L'Esperance Rock, New Zealand\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-177.5972,-30.9381,16]},\"id\":\"us600041lv\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":6.3,\"place\":\"93km ENE of L'Esperance Rock, New Zealand\",\"time\":1560662236749,\"updated\":1560774595040,\"tz\":-720,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us600041b3\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us600041b3&format=geojson\",\"felt\":2,\"cdi\":2,\"mmi\":0,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":611,\"net\":\"us\",\"code\":\"600041b3\",\"ids\":\",us600041b3,pt19167000,at00pt6eof,\",\"sources\":\",us,pt,at,\",\"types\":\",dyfi,geoserve,ground-failure,impact-link,losspager,moment-tensor,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":1.812,\"rms\":1.6,\"gap\":37,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.3 - 93km ENE of L'Esperance Rock, New Zealand\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-178.0232,-31.0615,35]},\"id\":\"us600041b3\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":7.2,\"place\":\"103km NE of L'Esperance Rock, New Zealand\",\"time\":1560639302415,\"updated\":1560797309596,\"tz\":-720,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us6000417i\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us6000417i&format=geojson\",\"felt\":12,\"cdi\":8.3,\"mmi\":6.553,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":807,\"net\":\"us\",\"code\":\"6000417i\",\"ids\":\",at00pt5wzj,us6000417i,pt19166001,\",\"sources\":\",at,us,pt,\",\"types\":\",dyfi,finite-fault,general-text,geoserve,impact-link,losspager,moment-tensor,origin,phase-data,poster,shakemap,\",\"nst\":null,\"dmin\":1.561,\"rms\":1.14,\"gap\":23,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 7.2 - 103km NE of L'Esperance Rock, New Zealand\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-178.0945,-30.8051,34.4]},\"id\":\"us6000417i\"}],\"bbox\":[-178.0945,-31.0615,10,139.4723,38.646,35]}";


    private GoogleMap map;
    private ArrayList<Earthquake> earthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialises ArrayList to parsed earthquake data
        earthquakes = GetParseEarthquakes.parseEarthquakes(EARTHQUAKE_DATA);

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
