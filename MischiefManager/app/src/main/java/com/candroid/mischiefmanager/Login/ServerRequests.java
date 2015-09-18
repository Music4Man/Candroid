package com.candroid.mischiefmanager.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

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

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack callBack;

        public fetchUserDataAsyncTask(User user, GetUserCallBack callBack) {
            this.user = user;
            this.callBack = callBack;
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

        public String post(JSONObject object) throws Exception {
            HttpURLConnection connection = null;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {

                System.setProperty("http.keepAlive", "false");
            }

            try {

                URL url = new URL("http://"+SERVER_ADDRESS+"Register.php");
                connection = (HttpURLConnection) url.openConnection();


                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setReadTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("parametros", object.toString());

                String query = builder.build().getEncodedQuery();

                connection.setFixedLengthStreamingMode(query.getBytes().length);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    StringBuilder sb = new StringBuilder();
                    try {

                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String linha;

                        while ((linha = br.readLine()) != null) {

                            sb.append(linha);
                        }

                        return sb.toString();
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                } else {

                    if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {

                        throw new Exception("Timed out: " + connection.getErrorStream());
                    }
                }

            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
                throw new Exception("Internet error");
            } finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected Void doInBackground(Void... params){
            JSONObject dataToSend = new JSONObject();
            try {
                dataToSend.put("name", user.name);
                dataToSend.put("surname", user.surname);
                dataToSend.put("age", user.age + "");
                dataToSend.put("email", user.email);
                dataToSend.put("password", user.password);
                dataToSend.put("nickname", user.nickname);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                post(dataToSend);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
