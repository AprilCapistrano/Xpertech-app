package com.company.xpertech.xpertech.Nav_Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.xpertech.xpertech.Nav_Fragment.Troubleshoot_Fragment.TroubleeshootItemFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Troubleshoot_Fragment.Troubleshoot;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FeedbackFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    EditText email_subject;
    EditText email_msg;
    String USER_SESSION;

    private OnFragmentInteractionListener mListener;

    public FeedbackFragment() {

    }

    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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
        final View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        getActivity().setTitle("Feedback");

        SharedPreferences s = this.getActivity().getSharedPreferences("values", Context.MODE_PRIVATE);
        USER_SESSION = s.getString("USER_SESSION", "USER_SESSION").replaceAll("\\s+","");

        email_subject = (EditText) view.findViewById(R.id.email_subject);
        email_msg = (EditText) view.findViewById(R.id.email_msg);

        FeedbackFragment.FeedbackTask task = new FeedbackFragment.FeedbackTask((FragmentActivity) getContext());
        task.execute("send feedback");

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        void onFragmentInteraction(Uri uri);
    }

    public class FeedbackTask extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;

        public FeedbackTask(FragmentActivity activity)
        {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sending");
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy,HH:mm");
            String stamp = format.format(c.getTime());

            String feedback_url = "https://uslsxpertech.000webhostapp.com/xpertech/feedback.php";
            String method = params[0];
            String feedback_message = email_msg.getText().toString();
            String feedback_title = email_subject.getText().toString();
            String ownership = USER_SESSION;
            String feedback_date = stamp.split(",")[0];
            String feedback_time = stamp.split(",")[1];
            if(method.equals("send feedback")){
                try {
                    URL url = new URL(feedback_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("feedback_title","UTF-8")+"="+URLEncoder.encode(feedback_title,"UTF-8");
                    data += "&" + URLEncoder.encode("feedback_message","UTF-8")+"="+URLEncoder.encode(feedback_message,"UTF-8");
                    data += "&" + URLEncoder.encode("ownership","UTF-8")+"="+URLEncoder.encode(ownership,"UTF-8");
                    data += "&" + URLEncoder.encode("feedback_time","UTF-8")+"="+URLEncoder.encode(feedback_time,"UTF-8");
                    data += "&" + URLEncoder.encode("feedback_date","UTF-8")+"="+URLEncoder.encode(feedback_date,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                    String line = "";
                    line = bufferedReader.readLine();

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    Toast.makeText(getContext(), "Feedback sending successful", Toast.LENGTH_SHORT).show();
                    return line;

                } catch (MalformedURLException e) {
                    Toast.makeText(getContext(), "Feedback sending failed. Please try again.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Feedback sending failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            return "";
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            email_subject.setText("");
            email_msg.setText("");
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
}
