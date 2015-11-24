package edu.depaul.csc472.tripz;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity {

    public int _dayID = -1;
    public int _cityID = -1;
    public int _tripID = -1;
    public int _dayNumber = -1;

    private static final String LOG_TAG = "PlacesActivity";

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private static final int GOOGLE_API_CLIENT_ID = 3;

    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        toolbar.setTitle("Trip Days");
        collapsingToolbarLayout.setTitle("");
        collapsingToolbarLayout.setTitleEnabled(false);

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


        Intent i = getIntent();
        String cityName = i.getStringExtra("CityName");
        _dayID = i.getIntExtra("DayID", _dayID);
        _cityID = i.getIntExtra("CityID", _cityID);
        _tripID = i.getIntExtra("TripID", _tripID);
        _dayNumber = i.getIntExtra("DayIndex", _dayNumber);

        //TODO: Get day from database

        TextView txtTitle = (TextView) findViewById(R.id.txtLine1);
        TextView txtLine1 = (TextView) findViewById(R.id.txtLine2);
        TextView txtLine2 = (TextView) findViewById(R.id.txtLine3);
        ImageView imgTrip = (ImageView) findViewById(R.id.imageView);

        imgTrip.setImageResource(R.mipmap.ic_place_white);
        txtTitle.setText(cityName);
        txtLine1.setText("Day " + _dayNumber);
        txtLine2.setVisibility(View.INVISIBLE);
    }
//
//    private void setAutoComplete(){
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(AppIndex.API)
//                .addApi(Places.GEO_DATA_API)
//                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
//                .addApi(LocationServices.API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        searchView = (AutoCompleteTextView) findViewById(R.id.editCity);
//
//        searchView.setThreshold(3);
//
//        searchView.setOnItemClickListener(mAutocompleteClickListener);
//
//        ArrayList<Integer> filterTypes = new ArrayList<Integer>();
//        filterTypes.add(Place.TYPE_GEOCODE);
//        filterTypes.add(Place.TYPE_LOCALITY);
//        filterTypes.add(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_3);
//
//        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
//                BOUNDS_MOUNTAIN_VIEW, filterTypes);
//
//
//        mEditCity.setAdapter(mPlaceArrayAdapter);
//    }
//
//    private AdapterView.OnItemClickListener mAutocompleteClickListener
//            = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
//            final String placeId = String.valueOf(item.placeId);
//
//            _placeId = placeId;
//
//            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
//
//            Log.i(LOG_TAG, "Selected: " + item.description);
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
//
//        }
//    };
//
//    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
//            = new ResultCallback<PlaceBuffer>() {
//        @Override
//        public void onResult(PlaceBuffer places) {
//            if (!places.getStatus().isSuccess()) {
//                Log.e(LOG_TAG, "Place query did not complete. Error: " +
//                        places.getStatus().toString());
//                return;
//            }
//            // Selecting the first object buffer.
//            place = places.get(0);
//        }
//    };

    private AutoCompleteTextView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_places, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (AutoCompleteTextView) MenuItemCompat.getActionView(searchItem);
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // Configure the search info and add any event listeners...


        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                searchView.clearFocus();
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                searchView.setWidth((getWindowManager().getDefaultDisplay().getWidth()) - 50);
                searchView.setSingleLine(true);
                searchView.setImeOptions(EditorInfo.IME_ACTION_GO);
                searchView.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                return true;  // Return true to expand action view
            }
        };

        // Get the MenuItem for the action item
        MenuItem actionMenuItem = menu.findItem(R.id.action_search);

        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(actionMenuItem, expandListener);

        // Any other things you have to do when creating the options menu…


        return super.onCreateOptionsMenu(menu);
    }
}
