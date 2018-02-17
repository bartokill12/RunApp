package pe.saul.runapp.Interfaces.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import pe.saul.runapp.R;
import pe.saul.runapp.Modelos.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Saul on 14/02/2018.
 */

public class ActivityAdapter extends BaseExpandableListAdapter {

    private android.app.Activity context;
    private Map<String, ArrayList<Activity>> groupCollection;
    private List<String> parentStrings;

    public ActivityAdapter(android.app.Activity context, List<String> parentStrings,
                           Map<String, ArrayList<Activity>> groupCollection) {
        this.context = context;
        this.groupCollection = groupCollection;
        this.parentStrings = parentStrings;
    }

    @Override
    public int getGroupCount() {
        return parentStrings.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupCollection.get(parentStrings.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentStrings.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupCollection.get(parentStrings.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        String typeTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_parent,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.heading);
        item.setText(typeTitle + " (" + groupCollection.get(parentStrings.get(groupPosition))
                .size() + ")");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        // Generate always a new view otherwise the background is sometimes not visible
        View ret = null;
        ActivityHolder activityHolder;
        if (ret == null) {
            ret = inflater.inflate(R.layout.activity_item, parent, false);
            activityHolder = new ActivityHolder();
            activityHolder.image = (ImageView) ret.findViewById(R.id.image_item);
            activityHolder.distance = (TextView) ret.findViewById(R.id.distance_item);
            activityHolder.avg = (TextView) ret.findViewById(R.id.avg_item);
            activityHolder.duration = (TextView) ret.findViewById(R.id.duration_item);
            activityHolder.date = (TextView) ret.findViewById(R.id.date_item);
            ret.setTag(activityHolder);
        } else {
            activityHolder = (ActivityHolder) ret.getTag();
        }
        Activity a = (Activity) getChild(groupPosition, childPosition);

        // The outputs in the TextViews are based on the Settings
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String unit = sp.getString("unit", "km");
        String dateFormat = sp.getString("date", "dd.MM.yyyy HH:mm:ss");

        String distance = a.getFormattedDistance(unit);
        String avg = a.getFormattedAvg(unit);
        String date = a.getFormattedDate(dateFormat);
        String duration = a.getFormattedDuration();

        switch (a.getType()) {
            case RUNNING:
                if (a.getRanking() == Activity.best) {
                    activityHolder.image.setImageResource(R.drawable.running_best);
                } else {
                    activityHolder.image.setImageResource(R.drawable.running);
                }
                break;
            case WALKING:
                if (a.getRanking() == Activity.best) {
                    activityHolder.image.setImageResource(R.drawable.walking_best);
                } else {
                    activityHolder.image.setImageResource(R.drawable.walking);
                }
                break;
            case CYCLING:
                if (a.getRanking() == Activity.best) {
                    activityHolder.image.setImageResource(R.drawable.cycling_best);
                } else {
                    activityHolder.image.setImageResource(R.drawable.cycling);
                }
                break;
            case OTHER:
                if (a.getRanking() == Activity.best) {
                    activityHolder.image.setImageResource(R.drawable.other_best);
                } else {
                    activityHolder.image.setImageResource(R.drawable.other);
                }
                break;
            default:
                break;
        }
        activityHolder.distance.setText(distance);
        activityHolder.avg.setText(avg);
        activityHolder.duration.setText(duration);
        activityHolder.date.setText(date);

        switch (a.getRanking()) {
            case Activity.worst:
                ret.setBackgroundResource(R.drawable.item_background_selector_worst);
                break;
            case Activity.best:
                ret.setBackgroundResource(R.drawable.item_background_selector_best);
                break;
            case Activity.avg:
                ret.setBackgroundResource(R.drawable.item_background_selector_avg);
                break;
            default:
                ret.setBackgroundResource(R.drawable.item_background_selector);
                break;
        }
        return ret;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // Class holds all UI Widgets
    private class ActivityHolder {
        ImageView image = null;
        TextView distance = null;
        TextView avg = null;
        TextView duration = null;
        TextView date = null;
    }
}
