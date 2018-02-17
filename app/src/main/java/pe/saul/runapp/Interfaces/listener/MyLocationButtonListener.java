package pe.saul.runapp.Interfaces.listener;

import android.content.Context;
import android.location.LocationManager;
import pe.saul.runapp.R;
import com.google.android.gms.maps.GoogleMap;
import pe.saul.runapp.Interfaces.message.ToastFactory;

/**
 * Created by Saul on 14/02/2018.
 */

public class MyLocationButtonListener implements GoogleMap.OnMyLocationButtonClickListener {

    private Context context = null;

    public MyLocationButtonListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context
                .LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled) {
            ToastFactory.makeToast(context, context.getResources().getString(R.string
                    .toast_enable_gps));
        }
        return false;
    }
}
