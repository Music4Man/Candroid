package com.candroid.mischiefmanager.Menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.candroid.mischiefmanager.R;


/**
 * Created by Firdous
 */
public class Edit_todo_item extends Fragment
{

    View rootview;
    EditText task;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_task_add, container, false);
        task = (EditText) rootview.findViewById(R.id.actual_task);

        return rootview;
    }
}
