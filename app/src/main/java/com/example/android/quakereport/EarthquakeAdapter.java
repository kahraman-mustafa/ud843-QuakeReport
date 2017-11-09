package com.example.android.quakereport;

/**
 * Created by kahra on 8.11.2017.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
* {@link EarthquakeAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
* based on a data source, which is a list of {@link AndroidFlavor} objects.
* */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOG_TAG = EarthquakeAdapter.class.getSimpleName();
    private static final String LOCATION_SEPERATOR = "of";

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context     The current context. Used to inflate the layout file.
     * @param earthquakes A List of Earthquake objects to display in a list
     */
    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, earthquakes);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Earthquake} object located at this position in the list
        Earthquake currentEarthquake = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID earthquake_magnitude
        TextView magTextView = (TextView) listItemView.findViewById(R.id.earthquake_magnitude);
        // Get the magnitude from the current Earthquake object and
        // set this text on the magnitude TextView
        magTextView.setText(Double.toString(currentEarthquake.getMagnitude()));

        // Find the TextView in the list_item.xml layout with the ID earthquake_locationprime
        TextView locationPrimeTextView = (TextView) listItemView.findViewById(R.id.earthquake_locationprime);
        TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.earthquake_locationoffset);

        // Get originalLocation info and check whether it contains "of" (offset information)
        String originalLocation = currentEarthquake.getLocation();
        String locationPrime, locationOffset;
        boolean hasOffset = originalLocation.contains(LOCATION_SEPERATOR);


        if (hasOffset) {
            // if originalLocation contains "of" split it to prime and offset
            String[] locationParts = originalLocation.split(LOCATION_SEPERATOR);
            locationOffset = locationParts[0] + LOCATION_SEPERATOR;
            locationPrime = locationParts[1];
        } else {
            // if originalLocation don't contain "of", set originalLocation to prime, and "Near the" to offset
            locationOffset = getContext().getString(R.string.near_the);
            locationPrime = originalLocation;
        }

        // Set the string values of originalLocation prime and offset to TextViews
        locationPrimeTextView.setText(locationPrime);
        locationOffsetTextView.setText(locationOffset);

        // Find the TextView in the list_item.xml layout with the ID earthquake_date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.earthquake_date);
        // Find the TextView in the list_item.xml layout with the ID earthquake_time
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.earthquake_time);

        /* Get the time in millisec from the current Earthquake object and
         * convert it to user friendly formatted time and date
         */
        Date dateObject = new Date(currentEarthquake.getTimeInMillisec());
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);

        // set formatted time and date on the TextViews
        dateTextView.setText(formattedDate);
        timeTextView.setText(formattedTime);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

    /**
     * Return the formatted date String (i.e. Mar 03, 2016) from a Date object
     *
     * @param dateObject
     * @return formatted date String
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormatter.format(dateObject);
    }

    /**
     * Return the formatted time String (i.e. 4:15, PM) from a Date object
     *
     * @param dateObject
     * @return
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        return timeFormatter.format(dateObject);
    }

}