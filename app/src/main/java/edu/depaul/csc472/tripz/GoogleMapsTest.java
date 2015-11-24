package edu.depaul.csc472.tripz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class GoogleMapsTest extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    // Static variable that holds the user's city

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
    private TextView tvLatLong;
    private TextView tvAtt;
    private AutoCompleteTextView tvSearch;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private Place place;

    private ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps_test);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        places = new ArrayList<Place>();
        place = null;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        tvSearch = (AutoCompleteTextView) findViewById(R.id.acCity);

        tvSearch.setThreshold(3);

        tvName = (TextView) findViewById(R.id.name);
        tvAddress = (TextView) findViewById(R.id.address);
        tvLatLong = (TextView) findViewById(R.id.latlong);
        tvAtt = (TextView) findViewById(R.id.att);

        tvSearch.setOnItemClickListener(mAutocompleteClickListener);

        ArrayList<Integer> filterTypes = new ArrayList<Integer>();
        filterTypes.add(Place.TYPE_LOCALITY);
        filterTypes.add(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_3);
        filterTypes.add(Place.TYPE_GEOCODE);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, filterTypes);
                //BOUNDS_MOUNTAIN_VIEW, filter);

        tvSearch.setAdapter(mPlaceArrayAdapter);


        // ***** Pick a Place *********************************************************************
        Button button = (Button) findViewById(R.id.busca);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickButtonClick(v);
            }
        });





        // ***** Test Buttons *********************************************************************
        Button trips_and_cities = (Button) findViewById(R.id.bt1);

        trips_and_cities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(place != null) {
//                    String dest, ori;
//
//                    if(place.getAddress() != null) {
//                        dest = place.getAddress().toString();
//                        dest.replace(' ', '+');
//                    }
//                    else
//                        dest = String.valueOf(place.getLatLng().latitude)
//                                + "," + String.valueOf(place.getLatLng().longitude);
//
//                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + dest);
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//                    startActivity(mapIntent);
//                }
//                else{
//                    Toast.makeText(GoogleMapsTest.this, "No destination selected", Toast.LENGTH_LONG).show();
//                }

                if(places.size() > 0 && MainActivity.actual_city != null){
                    String dest = "";

                    Log.i(LOG_TAG, "Cheguei aqui");

                    dest = String.valueOf(MainActivity.actual_location.latitude)
                            + "," + String.valueOf(MainActivity.actual_location.longitude) + "/";

                    Log.i(LOG_TAG, "Cheguei aqui5");

                    for(int i = 0; i < places.size(); i++){
                        if(places.get(i).getAddress() != null) {
                            String aux = places.get(i).getAddress().toString();
                            aux = aux.replace(" ", "+");

                            dest += aux;
                        }
                        else
                            dest += String.valueOf(places.get(i).getLatLng().latitude)
                                    + "," + String.valueOf(place.getLatLng().longitude);

                        dest += "/";
                    }

                    Log.i(LOG_TAG, "URL: " + "https://www.google.com/maps/dir/" + dest);

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/dir/" + dest));
                    startActivity(intent);
                }

            }
        });

        Button teste_banco = (Button) findViewById(R.id.button);

        teste_banco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoogleMapsTest.this, TesteBanco.class);
                startActivity(intent);
            }
        });

        Button teste_places = (Button) findViewById(R.id.bt2);

        teste_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoogleMapsTest.this, TestePlaces.class);

                intent.putExtra("LAT", place.getLatLng().latitude);
                intent.putExtra("LNG", place.getLatLng().longitude);

                startActivity(intent);
            }
        });
    }

    public void onPickButtonClick(View v) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            if(place != null)
                intentBuilder.setLatLngBounds(new LatLngBounds(
                        new LatLng(place.getLatLng().latitude-0.002,place.getLatLng().longitude-0.002),
                        new LatLng(place.getLatLng().latitude+0.002, place.getLatLng().longitude+0.002)));

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
        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            place = PlacePicker.getPlace(data, this);

            places.add(place);

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
            place = places.get(0);
            CharSequence attributions = places.getAttributions();

            tvName.setText(Html.fromHtml(place.getName() + ""));
            tvAddress.setText(Html.fromHtml(place.getAddress() + ""));
            tvLatLong.setText(Html.fromHtml(place.getLatLng().toString() + ""));

            if (attributions != null) {
                tvAtt.setText(Html.fromHtml(attributions.toString()));
            }
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
