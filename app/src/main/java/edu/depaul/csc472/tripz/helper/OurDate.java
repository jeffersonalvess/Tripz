package edu.depaul.csc472.tripz.helper;

import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Miller on 22/11/2015.
 */
public class OurDate extends Date{
    public static final SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat americanDate = new SimpleDateFormat("MM/dd/yyyy");

    public OurDate(){
        super();
    }

    public OurDate(Date date){
        super(date.getTime());
    }

    public OurDate(long milli){
        super(milli);
    }

    public OurDate(String s){
        super(curFormater.parse(s, new ParsePosition(0)).getTime());
    }

    public void setDate(Date date){
        setTime(date.getTime());
    }

    public void setDate(String date){
        setTime(new OurDate(date).getTime());
    }

    public String toString(){
        return curFormater.format(this);
    }

    public String getAmericanDate(){return americanDate.format(this);}

    public void setAmericanDate(String s){
        setTime(americanDate.parse(s, new ParsePosition(0)).getTime());
    }

    public OurDate addDaysOnDate(int days){
        long daysInMilli = 1000 * 60 * 60 * 24 * (long) days;

        //Log.i("Vai estourar: "daysInMilli)
        return new OurDate(getTime() + daysInMilli);
    }

    public long duration(Date date){
        long different;
        //milliseconds
        if(before(date))
            different = date.getTime() - getTime();
        else
            different = getTime() - date.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;

//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;

//        System.out.printf(
//                "%d days, %d hours, %d minutes, %d seconds%n",
//                elapsedDays,
//                elapsedHours, elapsedMinutes, elapsedSeconds);

        return elapsedDays;
    }

    public static OurDate stringToDate(String s){
        return new OurDate(s);
    }
}
