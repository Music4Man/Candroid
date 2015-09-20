package com.candroid.mischiefmanager.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candroid.mischiefmanager.Login.Login;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;

/**
 * Created by Frank on 2015-09-20.
 */
public class Logout_Fragment extends Fragment{
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       /* UserLocalStore userLocalStore;
        userLocalStore = new UserLocalStore(Login.this);
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);

        rootview = inflater.inflate(R.layout.activity_login, container, false);
        startActivity(new Intent(this, Login.class)); */
        return null;

    }
}
