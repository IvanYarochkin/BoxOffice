package by.litelife.entity;

import java.util.ArrayList;

/**
 * Created by John on 16.04.2017.
 */
public class Theater {

    private int id;
    private String name;
    private ArrayList<Spectacle> spectacles = new ArrayList<>();



    public Theater() {
    }

    public Theater(int id, String name, ArrayList<Spectacle> spectacles) {
        this.id = id;
        this.name = name;
        this.spectacles = spectacles;
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

    public ArrayList<Spectacle> getSpectacles() {
        return spectacles;
    }

    public void setSpectacles(ArrayList<Spectacle> spectacles) {
        this.spectacles = spectacles;
    }

    @Override
    public String toString() {
        return "Theater{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", spectacles=" + spectacles +
                '}';
    }
}
