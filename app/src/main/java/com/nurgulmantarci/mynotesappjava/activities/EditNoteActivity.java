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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.databinding.ActivityEditNoteBinding;
import com.nurgulmantarci.mynotesappjava.helper.UserInformationHelper;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract.CategoryEntry;
import com.nurgulmantarci.mynotesappjava.noteData.NoteQueryHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditNoteActivity extends AppCompatActivity {

    int noteId,categoryId;
    String noteContent;
    byte[] image_bytes;
    Cursor cursorCategory;
    SimpleCursorAdapter simpleCursorAdapter;
    NoteQueryHandler handler;
    ActivityEditNoteBinding dataBinding;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_note);

        handler=new NoteQueryHandler(this.getContentResolver());

        getIntentData();

        setSpinnerCategory();

        setNoteDetail();

    }


    private void getIntentData(){
        noteId=getIntent().getIntExtra("note_id",0);
        categoryId=getIntent().getIntExtra("category_id",0);
        image_bytes=getIntent().getByteArrayExtra("image_bytes");
        noteContent=getIntent().getStringExtra("note_content");

    }

    private void setSpinnerCategory(){
        String[] projection={CategoryEntry._ID,CategoryEntry.COLUMN_CATEGORY};
        cursorCategory=getContentResolver().query(CategoryEntry.CONTENT_URI,projection,null,null,CategoryEntry._ID);  //+" DESC"

        simpleCursorAdapter=new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,cursorCategory,new String[] {"category"},new int[]{android.R.id.text1},0);
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataBinding.spinnerCategory.setAdapter(simpleCursorAdapter);
        dataBinding.spinnerCategory.setSelection(categoryId-1);

    }

    private void setNoteDetail(){
        dataBinding.etNot.setText(noteContent);
        selectedImage = BitmapFactory.decodeByteArray(image_bytes,0,image_bytes.length);
        dataBinding.imageView.setImageBitmap(selectedImage);

    }

    public void editImage(View view){
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


    public void btnEditClicked(View view){

        String noteTextString = dataBinding.etNot.getText().toString();
        Bitmap smallImage = makeSmallerImage(selectedImage,300);
        String userEmail= UserInformationHelper.getUserEmail(this);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray = outputStream.toByteArray();

        long selectedCategory=dataBinding.spinnerCategory.getSelectedItemId();

        try {


            ContentValues values=new ContentValues();
            values.put(NoteContract.NotesEntry.COLUMN_NOTE_CONTENT,noteTextString);
            values.put(NoteContract.NotesEntry.COLUMN_EMAIL,userEmail);
            values.put(NoteContract.NotesEntry.COLUMN_IMAGE,byteArray);
            values.put(NoteContract.NotesEntry.COLUMN_CREATE_TIME,"13.06.2021");
            values.put(NoteContract.NotesEntry.COLUMN_FINISH_TIME,"13.06.2021");
            values.put(NoteContract.NotesEntry.COLUMN_DONE,0);
            values.put(NoteContract.NotesEntry.COLUMN_CATEGORY_ID,String.valueOf(selectedCategory));

            String selection= NoteContract.NotesEntry._ID+"=?";
            String[] args={String.valueOf(noteId)};
            handler.startUpdate(1,null, NoteContract.NotesEntry.CONTENT_URI,values,selection,args);
            Toast.makeText(this, " Notunuz Güncellendi", Toast.LENGTH_LONG).show();


        } catch (Exception e) {

        }


        Intent intent = new Intent(EditNoteActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

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