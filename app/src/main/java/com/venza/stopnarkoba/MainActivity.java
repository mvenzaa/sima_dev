package com.venza.stopnarkoba;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.venza.stopnarkoba.fragment.ArtikelFragment;
import com.venza.stopnarkoba.fragment.StreamingFragment;
import com.venza.stopnarkoba.fragment.VideoFragment;
import com.venza.stopnarkoba.fragment.WargaFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    private final int MenuItem_LoginId = 378;
    private final int MenuItem_LogoutId = 379;
    SharedPreferences pref;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArtikelFragment fragment = new ArtikelFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        setTitle("Stop Narkoba");



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        pref = getSharedPreferences("stopnarkoba", MODE_PRIVATE);
        setHeaderNavigation();
        setMenuNavigation();

//        TEST AMBIL HASH KEY
//        try {
//            PackageInfo info = context.getPackageManager()
//            .getPackageInfo("com.venza.stopnarkoba", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch(PackageManager.NameNotFoundException e){
//
//        }  catch (NoSuchAlgorithmException e) {
//
//        }

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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("key", query);
                startActivity(i);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        String is_login = pref.getString("is_login", null);
        if (is_login != null) {
            MenuItem menu_login = menu.findItem(R.id.action_login);
            menu_login.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.artikel) {

            ArtikelFragment fragment = new ArtikelFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
            setTitle("Stop Narkoba");

        } else if (id == R.id.video) {

            VideoFragment fragment = new VideoFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
            setTitle("Video");

        } else if (id == R.id.warga) {

            WargaFragment fragment = new WargaFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
            setTitle("Warga");

        } else if (id == R.id.nav_streaming) {

            StreamingFragment fragment = new StreamingFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
            setTitle("Live Streaming");

        } else if (id == R.id.nav_visit) {
            Intent google = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stopnarkoba.id/"));
            startActivity(google);
            return true;

        }  else if (id == R.id.logout) {
            pref.edit().clear().commit();
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

            Intent intent = getIntent();
            finish();
            startActivity(intent);
            return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void setHeaderNavigation(){
        String is_login = pref.getString("is_login", null);
        if (is_login != null) {
            View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_login, null);
            TextView  name = ((TextView) nav_header.findViewById(R.id.name));
            name.setText(pref.getString("name", ""));

            // set image di sini
            //ImageView imageView = (ImageView)nav_header.findViewById(R.id.imageView);


            navigationView.addHeaderView(nav_header);
        }else {
            View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
            TextView text_login = ((TextView) nav_header.findViewById(R.id.text_login));
            text_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });
            navigationView.addHeaderView(nav_header);
        }
    }


    public void setMenuNavigation(){
        String is_login = pref.getString("is_login", null);
        if (is_login != null) {
            Menu menu = navigationView.getMenu();
            MenuItem nav_logout = menu.findItem(R.id.logout);
            nav_logout.setVisible(true);
        }
    }
}
