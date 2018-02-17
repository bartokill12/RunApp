package pe.saul.runapp.Interfaces.dialogfactory;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Hashtable;

import pe.saul.runapp.R;
import pe.saul.runapp.Interfaces.activities.SaveActivityActivity;
import pe.saul.runapp.Modelos.DBAccessHelper;
import pe.saul.runapp.Modelos.Track;

/**
 * Created by Saul on 14/02/2018.
 */

public class AddTrackDialogFactory {

    private SaveActivityActivity saveActivityActivity = null;

    public AddTrackDialogFactory(SaveActivityActivity saveActivityActivity) {
        this.saveActivityActivity = saveActivityActivity;
    }

    public void makeCustomInputDialog() {
        LayoutInflater layoutInflater = saveActivityActivity.getLayoutInflater();
        final View promptView = layoutInflater.inflate(R.layout.add_change_track_dialog, null);

        final AlertDialog d = new AlertDialog.Builder(saveActivityActivity)
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.cancel, null)
                .setTitle(R.string.dialog_add_track_title)
                .setView(promptView)
                .create();

        d.show();

        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                EditText name = (EditText) promptView.findViewById(R.id.dialog_name);
                TextView error = (TextView) promptView.findViewById(R.id.dialog_error);

                Track track = new Track();
                String input = name.getText().toString();
                if (input != null) {
                    track.setName(name.getText().toString());
                }
                int result = DBAccessHelper.getInstance(null).insertTrack(track);
                if (result == 0) {
                    saveActivityActivity.setUpSpinners();
                    saveActivityActivity.spinnerTrack.setSelection(saveActivityActivity
                            .spinnerTrack.getAdapter().getCount() - 1);
                    d.dismiss();

                } else {
                    Hashtable<String, Integer> errors = track.getError();
                    if (error != null) {
                        if (errors.get("name") == Track.NAME_IS_NOT_SET) {
                            error.setVisibility(View.VISIBLE);
                            error.setText(saveActivityActivity.getResources().getString(R.string
                                    .dialog_error_no_name));
                        }
                        if (errors.get("name") == Track.NAME_ALREADY_EXISTS) {
                            error.setVisibility(View.VISIBLE);
                            error.setText(saveActivityActivity.getResources().getString(R.string
                                    .dialog_error_already_exists));
                        }
                    }
                }
            }
        });
    }
}
