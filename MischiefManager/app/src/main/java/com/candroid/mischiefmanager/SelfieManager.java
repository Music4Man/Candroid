package com.candroid.mischiefmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SelfieManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_manager);
        addButtonListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selfie_manager, menu);
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
    public void addButtonListener()
    {
        Button tD = (Button) findViewById(R.id.toDo);

        tD.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(SelfieManager.this, ToDo.class));
            }
        });
        Button J = (Button) findViewById(R.id.journal);

        J.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(SelfieManager.this, Journal.class));
            }
        });
}}
