package com.company.xpertech.xpertech.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.company.xpertech.xpertech.Method.Packages;
import com.company.xpertech.xpertech.Method.Task;
import com.company.xpertech.xpertech.Method.Troubleshoot;
import com.company.xpertech.xpertech.Nav_Fragment.AboutNBCFragment;
import com.company.xpertech.xpertech.Nav_Fragment.AboutXpertechFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Channel_Packages_Fragment.ChannelFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Channel_Packages_Fragment.PackagesFragment;
import com.company.xpertech.xpertech.Nav_Fragment.FeedbackFragment;
import com.company.xpertech.xpertech.Nav_Fragment.HomeFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Manual_Fragment.ManualListFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Remote_Fragment.RemoteItemFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Remote_Fragment.RemoteListFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Self_Install_Fragment.SelfInstallFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Self_Install_Fragment.Sub_Install_Fragment;
import com.company.xpertech.xpertech.Nav_Fragment.StatisticsFragment.Statistics_Fragment;
import com.company.xpertech.xpertech.Nav_Fragment.Troubleshoot_Fragment.IntroFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Troubleshoot_Fragment.TroubleeshootItemFragment;
import com.company.xpertech.xpertech.Nav_Fragment.Troubleshoot_Fragment.TroubleshootFragment;
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

import static android.Manifest.permission.CALL_PHONE;

/**
 *  "implements" the fragments under nav_fragment package
 */
public class MainActivity extends AppCompatActivity
        implements TroubleshootFragment.OnListFragmentInteractionListener,
        TroubleeshootItemFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        IntroFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        PackagesFragment.OnListFragmentInteractionListener,
        ChannelFragment.OnFragmentInteractionListener,
        SelfInstallFragment.OnFragmentInteractionListener,
        Sub_Install_Fragment.OnFragmentInteractionListener,
        FeedbackFragment.OnFragmentInteractionListener,
        RemoteListFragment.OnFragmentInteractionListener,
        RemoteItemFragment.OnFragmentInteractionListener,
        ManualListFragment.OnFragmentInteractionListener,
        AboutXpertechFragment.OnFragmentInteractionListener,
        AboutNBCFragment.OnFragmentInteractionListener,
        Statistics_Fragment.OnFragmentInteractionListener{

    TextView username;
    TextView contact;
    SharedPreferences s;
    String USER_SESSION = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         *  Onclick functionality for floating button that launches feedback form
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, feedbackFragment).addToBackStack("main").commit();
            }
        });

        /**
         *  Setting the action for the hamburger menu
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /**
         *  Shared preferences ask as the session for the whole application
         *  USER_SESSION contains the user_id + device_id
         */
        s = getSharedPreferences("values", Context.MODE_PRIVATE);
        USER_SESSION = s.getString("USER_SESSION", "USER_SESSION");
        Log.d("USER", USER_SESSION);

        /**
         *  Async task for extracting user information that is displayed on the hambuger menu
         */
        MainActivity.BackgroundTask backgroundTask = new MainActivity.BackgroundTask(this);
        backgroundTask.execute("get");

        /**
         *  Setting the layout for the hamburger menu
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         *  Task method that stores the statistics information, in this call, user logged in successfully
         */
        Task task = new Task();
        task.execute("stat", "login", "pass", USER_SESSION, "User Successfully Logged In");

        /**
         *  Setting the layout for the Home
         */
        HomeFragment hf = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, hf).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /**
         * Set the click action for the menu in Hamburger Menu
         */
        switch(item.getItemId()){
            case R.id.nav_troubleshoot:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new TroubleshootFragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_selfInstall:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new SelfInstallFragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new HomeFragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_package:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new PackagesFragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_remoteControl:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new RemoteListFragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_send:
                /**
                 *  Intent for a call action and setting the number to which the call will be directed
                 */
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:4458514"));

                /**
                 *  If statements check if the permission of the application to phone to make a phone call using the app is granted
                 */
                if (ContextCompat.checkSelfPermission(this.getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * Task method that stores the statistics information, in this case, user made a phone call
                     */
                    Task task = new Task();
                    task.execute("stat","call", "pass", USER_SESSION);
                    startActivity(callIntent);
                } else {
                    /**
                     *  when if condition is false, the application will request permission to make a phone call
                     */
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
                break;
            case R.id.nav_userManual:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new ManualListFragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_stat:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Statistics_Fragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_share:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new FeedbackFragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_aboutnbc:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new AboutNBCFragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_aboutxpert:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new AboutXpertechFragment()).addToBackStack("tag").commit();
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Troubleshoot item) {

    }

    @Override
    public void onListFragmentInteraction(Packages item) {

    }

    String global_name = "";
    String global_contact = "";

    /**
     *  Async task function to extract user information from the database host
     */
    public class BackgroundTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        public BackgroundTask(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading");
            dialog.show();
        }

        /**
         * Uses HttpURLConnection to connect the URL that contains the query for subscriber information
         * Uses BufferedWriter to encode the data required for the query of user information, in this case, ownership_id
         * Uses BufferedReader to decode the resulting data of the query
         */
        @Override
        protected String doInBackground(String... params) {
            String login_url = "https://uslsxpertech.000webhostapp.com/xpertech/subscriber_name.php";
            String method = params[0];
            if (method.equals("get")) {
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String ownership_id = s.getString("USER_SESSION", "USER_SESSION");
                    String data = URLEncoder.encode("ownership_id", "UTF-8") + "=" + URLEncoder.encode(ownership_id, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String line = "";
                    String[] detail;
                    while ((line = bufferedReader.readLine()) != null) {
                        detail = line.split("\\$");
                        global_name = detail[0].split(",")[0];
                        global_contact = detail[0].split(",")[1];
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

        /**
         * Set's the data after query to the user information in the hamburger menu
         */
        @Override
        protected void onPostExecute(String result) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            username = (TextView) drawer.findViewById(R.id.usernameText);
            contact = (TextView) drawer.findViewById(R.id.contactText);
            username.setText(global_name);
            contact.setText(global_contact);

            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

}
