package com.kelvin.jacksgogo.Fragments.Search;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.Activities.Jobs.JobStatusSummaryActivity;
import com.kelvin.jacksgogo.Activities.Search.RequestQuotationActivity;
import com.kelvin.jacksgogo.Adapter.Jobs.EditJobAddressAdapter;
import com.kelvin.jacksgogo.Adapter.Jobs.EditJobReportAdapter;
import com.kelvin.jacksgogo.Adapter.Jobs.EditJobTimeAdapter;
import com.kelvin.jacksgogo.CustomView.Views.JGGActionbarView;
import com.kelvin.jacksgogo.CustomView.Views.PostServiceTabbarView;
import com.kelvin.jacksgogo.CustomView.Views.RecyclerItemClickListener;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGServiceModel;
import com.kelvin.jacksgogo.R;

import java.util.Date;

public class EditServiceFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    private FrameLayout describeContainer;
    private RecyclerView recyclerView;
    private PostServiceTabbarView tabbarView;
    private JGGServiceModel serviceModel = new JGGServiceModel();
    private AlertDialog alertDialog;

    private String status;
    private boolean isRequest; // Request Or Edit

    public EditServiceFragment() {
        // Required empty public constructor
    }

    public static EditServiceFragment newInstance(PostServiceTabbarView.TabName status, boolean b) {
        EditServiceFragment fragment = new EditServiceFragment();
        Bundle args = new Bundle();
        args.putString("tabName", status.toString());
        args.putBoolean("isRequest", b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_service, container, false);

        LinearLayout tabbarLayout = (LinearLayout)view.findViewById(R.id.edit_job_tabbar_view);
        tabbarView = new PostServiceTabbarView(getContext());
        tabbarLayout.addView(tabbarView);

        describeContainer = (FrameLayout) view.findViewById(R.id.edit_job_describe_container);
        recyclerView = (RecyclerView) view.findViewById(R.id.descibe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        initTabbarView();

        tabbarView.setTabItemClickLietener(new PostServiceTabbarView.OnTabItemClickListener() {
            @Override
            public void onTabItemClick(View view) {
                onTabbarViewClick(view);
            }
        });

        return view;
    }

    private void initTabbarView() {

        if (status == "DESCRIBE") {
            tabbarView.setTabName(PostServiceTabbarView.TabName.DESCRIBE, isRequest);
        } else if (status == "TIME") {
            tabbarView.setTabName(PostServiceTabbarView.TabName.TIME, isRequest);
        } else if (status == "ADDRESS") {
            tabbarView.setTabName(PostServiceTabbarView.TabName.ADDRESS, isRequest);
        } else if (status == "REPORT") {
            tabbarView.setTabName(PostServiceTabbarView.TabName.REPORT, isRequest);
        }
        refreshRecyclerView();
    }

    private void onTabbarViewClick(View view) {

        if (view.getId() == R.id.btn_describe) {
            tabbarView.setTabName(PostServiceTabbarView.TabName.DESCRIBE, isRequest);
        } else if (view.getId() == R.id.btn_time) {
            tabbarView.setTabName(PostServiceTabbarView.TabName.TIME, isRequest);
        } else if (view.getId() == R.id.btn_address) {
            tabbarView.setTabName(PostServiceTabbarView.TabName.ADDRESS, isRequest);
        } else if (view.getId() == R.id.btn_report) {
            tabbarView.setTabName(PostServiceTabbarView.TabName.REPORT, isRequest);
        }
        refreshRecyclerView();
    }

    private void refreshRecyclerView() {

        describeContainer.setVisibility(View.GONE);

        if (tabbarView.getTabName() == PostServiceTabbarView.TabName.DESCRIBE) {
            describeContainer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            EditServiceDescribeFragment frag = EditServiceDescribeFragment.newInstance(isRequest);
            frag.setOnItemClickListener(new EditServiceDescribeFragment.OnItemClickListener() {
                @Override
                public void onNextButtonClick(PostServiceTabbarView.TabName status, String jobTitle, String jobDesc) {
                    tabbarView.setTabName(status, isRequest);
                    serviceModel.setTitle(jobTitle);
                    serviceModel.setComment(jobDesc);
                    refreshRecyclerView();
                }
            });
            ft.replace(R.id.edit_job_describe_container, frag, frag.getTag());
            ft.commit();
        } else if (tabbarView.getTabName() == PostServiceTabbarView.TabName.TIME) {
            recyclerView.setVisibility(View.VISIBLE);
            EditJobTimeAdapter mTimeAdapter = new EditJobTimeAdapter(mContext, isRequest, serviceModel);
            mTimeAdapter.setOnItemClickListener(new EditJobTimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PostServiceTabbarView.TabName status, Date date) {
                    tabbarView.setTabName(status, isRequest);
                    serviceModel.setDate(date);
                    refreshRecyclerView();
                }
            });
            recyclerView.setAdapter(mTimeAdapter);
        } else if (tabbarView.getTabName() == PostServiceTabbarView.TabName.ADDRESS) {
            recyclerView.setVisibility(View.VISIBLE);
            EditJobAddressAdapter addressAdapter = new EditJobAddressAdapter(mContext, isRequest, serviceModel);
            addressAdapter.setOnItemClickListener(new EditJobAddressAdapter.OnItemClickListener() {
                @Override
                public void onNextButtonClick(PostServiceTabbarView.TabName status, String unit, String street, String postcode) {
                    tabbarView.setTabName(status, isRequest);
                    serviceModel.setUnit(unit);
                    serviceModel.setStreet(street);
                    serviceModel.setPostcode(postcode);
                    refreshRecyclerView();
                }
            });
            recyclerView.setAdapter(addressAdapter);
        } else if (tabbarView.getTabName() == PostServiceTabbarView.TabName.REPORT) {
            recyclerView.setVisibility(View.VISIBLE);
            EditJobReportAdapter reportAdapter = new EditJobReportAdapter(mContext, isRequest, "SERVICE");
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //serviceModel.setReportType(position);
                    if (serviceModel.getTitle() == null
                            || serviceModel.getComment() == null
                            || serviceModel.getDate() == null
                            || serviceModel.getUnit() == null
                            || serviceModel.getStreet() == null
                            || serviceModel.getPostcode() == null
                            || serviceModel.getReportType().length == 0) {
                        showAlertDialog();
                        return;
                    }

                    EditServiceSummaryFragment editServiceSummaryFragment = EditServiceSummaryFragment.newInstance(true);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.request_quotation_container, editServiceSummaryFragment, editServiceSummaryFragment.getTag());
                    ft.commit();
                }
                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));
            recyclerView.setAdapter(reportAdapter);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.refreshDrawableState();
    }

    private void showAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View alertView = inflater.inflate(R.layout.jgg_alert_view, null);
        builder.setView(alertView);
        alertDialog = builder.create();
        TextView cancelButton = (TextView) alertView.findViewById(R.id.btn_alert_cancel);
        TextView okButton = (TextView) alertView.findViewById(R.id.btn_alert_ok);
        TextView title = (TextView) alertView.findViewById(R.id.lbl_alert_titile);
        TextView desc = (TextView) alertView.findViewById(R.id.lbl_alert_description);

        title.setText(R.string.jgg_alert_title);
        desc.setText(R.string.jgg_error_desc);
        okButton.setText(R.string.alert_ok);
        cancelButton.setVisibility(View.GONE);
        okButton.setOnClickListener(this);
        okButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.JGGGreen));
        alertDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        if (getArguments() != null) {
            status = getArguments().getString("tabName");
            isRequest = getArguments().getBoolean("isRequest");
            if (isRequest)((RequestQuotationActivity)context).setBottomViewHidden(true);
            else ((JobStatusSummaryActivity) context).setStatus(JGGActionbarView.EditStatus.EDIT_DETAIL);
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_alert_ok) {
            alertDialog.dismiss();
        }
    }

    private OnFragmentInteractionListener mListener;

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}