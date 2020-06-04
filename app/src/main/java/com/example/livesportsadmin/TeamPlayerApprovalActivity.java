package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TeamPlayerApprovalActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerTeamName;
    private Button buttonTeamView,TeamApprove,TeamReject,TeamReview;

    private TextView tp1,tp2,tp3,tp4,tp5,tp6,tp7,tp8,tp9,tp10,tp11,tp12,captionDetail,captianEmail,teamStatus;

    private ImageView pb1,pb2,pb3,pb4,pb5,pb6,pb7,pb8,pb9,pb10,pb11,pb12;

    private AlertDialog progressDialog,dialog;
    private AlertDialog.Builder builder,builderconfirmation;


    private FirebaseFirestore firestore;

    private Bundle bundle;
    private String collref;

    private static final int STORAGE_CODE = 1000 ;

    private Button pdfUpload,pdfSelect,btnpdf;
    private TextView pdfName;
    Uri pdfUri;


    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_player_approval);
        ActivityCompat.requestPermissions(TeamPlayerApprovalActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        bundle=getIntent().getExtras();
        collref=bundle.getString("collref");
        final String Captianmail= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        getSupportActionBar().setTitle(collref);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        spinnerTeamName=(Spinner)findViewById(R.id.TeamListSpinnerGame);
       // buttonTeamView=(Button)findViewById(R.id.);
        progressDialog=new ProgressDialog(TeamPlayerApprovalActivity.this);

        builder=new ProgressDialog.Builder(TeamPlayerApprovalActivity.this);
        LayoutInflater inflater =TeamPlayerApprovalActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(false);
        progressDialog= builder.create();
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(
                android.R.color.transparent
        );
        progressDialog.show();

        captianEmail=(TextView)findViewById(R.id.CaptianEmail);
        captionDetail=(TextView)findViewById(R.id.CaptianDetails);
        teamStatus=(TextView)findViewById(R.id.TeamStatus);

        btnpdf=(Button)findViewById(R.id.btnCreatePDFgame);
        pdfName=(TextView)findViewById(R.id.pdfnamegame);
        pdfSelect=(Button)findViewById(R.id.selectPdfGameTeam);
        pdfUpload=(Button)findViewById(R.id.uploadPdfGameTeam);

        pdfSelect.setOnClickListener(this);
        pdfUpload.setOnClickListener(this);
        btnpdf.setOnClickListener(this);

        tp1=(TextView)findViewById(R.id.gtp1);
        tp2=(TextView)findViewById(R.id.gtp2);
        tp3=(TextView)findViewById(R.id.gtp3);
        tp4=(TextView)findViewById(R.id.gtp4);
        tp5=(TextView)findViewById(R.id.gtp5);
        tp6=(TextView)findViewById(R.id.gtp6);
        tp7=(TextView)findViewById(R.id.gtp7);
        tp8=(TextView)findViewById(R.id.gtp8);
        tp9=(TextView)findViewById(R.id.gtp9);
        tp10=(TextView)findViewById(R.id.gtp10);
        tp11=(TextView)findViewById(R.id.gtp11);
        tp12=(TextView)findViewById(R.id.gtp12);

        pb1=(ImageView)findViewById(R.id.gip1);
        pb2=(ImageView)findViewById(R.id.gip2);
        pb3=(ImageView)findViewById(R.id.gip3);
        pb4=(ImageView)findViewById(R.id.gip4);
        pb5=(ImageView)findViewById(R.id.gip5);
        pb6=(ImageView)findViewById(R.id.gip6);
        pb7=(ImageView)findViewById(R.id.gip7);
        pb8=(ImageView)findViewById(R.id.gip8);
        pb9=(ImageView)findViewById(R.id.gip9);
        pb10=(ImageView)findViewById(R.id.gip10);
        pb11=(ImageView)findViewById(R.id.gip11);
        pb12=(ImageView)findViewById(R.id.gip12);

        pb1.setOnClickListener(this);
        pb2.setOnClickListener(this);
        pb3.setOnClickListener(this);
        pb4.setOnClickListener(this);
        pb5.setOnClickListener(this);
        pb6.setOnClickListener(this);
        pb7.setOnClickListener(this);
        pb8.setOnClickListener(this);
        pb9.setOnClickListener(this);
        pb10.setOnClickListener(this);
        pb11.setOnClickListener(this);
        pb12.setOnClickListener(this);

        TeamApprove=(Button)findViewById(R.id.gameApproveTeam);
        TeamReject=(Button)findViewById(R.id.gameRejectTeam);

        TeamReject.setOnClickListener(this);
        TeamApprove.setOnClickListener(this);



        firestore.enableNetwork();

        final List<String> Teams = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Teams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeamName.setAdapter(adapter);

        firestore.collection(collref)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Teams.add(document.getId());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("TeamData", "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    //  progressDialog.dismiss();
                    // Toast.makeText(CricketTeamApprovalActivity.this, Teams.get(0), Toast.LENGTH_SHORT).show();
                    loadTeamMember(Teams.get(0));
                }

            }
        });
        //spinnerTeamName.setOnItemSelectedListener(new OnCompleteListener<>());
        spinnerTeamName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressDialog.show();
                loadTeamMember(Teams.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



    }

    public void loadTeamMember(String teamname){

        firestore.collection(collref).document(teamname).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

//                ClassTeamMemberName classTeamMemberName=doc.getDocument().toObject(ClassTeamMemberName.class);
                try{



                progressDialog.show();

                String p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p15,p14,mobile,email,status;

                email=documentSnapshot.get("CaptainMail").toString();
                mobile=documentSnapshot.get("CaptainMobile").toString();
                status=documentSnapshot.get("TeamStatus").toString();
                p1=documentSnapshot.get("pl1").toString();
                p2=documentSnapshot.get("pl2").toString();
                p3=documentSnapshot.get("pl3").toString();
                p4=documentSnapshot.get("pl4").toString();
                p5=documentSnapshot.get("pl5").toString();
                p6=documentSnapshot.get("pl6").toString();
                p7=documentSnapshot.get("pl7").toString();
                p8=documentSnapshot.get("pl8").toString();
                p9=documentSnapshot.get("pl9").toString();
                p10=documentSnapshot.get("pl10").toString();
                p11=documentSnapshot.get("pl11").toString();
                p12=documentSnapshot.get("pl12").toString();


                tp1.setText("01: "+ p1);
                tp2.setText("02: "+p2);
                tp3.setText("03: "+p3);
                tp4.setText("04: "+p4);
                tp5.setText("05: "+p5);
                tp6.setText("06: "+p6);
                tp7.setText("07: "+p7);
                tp8.setText("08: "+p8);
                tp9.setText("09: "+p9);
                tp10.setText("10: "+p10);
                tp11.setText("11: "+p11);
                tp12.setText("12: "+p12);


                captianEmail.setText(email);
                teamStatus.setText(status);
                captionDetail.setText(mobile);

                if(status.equals("pending")){
                    teamStatus.setTextColor(Color.parseColor("#FF0000"));
                }


            } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    progressDialog.dismiss();
                }
            }
        });




    }



    public void confermation(final String dbmember,TextView name){
        final String memberName=name.getText().toString();
        builderconfirmation=new AlertDialog.Builder(TeamPlayerApprovalActivity.this);
        builderconfirmation.setTitle("Alert Message");
        builderconfirmation.setMessage("You want to remove "+memberName);
        builderconfirmation.setCancelable(false);




        builderconfirmation.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //  String pl="pl1";
                removemember(dbmember);
                String MemberRemoved=memberName+ "Was Reamoved by admin please update new member.";
                notification(MemberRemoved);

            }
        });
        builderconfirmation.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Operation Failed..", Toast.LENGTH_SHORT).show();

            }
        });

        dialog= builderconfirmation.create();
        dialog.show();




    }

    public void removemember(String member){

        String Teamname=spinnerTeamName.getSelectedItem().toString();
        firestore.collection(collref).document(Teamname).update(member," ");
        FirebaseFirestore.getInstance().collection("user").document(captianEmail.getText().toString()).update("teamMember","Done");
        loadTeamMember(Teamname);




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

    public void notification(String message){
        final String userref=captianEmail.getText().toString();
        FieldValue timestamp=FieldValue.serverTimestamp();
        // String message=(member+" Was Reamoved by admin please update new member.");
        Map<String,Object> notificationMessages=new HashMap<>();
        notificationMessages.put("message",message);
        notificationMessages.put("title",collref);
        notificationMessages.put("timestamp",timestamp);

        Task<DocumentReference> reference = firestore.collection("user/" + userref + "/Notification").add(notificationMessages).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), " Notification Send ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void teamApprove(){

        FirebaseFirestore.getInstance().collection(collref).document(spinnerTeamName.getSelectedItem().toString()).update("TeamStatus","Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), spinnerTeamName.getSelectedItem().toString() +" team approved", Toast.LENGTH_SHORT).show();
                            String Approve ="🎉🎉 HI,"+tp1.getText().toString()+" your team was approved by Admin 🎉🎉";
                            FirebaseFirestore.getInstance().collection("user").document(captianEmail.getText().toString()).update("teamMember","Approved");
                            notification(Approve);
                        }
                    }
                });

    }

    public void teamReject(){

        FirebaseFirestore.getInstance().collection(collref).document((String) spinnerTeamName.getSelectedItem()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), spinnerTeamName.getSelectedItem().toString() +" team removed", Toast.LENGTH_SHORT).show();
                            String Reject = " HI,"+tp1.getText().toString()+" your team was Rejected by Admin. Please contact Admin for any query.";
                            FirebaseFirestore.getInstance().collection("user").document(captianEmail.getText().toString()).update("teamMember","Removed");
                            notification(Reject);
                        }
                    }
                });


    }

    public void savepdf(){

        Document mDoc=new Document();
        String filename=(collref+"_"+ spinnerTeamName.getSelectedItem().toString()+"_2020");
        String filepath= Environment.getExternalStorageDirectory().getPath()  +"/"+ filename +".pdf";

        try {
            PdfWriter.getInstance(mDoc,new FileOutputStream(filepath));
            mDoc.setPageSize(PageSize.A4);
            mDoc.setMargins(70, 70, 70, 70);
            mDoc.setMarginMirroring(true);

            mDoc.open();


            com.itextpdf.text.Rectangle rect=new com.itextpdf.text.Rectangle(577,825,18,15);
            // Rectangle rect= new Rectangle(577,825,18,15);
            rect.enableBorderSide(1);
            rect.enableBorderSide(2);
            rect.enableBorderSide(4);
            rect.enableBorderSide(8);
            rect.setBorder(2);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderColor(BaseColor.BLACK);
            rect.setBorderWidth(2);
            mDoc.add(rect);

            com.itextpdf.text.Font f1= new com.itextpdf.text.Font (com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 13,com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
            com.itextpdf.text.Paragraph p1=new com.itextpdf.text.Paragraph("Sardar Vallabhbhi Patel Education Society's",f1);
            p1.setAlignment(Element.ALIGN_CENTER);
            mDoc.add(p1);

            com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 26, com.itextpdf.text.Font.UNDERLINE, BaseColor.BLACK);
            Paragraph title= new Paragraph("R. N. G. Patel Institute Of Technology",font);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(2.5f);
            mDoc.add(title);

            Paragraph p2=new Paragraph("At. Isroli-afwa,Bardoli-Navsari Road, Ta-Bardoli, Dist. Surat " +
                    "Pin 394620 \n E-mail: rngpit@gmil.com , Website: www.rngpit.ac.in , Ph. 92280 00867",f1);
            p2.setAlignment(Element.ALIGN_CENTER);
            p2.setSpacingAfter(30.5f);
            mDoc.add(p2);

            com.itextpdf.text.Font f2 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 20, com.itextpdf.text.Font.BOLDITALIC, BaseColor.BLACK);
            Paragraph title1=new Paragraph("SPORTS 2020",f2);
            title1.setAlignment(Element.ALIGN_CENTER);
            title1.setSpacingAfter(20.5f);
            mDoc.add(title1);

            Paragraph title2=new Paragraph(collref+" :-"+spinnerTeamName.getSelectedItem().toString(),f2);
            title2.setSpacingAfter(20.5f);

            mDoc.add(title2);

            float[] pointColumn={200f,80f};
            PdfPTable table=new PdfPTable(pointColumn);
            table.setHorizontalAlignment(25);
            table.addCell("NAME");
            table.addCell("MOBILE NUMBER");
            table.addCell(tp1.getText().toString()+"   ©");
            table.addCell(captionDetail.getText().toString());
            table.completeRow();
            table.addCell(tp2.getText().toString());
            table.completeRow();
            table.addCell(tp3.getText().toString());
            table.completeRow();
            table.addCell(tp4.getText().toString());
            table.completeRow();
            table.addCell(tp5.getText().toString());
            table.completeRow();
            table.addCell(tp6.getText().toString());
            table.completeRow();
            table.addCell(tp7.getText().toString());
            table.completeRow();
            table.addCell(tp8.getText().toString());
            table.completeRow();
            table.addCell(tp9.getText().toString());
            table.completeRow();
            table.addCell(tp10.getText().toString());
            table.completeRow();
            table.addCell(tp11.getText().toString());
            table.completeRow();
            table.addCell(tp12.getText().toString());
            table.completeRow();




            mDoc.add(table);

            com.itextpdf.text.Font sign = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 15, com.itextpdf.text.Font.BOLD, BaseColor.GRAY);
            Paragraph signCaptain=new Paragraph("Signature :-",sign);
            Paragraph signature=new Paragraph("Captain:  ___________________   |  HOD: ___________________",sign);
            signCaptain.setAlignment(Element.ALIGN_LEFT);
            signCaptain.setSpacingBefore(30.5f);
            signCaptain.setSpacingAfter(20.5f);

            mDoc.add(signCaptain);
            mDoc.add(signature);















            mDoc.add(new Chunk("\n"));
            //mDoc.add(new LineSeparator(2f,100,BaseColor.DARK_GRAY,Element.ALIGN_CENTER,-1f));

            mDoc.close();

            Toast.makeText(this,filename+".pdf\nis saved to\n"+filepath,Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    savepdf();
                }
                else {
                    Toast.makeText(this,"permission denied...!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void selectPdf() {

        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent,1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK) && (data != null) && data.getData()!=null) {

            pdfUri = data.getData();
            pdfName.setText(data.getData().getLastPathSegment());

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please Select a File..", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadPdf(Uri pdfUri){

        progressDialog=new ProgressDialog(this);
        ((ProgressDialog) progressDialog).setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File..");
        ((ProgressDialog)progressDialog).setCancelable(false);
        ((ProgressDialog) progressDialog).setProgress(0);
        progressDialog.show();
        final String filename=collref+"_2020_"+spinnerTeamName.getSelectedItem().toString()+"_.pdf";
        final StorageReference storageReference=storage.getReference();
        storageReference.child(collref+"Pdf").child(filename).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    String uri=task.getResult().toString();

                                    FirebaseFirestore.getInstance().collection(collref)
                                            .document(captianEmail.getText().toString()).update("Downloadlink",uri)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isComplete()){
                                                        Toast.makeText(TeamPlayerApprovalActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(TeamPlayerApprovalActivity.this,"Error..",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    Log.e("DLink", task.getResult().toString());
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeamPlayerApprovalActivity.this,"Error..",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int currentProgess= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                ((ProgressDialog) progressDialog).setProgress(currentProgess);
                if(currentProgess==100){
                    progressDialog.dismiss();
                }

            }
        });

    }


    @Override
    public void onClick(View v) {

        if(v==pdfSelect){
            if(ContextCompat.checkSelfPermission(TeamPlayerApprovalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

                selectPdf();
            }
            else
            {
                ActivityCompat.requestPermissions(TeamPlayerApprovalActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
        }

        if(v==pdfUpload){
            if(pdfUri!=null){
                uploadPdf(pdfUri);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "please Select a File", Toast.LENGTH_SHORT).show();
            }

        }

        if(v==btnpdf)
        {
            if(Build.VERSION.SDK_INT> Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    String[] permission ={ Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission,STORAGE_CODE);
                }
                else {
                    savepdf();
                }
            }
            else {
                savepdf();
            }
        }

        if(v==pb1){
            String pl="pl1";
            confermation(pl,tp1);
        }
        if(v==pb2){
            String pl="pl2";
            confermation(pl,tp2);
        }
        if(v==pb3){
            String pl="pl3";
            confermation(pl,tp3);
        }
        if(v==pb4){
            String pl="pl4";
            confermation(pl,tp4);
        }
        if(v==pb5){
            String pl="pl5";
            confermation(pl,tp5);
        }
        if(v==pb6){
            String pl="pl6";
            confermation(pl,tp6);
        }
        if(v==pb7){
            String pl="pl7";
            confermation(pl,tp7);
        }
        if(v==pb8){
            String pl="pl8";
            confermation(pl,tp8);
        }
        if(v==pb9){
            String pl="pl9";
            confermation(pl,tp9);
        }
        if(v==pb10){
            String pl="pl10";
            confermation(pl,tp10);
        }
        if(v==pb11){
            String pl="pl11";
            confermation(pl,tp11);
        }
        if(v==pb12){
            String pl="pl12";
            confermation(pl,tp12);
        }

        if (v == TeamApprove) {
            teamApprove();
        }
        if (v == TeamReject) {
            teamReject();
        }

    }
}
