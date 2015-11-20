package edu.depaul.csc472.tripz;

import java.util.ArrayList;

/**
 * Created by Miller on 19/11/2015.
 */

public class Trip {
    private String name;
    private ArrayList<City> city_list;

    public Trip(String name){
        this.name = name;
        city_list = new ArrayList<City>();
    }

    public Trip(String name, ArrayList<City> city_list){
        this.name = name;
        this.city_list = city_list;
    }

    public void addCity(City city){
        city_list.add(city);
    }

    public void removeCity(int index) {
        city_list.remove(index);
    }

    public void removeCity(String name){
        // Método inútil, não sei pq criei essa merda. --'
        for(int i = 0; i < city_list.size(); i++){
            if(name.equals(city_list.get(i).getName())){
                city_list.remove(i);
                i = city_list.size();
            }
        }
    }

    public City getCityFromList(int index){return city_list.get(index);}

    public Integer findCity(String name){
        Integer ret = -1;

        for(int i = 0; i < city_list.size(); i++){
            if(name.equals(city_list.get(i).getName())){
                ret = i;
                i = city_list.size();
            }
        }

        return ret;
    }

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public ArrayList<City> getCity_list(){return city_list;}

    public void setCity_list(ArrayList<City> city_list){this.city_list = city_list;}
}
