package com.kelvin.jacksgogo.Fragments.Jobs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.Adapter.Jobs.AppointmentReportAdapter;
import com.kelvin.jacksgogo.CustomView.Views.PostJobTabView;
import com.kelvin.jacksgogo.CustomView.Views.PostJobTabView.PostJobTabName;
import com.kelvin.jacksgogo.CustomView.Views.RecyclerItemClickListener;
import com.kelvin.jacksgogo.Fragments.Search.PostServiceAddressFragment;
import com.kelvin.jacksgogo.Fragments.Search.PostServiceDescribeFragment;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.JGGAppManager;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGAppointmentModel;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGReportModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.kelvin.jacksgogo.Utils.Global.DUPLICATE;
import static com.kelvin.jacksgogo.Utils.Global.EDIT;
import static com.kelvin.jacksgogo.Utils.Global.JOBS;
import static com.kelvin.jacksgogo.Utils.Global.POST;
import static com.kelvin.jacksgogo.Utils.Global.PostStatus;
import static com.kelvin.jacksgogo.Utils.Global.getReportTypeID;

public class PostJobMainTabFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Context mContext;

    private PostJobTabView tabbarView;
    private LinearLayout tabbarLayout;
    private ImageView imgCategory;
    private TextView lblCategory;
    private RecyclerView recyclerView;

    private FrameLayout container;
    private String tabName;
    private String postStatus;

    private JGGAppointmentModel creatingJob;
    private AppointmentReportAdapter reportAdapter;
    private List<Integer> selectedIds = new ArrayList<>();
    private boolean isMultiSelect = false;
    private int reportType = 0;
    private int tabIndex = 0;

    public PostJobMainTabFragment() {
        // Required empty public constructor
    }

    public static PostJobMainTabFragment newInstance(PostJobTabName name, PostStatus status) {
        PostJobMainTabFragment fragment = new PostJobMainTabFragment();
        Bundle args = new Bundle();
        args.putString("tabName", name.toString());
        args.putString("postStatus", status.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabName = getArguments().getString("tabName");
            postStatus = getArguments().getString("postStatus");
        }
        creatingJob = JGGAppManager.getInstance().getSelectedAppointment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_job_main_tab, container, false);

        this.container = (FrameLayout) view.findViewById(R.id.post_job_detail_container);
        imgCategory = (ImageView) view.findViewById(R.id.img_post_job_tab_category);
        lblCategory = (TextView) view.findViewById(R.id.lbl_post_job_tab_category_name);
        Picasso.with(mContext)
                .load(creatingJob.getCategory().getImage())
                .placeholder(null)
                .into(imgCategory);
        lblCategory.setText(creatingJob.getCategory().getName());

        recyclerView = (RecyclerView)view.findViewById(R.id.post_job_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        tabbarLayout = (LinearLayout)view.findViewById(R.id.post_job_tabbar_view);
        tabbarView = new PostJobTabView(mContext);
        tabbarLayout.addView(tabbarView);
        tabbarView.setTabItemClickListener(new PostJobTabView.OnTabItemClickListener() {
            @Override
            public void onTabItemClick(View view) {
                onTabbarViewClick(view);
            }
        });

        initTabbarView(view);

        return view;
    }

    public int getTabIndex() {
        return this.tabIndex;
    }

    public void setTabIndex(int index) {
        this.tabIndex = index;
        switch (index) {
            case 0:
                tabbarView.setTabName(PostJobTabName.DESCRIBE, true);
                break;
            case 1:
                tabbarView.setTabName(PostJobTabName.TIME, true);
                break;
            case 2:
                tabbarView.setTabName(PostJobTabName.ADDRESS, true);
                break;
            case 3:
                tabbarView.setTabName(PostJobTabName.BUDGET, true);
                break;
            case 4:
                tabbarView.setTabName(PostJobTabName.REPORT, true);
                break;
        }

        refreshFragment();
    }

    private void initTabbarView(View view) {
        if (tabName == "DESCRIBE") {
            setTabIndex(0);
        } else if (tabName == "TIME") {
            setTabIndex(1);
        } else if (tabName == "ADDRESS") {
            setTabIndex(2);
        } else if (tabName == "BUDGET") {
            setTabIndex(3);
        } else if (tabName == "REPORT") {
            setTabIndex(4);
        }
    }

    private void onTabbarViewClick(View view) {
        if (view.getId() == R.id.btn_describe) {
            setTabIndex(0);
        } else if (view.getId() == R.id.btn_time) {
            setTabIndex(1);
        } else if (view.getId() == R.id.btn_address) {
            setTabIndex(2);
        } else if (view.getId() == R.id.btn_budget) {
            setTabIndex(3);
        } else if (view.getId() == R.id.btn_report) {
            setTabIndex(4);
        }
    }

    private void refreshFragment() {

        container.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (tabbarView.getPostJobTabName() == PostJobTabName.DESCRIBE) {
            PostServiceDescribeFragment frag = PostServiceDescribeFragment.newInstance(JOBS);
            frag.setOnItemClickListener(new PostServiceDescribeFragment.OnItemClickListener() {
                @Override
                public void onNextButtonClick() {
                    setTabIndex(1);
                }
            });
            ft.replace(R.id.post_job_detail_container, frag, frag.getTag());
        } else if (tabbarView.getPostJobTabName() == PostJobTabName.TIME) {
            PostJobTimeFragment frag = new PostJobTimeFragment();
            frag.setOnItemClickListener(new PostJobTimeFragment.OnItemClickListener() {
                @Override
                public void onNextButtonClick() {
                    setTabIndex(2);
                }
            });
            ft.replace(R.id.post_job_detail_container, frag, frag.getTag());
        } else if (tabbarView.getPostJobTabName() == PostJobTabName.ADDRESS) {
            PostServiceAddressFragment frag = PostServiceAddressFragment.newInstance(JOBS);
            frag.setOnItemClickListener(new PostServiceAddressFragment.OnItemClickListener() {
                @Override
                public void onNextButtonClick() {
                    setTabIndex(3);
                }
            });
            ft.replace(R.id.post_job_detail_container, frag, frag.getTag());
        } else if (tabbarView.getPostJobTabName() == PostJobTabName.BUDGET) {
            PostJobPriceFragment frag = new PostJobPriceFragment();
            frag.setOnItemClickListener(new PostJobPriceFragment.OnItemClickListener() {
                @Override
                public void onNextButtonClick() {
                    setTabIndex(4);
                }
            });
            ft.replace(R.id.post_job_detail_container, frag, frag.getTag());
        } else if (tabbarView.getPostJobTabName() == PostJobTabName.REPORT) {
            container.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            creatingJob = JGGAppManager.getInstance().getSelectedAppointment();

            selectedIds = getReportTypeID(creatingJob.getReportType());

            reportAdapter = new AppointmentReportAdapter(mContext, true, JOBS);
            reportAdapter.setSelectedIds(selectedIds);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int itemCount = reportAdapter.getItemCount();
                    if (position == itemCount - 1) {
                        onSaveCreatingJob();
                        PostJobSummaryFragment fragment = new PostJobSummaryFragment();
                        if (postStatus.equals(POST))
                            fragment.setEditStatus(PostStatus.POST);
                        else if (postStatus.equals(EDIT) || postStatus.equals(DUPLICATE))
                            fragment.setEditStatus(PostStatus.EDIT);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.post_service_container, fragment, fragment.getTag())
                                .addToBackStack("post_job_summary")
                                .commit();
                    } else {
                        if (!isMultiSelect) {
                            selectedIds = new ArrayList<>();
                            isMultiSelect = true;
                        }
                        multiSelect(position);
                    }
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));
            recyclerView.setAdapter(reportAdapter);
        }
        ft.commit();
    }

    private void multiSelect(int position) {

        JGGReportModel report = reportAdapter.getItem(position - 1);

        if (report != null) {
            Integer id = report.getId();
            if (selectedIds.contains(id))
                selectedIds.remove(Integer.valueOf(id));
            else
                selectedIds.add(id);
            reportAdapter.setSelectedIds(selectedIds);
        }
    }

    private void onSaveCreatingJob() {
        for (Integer index : selectedIds) {
            if (index == 1) {
                reportType += 1;
            } else if (index == 2) {
                reportType += 2;
            } else if (index == 3) {
                reportType += 4;
            }
        }
        creatingJob.setReportType(reportType);
        JGGAppManager.getInstance().setSelectedAppointment(creatingJob);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
