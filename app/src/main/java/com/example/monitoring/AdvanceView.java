package com.example.monitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdvanceView extends AppCompatActivity {



    TextView temp,turb,ph,level;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitoring);
        temp=findViewById(R.id.temp);
        turb=findViewById(R.id.turb);
        ph=findViewById(R.id.ph);
        level=findViewById(R.id.level);



        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);




        db= FirebaseDatabase.getInstance().getReference().child("Member");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String templ= dataSnapshot.child("temp").getValue().toString() ;
                String turbl= dataSnapshot.child("turb").getValue().toString() ;
                String phl= dataSnapshot.child("ph").getValue().toString() ;
                String level1= dataSnapshot.child("level").getValue().toString() ;

                if(templ.length()>4) {
                    templ = templ.substring(0, 4);
                }
                if(turbl.length()>4) {
                    turbl = turbl.substring(0, 4);
                }


                temp.setText(templ+" °C");
                turb.setText(turbl+" NTU");
                ph.setText(phl+" pH");
                level.setText(level1+" m");





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Quality.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
