package com.nurgulmantarci.mynotesappjava.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.databinding.ActivityAddNoteBinding;
import com.nurgulmantarci.mynotesappjava.helper.UserInformationHelper;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract;
import com.nurgulmantarci.mynotesappjava.noteData.NoteDatabaseHelper;
import com.nurgulmantarci.mynotesappjava.noteData.NoteQueryHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddNoteActivity extends AppCompatActivity {


    Bitmap selectedImage;
    ActivityAddNoteBinding dataBinding;
    NoteQueryHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_note);

        handler=new NoteQueryHandler(this.getContentResolver());
        selectedImage=BitmapFactory.decodeResource(getResources(), R.drawable.default_image);

//        Intent intent = getIntent();
//        String info = intent.getStringExtra("info");
//
//        if (info.matches("new")) {
//            dataBinding.etNot.setText("");
//            Bitmap selectImage = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.select_image);
//            dataBinding.imageView.setImageBitmap(selectImage);
//
//        } else {




   //         int imageId = intent.getIntExtra("imageId",1);

  //          try {

//                Cursor cursor = database.rawQuery("SELECT * FROM arts WHERE id = ?",new String[] {String.valueOf(imageId)});

//                int noteTextID = cursor.getColumnIndex("notetext");
//                int imageIx = cursor.getColumnIndex("image");
//
//                while (cursor.moveToNext()) {
//
//                    dataBinding.etNot.setText(cursor.getString(noteTextID));
//                    byte[] bytes = cursor.getBlob(imageIx);
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                    dataBinding.imageView.setImageBitmap(bitmap);
//
//                }
//
//                cursor.close();

//            } catch (Exception e) {
//
//            }


  //      }



    }




    public void selectImage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Uri imageData = data.getData();


            try {

                if (Build.VERSION.SDK_INT >= 28) {

                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    dataBinding.imageView.setImageBitmap(selectedImage);

                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    dataBinding.imageView.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void btnAddClicked(View view){


        String noteTextString = dataBinding.etNot.getText().toString();
        Bitmap smallImage = makeSmallerImage(selectedImage,300);
        String userEmail= UserInformationHelper.getUserEmail(this);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray = outputStream.toByteArray();

        try {

//            database = this.openOrCreateDatabase("NotesTable",MODE_PRIVATE,null);
//            database.execSQL("CREATE TABLE IF NOT EXISTS AddNote (id INTEGER PRIMARY KEY,notetext VARCHAR, email VARCHAR, image BLOB)");
//
//
//            String sqlString = "INSERT INTO AddNote (notetext, email, image) VALUES (?, ?, ?)";
//            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
//            sqLiteStatement.bindString(1,noteTextString);
//            sqLiteStatement.bindString(2,userEmail);
//            sqLiteStatement.bindBlob(3,byteArray);
//            sqLiteStatement.execute();

            ContentValues values=new ContentValues();
            values.put(NoteContract.NotesEntry.COLUMN_NOTE_CONTENT,noteTextString);
            values.put(NoteContract.NotesEntry.COLUMN_EMAIL,userEmail);
            values.put(NoteContract.NotesEntry.COLUMN_IMAGE,byteArray);
            values.put(NoteContract.NotesEntry.COLUMN_CREATE_TIME,"13.06.2021");
            values.put(NoteContract.NotesEntry.COLUMN_FINISH_TIME,"13.06.2021");
            values.put(NoteContract.NotesEntry.COLUMN_DONE,0);
            values.put(NoteContract.NotesEntry.COLUMN_CATEGORY_ID,"1");
            handler.startInsert(1,null, NoteContract.NotesEntry.CONTENT_URI,values);


        } catch (Exception e) {

        }


        Intent intent = new Intent(AddNoteActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        //finish();
    }

    public Bitmap makeSmallerImage(Bitmap image, int maximumSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }
}