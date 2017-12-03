package com.example.user.artfolio_ver10;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.artfolio_ver10.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity_user extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String name, id, phone, email;
    TextView userid, useremail;
    int picList_size;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Dashboard_frag Dashboard_frag;
    private Home_frag Home_frag;
    String list[];
    String userlist[];
    ArrayList<String> piclist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        id = intent.getExtras().getString("id");
        phone = intent.getExtras().getString("phone");
        email = intent.getExtras().getString("email");
        userlist= intent.getExtras().getStringArray("userlist");
        System.out.println(name);
        setContentView(R.layout.activity_main_user);


        Dashboard_frag =  Dashboard_frag.newInstance();
        Home_frag = Home_frag.newInstance();
        Bundle home_bundle = new Bundle();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        home_bundle.putStringArray("userlist", userlist);
        Home_frag.setArguments(home_bundle);
        transaction.add(R.id.container, Home_frag);

        transaction.addToBackStack(null);
        transaction.commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Upload your work!", Snackbar.LENGTH_LONG)
                        .setAction(
                "UPLOAD", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(MainActivity_user.this, "Add work activity", Toast.LENGTH_SHORT).show();

                    }
                }).show();
            }
        });
        // mTitle=mDrawerTitle=getTitle();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header_view = navigationView.getHeaderView(0);
        userid= (TextView)nav_header_view.findViewById(R.id.user_id_nav);
        userid.setText(id);
        useremail= (TextView)nav_header_view.findViewById(R.id.user_email_nav);
        useremail.setText(email);
        set_picList();

    }
//    protected void onPostCreate(Bundle savedInstanceState){
//        super.onPostCreate(savedInstanceState);
//        toggle.syncState();
//    }
    public void set_picList(){
        picInfo_DB picInfo_db = new picInfo_DB();
        picInfo_db.execute();
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
        getMenuInflater().inflate(R.menu.main_activity_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


       // transaction.add(R.id.container, Dashboard_frag);


        // Handle navigation view item clicks here.
        int itemid = item.getItemId();

        if (itemid == R.id.nav_dashboard) {
            // Handle the dashboard action
            Bundle dash_bundel = new Bundle();




            if(list!=null) {
                dash_bundel.putString("initial", "notFirst");
                dash_bundel.putString("id", id);
                dash_bundel.putString("email", email);
                dash_bundel.putInt("picnum", picList_size);

                dash_bundel.putStringArray("piclist", list);

               // Dashboard_frag.setArguments(dash_bundel);
                //transaction.replace(R.id.container, Dashboard_frag);
            }else{
                dash_bundel.putString("initial", "First");
                dash_bundel.putString("id", id);
                dash_bundel.putString("email", email);
                dash_bundel.putInt("picnum", picList_size);

                //dash_bundel.putStringArray("piclist", list);


            }

            Dashboard_frag.setArguments(dash_bundel);
            transaction.replace(R.id.container, Dashboard_frag);

        } else if (itemid == R.id.nav_favorite) {

        } else if (itemid == R.id.nav_home) {
            transaction.replace(R.id.container, Home_frag);
        } else if (itemid == R.id.nav_export) {

        } else if (itemid == R.id.nav_manage) {

        }else if (itemid == R.id.nav_share) {

        }else if (itemid == R.id.nav_message) {

        }
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void getPic_List(String data){
        try {
            String string = data;

             list = string.split("<br>");

            picList_size=list.length;

        } catch (NullPointerException e) {
            e.printStackTrace();
            //Log.d(TAG, "showResult : ", e);
        }

    }
    public String[] update_list(){
        set_picList();

        return list;
    }

    class picInfo_DB extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... unuesed){

            String data = "";
            String value = "ID="+id+"";
            Log.e("POST",value);
            try {
                URL url = new URL("http://54.226.200.206/down_picInfo.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.connect();

                OutputStream outs = con.getOutputStream();
                outs.write(value.getBytes("UTF-8"));
                outs.flush();
                outs.close();


                InputStream is = null;
                BufferedReader in = null;


                is = con.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuilder buff = new StringBuilder();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                //System.out.println(data);
                Log.e("Path data",data);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        protected void onPostExecute(String data) {
            //super.onPostExecute(data);
                 /* 서버에서 응답 */
            //Log.e("RECV DATA",data);

            //System.out.print(data);
            // data = data.toString();
            if(data.equals("null")){
                //Toast.makeText(getApplicationContext(), "There is no files on server", Toast.LENGTH_SHORT).show();
            }
            else{
                getPic_List(data);
               // Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();

            }

        }




    }
}
