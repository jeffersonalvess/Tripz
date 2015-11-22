package edu.depaul.csc472.tripz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

import edu.depaul.csc472.tripz.helper.City;
import edu.depaul.csc472.tripz.helper.DatabaseHelper;
import edu.depaul.csc472.tripz.helper.Day;
import edu.depaul.csc472.tripz.helper.OurDate;
import edu.depaul.csc472.tripz.helper.OurPlace;
import edu.depaul.csc472.tripz.helper.Trip;


public class TesteBanco extends AppCompatActivity {

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_banco);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        getApplicationContext().deleteDatabase("contactsManager");

        db = new DatabaseHelper(getApplicationContext());

        Trip trip1 = new Trip("CALIFORNIA TRIP");
        Trip trip2 = new Trip("NEWYORK JA FOI");
        Trip trip3 = new Trip("WASHINGTON ZUADA");
        Trip trip4 = new Trip("FLORIDA EM BREVE");

        long trip1_id = db.createTrip(trip1);
        long trip2_id = db.createTrip(trip2);
        long trip3_id = db.createTrip(trip3);
        long trip4_id = db.createTrip(trip4);




        Log.d("Trip Count", "Trip Count: " + db.getTrips().size());

        ArrayList<Trip> allTrips = db.getTrips();

        for (Trip t1 : allTrips)
        {
            Log.d("Trip: ", t1.getId()+ " " +t1.getName());
        }


        City city1 = new City(1, "Chicago", new OurDate("2015/08/18"), new OurDate("2015/08/29"));
        City city2 = new City(1, "Sao Paulo", new OurDate("2015/07/18"), new OurDate("2015/07/29"));
        City city3 = new City(2, "Recife", new OurDate("2015/05/18"), new OurDate("2015/05/29"));
        City city4 = new City(3, "Chico", new OurDate("2015/03/18"), new OurDate("2015/03/29"));

        long city1_id = db.createCity(city1);
        long city2_id = db.createCity(city2);
        long city3_id = db.createCity(city3);
        long city4_id = db.createCity(city4);

        Log.d("City Count", "City Count: " + db.getCities().size());

        ArrayList<City> allCities = db.getCities();

        for (City c1 : allCities)
        {
            //Log.d("City: ", c1.getId()+ " "+c1.getIdTrip());
            Log.d("City: ", c1.getId()+ " "+c1.getIdTrip()+" " +c1.getName()+" " +c1.getStartString()+" "+c1.getEndString());
        }


        Day day1 = new Day(2, 1, new OurDate("2014/10/18"));
        Day day2 = new Day(3, 2, new OurDate("2014/10/18"));
        Day day3 = new Day(4, 3, new OurDate("2014/10/18"));
        Day day4 = new Day(2, 4, new OurDate("2014/10/18"));

        long day1_id = db.createDay(day1);
        long day2_id = db.createDay(day2);
        long day3_id = db.createDay(day3);
        long day4_id = db.createDay(day4);

        Log.d("Days Count", "Days Count: " + db.getDays().size());

        ArrayList<Day> allDays = db.getDays();

        for(Day d1 : allDays)
        {
           Log.d("City: ", d1.getId()+" "+d1.getIdCity()+" "+d1.getIndex()+" "+d1.getDate());
        }


        OurPlace place1 = new OurPlace(1, "STARBUCKS", "MUITO BOM", "RUA DE CASA");
        OurPlace place2 = new OurPlace(2, "OUTRO STARBUCKS", "BOM", "NA RUA DE TRAS");
        OurPlace place3 = new OurPlace(3, "MAIS UM STARBUCKS", "OTIMO", "PERTO DO CDM");
        OurPlace place4 = new OurPlace(4, "SEVEN ELEVEN", "VENDE PIZZA PRO PAULOBR", "AQUI DO LADO");

        long place1_id = db.createPlace(place1);
        long place2_id = db.createPlace(place2);
        long place3_id = db.createPlace(place3);
        long place4_id = db.createPlace(place4);

        Log.d("Places Count", "Places Count: " + db.getPlaces().size());

        ArrayList<OurPlace> allPlaces = db.getPlaces();

        for(OurPlace p1 : allPlaces)
        {
            Log.d("Place: ", p1.getId()+" "+p1.getIdDay()+" "+p1.getName()+" "+p1.getDescription()+" "+p1.getAddress()+" "+p1.getVisited() );
        }



        db.closeDB();
    }

}
