package com.paradigmshift.fyp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paradigmshift.fyp.FirebaseDatamodel.FoldableDataModel;
import com.paradigmshift.fyp.Hire.HireActivity;
import com.paradigmshift.fyp.R;
import com.paradigmshift.fyp.viewpager.TestListViewAdapter;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhysicsFragment extends Fragment {

    private static Context ctx;
    @BindView(R.id.recyclerView_physics)
    ListView mRecyclerView;
    public static PhysicsFragment newInstance(Context context) {
        ctx = context;
        return new PhysicsFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_physics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final ArrayList<FoldableDataModel> myList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference profileReference = db.collection("TutorData");

        profileReference.document().getId();
        profileReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            FoldableDataModel minimodel = documentSnapshot.toObject(FoldableDataModel.class);
                            minimodel.setDocumentID(documentSnapshot.getId());

                            myList.add(minimodel);

                        }
                        // mAdapter = new TestRecyclerViewAdapter(myList,ctx);
                        // mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
                        myList.add(0,new FoldableDataModel("abc",3,"abc","abc","Physics Professor",3,"abc","abc","abc","abc","abc","abc"));
                        final TestListViewAdapter mAdapter = new TestListViewAdapter(ctx, myList);

                        for(int i=0;i<myList.size();i++){
                            int finalI = i;
                            myList.get(i).setRequestBtnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String name = myList.get(finalI).getName();
                                    String hourlyRate = myList.get(finalI).getRate();
                                    String street = myList.get(finalI).getStrretAddress();
                                    String city = myList.get(finalI).getCity();
                                    int review = myList.get(finalI).getReview();
                                    String pic = myList.get(finalI).getProfilePic();
                                    String back = myList.get(finalI).getBackgroundImage();

                                    Intent intent = new Intent(PhysicsFragment.ctx, HireActivity.class);

                                    String AvFrom = myList.get(finalI).getAvailableFrom();
                                    String AvTo = myList.get(finalI).getAvailableTo();
                                    String email = myList.get(finalI).getEmail();
                                    intent.putExtra("E",email);
                                    intent.putExtra("from", AvFrom);
                                    intent.putExtra("to", AvTo);
                                    intent.putExtra("n",name);
                                    intent.putExtra("h",hourlyRate);
                                    intent.putExtra("s",street);
                                    intent.putExtra("c",city);
                                    intent.putExtra("r",review);
                                    intent.putExtra("p",pic);
                                    intent.putExtra("b",back);

                                    startActivity(intent);

                                }
                            });
                        }

                        mAdapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(PhysicsFragment.ctx, HireActivity.class));
                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                // toggle clicked cell state
                                ((FoldingCell) view).toggle(false);
                                // register in adapter that state for selected cell is toggled
                                mAdapter.registerToggle(pos);
                            }
                        });
                        // Toast.makeText(ctx, myList.get(0).getName(), Toast.LENGTH_SHORT).show();
                    }
                });








        //Use this now

    }
}
