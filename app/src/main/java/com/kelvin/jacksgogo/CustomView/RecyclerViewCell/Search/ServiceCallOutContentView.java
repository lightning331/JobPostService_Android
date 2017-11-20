package com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.R;
import com.makeramen.roundedimageview.RoundedImageView;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by PUMA on 11/17/2017.
 */

public class ServiceCallOutContentView extends RelativeLayout {

    Context mContext;
    LayoutInflater mLayoutInflater;

    View callOutContentView;

    TextView lblServiceTitle;
    MaterialRatingBar rateBar;
    RoundedImageView imgUserAvatar;
    TextView lblUserName;
    TextView lblPrice;

    public ServiceCallOutContentView(Context context) {
        super(context);

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        callOutContentView  = mLayoutInflater.inflate(R.layout.search_service_callout_view, this);

        lblServiceTitle = (TextView) callOutContentView.findViewById(R.id.lbl_callout_service_title);
        rateBar = (MaterialRatingBar) callOutContentView.findViewById(R.id.service_callout_user_ratingbar);
        imgUserAvatar = (RoundedImageView) callOutContentView.findViewById(R.id.img_service_callout_user_avatar);
        lblUserName = (TextView) callOutContentView.findViewById(R.id.lbl_service_callout_username);
        lblPrice = (TextView) callOutContentView.findViewById(R.id.lbl_service_callout_price);
    }
}