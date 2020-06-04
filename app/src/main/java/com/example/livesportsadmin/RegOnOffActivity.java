package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class RegOnOffActivity extends AppCompatActivity {

    private Switch aSwitchCricket,aSwitchVolleyball,aSwitchkabaddi;

    private AlertDialog alertDialog,dialog;
    private AlertDialog.Builder builder,builderconfirmation;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_on_off);
        getSupportActionBar().setTitle("Registration On/Off");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("RegStatus/");

        alertDialog= new ProgressDialog(getApplicationContext());
        builder=new ProgressDialog.Builder(RegOnOffActivity.this);
        LayoutInflater inflater =RegOnOffActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(false);
        alertDialog= builder.create();

        alertDialog.show();


        setCricketSwitchValue();
        setKabaddiSwitch();
        setVolleyBallSwitch();

    aSwitchCricket=(Switch)findViewById(R.id.switchCricketReg);
    aSwitchkabaddi=(Switch)findViewById(R.id.switchKabaddiReg);
    aSwitchVolleyball=(Switch)findViewById(R.id.switchVolleyReg);

    aSwitchCricket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked==true){
                //database
                databaseReference.child("CricketRegStatus").setValue(true);

            }
            else {
            databaseReference.child("CricketRegStatus").setValue(false);
        }
    }

    });

        aSwitchkabaddi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    //database
                    databaseReference.child("KabaddiRegStatus").setValue(true);

                }
                else {
                    databaseReference.child("KabaddiRegStatus").setValue(false);
                }
            }

        });

        aSwitchVolleyball.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    //database
                    databaseReference.child("VolleyBallRegStatus").setValue(true);

                }
                else {
                    databaseReference.child("VolleyBallRegStatus").setValue(false);
                }
            }

        });
}



public  void setCricketSwitchValue(){

        FirebaseDatabase.getInstance().getReference("RegStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           if (dataSnapshot.child("CricketRegStatus").getValue().equals(true)){
               aSwitchCricket.setChecked(true);
                alertDialog.dismiss();
           }
           else
           {
               alertDialog.dismiss();
               aSwitchCricket.setChecked(false);
           }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public  void setKabaddiSwitch(){

        FirebaseDatabase.getInstance().getReference("RegStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("KabaddiRegStatus").getValue().equals(true)){
                    aSwitchkabaddi.setChecked(true);
                    alertDialog.dismiss();
                }
                else
                {
                    alertDialog.dismiss();
                    aSwitchkabaddi.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public  void setVolleyBallSwitch(){

        FirebaseDatabase.getInstance().getReference("RegStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("VolleyBallRegStatus").getValue().equals(true)){
                    aSwitchVolleyball.setChecked(true);
                    alertDialog.dismiss();
                }
                else
                {
                    alertDialog.dismiss();
                    aSwitchVolleyball.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
