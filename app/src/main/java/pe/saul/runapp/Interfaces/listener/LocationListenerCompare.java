package pe.saul.runapp.Interfaces.listener;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import pe.saul.runapp.R;

import java.util.ArrayList;
import pe.saul.runapp.Interfaces.activities.MapsActivity;
import pe.saul.runapp.Modelos.Activity;
import pe.saul.runapp.Modelos.Coordinate;
import pe.saul.runapp.Modelos.DBAccessHelper;
import pe.saul.runapp.Modelos.Utils.StringFormatter;

/**
 * Created by Saul on 14/02/2018.
 */

public class LocationListenerCompare implements com.google.android.gms.location.LocationListener {


    private static final int ZOOM_LEVEL = 18;

    public static int notifyID = 2;

    private Activity activity = null;

    private LatLng actualLatLng = null;

    private LatLng previousLatLng = null;

    private TextView text = null;
    private GoogleMap map = null;

    private Context context = null;

    private long time = 0;

    private NotificationManager notificationManager = null;

    private NotificationCompat.Builder notificationBuilder = null;

    private boolean showNotification = false;

    public LocationListenerCompare(GoogleMap map, Context context, Activity activity, TextView
            text) {
        this.map = map;
        this.context = context;
        this.activity = activity;
        this.text = text;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (previousLatLng == null) {
            time = System.currentTimeMillis() / 1000;
        }
        this.previousLatLng = this.actualLatLng;
        this.actualLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        updateMap();
        int id = DBAccessHelper.getInstance(context).getIDOfClosestCoordinateInActivity(location
                .getLongitude(), location.getLatitude(), activity.getId());
        Coordinate c = DBAccessHelper.getInstance(context).getCoordinate(id);
        long difference = c.getTimeFromStart() - ((System.currentTimeMillis() / 1000) - time);
        String textToSet;
        if (difference < 0) {
            textToSet = StringFormatter.getFormattedDuration((int) -difference) + context
                    .getResources().getString(R.string.faster);
        } else {
            if (text.getVisibility() != View.VISIBLE) {
                text.setVisibility(View.VISIBLE);
            }
            if (difference > 0) {
                textToSet = StringFormatter.getFormattedDuration((int) difference) + context
                        .getResources().getString(R.string.slower);
            } else {
                textToSet = context.getResources().getString(R.string.equally);
            }
        }
        text.setText(textToSet);
        if (showNotification) {
            updateNotification(textToSet);
        }
    }

    /**
     * Puts a Polyline or a Marker into the map
     */
    private void updateMap() {
        if (previousLatLng == null) {
            addStartMarker();
        } else {
            addPolyline();
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(actualLatLng, ZOOM_LEVEL));
    }

    private void addStartMarker() {
        map.addMarker(new MarkerOptions()
                .position(actualLatLng)
                .title(context.getResources().getString(R.string.marker_start))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    private void addPolyline() {
        // inicial objeto polilinea y agregar marcadores
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(actualLatLng)
                .add(previousLatLng);
        // Get back the mutable Polyline
        polylineOptions.color(Color.RED);
        map.addPolyline(polylineOptions);
    }

    public void setUpNotification() {
        notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setOngoing(true)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, MapsActivity.class), PendingIntent
                                .FLAG_UPDATE_CURRENT));
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void updateNotification(String text) {
        notificationBuilder.setContentText(text);
        notificationManager.notify(notifyID, notificationBuilder.build());
    }

    public void cancelNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(notifyID);
        }
    }

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }
}
