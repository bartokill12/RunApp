package pe.saul.runapp.Interfaces.listener;

import android.content.Context;
import pe.saul.runapp.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import pe.saul.runapp.Interfaces.message.ToastFactory;

/**
 * Created by Saul on 14/02/2018.
 */

public class ConnectionFailed implements GoogleApiClient.OnConnectionFailedListener {

    private Context context = null;

    public ConnectionFailed(Context context) {
        this.context = context;
    }

    /**
     * The connection to Google Play services failed
     *
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        ToastFactory.makeToast(context, context.getResources()
                .getString(R.string.toast_connection_lost) + result.getErrorCode());
    }
}
