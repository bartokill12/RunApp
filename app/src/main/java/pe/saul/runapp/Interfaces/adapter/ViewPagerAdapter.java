package pe.saul.runapp.Interfaces.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pe.saul.runapp.R;
import pe.saul.runapp.Interfaces.fragments.ActivityFragment;
import pe.saul.runapp.Interfaces.fragments.TracksFragment;

/**
 * Created by Saul on 14/02/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_TABS = 2;
    private CharSequence titles[];

    private ActivityFragment activityFragment = null;

    public ViewPagerAdapter(FragmentManager fm, CharSequence titles[]) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            ActivityFragment activityFragment = new ActivityFragment();
            this.activityFragment = activityFragment;
            return activityFragment;
        } else {
            TracksFragment tracksFragment = new TracksFragment();
            return tracksFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return NUMBER_TABS;
    }

    public ActivityFragment getActivityFragment() {
        return activityFragment;
    }

    public void setActivityFragment(ActivityFragment activityFragment) {
        this.activityFragment = activityFragment;
    }
}
