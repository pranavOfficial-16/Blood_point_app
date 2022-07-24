package com.example.blood_point;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SearchDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dashboard);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new SearchedResult()).commit();
    }
}