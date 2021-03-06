package com.kelvin.jacksgogo.Activities.Jobs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kelvin.jacksgogo.Activities.Search.PostServiceActivity;
import com.kelvin.jacksgogo.CustomView.Views.JGGActionbarView;
import com.kelvin.jacksgogo.CustomView.Views.JGGAlertView;
import com.kelvin.jacksgogo.Fragments.Jobs.OutgoingJobFragment;
import com.kelvin.jacksgogo.Fragments.Search.PostQuotationSummaryFragment;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.API.JGGAPIManager;
import com.kelvin.jacksgogo.Utils.API.JGGURLManager;
import com.kelvin.jacksgogo.Utils.Global.AppointmentType;
import com.kelvin.jacksgogo.Utils.JGGAppManager;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGAppointmentActivityModel;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGAppointmentModel;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGContractModel;
import com.kelvin.jacksgogo.Utils.Models.Proposal.JGGProposalModel;
import com.kelvin.jacksgogo.Utils.Models.User.JGGUserProfileModel;
import com.kelvin.jacksgogo.Utils.Responses.JGGAppointmentActivityResponse;
import com.kelvin.jacksgogo.Utils.Responses.JGGBaseResponse;
import com.kelvin.jacksgogo.Utils.Responses.JGGGetContractResponse;
import com.kelvin.jacksgogo.Utils.Responses.JGGProposalResponse;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kelvin.jacksgogo.CustomView.Views.JGGActionbarView.EditStatus.APPOINTMENT;
import static com.kelvin.jacksgogo.CustomView.Views.JGGActionbarView.EditStatus.JOB_DETAILS;
import static com.kelvin.jacksgogo.Utils.Global.APPOINTMENT_TYPE;
import static com.kelvin.jacksgogo.Utils.Global.EDIT;
import static com.kelvin.jacksgogo.Utils.Global.EDIT_STATUS;
import static com.kelvin.jacksgogo.Utils.Global.JGGJobStatus.deleted;
import static com.kelvin.jacksgogo.Utils.Global.JOBS;
import static com.kelvin.jacksgogo.Utils.JGGTimeManager.getAppointmentTime;

public class OutgoingJobActivity extends AppCompatActivity implements TextWatcher {

    @BindView(R.id.app_detail_actionbar) Toolbar mToolbar;
    @BindView(R.id.img_detail) ImageView imgCategory;
    @BindView(R.id.lbl_title) TextView lblCategory;
    @BindView(R.id.lbl_date) TextView lblTime;
    @BindView(R.id.lblStatus) TextView lblCancel;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    public JGGActionbarView actionbarView;

