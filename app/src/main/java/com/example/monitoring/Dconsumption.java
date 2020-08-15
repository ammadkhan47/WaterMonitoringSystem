package com.example.monitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

public class Dconsumption extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView listView;

    ArrayAdapter<String> adapter;
    Member member;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dconsumption);


        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Consumption");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        databaseReference= FirebaseDatabase.getInstance().getReference().child("Consumption");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                CountDownLatch startSignal = new CountDownLatch(1);

                String query = "00-07-2020";

                final ArrayList<Member> peopleList = new ArrayList<>();
                final Member john[] = new Member[10];

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int i=0;

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(sdf.parse(query));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.DATE, 1);  // number of days to add
                    query = sdf.format(c.getTime());  // dt is now the new date

                    john[i] = new Member(query,
                            snapshot.getValue().toString()
                           );

                    startSignal.countDown();

                    peopleList.add(john[i]);
                    i++;


                }



                lv = (ListView) findViewById(R.id.list_view1);

                    /*
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> av, final View view, final int i, long i2) {

                        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(Avg.this, R.anim.animation);
                        view.startAnimation(hyperspaceJumpAnimation);

                    }
                });



                     */




                ConsumptionAdapter arrayAdapter = new ConsumptionAdapter(Dconsumption.this, R.layout.simple_list2, peopleList);



                lv.setAdapter(arrayAdapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Consumption.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


}
