package com.company.xpertech.xpertech.Nav_Fragment.Manual_Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.company.xpertech.xpertech.R;
import com.github.barteksc.pdfviewer.PDFView;

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


public class ManualListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    View view;
    Context ctx;

    private OnFragmentInteractionListener mListener;

    public ManualListFragment() {
        // Required empty public constructor
    }

    public static ManualListFragment newInstance(String param1, String param2) {
        ManualListFragment fragment = new ManualListFragment();
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
        getActivity().setTitle("Manual");

        view = inflater.inflate(R.layout.fragment_manual_list, container, false);

        /**
         *  Access the data session for box number that is required to be able to display the manual for the certain box
         */
        SharedPreferences s = this.getActivity().getSharedPreferences("values", Context.MODE_PRIVATE);
        String BOX_NUMBER_SESSION = s.getString("BOX_NUMBER_SESSION", "BOX_NUMBER_SESSION");
        BOX_NUMBER_SESSION = BOX_NUMBER_SESSION.replaceAll("\\s", "");

        /**
         *  PDFView is a library implemented on this application to be able to display PDF file since android does not cater
         *  displaying of PDF file.
         */
        PDFView pdfView = (PDFView) view.findViewById(R.id.pdfView);
        switch (BOX_NUMBER_SESSION){
            case "1001":
                pdfView.fromAsset("1001.pdf").load();
                break;
            case "1002":
                pdfView.fromAsset("1002.pdf").load();
                break;
            case "1003":
                pdfView.fromAsset("1003.pdf").load();
                break;
        }

        return view;
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
}
