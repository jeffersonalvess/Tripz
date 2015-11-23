package edu.depaul.csc472.tripz.helper;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Miller on 19/11/2015.
 */
public class City {
    private Integer id;
    private Integer id_trip;
    private String id_maps;
    private String name;
    private ArrayList<Day> list_day;
    private OurDate start;
    private OurDate end;

    // Atributo de formatação de datas
    // *** obs: Qualquer dúvida a respeito da classe Date me consultar,
    // ***      pois é uma bosta trabalhar com essa maldita classe. :/

    public City(String name){
        this.name = name;
    }

    public City() {}

    public City(Integer idTrip, String id_maps, String name, OurDate start, OurDate end){
        this.name = name;
        this.id_trip = idTrip;
        this.start = start;
        this.end = end;
        this.id_maps = id_maps;
        list_day = new ArrayList<Day>();

        long dif = start.duration(end);

        for(int i = 0; i <= dif; i++) {
            list_day.add(new Day(i + 1, start.addDaysOnDate(i)));
            Log.i("DayCreation", list_day.get(i).getIndex().toString());
            Log.i("DayCreation", list_day.get(i).getDateString().toString());
        }
    }

    public String getId_maps(){return id_maps;}

    public void setId_maps(String id_maps){this.id_maps = id_maps;}

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

    public OurDate getStart(){return start;}

    public String getStartString(){return start.toString();}

    public void setStart(OurDate start){this.start = start;}

    public OurDate getEnd(){return end;}

    public String getEndString(){return end.toString();}

    public void setEnd(OurDate end){this.end = end;}

    public ArrayList<Day> getList_day(){return list_day;}
}
