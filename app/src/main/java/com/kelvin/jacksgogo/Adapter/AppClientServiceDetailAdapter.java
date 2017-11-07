package com.kelvin.jacksgogo.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.Activities.Appointment.AppOriginalPostDetailActivity;
import com.kelvin.jacksgogo.Activities.Appointment.JGGMapViewActivity;
import com.kelvin.jacksgogo.Fragments.Appointments.AppClientServiceDetailFragment;
import com.kelvin.jacksgogo.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.synnapps.carouselview.CarouselView;


import me.zhanghai.android.materialratingbar.MaterialRatingBar;


/**
 * Created by PUMA on 11/3/2017.
 */

public class AppClientServiceDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppClientServiceDetailFragment mContext;
    private boolean expandState = false; // true: expanded, false: not expanded

    public void setExpandState(boolean expandState) {
        this.expandState = expandState;
    }

    public boolean isExpandState() {
        return expandState;
    }


    public AppClientServiceDetailAdapter(AppClientServiceDetailFragment context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                View nextStepTitleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_next_step_title_cell, parent, false);
                NextStepTitleViewHolder nextStepTitleViewHolder = new NextStepTitleViewHolder(nextStepTitleView);
                nextStepTitleViewHolder.title.setText("Waiting for service provider...");
                return nextStepTitleViewHolder;
            case 1:
                View typeTitleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_type_header_view, parent, false);
                SectionTitleViewHolder sectionTitleViewHolder = new SectionTitleViewHolder(typeTitleView);
                sectionTitleViewHolder.title.setText("Invited service provider:");
                return sectionTitleViewHolder;
            case 2:
                View sectionTitleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_user_name_rating_cell, parent, false);
                UserInfoViewHolder userAvatarNameRateViewHolder = new UserInfoViewHolder(sectionTitleView);
                return userAvatarNameRateViewHolder;
            case 3:
                View expandableView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_expandable_header_view, parent, false);
                ExpandableViewHolder expandableViewHolder = new ExpandableViewHolder(expandableView);
                expandableViewHolder.bind(this);
                return expandableViewHolder;
            case 4:
                View imgPageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_image_carousel_cell, parent, false);
                PageViewHolder pageViewHolder = new PageViewHolder(imgPageView);
                return pageViewHolder;
            case 5:
                View priceView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_description_cell, parent, false);
                PriceViewHolder priceViewHolder = new PriceViewHolder(priceView);
                priceViewHolder.descriptionImage.setImageResource(R.mipmap.icon_budget);
                priceViewHolder.description.setText("$50-$100");
                return priceViewHolder;
            case 6:
                View descriptionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_description_cell, parent, false);
                DescriptionViewHolder descriptionViewHolder = new DescriptionViewHolder(descriptionView);
                descriptionViewHolder.descriptionImage.setImageResource(R.mipmap.icon_info);
                descriptionViewHolder.description.setText("We are experts at gardening & landscaping. Please state in your quotation:size of your garden, " +
                        "what tasks you need deon, and any special requirements.");
                return descriptionViewHolder;
            case 7:
                View addressView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_location_button_cell, parent, false);
                AddressViewHolder addressViewHolder = new AddressViewHolder(addressView);
                addressViewHolder.description.setText("2 Jurong West Avenue 5 64386");
                addressViewHolder.location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mContext.getActivity().startActivity(new Intent(mContext.getActivity(), JGGMapViewActivity.class));
                    }
                });
                return addressViewHolder;
            case 8:
                View statusView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_description_cell, parent, false);
                StatusViewHolder statusViewHolder = new StatusViewHolder(statusView);
                statusViewHolder.descriptionImage.setImageResource(R.mipmap.icon_completion);
                statusViewHolder.description.setText("Requests:Before & After photos");
                return statusViewHolder;
            case 9:
                View referenceView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_job_no_cell, parent, false);
                ReferenceNoViewHolder referenceNoViewHolder = new ReferenceNoViewHolder(referenceView);
                return referenceNoViewHolder;
            case 10:
                View originalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_filter_option_cell, parent, false);
                OriginalServiceViewHolder originalViewHolder = new OriginalServiceViewHolder(originalView);
                originalViewHolder.title.setText("View Original Service Post");
                originalViewHolder.btnOriginal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mContext.getActivity().startActivity(new Intent(mContext.getActivity(), AppOriginalPostDetailActivity.class));
                    }
                });
                return originalViewHolder;
            case 11:
                View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_info_footer_cell, parent, false);
                HooterViewHolder footerViewHolder = new HooterViewHolder(footerView);
                footerViewHolder.title.setText("Job posted on 7 Jul, 2017 8:15PM");
                return footerViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (!expandState) return 5;
        return 12;
    }

    @Override
    public int getItemViewType(int position) {

        if (!expandState && position == 4) {
            return  11;
        } else {
            return position;
        }
    }
}

class NextStepTitleViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    RoundedImageView avatar;
    LinearLayout markLine;

    public NextStepTitleViewHolder(View itemView) {
        super(itemView);

        this.title = itemView.findViewById(R.id.lbl_next_step_title);
        this.avatar = itemView.findViewById(R.id.next_step_img_avatar);
        this.markLine = itemView.findViewById(R.id.next_step_mark_line);
    }
}

class SectionTitleViewHolder extends RecyclerView.ViewHolder {

    public void setTitle(TextView title) {
        this.title = title;
    }

    TextView title;

    public SectionTitleViewHolder(View itemView) {
        super(itemView);

        this.title = itemView.findViewById(R.id.lbl_detail_type_header);
    }
}

class UserInfoViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    MaterialRatingBar ratingBar;
    RoundedImageView avatar;

    public UserInfoViewHolder(View itemView) {
        super(itemView);

        this.name = itemView.findViewById(R.id.lbl_username);
        this.ratingBar = itemView.findViewById(R.id.user_ratingbar);
        this.avatar = itemView.findViewById(R.id.img_avatar);
    }
}

class ExpandableViewHolder extends RecyclerView.ViewHolder {

    LinearLayout btnExpand;
    ImageView imgExpand;

    public ExpandableViewHolder(View itemView) {
        super(itemView);

        this.btnExpand = itemView.findViewById(R.id.btn_expand);
        this.imgExpand = itemView.findViewById(R.id.img_expand);
    }

    public void bind(final AppClientServiceDetailAdapter adapter) {

        btnExpand.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                adapter.setExpandState(!adapter.isExpandState());
                if (!adapter.isExpandState()) imgExpand.setImageResource(R.mipmap.button_showless_green);
                else imgExpand.setImageResource(R.mipmap.button_showmore_green);
                adapter.notifyDataSetChanged();
            }
        });
    }
}

class HooterViewHolder extends RecyclerView.ViewHolder {

    TextView title;

    public HooterViewHolder(View itemView) {
        super(itemView);

        this.title = itemView.findViewById(R.id.detail_info_footer_title);
    }
}

class PageViewHolder extends RecyclerView.ViewHolder {

    CarouselView pageView;

    public PageViewHolder(View itemView) {
        super(itemView);

        pageView = itemView.findViewById(R.id.detail_images_carousel_view);
    }
}

class PriceViewHolder extends RecyclerView.ViewHolder {

    ImageView descriptionImage;
    TextView description;

    public PriceViewHolder(View itemView) {
        super(itemView);

        descriptionImage = itemView.findViewById(R.id.img_description);
        description = itemView.findViewById(R.id.lbl_description);
    }
}

class DescriptionViewHolder extends RecyclerView.ViewHolder {

    ImageView descriptionImage;
    TextView description;

    public DescriptionViewHolder(View itemView) {
        super(itemView);

        descriptionImage = itemView.findViewById(R.id.img_description);
        description = itemView.findViewById(R.id.lbl_description);
        description.setTypeface(Typeface.create("muliregular", Typeface.NORMAL));
    }
}

class AddressViewHolder extends RecyclerView.ViewHolder {

    TextView description;
    LinearLayout location;

    public AddressViewHolder(View itemView) {
        super(itemView);

        description = itemView.findViewById(R.id.lbl_location);
        location = itemView.findViewById(R.id.btn_location);
    }
}

class StatusViewHolder extends RecyclerView.ViewHolder {

    ImageView descriptionImage;
    TextView description;

    public StatusViewHolder(View itemView) {
        super(itemView);

        descriptionImage = itemView.findViewById(R.id.img_description);
        description = itemView.findViewById(R.id.lbl_description);
    }
}

class ReferenceNoViewHolder extends RecyclerView.ViewHolder {

    public ReferenceNoViewHolder(View itemView) {
        super(itemView);
    }
}

class OriginalServiceViewHolder extends RecyclerView.ViewHolder {

    RoundedImageView btnOriginal;
    TextView title;

    @SuppressLint("RestrictedApi")
    public OriginalServiceViewHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.lblFilterTitle);
        title.setTextColor(Color.parseColor("#20BF3B"));
        title.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        btnOriginal = itemView.findViewById(R.id.view_filter_bg);
        btnOriginal.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btnOriginal.setBorderColor(Color.parseColor("#20BF3B"));
        btnOriginal.setBorderWidth((float) 4);
        btnOriginal.setCornerRadius((float) 5);
        btnOriginal.setOval(false);
        btnOriginal.mutateBackground(true);
    }
}