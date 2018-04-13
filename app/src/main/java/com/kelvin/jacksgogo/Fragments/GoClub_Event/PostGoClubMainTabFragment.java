package com.kelvin.jacksgogo.Fragments.GoClub_Event;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kelvin.jacksgogo.CustomView.Views.PostGoClubTabView;
import com.kelvin.jacksgogo.CustomView.Views.PostGoClubTabView.GoClubTabName;
import com.kelvin.jacksgogo.Fragments.Search.PostServiceDescribeFragment;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.Global.PostStatus;

import static com.kelvin.jacksgogo.Utils.Global.GOCLUB;

public class PostGoClubMainTabFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Context mContext;

    private PostGoClubTabView tabView;

    private String tabName;
    private String postStatus;

    public PostGoClubMainTabFragment() {
        // Required empty public constructor
    }

    public static PostGoClubMainTabFragment newInstance(GoClubTabName name, PostStatus status) {
        PostGoClubMainTabFragment fragment = new PostGoClubMainTabFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_go_club_main_tab, container, false);

        initTabView(view);

        return view;
    }

    private void initTabView(View view) {

        tabView = new PostGoClubTabView(mContext);
        LinearLayout tabViewLayout = view.findViewById(R.id.post_go_club_tab_view);
        tabViewLayout.addView(tabView);
        if (tabName.equals("DESCRIBE")) {
            tabView.setTabName(GoClubTabName.DESCRIBE);
        } else if (tabName.equals("LIMIT")) {
            tabView.setTabName(GoClubTabName.LIMIT);
        } else if (tabName.equals("ADMIN")) {
            tabView.setTabName(GoClubTabName.ADMIN);
        }
        tabView.setTabItemClickListener(new PostGoClubTabView.OnTabItemClickListener() {
            @Override
            public void onTabItemClick(View view) {
                onTabViewClick(view);
            }
        });
        refreshFragment();
    }

    private void onTabViewClick(View view) {

        if (view.getId() == R.id.btn_describe) {
            tabView.setTabName(GoClubTabName.DESCRIBE);
        } else if (view.getId() == R.id.btn_time) {
            tabView.setTabName(GoClubTabName.LIMIT);
        } else if (view.getId() == R.id.btn_address) {
            tabView.setTabName(GoClubTabName.ADMIN);
        }
        refreshFragment();
    }

    private void refreshFragment() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (tabView.getTabName() == GoClubTabName.DESCRIBE) {
            PostServiceDescribeFragment frag = PostServiceDescribeFragment.newInstance(GOCLUB);
            frag.setOnItemClickListener(new PostServiceDescribeFragment.OnItemClickListener() {
                @Override
                public void onNextButtonClick() {
                    tabView.setTabName(GoClubTabName.LIMIT);
                    refreshFragment();
                }
            });
            ft.replace(R.id.go_club_main_tab_container, frag, frag.getTag());
        } else if (tabView.getTabName() == GoClubTabName.LIMIT) {

        } else if (tabView.getTabName() == GoClubTabName.ADMIN) {

        }
        ft.commit();
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
