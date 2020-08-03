package com.paradigmshift.fyp.Hire;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.paradigmshift.fyp.Chat.data.MainChatActivity;
import com.paradigmshift.fyp.Chat.data.StaticConfig;
import com.paradigmshift.fyp.FirebaseDatamodel.AppointmentDataModel;
import com.paradigmshift.fyp.Hire.payment.PaymentActivity;
import com.paradigmshift.fyp.Notifications.AlertReceiver;
import com.paradigmshift.fyp.Notifications.App;
import com.paradigmshift.fyp.Notifications.ReviewReceiver;
import com.paradigmshift.fyp.R;
import com.paradigmshift.fyp.fragments.DatePickerFragment;
import com.paradigmshift.fyp.fragments.TimePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class HireActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener ,  TimePickerDialog.OnTimeSetListener{


    private NotificationManagerCompat notificationManager;
    public Calendar AppointmentTime = Calendar.getInstance();
    public Calendar ReviewTime = Calendar.getInstance();
    ImageView ProfilePic;
    ImageView background;
    TextView HourlyRate;
    TextView Name;
    TextView StreetAddress;
    TextView CityAddress;
    TextView Date;
    TextView Time;
    SmileRating smileRating;
    String name;
    String hourly;
    String AvailFrom;
    String AvailTO;
    String email;
    int Hrs = 0;
    int Min = 0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);

        notificationManager = NotificationManagerCompat.from(this);

        initViews();
        LoadInfo();
    }

    private void initViews() {
        ProfilePic = findViewById(R.id.hire_head_image);
        background = findViewById(R.id.hire_head_image1);
        HourlyRate = findViewById(R.id.hire_hourlyRate);
        Name = findViewById(R.id.hire_name);
        StreetAddress = findViewById(R.id.hire_street);
        CityAddress = findViewById(R.id.hire_city);
        Date = findViewById(R.id.Session_date);
        Time = findViewById(R.id.hire_timePick);
        smileRating = findViewById(R.id.hire_smile_rating);

    }

    private void LoadInfo(){



        name = getIntent().getStringExtra("n");
        hourly = getIntent().getStringExtra("h");
        AvailFrom = getIntent().getStringExtra("from");
        AvailTO = getIntent().getStringExtra("to");
        email = getIntent().getStringExtra("E");
        String city = getIntent().getStringExtra("c");
        String street = getIntent().getStringExtra("s");
        int review = getIntent().getIntExtra("r",0);
        String pic = getIntent().getStringExtra("p");
        //String back = getIntent().getStringExtra("b");

            Glide.with(getApplicationContext()).load(pic).into(ProfilePic);
           // Glide.with(getApplicationContext()).load(back).into(background);
            HourlyRate.setText(hourly);
            Name.setText(name);
            StreetAddress.setText(street);
            CityAddress.setText(city);
            switch (review) {
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


    public void OpenDate(View view) {

        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    public void OpenTime(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

        AppointmentTime.set(Calendar.YEAR, year);
        AppointmentTime.set(Calendar.MONTH, month);
        AppointmentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        ReviewTime.set(Calendar.YEAR, year);
        ReviewTime.set(Calendar.MONTH, month);
        ReviewTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        Date.setText(currentDateString);
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        AppointmentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        AppointmentTime.set(Calendar.MINUTE, minute);
        AppointmentTime.set(Calendar.SECOND, 0);
        ReviewTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        ReviewTime.set(Calendar.MINUTE, minute);
        ReviewTime.set(Calendar.SECOND, 0+1);
            Min = minute;
            if(hourOfDay>12){
                hourOfDay = hourOfDay-12;
                Hrs = hourOfDay;
                Time.setText( hourOfDay + " : " + minute + " PM");
            }else{
                Hrs = hourOfDay;
                Time.setText( hourOfDay + " : " + minute + " AM");
            }


    }


    public void hireTutor(View view) {

        //THE ACTUAL WORK !!
        //Toast.makeText(this, Hrs + " : " + Min, Toast.LENGTH_SHORT).show();
        if(ValidateSchedule()) {
            new AlertDialog.Builder(HireActivity.this, R.style.MyAlertDialogStyle)
                    .setTitle("Hire tutor")
                    .setMessage("Are you sure want to hire " + name + "?\nHourly rate " + hourly
                            + "\nSession scheduled for " + Date.getText() + "\n" + Time.getText())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent(HireActivity.this, PaymentActivity.class);
                            intent.putExtra("date",""+Date.getText());
                            intent.putExtra("time",""+Time.getText());
                            intent.putExtra("name",""+name);
                            intent.putExtra("email",""+email);
                            startActivity(intent);
                            finish();
                            /*
                            SendNotification("You have hired : " + name + "\nYour tutoring session will be held on : "
                                    + Date.getText() + "\n" + Time.getText());
                            confirmAppointment();
                            startAlarm();
                            StartReviewTimer();
                            Intent intent = new Intent(HireActivity.this, MainChatActivity.class);
                            intent.putExtra("n",name);
                            intent.putExtra("E",email);
                            startActivity(intent);
                            finish();*/

                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
        else{
            new AlertDialog.Builder(HireActivity.this, R.style.MyAlertDialogStyle)
                    .setTitle("Error")
                    .setMessage("Please select a valid date and time within the available hours of the tutor")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    }).show();
        }
    }

    private boolean ValidateSchedule() {

        if(Date.getText().equals("Session date") ){
            return false;
        }
        if(Time.getText().equals("Session time") ){
            return false;
        }
        if(convertHours()[0] > Hrs || convertHours()[2] < Hrs){
            return false;
        }

        return true;
    }
    public int[] convertHours(){

        int time[] = new int[4];
        String[] Availtime = AvailFrom.split(":");
        String hrs = Availtime[0];
        String min = Availtime[1];
        String mins ="";
        for(int i=0;i<min.length();i++) {
            if(Character.isDigit(min.charAt(i))) {
                mins = ""+mins + min.charAt(i);
            }
        }
        int Ah = Integer.parseInt(hrs.trim());
        int Am = Integer.parseInt(mins);

        String[] Totime = AvailTO.split(":");
        String TOhrs = Totime[0];
        String TOmin = Totime[1];
        String TOmins ="";
        for(int i=0;i<min.length();i++) {
            if(Character.isDigit(TOmin.charAt(i))) {
                TOmins = ""+TOmins + TOmin.charAt(i);
            }
        }
        int Th = Integer.parseInt(TOhrs.trim());
        int Tm = Integer.parseInt(TOmins);

        time[0] = Ah;
        time[1] = Am;
        time[2] = Th;
        time[3] = Tm;

        return time;
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

        if (AppointmentTime.before(Calendar.getInstance())) {
            AppointmentTime.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, AppointmentTime.getTimeInMillis(), pendingIntent);
    }
    public void StartReviewTimer(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, ReviewReceiver.class);
        intent.putExtra("todo", "Tell us about your session with " + name);
        intent.putExtra("notificationId", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);



        alarmManager.setExact(AlarmManager.RTC_WAKEUP, ReviewTime.getTimeInMillis(), pendingIntent);
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
                Date.getText().toString(),
                Time.getText().toString(),
                email,
                "");

        db.collection("appointments").document(appointID).set(newAppointment);
    }


}
