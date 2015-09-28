package com.candroid.mischiefmanager.Menu;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.candroid.mischiefmanager.LAdapter;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.ToDo;
import com.candroid.mischiefmanager.db.TaskContract;
import com.candroid.mischiefmanager.db.TaskDBHelper;

/**
 * Created by Frank on 2015-09-20.
 * Edited by Firdous on 2015-09-23
 */
public class ToDo_Fragment extends Fragment{
    View rootview;
    private TaskDBHelper helper;
    ListView myList;
    AdapterView.OnItemClickListener d;
    LAdapter adapter;

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_to_do, container, false);
        //myList =  (ListActivity) getContext();
        myList =  (ListView) rootview.findViewById(R.id.list);





        Button add;
        add = (Button) rootview.findViewById(R.id.action_add_task);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.action_add_task:
                        Log.d("MainActivity", "clicked");
                        onAddClick(v);
                        break;
                }
            }
        });


      /*  myList.setOnItemClickListener(d);

        d = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d("MainActivity", "Inside");

            }
        };*/

       // Button done = (Button) rootview.findViewById(R.id.doneButton);

       // adapter = new LAdapter(getActivity(), );


/*
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View parentRow = (View) v.getParent();
                ListView li = (ListView) parentRow.getParent();
                final int pos = li.getPositionForView(parentRow);
                Log.d("MainActivity", "Inside");
            }
        });*/

        updateUI();

        return rootview;
    }



    public void onAddClick(View view)
    {
        View v = (View) view.getParent();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add a task");
        builder.setMessage("What do you want to do?");
        final EditText inputField = new EditText(getActivity());
        builder.setView(inputField);
        builder.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = inputField.getText().toString();
                        Log.d("MainActivity", task);

                        helper = new TaskDBHelper(getActivity());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.TASK, task);

                        db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        //Log.d("MainActivity", "Before");
                        updateUI();
                    }
                });



        builder.setNegativeButton("Cancel", null);

        builder.create().show();

        // Log.d("MainActivity", "fdfsdf");



    }


    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        Log.d("MainActivity", "Inside");
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        Log.d("MainActivity", "Inside2");

        String task = taskTextView.getText().toString();
        Log.d("MainActivity", "Inside3");
        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);
        Log.d("MainActivity", "Inside4");

        helper = new TaskDBHelper(getContext());
        Log.d("MainActivity", "Inside5");
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        Log.d("MainActivity", "Inside6");
        sqlDB.execSQL(sql);
        Log.d("MainActivity", "Inside7");
        updateUI();
    }




    private void updateUI()
    {
        helper = new TaskDBHelper(getActivity());
      //  Log.d("MainActivity", "Inside");
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
      //  Log.d("MainActivity", "Inside2");
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null, null, null, null, null);
       // Log.d("MainActivity", "Inside3");
        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.task_view,
                cursor,
                new String[]{TaskContract.Columns.TASK},
                new int[]{R.id.taskTextView},
                0
        );
      //  Log.d("MainActivity", "Inside4");
      //  List myList = new List();
      //  Log.d("MainActivity", "Inside5");
       // myList.setIt(listAdapter);
        myList.setAdapter(listAdapter);
      //  Log.d("MainActivity", "Inside6");


    }



}

