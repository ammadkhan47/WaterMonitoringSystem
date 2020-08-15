package com.example.monitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
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

public class SimpleView extends AppCompatActivity {
    TextView tv8;
    DatabaseReference db;
    Button btn14;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplev);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Quality");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        db= FirebaseDatabase.getInstance().getReference().child("Member");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String turbl= dataSnapshot.child("turb").getValue().toString() ;
                String phl= dataSnapshot.child("ph").getValue().toString() ;


                if(turbl.length()>4) {
                    turbl = turbl.substring(0, 4);
                }


                double turb=    Double.valueOf(turbl);
                double ph=   Double.valueOf(phl);
                if( ( (turb>1.30)&&(turb<1.60) )&&( (ph>=5.00)&&(ph<9)) ) {
                    tv8 = findViewById(R.id.textView8);
                    tv8.setBackgroundColor(Color.GREEN);
                    tv8.setText("Good");

                }

                if(((turb>0.00)&&(turb<1.30))||(((ph>=0.00)&&(ph<5))||(ph>8))){
                    tv8 = findViewById(R.id.textView8);
                    tv8.setBackgroundColor(Color.RED);
                    tv8.setText("Bad");

                }


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
