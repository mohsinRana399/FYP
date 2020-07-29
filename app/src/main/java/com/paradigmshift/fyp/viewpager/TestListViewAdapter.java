package com.paradigmshift.fyp.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.paradigmshift.fyp.FirebaseDatamodel.FoldableDataModel;
import com.paradigmshift.fyp.R;
import com.ramotion.foldingcell.FoldingCell;


import java.util.ArrayList;
import java.util.HashSet;

public class TestListViewAdapter extends ArrayAdapter<FoldableDataModel> {

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    Context ctx;

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    public String TestName = "";
    public TestListViewAdapter(Context context, ArrayList<FoldableDataModel> userItems) {
        super(context, 0, userItems);
        this.ctx = context;
    }
    // View lookup cache
    private static class ViewHolder {

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

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        FoldableDataModel item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource

        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.fold_ProfilePic = cell.findViewById(R.id.card_profile);
            viewHolder.fold_Name = cell.findViewById(R.id.card_Name);
            viewHolder.fold_Status = cell.findViewById(R.id.card_Status);
            viewHolder.unfoldBackgroundImage = cell.findViewById(R.id.hire_head_image1);
            viewHolder.StudentsTutored = cell.findViewById(R.id.hire_StudentsTutored);
            viewHolder.HourlyRate = cell.findViewById(R.id.hire_hourlyRate);
            viewHolder.unfold_profilePic = cell.findViewById(R.id.hire_head_image);
            viewHolder.unfold_Name = cell.findViewById(R.id.hire_name);
            viewHolder.unfold_smileRating = cell.findViewById(R.id.hire_smile_rating);
            viewHolder.StreetAddress = cell.findViewById(R.id.hire_street);
            viewHolder.CityAddress = cell.findViewById(R.id.hire_city);
            viewHolder.AvailableFrom = cell.findViewById(R.id.hire_date);
            viewHolder.AvailableTo = cell.findViewById(R.id.hire_time);
            viewHolder.TutionRequests = cell.findViewById(R.id.fold_tutorRequests);
            viewHolder.HireBtn = cell.findViewById(R.id.hire_confirm);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

       // bind data from selected element to view through view holder

        viewHolder.fold_Name.setText(item.getName());
        viewHolder.fold_Status.setText(item.getStatus());
        viewHolder.StudentsTutored.setText(""+item.getStudentsTutored());
        viewHolder.HourlyRate.setText(item.getRate());
        viewHolder.unfold_Name.setText(item.getName());



        viewHolder.StreetAddress.setText(item.getStrretAddress());
        viewHolder.CityAddress.setText(item.getCity());
        viewHolder.AvailableFrom.setText(item.getAvailableFrom());
        viewHolder.AvailableTo.setText(item.getAvailableTo());
        viewHolder.unfold_smileRating.post(new Runnable() {
            @Override
            public void run() {
                switch (item.getReview()) {
                    case 1:
                        viewHolder.unfold_smileRating.setSelectedSmile(BaseRating.TERRIBLE);
                        break;
                    case 2:
                        viewHolder.unfold_smileRating.setSelectedSmile(BaseRating.BAD);
                        break;
                    case 3:
                        viewHolder.unfold_smileRating.setSelectedSmile(BaseRating.OKAY);
                        break;
                    case 4:
                        viewHolder.unfold_smileRating.setSelectedSmile(BaseRating.GOOD);
                        break;
                    case 5:
                        viewHolder.unfold_smileRating.setSelectedSmile(BaseRating.GREAT);
                        break;
                    case 0:
                        viewHolder.unfold_smileRating.setSelectedSmile(BaseRating.NONE);
                        break;
                }
            }
        });
        viewHolder.TutionRequests.setText(item.getTutorRequests() + " people have requested this tutor");
       // Glide.with(ctx).load(item.getBackgroundImage()).into(viewHolder.unfoldBackgroundImage);
        Glide.with(ctx).load(item.getProfilePic()).into(viewHolder.fold_ProfilePic);
        Glide.with(ctx).load(item.getProfilePic()).into(viewHolder.unfold_profilePic);

        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            viewHolder.HireBtn.setOnClickListener(item.getRequestBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.HireBtn.setOnClickListener(defaultRequestBtnClickListener);

        }

        return cell;


    }


    // simple methods for register cell state changes
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
}