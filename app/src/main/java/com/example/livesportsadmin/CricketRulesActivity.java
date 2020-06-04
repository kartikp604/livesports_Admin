package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CricketRulesActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextCricketRules,editTextGameManagerName,getEditTextGameNumber;

    private Button buttonUpdateCricketRules;

    private Button pdfUpload,pdfSelect;
    private TextView pdfName;
    Uri pdfUri;
    ProgressDialog progressDialog;

    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_rules);
        getSupportActionBar().setTitle("Add Rules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        editTextGameManagerName=(EditText)findViewById(R.id.editTextGameManager);
        getEditTextGameNumber=(EditText)findViewById(R.id.editTextGameManagerMobile);
        editTextCricketRules=(EditText)findViewById(R.id.editText_cricket_rules);
        buttonUpdateCricketRules=(Button)findViewById(R.id.btn_cricket_update_rules);

        pdfUpload=(Button)findViewById(R.id.pdfUploadCric);
        pdfSelect=(Button)findViewById(R.id.selectFileCricRules);
        pdfName=(TextView)findViewById(R.id.pdfname);

        firebaseAuth=FirebaseAuth.getInstance();

        storage=FirebaseStorage.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Gamerules/CricketRules");

        buttonUpdateCricketRules.setOnClickListener(this);
        pdfSelect.setOnClickListener(this);
        pdfUpload.setOnClickListener(this);

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

    public void update(){
        String managername=editTextGameManagerName.getText().toString();
        String managernumber=getEditTextGameNumber.getText().toString();
        String crickrtRules=editTextCricketRules.getText().toString();

        databaseReference.child("Rules").setValue(crickrtRules).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Successdully Added",Toast.LENGTH_LONG).show();
                }
            }
        });

        databaseReference.child("Manager").setValue(managername);
        databaseReference.child("Number").setValue(managernumber);
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
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File..");
        progressDialog.setProgress(0);
        progressDialog.show();
        final String filename="CricketRules.pdf";
        final StorageReference storageReference=storage.getReference();
        storageReference.child("RulesPdf").child(filename).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            String uri=task.getResult().toString();
                            databaseReference.child("Downloadlink").setValue(uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CricketRulesActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(CricketRulesActivity.this,"Error..",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Log.e("DLink", task.getResult().toString());
                        }
                    }
                });
                //Toast.makeText(CricketRulesActivity.this,uri,Toast.LENGTH_SHORT).show();
              // String uri= storageReference.child("RulesPdf").child(filename).getDownloadUrl().toString();
//               databaseReference.child("Downloadlink").setValue(uri).addOnCompleteListener(new OnCompleteListener<Void>() {
//                   @Override
//                   public void onComplete(@NonNull Task<Void> task) {
//                       if(task.isSuccessful()){
//                           Toast.makeText(CricketRulesActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
//                       }
//                       else
//                       {
//                           Toast.makeText(CricketRulesActivity.this,"Error..",Toast.LENGTH_SHORT).show();
//                       }
//                   }
//               });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CricketRulesActivity.this,"Error..",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int currentProgess= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgess);
                if(currentProgess==100){
                    progressDialog.dismiss();
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v==buttonUpdateCricketRules){
            update();
        }
        if(v==pdfSelect){
            if(ContextCompat.checkSelfPermission(CricketRulesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

                selectPdf();
            }
            else
            {
                ActivityCompat.requestPermissions(CricketRulesActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            selectPdf();

        }

        else
        {
            Toast.makeText(getApplicationContext(), "Please Provide Permission..", Toast.LENGTH_SHORT).show();
        }
    }
}
