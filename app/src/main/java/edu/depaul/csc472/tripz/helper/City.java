package edu.depaul.csc472.tripz.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Miller on 19/11/2015.
 */
public class City {
    private Integer id;
    private Integer id_trip;
    private String name;
    private ArrayList<Day> list_day;
    private Date start;
    private Date end;

    // Atributo de formatação de datas
    // *** obs: Qualquer dúvida a respeito da classe Date me consultar,
    // ***      pois é uma bosta trabalhar com essa maldita classe. :/
    public static SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");

    public City(String name){
        this.name = name;
    }

    public City() {}

    public City(String name, Date start, Date end){
        this.name = name;
        this.start = start;
        this.end = end;
        list_day = new ArrayList<Day>();

        long dif = duration();

//        Teste de formatação das datas
//        System.out.println("Dia " + WelcomeScreen.curFormater.format(start) + " ao dia "
//                + WelcomeScreen.curFormater.format(end));

        for(int i = 0; i <= dif; i++) {
            list_day.add(new Day(i + 1, addDaysOnDate(start, i)));

//            Teste de formatação das datas
//            Date d = addDaysOnDate(start, i);
//            list_day.add(new Day(i + 1, d));
//            System.out.println("Dia " + (i+1) + ": " + WelcomeScreen.curFormater.format(d));
        }
    }

    public void setId(Integer id){ this.id = id;}

    public Integer getId(){ return this.id; }

    public void setIdTrip(Integer id){ this.id_trip = id;}

    public Integer getIdTrip(){ return this.id_trip;}

    public void addDay(Day day){
        list_day.add(day);
    }

    public void removeDay(int index){
        list_day.remove(index);
    }

    public Day getDayFromList(int index){
        return list_day.get(index);
    }

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public Date getStart(){return start;}

    public void setStart(Date start){this.start = start;}

    public Date getEnd(){return end;}

    public void setEnd(Date end){this.end = end;}

    public long duration(){

        //milliseconds
        long different = end.getTime() - start.getTime();

//        System.out.println("startDate : " + startDate);
//        System.out.println("endDate : "+ endDate);
//        System.out.println("different : " + different);

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

    private Date addDaysOnDate(Date date, int days){
        long daysInMilli = 1000 * 60 * 60 * 24 * days;

        return new Date(date.getTime() + daysInMilli);
    }
}
