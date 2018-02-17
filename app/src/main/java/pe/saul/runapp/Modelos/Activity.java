package pe.saul.runapp.Modelos;

import java.io.Serializable;
import java.util.ArrayList;

import pe.saul.runapp.Modelos.Enum.ActivityTypes;
import pe.saul.runapp.Modelos.Utils.StringFormatter;

/**
 * Created by Saul on 14/02/2018.
 */

public class Activity implements Serializable {
    public static final int worst = 0;
    public static final int avg = 1;
    public static final int best = 2;

    private int id = -1;
    private ActivityTypes.Type type = null;
    private long date = -1;
    private int duration = -1;
    private int ranking = -1;
    private Track track = null;
    private ArrayList<Coordinate> coordinates = null;

    public Activity() {
        this.date = 0;
    }

    public Activity(int id, ActivityTypes.Type type, long date, int duration) {
        this();
        this.id = id;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public Activity(int id, ActivityTypes.Type type, long date, int duration, Track track) {
        this(id, type, date, duration);
        this.track = track;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ActivityTypes.Type getType() {
        return type;
    }

    public void setType(ActivityTypes.Type type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public String getFormattedDistance(String unit) {
        return StringFormatter.getFormattedDistance(getCoordinates(), unit);
    }

    public String getFormattedAvg(String unit) {
        return StringFormatter.getFormattedAvg(this, unit);
    }

    public String getFormattedDuration() {
        return StringFormatter.getFormattedDuration(this.duration);
    }

    public String getFormattedDate(String format) {
        return StringFormatter.getFormattedDate(this.date, format);
    }

    public int getDistance() {
        int ret = 0;
        if (coordinates != null) {
            for (Coordinate c : coordinates) {
                ret += c.getDistanceFromPrevious();
            }
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (id != activity.id) return false;
        if (date != activity.date) return false;
        if (duration != activity.duration) return false;
        return type == activity.type;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", type=" + type +
                ", date=" + date +
                ", duration=" + duration +
                ", ranking=" + ranking +
                ", track=" + track.getId() +
                '}';
    }

}
