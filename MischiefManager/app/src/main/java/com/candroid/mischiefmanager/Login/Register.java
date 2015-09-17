package com.candroid.mischiefmanager.Login;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.candroid.mischiefmanager.Journal;
import com.candroid.mischiefmanager.R;

public class Register extends AppCompatActivity implements View.OnClickListener{

    Button registerButton;
    EditText editName, editNickName, editAge, editSurname, editEmail, editPassword;
    CheckBox agreeCheckBox;

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
        registerButton = (Button) findViewById(R.id.register);
        agreeCheckBox = (CheckBox) findViewById(R.id.LicenceAgreement);

        registerButton.setOnClickListener(this);

        registerButton.setEnabled(false);

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
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
                String password = editPassword.getText().toString();
                int age = -1;

                if(!tempAge.isEmpty()){
                    age = Integer.parseInt(editAge.getText().toString());
                }
                if(!name.isEmpty() && !surname.isEmpty() &&!tempAge.isEmpty() && !nickname.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    User user = new User(name, age, nickname, surname, password, email);
                    registerUser(user);
                } else {
                    showErrorMessage();
                }

                break;
        }
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
        dialogBuilder.setMessage("Please fill in all the boxes");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void registerUser(User user){
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.storeUserDataToServer(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(Register.this, Journal.class));
            }
        });
    }
}
