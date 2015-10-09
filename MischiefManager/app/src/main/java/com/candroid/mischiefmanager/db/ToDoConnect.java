package com.candroid.mischiefmanager.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.candroid.mischiefmanager.Login.GetUserCallBack;
import com.candroid.mischiefmanager.Login.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Firdous
 */
public class ToDoConnect {
    ProgressDialog updateDialog, loadDialog;

    public ToDoConnect(Context context){
        updateDialog = new ProgressDialog(context);
        updateDialog.setCancelable(false);
        updateDialog.setTitle("Updating item list");
        updateDialog.setMessage("Please wait...");

        loadDialog = new ProgressDialog(context);
        loadDialog.setCancelable(false);
        loadDialog.setTitle("Loading");
        loadDialog.setMessage("Please wait...");

    }


    public void displayItemDataFromServer(String username)
    {
        loadDialog.show();
        new displayItemDataAsyncTask(username);
    }

    public ArrayList<String> excutePost(String targetURL, String urlParameters)
    {
        URL url;
        HttpURLConnection connection = null;

        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");

            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    PasswordAuthentication pass = new PasswordAuthentication("Candroid", "kmTYHA6q".toCharArray());
                    return pass;
                }
            });

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());

            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            String line;
            ArrayList<String> returnedArray = new ArrayList<>();

            while ((line = rd.readLine()) != null) {
                if (!line.equals("[]")) {
                    String[] temp = line.split(",");
                    int pos = 13;
                    int lastPos = temp[0].lastIndexOf('"');
                    temp[0] = temp[0].substring(pos, lastPos);

                    pos = 12;
                    lastPos = temp[1].lastIndexOf('"');
                    temp[1] = temp[1].substring(pos, lastPos);

                    pos = 7;
                    lastPos = temp[2].lastIndexOf('"');
                    temp[2] = temp[2].substring(pos, lastPos);

                    pos = 9;
                    lastPos = temp[3].lastIndexOf('"');
                    temp[3] = temp[3].substring(pos, lastPos);

                    pos = 11;
                    lastPos = temp[4].lastIndexOf('"');
                    temp[4] = temp[4].substring(pos, lastPos);

                    returnedArray.add(temp[0]);
                    returnedArray.add(temp[1]);
                    returnedArray.add(temp[2]);
                    returnedArray.add(temp[3]);
                    returnedArray.add(temp[4]);
                    returnedArray.add(temp[5]);
                }
            }
            rd.close();
            return returnedArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }



    public class displayItemDataAsyncTask extends AsyncTask<String, String, String>
    {
        String username;

        public displayItemDataAsyncTask(String username)
        {
            this.username = username;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String dataToSend = null;

            try
            {
                dataToSend = "username=" + URLEncoder.encode(username, "UTF-8");
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            ArrayList<String> temp = excutePost("http://imy.up.ac.za/Candroid/display_todo_list.php", dataToSend);

            if(temp.isEmpty())
            {
                return null;
            } else
            {
                return username;
            }
        }

    }


}
