package by.litelife.entity;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by John on 16.04.2017.
 */
public class Spectacle {

    private int id;
    private String name;
    private Date date;
    private int countOfTickets;
    private int countOfSoldTickets;
    private ArrayList<Theater> theaters = new ArrayList<>();




    public Spectacle(){

    }

    public Spectacle(int id,String name, Date date, int countOfTickets, int countOfSoldTickets) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.countOfTickets = countOfTickets;
        this.countOfSoldTickets = countOfSoldTickets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCountOfTickets() {
        return countOfTickets;
    }

    public void setCountOfTickets(int countOfTickets) {
        this.countOfTickets = countOfTickets;
    }

    public int getCountOfSoldTickets() {
        return countOfSoldTickets;
    }

    public void setCountOfSoldTickets(int countOfSoldTickets) {
        this.countOfSoldTickets = countOfSoldTickets;
    }

    public ArrayList<Theater> getTheaters() {
        return theaters;
    }

    public void setTheaters(ArrayList<Theater> theaters) {
        this.theaters = theaters;
    }

    @Override
    public String toString() {
        return "Spectacle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", countOfTickets=" + countOfTickets +
                ", countOfSoldTickets=" + countOfSoldTickets +
                ", theaters=" + theaters +
                '}';
    }
}
