package edu.depaul.csc472.tripz;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import edu.depaul.csc472.tripz.helper.DatabaseHelper;

public class Splashscreen extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final String LOG_TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int _cityID = intent.getIntExtra("CITYID", -1);

        boolean loc = MainActivity.gps_enabled, net = MainActivity.network_enabled;

        MainActivity.gps_enabled = verLocation();
        MainActivity.network_enabled = verNetwork();

        if(MainActivity.gps_enabled) {
            if((MainActivity.gps_enabled != loc)) {
                setContentView(R.layout.activity_splashscreen);
                GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, 2, this)
                        .addApi(Places.PLACE_DETECTION_API)
                        .build();

                PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);

                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                        //Line2 = (TextView) findViewById(R.id.txtLine2);
                        String city = likelyPlaces.get(0).getPlace().getAddress().toString();

                        MainActivity.actual_location = likelyPlaces.get(0).getPlace().getLatLng();

                        int v1, v2;

                        for (v1 = 0; city.charAt(v1) != ','; v1++) ;
                        for (v2 = v1 + 1; city.charAt(v2) != ','; v2++) ;

                        String city2 = city.substring(v1 + 2, v2 + 4);

                        for (v1 = city.length() - 1; city.charAt(v1) != ','; v1--) ;

                        city2 = city2.concat(" - " + city.substring(v1 + 2));
                        likelyPlaces.release();

                        MainActivity.actual_city = city2;

                        setResult(1);
                        finish();
                    }
                });
            }

            if(_cityID > -1 && MainActivity.network_enabled == true) {
                GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(AppIndex.API)
                        .addApi(Places.GEO_DATA_API)
                        .enableAutoManage(this, 5, this)
                        .addApi(LocationServices.API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();

                DatabaseHelper d = new DatabaseHelper(getApplicationContext());

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, d.getCity(_cityID).getId_maps());

                d.closeDB();

                placeResult.setResultCallback(callback);
            }
        }
        else {
            setResult(0);
            finish();
        }
    }

    public static boolean verLocation(){
        boolean ret = false;

        try {
            ret = WelcomeScreen.lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        return ret;
    }

    public static boolean verNetwork(){
        boolean ret = false;

        try {
            ret = WelcomeScreen.lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return ret;
    }

    @Override
    public void onConnected(Bundle bundle) {
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
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    private ResultCallback<PlaceBuffer> callback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            DaysActivity.CITY_BOUNDS = places.get(0).getLatLng();
            Log.i(LOG_TAG, "Hierarchy");

            finish();
        }
    };
}
