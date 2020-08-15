package com.example.monitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Currentconsumption extends AppCompatActivity {

    TextView tv,tv1;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentconsumption);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Consumption");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv=findViewById(R.id.ccon1);
        tv1=findViewById(R.id.ccon);



            db= FirebaseDatabase.getInstance().getReference().child("Consumption");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String query = "01-07-2020";

                    tv.setText(dataSnapshot.child(query).getValue().toString()+"ml" );
                    tv1.setText("Today");

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
