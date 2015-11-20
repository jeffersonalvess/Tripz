package edu.depaul.csc472.tripz;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Miller on 19/11/2015.
 */
public class Day /*extends Date*/{
    Integer index;
    Date date;
    private ArrayList<Place> list_place;

    public Day(Integer index, Date date){
        this.index = index;
        this.date = date;
        list_place = new ArrayList<Place>();
    }

    public Day(Integer index, Date date, ArrayList<Place> list_place){
        this.index = index;
        this.date = date;
        this.list_place = list_place;
    }

    public void addPlace(Place place){
        list_place.add(place);
    }

    public void removePlace(int index){
        list_place.remove(index);
    }

    public Place getPlaceFromList(int index){
        return list_place.get(index);
    }

    public Integer getIndex(){return index;}

    public void setIndex(Integer index){this.index = index;}

    public Date getDate(){return date;}

    public void setDate(){this.date = date;}

    public ArrayList<Place> getList_place(){return list_place;}

    public void setList_place(ArrayList<Place> list_place){this.list_place = list_place;}
}
