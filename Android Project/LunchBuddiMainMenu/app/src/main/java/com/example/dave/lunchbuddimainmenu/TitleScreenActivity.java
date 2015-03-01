package com.example.dave.lunchbuddimainmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Dave on 3/1/2015.
 */
public class TitleScreenActivity extends Activity {
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        setContentView(R.layout.title_screen_activity_layout);
    };
    public void onPause()
        {
            super.onPause();
    };

    public void startMainMenuActivity(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

}
