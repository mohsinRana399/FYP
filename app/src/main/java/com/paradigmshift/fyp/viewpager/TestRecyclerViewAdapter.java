package com.paradigmshift.fyp.viewpager;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hsalf.smilerating.SmileRating;
import com.paradigmshift.fyp.FirebaseDatamodel.FoldableDataModel;
import com.paradigmshift.fyp.R;
import com.ramotion.foldingcell.FoldingCell;


import java.util.ArrayList;
import java.util.HashSet;


public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.myViewHolder> {

    private ArrayList<FoldableDataModel> userList;
    private OnItemClickListener mListener;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    Context ctx;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public static class myViewHolder extends RecyclerView.ViewHolder {
        public ImageView mProfile;
        public TextView mName;
        public TextView mStatus;
        ImageView fold_ProfilePic;
        TextView fold_Name;
        TextView fold_Status;
        ImageView unfoldBackgroundImage;
        TextView StudentsTutored;
        TextView HourlyRate;
        ImageView unfold_profilePic;
        TextView unfold_Name;
        SmileRating unfold_smileRating;
        TextView StreetAddress;
        TextView CityAddress;
        TextView AvailableFrom;
        TextView AvailableTo;
        TextView TutionRequests;
        TextView HireBtn;

        public myViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            FoldingCell cell = (FoldingCell) itemView;

            fold_ProfilePic = cell.findViewById(R.id.card_profile);
            fold_Name = cell.findViewById(R.id.card_Name);
            fold_Status = cell.findViewById(R.id.card_Status);
            unfoldBackgroundImage = cell.findViewById(R.id.hire_head_image1);
            StudentsTutored = cell.findViewById(R.id.hire_StudentsTutored);
            HourlyRate = cell.findViewById(R.id.hire_hourlyRate);
            unfold_profilePic = cell.findViewById(R.id.hire_head_image);
            unfold_Name = cell.findViewById(R.id.hire_name);
            unfold_smileRating = cell.findViewById(R.id.hire_smile_rating);
            StreetAddress = cell.findViewById(R.id.hire_street);
            CityAddress = cell.findViewById(R.id.hire_city);
            AvailableFrom = cell.findViewById(R.id.hire_date);
            AvailableTo = cell.findViewById(R.id.hire_time);
            TutionRequests = cell.findViewById(R.id.fold_tutorRequests);
            HireBtn = cell.findViewById(R.id.hire_confirm);
            mProfile = itemView.findViewById(R.id.card_profile);
            mName = itemView.findViewById(R.id.card_Name);
            mStatus = itemView.findViewById(R.id.card_Status);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public TestRecyclerViewAdapter(ArrayList<FoldableDataModel> uList, Context context) {
        this.userList = uList;
        this.ctx = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(ctx).inflate(R.layout.cell, parent, false), mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int position) {
        FoldableDataModel item = userList.get(position);

        viewHolder.fold_Name.setText(item.getName());
        viewHolder.fold_Status.setText(item.getStatus());
        viewHolder.StudentsTutored.setText("" + item.getStudentsTutored());
        viewHolder.HourlyRate.setText(item.getRate());
        viewHolder.unfold_Name.setText(item.getName());


        viewHolder.StreetAddress.setText(item.getStrretAddress());
        viewHolder.CityAddress.setText(item.getCity());
        viewHolder.AvailableFrom.setText(item.getAvailableFrom());
        viewHolder.AvailableTo.setText(item.getAvailableTo());
        viewHolder.TutionRequests.setText(item.getTutorRequests() + "people have requested this tutor");
        Glide.with(ctx).load(item.getBackgroundImage()).into(viewHolder.unfoldBackgroundImage);
        Glide.with(ctx).load(item.getProfilePic()).into(viewHolder.fold_ProfilePic);
        Glide.with(ctx).load(item.getProfilePic()).into(viewHolder.unfold_profilePic);


    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }


/*
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                break;
        }
    }*/
}