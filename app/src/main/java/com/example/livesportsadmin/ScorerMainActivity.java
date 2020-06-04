package com.example.livesportsadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScorerMainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView EditTextRuns, EditTextWicket, EditTextOvers, txtrunrate, txtrun1, txtrun2, overrun;
    private EditText team1, team2, WinnerTeam;
    private Button BtnUpdate, btnbat, btnball, noball, wideball,report_btn;
    private RadioButton radiobtn1, radiobtn2;
    private RadioGroup radioButton;
    private Button BtnReset;
    ValueEventListener listener;
    final List<String> team = new ArrayList<String>();

    Spinner sp;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerData;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1, databaseReferencetp;
    private DatabaseReference databaseReferencesp, databaseReferencerd, dbreference;

    int Runs = 0, Wicket = 0, balls = 0, fteam1 = 0, fteam2 = 0, runbt1 = 0, runbt2 = 0,ib=0;
    float Over = 0, RunRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scorer);
        getSupportActionBar().setTitle("Scorer");

        firebaseAuth = FirebaseAuth.getInstance();

        sp = (Spinner) findViewById(R.id.spin);
        btnbat = (Button) findViewById(R.id.btman);
        report_btn=(Button)findViewById(R.id.report_Cricket);
        radioButton = (RadioGroup) findViewById(R.id.radiogroup);
        radiobtn1 = (RadioButton) findViewById(R.id.btman1);
        radiobtn2 = (RadioButton) findViewById(R.id.btman2);
        EditTextRuns = (TextView) findViewById(R.id.EditRuns);
        EditTextOvers = (TextView) findViewById(R.id.EditOver);
        overrun = (TextView) findViewById(R.id.overrun);
        EditTextWicket = (TextView) findViewById(R.id.EditWicket);
        BtnReset = (Button) findViewById(R.id.btnReset);
        txtrunrate = (TextView) findViewById(R.id.runrate);
        BtnUpdate = (Button) findViewById(R.id.btnupdate);
        btnball = (Button) findViewById(R.id.bowler);
        team1 = (EditText) findViewById(R.id.team1id);
        team2 = (EditText) findViewById(R.id.team2id);
        txtrun1 = (TextView) findViewById(R.id.bt1);
        txtrun2 = (TextView) findViewById(R.id.bt2);
      //  WinnerTeam = (EditText) findViewById(R.id.winnerTeam);

        overrun.setText(" ");

        databaseReferencesp = FirebaseDatabase.getInstance().getReference("team");
        BtnReset.setOnClickListener(this);
        btnbat.setOnClickListener(this);
        btnball.setOnClickListener(this);
        BtnUpdate.setOnClickListener(this);
        report_btn.setOnClickListener(this);


        spinnerData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerData);
        sp.setAdapter(adapter);
        retrivedata();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Cricket/Team1");
        databaseReference1 = firebaseDatabase.getReference("Cricket/Team2");
        databaseReferencerd = firebaseDatabase.getReference("Cricket/Batsman");
        databaseReferencetp = firebaseDatabase.getReference("Cricket/ThisOver");
        dbreference = firebaseDatabase.getReference("Cricket/Player");

        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bt1name = dataSnapshot.child("Batsman1").getValue().toString();
                String bt2name = dataSnapshot.child("Batsman2").getValue().toString();

                radiobtn1.setText(bt1name);
                radiobtn2.setText(bt2name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    fteam1 = 1;
                    fteam2 = 0;
                } else {
                    fteam2 = 1;
                    fteam1 = 0;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    public void retrivedata() {
        listener = databaseReferencesp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    spinnerData.add(item.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void display(int score, int runbt1, int runbt2) {
        TextView scoreView = (TextView) findViewById(R.id.EditRuns);
        scoreView.setText(String.valueOf(score));
        txtrun1.setText(String.valueOf(runbt1));
        txtrun2.setText(String.valueOf(runbt2));
        updatesc();
    }

    public void zerorun(View v){
        Over(v);
        if(ib<7){

            recentrun("0️⃣ ");
            ib=ib+1;
        }
        if(ib==7){
            ib=0;
            overrun.setText(" ");
            recentrun("0️⃣ ");
            ib=ib+1;

        }
      //  Toast.makeText(this, ib, Toast.LENGTH_SHORT).show();

    }

    public void onerun(View v) {
        if (radioButton.getCheckedRadioButtonId() == R.id.btman1) {
            runbt1 = runbt1 + 1;
        } else {
            runbt2 = runbt2 + 1;
        }
        if(ib<7){

            recentrun(" 1️⃣ ");
            ib=ib+1;
        }
        if(ib==7){
            ib=0;
            overrun.setText(" ");
            recentrun(" 1️⃣ ");
            ib=ib+1;
        }
        Runs = Runs + 1;
        display(Runs, runbt1, runbt2);
        Over(v);
        //Toast.makeText(ScorerMainActivity.this, ib, Toast.LENGTH_SHORT).show();

    }

    public void tworun(View v) {
        if (radioButton.getCheckedRadioButtonId() == R.id.btman1) {
            runbt1 = runbt1 + 2;
        } else {
            runbt2 = runbt2 + 2;
        }
        if(ib<7){

            recentrun(" 2️⃣ ");
            ib=ib+1;
        }
        if(ib==7){
            ib=0;
            overrun.setText(" ");
            recentrun(" 2️⃣ ");
            ib=ib+1;

        }

        Over(v);
        Runs = Runs + 2;
        display(Runs, runbt1, runbt2);
        //Toast.makeText(this, ib, Toast.LENGTH_SHORT).show();

    }

    public void threerun(View v) {
        if (radioButton.getCheckedRadioButtonId() == R.id.btman1) {
            runbt1 = runbt1 + 3;
        } else {
            runbt2 = runbt2 + 3;
        }
        if(ib<7){

            recentrun(" 3️⃣ ");
            ib=ib+1;
        }
        if(ib==7){
            ib=0;
            overrun.setText(" ");
            recentrun(" 3️⃣ ");
            ib=ib+1;

        }
        Over(v);
        Runs = Runs + 3;
        display(Runs, runbt1, runbt2);
        //Toast.makeText(this, ib, Toast.LENGTH_SHORT).show();

    }

    public void fourrun(View v) {
        if (radioButton.getCheckedRadioButtonId() == R.id.btman1) {
            runbt1 = runbt1 + 4;
        } else {
            runbt2 = runbt2 + 4;
        }
        if(ib<7){

            recentrun(" 4️⃣ ");
            ib=ib+1;
        }
        if(ib==7){
            ib=0;
            overrun.setText(" ");
            recentrun(" 4️⃣ ");
            ib=ib+1;

        }
        Over(v);
        Runs = Runs + 4;
        display(Runs, runbt1, runbt2);
      //  Toast.makeText(this, ib, Toast.LENGTH_SHORT).show();

    }

    public void sixrun(View v) {
        if (radioButton.getCheckedRadioButtonId() == R.id.btman1) {
            runbt1 = runbt1 + 6;
        } else {
            runbt2 = runbt2 + 6;
        }
        if(ib<7){

            recentrun(" 6️⃣ ");
            ib=ib+1;
        }
        if(ib==7){
            ib=0;
            overrun.setText(" ");
            recentrun(" 6️⃣ ");
            ib=ib+1;

        }
        Over(v);
        Runs = Runs + 6;
        display(Runs, runbt1, runbt2);
        //Toast.makeText(this, ib, Toast.LENGTH_SHORT).show();

    }

    public void noball(View v) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mview = getLayoutInflater().inflate(R.layout.activity_dialog, null);
        final EditText txtinput = (EditText) mview.findViewById(R.id.addrun);
        Button btn_add = (Button) mview.findViewById(R.id.add);

        alert.setView(mview);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nbrun = txtinput.getText().toString();
                int Run = Integer.parseInt(nbrun);

                Runs = Runs + 1 + Run;
                display(Runs, runbt1, runbt2);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        recentrun(" 🚫 ");
    }

    public void wideball(View v) {
        Runs = Runs + 1;
        display(Runs, runbt1, runbt2);
        recentrun(" WD ");
    }

    public void displayWicket(int Wicket,int runbt1,int runbt2) {
        TextView scoreView = (TextView) findViewById(R.id.EditWicket);
        scoreView.setText(String.valueOf(Wicket));
        txtrun1.setText(String.valueOf(runbt1));
        txtrun2.setText(String.valueOf(runbt2));
    }

    public void Wicket(View v) {
        if (Wicket < 10) {
            Over(v);
            Wicket = Wicket + 1;

            if(ib<7){
                ib++;
                recentrun(" out ");
            }
            if(ib==7){
                ib=0;
                overrun.setText(" ");
                recentrun(" out ");

            }

            if (radioButton.getCheckedRadioButtonId() == R.id.btman1) {
                txtrun1.setText(String.valueOf(0));
                runbt1 = 0;
                displayWicket(Wicket, runbt1, runbt2);
            } else {
                txtrun2.setText(String.valueOf(0));
                runbt2 = 0;
                displayWicket(Wicket, runbt1, runbt2);
            }

        }
        updatesc();
    }

    public void DisplayRunrate(float RunRate) {
        TextView scoreView = (TextView) findViewById(R.id.runrate);
        float round = Math.round(RunRate);
        String myNumberString = String.format("%1.2f", Float.valueOf(RunRate));
        scoreView.setText(myNumberString);
    }

    public void runrate(View v) {
        String run1=EditTextRuns.getText().toString();
        float Runs1=Float.parseFloat(run1);

        String over1=EditTextOvers.getText().toString();
        float Over1=Float.parseFloat(over1);

        RunRate=Runs1/Over1;
        DisplayRunrate(RunRate);
    }

    public void displayOver(float Over) {
        TextView scoreView = (TextView) findViewById(R.id.EditOver);
        float round = Math.round(Over);
        String myNumberString = String.format("%1.1f", Float.valueOf(Over));
        scoreView.setText(myNumberString);
        updatesc();
    }

    public void Over(View v) {
        if (Over < 20) {
            if (balls == 5) {
                balls = 0;
                Over += 1;
                Over -= 0.5;
               //
            } else {
                Over += 0.1;
                balls++;
            }
            displayOver(Over);
        }
//        if(ib<7){
//            ib++;
//        }
//        if(ib==7){
//            ib=0;
//            overrun.setText(" ");
//
//        }
    }

    public void updatesc() {
        String Runs = EditTextRuns.getText().toString();
        String Wickets = EditTextWicket.getText().toString();
        String Overs = EditTextOvers.getText().toString();
        String runrate = txtrunrate.getText().toString();
        String Team1 = team1.getText().toString();
        String Team2 = team2.getText().toString();
      //  String winner=WinnerTeam.getText().toString();
        String ibrun=overrun.getText().toString();

        batsmanrun();

        if (fteam1 == 1 && fteam2 == 0) {

            databaseReference.child("Overs").setValue(Overs).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_LONG).show();
                    }
                }
            });
            databaseReference.child("Runs").setValue(Runs);
            databaseReference.child("Wickets").setValue(Wickets);
            databaseReference.child("RunRate").setValue(runrate);

        } else if (fteam2 == 1 && fteam1 == 0) {
            databaseReference1.child("Overs").setValue(Overs).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Successfully added", Toast.LENGTH_LONG).show();
                    }
                }
            });
            databaseReference1.child("Runs").setValue(Runs);
            databaseReference1.child("Wickets").setValue(Wickets);
            databaseReference1.child("RunRate").setValue(runrate);
        }
        databaseReferencetp.setValue(ibrun);
        databaseReferencesp.child("team1").setValue(Team1);
        databaseReferencesp.child("team2").setValue(Team2);
        //databaseReferencerd.child("winner").setValue(winner);

        if (TextUtils.isEmpty(Team1)){
            team1.setError("Please Enter team name");
        }
        if (TextUtils.isEmpty(Team2)){
            team2.setError("Please Enter team name");
        }
    }

    private void batsmanrun(){

        String btsrun1=txtrun1.getText().toString();
        String btsrun2=txtrun2.getText().toString();

        databaseReferencerd.child("runbatsman1").setValue(btsrun1);
        databaseReferencerd.child("runbatsman2").setValue(btsrun2);
    }

    private void resetsc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditTextRuns.setText("0");
                EditTextOvers.setText("0");
                EditTextWicket.setText("0");
                txtrunrate.setText("0");
                txtrun1.setText("0");
                txtrun2.setText("0");
                overrun.setText(" ");
                Runs = 0;
                Wicket = 0;
                Over = 0;
                RunRate=0;
                runbt2=0;
                runbt1=0;
                ib=0;
            }
        }).setNegativeButton("cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        CreateDialog();
    }

    private void CreateDialog() {
        AlertDialog.Builder alrtdlg = new AlertDialog.Builder(this);
        alrtdlg.setMessage("Do you want to Exit?");
        alrtdlg.setCancelable(false);
        alrtdlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ScorerMainActivity.super.onBackPressed();
            }
        });
        alrtdlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alrtdlg.create().show();
    }

    public void recentrun(String ballrun){
        String rerun=overrun.getText().toString();
        overrun.setText(rerun+""+ballrun);
        //databaseReferencetp.setValue(rerun);
        updatesc();
    }


    public void report(){

        final String[] t1Run = new String[1];
        final String[] t1Wicket = new String[1];
        final String[] t1Over = new String[1];
        final String[] t2Run = new String[1];
        final String[] t2Wicket = new String[1];
        final String[] t2Over = new String[1];


        FirebaseDatabase.getInstance().getReference("Cricket").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                t1Over[0] =dataSnapshot.child("Team1").child("Overs").getValue().toString();
                t1Run[0] =dataSnapshot.child("Team1").child("Runs").getValue().toString();
                t1Wicket[0] =dataSnapshot.child("Team1").child("Wickets").getValue().toString();
                t2Over[0] =dataSnapshot.child("Team2").child("Overs").getValue().toString();
                t2Run[0] =dataSnapshot.child("Team2").child("Runs").getValue().toString();
                t2Wicket[0] =dataSnapshot.child("Team2").child("Wickets").getValue().toString();

                Map<Object,String> value=new HashMap<>();
                value.put("t1Over",t1Over[0]);
                value.put("t1Run",t1Run[0]);
                value.put("t1Wicket",t1Wicket[0]);

                value.put("t2Over",t2Over[0]);
                value.put("t2Run",t2Run[0]);
                value.put("t2Wicket",t2Wicket[0]);



                FirebaseFirestore.getInstance().collection("CricketReport").document("name").set(value);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onClick(View view) {
        if (view == BtnUpdate) {
            updatesc();
        }
        if (view == BtnReset) {
            resetsc();
        }
        if (view == btnball) {
            startActivity(new Intent(getApplicationContext(), BallerActivity.class));
        }

        if (view==btnbat){
            startActivity(new Intent(getApplicationContext(),BatsActivity.class));
        }

        if(view==report_btn){
            report();
        }
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

}