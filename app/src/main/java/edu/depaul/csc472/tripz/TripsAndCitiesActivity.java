package edu.depaul.csc472.tripz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.depaul.csc472.tripz.helper.City;
import edu.depaul.csc472.tripz.helper.DatabaseHelper;
import edu.depaul.csc472.tripz.helper.OurDate;
import edu.depaul.csc472.tripz.helper.Trip;

public class TripsAndCitiesActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    int day, month, year, dayS, monthS, yearS;
    String whoIsMyMother = "";
    String dateStart, dateEnd;
    final static int DIALOG_ID1 = 0;
    final static int DIALOG_ID2 = 1;

    private static final String LOG_TAG = "TripsAndCities";

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private static final int GOOGLE_API_CLIENT_ID = 3;

    private static LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private AutoCompleteTextView mEditCity;
    private Place place;
    private String _placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_and_cities);

        setAutoComplete();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Button btnStart = (Button) findViewById(R.id.btnStart);
        final Button btnEnd = (Button) findViewById(R.id.btnEnd);
        TextView txtTrip = (TextView) findViewById(R.id.txtTrip);
        final TextView txtCity = (TextView) findViewById(R.id.txtVCity);
        final TextView startDate = (TextView) findViewById(R.id.txtStartDate);
        final TextView endDate = (TextView) findViewById(R.id.txtEndDate);
        final EditText editTrip = (EditText) findViewById(R.id.editTrip);
        final EditText editCity = (EditText) findViewById(R.id.editCity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        final Intent intent = getIntent();
        if (intent != null)
            whoIsMyMother = intent.getStringExtra("activityMother");


        if (whoIsMyMother.equals("MainActivity")) {

            toolbar.setTitle("New Trip");
            txtCity.setVisibility(View.GONE);
            editCity.setVisibility(View.GONE);
            startDate.setVisibility(View.INVISIBLE);
            endDate.setVisibility(View.INVISIBLE);
            btnEnd.setVisibility(View.INVISIBLE);
            btnStart.setVisibility(View.INVISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String tripName = String.valueOf(editTrip.getText());
                    String snack = "You need to give a name for your new trip.";

                    if (!tripName.equals("")) {
                        Trip t = new Trip(tripName);
                        DatabaseHelper d = new DatabaseHelper(getApplicationContext());
                        d.createTrip(t);

                        Toast.makeText(TripsAndCitiesActivity.this, "Trip created.", Toast.LENGTH_LONG).show();
                        onBackPressed();
                        finish();
                    }
                    else
                        Toast.makeText(TripsAndCitiesActivity.this, snack, Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (whoIsMyMother.equals("CitiesActivity")) {

            toolbar.setTitle("New City");
            txtTrip.setEnabled(true);
            editTrip.setEnabled(false);
            btnEnd.setEnabled(false);
            startDate.setVisibility(View.INVISIBLE);
            endDate.setVisibility(View.INVISIBLE);

            editTrip.setText( intent.getStringExtra("tripName"));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: Finish the save button

                    int tripID = intent.getIntExtra("tripID", -1);
                    String cityName = String.valueOf(mEditCity.getText());

                    try {
                        if (!cityName.equals("") && isDateOk(dateStart, dateEnd)) {
                            City c = new City(tripID, _placeId, cityName, new OurDate(String.valueOf(dateStart)), new OurDate((String.valueOf(dateEnd))));
                            DatabaseHelper d = new DatabaseHelper(getApplicationContext());
                            d.createCity(c);

                            Toast.makeText(TripsAndCitiesActivity.this, "City added.", Toast.LENGTH_LONG).show();
                            Intent intent1 = new Intent();
                            intent1.putExtra("success", true);
                            setResult(RESULT_OK, intent1);
                            finish();
                        }
                        else
                            Toast.makeText(TripsAndCitiesActivity.this, "You need to provide a city name to create a new city.", Toast.LENGTH_LONG).show();

                    } catch (ParseException e) {
                        Toast.makeText(TripsAndCitiesActivity.this, "Please check your dates.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
            });
        }


        View.OnClickListener showDatePickerDialog = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btnDate = (Button) v;

                if (btnDate.getText() == btnStart.getText()) {
                    final Calendar dateNow = Calendar.getInstance();

                    year = dateNow.get(Calendar.YEAR);
                    month = dateNow.get(Calendar.MONTH);
                    day = dateNow.get(Calendar.DAY_OF_MONTH);

                    showDialog(DIALOG_ID1);
                }
                else {
                    year =yearS;
                    month = monthS;
                    day = dayS;
                    showDialog(DIALOG_ID2);
                }

            }
        };

        View.OnClickListener showDatePickerDialog2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btnDate = (Button) v;
                final Calendar dateNow = Calendar.getInstance();

                year = dateNow.get(Calendar.YEAR);
                month = dateNow.get(Calendar.MONTH);
                day = dateNow.get(Calendar.DAY_OF_MONTH);

                if (btnDate.getText() == btnStart.getText())
                    showDialog(DIALOG_ID1);
                else
                    showDialog(DIALOG_ID2);

            }
        };

        btnStart.setOnClickListener(showDatePickerDialog);
        btnEnd.setOnClickListener(showDatePickerDialog2);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID1)
            return new DatePickerDialog(this, datePickerListenerStart, year, month, day);
        else if (id == DIALOG_ID2)
            return new DatePickerDialog(this, datePickerListenerEnd, year, month, day);
        else
            return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int _year, int monthOfYear, int dayOfMonth) {

            final Button btnEnd = (Button) findViewById(R.id.btnEnd);
            final TextView txtStart = (TextView) findViewById(R.id.txtStartDate);
            final TextView txtEnd = (TextView) findViewById(R.id.txtEndDate);
            final Calendar dateNow = Calendar.getInstance();

            String dialogDate = String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(_year);

            try {

                if (isDateOk(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH) + 1, dateNow.get(Calendar.DAY_OF_MONTH), _year, monthOfYear + 1, dayOfMonth)) {

                    yearS = _year;
                    monthS = monthOfYear + 1;
                    dayS = dayOfMonth;

                    txtStart.setVisibility(View.VISIBLE);
                    txtEnd.setVisibility(View.INVISIBLE);
                    txtStart.setText(dialogDate);
                    btnEnd.setEnabled(true);

                    dateStart = String.valueOf(yearS) + "/" + String.valueOf(monthS) + "/" + String.valueOf(dayS);

                } else {
                    Toast.makeText(TripsAndCitiesActivity.this, "The date should be equal or higher than today's date.", Toast.LENGTH_LONG).show();

                    txtStart.setVisibility(View.INVISIBLE);
                    txtEnd.setVisibility(View.INVISIBLE);
                    btnEnd.setEnabled(false);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int _year, int monthOfYear, int dayOfMonth) {

            final Button btnEnd = (Button) findViewById(R.id.btnEnd);
            final TextView txtStart = (TextView) findViewById(R.id.txtStartDate);
            final TextView txtEnd = (TextView) findViewById(R.id.txtEndDate);

            String dialogDate = String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(_year);

            try {
                if (isDateOk(yearS, monthS, dayS, _year, monthOfYear + 1, dayOfMonth)) {
                    txtStart.setVisibility(View.VISIBLE);
                    txtEnd.setVisibility(View.VISIBLE);
                    txtEnd.setText(dialogDate);
                    btnEnd.setEnabled(true);

                    dateEnd = String.valueOf(_year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth);

                } else {
                    Toast.makeText(TripsAndCitiesActivity.this, "The date should be equal or highre than start's date.", Toast.LENGTH_LONG).show();
                    txtEnd.setVisibility(View.INVISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };

    protected boolean isDateOk(int _startYear, int _startMouth, int _startDay, int _endYear, int _endMouth, int _endDay) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String paramDateStart = String.valueOf(_startYear) + "-" + String.valueOf(_startMouth) + "-" + String.valueOf(_startDay);
        String paramDateEnd = String.valueOf(_endYear) + "-" + String.valueOf(_endMouth) + "-" + String.valueOf(_endDay);

        Date start = dateFormat.parse(paramDateStart);
        Date end = dateFormat.parse(paramDateEnd);

        if (start.after(end))
            return false;

        return true;
    }

    protected boolean isDateOk(String start, String end) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Date s = dateFormat.parse(start);
        Date e = dateFormat.parse(end);

        if (s.after(e))
            return false;

        return true;
    }

    private void setAutoComplete(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mEditCity = (AutoCompleteTextView) findViewById(R.id.editCity);

        mEditCity.setThreshold(3);

        mEditCity.setOnItemClickListener(mAutocompleteClickListener);

        ArrayList<Integer> filterTypes = new ArrayList<Integer>();
        filterTypes.add(Place.TYPE_GEOCODE);
        filterTypes.add(Place.TYPE_LOCALITY);
        filterTypes.add(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_3);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, filterTypes);


        mEditCity.setAdapter(mPlaceArrayAdapter);
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

            _placeId = placeId;

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
            // Selecting the first object buffer.
            place = places.get(0);
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
