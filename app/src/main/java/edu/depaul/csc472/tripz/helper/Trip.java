package edu.depaul.csc472.tripz.helper;

import java.util.ArrayList;

import edu.depaul.csc472.tripz.helper.City;

/**
 * Created by Miller on 19/11/2015.
 */

public class Trip {
    private Integer id;
    private String name;
    private String startDate;
    private String endDate;
    private ArrayList<City> city_list;

    public Trip(String name) {
        this.name = name;
        city_list = new ArrayList<City>();
    }

    public Trip(){};

    public Trip(String name, ArrayList<City> city_list) {
        this.name = name;
        this.city_list = city_list;
    }

    public void setId(Integer id){this.id = id;}

    public Integer getId(){return this.id;}

    public void addCity(City city) {
        city_list.add(city);
    }

    public void removeCity(int index) {
        city_list.remove(index);
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<City> getCity_list() {
        return city_list;
    }

    public void setCity_list(ArrayList<City> city_list) {
        this.city_list = city_list;
    }
}
