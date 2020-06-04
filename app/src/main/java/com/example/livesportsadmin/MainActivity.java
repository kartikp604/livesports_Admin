package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonlogin;
    private EditText editTextAdminEmail,editTextAdminPassword;
    private TextView textViewWrongInfo;

    private FirebaseAuth firebaseAuth;
    private String firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DataSnapshot dataSnapshot;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog=new ProgressDialog(this);
        buttonlogin=(Button) findViewById(R.id.buttonLogin);
        
        editTextAdminEmail=(EditText)findViewById(R.id.adminLoginEmail);
        editTextAdminPassword=(EditText)findViewById(R.id.adminLoginPassword);

        textViewWrongInfo=(TextView)findViewById(R.id.textViewWrongInfo);

            firebaseAuth=FirebaseAuth.getInstance();
      //  firebaseUser=firebaseAuth.getCurrentUser().getUid();


        firebaseDatabase=FirebaseDatabase.getInstance();


        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {


            alreadyLogedIn();
        //            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//            finish();
        }





        buttonlogin.setOnClickListener(this);



    }


    private void  alreadyLogedIn(){
        databaseReference=firebaseDatabase.getReference("admin/");
        firebaseUser=firebaseAuth.getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            //  @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //  progressDialog.dismiss();

                String Admin = dataSnapshot.child(firebaseUser).getValue().toString();
                if (Admin.equals("Admin") && (Admin != null)) {
                    progressDialog.dismiss();

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();


                }

                else if (Admin.equals("CricketScorer")){
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),ScorerMainActivity.class));
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    //  String message = task.getException().getLocalizedMessage();
                    Toast.makeText(getApplicationContext(), "E R R O R.. ", Toast.LENGTH_LONG).show();
                }


            }

            // @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {





            }
        });



    }

    private void login(){
        String email=editTextAdminEmail.getText().toString().trim();
        String password=editTextAdminPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            editTextAdminEmail.setError("Please Enter Email");
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            editTextAdminPassword.setError("Please enter password");
            return;
        }

        progressDialog.setMessage("Verifying Detail.... ");
        progressDialog.show();

//        //adminAuthentication
//        String Admin;
//        Admin=dataSnapshot.child(firebaseUser).child("UserType").getValue().toString();
//
//        //ending


            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull final Task<AuthResult> task) {
                    firebaseAuth=FirebaseAuth.getInstance();

                    if(task.isSuccessful()) {
                        databaseReference=firebaseDatabase.getReference("admin/");

                        firebaseUser = firebaseAuth.getCurrentUser().getUid();

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            //  @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //  progressDialog.dismiss();

                                String Admin = dataSnapshot.child(firebaseUser).getValue().toString();
                                if (Admin.equals("Admin") && (Admin != null)) {
                                    progressDialog.dismiss();

                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    finish();


                                }

                                else if (Admin.equals("CricketScorer")){
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(),ScorerMainActivity.class));
                                    finish();
                                }
                                else {
                                    progressDialog.dismiss();
                                    //  String message = task.getException().getLocalizedMessage();
                                    Toast.makeText(getApplicationContext(), "E R R O R.. ", Toast.LENGTH_LONG).show();
                                }

//                            if(task.isSuccessful()) {
//
//                                progressDialog.dismiss();
//                                                           }
//
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                databaseError.getMessage();
                                return;




                            }
                        });
                    }
                    else {
                        progressDialog.dismiss();
                        String message = task.getException().getLocalizedMessage();
                        textViewWrongInfo.setText("**Wrong Information**"+message);



                    }

                }
            });






    }

    @Override
    public void onClick(View v) {

        if(v==buttonlogin){
            login();
            //startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
    }
}
