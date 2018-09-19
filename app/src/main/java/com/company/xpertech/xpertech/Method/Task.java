package com.company.xpertech.xpertech.Method;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Skylar Gail on 8/30/2018.
 */

public class Task extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        String urls = "https://uslsxpertech.000webhostapp.com/xpertech/statistic.php";
        String method = params[0];
        if (method.equals("stat")) {
            String type = params[1];
            String status = params[2];
            String ownership = params[3];
            String stat_desc = params[4];
            try {
                URL url = new URL(urls);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
                data += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");
                data += "&" + URLEncoder.encode("ownership", "UTF-8") + "=" + URLEncoder.encode(ownership, "UTF-8");
                data += "&" + URLEncoder.encode("stat_desc", "UTF-8") + "=" + URLEncoder.encode(stat_desc, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                line = bufferedReader.readLine();
                Log.d("Result", line);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("TASK ERR", String.valueOf(e));
            }
        }
        return null;
    }
}
