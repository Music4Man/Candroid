package com.candroid.mischiefmanager.Menu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candroid.mischiefmanager.R;

/**
 * Created by Frank on 2015-10-12.
 */
public class Weather_Fragment extends Fragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_weather, container, false);
        return rootview;
    }
}
