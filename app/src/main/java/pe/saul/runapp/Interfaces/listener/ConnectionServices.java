package pe.saul.runapp.Interfaces.listener;

import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import pe.saul.runapp.R;
import pe.saul.runapp.Interfaces.message.ToastFactory;
import pe.saul.runapp.Interfaces.fragments.ActivityFragment;

/**
 * Created by Saul on 14/02/2018.
 */

public class ConnectionServices implements GoogleApiClient.ConnectionCallbacks {

    private ActivityFragment context = null;

    public ConnectionServices(ActivityFragment context) {
        this.context = context;
    }

    /**
     * Called when a GoogleApiClient object successfully connects
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        /*if (context.startButtonEnabled) {
            context.startLocationUpdates();
        }*/
    }

    /**
     * The connection to Google Play services was lost for some reason
     *
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {

        ToastFactory.makeToast(context.getContext(), context.getResources()
                .getString(R.string.toast_connection_lost) + cause);
        context.googleApiClient.connect();
    }

}
