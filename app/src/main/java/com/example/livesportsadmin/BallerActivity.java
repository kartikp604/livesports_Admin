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

public class BallerActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtname1;
    private Button update;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase fbdatabse;
    private DatabaseReference dbrefernce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baller);
        getSupportActionBar().setTitle("Baller");

        firebaseAuth= FirebaseAuth.getInstance();

        txtname1=(EditText)findViewById(R.id.txtbname1);
        update= (Button)findViewById(R.id.update);
        update.setOnClickListener(this);
        fbdatabse= FirebaseDatabase.getInstance();
        dbrefernce=fbdatabse.getReference("Cricket/Player");
    }

    public void updatebl(){
        String baller=txtname1.getText().toString();
        dbrefernce.child("Baller1").setValue(baller).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Successfully added",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==update){
            updatebl();
        }
    }
}