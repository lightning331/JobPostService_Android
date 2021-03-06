package com.kelvin.jacksgogo.CustomView.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.R;

/**
 * Created by PUMA on 1/11/2018.
 */

public class PostJobTabView extends RelativeLayout implements View.OnClickListener {

    Context mContext;

    LinearLayout mDescribeButton;
    LinearLayout mTimeButton;
    LinearLayout mAddressButton;
    LinearLayout mBudgetButton;
    LinearLayout mReportButton;
    ImageView imgTimeLine;
    ImageView imgAddressLine;
    ImageView imgBudgetLine;
    ImageView imgReportLine;
    public ImageView mDescribeImage;
    public ImageView mTimeImage;
    public ImageView mAddressImage;
    public ImageView mBudgetImage;
    public ImageView mReportImage;
    public TextView mDescribeText;
    public TextView mTimeText;
    public TextView mAddressText;
    public TextView mBudgetText;
    public TextView mReportText;

    private PostJobTabName postJobTabName;

    public enum PostJobTabName {
        DESCRIBE,
        TIME,
        ADDRESS,
        BUDGET,
        REPORT
    }

    public PostJobTabView(Context context) {
        super(context);        this.mContext = context;

        initView();

    }

    private void initView() {

        LayoutInflater mLayoutInflater      = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mTabView                    = mLayoutInflater.inflate(R.layout.view_post_job_tab, this);

        mDescribeButton     = (LinearLayout) mTabView.findViewById(R.id.btn_describe);
        mTimeButton         = (LinearLayout) mTabView.findViewById(R.id.btn_time);
        mAddressButton      = (LinearLayout) mTabView.findViewById(R.id.btn_address);
        mBudgetButton       = (LinearLayout) mTabView.findViewById(R.id.btn_budget);
        mReportButton       = (LinearLayout) mTabView.findViewById(R.id.btn_report);
        mDescribeImage      = (ImageView) mTabView.findViewById(R.id.img_my_services);
        mTimeImage          = (ImageView) mTabView.findViewById(R.id.img_time);
        mAddressImage       = (ImageView) mTabView.findViewById(R.id.img_address);
        mBudgetImage        = (ImageView) mTabView.findViewById(R.id.img_budget);
        mReportImage        = (ImageView) mTabView.findViewById(R.id.img_report);
        mDescribeText       = (TextView) mTabView.findViewById(R.id.lbl_describe);
        mTimeText           = (TextView) mTabView.findViewById(R.id.lbl_time);
        mAddressText        = (TextView) mTabView.findViewById(R.id.lbl_address);
        mBudgetText         = (TextView) mTabView.findViewById(R.id.lbl_budget);
        mReportText         = (TextView) mTabView.findViewById(R.id.lbl_report);
        imgTimeLine         = (ImageView) mTabView.findViewById(R.id.img_time_line);
        imgAddressLine      = (ImageView) mTabView.findViewById(R.id.img_address_line);
        imgBudgetLine       = (ImageView) mTabView.findViewById(R.id.img_budget_line);
        imgReportLine       = (ImageView) mTabView.findViewById(R.id.img_report_line);

        //mAddressButton.setOnClickListener(this);
    }

    public PostJobTabName getPostJobTabName() {
        return postJobTabName;
    }

