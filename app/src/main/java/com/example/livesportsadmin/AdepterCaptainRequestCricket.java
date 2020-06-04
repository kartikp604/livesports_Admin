package com.example.livesportsadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdepterCaptainRequestCricket extends RecyclerView.Adapter<AdepterCaptainRequestCricket.ViewHolder>  {
    public FirebaseFirestore firebaseFirestore;

    private AlertDialog dialog;
    private AlertDialog.Builder builderconfirmation;


    public List<ClassCaptainRequestCricket> cricketList;
    public Context context;
    public String colref,notimsg;
    public CricketCaptianRequestActivity cricketCaptianRequestActivity;


    public AdepterCaptainRequestCricket(Context context, List<ClassCaptainRequestCricket> requestCricketList, String collref, CricketCaptianRequestActivity cricketCaptianRequestActivity){
        this.cricketList=requestCricketList;
        this.colref="CaptainRequest"+collref;
        this.notimsg=collref;
        this.context=context;
        this.cricketCaptianRequestActivity=cricketCaptianRequestActivity;
    }



    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cricket_captian_request,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.rname.setText(cricketList.get(position).name);
        holder.email.setText(cricketList.get(position).email);

        holder.pen.setText((cricketList).get(position).pen);
        holder.gender.setText(cricketList.get(position).gender);
        holder.clas.setText(cricketList.get(position).batch);
        holder.moNo.setText(cricketList.get(position).getMobile());
        if(cricketList.get(position).Status.equals("pending")){
            holder.status.setText("🔴 "+(cricketList).get(position).Status);
            holder.status.setTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(context, holder.status.getText().toString(), Toast.LENGTH_SHORT).show();

        }
        else
        {
            holder.status.setText("🟢 "+(cricketList).get(position).Status);
            holder.status.setTextColor(Color.parseColor("#29E11A"));

        }

        final String docid=cricketList.get(position).Captainid;

        holder.rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                confirmation(position,docid);
//                cricketList.remove(position);
//                notifyItemRemoved(position);
//
//                firebaseFirestore=FirebaseFirestore.getInstance();
//                DocumentReference dr=firebaseFirestore.collection(colref).document(docid);
//
//
//                dr.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                   if (task.isSuccessful()){
//                       Toast.makeText(context,docid+" Removed Successfully",Toast.LENGTH_LONG).show();
//
//
//                   }
//                   else
//                   {
//
//                   }
//                    }
//                });

            }
        });

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore=FirebaseFirestore.getInstance();
                DocumentReference dr=firebaseFirestore.collection(colref).document(docid);

                FirebaseFirestore.getInstance().collection("user").document(docid).update("userType","Captain");

                dr.update("Status","Captain").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            holder.status.setText("🟢 Captain");
                            holder.status.setTextColor(Color.parseColor("#29E11A"));
                            String message="Your captain request for"+notimsg  +"was approved by admin.Please fill up your team player.thank you";
                            notification(message,docid);
                            Toast.makeText(context,docid+" approve",Toast.LENGTH_LONG).show();
                            FirebaseFirestore.getInstance().collection("user").document(docid).update("teamMember","Pending");

                        }
                    }
                });


            }
        });


    }

    @Override
    public int getItemCount() {
        return cricketList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        View mView;
        public TextView rname,email,gender,clas,moNo,status,pen;
        public Button approve,rem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            pen=(TextView)mView.findViewById(R.id.gameName);
            status=(TextView)mView.findViewById(R.id.CaptainStatus);
            rname=(TextView) mView.findViewById(R.id.nameOfCaptain);
            email=(TextView) mView.findViewById(R.id.emailOfCaptain);
            gender=(TextView) mView.findViewById(R.id.gender);
            clas=(TextView) mView.findViewById(R.id.ClassOfCaptain);
            moNo=(TextView) mView.findViewById(R.id.mobileNumber);

            approve=(Button)mView.findViewById(R.id.acceptReqCricket);
            rem=(Button)mView.findViewById(R.id.rejectReqCricket);


        }
    }
    public void confirmation(final int position, final String docid){
        builderconfirmation=new AlertDialog.Builder(cricketCaptianRequestActivity);
        builderconfirmation.setTitle("Alert Message");
       builderconfirmation.setMessage("You want to delete request for "+cricketList.get(position).name);
        builderconfirmation.setCancelable(false);


        builderconfirmation.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                firebaseFirestore=FirebaseFirestore.getInstance();
                DocumentReference dr=firebaseFirestore.collection(colref).document(docid);


                dr.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            cricketList.remove(position);
                            notifyItemRemoved(position);

                            Toast.makeText(context,docid+" Removed Successfully",Toast.LENGTH_LONG).show();
                            String message=" Captain Request was rejected by Admin.";
                            FirebaseFirestore.getInstance().collection("user").document(docid).update("userType","user");
                            FirebaseFirestore.getInstance().collection("user").document(docid).update("teamMember","pending");
                            notification(message,docid);


                        }
                        else
                        {

                        }
                    }
                });




            }
        });
        builderconfirmation.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Operation Failed..", Toast.LENGTH_SHORT).show();

            }
        });

        dialog= builderconfirmation.create();
        dialog.show();

    }

    public void notification(String message,final String docref){
        FieldValue timestamp= FieldValue.serverTimestamp();

        // String message=(member+" Was Reamoved by admin please update new member.");
        Map<String,Object> notificationMessages=new HashMap<>();
        notificationMessages.put("message",message);
        notificationMessages.put("title","CaptainRequest");
        notificationMessages.put("timestamp",timestamp);


        Task<DocumentReference> reference =FirebaseFirestore.getInstance().collection("user/" + docref + "/Notification").add(notificationMessages).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(cricketCaptianRequestActivity, " Notification Send ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
