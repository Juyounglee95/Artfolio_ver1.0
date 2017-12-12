package com.example.user.artfolio_ver10;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    String name, id, phone, email,profile;
    TextView userid, useremail;
    ImageView userprofile;
    int picList_size;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Dashboard_frag Dashboard_frag;
    private Home_frag Home_frag;
    private favorite_frag favorite_frag;
    private export_frag export_frag;
    private Usersetting_frag usersetting_frag;
    private Dashboard_agency_frag Dashboard_agency_frag;
    String list[];
    String userlist[];
    String profilelist[];
    String mode;
    int vidList_size;
    String vidlist[];
    int fa_totalnum [];
    ArrayList<String> piclist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        id = intent.getExtras().getString("id");
        phone = intent.getExtras().getString("phone");
        email = intent.getExtras().getString("email");
        profile = intent.getExtras().getString("profile");
        userlist= intent.getExtras().getStringArray("wholelist");
        profilelist= intent.getExtras().getStringArray("wholeprofile");
        fa_totalnum= intent.getExtras().getIntArray("fa_numlist");
        mode = intent.getExtras().getString("mode");
        if(mode.equals("agency")){
            this.setTitle("ART-FOLIO AGENCY");
        }
//        for(int i=0; i<profilelist.length; i++){
//            if(!profilelist[i].equals("null")){
//                profilelist[i]= "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/"+profilelist[i];
//            }
//        }
       // System.out.println(name);
        setContentView(R.layout.activity_main_user);

        Dashboard_agency_frag = Dashboard_agency_frag.newInstance();
        Dashboard_frag =  Dashboard_frag.newInstance();
        Home_frag = Home_frag.newInstance();
        favorite_frag = favorite_frag.newInstance();
        export_frag = export_frag.newInstance();
        usersetting_frag = Usersetting_frag.newInstance();
        Bundle home_bundle = new Bundle();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        home_bundle.putString("loginID", id);
        home_bundle.putStringArray("userlist", userlist);
        home_bundle.putStringArray("wholeprofile", profilelist);
        home_bundle.putIntArray("fa_numlist", fa_totalnum);
        Home_frag.setArguments(home_bundle);
        transaction.replace(R.id.container, Home_frag);

        transaction.addToBackStack(null);
        transaction.commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        userprofile=(ImageView)nav_header_view.findViewById(R.id.user_profile_nav);
        if(profile.equals("null")){

            userprofile.setImageResource(R.drawable.kakao_default_profile_image);
        }else{
          //  profile = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/"+profile;
            Glide.with(nav_header_view).load(profile).into(userprofile);
        }
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
            if(mode.equals("user")) {
                if (list != null) {
                    if (vidlist != null) {
                        dash_bundel.putString("initial", "notFirst");
                        dash_bundel.putString("id", id);
                        dash_bundel.putString("email", email);
                        dash_bundel.putInt("picnum", picList_size);
                        dash_bundel.putInt("vidnum", vidList_size);
                        dash_bundel.putString("profile", profile);
                        dash_bundel.putStringArray("piclist", list);
                        dash_bundel.putStringArray("vidlist", vidlist);
                    } else {
                        dash_bundel.putString("initial", "notFirst_vidnull");
                        dash_bundel.putString("id", id);
                        dash_bundel.putString("email", email);
                        dash_bundel.putInt("picnum", picList_size);
                        dash_bundel.putString("profile", profile);
                        dash_bundel.putStringArray("piclist", list);
                        dash_bundel.putInt("vidnum", vidList_size);
                    }
                    // Dashboard_frag.setArguments(dash_bundel);
                    //transaction.replace(R.id.container, Dashboard_frag);
                } else {
                    if (vidlist != null) {
                        dash_bundel.putString("initial", "First");
                        dash_bundel.putString("id", id);
                        dash_bundel.putString("email", email);
                        dash_bundel.putString("profile", profile);
                        dash_bundel.putInt("picnum", picList_size);
                        dash_bundel.putStringArray("vidlist", vidlist);
                        dash_bundel.putInt("vidnum", vidList_size);
                    } else {
                        dash_bundel.putString("initial", "First_vidnull");
                        dash_bundel.putString("id", id);
                        dash_bundel.putString("email", email);
                        dash_bundel.putString("profile", profile);
                        dash_bundel.putInt("picnum", picList_size);
                        dash_bundel.putInt("vidnum", vidList_size);

                    }

                }

                Dashboard_frag.setArguments(dash_bundel);
                transaction.replace(R.id.container, Dashboard_frag);
            }
            else {
                if(list!=null) {
                    dash_bundel.putString("initial", "notFirst");
                    dash_bundel.putString("id", id);
                    dash_bundel.putString("email", email);
                    dash_bundel.putInt("picnum", picList_size);
                    dash_bundel.putString("profile", profile);
                    dash_bundel.putStringArray("piclist", list);
                    // Dashboard_frag.setArguments(dash_bundel);
                    //transaction.replace(R.id.container, Dashboard_frag);
                }else{
                    dash_bundel.putString("initial", "First");
                    dash_bundel.putString("id", id);
                    dash_bundel.putString("email", email);
                    dash_bundel.putString("profile", profile);
                    dash_bundel.putInt("picnum", picList_size);

                    //dash_bundel.putStringArray("piclist", list);

                }

                Dashboard_agency_frag.setArguments(dash_bundel);
                transaction.replace(R.id.container, Dashboard_agency_frag);
            }
        } else if (itemid == R.id.nav_favorite) {
            favorite_listInfo favorite_listInfo = new favorite_listInfo();

            String value = id+","+id;
            try {
                String in_falist = favorite_listInfo.execute(value).get();
                if(!in_falist.equals("null")){
                    String [] list = in_falist.split("/");
                    //list[list.length-1] == null
                    Bundle favorite_bundle = new Bundle();
                  //  favorite_bundle.putStringArray("profilelist",profilelist);
                    favorite_bundle.putStringArray("falist", list);
                    favorite_bundle.putString("id", id);
                    favorite_frag.setArguments(favorite_bundle);
                    transaction.replace(R.id.container, favorite_frag);

                }else{
                    Toast.makeText(MainActivity_user.this, "There is no favorite artist", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        } else if (itemid == R.id.nav_home) {
            Bundle home_bundle = new Bundle();
            home_bundle.putString("loginID", id);
            home_bundle.putStringArray("userlist", userlist);
            home_bundle.putStringArray("wholeprofile", profilelist);
            home_bundle.putIntArray("fa_numlist", fa_totalnum);
            Home_frag.setArguments(home_bundle);
            transaction.replace(R.id.container, Home_frag);
        }
        else if (itemid == R.id.nav_export) {
            Bundle export_bundle = new Bundle();

            if(list!=null) {
                if(vidlist!=null) {
                    export_bundle.putString("id", id);
                    export_bundle.putString("email", email);
                    //export_bundle.putInt("picnum", picList_size);

                    export_bundle.putStringArray("piclist", list);
                    export_bundle.putStringArray("vidlist", vidlist);
                    export_frag.setArguments(export_bundle);
                    transaction.replace(R.id.container, export_frag);
                }else{
                    export_bundle.putString("id", id);
                    export_bundle.putString("email", email);
                    //export_bundle.putInt("picnum", picList_size);

                    export_bundle.putStringArray("piclist", list);

                    export_frag.setArguments(export_bundle);
                    transaction.replace(R.id.container, export_frag);
                }
                // Dashboard_frag.setArguments(dash_bundel);
                //transaction.replace(R.id.container, Dashboard_frag);
            }else{
                if(vidlist!=null){
                    export_bundle.putString("id", id);
                    export_bundle.putString("email", email);
                    //export_bundle.putInt("picnum", picList_size);

                    export_bundle.putStringArray("vidlist", vidlist);

                    export_frag.setArguments(export_bundle);
                    transaction.replace(R.id.container, export_frag);
                }else {
                    Toast.makeText(MainActivity_user.this, "Your work does not exist! Please upload your work", Toast.LENGTH_LONG).show();
                }
            }


        } else if (itemid == R.id.nav_manage) {
            Intent intent = new Intent(this, usersetting_activity.class);
            intent.putExtra("id", id);
            intent.putExtra("profilepath", profile);
            startActivityForResult(intent, 200);
//            Bundle dash_bundle = new Bundle();
//            dash_bundle.putString("id", id);
//            usersetting_frag.setArguments(dash_bundle);
//            transaction.replace(R.id.container, usersetting_frag);

        }
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 200) {
            if (data!=null) {
                profile = data.getStringExtra("profile");
                Glide.with(this).load(profile).into(userprofile);
                System.out.println("#####");

            }
        }
    }
    public void getPic_List(String data){
        try {
            String string = data;

             list = string.split("<br>");

            picList_size=list.length;
//            vidList_db vidList_db = new vidList_db();
//
//            String vid = vidList_db.execute(id).get();
//            vidlist = vid.split("%");
//            vidList_size = vidlist.length;
        } catch (NullPointerException e) {
            e.printStackTrace();
            //Log.d(TAG, "showResult : ", e);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public String[] update_list(){
        set_picList();

        return list;
    }
    public String[] update_vidlist(){
        set_picList();

        return vidlist;
    }
    public void getMemo(String data){
        try {
            String string = data;

            list = string.split("<br>");

            picList_size=list.length;

        } catch (NullPointerException e) {
            e.printStackTrace();
            //Log.d(TAG, "showResult : ", e);
        }

    }


    class picInfo_DB extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... unuesed){

            String data = "";
            String value = "ID="+id+"";
            Log.e("POST",value);
            try {
                URL url = new URL("http://54.226.200.206/down_picInfo.php"); //picture list 정보랑 video list정보 다운로드

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
           // if (mode.equals("user")) {
                if (data.equals("null")) {
                    //Toast.makeText(getApplicationContext(), "There is no files on server", Toast.LENGTH_SHORT).show();
                    vidList_db vidList_db = new vidList_db();
                    try {
                        String vid = vidList_db.execute(id).get();
                        if (!vid.equals("null")) {
                            vidlist = vid.split("%");
                            vidList_size = vidlist.length;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getPic_List(data);
                    vidList_db vidList_db = new vidList_db();
                    try {
                        String vid = vidList_db.execute(id).get();
                        if (!vid.equals("null")) {
                            vidlist = vid.split("%");
                            vidList_size = vidlist.length;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();

                }

//            }else{
//                if(data.equals("null")){
//                    //Toast.makeText(getApplicationContext(), "There is no files on server", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    getMemo(data);
//                    // Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();
//                }
//            }
        }




    }
}
