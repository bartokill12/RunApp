package pe.saul.runapp.Interfaces.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import pe.saul.runapp.R;
import pe.saul.runapp.Interfaces.dialogfactory.AddTrackDialogFactory;
import pe.saul.runapp.Interfaces.message.ToastFactory;
import pe.saul.runapp.Modelos.Activity;
import pe.saul.runapp.Modelos.DBAccessHelper;
import pe.saul.runapp.Modelos.Track;
import pe.saul.runapp.Modelos.Enum.ActivityTypes;

import java.util.ArrayList;

/**
 * Created by Saul on 14/02/2018.
 */

public class SaveActivityActivity extends android.app.Activity {

    public Spinner spinnerTrack = null;
    pe.saul.runapp.Modelos.Activity activity = null;
    ArrayList<Track> tracks = null;
    private Spinner spinnerType = null;
    private Button add = null;
    private TextView distance = null;
    private TextView duration = null;
    private TextView avg = null;
    private Button save = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_activity_activity);

        spinnerTrack = (Spinner) findViewById(R.id.spinner_track);
        spinnerType = (Spinner) findViewById(R.id.spinner_type);
        add = (Button) findViewById(R.id.add);
        distance = (TextView) findViewById(R.id.distance);
        duration = (TextView) findViewById(R.id.duration);
        avg = (TextView) findViewById(R.id.avg);
        save = (Button) findViewById(R.id.save);

        activity = (pe.saul.runapp.Modelos.Activity) (getIntent().getSerializableExtra("activity"));

        setUpSpinners();
        setUpTextViews();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTrackDialogFactory addTrackDialogFactory = new AddTrackDialogFactory
                        (SaveActivityActivity.this);
                addTrackDialogFactory.makeCustomInputDialog();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prepareActivity()) {
                    int result = DBAccessHelper.getInstance(SaveActivityActivity.this)
                            .insertActivity(activity);
                    if (result == 0) {
                        finish();
                    } else {
                        ToastFactory.makeToast(SaveActivityActivity.this, getResources()
                                .getString(R.string.toast_error_save_track));
                    }
                } else {
                    ToastFactory.makeToast(SaveActivityActivity.this, getResources().getString(R
                            .string.toast_save_no_track_error));
                }
            }
        });
    }

    /**
     * Called when an activity started by this Fragment is finished
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setUpSpinners();
    }

    private void setUpTextViews() {
        distance.setText(activity.getFormattedDistance(getUnitFromSettings()));
        duration.setText(activity.getFormattedDuration());
        avg.setText(activity.getFormattedAvg(getUnitFromSettings()));
    }

    private String getUnitFromSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString("unit", "km");
    }

    public void setUpSpinners() {
        tracks = DBAccessHelper.getInstance(this).getTracks();
        setUpSpinnerTrack();
        setUpSpinnerType();
    }

    /**
     * True if success
     *
     * @return
     */
    private boolean prepareActivity() {
        boolean ret = false;
        activity.setType(ActivityTypes.Type.values()[spinnerType.getSelectedItemPosition()]);
        if (spinnerTrack.getSelectedItemPosition() != AdapterView.INVALID_POSITION) {
            activity.setTrack(tracks.get(spinnerTrack.getSelectedItemPosition()));
            ret = true;
        }
        return ret;
    }

    private void setUpSpinnerTrack() {
        if (tracks != null) {
            ArrayList<String> names = new ArrayList<>();
            for (Track t : tracks) {
                names.add(t.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
                    .simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTrack.setAdapter(adapter);
        }
    }

    private void setUpSpinnerType() {
        ArrayList<String> types = new ArrayList<>();
        for (ActivityTypes.Type type : ActivityTypes.Type.values()) {
            types.add(type.toString(this));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.dialog_back_pressed_title))
                    .setMessage(getResources().getString(R.string.dialog_back_pressed_subtitle))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create().show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
