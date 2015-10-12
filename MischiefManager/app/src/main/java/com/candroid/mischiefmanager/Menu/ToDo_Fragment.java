package com.candroid.mischiefmanager.Menu;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.JSONParser;
import com.candroid.mischiefmanager.db.TaskContract;
import com.candroid.mischiefmanager.db.TaskDBHelper;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    String userDetails;
    String task;
   // String jsonResult;
    private static String url_all_items = "http://imy.up.ac.za/Candroid/display_todo_list.php?username=";
    private static String url_delete_item = "http://imy.up.ac.za/Candroid/delete_todo_item.php";

    JSONArray items = null;
    private ProgressDialog load;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> itemList;


    // JSON Node names
    private static final String TAG_ITEMS = "items";
    private static final String TAG_entry = "entry";
    private static final String TAG_USER = "username";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_to_do, container, false);
        //myList =  (ListActivity) getContext();
        myList =  (ListView) rootview.findViewById(R.id.list);

        current = new UserLocalStore(getContext());

        loggedInUser = current.getLoggedInUser();
        userDetails = loggedInUser.getUserDetails().get(3);
        url_all_items+= userDetails;
        Log.d("MainActivity", userDetails);


        itemList = new ArrayList<>();

        new LoadAllItems().execute();




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
                        break;
                }
            }
        });



       // ToDoConnect conn = new ToDoConnect(getContext());

       d = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d("MainActivity", "Inside");

                Button done =  (Button) view.findViewById(R.id.doneButton);
                Button edit =  (Button) view.findViewById(R.id.edit);

                if(done.getVisibility() == View.VISIBLE)
                {
                   // Animation anim = new TranslateAnimation(done.getX(),done.getX()-10,done.getY(),done.getY());
                   // anim.setDuration(2000);
                   // done.startAnimation(anim);
                    done.setVisibility(View.INVISIBLE);
                    done.setOnClickListener(null);

                    edit.setVisibility(View.INVISIBLE);
                    edit.setOnClickListener(null);

                }
                else
                {
                    done.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    done.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Log.d("MainActivity", "Inside2");
                            //
                            task = rootview.findViewById(R.id.actual_task).toString();
                            new ItemDone().execute();
                        }
                    });

                    edit.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v) {

                            Log.d("MainActivity", "Inside2");

                        }
                    });

                }



            }
        };

        myList.setOnItemClickListener(d);

       // updateUI();

       // phpUI();






        return rootview;
    }

    private void phpUI()
    {

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

    class LoadAllItems extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();
            load = new ProgressDialog(getActivity());
            load.setMessage("Loading your domination tasks. Mwahahaha...");
            load.setIndeterminate(false);
            load.setCancelable(false);
            load.show();
        }

        protected String doInBackground(String... args)
        {
            List<NameValuePair> params = new ArrayList<>();
            //params.add(new BasicNameValuePair("username", userDetails));
            Log.d("MainActivity", "Inside");


           /* HashMap<String, String> map = new HashMap<String, String>();

            // adding each child node to HashMap key => value
            map.put(TAG_entry, "dsgdggdf");
            map.put(TAG_DATE, "2015-12-10");
            map.put(TAG_TIME, "08:15:01");
            itemList.add(map);*/

           // params.add((new BasicNameValuePair("username", userDetails)));

            JSONObject json = jParser.makeHttpRequest(url_all_items, "GET", params);
            Log.d("MainActivity", String.valueOf(json));
           // Log.d("MainActivity", json.toString());
            Log.d("MainActivity", "Inside3");
            try
            {
                items = json.getJSONArray(TAG_ITEMS);
                Log.d("MainActivity", "Inside4");
                for ( int index = 0; index < items.length(); index++)
                {
                    JSONObject c = items.getJSONObject(index);

                    // Storing each json item in variable
                    String entry = c.getString(TAG_entry);
                    String date = c.getString(TAG_DATE);
                    String time = c.getString(TAG_TIME);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_entry, entry);
                    map.put(TAG_DATE, date);
                    map.put(TAG_TIME, time);

                    // adding HashList to ArrayList
                    itemList.add(map);
                }
                Log.d("MainActivity", "Inside5");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog after getting all products
            Log.d("MainActivity", "Inside6");
            load.dismiss();
            Log.d("MainActivity", "Inside7");
            // updating UI from Background Thread
           /* runOnUiThread(new Runnable()
            {
                public void run()
                {*/
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            getActivity(), itemList,
                            R.layout.task_view, new String[] { TAG_entry, TAG_DATE, TAG_TIME},
                            new int[] { R.id.taskTextView, R.id.date, R.id.time });
                    // updating listview
            Log.d("MainActivity", "Inside8");
                   myList.setAdapter(adapter);
            Log.d("MainActivity", "Inside9");
               /* }
            });

        }*/

    }
    }



    class ItemDone extends AsyncTask<String, String, String>
    {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load = new ProgressDialog(getActivity());
            load.setMessage("Dominated!! Woohoo!!!");
            load.setIndeterminate(false);
            load.setCancelable(true);
            load.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // getting product details by making HTTP request
            url_delete_item+="?username="+userDetails+"&entry="+task;
            JSONObject json = jParser.makeHttpRequest(url_delete_item, "GET", params);

            // check your log for json response
            Log.d("Delete Product", json.toString());

            // json success tag

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog once product deleted
            load.dismiss();

            ToDo_Fragment obj = new ToDo_Fragment();

            FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, obj)
                    .commit();

            //getActivity().recreate();

        }

    }




}



