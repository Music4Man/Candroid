package com.candroid.mischiefmanager.Menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.candroid.mischiefmanager.Login.User;
import com.candroid.mischiefmanager.Login.UserLocalStore;
import com.candroid.mischiefmanager.R;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Frank on 2015-09-20.
 */
public class Callendar_Fragment extends Fragment{
    View rootview;
    CalendarView Cview;
    long date;
    String dateString;
    UserLocalStore current;
    User loggedInUser;
    String userDetails;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_callendar, container, false);
        Cview = (CalendarView) rootview.findViewById(R.id.calendarView);
        current = new UserLocalStore(getContext());

        loggedInUser = current.getLoggedInUser();
        userDetails = loggedInUser.getUserDetails().get(3);

    Cview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            date = Cview.getDate();
            if (month<10 && dayOfMonth<10)
            {
                dateString = String.valueOf(year)+ "-0" +String.valueOf(month) + "-0" + String.valueOf(dayOfMonth);
            }
            else
            if (month<10)
            {
                dateString = String.valueOf(year)+ "-0" +String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }
            else
            if (dayOfMonth<10)
            {
                dateString = String.valueOf(year)+ "-" +String.valueOf(month) + "-0" + String.valueOf(dayOfMonth);
            }
            else
            {

                dateString = String.valueOf(year)+ "-" +String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }
            Toast.makeText(view.getContext(),userDetails + dateString, Toast.LENGTH_SHORT).show();
            //showToDo(userDetails, dateString);
        }
    });
        return rootview;
    }
}
