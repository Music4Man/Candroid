package com.candroid.mischiefmanager.Login;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.candroid.mischiefmanager.Journal;
import com.candroid.mischiefmanager.Nav;
import com.candroid.mischiefmanager.R;
import com.candroid.mischiefmanager.ToDo;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button loginButton, skipLogin;
    EditText editNickname, editPassword;
    TextView registerLink;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editNickname = (EditText) findViewById(R.id.editNickname);
        editPassword = (EditText) findViewById(R.id.editPassword);
        loginButton = (Button) findViewById(R.id.login);
        skipLogin = (Button) findViewById(R.id.SkipLogin);
        registerLink = (TextView) findViewById(R.id.registerLink);

        loginButton.setOnClickListener(this);
        skipLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(authenticate()){
            startActivity(new Intent(this, Nav.class));
        } else {
            //startActivity(new Intent(this, Login.class));
        }
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
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
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
                startActivity(new Intent(Login.this, Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean authenticate() {
        return userLocalStore.getLoggedIn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                String nickname = editNickname.getText().toString();
                String password = editPassword.getText().toString();

                if(nickname.isEmpty() || password.isEmpty()){
                    showErrorMessage();
                } else if(!nickname.isEmpty() && !password.isEmpty()) {
                    User user = new User(nickname, password);
                    authenticate(user);
                }

                break;

            case R.id.registerLink:
                startActivity(new Intent(this, Register.class));
                break;

            case R.id.SkipLogin:
                startActivity(new Intent(this, Nav.class));
                break;
        }
    }

    private void authenticate(User user){
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataFromServer(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect login details");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, Nav.class));
    }
}
