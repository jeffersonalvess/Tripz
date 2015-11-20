package edu.depaul.csc472.tripz;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Miller on 19/11/2015.
 */
public class Place {
    // Esses atributos foram todos que eu consegui pensar agora as 3:18am...
    // Quem lembrar de mais algum atributo relevante, por favor, adicionar. :)
    private String name;
    private String description;
    private Date[] open_hours;
    private String address;
    //private String gps_coordinates; //Talvez não seja necessário
    private boolean visited;

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public String getDescription(){return description;}

    public void setDescription(String description){this.description = description;}

    public Date[] getOpen_hours(){return open_hours;}

    public void setOpen_hours(Date[] open_hours){this.open_hours = open_hours;}

    public String getAddress(){return address;}

    public void setAddress(String address){this.address = address;}

    public boolean getVisited(){return visited;}

    public void setVisited(boolean visited){this.visited = visited;}
}