    public void setTabName(PostJobTabName name, boolean isPost) {
        this.postJobTabName = name;

        mDescribeImage.setImageResource(R.mipmap.counter_greytick);
        mDescribeText.setTextColor(getResources().getColor(R.color.JGGGrey1));
        mTimeImage.setImageResource(R.mipmap.counter_greytick);
        mTimeText.setTextColor(getResources().getColor(R.color.JGGGrey1));
        mAddressImage.setImageResource(R.mipmap.counter_greytick);
        mAddressText.setTextColor(getResources().getColor(R.color.JGGGrey1));
        mBudgetImage.setImageResource(R.mipmap.counter_greytick);
        mBudgetText.setTextColor(getResources().getColor(R.color.JGGGrey1));
        mReportImage.setImageResource(R.mipmap.counter_greytick);
        mReportText.setTextColor(getResources().getColor(R.color.JGGGrey1));
        imgTimeLine.setImageResource(R.mipmap.line_dotted);
        imgAddressLine.setImageResource(R.mipmap.line_dotted);
        imgBudgetLine.setImageResource(R.mipmap.line_dotted);
        imgReportLine.setImageResource(R.mipmap.line_dotted);
        if (isPost) {
            mDescribeImage.setImageResource(R.mipmap.counter_grey);
            mAddressImage.setImageResource(R.mipmap.counter_grey);
            mTimeImage.setImageResource(R.mipmap.counter_grey);
            mBudgetImage.setImageResource(R.mipmap.counter_grey);
            mReportImage.setImageResource(R.mipmap.counter_grey);
        }

        switch (name) {
            case DESCRIBE:
                mDescribeButton.setOnClickListener(this);
                mDescribeImage.setImageResource(R.mipmap.counter_greentick);
                mDescribeText.setTextColor(getResources().getColor(R.color.JGGCyan));
                if (isPost) {
                    mDescribeImage.setImageResource(R.mipmap.counter_blueactive);
                }
                break;
            case TIME:
                mDescribeButton.setOnClickListener(this);
                mTimeButton.setOnClickListener(this);
                imgTimeLine.setImageResource(R.mipmap.line_full);
                mTimeImage.setImageResource(R.mipmap.counter_greentick);
                mTimeText.setTextColor(getResources().getColor(R.color.JGGCyan));
                if (isPost) {
                    mTimeImage.setImageResource(R.mipmap.counter_blueactive);
                    mDescribeImage.setImageResource(R.mipmap.counter_greytick);
                }
                break;
            case ADDRESS:
                mDescribeButton.setOnClickListener(this);
                mTimeButton.setOnClickListener(this);
                mAddressButton.setOnClickListener(this);
                imgTimeLine.setImageResource(R.mipmap.line_full);
                imgAddressLine.setImageResource(R.mipmap.line_full);
                mAddressImage.setImageResource(R.mipmap.counter_greentick);
                mAddressText.setTextColor(getResources().getColor(R.color.JGGCyan));
                if (isPost) {
                    mAddressImage.setImageResource(R.mipmap.counter_blueactive);
                    mDescribeImage.setImageResource(R.mipmap.counter_greytick);
                    mTimeImage.setImageResource(R.mipmap.counter_greytick);
                }
                break;
            case BUDGET:
                mDescribeButton.setOnClickListener(this);
                mTimeButton.setOnClickListener(this);
                mAddressButton.setOnClickListener(this);
                mBudgetButton.setOnClickListener(this);
                imgTimeLine.setImageResource(R.mipmap.line_full);
                imgAddressLine.setImageResource(R.mipmap.line_full);
                imgBudgetLine.setImageResource(R.mipmap.line_full);
                mBudgetImage.setImageResource(R.mipmap.counter_greentick);
                mBudgetText.setTextColor(getResources().getColor(R.color.JGGCyan));
                if (isPost) {
                    mBudgetImage.setImageResource(R.mipmap.counter_blueactive);
                    mDescribeImage.setImageResource(R.mipmap.counter_greytick);
                    mTimeImage.setImageResource(R.mipmap.counter_greytick);
                    mAddressImage.setImageResource(R.mipmap.counter_greytick);
                }
                break;
            case REPORT:
                mDescribeButton.setOnClickListener(this);
                mTimeButton.setOnClickListener(this);
                mAddressButton.setOnClickListener(this);
                mBudgetButton.setOnClickListener(this);
                mReportButton.setOnClickListener(this);
                imgTimeLine.setImageResource(R.mipmap.line_full);
                imgAddressLine.setImageResource(R.mipmap.line_full);
                imgBudgetLine.setImageResource(R.mipmap.line_full);
                imgReportLine.setImageResource(R.mipmap.line_full);
                mReportImage.setImageResource(R.mipmap.counter_greentick);
                mReportText.setTextColor(getResources().getColor(R.color.JGGCyan));
                if (isPost) {
                    mReportImage.setImageResource(R.mipmap.counter_blueactive);
                    mDescribeImage.setImageResource(R.mipmap.counter_greytick);
                    mTimeImage.setImageResource(R.mipmap.counter_greytick);
                    mAddressImage.setImageResource(R.mipmap.counter_greytick);
                    mBudgetImage.setImageResource(R.mipmap.counter_greytick);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        listener.onTabItemClick(view);
    }

    private OnTabItemClickListener listener;

    public interface OnTabItemClickListener {
        void onTabItemClick(View view);
    }

    public void setTabItemClickListener(OnTabItemClickListener listener) {
        this.listener = listener;
    }
}