    private JGGAppointmentModel mJob;
    private JGGContractModel mContract;
    private ArrayList<JGGProposalModel> mProposals = new ArrayList<>();
    private ArrayList<JGGAppointmentActivityModel> mActivities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_job);

        ButterKnife.bind(this);

        /* ---------    Custom view add to TopToolbar     --------- */
        actionbarView = new JGGActionbarView(this);
        mToolbar.addView(actionbarView);
        setSupportActionBar(mToolbar);

        mJob = JGGAppManager.getInstance().getSelectedAppointment();
        setCategory();

        actionbarView.setActionbarItemClickListener(new JGGActionbarView.OnActionbarItemClickListener() {
            @Override
            public void onActionbarItemClick(View view) {
                actionbarViewItemClick(view);
            }
        });
    }

    public void setRescheduleActionBar(final boolean isRescheduled) {
        actionbarView.setStatus(JGGActionbarView.EditStatus.APPOINTMENT, AppointmentType.UNKNOWN);
        actionbarView.setActionbarItemClickListener(new JGGActionbarView.OnActionbarItemClickListener() {
            @Override
            public void onActionbarItemClick(View view) {
                rescheduleActionbarViewItemClick(view, isRescheduled);
            }
        });
    }

    private void rescheduleActionbarViewItemClick(View view, boolean isRescheduled) {
        if (view.getId() == R.id.btn_more) {
            /* ---------    More button pressed     --------- */
            switch (actionbarView.getEditStatus()) {
                case APPOINTMENT:
                    onShowReshcedulePopUpMenu(view, isRescheduled);
                    break;
                default:
                    break;
            }
        } else if (view.getId() == R.id.btn_back) {
            onBack();
        }
    }

    private void onShowReshcedulePopUpMenu(View view, boolean isRescheduled) {
        actionbarView.setEditMoreButtonClicked(true);

        PopupMenu popupMenu = new PopupMenu(this, view);
        if (isRescheduled) {
            popupMenu.inflate(R.menu.reschedule_menu);
        } else {
            popupMenu.inflate(R.menu.delete_menu);
        }
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

    @Override
    protected void onResume() {
        super.onResume();

        actionbarView.setStatus(JGGActionbarView.EditStatus.APPOINTMENT, AppointmentType.UNKNOWN);
        getAppointmentActivities();
    }

    private void setCategory() {
        // Category
        Picasso.with(this)
                .load(mJob.getCategory().getImage())
                .placeholder(null)
                .into(imgCategory);
        lblCategory.setText(mJob.getCategory().getName());
        // Time
        lblTime.setText(getAppointmentTime(mJob));
    }

    private void onOutgoingJobFragment() {
        OutgoingJobFragment clientFrag = new OutgoingJobFragment();
        clientFrag.setAppointmentActivities(mActivities, mProposals, mContract);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.app_detail_container, clientFrag)
                .commit();
    }

    public void deleteJob(final String reason) {

        String jobID = mJob.getID();

        progressBar.setVisibility(View.VISIBLE);

        JGGAPIManager apiManager = JGGURLManager.createService(JGGAPIManager.class, this);
        Call<JGGBaseResponse> call = apiManager.deleteJob(jobID, reason);
        call.enqueue(new Callback<JGGBaseResponse>() {
            @Override
            public void onResponse(Call<JGGBaseResponse> call, Response<JGGBaseResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        mJob.setStatus(deleted);
                        mJob.setReason(reason);

                        deleteJobFinished();
                        getAppointmentActivities();
                    } else {
                        Toast.makeText(OutgoingJobActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OutgoingJobActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JGGBaseResponse> call, Throwable t) {
                Toast.makeText(OutgoingJobActivity.this, "Request time out!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void getAppointmentActivities() {
        JGGAPIManager apiManager = JGGURLManager.createService(JGGAPIManager.class, this);
        Call<JGGAppointmentActivityResponse> call = apiManager.getAppointmentActivities(mJob.getID());
        call.enqueue(new Callback<JGGAppointmentActivityResponse>() {
            @Override
            public void onResponse(Call<JGGAppointmentActivityResponse> call, Response<JGGAppointmentActivityResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        mActivities = response.body().getValue();
                        getProposalsByJob();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OutgoingJobActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(OutgoingJobActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JGGAppointmentActivityResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OutgoingJobActivity.this, "Request time out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProposalsByJob() {
        JGGAPIManager apiManager = JGGURLManager.createService(JGGAPIManager.class, this);
        Call<JGGProposalResponse> call = apiManager.getProposalsByJob(mJob.getID(), 0, 50);
        call.enqueue(new Callback<JGGProposalResponse>() {
            @Override
            public void onResponse(Call<JGGProposalResponse> call, Response<JGGProposalResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        mProposals = response.body().getValue();

                        getContractByAppointment();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OutgoingJobActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(OutgoingJobActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JGGProposalResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OutgoingJobActivity.this, "Request time out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Dump API
    private void getContractByAppointment() {
        JGGAPIManager apiManager = JGGURLManager.createService(JGGAPIManager.class, this);
        Call<JGGGetContractResponse> call = apiManager.getContractByAppointment(mJob.getID());
        call.enqueue(new Callback<JGGGetContractResponse>() {
            @Override
            public void onResponse(Call<JGGGetContractResponse> call, Response<JGGGetContractResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        mContract = response.body().getValue();

                        // Todo: send Appointment Status
                        onOutgoingJobFragment();

                    } else {
                        Toast.makeText(OutgoingJobActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OutgoingJobActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JGGGetContractResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OutgoingJobActivity.this, "Request time out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setStatus(JGGActionbarView.EditStatus status) {
        actionbarView.setStatus(status, AppointmentType.UNKNOWN);
    }

    private void onEditJob() {
        Intent intent = new Intent(this, PostServiceActivity.class);
        intent.putExtra(EDIT_STATUS, EDIT);
        intent.putExtra(APPOINTMENT_TYPE, JOBS);
        startActivity(intent);
    }
    // TODO - Reschedule Job
    private void onRescheduleJob() {
        Intent intent = new Intent(this, RescheduleActivity.class);
        intent.putExtra("isIncoming", false);
        startActivity(intent);
    }

    private void actionbarViewItemClick(View view) {
        if (view.getId() == R.id.btn_more) {
            /* ---------    More button pressed     --------- */
            switch (actionbarView.getEditStatus()) {
                case NONE:
                    onShowEditPopUpMenu(view);
                    break;
                case APPOINTMENT:
                    onShowEditPopUpMenu(view);
                    break;
                default:
                    break;
            }
        } else if (view.getId() == R.id.btn_back) {
            onBack();
        }
    }

    private void onBack() {
        if (actionbarView.getEditStatus() == null) {
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() == 0)
                onBackPressed();
            else
                manager.popBackStack();
        } else {
            if (actionbarView.getEditStatus() == JOB_DETAILS) {
                JGGUserProfileModel currentUser = JGGAppManager.getInstance().getCurrentUser();
                if (mJob.getUserProfileID().equals(currentUser.getID()))
                    actionbarView.setStatus(JGGActionbarView.EditStatus.APPOINTMENT, AppointmentType.UNKNOWN);
                else
                    actionbarView.setDeleteJobStatus();
                onBackPressed();
            } else if (actionbarView.getEditStatus() == APPOINTMENT)
                finish();
        }
    }

    private void showDeleteJobDialog() {
        final JGGAlertView builder = new JGGAlertView(this,
                "Delete Job?",
                getResources().getString(R.string.alert_edit_job_delete_desc),
                true,
                getResources().getString(R.string.alert_cancel),
                R.color.JGGGreen,
                R.color.JGGGreen10Percent,
                getResources().getString(R.string.alert_delete),
                R.color.JGGRed);
        final android.app.AlertDialog alertDialog = builder.create();
        builder.txtReason.addTextChangedListener(this);
        builder.setOnItemClickListener(new JGGAlertView.OnItemClickListener() {
            @Override
            public void onDoneButtonClick(View view) {
                if (view.getId() == R.id.btn_alert_cancel)
                    alertDialog.dismiss();
                else {
                    alertDialog.dismiss();
                    deleteJob(builder.txtReason.getText().toString());
                }
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void deleteJobFinished() {
        lblCancel.setVisibility(View.VISIBLE);
        actionbarView.setDeleteJobStatus();
    }

    private void onShowEditPopUpMenu(View view) {
        actionbarView.setEditMoreButtonClicked(true);

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.edit_menu);
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
            if (actionbarView.getEditStatus() == JGGActionbarView.EditStatus.APPOINTMENT
                    || actionbarView.getEditStatus() == JGGActionbarView.EditStatus.NONE)
                actionbarView.setEditMoreButtonClicked(false);
        }
    }

    private class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if (menuItem.getItemId() == R.id.menu_option_delete) {  // Delete Job
                showDeleteJobDialog();
            } else if (menuItem.getItemId() == R.id.menu_option_edit) {    // Edit Job
                onEditJob();
            } else if (menuItem.getItemId() == R.id.menu_option_reschedule) { // Reschedule Job
                onRescheduleJob();
            }
            return true;
        }
    }

    private void backToEditJobMainFragment() {
        actionbarView.setStatus(JGGActionbarView.EditStatus.EDIT_MAIN, AppointmentType.UNKNOWN);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.app_detail_container, PostQuotationSummaryFragment.newInstance(false))
                .commit();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
