package com.candroid.mischiefmanager.Menu;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;

import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.JSONParser;
import com.candroid.mischiefmanager.db.TaskContract;
import com.candroid.mischiefmanager.db.TaskDBHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Firdous on 2015-09-28.
 */
public class TaskAdd_Fragment extends Fragment
{
    View rootview;
    private TaskDBHelper helper;
    EditText task;
   // String jsonResult;
    private static String url_add = "http://localhost/android/add_todo_item.php";
    JSONParser jsonParser = new JSONParser();
    EditText entry;
    Date date;
    Time time;
    UserLocalStore current;
    User loggedInUser;
    ProgressDialog create;
    String userDetails;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootview = inflater.inflate(R.layout.activity_task_add, container, false);
        task = (EditText) rootview.findViewById(R.id.actual_task);

        current = new UserLocalStore(getContext());

        loggedInUser = current.getLoggedInUser();
        userDetails = loggedInUser.getUserDetails().get(3);

        entry = (EditText) rootview.findViewById(R.id.actual_task);
       // date = (Date) rootview.findViewById((R.id.datePicker);
       // time.setTime(rootview.findViewById(( R.id.timePicker)));




        Button add;
        add = (Button) rootview.findViewById(R.id.add_task);


        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_task:
                         //onAddClick(v);
                        //CreateNewItem().execute();
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

        protected String doInBackground(String... args) {
           // String en = entry.getText().toString();
            String d = date.toString();
            String t = time.toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
           // params.add(new BasicNameValuePair("entry", en));
            params.add(new BasicNameValuePair("date", d));
            params.add(new BasicNameValuePair("time", t));
            params.add(new BasicNameValuePair("username", userDetails));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_add, "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());




                    ToDo_Fragment obj = new ToDo_Fragment();

                    FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, obj)
                            .commit();


            return null;
        }

        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog once done
            create.dismiss();
        }


    }


}
