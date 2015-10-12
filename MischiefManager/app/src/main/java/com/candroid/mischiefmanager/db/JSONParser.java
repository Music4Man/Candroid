package com.candroid.mischiefmanager.db;

/**
 * Created by Firdous
 */

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";


    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String u, String method,
                                      List<NameValuePair> params) {

        HttpURLConnection connection = null;
       try {
           // Making HTTP request

               URL url = new URL(u);
               connection = (HttpURLConnection) url.openConnection();
               connection.setRequestMethod("GET");

           Authenticator.setDefault(new Authenticator() {
               @Override
               protected PasswordAuthentication getPasswordAuthentication() {
                   PasswordAuthentication pass = new PasswordAuthentication("Candroid", "kmTYHA6q".toCharArray());
                   return pass;
               }
           });

           connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
          // connection.setRequestProperty("Content-Length", "" + Integer.toString(params.size()));
           connection.setRequestProperty("Content-Language", "en-US");


           //connection.setUseCaches(false);
           //connection.setDoInput(true);

            int code = connection.getResponseCode();
           Log.d("serverError", String.valueOf(code));
          // DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
           String paramString = URLEncodedUtils.format(params, "utf-8");
           u += "?" + paramString;
         //  wr.writeBytes(paramString);
         //  wr.flush();
         //  wr.close();




            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
            json = sb.toString();

    } catch (Exception e)
       {
        e.printStackTrace();
        Log.d("serverError2", e.toString());
        return null;
    } finally
       {
           //18)close the connection
           if (connection != null)
           {
               connection.disconnect();
           }
       }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}