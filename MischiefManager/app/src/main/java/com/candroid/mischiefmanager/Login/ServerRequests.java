package com.candroid.mischiefmanager.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Elzahn on 17/09/2015.
 */
public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "127.0.0.1/Candroid";

    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataToServer(User user, GetUserCallBack callBack){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, callBack).execute();
    }

    public void fetchUserDataFromServer(User user, GetUserCallBack callBack){
        progressDialog.show();
        new fetchUserDataAsyncTask(user, callBack).execute();
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
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());

            wr.writeBytes (urlParameters);
            wr.flush();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;

            ArrayList<String> returnedArray = new ArrayList<>();

            while((line = rd.readLine()) != null) {
                Log.d("Register", line);
                if(!line.equals("[]")) {
                    String[] temp = line.split(",");
                    int pos = 13;
                    int lastPos = temp[0].lastIndexOf('"');
                    temp[0] = temp[0].substring(pos, lastPos);

                    pos = 12;
                    lastPos = temp[1].lastIndexOf('"');
                    temp[1] = temp[1].substring(pos, lastPos);

                    returnedArray.add(temp[0].toString());
                    returnedArray.add(temp[1].toString());
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

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack callBack;

        public fetchUserDataAsyncTask(User user, GetUserCallBack callBack) {
            this.user = user;
            this.callBack = callBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            String dataToSend = null;

            try {
                dataToSend = "nickname=" + URLEncoder.encode(user.nickname, "UTF-8") +
                        "&password=" + URLEncoder.encode(user.password, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            ArrayList<String> temp = excutePost("http://10.0.2.2:8000/Candroid/fetchUserData.php", dataToSend);

            if(temp.isEmpty()){
                return null;
            } else {
                User returnedUser = new User(temp.get(0), temp.get(1));
                return returnedUser;
            }
        }

        @Override
        protected void onPostExecute(User user){
            progressDialog.dismiss();
            callBack.done(user);
            super.onPostExecute(user);
        }
    }

    //background task is async
    //how we get, send, adn return - Voids
    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallBack callBack;

        public StoreUserDataAsyncTask(User user, GetUserCallBack callBack) {
            this.user = user;
            this.callBack = callBack;
        }

        @Override
        protected Void doInBackground(Void... params){
            String dataToSend = null;
            try {
                dataToSend = "name=" + URLEncoder.encode(user.name, "UTF-8") +
                            "&surname=" + URLEncoder.encode(user.surname, "UTF-8") +
                            "&age=" + URLEncoder.encode(String.valueOf(user.age), "UTF-8") +
                            "&email=" + URLEncoder.encode(user.email, "UTF-8") +
                            "&password=" + URLEncoder.encode(user.password, "UTF-8")+
                            "&nickname=" + URLEncoder.encode(user.nickname, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            excutePost("http://10.0.2.2:8000/Candroid/registerNewUser.php", dataToSend);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            progressDialog.dismiss();
            callBack.done(null);
            super.onPostExecute(aVoid);
        }
    }
}
