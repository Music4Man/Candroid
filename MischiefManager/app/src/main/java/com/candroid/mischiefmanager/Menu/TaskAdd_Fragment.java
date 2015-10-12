package com.candroid.mischiefmanager.Menu;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.JSONParser;
import com.candroid.mischiefmanager.db.TaskContract;
import com.candroid.mischiefmanager.db.TaskDBHelper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Firdous on 2015-09-28.
 */
public class TaskAdd_Fragment extends Fragment
{
    View rootview;
    private TaskDBHelper helper;
    EditText task;
   // String jsonResult;
    private static String url_add = "http://imy.up.ac.za/Candroid/add_todo_item.php";
    JSONParser jsonParser = new JSONParser();
    EditText e;
    String entry;
    String date;
    String time;
    UserLocalStore current;
    User loggedInUser;
    ProgressDialog create;
    String userDetails;
    DatePicker d;
    TimePicker t;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootview = inflater.inflate(R.layout.activity_task_add, container, false);
        task = (EditText) rootview.findViewById(R.id.actual_task);

        current = new UserLocalStore(getContext());
        loggedInUser = current.getLoggedInUser();
        userDetails = loggedInUser.getUserDetails().get(3);

        e = (EditText) rootview.findViewById(R.id.actual_task);
       // date = (Date) rootview.findViewById((R.id.datePicker);
       // time.setTime(rootview.findViewById(( R.id.timePicker)));

         d = (DatePicker) rootview.findViewById(R.id.datePicker);
        t = (TimePicker) rootview.findViewById(R.id.timePicker);
        t.setIs24HourView(true);





        Button add;
        add = (Button) rootview.findViewById(R.id.add_task);


        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_task:
                         //onAddClick(v);

                        if (d.getMonth()<10 && d.getDayOfMonth()<10)
                        {
                            date = String.valueOf(d.getYear())+ "-0" +String.valueOf(d.getMonth()) + "-0" + String.valueOf(d.getDayOfMonth());
                        }
                        else
                        if (d.getMonth()<10)
                        {
                            date = String.valueOf(d.getYear())+ "-0" +String.valueOf(d.getMonth()) + "-" + String.valueOf(d.getDayOfMonth());
                        }
                        else
                        if (d.getDayOfMonth()<10)
                        {
                            date = String.valueOf(d.getYear())+ "-" +String.valueOf(d.getMonth()) + "-0" + String.valueOf(d.getDayOfMonth());
                        }
                        else
                        {

                            date = String.valueOf(d.getYear())+ "-" +String.valueOf(d.getMonth()) + "-" + String.valueOf(d.getDayOfMonth());
                        }

                        String.valueOf(d.getDayOfMonth());

                        entry = e.getText().toString();

                        time="";
                        if (t.getHour()<10)
                            time += "0";

                             time+=String.valueOf(t.getHour())+":";

                        if(t.getMinute() < 10)
                            time+= "0";

                        time+= String.valueOf(t.getMinute());

                        Log.d("MainActivity", "Time"+time);
                        Log.d("MainActivity", "Date"+date);

                        new CreateNewItem().execute();
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




    class CreateNewItem extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();
            create = new ProgressDialog(getActivity());
            create.setMessage("Creating Product..");
            create.setIndeterminate(false);
            create.setCancelable(true);
            create.show();
        }

        protected String doInBackground(String... args)
        {
           // String en = entry.getText().toString();


            try {
                url_add+="?username="+ URLEncoder.encode(userDetails, "UTF-8")+"&entry="+URLEncoder.encode(entry, "UTF-8") +"&date="+URLEncoder.encode(date, "UTF-8") +"&time="+URLEncoder.encode(time, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.d("serverError", url_add);
            JSONObject json = jsonParser.makeHttpRequest(url_add, "GET");



            return null;
        }

        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog once done
            create.dismiss();

            ToDo_Fragment obj = new ToDo_Fragment();

            FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, obj)
                    .commit();
        }


    }


}
