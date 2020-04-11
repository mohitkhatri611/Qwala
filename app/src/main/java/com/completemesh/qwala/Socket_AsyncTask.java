package com.completemesh.qwala;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Mohit on 3/20/2018.
 */

public class Socket_AsyncTask extends AsyncTask<String,Void,Void>
{


    Socket socket;

    @Override
    protected Void doInBackground(String... arg0){
        try{
            String CMD = arg0[0];
            InetAddress inetAddress = InetAddress.getByName(MainActivity.wifiModuleIp);
            socket = new java.net.Socket(inetAddress,MainActivity.wifiModulePort);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeBytes(CMD);
            dataOutputStream.close();
            socket.close();
        }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
        return null;
    }
}
