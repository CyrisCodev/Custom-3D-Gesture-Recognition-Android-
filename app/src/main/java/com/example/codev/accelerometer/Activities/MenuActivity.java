package com.example.codev.accelerometer.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.codev.accelerometer.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout llPeformGesture, llSettings, llAppDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        llPeformGesture=(LinearLayout)findViewById(R.id.ll_performGesture_menuActivity);
        llSettings=(LinearLayout)findViewById(R.id.ll_settings_menuActivity);
        llAppDetails=(LinearLayout)findViewById(R.id.ll_appDetails_menuActivity);

        llPeformGesture.setOnClickListener(this);
        llSettings.setOnClickListener(this);
        llAppDetails.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ll_performGesture_menuActivity:

                Toast.makeText(this, "1 clicked", Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_settings_menuActivity:

                Toast.makeText(this, "2 clicked", Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_appDetails_menuActivity:

                Toast.makeText(this, "3 clicked", Toast.LENGTH_SHORT).show();

                break;
        }

    }
}
