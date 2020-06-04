package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
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

public class KabaddiRulesActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextKabaddiRules,editTextGameManagerName,getEditTextGameNumber;
    private Button buttonUpdateKabaddiRules;

    private Button pdfUpload,pdfSelect;
    private TextView pdfName;
    Uri pdfUri;
    ProgressDialog progressDialog;

    private FirebaseStorage storage;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kabaddi_rules);
        getSupportActionBar().setTitle("Add Rules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        editTextGameManagerName=(EditText)findViewById(R.id.editText_Kabaddi_rulesGameManager);
        getEditTextGameNumber=(EditText)findViewById(R.id.editText_Kabaddi_rulesGameManagerMobile);
        editTextKabaddiRules=(EditText)findViewById(R.id.editText_kabaddi_rules);
        buttonUpdateKabaddiRules=(Button)findViewById(R.id.btn_kabaddi_update_rules);

        pdfUpload=(Button)findViewById(R.id.pdfUploadKabaddi);
        pdfSelect=(Button)findViewById(R.id.selectFileKabaddiRules);
        pdfName=(TextView)findViewById(R.id.pdfnameKabaddiball);


        storage=FirebaseStorage.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Gamerules/KabaddiRules/");

        buttonUpdateKabaddiRules.setOnClickListener(this);
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
                FirebaseAuth.getInstance().signOut();
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
        final String KabaddiRules=editTextKabaddiRules.getText().toString();

        databaseReference.child("Rules").setValue(KabaddiRules).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),KabaddiRules+"Successfully Added",Toast.LENGTH_LONG).show();
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
        final String filename="KabaddiRules.pdf";
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
                                                Toast.makeText(KabaddiRulesActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(KabaddiRulesActivity.this,"Error..",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(KabaddiRulesActivity.this,"Error..",Toast.LENGTH_SHORT).show();
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
        if(v==buttonUpdateKabaddiRules){
            update();
        }
        if(v==pdfSelect){
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

                selectPdf();
            }
            else
            {
                ActivityCompat.requestPermissions(KabaddiRulesActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
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
