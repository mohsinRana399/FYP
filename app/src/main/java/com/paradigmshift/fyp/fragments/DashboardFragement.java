package com.paradigmshift.fyp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.paradigmshift.fyp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardFragement extends Fragment {

    private MaterialViewPager mViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dashboard_fragment, container, false);



    }


}
