package pe.saul.runapp.Modelos;

import java.io.Serializable;

/**
 * Created by Saul on 14/02/2018.
 */

public class Coordinate implements Serializable {

    private int id = -1;
    private double longitude = -1.0;
    private double latitude = -1.0;
    private boolean start = false;
    private boolean end = false;
    private int timeFromStart = -1;
    private double distanceFromPrevious = -1;
    private Activity activity = null;
    private boolean pause = false;

    public Coordinate() {
    }

    public Coordinate(int id, double longitude, double latitude, boolean start, boolean end, int
            timeFromStart, double distanceFromPrevious, boolean pause) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.start = start;
        this.end = end;
        this.timeFromStart = timeFromStart;
        this.distanceFromPrevious = distanceFromPrevious;
        this.pause = pause;
    }

    public Coordinate(int id, double longitude, double latitude, boolean start, boolean end, int
            timeFromStart, double distanceFromPrevious, Activity activity, boolean pause) {
        this(id, longitude, latitude, start, end, timeFromStart, distanceFromPrevious, pause);
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public int getTimeFromStart() {
        return timeFromStart;
    }

    public void setTimeFromStart(int timeFromStart) {
        this.timeFromStart = timeFromStart;
    }

    public double getDistanceFromPrevious() {
        return distanceFromPrevious;
    }

    public void setDistanceFromPrevious(double distanceFromPrevious) {
        this.distanceFromPrevious = distanceFromPrevious;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", start=" + start +
                ", end=" + end +
                ", timeFromStart=" + timeFromStart +
                ", distanceFromPrevious=" + distanceFromPrevious +
                ", activity=" + activity.getId() +
                ", pause=" + pause +
                '}';
    }

}
