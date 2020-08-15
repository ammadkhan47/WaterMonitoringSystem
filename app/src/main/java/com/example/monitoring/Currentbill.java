package com.example.monitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Currentbill extends AppCompatActivity {
    TextView tv,tv1;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentbill);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Consumption");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv=findViewById(R.id.textView29);
        tv1=findViewById(R.id.textView28);



        db= FirebaseDatabase.getInstance().getReference().child("Consumption");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String query = "01-07-2020";
                String cost;
                cost= String.valueOf(Double.valueOf(dataSnapshot.child(query).getValue().toString())*0.15);
                if(cost.length()>4) {
                    cost = cost.substring(0, 4);
                }

                tv.setText("Rs"+cost );
                tv1.setText("Today");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Generatebill.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
