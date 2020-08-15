package com.example.monitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText uname;
    EditText uemail;
    EditText upass;
    Button signup;
    TextView login;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        uname= findViewById(R.id.uname);
        uemail= findViewById(R.id.uemail);
        upass= findViewById(R.id.upass);
        signup=findViewById(R.id.signinbtn);
        login=findViewById(R.id.signupherebtn);

        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name= uname.getText().toString().trim();
                String email= uemail.getText().toString().trim();
                String pass= upass.getText().toString().trim();



                if(TextUtils.isEmpty(name)){
                    uname.setError("Name is required");
                }

                if(TextUtils.isEmpty(email)){
                    uemail.setError("Email is required");
                }

                if(TextUtils.isEmpty(pass)){
                    upass.setError("password is required");
                    return;
                }



                if(pass.length()<6){
                    upass.setError("Password cahracters should be more than 6");
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"User Created",Toast.LENGTH_SHORT ).show();
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        else{
                            Toast.makeText(Register.this,"User could not be Created",Toast.LENGTH_SHORT ).show();

                        }
                    }
                });



            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });


    }
}
