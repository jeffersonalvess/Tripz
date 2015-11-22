package edu.depaul.csc472.tripz;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.depaul.csc472.tripz.helper.OurDate;

public class GoogleMapsTest extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    // Static variable that holds the user's city
    public static String actual_city;

    private static final String LOG_TAG = "GoogleMapsTest";
    private static final String LOCATION_TAG = "Actual Location";
    private static final int GOOGLE_API_CLIENT_ID = 0;

    private static final int REQUEST_PLACE_PICKER = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https:/int/g.co/AppIndexing/AndroidStudio for more information.
     */

    private TextView tvName;
    private TextView tvAddress;
    private TextView tvAtt;
    private AutoCompleteTextView tvSearch;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
//    private static final LatLngBounds ALL_THE_WORLD = new LatLngBounds(
//            new LatLng(85, -180), new LatLng(-85, 180));

    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        OurDate date = new OurDate("2015/11/22");

        Log.i("TESTE_DATA: ", date.toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps_test);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Button button = (Button) findViewById(R.id.busca);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickButtonClick(v);
            }
        });

        tvSearch = (AutoCompleteTextView) findViewById(R.id.acCity);

        tvSearch.setThreshold(3);

        tvName = (TextView) findViewById(R.id.name);
        tvAddress = (TextView) findViewById(R.id.address);
//        mIdTextView = (TextView) findViewById(R.id.place_id);
//        mPhoneTextView = (TextView) findViewById(R.id.phone);
//        mWebTextView = (TextView) findViewById(R.id.web);
        tvAddress = (TextView) findViewById(R.id.att);

        tvSearch.setOnItemClickListener(mAutocompleteClickListener);

        ArrayList<Integer> filterTypes = new ArrayList<Integer>();
        filterTypes.add(Place.TYPE_LOCALITY);
        filterTypes.add(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_3);
//        filterTypes.add(Place.TYPE_GEOCODE);

//        AutocompleteFilter filter = null;
//        filter = AutocompleteFilter.create(filterTypes);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, filterTypes);
                //BOUNDS_MOUNTAIN_VIEW, filter);

        tvSearch.setAdapter(mPlaceArrayAdapter);

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                String city = likelyPlaces.get(0).getPlace().getAddress().toString();

                int v1, v2;

                for (v1 = 0; city.charAt(v1) != ','; v1++) ;
                for (v2 = v1 + 1; city.charAt(v2) != ','; v2++) ;

                String city2 = city.substring(v1 + 2, v2 + 4);

                for (v1 = city.length() - 1; city.charAt(v1) != ','; v1--) ;

                city2 = city2.concat(" - " + city.substring(v1 + 2));

                Log.i(LOCATION_TAG, city);
                Log.i(LOCATION_TAG, city2);
                likelyPlaces.release();

                actual_city = city2;
            }
        });

        Button trips_and_cities = (Button) findViewById(R.id.bt1);

        trips_and_cities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoogleMapsTest.this, TripsAndCitiesActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onPickButtonClick(View v) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {


            System.out.println("RENATOBR1" + resultCode);

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            ((TextView)findViewById(R.id.name)).setText(name);
            ((TextView)findViewById(R.id.address)).setText(address);
            ((TextView)findViewById(R.id.att)).setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "GoogleMapsTest Page",
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                Uri.parse("android-app://edu.depaul.csc472.tripz/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "GoogleMapsTest Page",
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                Uri.parse("android-app://edu.depaul.csc472.tripz/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            tvName.setText(Html.fromHtml(place.getName() + ""));
            tvAddress.setText(Html.fromHtml(place.getAddress() + ""));
//            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
//            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
//            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                tvAtt.setText(Html.fromHtml(attributions.toString()));
            }
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//            Log.i(LOCATION_TAG, String.valueOf(mLastLocation.getLatitude()));
//        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
}
