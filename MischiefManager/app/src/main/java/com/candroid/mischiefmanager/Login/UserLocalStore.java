package com.candroid.mischiefmanager.Login;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Elzahn on 17/09/2015.
 */
public class UserLocalStore {
    public static final String SP_NAME="userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.name);
        spEditor.putString("surname", user.surname);
        spEditor.putString("nickname", user.nickname);
        spEditor.putString("email", user.email);
        spEditor.putString("password", user.password);
        spEditor.putInt("age", user.age);
        spEditor.commit();
    }

    public boolean getLoggedIn(){
       return userLocalDatabase.getBoolean("loggedIn", false);
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor  spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor  spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
