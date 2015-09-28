package com.candroid.mischiefmanager.Menu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.candroid.mischiefmanager.Login.GetUserCallBack;
import com.candroid.mischiefmanager.Login.ServerRequests;
import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;

import java.util.ArrayList;

/**
 * Created by Elzahn on 2015-09-21.
 */
public class ManageProfile_Fragment extends Fragment implements View.OnClickListener{
    View rootview;
    User loggedInUser;
    UserLocalStore userLocalStore;
    Button updateButton;
    EditText editName, editNickName, editAge, editSurname, editEmail, editPassword,editConfPassword;
    String originalNickName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_manage_profile, container, false);

        setUpProfile();

        return rootview;
    }

    public void setUpProfile(){
        userLocalStore = new UserLocalStore(getContext());

        editNickName = (EditText) rootview.findViewById(R.id.editNickname);
        editName = (EditText) rootview.findViewById(R.id.editName);
        editAge = (EditText) rootview.findViewById(R.id.editAge);
        editSurname = (EditText) rootview.findViewById(R.id.editSurname);
        editEmail = (EditText) rootview.findViewById(R.id.editEmail);
        editPassword = (EditText) rootview.findViewById(R.id.editPassword);
        editConfPassword = (EditText) rootview.findViewById(R.id.editConfPassword);
        updateButton = (Button) rootview.findViewById(R.id.updateProfile);

        updateButton.setOnClickListener(this);

        if(userLocalStore.getLoggedIn()){
            loggedInUser = userLocalStore.getLoggedInUser();
            ArrayList<String> userDetails = loggedInUser.getUserDetails();

            originalNickName = userDetails.get(3);

            editNickName.setText(userDetails.get(3));
            editName.setText(userDetails.get(0));
            editAge.setText(""+userDetails.get(2));
            editSurname.setText(userDetails.get(1));
            editEmail.setText(userDetails.get(4));
            editPassword.setText(userDetails.get(5));
        }
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
                            updateUser(user, originalNickName);
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

    private void updateUser(User user, String originalNickName) {
        ServerRequests serverRequest = new ServerRequests(getContext());
        serverRequest.updateUserData(user, originalNickName, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage("Could not update your profile");
                } else {
                    ArrayList<String> userDetails = returnedUser.getUserDetails();
                    editNickName.setText(userDetails.get(3));
                    editName.setText(userDetails.get(0));
                    editAge.setText("" + userDetails.get(2));
                    editSurname.setText(userDetails.get(1));
                    editEmail.setText(userDetails.get(4));
                    editPassword.setText(userDetails.get(5));
                }
            }
        });
    }

    private void showErrorMessage(String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
