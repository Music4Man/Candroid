package com.candroid.mischiefmanager.Login;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.candroid.mischiefmanager.Journal;
import com.candroid.mischiefmanager.R;

public class Register extends AppCompatActivity implements View.OnClickListener{

    Button registerButton;
    EditText editName, editNickName, editAge, editSurname, editEmail, editPassword,editConfPassword;
    CheckBox agreeCheckBox;
    UserLocalStore userLocalStore;
    TextView backToLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editNickName = (EditText) findViewById(R.id.editNickname);
        editName = (EditText) findViewById(R.id.editName);
        editAge = (EditText) findViewById(R.id.editAge);
        editSurname = (EditText) findViewById(R.id.editSurname);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfPassword = (EditText) findViewById(R.id.editConfPassword);
        registerButton = (Button) findViewById(R.id.register);
        agreeCheckBox = (CheckBox) findViewById(R.id.LicenceAgreement);
        backToLoginLink = (TextView) findViewById(R.id.backToLogin);

        registerButton.setOnClickListener(this);
        backToLoginLink.setOnClickListener(this);

        registerButton.setEnabled(false);

        userLocalStore = new UserLocalStore(this);

        agreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if (buttonView.isChecked()) {
                    registerButton.setEnabled(true);
                } else {
                    registerButton.setEnabled(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())){
            case R.id.register:

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
                                registerUser(user);
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

            case R.id.backToLogin:
                startActivity(new Intent(this, Login.class));
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

    private void showErrorMessage(String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void registerUser(User user){
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.storeUserDataToServer(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if(returnedUser == null){
                    showErrorMessage("Error registering your profile");
                } else {
                    startActivity(new Intent(Register.this, Login.class));
                }
            }
        });
    }
}
