package com.kelvin.jacksgogo.CustomView.RecyclerViewCell.JobDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kelvin.jacksgogo.R;

/**
 * Created by PUMA on 12/12/2017.
 */

public class JobMainFooterView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;

    private LinearLayout reportLayout;
    private LinearLayout invoiceLayout;
    private LinearLayout reviewLayout;
    private LinearLayout tipLayout;
    private LinearLayout rehireLayout;

    public JobMainFooterView(Context context) {
        super(context);
        this.mContext = context;

        initView();
    }

    private void initView() {
        LayoutInflater mLayoutInflater       = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view                            = mLayoutInflater.inflate(R.layout.view_job_main_footer, this);

        reportLayout = (LinearLayout) view.findViewById(R.id.job_report_layout);
        invoiceLayout = (LinearLayout) view.findViewById(R.id.job_invoice_layout);
        reviewLayout = (LinearLayout) view.findViewById(R.id.job_review_layout);
        tipLayout = (LinearLayout) view.findViewById(R.id.job_tip_layout);
        rehireLayout = (LinearLayout) view.findViewById(R.id.job_rehire_layout);

        reportLayout.setOnClickListener(this);
        invoiceLayout.setOnClickListener(this);
        reviewLayout.setOnClickListener(this);
        tipLayout.setOnClickListener(this);
        rehireLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
