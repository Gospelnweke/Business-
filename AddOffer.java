package com.gospelnweke.businesscategorry.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gospelnweke.businesscategorry.MainActivity;
import com.gospelnweke.businesscategorry.R;
import com.gospelnweke.businesscategorry.model.BusinessUnit;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddOffer extends AppCompatActivity implements AdapterView.OnItemClickListener {




    @BindView(R.id.progressBar_id)
    ProgressBar mProgressBar;

    @BindView(R.id.close_imageViewId)
    ImageView closeImageView;

    @BindView(R.id.offerImage_imageView)
    ImageView offerImageView;


    @BindView(R.id.post_buttonId)
    Button postButton;

    @BindView(R.id.addCaption_edittext)
    EditText addCaptionEdittext;

    @BindView(R.id.businessName_editText_id)
    EditText businesName;


    @BindView(R.id.choosePic_Linearlayout)
    LinearLayout choosepicLinlout;

    @BindView(R.id.TakePhoto_Linearlayout)
    LinearLayout takepicLinlout;

    @BindView(R.id.spinner)
    Spinner spin;


    String[] category={"Food","Fashion","Building","Education","Religion","Housing","Market","Shop","Jobs","Sports"};



   // private String businessName;
    private String categorry;
    private Uri imageUri;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private CollectionReference mCollectionReference;
    //private String user_id;
    private Bitmap compressed;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mCollectionReference=db.collection("BusinnessUnit");


        ArrayAdapter<String>adapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorry=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        spin.setAdapter(adapter);
        //spin.setOnItemClickListener(this);

    }


    @OnClick(R.id.choosePic_Linearlayout)
    public void choosePic(View v){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(AddOffer.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(AddOffer.this, "Permission Denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(AddOffer.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {

                choseImage();

            }

        } else {

            choseImage();
        }



        }

    private void choseImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(AddOffer.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                offerImageView.setVisibility(View.VISIBLE);
                offerImageView.setImageURI(imageUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }


    }


    @OnClick(R.id.post_buttonId)
    public void postOffer(View view){
        mProgressBar.setVisibility(View.VISIBLE);



        File newFile = new File(imageUri.getPath());

        try {
            compressed = new Compressor(AddOffer.this)
                    .setMaxHeight(125)
                    .setMaxWidth(125)
                    .setQuality(50)
                    .compressToBitmap(newFile);

        } catch (IOException e) {
            e.printStackTrace();

        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        compressed.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] thumbData = byteArrayOutputStream.toByteArray();

        //upload image to firebase storage
        //UploadTask image_path = storageReference.child("Offer_image").child("Offer_Image" + ".jpg").putBytes(thumbData);
        StorageReference image_path=storageReference.child("Offer_image").child("Offer_Image" + ".jpg");


        image_path.putBytes(thumbData).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {


                    //String imageUrl=task.getResult().toString();
                    final String caption=addCaptionEdittext.getText().toString();
                    final String bizname=businesName.getText().toString();

                    storeData(task, caption, bizname);


                } else {


                    String error = task.getException().getMessage();


                    Toast.makeText(AddOffer.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();


                    mProgressBar.setVisibility(View.INVISIBLE);


                }


            }


        });







    }

    private void storeData(Task<UploadTask.TaskSnapshot> task, String caption, String bizname) {


        final String cat=categorry;
        //Uri download_uri;
        final String download_uri;
        if (task != null) {
            download_uri = task.getResult().getStorage().toString();
        } else {
            download_uri = imageUri.toString();

        }
        BusinessUnit bunit=new BusinessUnit(bizname,caption,download_uri,cat);

        mCollectionReference.add(bunit).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {

                    //progressDialog.dismiss();
                    mProgressBar.setVisibility(View.GONE);


                    Snackbar.make(findViewById(android.R.id.content),
                            "Successfully Added!", Snackbar.LENGTH_LONG).show();
                    //return to main activity
                    //finish();


                }

            }
        });






       /* db.collection("Users").document("BusinessUnit").set(bunit).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    //progressDialog.dismiss();
                    mProgressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(AddOffer.this, "User Data is Stored Successfully", Toast.LENGTH_LONG).show();

                    Intent mainIntent = new Intent(AddOffer.this, MainActivity.class);


                    startActivity(mainIntent);

                    finish();


                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(AddOffer.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }
                mProgressBar.setVisibility(View.GONE);

            }

        });*/

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
}
