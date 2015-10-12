package com.candroid.mischiefmanager.Menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Frank on 2015-09-20.
 */
public class Callendar_Fragment extends Fragment{
    // JSON Node names
    private static final String TAG_ITEMS = "items";
    private static final String TAG_entry = "entry";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";

    JSONParser jParser = new JSONParser();
    View rootview;
    ListView myList;
    CalendarView Cview;
    long date;
    String dateString;
    UserLocalStore current;
    User loggedInUser;
    String userDetails;
    JSONArray items = null;
    ArrayList<HashMap<String, String>> itemList;
    String url_all_items = "http://imy.up.ac.za/Candroid/display_todo_Calendar.php?username=";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_callendar, container, false);
        Cview = (CalendarView) rootview.findViewById(R.id.calendarView);
        current = new UserLocalStore(getContext());
        myList =  (ListView) rootview.findViewById(R.id.listC);
        loggedInUser = current.getLoggedInUser();
        userDetails = loggedInUser.getUserDetails().get(3);

        itemList = new ArrayList<>();
        new LoadAllItems().execute();
        Cview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            date = Cview.getDate();
            if (month<10 && dayOfMonth<10)
            {
                dateString = String.valueOf(year)+ "-0" +String.valueOf(month) + "-0" + String.valueOf(dayOfMonth);
            }
            else
            if (month<10)
            {
                dateString = String.valueOf(year)+ "-0" +String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }
            else
            if (dayOfMonth<10)
            {
                dateString = String.valueOf(year)+ "-" +String.valueOf(month) + "-0" + String.valueOf(dayOfMonth);
            }
            else
            {

                dateString = String.valueOf(year)+ "-" +String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }

            //if(Cview.getDate() != date) {
            try {
                url_all_items += URLEncoder.encode(userDetails,"UTF-8")+"&date="+URLEncoder.encode(dateString,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            itemList = null;
                itemList = new ArrayList<>();
                new LoadAllItems().execute();
            //}
            //showToDo(userDetails, dateString);
        }
    });

        return rootview;
    }

    class LoadAllItems extends AsyncTask<String, String, String> {
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {


            // params.add((new BasicNameValuePair("username", userDetails)));
            JSONObject json = jParser.makeHttpRequest(url_all_items, "GET");


            try {
                items = json.getJSONArray(TAG_ITEMS);

                for (int index = 0; index < items.length(); index++) {

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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products


            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), itemList,
                    R.layout.task_view, new String[] { TAG_entry, TAG_DATE, TAG_TIME},
                    new int[] { R.id.JournalTitle, R.id.date, R.id.time });
            // updating listview

            myList.setAdapter(adapter);

        }
    }
}

