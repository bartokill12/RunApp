package pe.saul.runapp.Modelos.Enum;

import android.content.Context;
import pe.saul.runapp.R;

/**
 * Created by Saul on 14/02/2018.
 */

public class ActivityTypes {

    public enum Type {
        RUNNING(R.string.running),
        WALKING(R.string.walking),
        CYCLING(R.string.cycling),
        OTHER(R.string.other);

        private int id = -1;

        Type(int id) {
            this.id = id;
        }

        public String toString(Context context) {
            return context.getResources().getString(id);
        }
    }
}
