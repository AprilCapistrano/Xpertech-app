package com.company.xpertech.xpertech.Nav_Fragment.Remote_Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.company.xpertech.xpertech.R;

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

public class RemoteListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View view;
    private ListView listView;
     Context ctx;
    String[] instruct;

    private OnFragmentInteractionListener mListener;

    public RemoteListFragment() {
    }

        public static RemoteListFragment newInstance(String param1, String param2) {
        RemoteListFragment fragment = new RemoteListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Remote");
        view = inflater.inflate(R.layout.fragment_remote_list, container, false);

        /**
         *  Initiate the query for the remote function
         */
        RemoteListFragment.RemoteTask remoteTask = new RemoteListFragment.RemoteTask(getContext());
        remoteTask.execute("remote");

        /**
         * On Click function for the button to initiate the remote item fragment
         */
        final FragmentActivity ft = (FragmentActivity) ctx;
        Button btn_remote = (Button) view.findViewById(R.id.btn_remote);
        btn_remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new RemoteItemFragment()).addToBackStack("remote").commit();
            }
        });
        return view;
    }

    /**
     *  Array adapter is used to display data in array list
     *  Data refers to the functionality of the remote that was queried
     *  Called in Remote Task async task's post execute
     */
    void display(){
        listView = (ListView) view.findViewById(R.id.remote_list);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                instruct
        );

        listView.setAdapter(listViewAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ctx = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Query for the remote function
     */
    public class RemoteTask extends AsyncTask<String,Void,String> {
        Context ctx;
        AlertDialog alertDialog;

        public RemoteTask(Context ctx)
        {
            this.ctx =ctx;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("");
        }
        @Override
        protected String doInBackground(String... params) {
            String remote_url = "https://uslsxpertech.000webhostapp.com/xpertech/remote.php";
            String method = params[0];
            if(method.equals("remote")){
                try {
                    URL url = new URL(remote_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("box_number","UTF-8")+"="+URLEncoder.encode("default","UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    String line = "";
                    line = bufferedReader.readLine();
                    String[] step = line.split("\\$");
                    instruct = new String[step.length];
                    for(int i = 0; i < step.length; i++){
                        instruct[i] = (i+1) + ".) " + step[i];
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
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
            display();
        }
    }
}
