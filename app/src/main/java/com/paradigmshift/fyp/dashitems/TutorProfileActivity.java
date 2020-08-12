package com.paradigmshift.fyp.dashitems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.paradigmshift.fyp.Chat.data.StaticConfig;
import com.paradigmshift.fyp.FirebaseDatamodel.FoldableDataModel;
import com.paradigmshift.fyp.FirebaseDatamodel.ImageDataModel;
import com.paradigmshift.fyp.FirebaseDatamodel.ProfileDataModel;
import com.paradigmshift.fyp.R;
import com.paradigmshift.fyp.fragments.TimePickerFragment;
import com.paradigmshift.fyp.viewpager.DashActivity;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;

import javax.security.auth.Subject;

import stream.customalert.CustomAlertDialogue;

public class TutorProfileActivity extends AppCompatActivity implements  TimePickerDialog.OnTimeSetListener,AdapterView.OnItemSelectedListener{


    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "TutorProfileActivity";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_STATUS = "status";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE= "PhoneNo";
    private static final String KEY_CITY = "city";
    private static final String KEY_STREET = "strretAddress";
    private static final String KEY_PROFILE_PIC_URL = "profilePic";
    private static final String AVAIL_FROM = "availableFrom";
    private static final String AVAIL_TO = "availableTo";
    private static final String RATE = "rate";
    private static final String SUBJECT = "subject";
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private FABProgressCircle fabProgressCircle;
    private String documentId = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference profileReference = db.collection("TutorData").document(StaticConfig.UID);
    private DatabaseReference userDB;
    public String TimeChoice = "";
    public String Subs = "";

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);


        LoadData();

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);

    }

    public void LoadData(){
        ImageView profilePic = findViewById(R.id.Tutor_Profile_pic);
        TextView name = findViewById(R.id.Tutor_name);
        TextView title = findViewById(R.id.Tutor_profile_title);
        TextView description = findViewById(R.id.Tutor_Profile_status);
        SmileRating smileRating = findViewById(R.id.tsr);
        TextView phoneNumber = findViewById(R.id.tutor_phone);
        TextView email = findViewById(R.id.tutor_email);
        TextView location = findViewById(R.id.tutor_location);
        TextView from = findViewById(R.id.Tutor_available_from);
        TextView to = findViewById(R.id.Tutor_available_to);
        TextView Requests = findViewById(R.id.sRequests);
        TextView Subjects = findViewById(R.id.SelectSubject);


        profileReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FoldableDataModel model = documentSnapshot.toObject(FoldableDataModel.class);
                documentId = model.getDocumentID();
                name.setText(model.getName());
                title.setText(model.getTitle());
                smileRating.setSelectedSmile(BaseRating.TERRIBLE);
                phoneNumber.setText(model.getPhoneNo());
                email.setText(model.getEmail());
                location.setText(model.getStrretAddress() + ", " + model.getCity());
                description.setText(model.getStatus());
                from.setText(model.getAvailableFrom());
                to.setText(model.getAvailableTo());
                Requests.setText(model.getRate());
                Glide.with(getApplicationContext()).load(model.getProfilePic()).into(profilePic);

                if(model.getSubject().equals("") || model.getSubject().equals(null)){
                    Subjects.setText("Select Subject");
                }else{
                    Subjects.setText(model.getSubject());
                }
                switch (model.getReview()) {
                    case 1:
                        smileRating.setSelectedSmile(BaseRating.TERRIBLE);
                        break;
                    case 2:
                        smileRating.setSelectedSmile(BaseRating.BAD);
                        break;
                    case 3:
                        smileRating.setSelectedSmile(BaseRating.OKAY);
                        break;
                    case 4:
                        smileRating.setSelectedSmile(BaseRating.GOOD);
                        break;
                    case 5:
                        smileRating.setSelectedSmile(BaseRating.GREAT);
                        break;
                    case 0:
                        smileRating.setSelectedSmile(BaseRating.NONE);
                        break;
                }
            }
        });
        String[] subjects = { "All", "Chemistry", "Physics", "Maths"};
        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);

    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        String text  = arg0.getItemAtPosition(position).toString();
        TextView Subjects = findViewById(R.id.SelectSubject);
        Subjects.setText(text);
        Subs = text;
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public void Back(View view) {
        finish();
        startActivity(new Intent(TutorProfileActivity.this, DashActivity.class));
    }
    public void updateData(String key,String value){
        DocumentReference docref = db.document("TutorData/"+documentId);
        docref.update(key,value);
        LoadData();
    }

    public void EditAboutMe(View view) {
        Context mContext = getApplicationContext();
        ArrayList<String> lineHint = new ArrayList<>();
        lineHint.add("About Me");

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(TutorProfileActivity.this)
                .setStyle(CustomAlertDialogue.Style.INPUT)
                .setTitle("Edit Description")
                .setPositiveText("Submit")
                .setPositiveColor(R.color.positive)
                .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                .setOnInputClicked(new CustomAlertDialogue.OnInputClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog, ArrayList<String> inputList) {
                        updateData(KEY_STATUS,inputList.get(0));
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

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(TutorProfileActivity.this)
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

            ImageView mImageView = findViewById(R.id.Tutor_Profile_pic);
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
                            Toast.makeText(TutorProfileActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            ImageDataModel upload = new ImageDataModel("Profile picture",
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TutorProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(TutorProfileActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile();

            fabProgressCircle.show();
        }
    }

    public void EditContactInfo(View view) {
        Context mContext = getApplicationContext();
        ArrayList<String> lineHint = new ArrayList<>();
        lineHint.add("Phone Number");
        lineHint.add("Street");
        lineHint.add("City");

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(TutorProfileActivity.this)
                .setStyle(CustomAlertDialogue.Style.INPUT)
                .setTitle("Edit contact and location info")
                .setPositiveText("Submit")
                .setPositiveColor(R.color.positive)
                .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                .setOnInputClicked(new CustomAlertDialogue.OnInputClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog, ArrayList<String> inputList) {
                        updateData(KEY_PHONE,inputList.get(0));
                        updateData(KEY_CITY,inputList.get(1));
                        updateData(KEY_STREET,inputList.get(1));
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

    public void EditHourlyRate(View view) {
        ArrayList<String> lineHint = new ArrayList<>();
        lineHint.add("Hourly rate");

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(TutorProfileActivity.this)
                .setStyle(CustomAlertDialogue.Style.INPUT)
                .setTitle("Edit hourly rate")
                .setPositiveText("Submit")
                .setPositiveColor(R.color.positive)
                .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                .setOnInputClicked(new CustomAlertDialogue.OnInputClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog, ArrayList<String> inputList) {
                        updateData(RATE,inputList.get(0));
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


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(TimeChoice.equals("from")) {
            if (hourOfDay > 12) {
                hourOfDay = hourOfDay - 12;
                String from = hourOfDay + " : " + minute + " PM";
                updateData(AVAIL_FROM, from);
                LoadData();
            } else {
                String from = hourOfDay + " : " + minute + " AM";
                updateData(AVAIL_FROM, from);
                LoadData();
            }
        }else if(TimeChoice.equals("to")){
            if (hourOfDay > 12) {
                hourOfDay = hourOfDay - 12;
                String to = hourOfDay + " : " + minute + " PM";
                updateData(AVAIL_TO, to);
                LoadData();
            } else {
                String to = hourOfDay + " : " + minute + " AM";
                updateData(AVAIL_TO, to);
                LoadData();
            }
        }


    }

    public void EditAvailFrom(View view) {
        TimeChoice = "from";
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    public void EditAvailableTo(View view) {
        TimeChoice = "to";
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    public void EditSubject(View view) {
        updateData(SUBJECT,Subs);

    }
}
