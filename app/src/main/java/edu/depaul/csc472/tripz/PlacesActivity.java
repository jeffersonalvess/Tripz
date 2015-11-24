package edu.depaul.csc472.tripz;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import edu.depaul.csc472.tripz.helper.City;
import edu.depaul.csc472.tripz.helper.DatabaseHelper;
import edu.depaul.csc472.tripz.helper.Day;
import edu.depaul.csc472.tripz.helper.OurPlace;

public class PlacesActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        PlacesListFragment.Callbacks{

    public int _dayID = -1;
    public int _cityID = -1;
    public int _tripID = -1;
    public int _dayNumber = -1;

    private static final int REQUEST_PLACE_PICKER = 1;

    private static final String LOG_TAG = "PlacesActivity";

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private static final int GOOGLE_API_CLIENT_ID = 4;

    private LatLng ll;

    //private Place place;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        toolbar.setTitle("Trip Places");
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickButtonClick(view);
            }
        });


        Intent i = getIntent();
        String cityName = i.getStringExtra("CityName");

        _dayID = i.getIntExtra("DayID", _dayID);

        Log.i(LOG_TAG, "DayID: " + _dayID);

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

        setAutoComplete();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        //HAHAHA
        ((PlacesListFragment) getFragmentManager().findFragmentById(R.id.place_list)).setActivateOnItemClick(true);
    }

    private void setAutoComplete() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        ll = DaysActivity.CITY_BOUNDS;

        ArrayList<Integer> filterTypes = new ArrayList<Integer>();
        filterTypes.add(Place.TYPE_ESTABLISHMENT);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this,R.layout.autocomplete_workarround,
                new LatLngBounds(new LatLng(ll.latitude - 0.5, ll.longitude - 0.5),
                        new LatLng(ll.latitude + 0.5, ll.longitude + 0.5)), filterTypes);

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            //_placeId = placeId;

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

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

            //((PlacesListFragment) getFragmentManager().findFragmentById(R.id.place_list)).
            // Selecting the first object buffer.

            String s = "";
            CharSequence att = places.getAttributions();
            if(att != null)
                s=att.toString();

            addPlace(places.get(0), s);
            ((PlacesListFragment) getFragmentManager().findFragmentById(R.id.place_list)).updateList();
        }
    };

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
                searchView.setText("");
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                searchView.setSingleLine(true);
                searchView.setImeOptions(EditorInfo.IME_ACTION_GO);
                searchView.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                searchView.setWidth((getWindowManager().getDefaultDisplay().getWidth()) - (int) searchView.getX() - 50);

                searchView.setThreshold(3);

                searchView.setOnItemClickListener(mAutocompleteClickListener);

                searchView.setAdapter(mPlaceArrayAdapter);

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

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.

            String s = PlacePicker.getAttributions(data);

            if(s == null)
                s = "";

            addPlace(PlacePicker.getPlace(data, this), s);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void addPlace(Place place, String att){
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Day d = databaseHelper.getDay(_dayID);
        OurPlace p = new OurPlace(_dayID, place.getName().toString(), att, place.getAddress().toString());

        d.addOurPlace(p);
        databaseHelper.createPlace(p);

        databaseHelper.closeDB();
    }

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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Places Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.depaul.csc472.tripz/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Places Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.depaul.csc472.tripz/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void onPickButtonClick(View v) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            if(ll != null)
                intentBuilder.setLatLngBounds(new LatLngBounds(
                        new LatLng(ll.latitude-0.002, ll.longitude-0.002),
                        new LatLng(ll.latitude+0.002, ll.longitude+0.002)));

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
    public void onItemSelected(OurPlace place) {

//        Intent intent = new Intent();
//
//        intent.

        Log.i(LOG_TAG, "YES!");
    }
}
