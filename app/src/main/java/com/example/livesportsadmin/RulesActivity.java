package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RulesActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewCricketRules,imageViewVolleyballRules,imageViewKabaddiRules,imageViewChessRules;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        getSupportActionBar().setTitle("Add Rules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        firebaseAuth=FirebaseAuth.getInstance();
//        //Authenticate User
//
//        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
//
//        if(firebaseAuth.getCurrentUser() == null)
//        {
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//        }
//        //ending
//
        imageViewCricketRules=(ImageView)findViewById(R.id.cricket_rules);
        imageViewChessRules=(ImageView)findViewById(R.id.Chess_rules);
        imageViewKabaddiRules=(ImageView)findViewById(R.id.Kabaddi_rules);
        imageViewVolleyballRules=(ImageView) findViewById(R.id.Volley_Ball_rules);



        imageViewCricketRules.setOnClickListener(this);
        imageViewVolleyballRules.setOnClickListener(this);
        imageViewKabaddiRules.setOnClickListener(this);
        imageViewChessRules.setOnClickListener(this);

    }
    //MenuStarting
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Home:
                finish();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                break;

            case R.id.LogOut:

               firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //menu Ending


    @Override
    public void onClick(View v) {
        if(v==imageViewCricketRules){
            startActivity(new Intent(getApplicationContext(),CricketRulesActivity.class));

        }
        if(v==imageViewChessRules){
            startActivity(new Intent(getApplicationContext(),CricketRulesActivity.class));

        }
        if(v==imageViewKabaddiRules){
            startActivity(new Intent(getApplicationContext(),KabaddiRulesActivity.class));

        }
        if(v==imageViewVolleyballRules){
            startActivity(new Intent(getApplicationContext(),VolleyballRulesActivity.class));

        }
    }
}
