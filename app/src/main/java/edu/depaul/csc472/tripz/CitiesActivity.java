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

    int _tripID = -1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        int tripID = intent.getIntExtra("TripID", -1);
        _tripID = tripID;

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        final Trip t = databaseHelper.getTrip(tripID);

        System.out.println("Meu deus olha isso: " + tripID);


        TextView txtTitle = (TextView) findViewById(R.id.txtLine1);
        TextView txtLine1 = (TextView) findViewById(R.id.txtLine2);
        TextView txtLine2 = (TextView) findViewById(R.id.txtLine3);
        ImageView imgTrip = (ImageView) findViewById(R.id.imageView);

        txtTitle.setText(t.getName());
        imgTrip.setImageResource(android.R.drawable.ic_dialog_map);
        txtLine2.setVisibility(View.INVISIBLE);

        if(t.getStart().getAmericanDate().equals("02/28/1992") || t.getEnd().getAmericanDate().equals("02/28/1992"))
            txtLine1.setVisibility(View.INVISIBLE);
        else
            txtLine1.setText(t.getStart().getAmericanDate() + " - " + t.getEnd().getAmericanDate());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitiesActivity.this, TripsAndCitiesActivity.class);
                intent.putExtra("activityMother", "CitiesActivity");
                intent.putExtra("tripID", t.getId());
                intent.putExtra("tripName", t.getName());
                startActivityForResult(intent, 0, savedInstanceState);
            }
        });

        //TODO: implement create city screen
        //TODO: implement fragments

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0) {
            Intent intent = getIntent();
            boolean b = intent.getBooleanExtra("success", false);
            if (b) {

                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                final Trip t = databaseHelper.getTrip(_tripID);

                TextView txtLine1 = (TextView) findViewById(R.id.txtLine2);

                if(t.getStart().getAmericanDate().equals("02/28/1992") || t.getEnd().getAmericanDate().equals("02/28/1992"))
                    txtLine1.setVisibility(View.INVISIBLE);
                else
                    txtLine1.setText(t.getStart().getAmericanDate() + " - " + t.getEnd().getAmericanDate());


            }

        }

    }
}
