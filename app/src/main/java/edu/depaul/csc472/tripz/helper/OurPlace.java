package edu.depaul.csc472.tripz.helper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Miller on 19/11/2015.
 */
public class OurPlace {
    // Esses atributos foram todos que eu consegui pensar agora as 3:18am...
    // Quem lembrar de mais algum atributo relevante, por favor, adicionar. :)
    private Integer id;
    private Integer id_day;
    private String name;
    private String description;
    private Date[] open_hours;
    private String address;
    //private String gps_coordinates; //Talvez não seja necessário
    private Integer visited;

    public OurPlace(){}

    public void setId(Integer id){ this.id = id;}

    public Integer getId(){ return this.id; }

    public void setIdDay(Integer id){ this.id_day = id;}

    public Integer getIdDay(){ return this.id_day;}

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public String getDescription(){return description;}

    public void setDescription(String description){this.description = description;}

    public Date[] getOpen_hours(){return open_hours;}

    public void setOpen_hours(Date[] open_hours){this.open_hours = open_hours;}

    public String getAddress(){return address;}

    public void setAddress(String address){this.address = address;}

    public Integer getVisited(){return visited;}

    public void setVisited(Integer visited){this.visited = visited;}
}
