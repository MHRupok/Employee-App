package com.example.tigeritemployee;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
public class RecordListActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Model> mList;
    RecordListAdapter mAdapter = null;
    ImageView imageViewIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Employee Records");

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new RecordListAdapter(this,R.layout.row, mList);
        mListView.setAdapter(mAdapter);

        //Cursor cursor = AddEmployeeActivity.myDB.getData("SELECT * FROM RECORD");
        Cursor cursor = MainActivity.mainDB.getData("SELECT * FROM RECORD");

        mList.clear();

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String age = cursor.getString(2);
            String gender = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            mList.add(new Model(id,name,gender,age,image));
        }

        mAdapter.notifyDataSetChanged();
        if(mList.size()==0){
            Toast.makeText(this,"NO DATA AVAILABLE",Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // for update or delete!
                CharSequence[] items = {"Update","Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(RecordListActivity.this);

                dialog.setTitle("Choose an Action!");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(which==0){
                            // Update
                            Cursor c = MainActivity.mainDB.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();

                            while(c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }

                            showDialogUpdate(RecordListActivity.this,arrID.get(position));

                        }

                        if(which==1){
                            // Delete
                            Cursor c = MainActivity.mainDB.getData("SELECT id FROM RECORD");

                            ArrayList<Integer> arrID = new ArrayList<Integer>();

                            while(c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }

                            showDialogDelete(arrID.get(position));

                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    public void showDialogDelete(final int idRecord){
        AlertDialog.Builder dialogDelete= new AlertDialog.Builder(RecordListActivity.this);
        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure to Delete?");
        dialogDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    MainActivity.mainDB.deleteData(idRecord);
                    Toast.makeText(RecordListActivity.this,"Delete Successfull!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Log.e("Error!",e.getMessage());
                }
                updateRecordList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               finish();

            }
        });
        dialogDelete.show();

    }

    private void showDialogUpdate(final Activity activity, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Update!");

        imageViewIcon = dialog.findViewById(R.id.imageViewRecord);
        final EditText editName = dialog.findViewById(R.id.editNameRecord);
        final EditText editAge = dialog.findViewById(R.id.editAgeRecord);
        final EditText editGender = dialog.findViewById(R.id.editGenderRecord);

        Button buttonUpdate = dialog.findViewById(R.id.buttonSaveDataRecord);

        // set width of dialog

        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.95);

        dialog.getWindow().setLayout(width,height);
        dialog.show();


        /////
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        RecordListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    MainActivity.mainDB.updateData(
                            editName.getText().toString().trim(),
                            editAge.getText().toString().trim(),
                            editGender.getText().toString().trim(),
                            AddEmployeeActivity.imageViewToByte(imageViewIcon),
                            position
                    );

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Update Successfull!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.e("Update Error!",error.getMessage());
                }

                updateRecordList();
            }
        });




    }

    public void updateRecordList()
    {
        Cursor cursor = MainActivity.mainDB.getData("SELECT * FROM RECORD");
        mList.clear();

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String age = cursor.getString(2);
            String gender = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            mList.add(new Model(id,name,gender,age,image));
        }

        mAdapter.notifyDataSetChanged();
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
        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
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
        if(requestCode == 888 && resultCode == RESULT_OK && data != null) {

            Uri imageData = data.getData();

            imageViewIcon.setImageURI(imageData);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
