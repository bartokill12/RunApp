package pe.saul.runapp.Modelos.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pe.saul.runapp.R;
import pe.saul.runapp.Modelos.Activity;
import pe.saul.runapp.Modelos.Coordinate;

/**
 * Created by Saul on 14/02/2018.
 */

public class StringFormatter {

    public final static long ONE_SECOND = 1000;
    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long ONE_DAY = ONE_HOUR * 24;
    private static final double FACTOR_MILE = 0.621371;

    /**
     * Returns the distance as string in a certain unit
     * Example: getFormattedDistance() --> "11.53 km"
     *
     * @param coordinates
     * @param unit
     * @return
     */
    public static String getFormattedDistance(ArrayList<Coordinate> coordinates, String unit) {
        String ret = "";
        double distanceInMeter = 0.0;
        for (Coordinate c : coordinates) {
            distanceInMeter += c.getDistanceFromPrevious();
        }
        switch (unit) {
            case "km":
                if (distanceInMeter >= 1000) {
                    double result = Math.round(distanceInMeter / 1000 * 100.0) / 100.0;
                    ret = getFormattedValue(result) + " km";
                } else {
                    double result = Math.round(distanceInMeter * 100.0) / 100.0;
                    ret = getFormattedValue(result) + " m";
                }
                break;
            case "mi":
                double result = Math.round(distanceInMeter / 1000 * FACTOR_MILE * 100.0) / 100.0;
                ret = getFormattedValue(result) + " mi";
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * Returns the average speed as string in a certain unit
     * Example: getFormattedAvg() --> "11 mph"
     * Example:
     *
     * @param activity
     * @param unit
     * @return
     */
    public static String getFormattedAvg(Activity activity, String unit) {
        String ret = "";
        double distanceInMeter = 0.0;
        for (Coordinate c : activity.getCoordinates()) {
            distanceInMeter += c.getDistanceFromPrevious();
        }
        double result;
        switch (unit) {
            case "km":
                result = Math.round(distanceInMeter / activity.getDuration() * 3.6 * 100.0) / 100.0;
                ret = getFormattedValue(result) + " km/h";
                break;
            case "mi":
                result = Math.round(distanceInMeter / activity.getDuration() * 3.6 * FACTOR_MILE
                        * 100.0) / 100.0;
                ret = getFormattedValue(result) + " mph";
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * Returns the duration as string
     * Example: convertDurationToString(661) --> "11min, 1sec"
     */
    public static String getFormattedDuration(int durationInSeconds) {
        StringBuffer ret = new StringBuffer();
        long duration = durationInSeconds * 1000;
        long temp;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                ret.append(temp).append("d");
                if (duration >= ONE_MINUTE) {
                    ret.append(", ");
                }
            }
            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                ret.append(temp).append("h");
                if (duration >= ONE_MINUTE) {
                    ret.append(", ");
                }
            }
            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                ret.append(temp).append("min");
                if (duration >= ONE_SECOND) {
                    ret.append(", ");
                }
            }
            temp = duration / ONE_SECOND;
            if (temp > 0) {
                ret.append(temp).append("sec");
            }
        } else {
            ret.append("0sec");
        }
        return ret.toString();
    }

    /**
     * Returns the date as string in a certain format
     * Example: getFormattedDate(1447115888179l, "dd.MM.yyyy") --> "10.11.2015"
     *
     * @return
     */
    public static String getFormattedDate(long date, String format) {
        String ret;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        ret = sdf.format(new java.util.Date(date));
        return ret;
    }

    // Returns true if the passed double value can be converted to an integer without changing
    // the value
    public static boolean needToCast(double d) {
        boolean ret = false;
        if (d % 1 == 0) {
            ret = true;
        }
        return ret;
    }

    // Returns a double as string and removes the decimal places if they are not needed
    public static String getFormattedValue(double value) {
        String ret;
        if (needToCast(value)) {
            ret = String.valueOf((int) value);
        } else {
            ret = String.valueOf(value);
        }
        return ret;
    }
}
