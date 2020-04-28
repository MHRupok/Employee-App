package com.example.tigeritemployee;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;


import com.ajts.androidmads.sqliteimpex.SQLiteImporterExporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button buttonAddEmployee,buttonEmployeeList,buttonExportData,buttonImportData;
    public static DatabaseHelper mainDB;

    ImageView imageViewIcons;
    Uri fileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonAddEmployee = (Button)findViewById(R.id.buttonAddEmployee);
        buttonEmployeeList = (Button)findViewById(R.id.buttonEmployeeList);
        buttonExportData = (Button)findViewById(R.id.buttonExportData);
        buttonImportData = (Button)findViewById(R.id.buttonImportData);



        // button actions

        buttonExportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // eikhane
                exportDBS();
            }
        });

        buttonImportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDBS();

            }
        });





        buttonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddEmployeeActivity();
            }
        });

        buttonEmployeeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecord();
            }
        });

        // check database exists

        String dbpath = getApplicationContext().getDatabasePath("RECORDDB.sqlite").getAbsolutePath();
       // Toast.makeText(MainActivity.this," "+dbpath,Toast.LENGTH_SHORT).show();
//
//        File database = new File(dbpath);
//        if(database.exists())
//        {
//            Toast.makeText(MainActivity.this,"acche!",Toast.LENGTH_SHORT).show();
//        }
//
//        else
//        {
//            Toast.makeText(MainActivity.this,"naai!",Toast.LENGTH_SHORT).show();
//        }

        mainDB = new DatabaseHelper(this,"RECORDDB.sqlite",null,1);
        mainDB.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, age VARCHAR,gender VARCHAR,image BLOB)");

        //initFolder();
    }

    public void tempDB(){
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                777
        );



    }





    public void importDBS()
    {

        String dbpath = getApplicationContext().getDatabasePath("RECORDDB.sqlite").getAbsolutePath();

        if(deleteDatabase(dbpath)){

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        File targetDir = Environment.getDataDirectory();
        File sourceDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);



        FileChannel source=null;
        FileChannel destination=null;

        String currentDBPath = "/RECORDDB.sqlite";
        String backupDBPath = "/data/"+ "com.example.tigeritemployee" +"/databases/"+"RECORDDB.sqlite";



        File currentDB = new File(sourceDir, currentDBPath);
        File backupDB = new File(targetDir, backupDBPath);

        if(currentDB.exists()){
            try {
                source = new FileInputStream(currentDB).getChannel();
                Toast.makeText(MainActivity.this,"Read success!",Toast.LENGTH_SHORT).show();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(this, "DB Imported!", Toast.LENGTH_LONG).show();
            } catch(IOException e) {
                e.printStackTrace();
            }

        }

        else
        {
            Toast.makeText(MainActivity.this,"Database not found in downloads",Toast.LENGTH_LONG).show();
        }

    }



    public void exportDBS(){
        File sd = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File data = Environment.getDataDirectory();

        FileChannel source=null;
        FileChannel destination=null;

        String currentDBPath = "/data/"+ "com.example.tigeritemployee" +"/databases/"+"RECORDDB.sqlite";
        String backupDBPath = "RECORDDB.sqlite";

        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported to data/com.example.tigeritemployee/files/Download", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void initFolder()
    {
        File sd = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File data = Environment.getDataDirectory();

        FileChannel source=null;
        FileChannel destination=null;

        String currentDBPath = "/data/"+ "com.example.tigeritemployee" +"/databases/"+"RECORDDB.sqlite";
        String backupDBPath = "RECORDDB.sqlite";

        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            //Toast.makeText(this, "DB Exported to data/com.example.tigeritemployee/files/Download", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDatabase (File file){
        return true;
    }

    public void goAddEmployeeActivity()
    {
        Intent intent = new Intent(this,AddEmployeeActivity.class);
        startActivity(intent);
    }

    public void showRecord()
    {

        Intent intent = new Intent(this,RecordListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 777){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("*/*");
                startActivityForResult(intent, 777);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 777 && resultCode == RESULT_OK && data != null) {

            fileData = data.getData();



        }
        super.onActivityResult(requestCode, resultCode, data);
    }





}
