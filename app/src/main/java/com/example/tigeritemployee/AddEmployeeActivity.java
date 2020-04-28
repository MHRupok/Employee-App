package com.example.tigeritemployee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import java.io.ByteArrayOutputStream;

public class AddEmployeeActivity extends AppCompatActivity {



    public static DatabaseHelper myDB;

    EditText editName, editGender,editAge;
    Button addProfileButton;
    ImageView imgView;

    final int REQUEST_CODE_GALLERY = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Entry");

        editName = findViewById(R.id.editName);
        editGender = findViewById(R.id.editGender);
        editAge = findViewById(R.id.editAge);
        addProfileButton = (Button)findViewById(R.id.buttonSaveData);
        imgView = findViewById(R.id.imageView);


        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        AddEmployeeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );

            }
        });

        myDB = new DatabaseHelper(this,"RECORDDB.sqlite",null,1);

        myDB.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, age VARCHAR,gender VARCHAR,image BLOB)");


        addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myDB.insertData(
                            editName.getText().toString().trim(),
                            editAge.getText().toString(),
                            editGender.getText().toString().trim(),
                            imageViewToByte(imgView)
                    );
                    Toast.makeText(AddEmployeeActivity.this,"Added Successfully",Toast.LENGTH_SHORT).show();
//                    editName.setText("");
//                    editGender.setText("");
//                    editAge.setText("");
//                    imgView.setImageResource(R.drawable.app_photo);
                    finish();


                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });



    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
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
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {

            Uri imageData = data.getData();

            imgView.setImageURI(imageData);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}


