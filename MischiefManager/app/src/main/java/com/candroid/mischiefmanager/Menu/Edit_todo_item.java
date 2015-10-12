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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.JSONParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Firdous
 */
public class Edit_todo_item extends Fragment
{

    View rootview;
    //EditText task
    private static String url_update_item = "http://imy.up.ac.za/Candroid/update_todo_item.php";
    JSONParser jsonParser = new JSONParser();
    EditText entry;
    Date date;
    Time time;
    UserLocalStore current;
    User loggedInUser;
    ProgressDialog fetch;
    String userDetails;
    Button btnSave;
    Button btnCancel;
    Date datetime;
    Date tDate;
    String updatedDated, updEntry, updTime;
    String entryText;

    // JSON Node names
    private static final String TAG_ITEMS = "items";
    private static final String TAG_entry = "entry";
    private static final String TAG_USER = "username";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.activity_edit_item, container, false);
        //task = (EditText) rootview.findViewById(R.id.actual_task);

        current = new UserLocalStore(getContext());

        loggedInUser = current.getLoggedInUser();
        userDetails = loggedInUser.getUserDetails().get(3);

        entryText = getArguments().getString("entry");
        String iniDateTime = getArguments().getString("date")+" "+getArguments().getString("time");

        entry = (EditText) rootview.findViewById(R.id.actual_task);
        entry.setText(entryText);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try
        {
            datetime = formatter.parse(iniDateTime);
            tDate = formatter2.parse(getArguments().getString("date"));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        final TimePicker timePicker = (TimePicker) rootview.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(datetime.getHours());
        timePicker.setMinute(datetime.getMinutes());

        Calendar c = Calendar.getInstance();
        c.setTime(tDate);

        final DatePicker datePicker = (DatePicker) rootview.findViewById(R.id.datePicker);
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);



        btnSave = (Button) rootview.findViewById(R.id.save_changes);
        btnCancel = (Button) rootview.findViewById(R.id.cancel);

      //  new GetItemDetails().execute();


        btnSave.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product


                if (datePicker.getMonth()<10 && datePicker.getDayOfMonth()<10)
                {
                    updatedDated = String.valueOf(datePicker.getYear())+ "-0" +String.valueOf(datePicker.getMonth()) + "-0" + String.valueOf(datePicker.getDayOfMonth());
                }
                else
                if (datePicker.getMonth()<10)
                {
                    updatedDated = String.valueOf(datePicker.getYear())+ "-0" +String.valueOf(datePicker.getMonth()) + "-" + String.valueOf(datePicker.getDayOfMonth());
                }
                else
                if (datePicker.getDayOfMonth()<10)
                {
                    updatedDated = String.valueOf(datePicker.getYear())+ "-" +String.valueOf(datePicker.getMonth()) + "-0" + String.valueOf(datePicker.getDayOfMonth());
                }
                else
                {

                    updatedDated = String.valueOf(datePicker.getYear())+ "-" +String.valueOf(datePicker.getMonth()) + "-" + String.valueOf(datePicker.getDayOfMonth());
                }

                String.valueOf(datePicker.getDayOfMonth());

                updEntry = entry.getText().toString();

                updTime="";
                if (timePicker.getHour()<10)
                    updTime += "0";

                updTime+=String.valueOf(timePicker.getHour())+":";

                if(timePicker.getMinute() < 10)
                    updTime+= "0";

                updTime+= String.valueOf(timePicker.getMinute());

                Log.d("MainActivity", "Time" + updTime);
                Log.d("MainActivity", "Date"+updatedDated);



                new SaveItemDetails().execute();
            }
        });

        // Delete button click event
        btnCancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                ToDo_Fragment obj = new ToDo_Fragment();

                FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, obj)
                        .commit();

            }
        });



        return rootview;
    }


    class SaveItemDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetch = new ProgressDialog(getActivity());
            fetch.setMessage("Saving changes to evil plans ;)");
            fetch.setIndeterminate(false);
            fetch.setCancelable(true);
            fetch.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            try {
                url_update_item+="?username="+ URLEncoder.encode(userDetails, "UTF-8")+"&entry="+URLEncoder.encode(updEntry, "UTF-8") +"&date="+URLEncoder.encode(updatedDated, "UTF-8") +"&time="+URLEncoder.encode(updTime, "UTF-8")+"&oldEntry="+URLEncoder.encode(entryText, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.d("serverError", url_update_item);
            JSONObject json = jsonParser.makeHttpRequest(url_update_item, "GET");



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog once item updated
           fetch.dismiss();

            ToDo_Fragment obj = new ToDo_Fragment();

            FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, obj)
                    .commit();

        }
    }
}
