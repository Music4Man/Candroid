package com.candroid.mischiefmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.candroid.mischiefmanager.Login.Login;
import com.candroid.mischiefmanager.Login.ManageProfile;
import com.candroid.mischiefmanager.Login.UserLocalStore;

public class Journal extends AppCompatActivity {

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(userLocalStore.getLoggedIn()){
            menu.findItem(R.id.action_logout).setVisible(true);
            menu.findItem(R.id.action_manage_profile).setVisible(true);
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_manage_profile).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                return true;

            case R.id.action_logout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(Journal.this, Login.class));
                break;

            case R.id.action_manage_profile:
                startActivity(new Intent(Journal.this, ManageProfile.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
