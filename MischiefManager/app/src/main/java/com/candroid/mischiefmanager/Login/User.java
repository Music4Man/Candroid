package com.candroid.mischiefmanager.Login;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Elzahn on 17/09/2015.
 */
public class User {
    String name, surname, nickname, email, password;
    int age;

    public User(String name, int age, String nickname, String surname, String password, String email)
    {
        this.name = name;
        this. surname = surname;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.age = age;
    }

    public ArrayList<String> getUserDetails(){
        ArrayList<String> user = new ArrayList<>();
        user.add(name);
        user.add(surname);
        user.add(String.valueOf(age));
        user.add(nickname);
        user.add(email);
        user.add(password);

        return user;
    }

    public User(String nickname, String password)
    {
        this.name = "";
        this. surname = "";
        this.email = "";
        this.nickname = nickname;
        this.password = password;
        this.age = -1;
    }
}
