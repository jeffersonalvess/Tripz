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
    private ArrayList<OurPlace> list_place;

    public Day(Integer index, Date date){
        this.index = index;
        this.date = date;
        list_place = new ArrayList<OurPlace>();
    }

    public Day(Integer index, Date date, ArrayList<OurPlace> list_place){
        this.index = index;
        this.date = date;
        this.list_place = list_place;
    }

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

    public void setDate(){this.date = date;}

    public ArrayList<OurPlace> getList_place(){return list_place;}

    public void setList_place(ArrayList<OurPlace> list_place){this.list_place = list_place;}
}
