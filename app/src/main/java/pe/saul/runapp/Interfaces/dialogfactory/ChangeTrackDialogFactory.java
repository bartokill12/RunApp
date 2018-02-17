package pe.saul.runapp.Interfaces.dialogfactory;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Hashtable;

import pe.saul.runapp.R;
import pe.saul.runapp.Modelos.DBAccessHelper;
import pe.saul.runapp.Modelos.Track;

/**
 * Created by Saul on 14/02/2018.
 */

public class ChangeTrackDialogFactory {

    private EditText name = null;
    private FragmentActivity mainActivity = null;
    private Track track = null;
    private ActionMode mode = null;

    public ChangeTrackDialogFactory(FragmentActivity mainActivity, Track track, ActionMode mode) {
        this.mainActivity = mainActivity;
        this.track = track;
        this.mode = mode;
    }

    public void makeCustomInputDialog() {
        LayoutInflater layoutInflater = mainActivity.getLayoutInflater();
        final View promptView = layoutInflater.inflate(R.layout.add_change_track_dialog, null);

        final AlertDialog d = new AlertDialog.Builder(mainActivity)
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.cancel, null)
                .setTitle(R.string.dialog_change_track_title)
                .setView(promptView)
                .create();

        d.show();

        name = (EditText) promptView.findViewById(R.id.dialog_name);
        name.setText(track.getName());

        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                EditText name = (EditText) promptView.findViewById(R.id.dialog_name);
                TextView error = (TextView) promptView.findViewById(R.id.dialog_error);

                String input = name.getText().toString();
                if (input != null) {
                    track.setName(name.getText().toString());
                }
                int result = DBAccessHelper.getInstance(null).updateTrack(track);
                if (result == 0) {
                    d.dismiss();
                    mode.finish();
                } else {
                    Hashtable<String, Integer> errors = track.getError();
                    if (error != null) {
                        if (errors.get("name") == Track.NAME_IS_NOT_SET) {
                            error.setVisibility(View.VISIBLE);
                            error.setText(mainActivity.getResources().getString(R.string
                                    .dialog_error_no_name));
                        }
                        if (errors.get("name") == Track.NAME_ALREADY_EXISTS) {
                            error.setVisibility(View.VISIBLE);
                            error.setText(mainActivity.getResources().getString(R.string
                                    .dialog_error_already_exists));
                        }
                    }
                }
            }
        });
        d.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                mode.finish();
            }
        });
    }
}
