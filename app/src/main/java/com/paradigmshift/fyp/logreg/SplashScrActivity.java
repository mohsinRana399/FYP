package com.paradigmshift.fyp.logreg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.connectycube.auth.session.ConnectycubeSettings;
import com.connectycube.core.LogLevel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paradigmshift.fyp.R;

import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.PenPainter;

public class SplashScrActivity extends AppCompatActivity {

    private ImageView logoSplash;
    private Animation anim1, anim2, anim3;
    private SyncTextPathView ParadigmShiftLogo;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_scr);
        init();


        logoSplash.startAnimation(anim1);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ParadigmShiftLogo.setPathPainter(new PenPainter());
                ParadigmShiftLogo.setDuration(4000);
                ParadigmShiftLogo.startAnimation(0,1);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logoSplash.startAnimation(anim2);
                //logoSplash.setVisibility(View.GONE);


                logoSplash.startAnimation(anim3);
                anim3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                       // chmaraTech.setVisibility(View.VISIBLE);

                        finish();
                        startActivity(new Intent(SplashScrActivity.this,LoginActivity.class));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }

    /*private void initFirebase() {
        //Khoi tao thanh phan de dang nhap, dang ky
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String UID = user.getUid();
                    SplashScrActivity.this.finish();
                    // User is signed in
                    startActivity(new Intent(SplashScrActivity.this, LoginActivity.class));
                } else {
                    SplashScrActivity.this.finish();
                    // User is signed in
                    startActivity(new Intent(SplashScrActivity.this, LoginActivity.class));
                }
                // ...
            }
        };
    }*/

    private void init(){

        logoSplash = findViewById(R.id.ivLogoSplash);
        ParadigmShiftLogo = findViewById(R.id.ParadigmShiftLogo);
        anim1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        anim2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
        anim3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
    }

}
