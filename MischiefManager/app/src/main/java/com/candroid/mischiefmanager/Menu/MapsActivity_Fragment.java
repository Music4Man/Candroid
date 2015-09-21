package com.candroid.mischiefmanager.Menu;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candroid.mischiefmanager.R;

/**
 * Created by Tania on 2015-09-21.
 */
public class MapsActivity_Fragment extends Fragment{
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_maps, container, false);
        return rootview;
    }
}
