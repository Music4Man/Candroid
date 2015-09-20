package com.candroid.mischiefmanager.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.candroid.mischiefmanager.Journal;
import com.candroid.mischiefmanager.R;

public class ManageProfile extends AppCompatActivity implements View.OnClickListener{

    User user;
    UserLocalStore userLocalStore;
    Button updateButton;
    EditText editName, editNickName, editAge, editSurname, editEmail, editPassword,editConfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        userLocalStore = new UserLocalStore(this);

        editNickName = (EditText) findViewById(R.id.editNickname);
        editName = (EditText) findViewById(R.id.editName);
        editAge = (EditText) findViewById(R.id.editAge);
        editSurname = (EditText) findViewById(R.id.editSurname);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfPassword = (EditText) findViewById(R.id.editConfPassword);
        updateButton = (Button) findViewById(R.id.updateProfile);

        updateButton.setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(userLocalStore.getLoggedIn()){
            user = userLocalStore.getLoggedInUser();
            editNickName.setText(user.nickname);
            editName.setText(user.name);
            editAge.setText(""+user.age);
            editSurname.setText(user.surname);
            editEmail.setText(user.email);
            editPassword.setText(user.password);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(userLocalStore.getLoggedIn()){
            menu.findItem(R.id.action_logout).setVisible(true);
            menu.findItem(R.id.action_manage_profile).setVisible(true);
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_profile, menu);
        return true;
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
                startActivity(new Intent(ManageProfile.this, Login.class));
                break;

            case R.id.action_manage_profile:
                startActivity(new Intent(ManageProfile.this, ManageProfile.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())){
            case R.id.updateProfile:

                String name = editName.getText().toString();
                String surname = editSurname.getText().toString();
                String tempAge = editAge.getText().toString();
                String nickname = editNickName.getText().toString();
                String email = editEmail.getText().toString();
                String confEmail = editConfPassword.getText().toString();
                String password = editPassword.getText().toString();
                int age = -1;

                if(!tempAge.isEmpty()){
                    age = Integer.parseInt(editAge.getText().toString());
                }

                if(!name.isEmpty() && !confEmail.isEmpty() && !surname.isEmpty() &&!tempAge.isEmpty() && !nickname.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if(age < 15) {
                        showErrorMessage("You must be at least 15 to use this app");
                    } else if(confEmail.equals(password)) {
                        if(isValidEmail(email)) {
                            User user = new User(name, age, nickname, surname, password, email);
                            updateUser(user);
                        } else {
                            showErrorMessage("Registration not complete");
                        }
                    } else {
                        showErrorMessage("Passwords don't match");
                    }
                } else {
                    showErrorMessage("Please fill in all boxes");
                }

                break;
        }
    }

    public final boolean isValidEmail(CharSequence target) {
        if(!TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches() == false) {
            showErrorMessage("Please enter a valid email address");
            return false;
        } else {
            return true;
        }
    }

    private void updateUser(User user) {

    }

    private void showErrorMessage(String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ManageProfile.this);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
