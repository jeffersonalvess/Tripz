package edu.depaul.csc472.tripz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

public class TestePlaces extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    // Static variable that holds the user's city
    public static String actual_city;

    private static final String LOG_TAG = "PlacesTest";
    private static final int GOOGLE_API_CLIENT_ID = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https:/int/g.co/AppIndexing/AndroidStudio for more information.
     */

    private TextView tvName2;
    private TextView tvAddress2;
    private TextView tvLatLong2;
    private TextView tvPhone;
    private TextView tvWebPage;
    private TextView tvAtt2;
    private AutoCompleteTextView tvLocal;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static LatLngBounds BOUNDS;

    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_places);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        Intent intent = getIntent();

        lat = intent.getDoubleExtra("LAT", -190);
        lng = intent.getDoubleExtra("LGN", -190);

        Log.i(LOG_TAG, "UFA");

        if(lat == -190 || lng == 190)
            BOUNDS = new LatLngBounds(new LatLng(37.398160, -122.180831),
                    new LatLng(37.430610, -121.972090));
        else
            BOUNDS = new LatLngBounds(new LatLng(lat-0.5, lng-0.5),
                    new LatLng(lat+0.5, lng+0.5));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        tvLocal = (AutoCompleteTextView) findViewById(R.id.acLocals);

        tvLocal.setThreshold(3);

        tvName2 = (TextView) findViewById(R.id.name2);
        tvAddress2 = (TextView) findViewById(R.id.address2);
        tvLatLong2 = (TextView) findViewById(R.id.latlong2);
        tvPhone = (TextView) findViewById(R.id.phone);
        tvWebPage = (TextView) findViewById(R.id.webpage);
        tvAtt2 = (TextView) findViewById(R.id.att2);

        tvLocal.setOnItemClickListener(mAutocompleteClickListener);

        ArrayList<Integer> filterTypes = new ArrayList<Integer>();
        filterTypes.add(Place.TYPE_ESTABLISHMENT);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS, filterTypes);

        tvLocal.setAdapter(mPlaceArrayAdapter);
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
            Log.i(LOG_TAG, "Cheguei aqui2");
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            Log.i(LOG_TAG, "Mas aqui nao2");
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            Log.i(LOG_TAG, "Cheguei aqui");
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            tvName2.setText(Html.fromHtml(place.getName() + ""));
            tvAddress2.setText(Html.fromHtml(place.getAddress() + ""));
            tvLatLong2.setText(Html.fromHtml(place.getLatLng().toString() + ""));
            tvPhone.setText(Html.fromHtml(place.getPhoneNumber().toString() + ""));
            tvWebPage.setText(Html.fromHtml(place.getWebsiteUri().toString() + ""));

            if (attributions != null) {
                tvAtt2.setText(Html.fromHtml(attributions.toString()));
            }
            Log.i(LOG_TAG, "Mas aqui nao");
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
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
