package com.company.xpertech.xpertech.Nav_Fragment.Self_Install_Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.company.xpertech.xpertech.Nav_Fragment.Troubleshoot_Fragment.TroubleeshootItemFragment;
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


public class SelfInstallFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View view;
    private ListView listView;
    private Context ctx;
    private FragmentActivity ft;
    String[] install;
    ArrayAdapter<String> listViewAdapter;

    private OnFragmentInteractionListener mListener;

    public SelfInstallFragment() {
    }

    public static SelfInstallFragment newInstance(String param1, String param2) {
        SelfInstallFragment fragment = new SelfInstallFragment();
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

        /**
         *  get the box id from the session
         */
        SharedPreferences s = this.getActivity().getSharedPreferences("values", Context.MODE_PRIVATE);
        String BOX_NUMBER_SESSION = s.getString("BOX_NUMBER_SESSION", "BOX_NUMBER_SESSION");
        String method = "selfinstall";

        BOX_NUMBER_SESSION = BOX_NUMBER_SESSION.replaceAll("\\s", "");

        /**
         *  Initiate the query for the displaying of the list of self install function
         */
        SelfInstallFragment.MenuTask menuTask = new SelfInstallFragment.MenuTask(getContext());
        menuTask.execute(method, BOX_NUMBER_SESSION);

        getActivity().setTitle("Self Install");
        view = inflater.inflate(R.layout.fragment_self_install, container, false);

        return view;
    }

    /**
     *  Called in Menu task async task's post execute to display the list of function after query
     */
    void display(){
        listView = (ListView) view.findViewById(R.id.listview_manual);

        if(install.length == 0)
            install = new String[]{"Unpacking"};

        listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                install
        );

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ft = (FragmentActivity) ctx;
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                Sub_Install_Fragment sif = new Sub_Install_Fragment();
                sif.setArguments(bundle);

                ft.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, sif).addToBackStack("tag").commit();
            }
        });
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
     *  Query for the Self install function list
     */
    public class MenuTask extends AsyncTask<String,Void,String> {
        Context ctx;
        AlertDialog alertDialog;

        public MenuTask(Context ctx)
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
            String selfinstall_url = "https://uslsxpertech.000webhostapp.com/xpertech/selfinstall.php";
            String method = params[0];
            if(method.equals("selfinstall")){
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
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    String response = "";
                    String line = "";
                    line = bufferedReader.readLine();
                    String[] title = line.split("\\$");
                    install = new String[title.length];
                    install = title;
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
            display();
        }
    }
}
