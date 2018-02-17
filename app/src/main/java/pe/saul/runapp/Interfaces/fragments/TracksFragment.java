package pe.saul.runapp.Interfaces.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import pe.saul.runapp.R;
import pe.saul.runapp.Interfaces.activities.ActivitiesActivity;
import pe.saul.runapp.Interfaces.adapter.TrackAdapter;
import pe.saul.runapp.Interfaces.dialogfactory.ChangeTrackDialogFactory;
import pe.saul.runapp.Interfaces.message.ToastFactory;
import pe.saul.runapp.Modelos.DBAccessHelper;
import pe.saul.runapp.Modelos.Track;

/**
 * Created by Saul on 14/02/2018.
 */

public class TracksFragment extends Fragment{
    private ArrayList<Track> selection = null;
    private ArrayList<Track> tracks = null;
    private ListView listView = null;
    private TextView empty = null;
    private TrackAdapter trackAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tracks_fragment, container, false);

        listView = (ListView) v.findViewById(R.id.listviewTracks);
        empty = (TextView) v.findViewById(R.id.tracksEmpty);
        tracks = DBAccessHelper.getInstance(getContext()).getTracks();

        selection = new ArrayList<>();

        if (tracks == null) {
            tracks = new ArrayList<>();
            empty.setVisibility(View.VISIBLE);
            empty.setText(getResources().getString(R.string.tracks_empty));
        } else {
            empty.setVisibility(View.GONE);
        }

        trackAdapter = new TrackAdapter(this.getActivity(), tracks);
        listView.setAdapter(trackAdapter);
        listView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                    checked) {
                if (checked) {
                    selection.add(tracks.get(position));
                } else {
                    selection.remove(tracks.get(position));
                }
                mode.setTitle(listView.getCheckedItemCount() + getResources().getString(R
                        .string.select));
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = getActivity().getMenuInflater();
                menuInflater.inflate(R.menu.contextual_action_bar_menu_delete_change, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        final ActionMode modeFinal = mode;
                        final MenuItem itemFinal = item;
                        new AlertDialog.Builder(getContext())
                                .setTitle(getResources().getString(R.string
                                        .dialog_delete_track_title))
                                .setMessage(getResources().getString(R.string
                                        .dialog_delete_track_message))
                                .setPositiveButton(android.R.string.yes, new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        for (Track Item : selection) {
                                            tracks.remove(Item);
                                            if (DBAccessHelper.getInstance(getContext())
                                                    .deleteTrack(Item) != 0) {
                                                ToastFactory.makeToast(getContext(), getResources
                                                        ().getString(R
                                                        .string.toast_delete_track_error));
                                            } else {
                                                itemFinal.setVisible(false);
                                            }
                                        }

                                        trackAdapter = new TrackAdapter(TracksFragment.this
                                                .getActivity(), tracks);
                                        listView.setAdapter(trackAdapter);
                                        // Close CAB
                                        modeFinal.finish();
                                        // Close the dialog
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Close CAB
                                        modeFinal.finish();
                                        // Close the dialog
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                        break;
                    case R.id.change:
                        ChangeTrackDialogFactory changeTrackDialogFactory = new
                                ChangeTrackDialogFactory(getActivity(), selection.get(0), mode);
                        changeTrackDialogFactory.makeCustomInputDialog();
                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selection.clear();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),
                        ActivitiesActivity.class);
                i.putExtra("track", tracks.get(position));
                startActivityForResult(i, 0);
            }
        });
        return v;
    }

    /**
     * Called when an activity started by this Fragment is finished
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refresh();
        }
    }

    private void refresh() {
        tracks = DBAccessHelper.getInstance(getContext()).getTracks();
        if (tracks == null) {
            tracks = new ArrayList<>();
            empty.setVisibility(View.VISIBLE);
            empty.setText(getResources().getString(R.string.tracks_empty));
        } else {
            empty.setVisibility(View.GONE);
        }
        trackAdapter.notifyDataSetChanged();
        trackAdapter = new TrackAdapter(this.getActivity(), tracks);
        listView.setAdapter(trackAdapter);
    }
}
