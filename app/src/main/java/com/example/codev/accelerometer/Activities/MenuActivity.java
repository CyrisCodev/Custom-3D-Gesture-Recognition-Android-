package com.example.codev.accelerometer.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.codev.accelerometer.Fragments.PerformGestureFragment;
import com.example.codev.accelerometer.Fragments.SettingsFragment;
import com.example.codev.accelerometer.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout llPeformGesture, llSettings, llAppDetails;
    PerformGestureFragment performGestureFragment;
    SettingsFragment settingsFragment;

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

        performGestureFragment=new PerformGestureFragment();
        settingsFragment=new SettingsFragment();

        //intially replace the frame layout with detaulf i.e perform gesture fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_replacablePart_menuActivity, performGestureFragment).commit();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ll_performGesture_menuActivity:

                getSupportFragmentManager().beginTransaction().replace(R.id.fl_replacablePart_menuActivity, performGestureFragment).commit();

                break;

            case R.id.ll_settings_menuActivity:

                getSupportFragmentManager().beginTransaction().replace(R.id.fl_replacablePart_menuActivity, settingsFragment).commit();

                break;

            case R.id.ll_appDetails_menuActivity:

                Toast.makeText(this, "3 clicked", Toast.LENGTH_SHORT).show();

                break;
        }

    }
}
