package com.teamgrid.fashhub;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teamgrid.fashhub.models.UserDetail;
import com.teamgrid.fashhub.utils.Constants;
import com.teamgrid.fashhub.utils.Device;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnbosco on 24-Aug-17.
 */

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditProfile";

    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    private String userChoosenTask;
    CharSequence[] items = {"from Camera", "from Gallery", "cancel"};
    ImageView profilePic;
    EditText nameField, addressField, phoneField, emailField, bioField;
    ImageButton nameFieldEdit, addressFieldEdit, phoneFieldEdit, bioFieldEdit;
    String name, address, phoneNo, email, aboutMe, gender;
    RadioButton rdMale, rdFemale;
    Button btnSave;
    String folderName;
    String fileName = "profile/profilePicture.png";

    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";
    private Uri mFileUri = null;
    private String avaterUrl = null;

    @Override
    public void onSaveInstanceState(Bundle out) {
        out.putParcelable(KEY_FILE_URI, mFileUri);
        out.putString(KEY_DOWNLOAD_URL, avaterUrl);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (getIntent() != null) {
            email = getIntent().getExtras().getString("user");
        }

        setupView();

        // Restore instance state
        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            avaterUrl = savedInstanceState.getString(KEY_DOWNLOAD_URL);
        }

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.URL_STORAGE_REFERENCE);
        folderName = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (mStorageRef != null) {
            getUserDetails();
        }
    }

    private void setupView(){

        profilePic = (ImageView) findViewById(R.id.user_photo);
        profilePic.setOnClickListener(this);
        nameField = (EditText) findViewById(R.id.name);
        nameField.setEnabled(false);
        addressField = (EditText) findViewById(R.id.address);
        addressField.setEnabled(false);
        phoneField = (EditText) findViewById(R.id.phoneNo);
        phoneField.setEnabled(false);
        emailField = (EditText) findViewById(R.id.email);
        emailField.setEnabled(false);
        bioField = (EditText) findViewById(R.id.bio);
        bioField.setEnabled(false);

        rdMale = (RadioButton) findViewById(R.id.rb_male);
        rdMale.setOnClickListener(this);
        rdMale.setChecked(true);
        gender = "MALE";
        rdFemale = (RadioButton) findViewById(R.id.rb_female);
        rdFemale.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.save_button);
        btnSave.setOnClickListener(this);

        nameFieldEdit = (ImageButton) findViewById(R.id.clickName);
        nameFieldEdit.setOnClickListener(this);
        addressFieldEdit = (ImageButton) findViewById(R.id.clickAddress);
        addressFieldEdit.setOnClickListener(this);
        phoneFieldEdit = (ImageButton) findViewById(R.id.clickPhone);
        phoneFieldEdit.setOnClickListener(this);
        bioFieldEdit = (ImageButton) findViewById(R.id.clickBio);
        bioFieldEdit.setOnClickListener(this);

        //if() findViewById(R.id.bio_card).setVisibility(View.VISIBLE);
        //else findViewById(R.id.bio_card).setVisibility(View.VISIBLE);

    }

    private void getUserDetails() {
        DatabaseReference userDetailsRef = mDatabaseRef.child(Constants.FOLDER_DATABASE_USERDETAILS).child(folderName);
        userDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
                nameField.setText(userDetail.getName());
                addressField.setText(userDetail.getAddress());
                phoneField.setText(userDetail.getPhone());
                emailField.setText(userDetail.getEmail());
                bioField.setText(userDetail.getBio());

                if(userDetail.getAvaterUrl()!=null) {
                    avaterUrl = userDetail.getAvaterUrl();
                    Glide.with(getBaseContext()).load(userDetail.getAvaterUrl())
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.ic_person_white_36dp)
                            .into(profilePic);
                }
                if(userDetail.getGender()==null){
                    rdMale.setClickable(true);
                    rdFemale.setClickable(true);
                }else{
                    if(userDetail.getGender().equalsIgnoreCase("MALE")){
                        gender="MALE";
                        rdMale.setChecked(true);
                        rdFemale.setChecked(false);
                    }else if(userDetail.getGender().equalsIgnoreCase("FEMALE")){
                        gender="FEMALE";
                        rdFemale.setChecked(true);
                        rdFemale.setChecked(false);
                    }

                    rdMale.setClickable(false);
                    rdFemale.setClickable(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //  System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.user_photo:
                getImageFrom();
                break;
            case R.id.clickName:
                if(!nameField.isEnabled()){ nameField.setEnabled(true); nameField.requestFocus(); }
                else nameField.setEnabled(false);
                break;
            case R.id.clickAddress:
                if(!addressField.isEnabled()){ addressField.setEnabled(true); addressField.requestFocus(); }
                else addressField.setEnabled(false);
                break;
            case R.id.clickPhone:
                if(!phoneField.isEnabled()){ phoneField.setEnabled(true); phoneField.requestFocus();}
                else phoneField.setEnabled(false);
                break;
            case R.id.rb_male:
                gender="MALE";
                rdMale.setChecked(true);
                rdFemale.setChecked(false);
                break;
            case R.id.rb_female:
                gender="FEMALE";
                rdFemale.setChecked(true);
                rdMale.setChecked(false);
                break;
            case R.id.clickBio:
                if(!bioField.isEnabled()){ bioField.setEnabled(true); bioField.requestFocus(); }
                else bioField.setEnabled(false);
                break;
            case R.id.save_button:
                sendToFirebase();
                break;
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

                if (mStorageRef != null && mFileUri != null) {
                    StorageReference profilePhotoRef = mStorageRef.child(Constants.FOLDER_STORAGE_PHOTO).child(folderName).child(fileName);
                    UploadTask uploadTask = profilePhotoRef.putFile(mFileUri);
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                         //   System.out.println(taskSnapshot.getBytesTransferred());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            avaterUrl = taskSnapshot.getDownloadUrl().toString();
                            updateUser();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Failed to upload picture", Toast.LENGTH_LONG).show();
                            Device.dismissProgressDialog();
                        }
                    });
                }else if(mDatabaseRef!=null){
                    updateUser();
                } else{
                    Device.dismissProgressDialog();
                }
            }
        }
    }
    
    private void updateUser(){
        Map<String, Object> userUpdates = new HashMap<String, Object>();
        userUpdates.put("name", name);
        userUpdates.put("address", address);
        userUpdates.put("phone", phoneNo);
        userUpdates.put("gender", gender);
        userUpdates.put("bio", aboutMe);
        userUpdates.put("avaterUrl", avaterUrl);
        mDatabaseRef.child(Constants.FOLDER_DATABASE_USERDETAILS)
                .child(folderName).updateChildren(userUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getBaseContext(), "Failed to save data", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Successfully saved data", Toast.LENGTH_LONG).show();
                }
            }
        });
        Device.dismissProgressDialog();
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
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                mFileUri = data.getData();
                if (mFileUri != null) onSelectFromGalleryResult(data);
            }
            else if (requestCode == IMAGE_CAMERA_REQUEST) {
                mFileUri = data.getData();
                if (mFileUri != null) onCaptureImageResult(data);
            }
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

        name = nameField.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            nameField.setError("Enter a name");
            Toast.makeText(getApplicationContext(), "Enter a name", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            nameField.setError(null);
        }

        address = addressField.getText().toString().trim();
        if (TextUtils.isEmpty(address) || address.length() < 10 || address.length() > 100) {
            addressField.setError("Enter a valid address");
            valid = false;
        } else {
            addressField.setError(null);
        }

       phoneNo = phoneField.getText().toString().trim();
       if (TextUtils.isEmpty(phoneNo) || phoneNo.length() < 11 || phoneNo.length() > 13) {
           phoneField.setError("Enter a valid phone number");
           valid = false;
       } else {
           phoneField.setError(null);
       }
       aboutMe = bioField.getText().toString().trim();
       if (TextUtils.isEmpty(aboutMe)) {
           bioField.setError("Say something about your business");
           valid = false;
       } else {
           bioField.setError(null);
       }

       if (mFileUri == null && avaterUrl == null) {
           Toast.makeText(this,"No photo file", Toast.LENGTH_LONG).show();
           valid = false;
       } else {
       }

       return valid;
   }

}
