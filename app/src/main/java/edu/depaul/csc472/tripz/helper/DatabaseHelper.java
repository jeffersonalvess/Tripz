package edu.depaul.csc472.tripz.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by matheuscz on 11/21/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Table Names

    private static final String TABLE_TRIP = "trip";
    private static final String TABLE_CITY = "city";
    private static final String TABLE_DAY = "day";
    private static final String TABLE_OURPLACE = "ourplace";

    // Common column names

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";

    // TRIP Table - column names

    // CITY Table - column names

    private static final String KEY_ID_TRIP = "id_trip";

    // DAY Table - column names

    private static final String KEY_ID_CITY = "id_city";
    private static final String KEY_INDEX = "index";
    private static final String KEY_DATE = "date";

    //PLACE Table - columns names

    private static final String KEY_ID_DAY = "id_day";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_OPEN_HOURS = "open_hours";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_VISITED = "visited";

    // Table Create Statements

    //TRIP table create statement

    private static final String CREATE_TABLE_TRIP = "CREATE TABLE "
            + TABLE_TRIP + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_START_DATE + " INTEGER,"
            + KEY_END_DATE + " INTEGER"
            + ")";

    //CITY table create statement

    private static final String CREATE_TABLE_CITY = "CREATE TABLE "
            + TABLE_CITY + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_TRIP + " INTEGER,"
            + KEY_NAME + " TEXT,"
            + KEY_START_DATE + " INTEGER,"
            + KEY_END_DATE + " INTEGER"
            + ")";

    //DAY table create statement

    private static final String CREATE_TABLE_DAY = "CREATE TABLE "
            + TABLE_DAY + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_CITY + " INTEGER,"
            + KEY_INDEX + " INTEGER,"
            + KEY_DATE + " INTEGER"
            + ")";

    //PLACE table create statement

    private static final String CREATE_TABLE_OURPLACE = "CREATE TABLE "
            + TABLE_OURPLACE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_DAY + " INTEGER,"
            + KEY_NAME + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_OPEN_HOURS + " INTEGER,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_VISITED + " INTEGER"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_TRIP);
        db.execSQL(CREATE_TABLE_CITY);
        db.execSQL(CREATE_TABLE_DAY);
        db.execSQL(CREATE_TABLE_OURPLACE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OURPLACE);

        //create new ones

        onCreate(db);

    }

    //----------------  TRIP -----------------------------------------------------------------------
    public long createTrip (Trip trip)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID,);
        values.put(KEY_NAME, trip.getName());
        values.put(KEY_START_DATE, trip.getStartDate());
        values.put(KEY_END_DATE, trip.getEndDate());

        long trip_id = db.insert(TABLE_TRIP, null, values);

        return trip_id;
    }

    public ArrayList<Trip> getTrips()
    {
        ArrayList<Trip> trips = new ArrayList<Trip>();
        String selectQuery = "SELECT * FROM " + TABLE_TRIP;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst() )
        {
            do {
                Trip aux = new Trip();
                //aux.setId(c.getInt((c.getColumnIndex(KEY_ID)) ) );
                aux.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                //aux.setStartDate(c.getInt(c.getColumnIndex(KEY_START_DATE)));
                //aux.setEndDate(c.getInt(c.getColumnIndex(KEY_END_DATE)));

                trips.add(aux);
            }while(c.moveToNext());
        }

        return trips;
    }

    public int updateTrip(Trip trip)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID,);
        values.put(KEY_NAME, trip.getName());
        values.put(KEY_START_DATE, trip.getStartDate());
        values.put(KEY_END_DATE, trip.getEndDate());

        //updating

        return db.update(TABLE_TRIP, values, KEY_ID + " = ?",
                new String[] { String.valueOf(trip.getId())});
    }

    public void deleteTrip(long trip_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIP, KEY_ID + " = ?",
                new String[]{String.valueOf(trip_id)});
    }

    //---------------- CITY ------------------------------------------------------------------------
    public long createDay (City city)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, city.getId());
        values.put(KEY_ID_TRIP, city.getIdTrip());
        values.put(KEY_NAME, city.getName());
        //values.put(KEY_START_DATE, city.getStart());
        //values.put(KEY_END_DATE, city.getEnd());

        long city_id = db.insert(TABLE_CITY, null, values);

        return city_id;
    }

    public ArrayList<City> getCities()
    {
        ArrayList<City> cities = new ArrayList<City>();
        String selectQuery = "SELECT * FROM " + TABLE_CITY;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst() )
        {
            do {
                City aux = new City();
                //aux.setId(c.getInt((c.getColumnIndex(KEY_ID)) ) );
                //aux.setIdTrip(c.getInt(c.getColumnIndex(KEY_ID_TRIP)) );
                aux.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                //aux.setStart(c.getInt(c.getColumnIndex(KEY_START_DATE));
                //aux.setEnd(c.getInt(c.getColumnIndex(KEY_END_DATE)));

                cities.add(aux);
            }while(c.moveToNext());
        }

        return cities;
    }

    public int updateCity(City city)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID,);
        //values.put(KEY_ID_TRIP,);
        values.put(KEY_NAME, city.getName());
        //values.put(KEY_START_DATE, city.getStart());
        //values.put(KEY_END_DATE, city.getEnd());

        //updating

        return db.update(TABLE_CITY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(city.getId())});
    }

    public void deleteCity(long city_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIP, KEY_ID + " = ?",
                new String[] {String.valueOf(city_id)});
    }

    //---------------- DAY  ------------------------------------------------------------------------
    public long createDay (Day day)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID,);
        //values.put(KEY_ID_CITY,);
        values.put(KEY_INDEX, day.getIndex());
        //values.put(KEY_DATE, day.getDate());

        long day_id = db.insert(TABLE_DAY, null, values);

        return day_id;
    }

    public ArrayList<Day> getDays()
    {
        ArrayList<Day> days = new ArrayList<Day>();
        String selectQuery = "SELECT * FROM " + TABLE_DAY;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst() )
        {
            do {
                Day aux = new Day();
                //aux.setId(c.getInt((c.getColumnIndex(KEY_ID)) ) );
                //aux.setIdCity(c.getInt((c.getColumnIndex(KEY_ID_CITY)) ) );
                aux.setIndex(c.getInt(c.getColumnIndex(KEY_INDEX)));
                //aux.setDate(c.getInt(c.getColumnIndex(KEY_DATE)));

                days.add(aux);
            }while(c.moveToNext());
        }

        return days;
    }

    public int updateDay(Day day)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID,);
        //values.put(KEY_ID_CITY,);
        values.put(KEY_INDEX, day.getIndex());
        //values.put(KEY_DATE, day.getDate());

        //updating

        return db.update(TABLE_DAY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(day.getId())});
    }

    public void deleteDay(long day_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DAY, KEY_ID + " = ?",
                new String[] {String.valueOf(day_id)});
    }

    //----------------  PLACE  ---------------------------------------------------------------------

    public long createPlace (OurPlace place)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID,);
        //values.put(KEY_ID_DAY,);
        values.put(KEY_NAME, place.getName());
        values.put(KEY_DESCRIPTION, place.getDescription());
        //values.put(KEY_OPEN_HOURS, place.getOpen_hours());
        values.put(KEY_ADDRESS, place.getAddress());
        values.put(KEY_VISITED, place.getVisited());

        long place_id = db.insert(TABLE_OURPLACE, null, values);

        return place_id;
    }

    public ArrayList<OurPlace> getPlaces()
    {
        ArrayList<OurPlace> places = new ArrayList<OurPlace>();
        String selectQuery = "SELECT * FROM " + TABLE_OURPLACE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst() )
        {
            do {
                OurPlace aux = new OurPlace();
                //aux.setId(c.getInt((c.getColumnIndex(KEY_ID)) ) );
                //aux.setIdDay(c.getInt((c.getColumnIndex(KEY_ID_DAY)) ) );
                aux.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                aux.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                //aux.setOpen_hours(c.getInt(c.getColumnIndex(KEY_OPEN_HOURS)));
                aux.setName(c.getString(c.getColumnIndex(KEY_ADDRESS)));
                aux.setVisited(c.getInt(c.getColumnIndex(KEY_VISITED)));

                places.add(aux);
            }while(c.moveToNext());
        }

        return places;
    }

    public int updatePlace(OurPlace place)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID,);
        //values.put(KEY_ID_DAY,);
        values.put(KEY_NAME, place.getName());
        values.put(KEY_DESCRIPTION, place.getDescription());
        //values.put(KEY_OPEN_HOURS, place.getOpen_hours());
        values.put(KEY_ADDRESS, place.getAddress());
        values.put(KEY_VISITED, place.getVisited());

        //updating

        return db.update(TABLE_OURPLACE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(place.getId())});
    }

    public void deletePlace(long place_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OURPLACE, KEY_ID + " = ?",
                new String[] {String.valueOf(place_id)});
    }
}
