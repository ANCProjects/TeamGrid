package com.teamgrid.fashhub;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.teamgrid.fashhub.utils.Device;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Johnbosco on 24-Aug-17.
 */

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditProfile";

    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    //File
    private File filePathImageCamera;
    //Firebase Database
    DatabaseReference mFirebaseDatabaseReference;
    //Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private String userChoosenTask;
    CharSequence[] items = {"from Camera", "from Gallary", "cancel"};
    ImageView profilePic;
    EditText nameField, addressField, phoneField,emailField,bioField;
    RadioButton rdMale, rdFemale;
    Button btnSave;
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if(getIntent()!=null){
            user = getIntent().getExtras().getString("user");
        }

        profilePic = (ImageView) findViewById(R.id.user_photo);
        profilePic.setOnClickListener(this);
        nameField = (EditText) findViewById(R.id.name);
        addressField = (EditText) findViewById(R.id.address);
        phoneField = (EditText) findViewById(R.id.phoneNo);
        emailField = (EditText) findViewById(R.id.email);
        emailField.setText(user);
        emailField.setEnabled(false);
        bioField = (EditText) findViewById(R.id.bio);

        rdMale = (RadioButton) findViewById(R.id.rb_male);
        rdMale.setOnClickListener(this);
        rdMale.setChecked(true);
        rdFemale = (RadioButton) findViewById(R.id.rb_female);
        rdFemale.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.save_button);
        btnSave.setOnClickListener(this);

        //if() findViewById(R.id.bio_card).setVisibility(View.VISIBLE);
        //else findViewById(R.id.bio_card).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v==profilePic){
            getImageFrom();
        }else if(v==btnSave){
            sendToFirebase();
        }else if(v==rdMale){
            rdMale.setChecked(true);
            rdFemale.setChecked(false);
        }else if(v==rdFemale){
            rdFemale.setChecked(true);
            rdMale.setChecked(false);
        }
    }

    private void sendToFirebase(){
        if (!validateForm()) {
            return;
        }

        if (Device.isConnected(this)) {

            if (!Device.isProgressDialogShowing()) {
                Device.showProgressDialog(this, false, "Please wait..");

                Device.hideKeyboard(this);

                Toast.makeText(this, "Still working on it, TeamGrid", Toast.LENGTH_LONG).show();

                Device.dismissProgressDialog();
            }
        }

    }


    private void getImageFrom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Get photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int chosenitem) {
                boolean result = Device.checkStoragePermission(getApplicationContext());

                if (items[chosenitem].equals(items[0])) {
                    userChoosenTask = items[0].toString();
                    if(result) photoCameraIntent();
                } else if (items[chosenitem].equals(items[1])) {
                    userChoosenTask =items[1].toString();
                    if(result) photoGalleryIntent();
                } else if (items[chosenitem].equals(items[2])) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Device.REQUEST_EXTERNAL_STORAGE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(items[0].toString()))
                        photoCameraIntent();
                    else if(userChoosenTask.equals(items[1].toString()))
                        photoGalleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void photoCameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IMAGE_CAMERA_REQUEST);
    }

    private void photoGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), IMAGE_GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == IMAGE_CAMERA_REQUEST)
                onCaptureImageResult(data);
        }
    }



    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profilePic.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profilePic.setImageBitmap(bm);
    }

   private boolean validateForm() {
        boolean valid = true;

        String name = nameField.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            nameField.setError("Enter a name");
            Toast.makeText(getApplicationContext(), "Enter a name", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            nameField.setError(null);
        }

       String address = addressField.getText().toString().trim();
        if (TextUtils.isEmpty(address) || address.length() < 10 || address.length() > 100) {
            addressField.setError("Enter a valid address");
            valid = false;
        } else {
            addressField.setError(null);
        }

       String phoneNo = phoneField.getText().toString().trim();
       if (TextUtils.isEmpty(phoneNo) || phoneNo.length() < 11 || phoneNo.length() > 13) {
           phoneField.setError("Enter a valid phone number");
           valid = false;
       } else {
           phoneField.setError(null);
       }

       String aboutMe = bioField.getText().toString().trim();
       if (TextUtils.isEmpty(aboutMe)) {
           bioField.setError("Say something about your business");
           valid = false;
       } else {
           bioField.setError(null);
       }

       return valid;
   }

}
