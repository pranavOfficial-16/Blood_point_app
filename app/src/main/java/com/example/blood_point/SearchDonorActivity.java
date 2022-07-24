package com.example.blood_point;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchDonorActivity extends AppCompatActivity {
    ImageView back;
    EditText bg, loc;
    Button Search;
    static String blood_group, location;
    static String getBg() {
        return blood_group;
    }
    static String getLoc() {
        return location;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donor);
        back = findViewById(R.id.backarrow);
        bg = findViewById(R.id.search_blood_group);
        loc = findViewById(R.id.search_location);
        Search = findViewById(R.id.search_button);
        back.setOnClickListener(v -> startActivity(new Intent(this, Dashboard.class)));
        try {
            Search.setOnClickListener(v -> {
                if (bg.getText().length() == 0) {
                    bg.setError("Enter your Blood group!");
                } else if (loc.getText().length() == 0) {
                    loc.setError("Enter your Location!");
                }
                else{
                    startActivity(new Intent(this, SearchDashboard.class));
                }
                blood_group = bg.getText().toString();
                location = loc.getText().toString();
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}