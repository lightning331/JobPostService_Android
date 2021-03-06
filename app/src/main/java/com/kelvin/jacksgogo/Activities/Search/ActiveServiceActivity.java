package com.kelvin.jacksgogo.Activities.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.kelvin.jacksgogo.Activities.BottomNavigation.BottomNavigationViewBehavior;
import com.kelvin.jacksgogo.Activities.BottomNavigation.BottomNavigationViewHelper;
import com.kelvin.jacksgogo.CustomView.Views.JGGActionbarView;
import com.kelvin.jacksgogo.Fragments.Search.ActiveServiceMainFragment;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.Global.AppointmentType;
import com.kelvin.jacksgogo.Utils.JGGAppManager;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGCategoryModel;

import static com.kelvin.jacksgogo.Utils.Global.APPOINTMENT_TYPE;
import static com.kelvin.jacksgogo.Utils.Global.EDIT;
import static com.kelvin.jacksgogo.Utils.Global.EDIT_STATUS;
import static com.kelvin.jacksgogo.Utils.Global.EVENTS;
import static com.kelvin.jacksgogo.Utils.Global.JOBS;
import static com.kelvin.jacksgogo.Utils.Global.POST;
import static com.kelvin.jacksgogo.Utils.Global.SERVICES;

public class ActiveServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private JGGActionbarView actionbarView;
    private ActiveServiceMainFragment activeFrag;
    private BottomNavigationView mbtmView;
    private TextView btnPost;

    public String appType;
    public String editStatus;
    public int status;      // 0: Category, 1: Active, 2: Joined

    private JGGCategoryModel selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_active_service);

        selectedCategory = JGGAppManager.getInstance().getSelectedCategory();

        initializeView();
    }

    private void initializeView() {

        appType = getIntent().getStringExtra(APPOINTMENT_TYPE);
        editStatus = getIntent().getStringExtra(EDIT_STATUS);
        status = getIntent().getIntExtra("active_status", 0);

        // Hide Bottom NavigationView and ToolBar
        mbtmView = (BottomNavigationView) findViewById(R.id.active_service_navigation);
        btnPost = (TextView) findViewById(R.id.btn_post);
        BottomNavigationViewHelper.disableShiftMode(mbtmView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mbtmView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        mbtmView.setOnClickListener(this);

        // Top NavigationBar View
        actionbarView = new JGGActionbarView(this);
        mToolbar = (Toolbar) findViewById(R.id.active_service_actionbar);
        mToolbar.addView(actionbarView);
        setSupportActionBar(mToolbar);

        if (appType.equals(SERVICES)) {
            if (status == 1)
                actionbarView.setStatus(JGGActionbarView.EditStatus.ACTIVE_AROUND, AppointmentType.SERVICES);
            else if (status == 0)
                actionbarView.setCategoryNameToActionBar(selectedCategory.getName(), AppointmentType.SERVICES);
            btnPost.setText(R.string.title_post_service);
            btnPost.setBackgroundColor(ContextCompat.getColor(this, R.color.JGGGreen));
        } else if (appType.equals(JOBS)) {
            if (status == 1)
                actionbarView.setStatus(JGGActionbarView.EditStatus.ACTIVE_AROUND, AppointmentType.JOBS);
            else if (status == 0)
                actionbarView.setCategoryNameToActionBar(selectedCategory.getName(), AppointmentType.JOBS);
            btnPost.setText(R.string.title_post_job);
            btnPost.setBackgroundColor(ContextCompat.getColor(this, R.color.JGGCyan));
        } else if (appType.equals(EVENTS)) {
            if (status == 0)
                actionbarView.setCategoryNameToActionBar(selectedCategory.getName(), AppointmentType.EVENTS);
            else if (status == 1)
                actionbarView.setPurpleBackButton(R.string.title_active_event_around, R.string.title_empty);
            else if (status == 2)
                actionbarView.setPurpleBackButton(R.string.title_joined_event, R.string.title_empty);

            btnPost.setText(R.string.title_post_event);
            btnPost.setBackgroundColor(ContextCompat.getColor(this, R.color.JGGPurple));
        }
        if (editStatus.equals(EDIT))
            setBottomViewHidden(true);
        else
            setBottomViewHidden(false);
        actionbarView.setActionbarItemClickListener(new JGGActionbarView.OnActionbarItemClickListener() {
            @Override
            public void onActionbarItemClick(View view) {
                actionbarViewItemClick(view);
            }
        });

        // Main Fragment
        activeFrag = ActiveServiceMainFragment.newInstance(appType);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.active_service_container, activeFrag, activeFrag.getTag());
        ft.commit();
    }

    public void setBottomViewHidden(boolean isHidden) {
        if (isHidden) {
            this.mbtmView.setVisibility(View.GONE);
        } else {
            this.mbtmView.setVisibility(View.VISIBLE);
        }
    }

    private void actionbarViewItemClick(View view) {
        if (view.getId() == R.id.btn_back) {
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                manager.popBackStack();
                setBottomViewHidden(false);
            }
        }
    }

    public int getStatus() {
        return status;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.active_service_navigation) {
            if (appType.equals(SERVICES) || appType.equals(JOBS)) {
                Intent intent = new Intent(this, PostServiceActivity.class);
                intent.putExtra(EDIT_STATUS, POST);
                intent.putExtra(APPOINTMENT_TYPE, appType);
                startActivity(intent);
            } else if (appType.equals(EVENTS)) {

            }
        }
    }
}
