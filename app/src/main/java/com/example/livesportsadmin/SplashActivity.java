package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);



        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();


        if(FirebaseAuth.getInstance().getCurrentUser()!= null){
            alreadyLogedIn();

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    progressBar.setVisibility(View.GONE);
//
//                }
//            },6000);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            },6000);
            FirebaseAuth.getInstance().signOut();
           // finish();

        }
    }


    private void  alreadyLogedIn(){
        DatabaseReference databaseReference=firebaseDatabase.getReference("admin/");
       final String firebaseUser=FirebaseAuth.getInstance().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            //  @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  progressBar.setVisibility(View.GONE);

                String Admin = dataSnapshot.child(firebaseUser).getValue().toString();
                if (Admin.equals("Admin") && (Admin != null)) {
                   // progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();


                }

                else if (Admin.equals("CricketScorer")){
                    progressBar.setVisibility(View.GONE);
                   // progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),ScorerMainActivity.class));
                    finish();
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    //progressDialog.dismiss();
                    //  String message = task.getException().getLocalizedMessage();
                    Toast.makeText(getApplicationContext(), "E R R O R.. ", Toast.LENGTH_LONG).show();
                }


            }

            // @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {




            }
        });



    }
}
