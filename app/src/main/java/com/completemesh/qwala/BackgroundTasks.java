package com.completemesh.qwala;

/**
 * Created by Mohit on 11/24/2017.
 */
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Mohit on 11/6/2017.
 */

public class BackgroundTasks extends AsyncTask<String ,Void, String>{
    String Result;
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        String query = arg0[0];
        String link = arg0[1];
        Result = link(query, link);
        return Result;
    }
    // This method is used to establish a connection to PHP file.
    // SQL query and link to PHP file is passed as parameter.
    public String link(String query, String link) {
        try {

            URL url = new URL(link);


            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data = URLEncoder.encode("query", "UTF-8") + "=" +URLEncoder.encode(query, "UTF-8");
            //wr.write(data);

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            //wr.flush();
            String line="";

            String result="";
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            //BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                //result+=line;
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();



        } catch (Exception e) {
            e.printStackTrace();
        }
        // return sb.toString();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.v("data",Result);
    }

    public String insertuser(String Result){
        JSONObject json_data;

        String msg="";
        try{
            json_data=new JSONObject(Result);
            int code = (json_data.getInt("data"));
            Log.v("data",code + "");
            if(code==1){
                msg="User Register successfully";

            }else{
                msg="User Registration Failed..";

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;
    }



}
