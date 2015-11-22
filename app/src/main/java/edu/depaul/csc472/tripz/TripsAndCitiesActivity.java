package edu.depaul.csc472.tripz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.depaul.csc472.tripz.helper.City;
import edu.depaul.csc472.tripz.helper.DatabaseHelper;
import edu.depaul.csc472.tripz.helper.OurDate;
import edu.depaul.csc472.tripz.helper.Trip;

public class TripsAndCitiesActivity extends AppCompatActivity {

    int day, month, year, dayS, monthS, yearS;
    String whoIsMyMother = "";
    String dateStart, dateEnd;
    final static int DIALOG_ID1 = 0;
    final static int DIALOG_ID2 = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_and_cities);

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

                        Snackbar.make(view, "Trip created.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        onBackPressed();
                        finish();
                    }

                    Snackbar.make(view, snack, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        } else if (whoIsMyMother.equals("CitiesActivity")) {

            toolbar.setTitle("New City");
            txtTrip.setEnabled(false);
            editTrip.setEnabled(false);
            btnEnd.setEnabled(false);
            startDate.setVisibility(View.INVISIBLE);
            endDate.setVisibility(View.INVISIBLE);


            String tripName = intent.getStringExtra("tripName");
            editTrip.setText(tripName);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tripID = intent.getIntExtra("tripID", -1);
                    String cityName = String.valueOf(editCity.getText());

                    try {
                        if (!cityName.equals("") && isDateOk(dateStart, dateEnd)) {
                            City c = new City(tripID, cityName, new OurDate(String.valueOf(dateStart)), new OurDate((String.valueOf(dateEnd))));
                            DatabaseHelper d = new DatabaseHelper(getApplicationContext());
                            d.createCity(c);

                            Snackbar.make(view, "City added.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            onBackPressed();
                            finish();
                        }
                    } catch (ParseException e) {
                        Snackbar.make(view, "Please check your dates.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        e.printStackTrace();
                    }

                    Snackbar.make(view, "You need to provide a city name to create a new city.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }


        View.OnClickListener showDatePickerDialog = new View.OnClickListener() {
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
        btnEnd.setOnClickListener(showDatePickerDialog);

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

                    dateStart = String.valueOf(yearS) + "/" + String.valueOf(monthS) + String.valueOf(dayS);

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

                    dateEnd = String.valueOf(_year) + "/" + String.valueOf(monthOfYear + 1) + String.valueOf(dayOfMonth);

                } else {
                    Toast.makeText(TripsAndCitiesActivity.this, "The date should be equal or higher than start's date.", Toast.LENGTH_LONG).show();
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

}
