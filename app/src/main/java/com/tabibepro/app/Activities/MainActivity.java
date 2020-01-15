package com.tabibepro.app.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.tabibepro.app.Activities.UserManagement.Profile;
import com.tabibepro.app.NetworkManager.AppConfig;
import com.tabibepro.app.R;
import com.tabibepro.app.Utils.SharedPrefs;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    Button addAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addAppointment = findViewById(R.id.addAppointment);

        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddAppointment.class));
            }
        });

        initDrawer();

    }

    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        CircleImageView image = headerView.findViewById(R.id.imageView);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name_drawer);
        TextView navSubtitle = (TextView) headerView.findViewById(R.id.subtitle_drawer);
        if (SharedPrefs.getUserModel() != null) {
//            if (SharedPrefs.getUserModel().getImage() != null) {
//                Glide.with(MainActivity.this).load(AppConfig.BASE_URL_Image + SharedPrefs.getUserModel().get()).into(image);
//            } else {
            Glide.with(MainActivity.this).load(R.drawable.logo).into(image);
//
//            }

            navUsername.setText("Welcome, " + SharedPrefs.getUserModel().getFirstName());
//            navSubtitle.setText(SharedPrefs.getUserModel().getPhoneNumber());
        }
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, Profile.class));

        } else if (id == R.id.nav_availabilities) {
            startActivity(new Intent(MainActivity.this, MyAvailabilities.class));

        } else if (id == R.id.nav_schedule) {
            startActivity(new Intent(MainActivity.this, MySchedule.class));

        } else if (id == R.id.nav_appointments) {


        } else if (id == R.id.nav_signuout) {
            SharedPrefs.logout();
            Intent i = new Intent(MainActivity.this, Splash.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
