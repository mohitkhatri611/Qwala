package com.completemesh.qwala;

import android.app.Application;


/**
 * Created by Mohit on 11/6/2017.
 */

public class GlobalVariable extends Application {
     String localHostip="192.168.43.123:21567";
     String host="mypatshala.com";
     String networkState="";
    public String getLocalHostip() {
        return localHostip;
    }

    public void setLocalHostip(String localHostip) {
        this.localHostip = localHostip;
    }

    public String getNetworkState() {
        return networkState;
    }

    public void setNetworkState(String networkState) {
        this.networkState = networkState;
    }

    //http://mypatshala.com/qwala/myconnection.php


    public String getHost() {
        return host;
    }


    public void setHost(String host) {
        this.host = host;
    }
}
