package com.martint.earthquakeapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Helper class for getting/parsing earthquake data
 */
public class ParseEarthquakes {

    private ParseEarthquakes() {
    }

    /**
     * Parses USGS json string to extract data to create Earthquake objects
     *
     * @param jsonResponse USGS json string from website
     * @return Array of Earthquake objects
     */
    public static ArrayList<Earthquake> parseEarthquakes(String jsonResponse) {
        // Empty array list to add earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        try {
            // Create JSONObject from json string
            JSONObject jsonObject = new JSONObject(jsonResponse);
            // Create JSONArray of the features of each earthquake in JSONObject
            JSONArray feature = jsonObject.getJSONArray("features");

            // loop for each earthquake
            for (int i = 0; i < feature.length(); i++) {
                // Creates JSONObject for individual eartquake
                JSONObject jsonObjectIndividual = feature.getJSONObject(i);
                // JSONObjects hold json objects with keys properties/geometry
                JSONObject jsonObjectProperties = jsonObjectIndividual.getJSONObject("properties");
                JSONObject jsonObjectGeomotry = jsonObjectIndividual.getJSONObject("geometry");
                // JSONArray hold json array of object geometry with key coordinates
                JSONArray jsonArray = jsonObjectGeomotry.getJSONArray("coordinates");
                // Access JSONArray to get latitude/longitude
                double latitude = jsonArray.getDouble(1);
                double longitude = jsonArray.getDouble(0);
                // Access JSONObject to get location/magnitude/url
                String location = jsonObjectProperties.getString("place");
                double magnitude = jsonObjectProperties.getDouble("mag");
                String url = jsonObjectProperties.getString("url");
                // Create new earthquake object
                earthquakes.add(new Earthquake(latitude, longitude, location, magnitude, url));
            }


        } catch (JSONException e) {
            return null;
        }

        return earthquakes;
    }
}
