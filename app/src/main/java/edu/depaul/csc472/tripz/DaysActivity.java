package edu.depaul.csc472.tripz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.depaul.csc472.tripz.helper.City;
import edu.depaul.csc472.tripz.helper.DatabaseHelper;
import edu.depaul.csc472.tripz.helper.Trip;

public class DaysActivity extends AppCompatActivity implements DaysListFragment.Callbacks{

    public int _dayID = -1;
    public int _cityID = -1;
    public int _tripID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //TODO: Get an Intent from the CitiesActivity with the city name and Change the WindowTitle
        toolbar.setTitle("Trip");
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

        //TODO: Complete these information with the Information that come from CitiesActivity information
        TextView txtTitle = (TextView) findViewById(R.id.txtLine1);
        TextView txtLine1 = (TextView) findViewById(R.id.txtLine2);
        TextView txtLine2 = (TextView) findViewById(R.id.txtLine3);
        ImageView imgTrip = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        int cityID = intent.getIntExtra("CityID", -1);
        _cityID = cityID;
        int tripID = intent.getIntExtra("TripID", -1);
        _tripID = tripID;

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        final City c = databaseHelper.getCity(cityID);

        txtTitle.setText(c.getName());
        imgTrip.setImageResource(R.mipmap.ic_location_city);
        txtLine2.setVisibility(View.INVISIBLE);
        txtLine2.setText(String.valueOf(cityID));



        txtLine1.setVisibility(View.INVISIBLE);




        //Fragment call << Need improvement to implement tablet compatibility>>
        ((DaysListFragment) getFragmentManager().findFragmentById(R.id.days_list)).setActivateOnItemClick(true);
    }

    //This method is create to support Callbacks of DaysListFragment!!!

    @Override
    public void onItemSelected(int id, long cityID) {
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
        cityIntent.putExtra("CityID", cityID);
        startActivity(cityIntent);
    }
}
