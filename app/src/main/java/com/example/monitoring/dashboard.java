package com.example.monitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class dashboard extends AppCompatActivity {

    ImageView quality,report,consumption,bill,map,average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard");
        actionBar.hide();

        quality=findViewById(R.id.quality);
        report=findViewById(R.id.report);
        consumption= findViewById(R.id.consumption);
        bill=findViewById(R.id.bill);
        map=findViewById(R.id.map);
        average=findViewById(R.id.average);



        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Quality.class));
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Phgraph.class));
            }
        });

        consumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Consumption.class));
            }
        });

        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Generatebill.class));
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Map.class));
            }
        });

        average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Avg.class));
            }
        });



    }
}
