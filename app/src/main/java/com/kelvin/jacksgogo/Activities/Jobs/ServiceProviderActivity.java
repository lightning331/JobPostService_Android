package com.kelvin.jacksgogo.Activities.Jobs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kelvin.jacksgogo.Adapter.Jobs.ServiceProviderAdapter;
import com.kelvin.jacksgogo.CustomView.Views.JGGActionbarView;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.API.JGGAPIManager;
import com.kelvin.jacksgogo.Utils.API.JGGURLManager;
import com.kelvin.jacksgogo.Utils.Global;
import com.kelvin.jacksgogo.Utils.Global.AppointmentType;
import com.kelvin.jacksgogo.Utils.JGGAppManager;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGAppointmentModel;
import com.kelvin.jacksgogo.Utils.Models.Proposal.JGGProposalModel;
import com.kelvin.jacksgogo.Utils.Responses.JGGProposalResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kelvin.jacksgogo.Utils.Global.JGGProposalStatus.award;
import static com.kelvin.jacksgogo.Utils.Global.JGGProposalStatus.confirmed;
import static com.kelvin.jacksgogo.Utils.Global.JGGProposalStatus.rejected;
import static com.kelvin.jacksgogo.Utils.Global.JGGProposalStatus.withdrawn;
import static com.kelvin.jacksgogo.Utils.JGGTimeManager.getAppointmentTime;

public class ServiceProviderActivity extends AppCompatActivity {

    @BindView(R.id.app_invite_actionbar) Toolbar mToolbar;
    @BindView(R.id.img_detail) ImageView imgCategory;
    @BindView(R.id.lbl_title) TextView lblCategory;
    @BindView(R.id.lbl_date) TextView lblTime;
    @BindView(R.id.quotation_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.btn_invite_service_provider) TextView btnInvite;
    @BindView(R.id.swipeSearchContainer) SwipeRefreshLayout swipeContainer;

    private JGGActionbarView actionbarView;

    private ArrayList<JGGProposalModel> proposals = new ArrayList<>();
    private JGGAppointmentModel mJob;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);
        ButterKnife.bind(this);

        mJob = JGGAppManager.getInstance().getSelectedAppointment();
        if (mJob != null)
            setCategory();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProposalsByJob();
    }

    private void initView() {
        // Top Navigationbar View
        actionbarView = new JGGActionbarView(this);
        mToolbar.addView(actionbarView);
        setSupportActionBar(mToolbar);

        actionbarView.setStatus(JGGActionbarView.EditStatus.SERVICE_PROVIDER, AppointmentType.UNKNOWN);
        actionbarView.setActionbarItemClickListener(new JGGActionbarView.OnActionbarItemClickListener() {
            @Override
            public void onActionbarItemClick(View view) {
                onBackPressed();
            }
        });

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceProviderActivity.this, InviteProviderActivity.class);
                startActivity(intent);
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setColorSchemeResources(R.color.JGGGreen,
                R.color.JGGGreen,
                R.color.JGGGreen,
                R.color.JGGGreen);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            getProposalsByJob();
                                        }
                                    }
                );
            }
        });
    }

    private void getProposalsByJob() {
        proposals.clear();
        swipeContainer.setRefreshing(true);
        JGGAPIManager apiManager = JGGURLManager.createService(JGGAPIManager.class, this);
        Call<JGGProposalResponse> call = apiManager.getProposalsByJob(mJob.getID(), 0, 50);
        call.enqueue(new Callback<JGGProposalResponse>() {
            @Override
            public void onResponse(Call<JGGProposalResponse> call, Response<JGGProposalResponse> response) {
                swipeContainer.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {

                        ArrayList<JGGProposalModel> openProposal = new ArrayList<>();
                        ArrayList<JGGProposalModel> invitedProposal = new ArrayList<>();
                        ArrayList<JGGProposalModel> confirmedProposal = new ArrayList<>();
                        ArrayList<JGGProposalModel> awaredProposal = new ArrayList<>();
                        ArrayList<JGGProposalModel> withdrawnProposal = new ArrayList<>();
                        ArrayList<JGGProposalModel> rejectedProposal = new ArrayList<>();

                        for (JGGProposalModel p: response.body().getValue()) {
                            if (p.getStatus() == Global.JGGProposalStatus.open) {
                                if (p.isInvited()) {
                                    if (p.getAcceptedInvite() == null)
                                        invitedProposal.add(p);
                                    else
                                        openProposal.add(p);
                                } else
                                    openProposal.add(p);
                            }
                            else if (p.getStatus() == award)
                                awaredProposal.add(p);
                            else if (p.getStatus() == confirmed)
                                confirmedProposal.add(p);
                            else if (p.getStatus() == withdrawn)
                                withdrawnProposal.add(p);
                            else if (p.getStatus() == rejected)
                                rejectedProposal.add(p);
                        }
                        proposals.addAll(confirmedProposal);
                        proposals.addAll(awaredProposal);
                        proposals.addAll(openProposal);
                        proposals.addAll(invitedProposal);
                        proposals.addAll(withdrawnProposal);
                        proposals.addAll(rejectedProposal);

                        updateRecyclerView();
                    } else {
                        Toast.makeText(ServiceProviderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ServiceProviderActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JGGProposalResponse> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                Toast.makeText(ServiceProviderActivity.this, "Request time out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        }
        ServiceProviderAdapter adapter = new ServiceProviderAdapter(this, proposals);
        adapter.setOnItemClickListener(new ServiceProviderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onShowBidDetailClick(position);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void onShowBidDetailClick(int position) {
        JGGAppManager.getInstance().setSelectedProposal(proposals.get(position));
        Intent intent = new Intent(this, BidDetailActivity.class);
        startActivity(intent);
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
}
