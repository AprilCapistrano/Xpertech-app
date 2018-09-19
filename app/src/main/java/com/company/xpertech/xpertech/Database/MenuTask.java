package com.company.xpertech.xpertech.Database;

/**
 * Created by Skylar Gail on 8/19/2018.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

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
import java.util.ArrayList;

public class MenuTask extends AsyncTask<String,Void,String> {
    AlertDialog alertDialog;
    Context ctx;

    ArrayList<String> title_list;

    public interface Listener{
        public void response(ArrayList<String> title);
    }

    public MenuTask(Context ctx)
    {
        this.ctx =ctx;
    }

    @Override
    protected void onPreExecute() {
        title_list = new ArrayList<String>();
    }
    @Override
    protected String doInBackground(String... params) {
        String troubleshoot_url = "https://uslsxpertech.000webhostapp.com/xpertech/troubleshoot.php";
        String selfinstall_url = "https://uslsxpertech.000webhostapp.com/xpertech/selfinstall.php";
        String usermanual_url = "https://uslsxpertech.000webhostapp.com/xpertech/usermanual.php";
        String method = params[0];
        if(method.equals("troubleshoot")){
            String box_number = params[1];
            try {
                URL url = new URL(troubleshoot_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("box_number","UTF-8")+"="+URLEncoder.encode(box_number,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("selfinstall")){
            String box_number = params[1];
            try {
                URL url = new URL(selfinstall_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("box_number","UTF-8")+"="+URLEncoder.encode(box_number,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("usermanual")){
            String box_number = params[1];
            try {
                URL url = new URL(usermanual_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("box_number","UTF-8")+"="+URLEncoder.encode(box_number,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        String[] title = result.split("$");
        for (int i = 0; i < title.length; i++) {
            title_list.add(title[i]);
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("title_list", title_list);
    }
        /*Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("title_list", title_list);
        msg.setData(bundle);
        Handler handler = new Handler();
        handler.sendMessage(msg);*/


    public ArrayList<String> getArrayTitle() {
        return title_list;
    }
}