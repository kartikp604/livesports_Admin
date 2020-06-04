package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddCricketScheduleActivity extends AppCompatActivity implements View.OnClickListener {


    public Button btnDate,btnTime,update;
    public TextView txtTime,txtDate,scheduleTitle;
    public ImageView refresh;

    public EditText editTextLocation;

    public FirebaseFirestore firestore;
    public StringBuilder stringBuilder;

    public AlertDialog alertDialog;
    public AlertDialog.Builder builder;

    //listview
    private ListView cricketSchedulelist;
    private static final String Tag="FireLog";

    private FirebaseFirestore firebaseFirestore;
    private AdepterScheduleC adepterScheduleC;

    private List<Classcricketschedule> scheduleList;
    //end

    public Spinner sp1,sp2,spgameName;
    ArrayList<String> gamelist;
    ArrayAdapter<String> gameadapter;
    String[] games={"CricketTeam","KabaddiTeam","VolleyBallTeam"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cricket_schedule);
        getSupportActionBar().setTitle("Add Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        btnDate=(Button)findViewById(R.id.btnSelectDate);
        btnTime=(Button)findViewById(R.id.btnSelectTime);
        update=(Button)findViewById(R.id.criSchUpdBtn);
        txtDate=(TextView)findViewById(R.id.txtViewDate);
        txtTime=(TextView)findViewById(R.id.txtViewTime);
        scheduleTitle=(TextView)findViewById(R.id.titleSchedule);

        editTextLocation=(EditText)findViewById(R.id.criLocation);
        refresh=(ImageView)findViewById(R.id.refresh);

        firestore=FirebaseFirestore.getInstance();
        stringBuilder=new StringBuilder();

        sp1=(Spinner)findViewById(R.id.Team1sp);
        sp2=(Spinner)findViewById(R.id.Team2sp);
        spgameName=(Spinner)findViewById(R.id.selectGameforSchedule);

        alertDialog=new ProgressDialog(AddCricketScheduleActivity.this);
        builder=new ProgressDialog.Builder(AddCricketScheduleActivity.this);
        LayoutInflater inflater =AddCricketScheduleActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(false);
        alertDialog= builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(
                android.R.color.transparent
        );
        alertDialog.show();


        gamelist=new ArrayList<String>();
        gameadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,games);
        spgameName.setAdapter(gameadapter);

        spgameName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.show();
                loadteamname();
                scheduleList();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadteamname();

        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        update.setOnClickListener(this);
        refresh.setOnClickListener(this);

        //listview

//        scheduleList=new ArrayList<>();
//        adepterScheduleC=new AdepterScheduleC(getApplicationContext(),scheduleList,spgameName.getSelectedItem().toString());
//
//        cricketSchedulelist=(ListView)findViewById(R.id.listView_Cricket_Schedule);
//        cricketSchedulelist.setHasTransientState(true);
//        cricketSchedulelist.setAdapter(adepterScheduleC);
        firebaseFirestore=FirebaseFirestore.getInstance();




//        firestore.enableNetwork();
//
//        final List<String> Teams = new ArrayList<>();
//        final List<String> Teams2 = new ArrayList<>();
//
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Teams);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp1.setAdapter(adapter);
//
//        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Teams2);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp2.setAdapter(adapter2);
//
//        firestore.collection("CricketTeam")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Teams.add(document.getId());
//                                adapter.notifyDataSetChanged();
//                            }
//                        } else {
//                            Log.d("TeamData", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Teams2.clear();
//                for(int i=0; i < Teams.size(); i++){
//                    if(position == i) continue;
//                    Teams2.add(Teams.get(i));
//                }
//                adapter2.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });

        //ending
    }

    public void scheduleList(){
        String colref=spgameName.getSelectedItem().toString();
        String ScheduleRef = "";
        //colref
        if(colref.equals("CricketTeam"))
        {
            ScheduleRef="CricketSchedule";

        }
        if (colref.equals("VolleyBallTeam"))
        {
            ScheduleRef="VolleyBallSchedule";

        }
        if (colref.equals("KabaddiTeam"))
        {
            ScheduleRef="KabaddiSchedule";

        }
        //
        scheduleList=new ArrayList<>();
        adepterScheduleC=new AdepterScheduleC(getApplicationContext(),scheduleList,ScheduleRef);

        cricketSchedulelist=(ListView)findViewById(R.id.listView_Cricket_Schedule);
        cricketSchedulelist.setHasTransientState(true);
        cricketSchedulelist.setAdapter(adepterScheduleC);


        firebaseFirestore.collection(ScheduleRef).orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if(e != null){

                    Log.d(Tag,"Error"+e.getMessage());
                }

                for(DocumentChange doc :queryDocumentSnapshots.getDocumentChanges()){

                    if(doc.getType()==DocumentChange.Type.ADDED)
                    {

                        Classcricketschedule classcricketschedule=doc.getDocument().toObject(Classcricketschedule.class).withId(doc.getDocument().getId());

                        scheduleList.add(classcricketschedule);
                        adepterScheduleC.notifyDataSetChanged();

                    }
                    if(doc.getType()==DocumentChange.Type.REMOVED){
                        adepterScheduleC.notifyDataSetChanged();

                    }


                }

            }
        });
    }

    public void loadteamname(){
        firestore.enableNetwork();

        final List<String> Teams = new ArrayList<>();
        final List<String> Teams2 = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Teams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Teams2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);
        String gameTeamRef=spgameName.getSelectedItem().toString();
        scheduleTitle.setText(gameTeamRef+" Schedule");
        firestore.collection(gameTeamRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Teams.add(document.getId());
                                adapter.notifyDataSetChanged();
                                alertDialog.dismiss();
                            }
                        } else {
                            Log.d("TeamData", "Error getting documents: ", task.getException());
                        }
                    }
                });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Teams2.clear();
                for(int i=0; i < Teams.size(); i++){
                    if(position == i) continue;
                    Teams2.add(Teams.get(i));
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void handleDateButton(){
        final int year,month,date;

        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        date=calendar.get(Calendar.DATE);


        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                String DateString=dayOfMonth+" / "+month+" / "+year;
////                txtDate.setText(DateString);

                Calendar calendar1=Calendar.getInstance();
                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.DATE,dayOfMonth);

                CharSequence dateCharSeq= DateFormat.format("EEEE, dd MMM yyyy",calendar1);
                txtDate.setText(dateCharSeq);




            }
        }, year, month, date);
        datePickerDialog.show();




    }

    public void handleTimeButton(){

        Calendar calendar=Calendar.getInstance();
        int hour,minute;
        hour=calendar.get(Calendar.HOUR);
        minute=calendar.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String TimeString=hourOfDay+":"+minute;
                txtTime.setText(TimeString);

            }
        },hour,minute,true);
        timePickerDialog.show();
    }

    public void updateschedule(String ScheduleRef){
        FieldValue timestamp=FieldValue.serverTimestamp();
        int flag=0;


        String gameRef;
        final String[] team1capmail = new String[1];
        final String[] team2capMail = new String[1];
        final String date,time,location,team1,team2;
        team1=sp1.getSelectedItem().toString();
        team2=sp2.getSelectedItem().toString();
         date=txtDate.getText().toString();
         time=txtTime.getText().toString();
         location=editTextLocation.getText().toString();


        if(TextUtils.isEmpty(location)){

            editTextLocation.setError("Please Enter Location");
            flag=1;
        }


         if(TextUtils.isEmpty(date)){



             txtDate.setError("Please Select Date");
          //   return;
             flag=1;

         }

         if(TextUtils.isEmpty(time)){

            txtTime.setError("Please Select Time");


            flag=1;
         }

        Map<String, Object> data = new HashMap<>();
         data.put("Team1",team1);
         data.put("Team2",team2);
        data.put("Date", date);
        data.put("Location",location);
        data.put("Time", time);
        data.put("timestamp",timestamp);

        if(flag==0) {

            CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(ScheduleRef);
            Task<DocumentReference> documentReference = collectionReference.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(AddCricketScheduleActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();
        }
        gameRef=spgameName.getSelectedItem().toString();




            FirebaseFirestore.getInstance().collection(gameRef).document(team1).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    try {
                        team1capmail[0] = documentSnapshot.get("CaptainMail").toString();
                    }
                    catch (Exception e){

                        Toast.makeText(AddCricketScheduleActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    notification(team1capmail[0], date, team2);
                }
            });
            FirebaseFirestore.getInstance().collection(gameRef).document(team2).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    team2capMail[0] = documentSnapshot.get("CaptainMail").toString();

                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    notification(team2capMail[0], date, team1);
                }
            });




    }

    public void notification(String capMail,String date,String team){
        FieldValue timestamp=FieldValue.serverTimestamp();

        String message=("your next match will be Schedule on "+date+" with "+team);
        Map<String,Object> notificationMessages=new HashMap<>();
        notificationMessages.put("message",message);
        notificationMessages.put("title",spgameName.getSelectedItem().toString()+"Schedule");
        notificationMessages.put("timestamp",timestamp);

        Task<DocumentReference> reference =FirebaseFirestore.getInstance().collection("user/" + capMail+ "/Notification").add(notificationMessages).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), " Notification Send ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v==btnDate){

            handleDateButton();
        }

        if(v==btnTime){

            handleTimeButton();
        }

        if (v==refresh){
            scheduleList();
        }

        if(v==update){

            String ref=spgameName.getSelectedItem().toString();
            String ScheduleRef=null;
            if(ref.equals("CricketTeam"))
            {
                ScheduleRef="CricketSchedule";
                updateschedule(ScheduleRef);
            }
            if (ref.equals("VolleyBallTeam"))
            {
                ScheduleRef="VolleyBallSchedule";
                updateschedule(ScheduleRef);
            }
            if (ref.equals("KabaddiTeam"))
            {
                ScheduleRef="KabaddiSchedule";
                updateschedule(ScheduleRef);
            }

        }
    }
}
