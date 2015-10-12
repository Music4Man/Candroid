package com.candroid.mischiefmanager.Menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.db.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;


/**
 * Created by Firdous
 */
public class Edit_todo_item extends Fragment
{

    View rootview;
    //EditText task;

    private static String url_fetch_item = "http://localhost/android/edit_todo_item.php";
    private static String url_update_item = "http://localhost/android/update_todo_item.php";
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

    // JSON Node names
    private static final String TAG_ITEMS = "items";
    private static final String TAG_entry = "entry";
    private static final String TAG_USER = "username";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_edit_item, container, false);
        //task = (EditText) rootview.findViewById(R.id.actual_task);

        //current = new UserLocalStore(getContext());

        loggedInUser = current.getLoggedInUser();
        userDetails = loggedInUser.getUserDetails().get(3);

        entry = (EditText) rootview.findViewById(R.id.actual_task);
        //date = (Date) rootview.findViewById(R.id.datePicker);
       // time.setTime(rootview.findViewById(( R.id.timePicker)));

        btnSave = (Button) rootview.findViewById(R.id.save_changes);
        btnCancel = (Button) rootview.findViewById(R.id.cancel);

        new GetItemDetails().execute();


        btnSave.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SaveItemDetails().execute();
            }
        });

        // Delete button click event
        btnCancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
               /* ToDo_Fragment obj = new ToDo_Fragment();

                FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, obj)
                        .commit();*/

            }
        });


        return rootview;
    }


    class GetItemDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetch = new ProgressDialog(getActivity());
            fetch.setMessage("Loading product details. Please wait...");
            fetch.setIndeterminate(false);
            fetch.setCancelable(true);
            //pDialog.show();
        }

        protected String doInBackground(String... params)
        {
           /* List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pid", pid));

            JSONObject json = jsonParser.makeHttpRequest(
                    url_product_detials, "GET", params);

            Log.d("Single Product Details", json.toString());

            try
            {
                JSONArray productObj = json.getJSONArray(TAG_ITEMS);
                JSONObject product = productObj.getJSONObject(0);

                txtName = (EditText) findViewById(R.id.inputName);
                txtPrice = (EditText) findViewById(R.id.inputPrice);
                txtDesc = (EditText) findViewById(R.id.inputDesc);

                // display product data in EditText
                txtName.setText(product.getString(TAG_NAME));
                txtPrice.setText(product.getString(TAG_PRICE));
                txtDesc.setText(product.getString(TAG_DESCRIPTION));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }*/

            return null;
        }
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

            // getting updated data from EditTexts
            /*String name = txtName.getText().toString();
            String price = txtPrice.getText().toString();
            String description = txtDesc.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_PRICE, price));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                    "POST", params);

            */

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog once item updated
           fetch.dismiss();
        }
    }
}
