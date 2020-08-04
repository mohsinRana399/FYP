package com.paradigmshift.fyp.Hire.payment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.VolleyError;
import com.checkout.android_sdk.PaymentForm;
import com.checkout.android_sdk.PaymentForm.PaymentFormCallback;
import com.checkout.android_sdk.Response.CardTokenisationFail;
import com.checkout.android_sdk.Response.CardTokenisationResponse;
import com.checkout.android_sdk.Utils.Environment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paradigmshift.fyp.Chat.data.MainChatActivity;
import com.paradigmshift.fyp.Chat.data.StaticConfig;
import com.paradigmshift.fyp.FirebaseDatamodel.AppointmentDataModel;
import com.paradigmshift.fyp.Hire.HireActivity;
import com.paradigmshift.fyp.MainActivity;
import com.paradigmshift.fyp.Notifications.AlertReceiver;
import com.paradigmshift.fyp.Notifications.App;
import com.paradigmshift.fyp.Notifications.ReviewReceiver;
import com.paradigmshift.fyp.R;

import java.util.Calendar;
import java.util.Locale;

public class PaymentActivity extends Activity {

    private PaymentForm mPaymentForm;
    private ProgressDialog mProgressDialog;
    private NotificationManagerCompat notificationManager;
    private String date;
    private String time;
    private String name;
    private String email;
    private HireActivity h;
    // Callback used for the Payment Form interaction
    private final PaymentFormCallback mFormListener = new PaymentFormCallback() {
        @Override
        public void onFormSubmit() {
            mProgressDialog.show(); // show loader
        }

        @Override
        public void onTokenGenerated(CardTokenisationResponse response) {
            mProgressDialog.dismiss(); // dismiss the loader
            mPaymentForm.clearForm(); // clear the form
            displayMessage("Token", response.getToken());
        }

        @Override
        public void onError(CardTokenisationFail response) {
            displayMessage("Token Error", response.getErrorType());
        }

        @Override
        public void onNetworkError(VolleyError error) {
            displayMessage("Network Error", String.valueOf(error));
        }

        @Override
        public void onBackPressed() {
            displayMessage("Back", "The user decided to leave the payment page.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // initialise the loader
        mProgressDialog = new ProgressDialog(PaymentActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading...");

        //notifications
        notificationManager = NotificationManagerCompat.from(this);


        mPaymentForm = findViewById(R.id.checkout_card_form);
        mPaymentForm
                .setFormListener(mFormListener)
                .setEnvironment(Environment.SANDBOX)
                .setKey("pk_test_6e40a700-d563-43cd-89d0-f9bb17d35e73")
                .setDefaultBillingCountry(Locale.UK);

        //getting inents
         date = getIntent().getStringExtra("date");
         time = getIntent().getStringExtra("time");
         name = getIntent().getStringExtra("name");
         email = getIntent().getStringExtra("email");
         h= new HireActivity();
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void skip(View view) {


        SendNotification("You have hired : " + name + "\nYour tutoring session will be held on : "
                + date + "\n" + time);
        confirmAppointment();
        startAlarm();
        StartReviewTimer();
        Intent intent = new Intent(PaymentActivity.this, MainChatActivity.class);
        intent.putExtra("n",name);
        intent.putExtra("E",email);
        startActivity(intent);
        finish();
    }
    public void SendNotification(String message){
        Notification notification = new NotificationCompat.Builder(this, App.INSTANT_NOTIFY)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Appointment scheduled")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }
    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("notificationId", 1);
        intent.putExtra("todo", " You have a tutor session scheduled with : "+name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (h.AppointmentTime.before(Calendar.getInstance())) {
            h.AppointmentTime.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, h.AppointmentTime.getTimeInMillis(), pendingIntent);
    }
    public void StartReviewTimer(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, ReviewReceiver.class);
        intent.putExtra("todo", "Tell us about your session with " + name);
        intent.putExtra("notificationId", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);



        alarmManager.setExact(AlarmManager.RTC_WAKEUP, h.ReviewTime.getTimeInMillis(), pendingIntent);
    }
    public void confirmAppointment(){

        /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        reference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String tutorID = datas.
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/
        String appointID = StaticConfig.UID+"_"+email;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AppointmentDataModel newAppointment = new AppointmentDataModel(
                StaticConfig.UID,
                date,
                time,
                email,
                "");

        db.collection("appointments").document(appointID).set(newAppointment);
    }
}