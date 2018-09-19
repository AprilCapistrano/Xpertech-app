package com.company.xpertech.xpertech.Nav_Fragment.Troubleshoot_Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.company.xpertech.xpertech.Method.Troubleshoot;
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
import java.util.ArrayList;

public class TroubleshootFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    MyTroubleshootRecyclerViewAdapter mAdapter;

    ArrayList <String> troubleshootTitle;
    ArrayList <Troubleshoot> troubleshootList;

    View view = null;
    static String BOX_NUMBER_SESSION;

    RecyclerView recyclerView;

    public TroubleshootFragment() {
    }

    public static TroubleshootFragment newInstance(int columnCount) {
        TroubleshootFragment fragment = new TroubleshootFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         *  Access session record to get box id
         */
        SharedPreferences s = this.getActivity().getSharedPreferences("values", Context.MODE_PRIVATE);
        BOX_NUMBER_SESSION = s.getString("BOX_NUMBER_SESSION", "BOX_NUMBER_SESSION");

        String method = "troubleshoot";
        /**
         * Initiate MenuTask async task to query for the types of problems to be troubleshoot
         */
        MenuTask menuTask = new MenuTask(getContext());
        menuTask.execute(method, BOX_NUMBER_SESSION);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Troubleshoot");
        troubleshootTitle = new ArrayList<String>();
        troubleshootList = new ArrayList<Troubleshoot>();

         view = inflater.inflate(R.layout.fragment_troubleshoot_list, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        /**
         * Function for search to filter list of troubleshooting problems
         */
        EditText editText = (EditText) view.findViewById(R.id.search_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        return view;
    }

    public void filter(String text){
        ArrayList<Troubleshoot> filtered = new ArrayList<>();
        for(Troubleshoot item: troubleshootList){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filtered.add(item);
            }
        }

        mAdapter.filterList(filtered);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Troubleshoot item);
    }

    /**
     * Query for the list of troubleshooting problems
     * Post Execute calls MyTroubleshootRecyclerViewAdapter to display the data queried
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
            String troubleshoot_url = "https://uslsxpertech.000webhostapp.com/xpertech/troubleshoot.php";
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
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
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
            String[] title = result.split("\\$");
            for (int i = 0; i < title.length; i++) {
                troubleshootTitle.add(title[i]);
            }
            for (int i = 0; i < troubleshootTitle.size(); i++){
                Troubleshoot trbl = new Troubleshoot(troubleshootTitle.get(i));
                troubleshootList.add(trbl);
            }

            mAdapter = new MyTroubleshootRecyclerViewAdapter(troubleshootList,mListener);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
