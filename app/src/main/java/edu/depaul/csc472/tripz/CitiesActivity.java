package edu.depaul.csc472.tripz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.depaul.csc472.tripz.helper.DatabaseHelper;
import edu.depaul.csc472.tripz.helper.Trip;

public class CitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        Intent intent = getIntent();
        final int tripID = intent.getIntExtra("tripID", -1);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        final Trip t = new Trip(); //databaseHelper.getTrip(tripID);


        TextView txtTitle = (TextView) findViewById(R.id.txtLine1);
        TextView txtLine1 = (TextView) findViewById(R.id.txtLine2);
        TextView txtLine2 = (TextView) findViewById(R.id.txtLine3);
        ImageView imgTrip = (ImageView) findViewById(R.id.imageView);

        txtTitle.setText(t.getName());
        txtLine1.setText(t.getStart().getAmericanDate() + " - " + t.getEnd().getAmericanDate());
        txtLine2.setVisibility(View.INVISIBLE);
        imgTrip.setImageResource(android.R.drawable.ic_dialog_map);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitiesActivity.this, TripsAndCitiesActivity.class);
                intent.putExtra("activityMother", "CitiesActivity");
                intent.putExtra("tripID", tripID);
                intent.putExtra("tripName", t.getName());
                startActivity(intent);
            }
        });

        //TODO: implement create city screen
        //TODO: implement fragments


    }
}
