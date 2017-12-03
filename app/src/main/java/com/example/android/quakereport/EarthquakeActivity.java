/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * URL to query the USGS dataset for earthquake information
     */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=2&limit=10";
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    /**
     * Adapter for the list of earthquakes
     */
    private EarthquakeAdapter mAdapter;
    // ListView containing earthquake results to show on the screen
    private ListView earthquakeListView;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        // Set empty view which shows a message to user when there is no data
        // But don't add a text yet not to cause showing the message while data is fetching when opening the app
        // It should only seen when loadFinished and there is no data to show
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Check if there is an available internet connection or not
        // Don't init loader and directly display warning message to the user if no internet connection
        if (isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            Log.i(LOG_TAG, "Loader manager instantiated");

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Hide progress bar to show loaded data or informing message about no results
            View progressBar = findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);

            // If no internet connection is available, inform the user
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    /**
     * Check if there is an available internet connection or not, return true or false
     */
    private boolean isConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "onCreateLoader() run");
        // Create a new loader for the given URL
        return new EarthquakeLoader(EarthquakeActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        // Hide progress bar to show loaded data or informing message about no results
        View progressBar = findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);

        // If there'd be no earthquake data to show, informing message will be displayed to the user
        mEmptyStateTextView.setText(R.string.no_earthquake_found);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            Log.i(LOG_TAG, "onLoadFinished(): Adapter not empty and loaded with data");
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        Log.i(LOG_TAG, "onLoaderReset() run");
    }
}
