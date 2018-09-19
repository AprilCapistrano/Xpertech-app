package com.company.xpertech.xpertech.Nav_Fragment.Self_Install_Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.company.xpertech.xpertech.Method.InstallAdapter;
import com.company.xpertech.xpertech.Method.Sub_Manual;

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

import static com.company.xpertech.xpertech.R.id;
import static com.company.xpertech.xpertech.R.layout;

public class Sub_Install_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static int position = 0;
    private Context ctx;

    TextView textView;
    ArrayList<Sub_Manual> subManual;
    String title;
    ListView listView;

    private OnFragmentInteractionListener mListener;

    public Sub_Install_Fragment() {
        // Required empty public constructor
    }

    public static Sub_Install_Fragment newInstance(String param1, String param2) {
        Sub_Install_Fragment fragment = new Sub_Install_Fragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_sub__install_, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences s = this.getActivity().getSharedPreferences("values", Context.MODE_PRIVATE);
        String BOX_NUMBER_SESSION = s.getString("BOX_NUMBER_SESSION", "BOX_NUMBER_SESSION");

        Bundle bundle = getArguments();
        position = bundle.getInt("position")+1;
        subManual = new ArrayList<Sub_Manual>();
        listView = (ListView) view.findViewById(id.sub_install_list);
        textView = (TextView) view.findViewById(id.sub_install_name);

        /**
         *  Initiate async task SubInstallTask to query for detail info of the selected data from SelfIntallFragment
         */
        Sub_Install_Fragment.SubInstallTask sit = new Sub_Install_Fragment.SubInstallTask(getContext());
        sit.execute("selfinstall_steps", position+"", BOX_NUMBER_SESSION);
    }

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
     * Query for Detailed information based on the selected item in SubIntallFragment
     */
    public class SubInstallTask extends AsyncTask<String,Void,String> {
        Context ctx;
        AlertDialog alertDialog;

        public SubInstallTask(Context ctx)
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
            String install_url = "https://uslsxpertech.000webhostapp.com/xpertech/selfinstall_steps.php";
            String title_url = "https://uslsxpertech.000webhostapp.com/xpertech/selfinstall_title.php";
            String img_url = "https://uslsxpertech.000webhostapp.com/xpertech/selfinstall_image.php";
            String method = params[0];
            if(method.equals("selfinstall_steps")){
                String selfinstall_id = params[1];
                String box_id = params[2];
                try {
                    //Retreiving Steps
                    URL url = new URL(install_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("selfinstall_id","UTF-8")+"="+URLEncoder.encode(selfinstall_id,"UTF-8");
                    data += "&" + URLEncoder.encode("box_id","UTF-8")+"="+URLEncoder.encode(box_id,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    String step_line = "";
                    step_line = bufferedReader.readLine();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    //Retreiving Images
                    url = new URL(img_url);
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    outputStream = httpURLConnection.getOutputStream();
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    data = URLEncoder.encode("selfinstall_id","UTF-8")+"="+URLEncoder.encode(selfinstall_id,"UTF-8");
                    data += "&" + URLEncoder.encode("box_id","UTF-8")+"="+URLEncoder.encode(box_id,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String image_line = "";
                    image_line = bufferedReader.readLine();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    String[] step = step_line.split("\\$");
                    String[] image = image_line.split("\\$");
                    for (int i = 0; i < step.length; i++) {
                        int img = 0;
                        Log.d("IMG",""+image[i]);
                        if (image[i] != "0") {
                            img = getResources().getIdentifier(image[i].replaceAll("\\s+",""), "drawable", ctx.getPackageName());
                        }
                        subManual.add(new Sub_Manual("\t"+(i+1)+".) "+step[i], img));
                    }

                    //Retreiving title
                    url = new URL(title_url);
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    outputStream = httpURLConnection.getOutputStream();
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    data = URLEncoder.encode("selfinstall_id","UTF-8")+"="+URLEncoder.encode(selfinstall_id,"UTF-8");
                    data += "&" + URLEncoder.encode("box_id","UTF-8")+"="+URLEncoder.encode(box_id,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String title_line = "";
                    title_line = bufferedReader.readLine();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    title = title_line;

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
            InstallAdapter installAdapter = new InstallAdapter(subManual, ctx);
            listView.setAdapter(installAdapter);
            if(title != null & !title.contains("No title found.")) {
                textView.setText(title);
            }
        }
    }
}
