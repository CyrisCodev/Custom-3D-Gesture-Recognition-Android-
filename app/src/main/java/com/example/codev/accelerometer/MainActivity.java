package com.example.codev.accelerometer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener{

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private static final int ALARM_STATE_DELAY = 1;
    private static int SPLASH_TIME_OUT = 1200;
    int DELAY_COUNT=1;

    private long lastUpdate = 0;
    private float last_x;
    private float last_y;

    int countReadings=0;
    long initialTime=0;


    private boolean continueIncrementing;
    private Runnable incrementerThread;
    boolean isThreadRunning=false;
    Thread time;

    FancyButton bsense;
    Button bSaveFile;
    TextView tvx, tvy, tvz, tvcount;
    EditText etFileName, etIPAddress, etPort;
    LinearLayout llSettings, llbuttons;
    ImageView ivUp,ivDown, ivLeft, ivRight;

    int delayTime=0;
    int useGravity=0;
    float gravityFloat=0;
    String logs="0.000 0.000 0.000";
    String last_sent_string="0";
    String sUp="Up", sDown="Down", sLeft="Left", sRight="Right";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senSensorManager = (SensorManager) getSystemService("sensor");
       senAccelerometer = senSensorManager.getDefaultSensor(ALARM_STATE_DELAY);
        senAccelerometer.getResolution();
        //senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        llSettings=(LinearLayout)findViewById(R.id.ll_settings);
        llbuttons=(LinearLayout)findViewById(R.id.ll_buttons);
        bsense=(FancyButton)findViewById(R.id.activity_main_bsense);
        bSaveFile=(Button)findViewById(R.id.activity_main_bsaveFile);
        tvx=(TextView)findViewById(R.id.activity_main_tv_x);
        tvy=(TextView)findViewById(R.id.activity_main_tv_y);
        tvz=(TextView)findViewById(R.id.activity_main_tv_z);
        tvcount=(TextView)findViewById(R.id.activity_main_tv_count);
        etIPAddress=(EditText)findViewById(R.id.activity_main_etIPAddress);
        etFileName=(EditText)findViewById(R.id.activity_main_etFileName);
        etPort=(EditText)findViewById(R.id.activity_main_etIPPort);
        ivUp=(ImageView)findViewById(R.id.iv_upbutton);
        ivDown=(ImageView)findViewById(R.id.iv_downbutton);
        ivRight=(ImageView)findViewById(R.id.iv_rightbutton);
        ivLeft=(ImageView)findViewById(R.id.iv_leftbutton);

        initializeViews();

        bSaveFile.setOnClickListener(this);
        ivUp.setOnClickListener(this);
        ivDown.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        bsense.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(DELAY_COUNT==1) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.i("button", "pressed");
                            //startIncrmenting();
                            countReadings = 0;
                            registersensor();
                            logs = "0.0000 0.0000 0.0000";
                            break;

                        case MotionEvent.ACTION_UP:
                            Log.i("sentreadingssss", logs);

                            if (countReadings < 100) {
                                Toast.makeText(getApplicationContext(), "very small gesture, TRY AGAIN", Toast.LENGTH_SHORT).show();
                            }

                            initialTime = 0;
                            addDelayOnButtonClick();
                            unregistersensor();
                            if(logs!=last_sent_string)
                            {
                                sendReadingsToPC(logs);
                                last_sent_string=logs;
                            }
                            //stopIncrmenting();
                    }
                }
                return true;



            }
        });

    }

    private void sendReadingsToPC(final String lg) {

        Thread t=new Thread()
        {

            @Override
            public void run() {

                try {

                    Socket s=new Socket(etIPAddress.getText().toString(), 8000);
                    DataOutputStream dos=new DataOutputStream(s.getOutputStream());

                    Log.i("sending", lg);

                    dos.writeUTF(lg);
                    dos.close();
                    s.close();

                }catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        };
        t.start();
        Toast.makeText(getApplicationContext(), "This message sent", Toast.LENGTH_SHORT).show();


    }


    private void initializeViews()
    {
        countReadings=0;
        tvx.setText("0");
        tvy.setText("0");
        tvz.setText("0");
        tvcount.setText("0");
        delayTime=0;
        etPort.setText("8000");
        etIPAddress.setText("192.168.0.5");
    }

    private void registersensor()
    {
       // senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregistersensor()
    {
        senSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.activity_main_menu_reset) {
            initializeViews();
            initialTime=0;
            unregistersensor();
            return true;
        }
        if (id == R.id.activity_main_menu_settings) {
            if(llSettings.getVisibility()==View.VISIBLE)
            {
                llSettings.setVisibility(View.GONE);
                llbuttons.setVisibility(View.VISIBLE);
            }
            else
            {
                llSettings.setVisibility(View.VISIBLE);
                llbuttons.setVisibility(View.GONE);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
float x, y, z, gx=0, gy=0, gz=0;
    float prevX=0, prevY=0, prevZ=0;
    float calX=0, calY=0, calZ=0;
    float nx=0, ny=0, nz=0;

    float squarex=0, squarey=0, squarez=0, resultantmod=0;

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
//            if(useGravity==1)
//            {

//            }
//            else
//            {
//                //we need not to use gravity
//                //only linear acceleration is to be used
//
//
//                 //In this example, alpha is calculated as t / (t + dT),
//                 //where t is the low-pass filter's time-constant and
//                 //dT is the event delivery rate.
//
////                final float alpha = 0.6f;
////
////               // gravityFloat=Float.parseFloat(etFloatGravity.getText().toString());
////
////               // final float alpha = gravityFloat;
////                // Isolate the force of gravity with the low-pass filter.
////                gx = alpha * gx + (1 - alpha) * event.values[0];
////                gy = alpha * gy + (1 - alpha) * event.values[1];
////                gz = alpha * gz + (1 - alpha) * event.values[2];
////
////                // Remove the gravity contribution with the high-pass filter.
////
////                x= event.values[0] - gx;
////                y = event.values[1] - gy;
////                z = event.values[2] - gz;
//            }

                 x = event.values[0];
                 y = event.values[1];
                 z = event.values[2];


          //  x=x*10f;
          // y=y*10f;
          //  z=z*10f;

            String sx;
                    //= String.valueOf(x);

            String sy;
                    //= String.valueOf(y);

            String sz;
                    //= String.valueOf(z);


            long curTime = System.currentTimeMillis();

            if (initialTime == 0) {
                initialTime = curTime;
               // prevX=x;
              //  prevY=y;
               // prevZ=z;
                prevX=-0.126f;
                prevY=-0.035f;
                prevZ=-0.323f;
              //  Log.i("gravityy", "called");
            }

         //   if (curTime-initialTime<1000) {

                if ((curTime - lastUpdate) > delayTime) {

                   // Log.i("delayyy", String.valueOf(delayTime));

                //    Log.i("accelerationss " + String.valueOf(countReadings), sx + " " + sy + " " + sz);

                   // logs=logs+"\n"+sx+" "+sy+" "+sz;
                  //  Log.i("valuesss","\n"+sx+" "+sy+" "+sz);
//
                    prevX=prevX*0.9f+x*0.1f;
                    prevY=prevY*0.9f+y*0.1f;
                    prevZ=prevZ*0.9f+z*0.1f;
//
              //      Log.i("gravityy", String.valueOf(prevX)+" "+String.valueOf(prevY)+" "+String.valueOf(prevZ));

                    calX=x-prevX;
                    calY=y-prevY;
                    calZ=z-prevZ;
//
//

                    DecimalFormat decimalFormat = new DecimalFormat("0.000");
                    decimalFormat.setMaximumFractionDigits(3);
//                    //this.xSI.setText("x: " + decimalFormat.format((double)calX));
//
                    sx=decimalFormat.format((double)calX);
                    sy=decimalFormat.format((double)calY);
                    sz=decimalFormat.format((double)calZ);
//
                    nx=calX;
                    ny=calY;
                    nz=calZ;

                    nx = (float) (((double) ((int) (((double)nx) * 1000.0d))) / 1000.0d);
                    ny = (float) (((double) ((int) (((double)ny) * 1000.0d))) / 1000.0d);
                    nz = (float) (((double) ((int) (((double)nz) * 1000.0d))) / 1000.0d);

//
                   // Log.i("accelerationss " + String.valueOf(countReadings), sx + " " + sy + " " + sz);
                   // Log.i("accelerationss ", String.valueOf(nx)+" "+String.valueOf(ny)+" "+String.valueOf(nz));

//                    //logs=logs+"\n"+String.valueOf(calX)+" "+String.valueOf(calY)+" "+String.valueOf(calZ);
//
                    //logs=logs+"\n"+sx+" "+sy+" "+sz;
                    logs=logs+"\n"+String.valueOf(nx)+" "+String.valueOf(ny)+" "+String.valueOf(nz);
                  //  Log.i("accelerationssi" + String.valueOf(countReadings), sx + " " + sy + " " + sz);

                    squarex=nx*nx;
                    squarey=ny*ny;
                    squarez=nz*nz;

                    resultantmod= (float) Math.sqrt(squarex+squarey+squarez);

                    Log.i("accelerationssi",String.valueOf(resultantmod));

                    lastUpdate = curTime;
                    tvx.setText(sx);
                    tvy.setText(sy);
                    tvz.setText(sz);
                    tvcount.setText(String.valueOf(countReadings));
                    countReadings++;

                }

          //
          // }
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {



        switch (v.getId())
        {
            case R.id.iv_upbutton:

               Log.i("checkinngclicks", "upbutton");
                sendReadingsToPC("up up");

                break;
            case R.id.iv_downbutton:

                Log.i("checkinngclicks", "Downbutton");
                sendReadingsToPC("down down");

                break;
            case R.id.iv_rightbutton:

                Log.i("checkinngclicks", "rightbtn");
                sendReadingsToPC("right right");

                break;
            case R.id.iv_leftbutton:

                Log.i("checkinngclicks", "leftbutton");
                sendReadingsToPC("left left");

                break;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
       // bsense.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // bsense.setEnabled(true);
    }

    public void addDelayOnButtonClick()
    {
        //bsense.setEnabled(false);
        DELAY_COUNT=0;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                DELAY_COUNT=1;
               //bsense.setEnabled(true);

            }
        }, SPLASH_TIME_OUT);
    }

    public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "3DGestures");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }

}
