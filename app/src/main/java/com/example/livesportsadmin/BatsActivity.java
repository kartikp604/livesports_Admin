package com.example.livesportsadmin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BatsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="BatsActivity" ;
    private EditText txtbat1,txtbat2;
    private Button updatebt;

    private FirebaseDatabase firebaseDatabasebt;
    private DatabaseReference dbreference1,databaseReferencerd;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bats);

        fbAuth= FirebaseAuth.getInstance();

        txtbat1=(EditText)findViewById(R.id.btm1name);
        txtbat2=(EditText)findViewById(R.id.btm2name);
        updatebt=(Button)findViewById(R.id.updatebt);

        updatebt.setOnClickListener(this);

        firebaseDatabasebt= FirebaseDatabase.getInstance();
        dbreference1=firebaseDatabasebt.getReference("Cricket/Player");
    }

    public void updatebatsman(){
        String Batsman1=txtbat1.getText().toString();
        String Batsman2=txtbat2.getText().toString();
        dbreference1.child("Batsman1").setValue(Batsman1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Successfully added",Toast.LENGTH_LONG).show();
                }
            }
        });
        dbreference1.child("Batsman2").setValue(Batsman2);
    }

    @Override
    public void onClick(View v) {
        if (v==updatebt){
            updatebatsman();
        }
    }
}