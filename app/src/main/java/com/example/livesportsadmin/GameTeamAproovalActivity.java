package com.example.livesportsadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameTeamAproovalActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView card_cricket,card_volleyball,card_kabaddi,card_other_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_team_aprooval);
        getSupportActionBar().setTitle("Team Approval");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        card_cricket=(CardView)findViewById(R.id.card_TeamApprove_Cricket);
        card_kabaddi=(CardView)findViewById(R.id.card_TeamApprove_Kabaddi);
        card_volleyball=(CardView)findViewById(R.id.card_TeamApprove_VolleyBall);
        card_other_game=(CardView)findViewById(R.id.card_others_games);


        card_volleyball.setOnClickListener(this);
        card_kabaddi.setOnClickListener(this);
        card_cricket.setOnClickListener(this);
        card_other_game.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v==card_cricket){
            startActivity(new Intent(getApplicationContext(),CricketTeamApprovalActivity.class));

        }
        if (v==card_kabaddi){
            Intent i =new Intent(getApplicationContext(),TeamPlayerApprovalActivity.class);

            Bundle bundle=new Bundle();
            bundle.putString("collref","KabaddiTeam");

            i.putExtras(bundle);
            startActivity(i);



        }
        if(v==card_volleyball){
            Intent i =new Intent(getApplicationContext(),TeamPlayerApprovalActivity.class);

            Bundle bundle=new Bundle();
            bundle.putString("collref","VolleyBallTeam");

            i.putExtras(bundle);
            startActivity(i);


        }
        if(v==card_other_game){
            startActivity(new Intent(getApplicationContext(),OtherGamePdfActivity.class));
        }


    }
}
