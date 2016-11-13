package com.example.codev.accelerometer.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.codev.accelerometer.Models.SharedPreferencesFile;
import com.example.codev.accelerometer.R;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    View v;
    EditText etIPAddress, etPortNumber;
    Button btnReset, btnSave;

    String defaultIPAddress="192.168.0.4";
    int defaultPortNumber=8000;

    SharedPreferencesFile file;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_settings, container, false);

        etIPAddress=(EditText)v.findViewById(R.id.etIPAddress);
        etPortNumber=(EditText)v.findViewById(R.id.etPortNumber);
        btnReset=(Button)v.findViewById(R.id.btn_reset_fragmentSettings);
        btnSave=(Button)v.findViewById(R.id.btn_save_fragmentSettings);

        btnReset.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        file=new SharedPreferencesFile();



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        //1-set the ip address and port number from shared preferences
        SharedPreferences prefss = getActivity().getSharedPreferences(file.getSpIpAddress(), getActivity().MODE_PRIVATE);
        String s1 = prefss.getString(file.getIpAddressString(), defaultIPAddress);

        SharedPreferences prefss2 = getActivity().getSharedPreferences(file.getSpPortNo(), getActivity().MODE_PRIVATE);
        int i1 = prefss.getInt(file.getPortNumberString(), defaultPortNumber);

        etIPAddress.setText(s1);
        etPortNumber.setText(String.valueOf(i1));

        //-1

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_reset_fragmentSettings:

                //replace both edit texts with default value
                etIPAddress.setText(defaultIPAddress);
                etPortNumber.setText(String.valueOf(defaultPortNumber));

                break;

            case R.id.btn_save_fragmentSettings:

                //save the new value to shared preferences
                String newIp=etIPAddress.getText().toString();
                int newPort=Integer.parseInt(etPortNumber.getText().toString());

                updateIpAddressAndPortNumberToSP(newIp, newPort);

                break;
        }

    }

    private void updateIpAddressAndPortNumberToSP(String ipaddress, int portNumber)
    {



        SharedPreferences.Editor editorr1 = getActivity().getSharedPreferences(file.getSpIpAddress(), getActivity().MODE_PRIVATE).edit();
        editorr1.putString(file.getIpAddressString(), ipaddress);
        editorr1.commit();

        SharedPreferences.Editor editorr2 = getActivity().getSharedPreferences(file.getSpPortNo(), getActivity().MODE_PRIVATE).edit();
        editorr2.putInt(file.getPortNumberString(), portNumber);
        editorr2.commit();



        Toast.makeText(getActivity(), "New values updated", Toast.LENGTH_SHORT).show();
    }
}
