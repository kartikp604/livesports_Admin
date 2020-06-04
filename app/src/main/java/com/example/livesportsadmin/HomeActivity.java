package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView imageViewRules,imageViewRegOnOff,imageViewGameSchedule,imageViewTeamApproval,imageViewSendNotification;
    private LinearLayout cricketbtn;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");


//Authenticate User

    firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        //ending

        imageViewRules=(ImageView) findViewById(R.id.rules);
        imageViewGameSchedule=(ImageView)findViewById(R.id.image_GameSchedule);
        imageViewRegOnOff=(ImageView)findViewById(R.id.image_Reg_on_off);
        imageViewTeamApproval=(ImageView)findViewById(R.id.TeamApproval);
        imageViewSendNotification=(ImageView)findViewById(R.id.image_notification);
        cricketbtn=(LinearLayout)findViewById(R.id.cricketRequestbtn);

        imageViewRules.setOnClickListener(this);
        imageViewRegOnOff.setOnClickListener(this);
        imageViewGameSchedule.setOnClickListener(this);
        imageViewTeamApproval.setOnClickListener(this);
        imageViewSendNotification.setOnClickListener(this);

        cricketbtn.setOnClickListener(this);







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
        if(v==imageViewRules){
            startActivity(new Intent(getApplicationContext(),RulesActivity.class));
        }
        if(v==imageViewRegOnOff){
            startActivity(new Intent(getApplicationContext(),RegOnOffActivity.class));

        }

        if(v==cricketbtn){
            startActivity(new Intent(getApplicationContext(),CricketCaptianRequestActivity.class));
        }
        if(v==imageViewGameSchedule){
            startActivity(new Intent(getApplicationContext(),AddCricketScheduleActivity.class));
        }
        if(v==imageViewTeamApproval){
            startActivity(new Intent(getApplicationContext(),GameTeamAproovalActivity.class));
        }
        if (v==imageViewSendNotification){
            startActivity(new Intent(getApplicationContext(),WebNotificationActivity.class));
        }
    }
}
