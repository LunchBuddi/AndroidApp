package com.lunchbuddi;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/*
    Author: David McKnight
    Date: 11 October 2015
 */

public class HomePage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
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

    //Methods to open other pages

    //Opens up the Settings page from the HomePage
    public void homePageToSettingsPage(View view)
    {
        Intent intent=new Intent(this, Settings.class);
        startActivity(intent);
    }

    //Opens up the Inbox page from the HomePage
    public void homePageToInboxPage(View view)
    {
        Intent intent=new Intent(this, Inbox.class);
        startActivity(intent);
    }

    //Opens up the Inbox page from the HomePage
    public void homePageToMapsActivity(View view)
    {
        Intent intent=new Intent(this, Maps.class);
        startActivity(intent);
    }
}
