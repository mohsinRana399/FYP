package com.paradigmshift.fyp.viewpager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.paradigmshift.fyp.Chat.data.FriendDB;
import com.paradigmshift.fyp.Chat.data.GroupDB;
import com.paradigmshift.fyp.Chat.data.MainChatActivity;
import com.paradigmshift.fyp.Chat.data.SharedPreferenceHelper;
import com.paradigmshift.fyp.Chat.data.StaticConfig;
import com.paradigmshift.fyp.Chat.data.service.ServiceUtils;
import com.paradigmshift.fyp.FirebaseDatamodel.FoldableDataModel;
import com.paradigmshift.fyp.FirebaseDatamodel.ProfileDataModel;
import com.paradigmshift.fyp.FirebaseDatamodel.model.User;
import com.paradigmshift.fyp.MainActivity;
import com.paradigmshift.fyp.R;
import com.paradigmshift.fyp.dashitems.ProfileActivity;
import com.paradigmshift.fyp.dashitems.TutorProfileActivity;
import com.paradigmshift.fyp.drawer.DrawerAdapter;
import com.paradigmshift.fyp.drawer.DrawerItem;
import com.paradigmshift.fyp.drawer.SimpleItem;
import com.paradigmshift.fyp.drawer.SpaceItem;
import com.paradigmshift.fyp.fragments.ChatFragement;
import com.paradigmshift.fyp.fragments.ChemistryFragment;
import com.paradigmshift.fyp.fragments.MathFragment;
import com.paradigmshift.fyp.fragments.PhysicsFragment;
import com.paradigmshift.fyp.fragments.ProfileFragement;
import com.paradigmshift.fyp.fragments.RecyclerViewFragment;
import com.paradigmshift.fyp.fragments.ScrollFragment;
import com.paradigmshift.fyp.logreg.LoginActivity;
import com.paradigmshift.fyp.review.Review_activity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_FIND_TUTOR = 3;
    private static final int POS_LOGOUT = 5;
    private FirebaseAuth firebaseAuth;
    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    private String ROLE = "none";
    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        setTitle("");
        ButterKnife.bind(this);


        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        CheckRole();
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_FIND_TUTOR),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public androidx.fragment.app.Fragment getItem(int position) {
                switch (position % 4) {
                    case 1:
                        return ChemistryFragment.newInstance(getApplicationContext());
                    case 2:
                        return PhysicsFragment.newInstance(getApplicationContext());
                    case 3:
                        return MathFragment.newInstance(getApplicationContext());
                    default:
                        return RecyclerViewFragment.newInstance(getApplicationContext(), 1);
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 4) {
                    case 0:
                        return "Computer Science";
                    case 1:
                        return "Chemistry";
                    case 2:
                        return "Physics";
                    case 3:
                        return "Maths";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fcs.png?alt=media&token=cf7f5247-8f28-4ec6-80b5-f523157693a4");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fchem.jpg?alt=media&token=2e2ab1e8-72a7-446d-8929-6fc748e13804");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fphysics.jpg?alt=media&token=64d58b39-6b76-4ba9-807e-3568b97bd2d9");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "https://firebasestorage.googleapis.com/v0/b/fypfirebaseproject-9398c.appspot.com/o/Backgrounds%2Fmath.jpg?alt=media&token=43df1398-187b-4895-9a2f-c6814ff161f5");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        ScrollFragment sc = ScrollFragment.newInstance();
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        /*final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }*/


    }

    //For recyclerView items

    public void recyclerItems() {


    }

    //for drawer
    @Override
    public void onItemSelected(int position) {

        firebaseAuth = FirebaseAuth.getInstance();
        if (position == POS_LOGOUT) {
            FirebaseAuth.getInstance().signOut();
            FriendDB.getInstance(DashActivity.this).dropDB();
            GroupDB.getInstance(DashActivity.this).dropDB();
            ServiceUtils.stopServiceFriendChat(DashActivity.this.getApplicationContext(), true);
            finish();
            startActivity(new Intent(DashActivity.this, LoginActivity.class));
        } else if (position == POS_ACCOUNT) {
            if (ROLE.equals("Tutor")) {

                slidingRootNav.closeMenu();
                finish();
                startActivity(new Intent(DashActivity.this, TutorProfileActivity.class));
            } else {
                slidingRootNav.closeMenu();
                finish();
                startActivity(new Intent(DashActivity.this, ProfileActivity.class));
            }

        } else if (position == POS_MESSAGES) {

            slidingRootNav.closeMenu();
            startActivity(new Intent(DashActivity.this, MainChatActivity.class));
        } else if (position == POS_FIND_TUTOR) {
            slidingRootNav.closeMenu();
            startActivity(new Intent(DashActivity.this, Review_activity.class));
        } else {
            //slidingRootNav.closeMenu();
            //startActivity(new Intent(DashActivity.this, DashActivity.class));


        }

    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    public void CheckRole() {
        FirebaseDatabase.getInstance().getReference().child("user/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap hashUser = (HashMap) dataSnapshot.getValue();
                String tempR = (String) hashUser.get("Role");
                ROLE = tempR;
                if (tempR != null) {
                    if (tempR.equals("Student")) {
                        LoadData("userData", 1);
                    } else if (tempR.equals("Tutor")) {
                        LoadData("TutorData", 2);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DashActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LoadData(String CollectionName, int type) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String DocID = SharedPreferenceHelper.getInstance(DashActivity.this).getUID();
        DocumentReference profileReference = db.collection(CollectionName).document(StaticConfig.UID);


        TextView name = findViewById(R.id.Dash_name);
        TextView email = findViewById(R.id.Dash_email);
        ImageView profilePic = findViewById(R.id.Dash_profilePic);


        if (type == 1) {

            //Database fetch
            profileReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ProfileDataModel model = documentSnapshot.toObject(ProfileDataModel.class);
                    if (model != null) {
                        model.setDocumentID(documentSnapshot.getId());
                        name.setText(model.getName());
                        email.setText(model.getEmail());
                        Glide.with(getApplicationContext()).load(model.getProfilePicUrl()).into(profilePic);
                    } else {
                        name.setText("John Doe");
                        email.setText("JD@gmail");
                    }

                }
            });
        } else if (type == 2) {
            //Database fetch
            profileReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    FoldableDataModel model = documentSnapshot.toObject(FoldableDataModel.class);
                    if (model != null) {
                        model.setDocumentID(documentSnapshot.getId());
                        name.setText(model.getName());
                        email.setText(model.getEmail());
                        Glide.with(getApplicationContext()).load(model.getProfilePic()).into(profilePic);
                    } else {
                        name.setText("John Doe");
                        email.setText("JD@gmail");
                    }

                }
            });
        }


    }
}
