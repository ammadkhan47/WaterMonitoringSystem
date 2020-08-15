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

public class Avg extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView listView;

    String templ;
    String turbl;
    String phl;

    ArrayAdapter<String> adapter;
    Member member;
    private ListView lv;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_dbill);


        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Average");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                CountDownLatch startSignal = new CountDownLatch(1);

                String query = "00-07-2020";

                final ArrayList<Member> peopleList = new ArrayList<>();
                final Member john[] = new Member[10];

                    for (int i = 0; i < 3; i++) {

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
                                dataSnapshot.child(query).child("Temp").getValue().toString(),
                                dataSnapshot.child(query).child("Turb").getValue().toString(),
                                dataSnapshot.child(query).child("PH").getValue().toString());

                        startSignal.countDown();

                        peopleList.add(john[i]);


                    }



                lv = (ListView) findViewById(R.id.list_view);

                    /*
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> av, final View view, final int i, long i2) {

                        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(Avg.this, R.anim.animation);
                        view.startAnimation(hyperspaceJumpAnimation);

                    }
                });



                     */




                MemberAdapter arrayAdapter = new MemberAdapter(Avg.this, R.layout.simple_list, peopleList);



                lv.setAdapter(arrayAdapter);





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
