package com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Jobs;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.R;

/**
 * Created by PUMA on 12/12/2017.
 */

public class JobStatusSummaryWorkProgressView extends RelativeLayout {

    private Context mContext;

    public ImageView imgStartWork;
    public LinearLayout startWorkLine;
    public TextView lblStartTime;
    public TextView lblUserName;
    public TextView lblStartedWork;
    public LinearLayout billableLayout;

    public JobStatusSummaryWorkProgressView(Context context) {
        super(context);
        this.mContext = context;

        initView();
    }

    private void initView() {
        LayoutInflater mLayoutInflater       = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view                            = mLayoutInflater.inflate(R.layout.view_job_status_summary_work_progress, this);

        imgStartWork = view.findViewById(R.id.img_start_time);
        startWorkLine = view.findViewById(R.id.start_time_line);
        lblStartTime = view.findViewById(R.id.lbl_start_time);
        lblUserName = view.findViewById(R.id.lbl_confirmed_user_name);
        lblStartedWork = view.findViewById(R.id.lbl_started_work);
        billableLayout = view.findViewById(R.id.billable_item_layout);
    }
}
