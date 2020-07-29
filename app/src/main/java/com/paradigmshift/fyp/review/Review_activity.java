package com.paradigmshift.fyp.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paradigmshift.fyp.Chat.data.StaticConfig;
import com.paradigmshift.fyp.FirebaseDatamodel.AppointmentDataModel;
import com.paradigmshift.fyp.FirebaseDatamodel.FoldableDataModel;
import com.paradigmshift.fyp.FirebaseDatamodel.ProfileDataModel;
import com.paradigmshift.fyp.R;
import com.paradigmshift.fyp.dashitems.ProfileActivity;
import com.paradigmshift.fyp.dashitems.TutorProfileActivity;
import com.paradigmshift.fyp.viewpager.DashActivity;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class Review_activity extends AppCompatActivity implements RatingDialogListener {


    public static String TUTOR_DOC_ID = null;
    public static String APPOINTMENT_DOC_ID = null;
    public static String NAME = null;
    public static String REVIEW = null;
    public static int revNum= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_activity);

        showRatingDialog_example1();
        Toast.makeText(this, "your ratings : " +APPOINTMENT_DOC_ID, Toast.LENGTH_SHORT).show();
    }

    private void showRatingDialog_example1() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(3)
                .setTitle("Rate your session")
                .setStarColor(R.color.fab_ripple)
                .setNoteDescriptionTextColor(R.color.fab_ripple)
                .setTitleTextColor(R.color.black)
                .setDescriptionTextColor(R.color.black)
                .setCommentTextColor(R.color.black)
                .setCommentBackgroundColor(R.color.descriptionTextColor)
                .setWindowAnimation(R.style.MyDialogSlideHorizontalAnimation)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.hintTextColor)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(Review_activity.this)
                .show();
    }

    @Override
    public void onNegativeButtonClicked() {

        finish();
        startActivity(new Intent(Review_activity.this, DashActivity.class));

    }

    @Override
    public void onNeutralButtonClicked() {

        finish();
        startActivity(new Intent(Review_activity.this, DashActivity.class));
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {

        REVIEW = s;
        revNum = i;
        getData();

    }
    public String[] getData(){
        final String[] did = new String[3];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appointments")
                .whereEqualTo("sced_by", StaticConfig.UID)
                .whereEqualTo("sced_review","")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AppointmentDataModel adm = document.toObject(AppointmentDataModel.class);
                                //Toast.makeText(Review_activity.this, "with : " +document.getId(), Toast.LENGTH_SHORT).show();
                                //String name = adm.getSced_with().substring(0,  adm.getSced_with().indexOf("@"));
                                updateReview(document.getId());
                                getTutorID(adm.getSced_with());
                            }
                        } else {

                        }
                    }
                });
        return did;
    }
    public void updateReview(String appDoc){
        if(REVIEW != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference AppRef = db.collection("appointments").document(appDoc);
            AppRef
                    .update("sced_review", REVIEW+".")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error updating document", e);
                        }
                    });


        }else{
            Toast.makeText(this, "REVIEW was null", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(Review_activity.this, DashActivity.class));
        }

    }
    public void getTutorID(String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("TutorData")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AppointmentDataModel adm = document.toObject(AppointmentDataModel.class);
                                getReviews(document.getId());
                            }
                        } else {

                        }
                    }
                });

    }


    public void getReviews(String tutDoc){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference tuttorRef = db.collection("TutorData").document(tutDoc);

        tuttorRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FoldableDataModel model = documentSnapshot.toObject(FoldableDataModel.class);
                updateRevNum(tutDoc,model.getReview());
            }
        });

    }
    public void updateRevNum(String tutDoc , int review){

        if(revNum != 0){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference tuttorRef = db.collection("TutorData").document(tutDoc);

            //Averaging out

            review = review + revNum;
            float latestRev = review/2;
            int rev = Math.round(latestRev);

            tuttorRef
                    .update("review", rev)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Review_activity.this, "Thank you! ", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(Review_activity.this, DashActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error updating document", e);
                        }
                    });
        }else{
            Toast.makeText(this, "revNUm was 0", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(Review_activity.this, DashActivity.class));
        }

    }



    public void rev(View view) {
        Toast.makeText(this, "revNUm was 0", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(Review_activity.this, DashActivity.class));
    }
}
