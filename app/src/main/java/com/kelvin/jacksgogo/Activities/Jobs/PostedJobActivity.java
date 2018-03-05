package com.kelvin.jacksgogo.Activities.Jobs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.kelvin.jacksgogo.Activities.MainActivity;
import com.kelvin.jacksgogo.Activities.Search.PostServiceActivity;
import com.kelvin.jacksgogo.CustomView.Views.JGGActionbarView;
import com.kelvin.jacksgogo.CustomView.Views.JGGShareIntentDialog;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.Global;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGAppBaseModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.kelvin.jacksgogo.Utils.API.JGGAppManager.creatingAppointment;
import static com.kelvin.jacksgogo.Utils.API.JGGAppManager.selectedCategory;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.kelvin.jacksgogo.Utils.Global.APPOINTMENT_TYPE;
import static com.kelvin.jacksgogo.Utils.Global.JOBS;

public class PostedJobActivity extends AppCompatActivity {

    @BindView(R.id.posted_job_actionbar) Toolbar mToolbar;
    @BindView(R.id.posted_service_tag_list) TagContainerLayout tagList;
    @BindView(R.id.posted_job_viewd_layout) LinearLayout viewedLayout;
    @BindView(R.id.img_posted_job_category) ImageView imgCategory;
    @BindView(R.id.lbl_posted_job_category_name) TextView lblCategory;
    @BindView(R.id.lbl_posted_job_title) TextView lblTitle;
    @BindView(R.id.lbl_posted_job_time_type) TextView lblType;
    @BindView(R.id.lbl_posted_job_time) TextView lblTime;
    @BindView(R.id.lbl_posted_job_description) TextView lblDescription;
    @BindView(R.id.lbl_posted_job_address) TextView lblAddress;
    @BindView(R.id.lbl_posted_job_budget) TextView lblBudget;
    @BindView(R.id.lbl_posted_job_report_type) TextView lblReportType;
    @BindView(R.id.img_posted_job_user_avatar) RoundedImageView imgAvatar;
    @BindView(R.id.lbl_posted_job_username) TextView lblUserName;
    @BindView(R.id.posted_job_user_rating) MaterialRatingBar ratingBar;

