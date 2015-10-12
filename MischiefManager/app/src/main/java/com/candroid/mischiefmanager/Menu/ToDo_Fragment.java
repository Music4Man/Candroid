package com.candroid.mischiefmanager.Menu;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.JSONParser;

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
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_to_do, container, false);

        myList =  (ListView) rootview.findViewById(R.id.list);

        current = new UserLocalStore(getContext());

        loggedInUser = current.getLoggedInUser();
        userDetails = loggedInUser.getUserDetails().get(3);
        url_all_items+= userDetails;

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
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
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

                            Log.d("MainActivity", "Inside");
                            //
                           TextView vi = (TextView) view.findViewById(R.id.taskTextView);
                            task = vi.getText().toString();
                            Log.d("MainActivity", task);
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

        return rootview;
    }



    class LoadAllItems extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();
            load = new ProgressDialog(getActivity());
            load.setMessage("Refreshing your domination tasks. Mwahahaha...");
            load.setIndeterminate(false);
            load.setCancelable(false);
            load.show();
        }

        protected String doInBackground(String... args)
        {


           // params.add((new BasicNameValuePair("username", userDetails)));

            JSONObject json = jParser.makeHttpRequest(url_all_items, "GET");
            Log.d("MainActivity", String.valueOf(json));

            try
            {
                items = json.getJSONArray(TAG_ITEMS);

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

            load.dismiss();

                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            getActivity(), itemList,
                            R.layout.task_view, new String[] { TAG_entry, TAG_DATE, TAG_TIME},
                            new int[] { R.id.taskTextView, R.id.date, R.id.time });
                    // updating listview

                   myList.setAdapter(adapter);

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

            // getting product details by making HTTP request
            url_delete_item+="?username="+userDetails+"&entry="+task;
            JSONObject json = jParser.makeHttpRequest(url_delete_item, "GET");

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

           /*ToDo_Fragment obj = new ToDo_Fragment();

            FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, obj)
                    .commit();*/
            itemList = null;
            itemList = new ArrayList<>();
            new LoadAllItems().execute();

        }

    }




}



