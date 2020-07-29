package com.paradigmshift.fyp.dashitems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.paradigmshift.fyp.Chat.data.SharedPreferenceHelper;
import com.paradigmshift.fyp.Chat.data.StaticConfig;
import com.paradigmshift.fyp.FirebaseDatamodel.ImageDataModel;
import com.paradigmshift.fyp.FirebaseDatamodel.InitializeData;
import com.paradigmshift.fyp.FirebaseDatamodel.ProfileDataModel;
import com.paradigmshift.fyp.MainActivity;
import com.paradigmshift.fyp.R;
import com.paradigmshift.fyp.viewpager.DashActivity;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import stream.customalert.CustomAlertDialogue;


public class ProfileActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    LinearLayout personalinfo, experience, review;
    TextView personalinfobtn, experiencebtn, reviewbtn;

    private static final String TAG = "ProfileActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "descruption";
    private static final String KEY_NAME = "Name";
    private static final String KEY_RATINGS = "Ratings";
    private static final String KEY_PHONE= "phoneNo";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LOCATION = "Location";
    private static final String KEY_PROFILE_PIC_URL = "profilePicUrl";
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private FABProgressCircle fabProgressCircle;

    private String documentId = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference profileReference = db.collection("userData").document(StaticConfig.UID);
    private DatabaseReference userDB;


    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FillData();

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);



    }





    public void FillData(){

        ImageView profilePic = findViewById(R.id.profile_pic);
        TextView name = findViewById(R.id.text_name);
        TextView title = findViewById(R.id.text_title);
        TextView description = findViewById(R.id.text_MyDescription);

        TextView phoneNumber = findViewById(R.id.text_phoneNo);
        TextView email = findViewById(R.id.text_email);
        TextView location = findViewById(R.id.text_location);

        //profilePic.setImageResource(R.drawable.ic_profile);

        //InitializeData iData = new InitializeData(profilePic,name,title,description,smileRating,phoneNumber,email,location);
        // iData.initialize(profileReference);
       // profileReference.document().getId();
        profileReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ProfileDataModel model = documentSnapshot.toObject(ProfileDataModel.class);
                documentId = model.getDocumentID();
                name.setText(model.getName());
                title.setText(model.getTitle());

                phoneNumber.setText(model.getPhoneNo());
                email.setText(model.getEmail());
                location.setText(model.getLocation());
                description.setText(model.getDescruption());
                Glide.with(getApplicationContext()).load(model.getProfilePicUrl()).into(profilePic);
            }
        });






    }
    public void Back(View view) {
        finish();
        startActivity(new Intent(ProfileActivity.this, DashActivity.class));

    }

    public void updateData(String key,String value){
        DocumentReference docref = db.document("userData/"+documentId);
        docref.update(key,value);
        FillData();
    }

    public void EditAboutMe(View view) {

        Context mContext = getApplicationContext();
        ArrayList<String> lineHint = new ArrayList<>();
        lineHint.add("About Me");

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(ProfileActivity.this)
                .setStyle(CustomAlertDialogue.Style.INPUT)
                .setTitle("Edit Description")
                .setPositiveText("Submit")
                .setPositiveColor(R.color.positive)
                .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                .setOnInputClicked(new CustomAlertDialogue.OnInputClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog, ArrayList<String> inputList) {
                        updateData(KEY_DESCRIPTION,inputList.get(0));
                        dialog.dismiss();

                    }
                })
                .setNegativeText("Cancel")
                .setNegativeColor(R.color.negative)
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setLineInputHint(lineHint)
                .setDecorView(getWindow().getDecorView())
                .build();
        alert.show();


    }

    public void EditName(View view) {

        Context mContext = getApplicationContext();
        ArrayList<String> lineHint = new ArrayList<>();
        lineHint.add("Name");
        lineHint.add("title");

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(ProfileActivity.this)
                .setStyle(CustomAlertDialogue.Style.INPUT)
                .setTitle("Edit Name")
                .setPositiveText("Submit")
                .setPositiveColor(R.color.positive)
                .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                .setOnInputClicked(new CustomAlertDialogue.OnInputClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog, ArrayList<String> inputList) {
                            updateData(KEY_NAME,inputList.get(0));
                            updateData(KEY_TITLE,inputList.get(1));
                            dialog.dismiss();
                    }
                })
                .setNegativeText("Cancel")
                .setNegativeColor(R.color.negative)
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setLineInputHint(lineHint)
                .setDecorView(getWindow().getDecorView())
                .build();
        alert.show();

    }

    public void EditContactInfo(View view) {

        Context mContext = getApplicationContext();
        ArrayList<String> lineHint = new ArrayList<>();
        lineHint.add("Phone Number");
        lineHint.add("Location");

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(ProfileActivity.this)
                .setStyle(CustomAlertDialogue.Style.INPUT)
                .setTitle("Edit contact and location info")
                .setPositiveText("Submit")
                .setPositiveColor(R.color.positive)
                .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                .setOnInputClicked(new CustomAlertDialogue.OnInputClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog, ArrayList<String> inputList) {
                        updateData(KEY_PHONE,inputList.get(0));
                        updateData(KEY_LOCATION,inputList.get(1));
                        dialog.dismiss();
                    }
                })
                .setNegativeText("Cancel")
                .setNegativeColor(R.color.negative)
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setLineInputHint(lineHint)
                .setDecorView(getWindow().getDecorView())
                .build();
        alert.show();
    }


    public void EditProfilePic(View view) {

        openFileChooser();


    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        fabProgressCircle.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            ImageView mImageView = findViewById(R.id.profile_pic);
            Picasso.get().load(mImageUri).into(mImageView);


        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //mProgressBar.setProgress(0);
                                }
                            }, 500);

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Uri downloadUrl = uri;
                                    updateData(KEY_PROFILE_PIC_URL,""+downloadUrl);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user").child(StaticConfig.UID);
                                    mDatabase.child("avata").setValue(""+downloadUrl);
                                }
                            });
                            fabProgressCircle.beginFinalAnimation();
                            Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            ImageDataModel upload = new ImageDataModel("Profile picture",
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("***********************************************************************************"+e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            if(progress == 100){
                                fabProgressCircle.setVisibility(View.INVISIBLE);

                               // fabProgressCircle.hide();
                            }
                            //mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void fabLoad(View view) {
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            fabProgressCircle.hide();
            Toast.makeText(ProfileActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile();
            fabProgressCircle.show();
        }
    }

}
