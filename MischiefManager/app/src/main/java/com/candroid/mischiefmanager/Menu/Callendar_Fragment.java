package com.candroid.mischiefmanager.Menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    CalendarView Cview;
    long date;
    String dateString;
    UserLocalStore current;
    User loggedInUser;
    String userDetails;
    JSONArray items = null;
    ArrayList<HashMap<String, String>> itemList;
    String url_all_items = "http://imy.up.ac.za/Candroid/display_todo_list.php?username=";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_callendar, container, false);
        Cview = (CalendarView) rootview.findViewById(R.id.calendarView);
        current = new UserLocalStore(getContext());

        loggedInUser = current.getLoggedInUser();
        userDetails = loggedInUser.getUserDetails().get(3);
        url_all_items += userDetails;
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
            Toast.makeText(view.getContext(),userDetails + dateString, Toast.LENGTH_SHORT).show();


            //showToDo(userDetails, dateString);
        }
    });
        return rootview;
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
}

