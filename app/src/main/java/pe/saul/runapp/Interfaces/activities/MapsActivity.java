package pe.saul.runapp.Interfaces.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pe.saul.runapp.R;
import pe.saul.runapp.Interfaces.listener.ConnectionFailed;
import pe.saul.runapp.Interfaces.listener.LocationListenerCompare;
import pe.saul.runapp.Interfaces.listener.MyLocationButtonListener;
import pe.saul.runapp.Interfaces.message.ToastFactory;
import pe.saul.runapp.Modelos.Activity;
import pe.saul.runapp.Modelos.Coordinate;
import pe.saul.runapp.Modelos.DBAccessHelper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Saul on 14/02/2018.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int MIN_ACCURACY = 15;
    private final int MAX_DISTANCE_TO_START = 75;
    private final int FACTOR_BETWEEN_INTERVALS = 1 / 3;
    private final float FACTOR_DISPLACEMENT = 1 / 4;
    private final double FACTOR_ACTION_BAR = 8.3;
    public Boolean startButtonEnabled = false;
    public long updateIntervalInMilliseconds = -1;
    public long fastestUpdateIntervalInMilliseconds = -1;
    public float smallestDisplacementInMeter = -1;
    public GoogleApiClient googleApiClient = null;
    protected LocationRequest locationRequest = null;
    protected LocationListenerCompare locationListener = null;
    protected GoogleApiClient.ConnectionCallbacks connectionCallbacks = null;
    protected GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = null;
    private GoogleMap map = null;
    private Button startStopComparison = null;
    private ArrayList<Coordinate> coordinates = null;
    private TextView text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_fragment);

        getActionBar().setDisplayShowTitleEnabled(false);

        text = (TextView) findViewById(R.id.time);

        startStopComparison = (Button) findViewById(R.id.mapsStartStop);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsGoogleMap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        Activity a = (Activity) intent.getSerializableExtra("activity");
        this.coordinates = DBAccessHelper.getInstance(this).getCoordinates(a);

        startStopComparison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startButtonEnabled) {
                    LocationManager locationManager = (LocationManager) MapsActivity.this
                            .getSystemService(Context.LOCATION_SERVICE);
                    boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager
                            .GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager
                            .NETWORK_PROVIDER);
                    if (gpsEnabled) {
                        final float[] result = new float[1];
                        result[0] = 0;
                        if (googleApiClient != null && googleApiClient.isConnected()) {
                            Location actualPosition = LocationServices.FusedLocationApi
                                    .getLastLocation
                                            (googleApiClient);
                            if (actualPosition != null) {
                                Location.distanceBetween(actualPosition.getLongitude(),
                                        actualPosition.getLatitude(), coordinates.get(0)
                                                .getLongitude(),
                                        coordinates.get(0).getLatitude(), result);
                                if (actualPosition.getAccuracy() > MIN_ACCURACY) {
                                    new AlertDialog.Builder(MapsActivity
                                            .this)
                                            .setTitle(getResources().getString(R.string
                                                    .dialog_bad_accuracy_title))
                                            .setMessage(getResources().getString(R.string
                                                    .dialog_bad_accuracy_message))
                                            .setPositiveButton(android.R.string.yes, new
                                                    DialogInterface
                                                            .OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface
                                                                                    dialog, int
                                                                                    id) {
                                                            // Close the dialog
                                                            dialog.dismiss();
                                                            if (result[0] > MAX_DISTANCE_TO_START) {
                                                                ToastFactory.makeToast
                                                                        (MapsActivity.this,
                                                                                getResources()
                                                                                        .getString
                                                                                                (R.string
                                                                                                        .toast_not_at_start));
                                                            } else {
                                                                start();
                                                            }
                                                        }
                                                    })
                                            .setNegativeButton(android.R.string.no, null)
                                            .create().show();
                                } else {
                                    if (result[0] > MAX_DISTANCE_TO_START) {
                                        ToastFactory.makeToast
                                                (MapsActivity.this,
                                                        getResources()
                                                                .getString
                                                                        (R.string
                                                                                .toast_not_at_start));
                                    } else {
                                        start();
                                    }
                                }
                            } else {
                                new AlertDialog.Builder(MapsActivity.this)
                                        .setTitle(getResources().getString(R.string
                                                .dialog_no_location_title))
                                        .setMessage(getResources().getString(R.string
                                                .dialog_no_location_message))
                                        .setPositiveButton(android.R.string.ok, null)
                                        .create().show();
                            }
                        } else {
                            ToastFactory.makeToast(MapsActivity.this, getResources().getString(R
                                    .string.not_connected));
                        }
                    } else {
                        ToastFactory.makeToast(MapsActivity.this, getResources().getString(R.string
                                .toast_enable_gps));
                    }
                } else {
                    startButtonEnabled = false;
                    startStopComparison.setText(getResources().getString(R.string
                            .maps_activity_start));
                    text.setVisibility(TextView.GONE);
                    stopLocationUpdates();
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission
                            .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MainActivity.REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }
                    map.setMyLocationEnabled(false);
                    drawRoute();
                    showDialog();
                }
            }
        });

        onConnectionFailedListener = new ConnectionFailed(this);

        setUpdateIntervalsAndDisplacement();

        buildGoogleApiClient();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        startStopComparison.setVisibility(View.VISIBLE);
        drawRoute();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(new MyLocationButtonListener(this));
        map.getUiSettings().setMapToolbarEnabled(false);
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION
     * These settings control the accuracy of the current location. This sample uses
     * ACCESS_FINE_LOCATION, as defined in the AndroidManifest.xml.
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet
     */
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        locationRequest.setInterval(1000);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        locationRequest.setFastestInterval(1000);

        // Sets the minimum displacement between location updates in meters.
        // If the displacement is too small the updates will be suppressed
        locationRequest.setSmallestDisplacement(smallestDisplacementInMeter);

        // Accuracy must be high for tracking a route
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * Requests location updates from the FusedLocationApi
     */
    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        // LocationListener is fired every x seconds
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, locationListener);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
    }

    /**
     * Sets up the two intervals and the displacement for the LocationRequest
     */
    private void setUpdateIntervalsAndDisplacement() {

        fastestUpdateIntervalInMilliseconds = getIntervalFromSettingsInMilliseconds();
        updateIntervalInMilliseconds = fastestUpdateIntervalInMilliseconds *
                FACTOR_BETWEEN_INTERVALS;

        smallestDisplacementInMeter = fastestUpdateIntervalInMilliseconds / 1000 *
                FACTOR_DISPLACEMENT;
    }

    /**
     * Gets the interval from Settings in milliseconds
     */
    private int getIntervalFromSettingsInMilliseconds() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
        return Integer.valueOf(sp.getString("interval", "1")) * 1000;

    }

    /**
     * Called when the Activity starts
     */
    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    /**
     * Called when user resumes to display the Activity
     */
    @Override
    public void onResume() {
        super.onResume();

        if (googleApiClient.isConnected() && locationRequest != null && startButtonEnabled) {
            stopLocationUpdates();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            startLocationUpdates();
        }
        if (locationListener != null && startButtonEnabled) {
            locationListener.cancelNotification();
            locationListener.setShowNotification(false);
        }
    }

    /**
     * Called when user doesn't see the Activity
     */
    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient.isConnected() && locationRequest != null && startButtonEnabled) {
            stopLocationUpdates();
            locationRequest.setInterval(updateIntervalInMilliseconds);
            locationRequest.setFastestInterval(fastestUpdateIntervalInMilliseconds);
            startLocationUpdates();
        }
        if (locationListener != null && startButtonEnabled) {
            locationListener.setShowNotification(true);
        }

    }

    private void drawRoute() {
        map.clear();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int value = Integer.valueOf(sp.getString("type", "1"));
        int type;
        switch (value) {
            case 0:
                type = GoogleMap.MAP_TYPE_NORMAL;
                break;
            case 1:
                type = GoogleMap.MAP_TYPE_HYBRID;
                break;
            case 2:
                type = GoogleMap.MAP_TYPE_SATELLITE;
                break;
            case 3:
                type = GoogleMap.MAP_TYPE_TERRAIN;
                break;
            case 4:
                type = GoogleMap.MAP_TYPE_NONE;
                break;
            default:
                type = GoogleMap.MAP_TYPE_NORMAL;
                break;
        }
        map.setMapType(type);


        PolylineOptions polylineOptions;
        ArrayList<ArrayList<LatLng>> all = new ArrayList<>(0);
        ArrayList<LatLng> latLngGroup = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate c = coordinates.get(i);
            LatLng latLng = new LatLng(c.getLatitude(), c.getLongitude());
            builder.include(latLng);
            latLngGroup.add(latLng);
            if (c.isPause()) {
                all.add(latLngGroup);
                latLngGroup = new ArrayList<>();
            }
            if (i == coordinates.size() - 1) {
                all.add(latLngGroup);
            }
            if (c.isStart()) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.marker_start))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                .HUE_GREEN)));

                marker.showInfoWindow();
            }
            if (c.isEnd()) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.marker_end))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                .HUE_RED)));
            }
        }
        for (ArrayList<LatLng> group : all) {
            polylineOptions = new PolylineOptions();
            polylineOptions.addAll(group).color(Color.MAGENTA);
            map.addPolyline(polylineOptions);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        int actionBarHeight = (int) (height / (100 / FACTOR_ACTION_BAR));

        int padding = (int) (width * 0.18);
        height = height - (3 * actionBarHeight);

        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        map.moveCamera(cu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                ret = true;
                break;
            default:
                ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    public TextView getText() {
        return text;
    }

    private void start() {
        startButtonEnabled = true;
        startStopComparison.setText(getResources().getString
                (R.string
                        .maps_activity_stop));
        locationListener = new LocationListenerCompare(map,
                MapsActivity.this,
                coordinates.get(0).getActivity(), text);
        locationListener.setUpNotification();
        startLocationUpdates();
        if (ActivityCompat.checkSelfPermission(MapsActivity
                .this, Manifest
                .permission
                .ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission
                            .ACCESS_FINE_LOCATION},
                    MainActivity.REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBack() {
        if (startButtonEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.maps_activity_stop))
                    .setMessage(getResources().getString(R.string.dialog_go_back))
                    .setPositiveButton(android.R.string.yes, new DialogInterface
                            .OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            stopLocationUpdates();
                            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest
                                    .permission
                                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MainActivity.REQUEST_CODE_ASK_PERMISSIONS);
                                return;
                            }
                            map.setMyLocationEnabled(false);
                            locationListener.cancelNotification();
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .create().show();
        } else {
            finish();
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.finished))
                .setMessage(text.getText())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService
                (Context
                        .NOTIFICATION_SERVICE);
        notificationManager.cancel(LocationListenerCompare.notifyID);
    }
}
