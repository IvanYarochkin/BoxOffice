package by.litelife.entity;

/**
 * Created by John on 13.05.2017.
 */
public class Ticket {

    private int id;
    private Theater theater;
    private Spectacle spectacle;
    private User user;


    public  Ticket(){
    }

    public Ticket(int id, Theater theater, Spectacle spectacle, User user) {
        this.id = id;
        this.theater = theater;
        this.spectacle = spectacle;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public Spectacle getSpectacle() {
        return spectacle;
    }

    public void setSpectacle(Spectacle spectacle) {
        this.spectacle = spectacle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "id билета = "+id+", название театра: "+theater.getName()+", название спектакля: "+spectacle.getName()+
                ", дата представления: "+spectacle.getDate()+", логин пользователя: "+user.getLogin();
    }
}
