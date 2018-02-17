package pe.saul.runapp.Interfaces.listener;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import pe.saul.runapp.R;
import pe.saul.runapp.Modelos.Activity;
import pe.saul.runapp.Modelos.Coordinate;

import java.util.ArrayList;

/**
 * Created by Saul on 14/02/2018.
 */

public class LocationListener implements com.google.android.gms.location.LocationListener {

    // Zoom Level en el mapa
    private static final int ZOOM_LEVEL = 18;
    // actual location
    private LatLng actualLatLng = null;
    // previa location
    private LatLng previousLatLng = null;
    // coordenadas de la actividad
    private ArrayList<Coordinate> coordinates = null;
    // UI Widgets
    private GoogleMap map = null;
    // Used for getRessources()
    private Context context = null;
    // actividad actual
    private Activity activity = null;
    // Coordenadas actual
    private Coordinate actualCoordinate = null;
    // Pausa actividad
    private boolean lastCoordinateIsPause = false;
    // localizacion d envio
    private Location location = null;

    public LocationListener(GoogleMap map, Context context) {
        this.map = map;
        this.context = context;
        // inicializar objetos
        coordinates = new ArrayList<>();
        activity = new Activity();
        // set fecha actual
        activity.setDate(System.currentTimeMillis());
        // Set coordenadas de la actividad
        activity.setCoordinates(coordinates);
    }

    public boolean isLastCoordinateIsPause() {
        return lastCoordinateIsPause;
    }

    public void setLastCoordinateIsPause(boolean lastCoordinateIsPause) {
        this.lastCoordinateIsPause = lastCoordinateIsPause;
    }

    /**
     * Callback that fires when the location changes forced by
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d("####", "--- new coordinate ----");
        this.previousLatLng = this.actualLatLng;
        this.actualLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Set localizaciond e envio
        this.location = location;

        Coordinate c = new Coordinate();
        c.setLongitude(location.getLongitude());
        c.setLatitude(location.getLatitude());
        this.actualCoordinate = c;
        addCoordinate();

        updateMap();
    }

    private void addCoordinate() {
        if (previousLatLng == null) {
            actualCoordinate.setStart(true);
            actualCoordinate.setTimeFromStart(0);
            actualCoordinate.setDistanceFromPrevious(0);
        } else {
            if (lastCoordinateIsPause) {
                coordinates.get(coordinates.size() - 1).setPause(true);
                previousLatLng = null;
                actualCoordinate.setDistanceFromPrevious(0);
                actualCoordinate.setTimeFromStart((int) ((System.currentTimeMillis() - activity
                        .getDate()) / 1000));

                lastCoordinateIsPause = false;
            } else {
                float[] result = new float[1];
                Location.distanceBetween(previousLatLng.latitude, previousLatLng.longitude,
                        actualLatLng.latitude, actualLatLng.longitude, result);
                actualCoordinate.setDistanceFromPrevious(result[0]);
                actualCoordinate.setTimeFromStart((int) ((System.currentTimeMillis() - activity
                        .getDate()) / 1000));
            }
        }
        actualCoordinate.setActivity(activity);
        coordinates.add(actualCoordinate);
    }

    /**
     * Puts a Polyline or a Marker into the map
     */
    private void updateMap() {
        if (previousLatLng == null && coordinates.size() == 1) {
            addStartMarker();
        } else {
            if (!coordinates.get(coordinates.size() - 1).isPause()) {
                if (actualLatLng != null && previousLatLng != null) {
                    addPolyline();
                }
            }
        }
        if (actualLatLng != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(actualLatLng, ZOOM_LEVEL));
        }
    }

    private void addStartMarker() {
        map.addMarker(new MarkerOptions()
                .position(actualLatLng)
                .title(context.getResources().getString(R.string.marker_start))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    private void addPolyline() {
        // Instantiates a new Polyline object and adds points
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(actualLatLng)
                .add(previousLatLng);
        // Get back the mutable Polyline
        polylineOptions.color(Color.MAGENTA);
        map.addPolyline(polylineOptions);
    }

    public Activity getActivity() {
        return activity;
    }

    public Location getLocation() {
        return location;
    }
}
