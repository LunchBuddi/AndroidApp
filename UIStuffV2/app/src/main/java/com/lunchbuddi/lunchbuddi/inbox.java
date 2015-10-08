package com.lunchbuddi.lunchbuddi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

// THIS CLASS IS ASSOCIATED WITH THE INBOX

public class inbox extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        ListView listView = (ListView) findViewById(R.id.listViewForInbox);

        // Create an empty array list of strings
        ArrayList<String> items = new ArrayList<String>();

        // Set the adapter
        InboxAdapter adapter = new InboxAdapter(this, items);
        listView.setAdapter(adapter);

        // Set the emptyView to the ListView
        listView.setEmptyView(findViewById(R.id.empty));
    }
}