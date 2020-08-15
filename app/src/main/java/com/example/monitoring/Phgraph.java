package com.example.monitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Phgraph extends AppCompatActivity  {

    DatabaseReference db;
    TextView tv7,tv8,tv10;
    //Firebase fb=new Firebase();

    CountDownLatch done = new CountDownLatch(1);
    private static final int GROUPS = 3;
    private static final String GROUP_1_LABEL = "Temperature";
    private static final String GROUP_2_LABEL = "Turbidity";
    private static final String GROUP_3_LABEL = "PH";
    private static final float BAR_SPACE = 0.05f;
    private static final float BAR_WIDTH = 0.2f;
    private BarChart chart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phgraph);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        chart = findViewById(R.id.group_chart);



        //HashMap<String,Integer> hm= fb.getHashMap("28-06-2020");


        //data();
        //x=Integer.valueOf(tv7.getText().toString());
        //y=Integer.valueOf(tv8.getText().toString());
        //z=Integer.valueOf(tv10.getText().toString());



        db= FirebaseDatabase.getInstance().getReference().child("Average");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                CountDownLatch startSignal = new CountDownLatch(1);
                ArrayList<BarEntry> values1 = new ArrayList<>();
                ArrayList<BarEntry> values2 = new ArrayList<>();
                ArrayList<BarEntry> values3 = new ArrayList<>();

                String query = "00-07-2020";  // Start date

                for(int i=0;i<3;i++) {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(sdf.parse(query));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.DATE, 1);  // number of days to add
                    query = sdf.format(c.getTime());  // dt is now the new date


                    String templ = dataSnapshot.child(query).child("Temp").getValue().toString();
                    String turbl = dataSnapshot.child(query).child("Turb").getValue().toString();
                    String phl = dataSnapshot.child(query).child("PH").getValue().toString();


                    startSignal.countDown();

                    values1.add(new BarEntry(i, Float.valueOf(templ)));
                    values2.add(new BarEntry(i, Float.valueOf(turbl)));
                    values3.add(new BarEntry(i, Float.valueOf(phl)));

                }


        BarDataSet set1 = new BarDataSet(values1, GROUP_1_LABEL);
        BarDataSet set2 = new BarDataSet(values2, GROUP_2_LABEL);
        BarDataSet set3 = new BarDataSet(values3, GROUP_3_LABEL);

        set1.setColor(ColorTemplate.MATERIAL_COLORS[0]);
        set2.setColor(ColorTemplate.MATERIAL_COLORS[1]);
        set3.setColor(ColorTemplate.MATERIAL_COLORS[2]);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);


        BarData data = new BarData(dataSets);

        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(7);

        chart.setData(data);

        chart.getBarData().setBarWidth(BAR_WIDTH);

        float groupSpace = 1f - ((BAR_SPACE + BAR_WIDTH) * GROUPS);
        chart.groupBars(0, groupSpace, BAR_SPACE);

        chart.invalidate();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }



    }




