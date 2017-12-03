package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by kahraman on 2.12.2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    /**
     * Tag for log messages
     */
    public static final String LOG_TAG = EarthquakeLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.i(LOG_TAG, "EarthquakeLoader created");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "onStartLoading() run");
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Earthquake> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl.isEmpty() || mUrl == null) {
            return null;
        }

        List<Earthquake> result = QueryUtils.fetchEarthquakeData(mUrl);
        Log.i(LOG_TAG, "loadInBackground(): Call for fetching data from UGS");
        return result;
    }
}
