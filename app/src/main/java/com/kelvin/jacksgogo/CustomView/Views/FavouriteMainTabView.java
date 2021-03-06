package com.kelvin.jacksgogo.CustomView.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.Global.AppointmentType;
import com.kelvin.jacksgogo.Utils.Global.AppointmentType;
import static com.kelvin.jacksgogo.Utils.Global.EVENTS;
import static com.kelvin.jacksgogo.Utils.Global.JOBS;
import static com.kelvin.jacksgogo.Utils.Global.SERVICES;
import static com.kelvin.jacksgogo.Utils.Global.USERS;

/**
 * Created by PUMA on 12/18/2017.
 */

public class FavouriteMainTabView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private View favouriteTabView;

    private LinearLayout servicesButton;
    private LinearLayout jobsButton;
    private LinearLayout eventButton;
    private LinearLayout userButton;
    private TextView servicesTextView;
    private TextView jobsTextView;
    private TextView eventTextView;
    private TextView userTextView;
    private ImageView servicesDotImageView;
    private ImageView jobsDotImageView;
    private ImageView eventDotImageView;
    private ImageView userDotImageView;
    private ImageButton searchButton;

    private AppointmentType type;

    public FavouriteMainTabView(Context context) {
        super(context);
        this.mContext = context;

        initView();
    }

    private void initView(){

        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        favouriteTabView = mLayoutInflater.inflate(R.layout.view_favourite_main_tab, this);

        servicesButton = (LinearLayout) favouriteTabView.findViewById(R.id.services_layout);
        jobsButton = (LinearLayout) favouriteTabView.findViewById(R.id.jobs_layout);
        eventButton = (LinearLayout) favouriteTabView.findViewById(R.id.event_layout);
        userButton = (LinearLayout) favouriteTabView.findViewById(R.id.users_layout);
        servicesTextView = (TextView) favouriteTabView.findViewById(R.id.lbl_service);
        jobsTextView = (TextView) favouriteTabView.findViewById(R.id.lbl_jobs);
        eventTextView = (TextView) favouriteTabView.findViewById(R.id.lbl_event);
        userTextView = (TextView) favouriteTabView.findViewById(R.id.lbl_users);
        servicesDotImageView = (ImageView) favouriteTabView.findViewById(R.id.img_services_cirle);
        jobsDotImageView = (ImageView) favouriteTabView.findViewById(R.id.img_jobs_circle);
        eventDotImageView = (ImageView) favouriteTabView.findViewById(R.id.img_event_circle);
        userDotImageView = (ImageView) favouriteTabView.findViewById(R.id.img_users_circle);
        searchButton = (ImageButton) favouriteTabView.findViewById(R.id.btn_search);

        searchButton.setOnClickListener(this);
        servicesButton.setOnClickListener(this);
        jobsButton.setOnClickListener(this);
        eventButton.setOnClickListener(this);
        userButton.setOnClickListener(this);
        servicesTextView.setTag(SERVICES);
        jobsTextView.setTag(JOBS);
        eventTextView.setTag(EVENTS);
        userTextView.setTag(USERS);

//        type = AppointmentType.SERVICES;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_search) {
//            Intent intent = new Intent(view.getContext(), ServiceSearchActivity.class);
//            if (type == AppointmentType.SERVICES) {
//                intent.putExtra(APPOINTMENT_TYPE, SERVICES);
//            } else if (type == AppointmentType.JOBS) {
//                intent.putExtra(APPOINTMENT_TYPE, GOCLUB);
//            } else if (type == AppointmentType.EVENTS) {
//                intent.putExtra(APPOINTMENT_TYPE, EVENTS);
//            } else if (type == AppointmentType.USERS) {
//                intent.putExtra(APPOINTMENT_TYPE, USERS);
//            }
//            mContext.startActivity(intent);
        } else {
            servicesDotImageView.setVisibility(View.INVISIBLE);
            jobsDotImageView.setVisibility(View.INVISIBLE);
            eventDotImageView.setVisibility(View.INVISIBLE);
            userDotImageView.setVisibility(View.INVISIBLE);

            servicesTextView.setTextColor(getResources().getColor(R.color.JGGGrey1));
            jobsTextView.setTextColor(getResources().getColor(R.color.JGGGrey1));
            eventTextView.setTextColor(getResources().getColor(R.color.JGGGrey1));
            userTextView.setTextColor(getResources().getColor(R.color.JGGGrey1));

            if (view.getId() == R.id.services_layout) {
                servicesTextView.setTextColor(getResources().getColor(R.color.JGGGreen));
                servicesDotImageView.setVisibility(View.VISIBLE);
                listener.onTabbarItemClick(servicesTextView);
                searchButton.setImageResource(R.mipmap.button_search_green);
//                type = AppointmentType.SERVICES;
            } else if (view.getId() == R.id.jobs_layout) {
                jobsTextView.setTextColor(getResources().getColor(R.color.JGGCyan));
                jobsDotImageView.setVisibility(View.VISIBLE);
                listener.onTabbarItemClick(jobsTextView);
                searchButton.setImageResource(R.mipmap.button_search_cyan);
//                type = AppointmentType.JOBS;
            } else if (view.getId() == R.id.event_layout) {
                eventTextView.setTextColor(getResources().getColor(R.color.JGGPurple));
                eventDotImageView.setVisibility(View.VISIBLE);
                listener.onTabbarItemClick(eventTextView);
                searchButton.setImageResource(R.mipmap.button_search_purple);
//                type = AppointmentType.EVENTS;
            } else if (view.getId() == R.id.users_layout) {
                userTextView.setTextColor(getResources().getColor(R.color.JGGOrange));
                userDotImageView.setVisibility(View.VISIBLE);
                listener.onTabbarItemClick(userTextView);
                searchButton.setImageResource(R.mipmap.button_search_orange);
//                type = AppointmentType.USERS;
            }
        }
    }

    private OnTabbarItemClickListener listener;

    public interface OnTabbarItemClickListener {
        void onTabbarItemClick(TextView item);
    }

    public void setTabbarItemClickListener(OnTabbarItemClickListener listener) {
        this.listener = listener;
    }
}
