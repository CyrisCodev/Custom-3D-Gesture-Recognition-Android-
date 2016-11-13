package com.example.codev.accelerometer.Models;

/**
 * Created by Codev on 11/13/2016.
 */
public class SharedPreferencesFile {

    public static final String SP_IP_ADDRESS="sharedpreference_ipaddress";
    public static final String SP_PORT_NO="sharedpreference_portnumber";

    public static final String IP_ADDRESS_STRING="string_ipaddress";
    public static final String PORT_NUMBER_STRING="string_portnumber";

    public static String getSpIpAddress() {
        return SP_IP_ADDRESS;
    }

    public static String getSpPortNo() {
        return SP_PORT_NO;
    }

    public static String getIpAddressString() {
        return IP_ADDRESS_STRING;
    }

    public static String getPortNumberString() {
        return PORT_NUMBER_STRING;
    }
}
