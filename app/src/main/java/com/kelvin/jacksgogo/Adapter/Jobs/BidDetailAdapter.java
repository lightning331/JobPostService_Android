package com.kelvin.jacksgogo.Adapter.Jobs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Jobs.JobDetailDescriptionCell;
import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Jobs.JobDetailReferenceNoCell;
import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Jobs.UserNameRatingCell;
import com.kelvin.jacksgogo.R;

/**
 * Created by PUMA on 12/14/2017.
 */

public class BidDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    public BidDetailAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View biderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job_detail_user_name_rating, parent, false);
            UserNameRatingCell biderCell = new UserNameRatingCell(mContext, biderView);
            biderCell.ratingBar.setRating((float)4.8);
            return biderCell;
        } else if (viewType == 1) {
            View descriptionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job_detail_description, parent, false);
            JobDetailDescriptionCell descriptionCell = new JobDetailDescriptionCell(descriptionView);
            descriptionCell.descriptionImage.setImageResource(R.mipmap.icon_info);
            descriptionCell.setDescription("We are a team of 5 with 8+ experience in cleaning. We can send over 2 cleaners and have the work done in 1 hour.");
            return descriptionCell;
        } else if (viewType == 2) {
            View descriptionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job_detail_description, parent, false);
            JobDetailDescriptionCell descriptionCell = new JobDetailDescriptionCell(descriptionView);
            descriptionCell.titleLayout.setVisibility(View.VISIBLE);
            descriptionCell.titleLayout.setOrientation(LinearLayout.VERTICAL);
            descriptionCell.setTitle("$100.00", true);
            descriptionCell.setDescription("- Our own supplies $20.");
            return descriptionCell;
        } else if (viewType == 3) {
            View descriptionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job_detail_description, parent, false);
            JobDetailDescriptionCell descriptionCell = new JobDetailDescriptionCell(descriptionView);
            descriptionCell.titleLayout.setVisibility(View.VISIBLE);
            descriptionCell.titleLayout.setOrientation(LinearLayout.VERTICAL);
            descriptionCell.setTitle("$800.00 for 10", true);
            descriptionCell.setDescription("- Min. 3 days prior booking required. Booking subject to availability." +
                    "- Must be same address.");
            return descriptionCell;
        } else if (viewType == 4) {
            View descriptionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job_detail_description, parent, false);
            JobDetailDescriptionCell descriptionCell = new JobDetailDescriptionCell(descriptionView);
            descriptionCell.descriptionImage.setImageResource(R.mipmap.icon_reschedule);
            descriptionCell.titleLayout.setVisibility(View.VISIBLE);
            descriptionCell.setTitle("Rescheduling:", true);
            descriptionCell.setDescription("at least 1 day before.");
            return descriptionCell;
        } else if (viewType == 5) {
            View referenceView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job_detail_reference_no, parent, false);
            JobDetailReferenceNoCell jobDetailReferenceNoCell = new JobDetailReferenceNoCell(referenceView);
            jobDetailReferenceNoCell.lblReferenceNo.setText("Proposal reference no: P38235-11");
            jobDetailReferenceNoCell.lblPostedDate.setText("Posted on 14 Dec, 2017");
            return jobDetailReferenceNoCell;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
