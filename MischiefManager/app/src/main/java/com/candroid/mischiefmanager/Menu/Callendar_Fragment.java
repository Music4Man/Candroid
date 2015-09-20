package com.candroid.mischiefmanager.Menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candroid.mischiefmanager.R;

/**
 * Created by Frank on 2015-09-20.
 */
public class Callendar_Fragment extends Fragment{
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_callendar, container, false);
        return rootview;
    }
}
