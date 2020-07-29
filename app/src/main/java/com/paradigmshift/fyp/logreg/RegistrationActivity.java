package com.paradigmshift.fyp.logreg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.connectycube.core.EntityCallback;
import com.connectycube.core.exception.ResponseException;
import com.connectycube.users.ConnectycubeUsers;
import com.connectycube.users.model.ConnectycubeUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paradigmshift.fyp.FirebaseDatamodel.ProfileDataModel;
import com.paradigmshift.fyp.MainActivity;
import com.paradigmshift.fyp.R;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView logo, joinus;
    private AutoCompleteTextView  email, password,repass,fullName;
    private Button signup;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser ux;
    private FirebaseDatabase firebaseDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeGUI();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String inputPw = password.getText().toString().trim();
                final String inputEmail = email.getText().toString().trim();
                final String inputrepw = repass.getText().toString().trim();
                final String inputFullName = fullName.getText().toString().trim();

                if(validateInput(inputPw,
                        inputEmail,
                        inputrepw,
                        inputFullName))
                    registerUser(inputFullName, inputPw, inputEmail);

            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });

    }


    private void initializeGUI(){

        logo = findViewById(R.id.ivRegLogo);
        joinus = findViewById(R.id.ivJoinUs);
        email =  findViewById(R.id.atvEmailReg);
        repass = findViewById(R.id.atvRePasswordReg);
        fullName = findViewById(R.id.atvFullnameReg);
        password = findViewById(R.id.atvPasswordReg);
        signin = findViewById(R.id.tvSignIn);
        signup = findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }




    private void registerUser(final String inputName, final String inputPw, String inputEmail) {

        progressDialog.setMessage("Verificating...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    sendUserData(inputName, inputPw,inputEmail);
                    ux = firebaseAuth.getCurrentUser();
                    if(ux != null) {
                       CreateProfile(inputName,inputEmail,ux.getUid());
                    }
                    Toast.makeText(RegistrationActivity.this,"You've been registered successfully.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this,"Error : "+ task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void sendUserData(String username, String password, String Email){

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference users = firebaseDatabase.getReference("users");
        UserProfile user = new UserProfile(username, password,Email);
        users.push().setValue(user);

    }
    private void CreateProfile(String Name,String Email,String UID){

        ProfileDataModel userProfile = new ProfileDataModel(Name,"Enter Your Title","Describe Yourself",0,"Your Phone No",Email,"Your Location");
        userProfile.setDocumentID(UID);
        userProfile.setProfilePicUrl("https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/uploads%2Fdefault_profile.png?alt=media&token=9a2c008f-cfbf-4a92-8312-1e371e7a8800");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("userData").document(UID).set(userProfile);

    }

    private boolean validateInput( String inPw, String inEmail,String inRePass, String inFullName){


        if(inPw.isEmpty()){
            password.setError("Password is empty.");
            return false;
        }
        if(inEmail.isEmpty()){
            email.setError("Email is empty.");
            return false;
        }
        if(inRePass.isEmpty()){
            repass.setError("Username is empty.");
            return false;
        }
        if(inFullName.isEmpty()){
            fullName.setError("Email is empty.");
            return false;
        }
        if(!inPw.equals(inRePass)){
            repass.setError("Passwords don't Match");
            return false;
        }

        return true;
    }
}
