package com.kelvin.jacksgogo.CustomView.RecyclerViewCell.GoClub_Events;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.Activities.GoClub_Event.CreateGoClubActivity;
import com.kelvin.jacksgogo.Activities.GoClub_Event.EventDetailActivity;
import com.kelvin.jacksgogo.Activities.GoClub_Event.PastEventsActivity;
import com.kelvin.jacksgogo.Adapter.GoClub_Event.EventsListingAdapter;
import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Appointment.AppInviteProviderCell;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.Global.EventUserStatus;
import com.kelvin.jacksgogo.Utils.Global.EventUserType;
import com.kelvin.jacksgogo.Utils.JGGAppManager;
import com.kelvin.jacksgogo.Utils.Models.GoClub_Event.JGGGoClubModel;
import com.kelvin.jacksgogo.Utils.Models.User.JGGGoClubUserModel;

import java.util.ArrayList;

import static com.kelvin.jacksgogo.Utils.Global.APPOINTMENT_TYPE;
import static com.kelvin.jacksgogo.Utils.Global.EDIT_STATUS;
import static com.kelvin.jacksgogo.Utils.Global.EVENTS;
import static com.kelvin.jacksgogo.Utils.Global.POST;

public class GoClubDetailEventView extends RecyclerView.ViewHolder {

    private Context mContext;

    private RecyclerView eventRecyclerView;
    private RecyclerView memberRecyclerView;
    public TextView btnViewPastEvents;
    public TextView btnCreateEvent;

    private JGGGoClubModel mClub;
    private ArrayList<JGGGoClubUserModel> adminUsers = new ArrayList<>();
    private ArrayList<JGGGoClubUserModel> owners = new ArrayList<>();
    private JGGGoClubUserModel owner = new JGGGoClubUserModel();

    public GoClubDetailEventView(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mClub = JGGAppManager.getInstance().getSelectedClub();
        // Todo - Create Owner for add to the ClubUser list
        owner.setClubID(mClub.getID());
        owner.setUserProfileID(mClub.getUserProfileID());
        owner.setUserType(EventUserType.owner);
        owner.setUserStatus(EventUserStatus.approved);
        owner.setAddedOn(mClub.getCreatedOn());
        owner.setUserProfile(mClub.getUserProfile());
        owners.add(owner);
        // Todo - Make ClubUser list
        adminUsers.addAll(owners);
        adminUsers.addAll(mClub.getClubUsers());

        // Todo - Events
        initEventView();
        // Todo - Members
        initMembersView();
    }

    private void initEventView() {
        eventRecyclerView = itemView.findViewById(R.id.go_club_detail_event_recycler_view);
        memberRecyclerView = itemView.findViewById(R.id.go_club_detail_member_recycler_view);
        btnViewPastEvents = itemView.findViewById(R.id.btn_past_event);
        btnCreateEvent = itemView.findViewById(R.id.btn_create_event);

        if (eventRecyclerView != null) {
            eventRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        }
        EventsListingAdapter adapter = new EventsListingAdapter(mContext, mClub.getEvents());
        adapter.setOnItemClickListener(new EventsListingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                mContext.startActivity(new Intent(mContext, EventDetailActivity.class));
            }
        });
        eventRecyclerView.setAdapter(adapter);
    }

    private void initMembersView() {
        // Todo - Members RecyclerView
        if (memberRecyclerView != null)
            memberRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        GoClubDetailMemberAdapter memberAdapter = new GoClubDetailMemberAdapter();
        memberRecyclerView.setAdapter(memberAdapter);

        // Todo - View Posted Events
        btnViewPastEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, PastEventsActivity.class));
            }
        });

        // Todo - Create New Event
        for (JGGGoClubUserModel clubUser : adminUsers) {
            if (clubUser.getUserProfileID().equals(JGGAppManager.getInstance().getCurrentUser().getID())) {
                switch (clubUser.getUserType()) {
                    case owner:
                    case admin:
                        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent mIntent = new Intent(mContext, CreateGoClubActivity.class);
                                mIntent.putExtra(EDIT_STATUS, POST);
                                mIntent.putExtra(APPOINTMENT_TYPE, EVENTS);
                                mContext.startActivity(mIntent);
                            }
                        });
                        break;
                    case user:
                    case none:
                        btnCreateEvent.setVisibility(View.GONE);
                        break;
                }
            } else
                btnCreateEvent.setVisibility(View.GONE);
        }
    }

    public class GoClubDetailMemberAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View posterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_app_invite_provider, parent, false);
            AppInviteProviderCell posterViewHolder = new AppInviteProviderCell(mContext, posterView);
            return posterViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            AppInviteProviderCell viewHolder = (AppInviteProviderCell) holder;
            JGGGoClubUserModel userProfileModel = adminUsers.get(position);
            viewHolder.setClubUser(userProfileModel);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return adminUsers.size();
        }
    }
}
