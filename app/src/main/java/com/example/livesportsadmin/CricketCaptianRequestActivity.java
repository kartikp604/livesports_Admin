package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CricketCaptianRequestActivity extends AppCompatActivity {


    private RecyclerView CricketCaptainRequest;
    private Spinner spGame;

    private FirebaseFirestore firebaseFirestore;

    private List<ClassCaptainRequestCricket>requestCricketList;
    private AdepterCaptainRequestCricket adepterCaptainRequestCricket;

    public AlertDialog alertDialog;
    public AlertDialog.Builder builder;

    ArrayList<String> gamelist;
    ArrayAdapter<String> gameadapter;
    String[] games={"Cricket","Kabaddi","VolleyBall"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_captian_request);
        getSupportActionBar().setTitle("Captain Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spGame=(Spinner)findViewById(R.id.SpinnerGameforCapReq);

        alertDialog=new ProgressDialog(CricketCaptianRequestActivity.this);
        builder=new ProgressDialog.Builder(CricketCaptianRequestActivity.this);
        LayoutInflater inflater =CricketCaptianRequestActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(false);
        alertDialog= builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(
                android.R.color.transparent
        );
      //  alertDialog.show();


        gamelist=new ArrayList<String>();
        gameadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,games);
        spGame.setAdapter(gameadapter);
        firebaseFirestore=FirebaseFirestore.getInstance();


        spGame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadcaptain();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    public void loadcaptain(){

        String ref="CaptainRequest"+spGame.getSelectedItem().toString();


        requestCricketList=new ArrayList<>();
        adepterCaptainRequestCricket=new AdepterCaptainRequestCricket(getApplicationContext(),requestCricketList,spGame.getSelectedItem().toString(),CricketCaptianRequestActivity.this);




        CricketCaptainRequest=(RecyclerView) findViewById(R.id.RecViewCricketCaptain);

        CricketCaptainRequest.setHasFixedSize(true);
        CricketCaptainRequest.setLayoutManager(new LinearLayoutManager(this));
        CricketCaptainRequest.setAdapter(adepterCaptainRequestCricket);

        firebaseFirestore.collection(ref).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){


                    if(doc.getType()==DocumentChange.Type.ADDED){

                        ClassCaptainRequestCricket captainRequestCricket=doc.getDocument().toObject(ClassCaptainRequestCricket.class).withId(doc.getDocument().getId());
                        requestCricketList.add(captainRequestCricket);

                        adepterCaptainRequestCricket.notifyDataSetChanged();

                    }
                    if(doc.getType()==DocumentChange.Type.REMOVED){
                        adepterCaptainRequestCricket.notifyDataSetChanged();

                    }

                }
            }
        });


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
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //menu Ending
}
