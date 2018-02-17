package pe.saul.runapp.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Saul on 14/02/2018.
 */

public class Track implements Serializable {

    public static final int NAME_IS_NOT_SET = -1;
    public static final int NAME_ALREADY_EXISTS = -2;

    private int id = -1;
    private String name = null;
    private ArrayList<Activity> activities = null;
    private Hashtable<String, Integer> error = null;

    public Track() {
    }

    public Track(int id, String name) {
        this.id = id;
        this.name = name;
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

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public Hashtable<String, Integer> getError() {
        return error;
    }

    public void setError(Hashtable<String, Integer> error) {
        this.error = error;
    }

    public void setError(String key, Integer value) {
        if (key != null && key.length() > 0 && value != null) {
            if (this.error == null) {
                this.error = new Hashtable<>();
            }
            this.error.put(key, value);
        }
    }

    public void validate() {
        this.error = null;
        if (this.name == null || this.name.length() == 0) {
            setError("name", Track.NAME_IS_NOT_SET);
        }
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
