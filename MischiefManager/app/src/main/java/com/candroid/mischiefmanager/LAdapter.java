package com.candroid.mischiefmanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Firdous on 2015-09-22.
 */
public class LAdapter extends BaseAdapter
{
    private ArrayList complete;
    private Activity act;

    public LAdapter(Activity a, ArrayList l)
    {
        this.complete = l;
        this.act = a;
    }

    @Override
    public int getCount()
    {
        return complete.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v;
        LayoutInflater inflater = (LayoutInflater) act.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.task_view, null);

        Button delete = (Button) v.findViewById(R.id.doneButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //your ON CLICK CODE

            }


        });
        return v;
    }
}
