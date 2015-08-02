package com.lunchbuddi.lunchbuddi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

// THIS CLASS IS ASSOCIATED WITH THE INBOX

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        ListView listView = (ListView) findViewById(R.id.list);

        // Create an empty array list of strings
        List<String> items = new ArrayList<String>();

        // Set the adapter
        MyCustomAdapter adapter = new MyCustomAdapter(items);  //ERROR?
        listView.setAdapter(adapter);

        // Set the emptyView to the ListView
        listView.setEmptyView(findViewById(R.id.empty));
    }
}