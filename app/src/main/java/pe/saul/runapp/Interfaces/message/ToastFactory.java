package pe.saul.runapp.Interfaces.message;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Saul on 14/02/2018.
 */

public class ToastFactory {
    public static void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
