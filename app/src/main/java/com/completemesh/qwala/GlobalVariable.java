package com.completemesh.qwala;

import android.app.Application;


/**
 * Created by Mohit on 11/6/2017.
 */

public class GlobalVariable extends Application {
    // String host="10.0.2.2";
  String host="mypatshala.com";
    //http://mypatshala.com/qwala/myconnection.php


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
