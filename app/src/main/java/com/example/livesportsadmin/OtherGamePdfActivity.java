package com.example.livesportsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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

public class OtherGamePdfActivity extends AppCompatActivity implements View.OnClickListener {

private Button btnpdfgen;
    Spinner spgamelist,spDepartment,spClass;
    ArrayList<String> dept_list,game_list,class_list;
    ArrayAdapter<String> adapter,adapterdept,adapterClass;
    String[] games={"Chess","Badminton","Tabletennis","Carrom"};
    String[] dept={"Computer","Chemical","Civil","Electrical","Mechanical"};
    String[] Class={"16","17","18","19"};

    ArrayList<String> docref;
    ArrayAdapter<String> docadapter;

    private TextView reportprogress;

    private AlertDialog progressDialog;
    private AlertDialog.Builder builder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_game_pdf);


        reportprogress=(TextView)findViewById(R.id.plname);
        spgamelist=(Spinner)findViewById(R.id.spinnerGameList);
        spClass=(Spinner)findViewById(R.id.spinnerClass);
        spDepartment=(Spinner)findViewById(R.id.spinnerDepartment);

        game_list=new ArrayList<String>();
        class_list=new ArrayList<String>();
        dept_list=new ArrayList<String>();

        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,games);
        adapterClass=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,Class);
        adapterdept=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,dept);

        spgamelist.setAdapter(adapter);
        spClass.setAdapter(adapterClass);
        spDepartment.setAdapter(adapterdept);

        builder=new ProgressDialog.Builder(OtherGamePdfActivity.this);
        LayoutInflater inflater =OtherGamePdfActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(true);
        progressDialog= builder.create();

        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(
                android.R.color.transparent
        );

        btnpdfgen=(Button)findViewById(R.id.genpdf);
        btnpdfgen.setOnClickListener(this);


        docref=new ArrayList<String>();


    }


    public void  genpdf(){

        final List<String> doclist = new ArrayList<>();

        final int[] total = {0};
        progressDialog.show();
        reportprogress.setText(" ");

        String value=spDepartment.getSelectedItem().toString()+spClass.getSelectedItem().toString();

        FirebaseFirestore.getInstance().collection(spgamelist.getSelectedItem().toString()).whereEqualTo("batch",value).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        doclist.add(document.get("name").toString());
                        doclist.add(document.get("batch").toString());
                        total[0]+=2;
                        String x=reportprogress.getText().toString();
                        reportprogress.setText(x);

                       // Toast.makeText(OtherGamePdfActivity.this, doclist.get(0), Toast.LENGTH_SHORT).show();

                    }
                }

            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isComplete()){
                    putvaluepdf(doclist,total[0]);
                }
            }
        });


    }

//    public void putvaluepdf(List<String> doclist, int n) {
//
//        for(int i=0;i<n;i++)
//        {
//            final List<String> namelist =new ArrayList<>();
//            final int[] totalname = {0};
////                Paragraph docid=new Paragraph(name[0],f1);
//            // mDoc.add(docid);
//
//            FirebaseFirestore.getInstance().collection(spgamelist.getSelectedItem().toString()).document(doclist.get(i))
//                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                         String name=documentSnapshot.get("name").toString();
//                         namelist.add(name);
//                         totalname[0]++;
//                            Toast.makeText(OtherGamePdfActivity.this, name, Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
////            Paragraph docid=new Paragraph(name[0],f1);
////            mDoc.add(docid);
//            if((i+1)==n){
//                savepdf(namelist,totalname[0]);
//                Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }

    public void putvaluepdf(List<String> namelist, int i){
        final Document mDoc=new Document();
        String filename=spgamelist.getSelectedItem().toString()+"-"+spDepartment.getSelectedItem().toString()
                +"-"+spClass.getSelectedItem().toString()+"_2020";
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

            final com.itextpdf.text.Font f1= new com.itextpdf.text.Font (com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 13,com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
            com.itextpdf.text.Paragraph p1=new com.itextpdf.text.Paragraph("Sardar Vallabhbhi Patel Education Society's",f1);
            p1.setAlignment(Element.ALIGN_CENTER);
            mDoc.add(p1);

            com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 26, com.itextpdf.text.Font.UNDERLINE, BaseColor.BLACK);
            Paragraph title= new Paragraph("R. N. G. Patel Institute Of Technology",font);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(2.5f);
            mDoc.add(title);

            final Paragraph p2=new Paragraph("At. Isroli-afwa,Bardoli-Navsari Road, Ta-Bardoli, Dist. Surat " +
                    "Pin 394620 \n E-mail: rngpit@gmil.com , Website: www.rngpit.ac.in , Ph. 92280 00867",f1);
            p2.setAlignment(Element.ALIGN_CENTER);
            p2.setSpacingAfter(30.5f);
            mDoc.add(p2);

            com.itextpdf.text.Font f2 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 20, com.itextpdf.text.Font.BOLDITALIC, BaseColor.BLACK);
            Paragraph title1=new Paragraph("SPORTS 2020",f2);
            title1.setAlignment(Element.ALIGN_CENTER);
            title1.setSpacingAfter(20.5f);
            mDoc.add(title1);

            Paragraph gameTitle=new Paragraph(spgamelist.getSelectedItem().toString()+"-"+spDepartment.getSelectedItem().toString()
                    +"-"+spClass.getSelectedItem().toString(),f2);
            gameTitle.setAlignment(Element.ALIGN_CENTER);
            mDoc.add(gameTitle);


            float[] pointColumn={200f,80f};
            PdfPTable table=new PdfPTable(pointColumn);
            table.setHorizontalAlignment(25);
            table.addCell("NAME");
            table.addCell("CLASS");

           for (int j=0;j<i;j++){

              // Toast.makeText(this, namelist.get(j), Toast.LENGTH_SHORT).show();
                String name=namelist.get(j);
               Paragraph nam = new Paragraph(name, f1);
               table.addCell(nam);
               if(j%2!=0) {

                table.completeRow();

               }

           }
            mDoc.add(table);





            mDoc.add(new Chunk("\n"));
            //mDoc.add(new LineSeparator(2f,100,BaseColor.DARK_GRAY,Element.ALIGN_CENTER,-1f));

            mDoc.close();

            Toast.makeText(this,filename+".pdf\nis saved to\n"+filepath,Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            reportprogress.setText("Status:- Done ");


        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }








    }

    @Override
    public void onClick(View v){

        if(v==btnpdfgen){
            genpdf();
        }

    }
}
