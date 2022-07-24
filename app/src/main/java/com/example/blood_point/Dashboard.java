package com.example.blood_point;

import static com.example.blood_point.R.style.comic_sans;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAnalytics firebaseAnalytics;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton Post, Search;
    TextView name_blood_header, email_header;
    FirebaseAuth mAuth;
    FirebaseDatabase db_user;
    FirebaseUser cur_user;
    DatabaseReference db_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        Post = findViewById(R.id.post);
        Search = findViewById(R.id.search);
        Search.setOnClickListener(v -> startActivity(new Intent(this, SearchDonorActivity.class)));
        Post.setOnClickListener(v -> startActivity(new Intent(this, PostActivity.class)));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        toolbar.setTitleTextAppearance(this, comic_sans);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        View header = navigationView.getHeaderView(0);
        name_blood_header = header.findViewById(R.id.text_name_blood);
        email_header = header.findViewById(R.id.text_email_id);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        db_user = FirebaseDatabase.getInstance();
        cur_user = mAuth.getCurrentUser();
        db_ref = db_user.getReference("USERS");

        Query single_user = db_ref.child(cur_user.getUid());
        single_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_data data = dataSnapshot.getValue(user_data.class);
                assert data != null;
                String name = Objects.requireNonNull(dataSnapshot.child("NAME").getValue()).toString();
                String blood = Objects.requireNonNull(dataSnapshot.child("BLOOD_GROUP").getValue()).toString();
                String header1 = name + " -> " + blood;
                String header2 = cur_user.getEmail();
                name_blood_header.setText(header1);
                email_header.setText(header2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new HomeView()).commit();
            navigationView.getMenu().getItem(0).setChecked(true);

        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> finish())
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        try {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new HomeView()).commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
                    toolbar.setTitleTextAppearance(this, comic_sans);
                    Post.show();
                    Search.show();
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(this, ProfileActivity.class));
                    break;
                case R.id.nav_post:
                    startActivity(new Intent(this, MyBloodpost.class));
                    break;
                case R.id.nav_google_map:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new google_map_fragment()).commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Google Maps");
                    toolbar.setTitleTextAppearance(this, comic_sans);
                    Post.hide();
                    Search.hide();
                    Bundle bundle = new Bundle();
                    bundle.putString("map", "google maps");
                    firebaseAnalytics.logEvent("GoogleMaps", bundle);
                    break;
                case R.id.nav_About:
                    startActivity(new Intent(this, Blood_info.class));
                    break;
                case R.id.nav_Rate_us:
                    startActivity(new Intent(this, Rate_us.class));
                    break;
                case R.id.nav_log_out:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Do you want to log out ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> {
                                mAuth.signOut();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            })
                            .setNegativeButton("No", (dialog, id) -> dialog.cancel()).create().show();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}