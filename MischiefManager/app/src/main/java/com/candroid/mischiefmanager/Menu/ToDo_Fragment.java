package com.candroid.mischiefmanager.Menu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.candroid.mischiefmanager.Login.Login;
import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.TaskContract;
import com.candroid.mischiefmanager.db.TaskDBHelper;
import com.candroid.mischiefmanager.db.ToDoConnect;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Frank on 2015-09-20.
 * Edited by Firdous on 2015-09-23
 */
public class ToDo_Fragment extends Fragment{
    View rootview;
    private TaskDBHelper helper;
    ListView myList;
    AdapterView.OnItemClickListener d;
    UserLocalStore current;
    User loggedInUser;
    String jsonResult;
    private static String url_all_items = "http://localhost/android/display_todo_list.php";
    ArrayList<HashMap<String, String>> itemList;
    JSONArray products = null;

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_to_do, container, false);
        //myList =  (ListActivity) getContext();
        myList =  (ListView) rootview.findViewById(R.id.list);

        current = new UserLocalStore(getContext());

        loggedInUser = current.getLoggedInUser();
        String userDetails = loggedInUser.getUserDetails().get(3);

        Button add;
        add = (Button) rootview.findViewById(R.id.action_add_task);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.action_add_task:
                        Log.d("MainActivity", "clicked");
                        TaskAdd_Fragment obj = new TaskAdd_Fragment();

                        FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, obj)
                                .commit();

                        // startActivity(getContext(), TaskAdd.class);
                        // onAddClick(v);
                        break;
                }
            }
        });



        ToDoConnect conn = new ToDoConnect(getContext());

        d = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d("MainAct9ivity", "Inside");

                Button done =  (Button) view.findViewById(R.id.doneButton);

                if(done.getVisibility() == View.VISIBLE)
                {
                   // Animation anim = new TranslateAnimation(done.getX(),done.getX()-10,done.getY(),done.getY());
                   // anim.setDuration(2000);
                   // done.startAnimation(anim);
                    done.setVisibility(View.INVISIBLE);
                    done.setOnClickListener(null);

                }
                else
                {
                    done.setVisibility(View.VISIBLE);
                    done.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Log.d("MainActivity", "Inside2");
                            onDoneButtonClick(v);
                        }
                    });


                }



            }
        };

        myList.setOnItemClickListener(d);

        updateUI();

        return rootview;
    }


    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);

        String task = taskTextView.getText().toString();
        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);

        helper = new TaskDBHelper(getContext());
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }




    private void updateUI()
    {
        helper = new TaskDBHelper(getActivity());
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null, null, null, null, null);

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false)
        {
            /*Button done = (Button) getView().findViewById(R.id.doneButton);
            done.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Log.d("MainActivity", "Inside2hhh");
                }


            });*/
            cursor.moveToNext();
        }

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.task_view,
                cursor,
                new String[]{TaskContract.Columns.TASK},
                new int[]{R.id.taskTextView},
                0
        );

         myList.setAdapter(listAdapter);
    }




}



