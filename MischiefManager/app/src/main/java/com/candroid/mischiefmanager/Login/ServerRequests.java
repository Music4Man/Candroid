package com.candroid.mischiefmanager.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by Elzahn on 17/09/2015.
 */
public class ServerRequests {
    ProgressDialog updateDialog, loadDialog, registerDialog;

    public ServerRequests(Context context){
        updateDialog = new ProgressDialog(context);
        updateDialog.setCancelable(false);
        updateDialog.setTitle("Updating your profile");
        updateDialog.setMessage("Please wait...");

        loadDialog = new ProgressDialog(context);
        loadDialog.setCancelable(false);
        loadDialog.setTitle("Logging in");
        loadDialog.setMessage("Please wait...");

        registerDialog = new ProgressDialog(context);
        registerDialog.setCancelable(false);
        registerDialog.setTitle("Registering your profile");
        registerDialog.setMessage("Please wait...");
    }

    public void updateUserData(User user, String originalNickName, String oldPassword, GetUserCallBack callBack){
        updateDialog.show();
        new updateUserAsyncTask(user, originalNickName, oldPassword, callBack).execute();
    }

    public void storeUserDataToServer(User user, GetUserCallBack callBack){
        registerDialog.show();
        new StoreUserDataAsyncTask(user, callBack).execute();
    }

    public void fetchUserDataFromServer(User user, GetUserCallBack callBack){
        loadDialog.show();
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

                    pos = 8;
                    lastPos = temp[5].lastIndexOf('"');
                    temp[5] = temp[5].substring(pos, lastPos);

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
            Log.d("serverError", e.toString());
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public class updateUserAsyncTask extends AsyncTask<Void, String, User>{
        User user;
        String originalNickName;
        String oldPassword;
        GetUserCallBack callBack;

        public updateUserAsyncTask(User user, String originalNickName, String oldPassword, GetUserCallBack callBack){
            this.user = user;
            this.callBack = callBack;
            this.originalNickName = originalNickName;
            this.oldPassword = oldPassword;
        }

        @Override
        protected User doInBackground(Void... params) {
            String dataToSend = null;

            try {
                dataToSend = "name=" + URLEncoder.encode(user.name, "UTF-8") +
                        "&surname=" + URLEncoder.encode(user.surname, "UTF-8") +
                        "&age=" + URLEncoder.encode(String.valueOf(user.age), "UTF-8") +
                        "&email=" + URLEncoder.encode(user.email, "UTF-8") +
                        "&password=" + URLEncoder.encode(user.password, "UTF-8")+
                        "&nickname=" + URLEncoder.encode(user.nickname, "UTF-8")+
                        "&originalNickName=" + URLEncoder.encode(originalNickName, "UTF-8")+
                        "&oldPassword=" + URLEncoder.encode(oldPassword, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            ArrayList<String> temp = excutePost("http://imy.up.ac.za/Candroid/updateUserData.php", dataToSend);

            if(temp == null){
                return null;
            } else {
                User returnedUser = new User(temp.get(5), Integer.parseInt(temp.get(2)), temp.get(0), temp.get(4), temp.get(1), temp.get(3));
                return returnedUser;
            }
        }

        @Override
        protected void onPostExecute(User user){
            updateDialog.dismiss();
            callBack.done(user);
            super.onPostExecute(user);
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

            ArrayList<String> temp = excutePost("http://imy.up.ac.za/Candroid/fetchUserData.php", dataToSend);

            if(temp == null){
                return null;
            } else {
                User returnedUser = new User(temp.get(5), Integer.parseInt(temp.get(2)), temp.get(0), temp.get(4), temp.get(1), temp.get(3));
                return returnedUser;
            }
        }

        @Override
        protected void onPostExecute(User user){
            loadDialog.dismiss();
            callBack.done(user);
            super.onPostExecute(user);
        }
    }

    //background task is async
    //how we get, send, adn return - Voids
    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack callBack;

        public StoreUserDataAsyncTask(User user, GetUserCallBack callBack) {
            this.user = user;
            this.callBack = callBack;
        }

        @Override
        protected User doInBackground(Void... params) {
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

            ArrayList<String> temp = excutePost("http://imy.up.ac.za/Candroid/registerNewUser.php", dataToSend);

            if(temp == null){
                return null;
            } else {
                User returnedUser = new User(temp.get(5), Integer.parseInt(temp.get(2)), temp.get(0), temp.get(4), temp.get(1), temp.get(3));
                return returnedUser;
            }
        }

        @Override
        protected void onPostExecute(User user){
            registerDialog.dismiss();
            callBack.done(user);
            super.onPostExecute(user);
        }
    }
}
