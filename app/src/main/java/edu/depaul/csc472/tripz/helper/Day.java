package edu.depaul.csc472.tripz.helper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Miller on 19/11/2015.
 */
public class Day /*extends Date*/{
    private Integer id;
    private Integer id_city;
    Integer index;
    OurDate date;
    private ArrayList<OurPlace> list_place;

    public Day(Integer index, OurDate date){
        this.index = index;
        this.date = date;
        list_place = new ArrayList<OurPlace>();
    }

    public Day(){}

    public Day(Integer idCity, Integer index, OurDate date, ArrayList<OurPlace> list_place){
        this.index = index;
        this.id_city = idCity;
        this.date = date;
        this.list_place = list_place;
    }

    public Day(Integer idCity, Integer index, OurDate date){
        this.index = index;
        this.id_city = idCity;
        this.date = date;
    }

    public void setId(Integer id){ this.id = id;}

    public Integer getId(){ return this.id; }

    public void setIdCity(Integer id){ this.id_city = id;}

    public Integer getIdCity(){ return this.id_city;}

    public void addOurPlace(OurPlace place){
        list_place.add(place);
    }

    public void removeOurPlace(int index){
        list_place.remove(index);
    }

    public OurPlace getOurPlaceFromList(int index){
        return list_place.get(index);
    }

    public Integer getIndex(){return index;}

    public void setIndex(Integer index){this.index = index;}

    public Date getDate(){return date;}

    public String getDateString(){return date.toString();}

    public void setDate(OurDate date){this.date = date;}

    public ArrayList<OurPlace> getList_place(){return list_place;}

    public void setList_place(ArrayList<OurPlace> list_place){this.list_place = list_place;}
}
