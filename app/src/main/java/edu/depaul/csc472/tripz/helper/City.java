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
    private OurDate start;
    private OurDate end;

    // Atributo de formatação de datas
    // *** obs: Qualquer dúvida a respeito da classe Date me consultar,
    // ***      pois é uma bosta trabalhar com essa maldita classe. :/

    public City(String name){
        this.name = name;
    }

    public City() {}

    public City(String name, OurDate start, OurDate end){
        this.name = name;
        this.start = start;
        this.end = end;
        list_day = new ArrayList<Day>();

        long dif = start.duration(end);

//        Teste de formatação das datas
//        System.out.println("Dia " + WelcomeScreen.curFormater.format(start) + " ao dia "
//                + WelcomeScreen.curFormater.format(end));

        for(int i = 0; i <= dif; i++) {
            list_day.add(new Day(i + 1, start.addDaysOnDate(i)));

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

    public OurDate getStart(){return start;}

    public String getStartString(){return start.toString();}

    public void setStart(OurDate start){this.start = start;}

    public Date getEnd(){return end;}

    public String getEndString(){return end.toString();}

    public void setEnd(OurDate end){this.end = end;}
}
