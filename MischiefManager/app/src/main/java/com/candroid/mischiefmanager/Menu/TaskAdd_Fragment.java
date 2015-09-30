package com.candroid.mischiefmanager.Menu;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.TaskContract;
import com.candroid.mischiefmanager.db.TaskDBHelper;

/**
 * Created by Firdous on 2015-09-28.
 */
public class TaskAdd_Fragment extends Fragment
{
    View rootview;
    private TaskDBHelper helper;
    EditText task;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootview = inflater.inflate(R.layout.activity_task_add, container, false);
        task = (EditText) rootview.findViewById(R.id.actual_task);

        Button add;
        add = (Button) rootview.findViewById(R.id.add_task);


        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_task:
                        Log.d("MainActivity", "clicked");






                         onAddClick(v);
                        break;
                }
            }
        });

        return rootview;
    }



    private void onAddClick(View v)
    {
        String taskText = task.getText().toString();

        helper = new TaskDBHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.clear();
        values.put(TaskContract.Columns.TASK, taskText);

        db.insertWithOnConflict(TaskContract.TABLE, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);

        ToDo_Fragment obj = new ToDo_Fragment();

        FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, obj)
                .commit();
    }


}
