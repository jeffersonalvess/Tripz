package edu.depaul.csc472.tripz.helper;

import java.util.ArrayList;

/**
 * Created by Miller on 19/11/2015.
 */

public class Trip {
    private Integer id;
    private String name;

    public void setStart(OurDate start) {
        this.start = start;
    }

    private OurDate start;
    private OurDate end;
    private ArrayList<City> city_list;

    public Trip(String name) {
        this.name = name;
        city_list = new ArrayList<City>();
        start = new OurDate("1992/02/28");
        end = new OurDate("1992/02/28");
    }

    public Trip(Integer id, String name) {
        this.id = id;
        this.name = name;
        city_list = new ArrayList<City>();
        start = new OurDate("1992/02/28");
        end = new OurDate("1992/02/28");
    }

    public Trip(){
        city_list = new ArrayList<City>();
        start = new OurDate("1992/02/28");
        end = new OurDate("1992/02/28");};

    public Trip(String name, ArrayList<City> city_list) {
        this.name = name;
        this.city_list = city_list;
        start = new OurDate("1992/02/28");
        end = new OurDate("1992/02/28");
    }

    public void setId(Integer id){this.id = id;}

    public Integer getId(){return this.id;}

    public void addCity(City city) {
        city_list.add(city);

        if(city.getStart().before(start) && city.getEnd().before(end)){
            start.setDate(city.getStart());
        }
        else if(city.getStart().after(start) && city.getEnd().after(end)){
            end.setDate(city.getEnd());
        }
    }

    public void removeCity(int index) {
        city_list.remove(index);
        OurDate temp = new OurDate(start);
        start.setDate(end);
        end.setDate(temp);

        for(int i = 0; i < city_list.size(); i++){
            if(city_list.get(i).getStart().before(start)){
                start.setDate(city_list.get(i).getStart());
            }
            if(city_list.get(i).getEnd().after(end)){
                end.setDate(city_list.get(i).getEnd());
            }
        }
    }

    public void removeCity(String name) {
        // Useless method, I don't know why I've created this shit. --'
        for (int i = 0; i < city_list.size(); i++) {
            if (name.equals(city_list.get(i).getName())) {
                city_list.remove(i);
                i = city_list.size();
            }
        }
    }

    public City getCityFromList(int index) {
        return city_list.get(index);
    }

    public Integer findCity(String name) {
        Integer ret = -1;

        for (int i = 0; i < city_list.size(); i++) {
            if (name.equals(city_list.get(i).getName())) {
                ret = i;
                i = city_list.size();
            }
        }

        return ret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OurDate getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start.setDate(start);
    }

    public OurDate getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end.setDate(end);
    }

    public void setEnd(OurDate end) {
        this.end.setDate(end);
    }

    public ArrayList<City> getCity_list() {
        return city_list;
    }

    public void setCity_list(ArrayList<City> city_list) {
        this.city_list = city_list;
    }

    public String toString() {
        return name;
    }
}
