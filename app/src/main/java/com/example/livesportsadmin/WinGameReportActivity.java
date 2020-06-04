package com.example.livesportsadmin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WinGameReportActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000 ;
    EditText text1,text2;
    Button sbtn;
    DatabaseReference db;
    DatabaseReference cwinnerteam,crunnersup,kwinnerteam,krunnersup,vwinnerteam,vrunnersup;
    String cwinner_team,crunnersup_team,krunnersup_team,kwinner_team,vwinner_team,vrunnersup_team;
    TextView tv1,tv2,tv11,tv12,tv21,tv22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1=(EditText)findViewById(R.id.et_text1);
        text2=(EditText)findViewById(R.id.et_text2);
        sbtn=(Button)findViewById(R.id.savebt);
        tv1=(TextView)findViewById(R.id.textview1);
        tv2=(TextView)findViewById(R.id.textview2);
        tv11=(TextView)findViewById(R.id.textview11);
        tv12=(TextView)findViewById(R.id.textview12);
        tv21=(TextView)findViewById(R.id.textview21);
        tv22=(TextView)findViewById(R.id.textview22);
        db= FirebaseDatabase.getInstance().getReference();
        cwinnerteam=db.child("Cricket").child("winner_team");
        crunnersup=db.child("Cricket").child("runnersup_team");
        kwinnerteam= db.child("Kabaddi").child("winner_team");
        krunnersup=db.child("Kabaddi").child("runnersup_team");
        vwinnerteam=db.child("Volleyball").child("winner_team");
        vrunnersup=db.child("Volleyball").child("runnersup_team");

        ActivityCompat.requestPermissions(WinGameReportActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);


        cwinnerteam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d("teams",dataSnapshot.getValue(String.class));
                cwinner_team=dataSnapshot.getValue(String.class);
                tv1.setText(cwinner_team);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        crunnersup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                crunnersup_team=dataSnapshot.getValue(String.class);
                tv2.setText(crunnersup_team);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        kwinnerteam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d("teams",dataSnapshot.getValue(String.class));
                kwinner_team=dataSnapshot.getValue(String.class);
                tv11.setText(kwinner_team);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        krunnersup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                krunnersup_team=dataSnapshot.getValue(String.class);
                tv12.setText(krunnersup_team);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        vwinnerteam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d("teams",dataSnapshot.getValue(String.class));
                vwinner_team=dataSnapshot.getValue(String.class);
                tv21.setText(vwinner_team);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        vrunnersup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vrunnersup_team=dataSnapshot.getValue(String.class);
                tv22.setText(vrunnersup_team);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

    }

    private void savepdf() {

        Document mDoc=new Document();
        String filename="2020SportsReport"+new  SimpleDateFormat("dd-MM-YYYY",
                Locale.getDefault()).format(System.currentTimeMillis());
        String filepath= Environment.getExternalStorageDirectory().getPath()  +"/"+ filename +".pdf";

        try {
            PdfWriter.getInstance(mDoc,new FileOutputStream(filepath));
            mDoc.setPageSize(PageSize.A4);
            mDoc.setMargins(70, 70, 70, 70);
            mDoc.setMarginMirroring(true);

            mDoc.open();

            Rectangle rect= new Rectangle(577,825,18,15);
            rect.enableBorderSide(1);
            rect.enableBorderSide(2);
            rect.enableBorderSide(4);
            rect.enableBorderSide(8);
            rect.setBorder(2);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderColor(BaseColor.BLACK);
            rect.setBorderWidth(2);
            mDoc.add(rect);

            Font f1= new Font (Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL, BaseColor.BLACK);
            Paragraph p1=new Paragraph("Sardar Vallabhbhi Patel Education Society's",f1);
            p1.setAlignment(Element.ALIGN_CENTER);
            mDoc.add(p1);

            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 26, Font.UNDERLINE, BaseColor.BLACK);
            Paragraph title= new Paragraph("R. N. G. Patel Institute Of Technology",font);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(2.5f);
            mDoc.add(title);

            Paragraph p2=new Paragraph("At. Isroli-afwa,Bardoli-Navsari Road, Ta-Bardoli, Dist. Surat " +
                    "Pin 394620 \n E-mail: rngpit@gmil.com , Website: www.rngpit.ac.in , Ph. 92280 00867",f1);
            p2.setAlignment(Element.ALIGN_CENTER);
            p2.setSpacingAfter(30.5f);
            mDoc.add(p2);

            Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLDITALIC, BaseColor.BLACK);
            Paragraph title1=new Paragraph("SPORTS 2020",f2);
            title1.setAlignment(Element.ALIGN_CENTER);
            title1.setSpacingAfter(20.5f);
            mDoc.add(title1);

            String ettext1=text1.getText().toString();
            mDoc.add(new Paragraph(ettext1));

            Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
            Paragraph p3=new Paragraph("->Cricket Result\n",f3);
            p3.setSpacingBefore(5.5f);
            mDoc.add(p3);

            Paragraph p4=new Paragraph("(1)Winner team is "+cwinner_team+"\n (2)Renners up team is "+crunnersup_team,f1);
            p4.setSpacingAfter(5.5f);
            mDoc.add(p4);

            Paragraph p5=new Paragraph("->Kabaddi Result\n",f3);
            mDoc.add(p5);

            Paragraph p6=new Paragraph("(1)Winner team is "+kwinner_team+"\n (2)Renners up team is "+krunnersup_team,f1);
            p6.setSpacingAfter(5.5f);
            mDoc.add(p6);

            Paragraph p7=new Paragraph("->Volleyball Result\n",f3);
            mDoc.add(p7);

            Paragraph p8=new Paragraph("(1)Winner team is "+vwinner_team+"\n (2)Renners up team is "+vrunnersup_team,f1);
            p8.setSpacingAfter(10.5f);
            mDoc.add(p8);

            String ettext2=text2.getText().toString();
            mDoc.add(new Paragraph(ettext2));

            /**Paragraph info= new Paragraph("Name\n\nEmail\n\nContact Number",f1);
            Paragraph addr= new Paragraph("Street\n\nCity\n\nPin",f1);
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.spacingAfter();
            PdfPCell cell = new PdfPCell(info);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.disableBorderSide(Rectangle.BOX);
            cell.setExtraParagraphSpace(1.5f);
            table.addCell(cell);
            cell = new PdfPCell(addr);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.disableBorderSide(Rectangle.BOX);
            cell.setExtraParagraphSpace(1.5f);
            table.addCell(cell);
            mDoc.add(table);**/


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
}
