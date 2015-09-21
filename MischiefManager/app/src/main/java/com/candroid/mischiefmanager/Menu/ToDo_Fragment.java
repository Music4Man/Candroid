package com.candroid.mischiefmanager.Menu;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.ToDo;
import com.candroid.mischiefmanager.db.TaskContract;
import com.candroid.mischiefmanager.db.TaskDBHelper;

/**
 * Created by Frank on 2015-09-20.
 */
public class ToDo_Fragment extends Fragment{
    View rootview;
    private TaskDBHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_to_do, container, false);

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

                        Log.d("MainActivity", "Before");
                        updateUI();
                    }
                });

        builder.setNegativeButton("Cancel",null);

        builder.create().show();

        // Log.d("MainActivity", "fdfsdf");



    }


    private void updateUI()
    {
        helper = new TaskDBHelper(getActivity());
        Log.d("MainActivity", "Inside");
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null, null, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.task_view,
                cursor,
                new String[]{TaskContract.Columns.TASK},
                new int[]{R.id.taskTextView},
                0
        );

        List myList = new List();
        myList.setListAdapter(listAdapter);

    }



}


class List extends ListActivity
{
    public void setIt(SimpleCursorAdapter list)
    {
        this.setListAdapter(list);
    }
}