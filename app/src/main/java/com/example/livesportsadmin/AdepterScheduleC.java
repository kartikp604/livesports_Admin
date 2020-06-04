package com.example.livesportsadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdepterScheduleC extends BaseAdapter {


    public FirebaseFirestore firebaseFirestore;
    public List<Classcricketschedule> scheduleList;
    public Context context;
    public String collref;

    public AdepterScheduleC(Context context,List<Classcricketschedule>scheduleList,String collref){
        this.collref=collref;
        this.context=context;
        this.scheduleList=scheduleList;

    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_view_layout,null);
        final String docid=scheduleList.get(position).Scheduleid;
        Button remove=view.findViewById(R.id.removeschedule);
        TextView team1=view.findViewById(R.id.textTeamA);
        TextView team2=view.findViewById(R.id.textTeamB);
        TextView time=view.findViewById(R.id.textTime);
        TextView date=view.findViewById(R.id.textDate);
        TextView location=view.findViewById(R.id.textLocation);

        team1.setText(scheduleList.get(position).getTeam1());
        team2.setText(scheduleList.get(position).getTeam2());
        time.setText("Time: "+scheduleList.get(position).getTime());
        date.setText("Date: "+scheduleList.get(position).getDate());
        location.setText("Location: "+scheduleList.get(position).getLocation());

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleList.remove(position);
                notifyDataSetChanged();
                firebaseFirestore= FirebaseFirestore.getInstance();
                DocumentReference dr=firebaseFirestore.collection(collref).document(docid);

//                Toast.makeText(context,docid,Toast.LENGTH_LONG).show();
                dr.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context,docid+" Removed Successfully",Toast.LENGTH_LONG).show();


                        }
                        else
                        {

                        }
                    }
                });

            }
        });


        return (view);
    }


}
