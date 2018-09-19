package com.company.xpertech.xpertech.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.xpertech.xpertech.AppEULA;
import com.company.xpertech.xpertech.Method.Task;
import com.company.xpertech.xpertech.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

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

public class SignUpActivity extends AppCompatActivity {

    TextView qr_result;
    SurfaceView cameraPreview;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String BOX_NUMBER_SESSION;
    String USER_SESSION;
    Intent intent;

    final int RequestCameraPermissionID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show EULA
        new AppEULA(this).show();

        setContentView(R.layout.activity_scan_qr);

        // Start the session
        sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE);

        // Static User
        String result = "10011000000001";
        USER_SESSION = result;
        Log.d("SESSION", USER_SESSION);
        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
        backgroundTask.execute("login", result);


        qr_result = (TextView) findViewById(R.id.qr_result);

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        //Add Event
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(SignUpActivity.this,
                            new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                String result = "0";

                /**
                 * If a qr code is detected, it initiate the query to check for the device information
                 * of the detected qr code and if it exist in the system
                 */
                if (qrcodes.size() != 0) {
                    result = qrcodes.valueAt(0).displayValue;
                    USER_SESSION = result;
                    BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                    backgroundTask.execute("login", result);
                }
                Log.d("QR", result);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }


    public class BackgroundTask extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        Context ctx;
        public String boxNumber = null;

        public BackgroundTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url = "http://uslsxpertech.000webhostapp.com/xpertech/login.php";
            String method = params[0];
            if (method.equals("login")) {
                String accountNumber = params[1];
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("accountNumber", "UTF-8") + "=" + URLEncoder.encode(accountNumber, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String line = "";
                    line = bufferedReader.readLine();
                    String[] response = line.split("\\,");
                    BOX_NUMBER_SESSION = response[0];
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
            BOX_NUMBER_SESSION = BOX_NUMBER_SESSION.replaceAll("\\s+", "");
            if (BOX_NUMBER_SESSION.equals("1001") ||
                    BOX_NUMBER_SESSION.equals("1002") ||
                    BOX_NUMBER_SESSION.equals("1003")) {
                cameraSource.stop();

                //Storing the box number and user id to the session
                editor = sharedPref.edit();
                editor.putString("BOX_NUMBER_SESSION", BOX_NUMBER_SESSION);
                editor.putString("USER_SESSION", USER_SESSION);
                editor.commit();

                //if the main activity will start, the sign up activity will instantly be stop
                intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("keep", false);

                Toast.makeText(getApplicationContext(), "You have successfully logged in.", Toast.LENGTH_SHORT).show();

                startActivity(intent);
                finish();
            } else {
                // if the scanned qr code does not exist in the system, Task for statistics will be initiated and store the information that
                // an un successful log in was performed
                Toast.makeText(getApplicationContext(), "Sorry, device \"" + BOX_NUMBER_SESSION + "\" is not registered.", Toast.LENGTH_SHORT).show();
                Task task = new Task();
                task.execute("stat", "login", "fail", "10001000000000", "Failed Login Attemp");
            }
        }
    }
}