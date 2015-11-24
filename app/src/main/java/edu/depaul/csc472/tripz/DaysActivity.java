package edu.depaul.csc472.tripz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import edu.depaul.csc472.tripz.helper.City;
import edu.depaul.csc472.tripz.helper.DatabaseHelper;

public class DaysActivity extends AppCompatActivity implements DaysListFragment.Callbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    public int _dayID = -1;
    public int _cityID = -1;
    public int _tripID = -1;
    public String _cityName = "";

    public static LatLng CITY_BOUNDS;
    private static final String LOG_TAG = "DaysActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Trip Days");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        TextView txtTitle = (TextView) findViewById(R.id.txtLine1);
        TextView txtLine1 = (TextView) findViewById(R.id.txtLine2);
        TextView txtLine2 = (TextView) findViewById(R.id.txtLine3);
        ImageView imgTrip = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        int cityID = intent.getIntExtra("CityID", -1);
        int tripID = intent.getIntExtra("TripID", -1);
        _cityID = cityID;
        _tripID = tripID;

        getCityBounds();

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        final City c = databaseHelper.getCity(cityID);

        _cityName = c.getName();
        txtTitle.setText(c.getName());
        imgTrip.setImageResource(R.mipmap.ic_location_city_white);
        txtLine2.setVisibility(View.INVISIBLE);
        txtLine2.setText(String.valueOf(cityID));

        txtLine1.setVisibility(View.INVISIBLE);

        //Fragment call << Need improvement to implement tablet compatibility>>
        ((DaysListFragment) getFragmentManager().findFragmentById(R.id.days_list)).setActivateOnItemClick(true);
    }

    //This method is create to support Callbacks of DaysListFragment!!!
    @Override
    public void onItemSelected(int id, int dayNumber) {
        /* Use this when implement two panel layout, it's not implement yet. But I'll left the code for example.

        /*if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(WineDetailFragment.ARG_ITEM_ID, id);
            WineDetailFragment fragment = new WineDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.wine_detail_container, fragment)
                    .commit();

        }
        else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, WineDetailActivity.class);
            detailIntent.putExtra(WineDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }*/


        Intent cityIntent = new Intent(DaysActivity.this, PlacesActivity.class);
        cityIntent.putExtra("DayID", id);
        cityIntent.putExtra("DayIndex", dayNumber);
        cityIntent.putExtra("CityID", _cityID);
        cityIntent.putExtra("TripID", _tripID);
        cityIntent.putExtra("CityName", _cityName);
        startActivity(cityIntent);
    }

    private void getCityBounds(){
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
            CITY_BOUNDS = places.get(0).getLatLng();
            Log.i(LOG_TAG, "Hierarchy");
        }
    };

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
}