    private JGGActionbarView actionbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_job);

        ButterKnife.bind(this);

        // Top Navigationbar View
        actionbarView = new JGGActionbarView(this);
        mToolbar.addView(actionbarView);
        setSupportActionBar(mToolbar);

        actionbarView.setStatus(JGGActionbarView.EditStatus.POSTED, JGGAppBaseModel.AppointmentType.JOBS);
        actionbarView.setActionbarItemClickListener(new JGGActionbarView.OnActionbarItemClickListener() {
            @Override
            public void onActionbarItemClick(View view) {
                actionbarViewItemClick(view);
            }
        });

        if (selectedCategory != null && creatingAppointment != null)
            setData();
    }

    private void setData() {
        // Category
        Picasso.with(this)
                .load(selectedCategory.getImage())
                .placeholder(null)
                .into(imgCategory);
        lblCategory.setText(selectedCategory.getName());
        lblTitle.setText(creatingAppointment.getTitle());
        // Time
        String time = "";
        String type = "";
        if (creatingAppointment.getJobType() == Global.JGGJobType.oneTime) {
            if (creatingAppointment.getJobTime().isSpecific()) {
                type = "on";
                if (creatingAppointment.getJobTime().getEndOn() != null)
                    time = getDateString(creatingAppointment.getJobTime().getStartOn())
                            + " " + Global.getTimePeriodString(creatingAppointment.getJobTime().getStartOn())
                            + " - "
                            + Global.getTimePeriodString(creatingAppointment.getJobTime().getEndOn());
                else
                    time = getDateString(creatingAppointment.getJobTime().getStartOn())
                            + " " + Global.getTimePeriodString(creatingAppointment.getJobTime().getStartOn());
            } else {
                type = "any time until";
                if (creatingAppointment.getJobTime().getEndOn() != null)
                    time = getDateString(creatingAppointment.getJobTime().getStartOn())
                            + " " + Global.getTimePeriodString(creatingAppointment.getJobTime().getStartOn())
                            + " - "
                            + Global.getTimePeriodString(creatingAppointment.getJobTime().getEndOn());
                else
                    time = getDateString(creatingAppointment.getJobTime().getStartOn())
                            + " " + Global.getTimePeriodString(creatingAppointment.getJobTime().getStartOn());
            }
            lblType.setText(type);
            lblTime.setText(time);
        } else if (creatingAppointment.getJobType() == Global.JGGJobType.repeating) {
            String dayString = creatingAppointment.getRepetition();
            String[] items = dayString.split(",");
            if (creatingAppointment.getRepetitionType() == Global.JGGRepetitionType.weekly) {
                for (int i = 0; i < items.length; i ++) {
                    if (time.equals(""))
                        time = Global.getWeekName(Integer.parseInt(items[i]));
                    else
                        time = time + ", " + "Every " + Global.getWeekName(Integer.parseInt(items[i]));
                }
            } else if (creatingAppointment.getRepetitionType() == Global.JGGRepetitionType.monthly) {
                for (int i = 0; i < items.length; i ++) {
                    if (time.equals(""))
                        time = "Every " + Global.getDayName(Integer.parseInt(items[i])) + " of the month";
                    else
                        time = time + ", " + "Every " + Global.getDayName(Integer.parseInt(items[i])) + " of the month";
                }
            }
            lblType.setText(time);
            lblTime.setVisibility(View.GONE);
        }
        // Description
        lblDescription.setText(creatingAppointment.getDescription());
        // Address
        lblAddress.setText(creatingAppointment.getAddress().getFullAddress());
        // Price
        if (creatingAppointment.getSelectedServiceType() == 1) lblBudget.setText("No limit");
        else if (creatingAppointment.getSelectedServiceType() == 2) lblBudget.setText("$ " + creatingAppointment.getBudget().toString());
        else if (creatingAppointment.getSelectedServiceType() == 3)
            lblBudget.setText("$ " + creatingAppointment.getBudgetFrom().toString()
                    + " "
                    + "$ " + creatingAppointment.getBudgetTo().toString());
        // Report
        lblReportType.setText(Global.reportTypeName(creatingAppointment.getReportType()));
        // User
        Picasso.with(this)
                .load(creatingAppointment.getUserProfile().getUser().getPhotoURL())
                .placeholder(null)
                .into(imgAvatar);
        lblUserName.setText(creatingAppointment.getUserProfile().getUser().getFullName());
        ratingBar.setRating(creatingAppointment.getUserProfile().getUser().getRate().floatValue());
        // Tag View
        String [] strings = creatingAppointment.getTags().split(",");
        tagList.setTags(Arrays.asList(strings));
    }

    private void actionbarViewItemClick(View view) {
        if (view.getId() == R.id.btn_back) {
            onBackClick();
        } else if (view.getId() == R.id.btn_more) {
            actionbarView.setMoreButtonClicked(true);
            onMorePopUpMenu(view);
        }
    }

    private void onBackClick() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("is_post", true);
        startActivity(intent);
    }

    private void openShareDialog() {
        JGGShareIntentDialog dialogShare = new JGGShareIntentDialog.Builder(this)
                .setDialogTitle("Share with your friends")
                .setShareLink(null)
                .build();
        dialogShare.show();
    }

    private void onMorePopUpMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.posted_job_menu);
        popupMenu.setOnDismissListener(new OnDismissListener());
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener());

        // Force icons to show in Custom Overflow Menu
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popupMenu);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            popupMenu.show();
            return;
        }
        popupMenu.show();
    }

    private class OnDismissListener implements PopupMenu.OnDismissListener {
        @Override
        public void onDismiss(PopupMenu menu) {
            actionbarView.setMoreButtonClicked(false);
        }
    }

    private class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if (menuItem.getItemId() == R.id.posted_job_menu_share) {  // Share the Service
                openShareDialog();
            } else if (menuItem.getItemId() == R.id.posted_job_menu_edit) {    // Edit Service
                Intent intent = new Intent(PostedJobActivity.this, PostServiceActivity.class);
                intent.putExtra("EDIT_STATUS", "Edit");
                intent.putExtra(APPOINTMENT_TYPE, JOBS);
                startActivity(intent);
            } else if (menuItem.getItemId() == R.id.posted_job_menu_duplicate) {    // Duplicate Service
                Intent intent = new Intent(PostedJobActivity.this, PostServiceActivity.class);
                intent.putExtra("EDIT_STATUS", "Duplicate");
                intent.putExtra(APPOINTMENT_TYPE, JOBS);
                startActivity(intent);
            } else if (menuItem.getItemId() == R.id.posted_job_menu_delete) {    // Delete Service
                onDeleteButtonClick();
            }
            return true;
        }
    }

    private void onDeleteButtonClick() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (this).getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.jgg_alert_view, null);
        builder.setView(dialogView);
        TextView cancelButton = (TextView) dialogView.findViewById(R.id.btn_alert_cancel);
        cancelButton.setBackgroundColor(ContextCompat.getColor(this, R.color.JGGCyan10Percent));
        cancelButton.setTextColor(ContextCompat.getColor(this, R.color.JGGCyan));
        TextView deleteButton = (TextView) dialogView.findViewById(R.id.btn_alert_ok);
        TextView title = (TextView) dialogView.findViewById(R.id.lbl_alert_titile);
        TextView desc = (TextView) dialogView.findViewById(R.id.lbl_alert_description);

        title.setText(R.string.alert_posted_job_delete_title);
        desc.setText(R.string.alert_posted_job_delete_desc);

        final AlertDialog alertDialog = builder.create();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                onBackClick();
            }
        });
        alertDialog.show();
    }

    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        String dateString = dateFormat.format(date);
        return dateString;
    }
}
