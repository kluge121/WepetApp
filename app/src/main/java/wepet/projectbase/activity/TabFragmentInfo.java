package wepet.projectbase.activity;

import android.support.v4.app.Fragment;

/**
 * Created by ccei on 2016-08-19.
 */
public class TabFragmentInfo {

    private  int iconResId;
    private Fragment fragment;

    public TabFragmentInfo(int iconResId, Fragment fragment) {
        this.iconResId = iconResId;
        this.fragment = fragment;
    }
    public int getIconResId(){
        return iconResId;
    }

    public Fragment getFragment(){
        return fragment;
    }
}
