package com.example.monitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
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

import android.os.Bundle;

public class Dateconsumption extends AppCompatActivity {

    DatabaseReference db;
    TextView tv24;
    EditText editText2;
    String query="";
    Button btn4,btn5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateconsumption);

        btn4=findViewById(R.id.button4);

        tv24=findViewById(R.id.textView24);

        btn5=findViewById(R.id.button5);


        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Consumption.class));
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                data();
            }
        });



    }
    public void data(){
        db= FirebaseDatabase.getInstance().getReference().child("Consumption");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editText2=findViewById(R.id.editText2);
                query=editText2.getText().toString();


                String templ= dataSnapshot.child(query).getValue().toString() ;

                tv24.setText(templ);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}






