package com.kelvin.jacksgogo.Adapter.GoClub_Event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Jobs.UserNameRatingCell;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.Global;
import com.kelvin.jacksgogo.Utils.Models.User.JGGGoClubUserModel;

import java.util.ArrayList;


public class UserListingOfOwnerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<JGGGoClubUserModel> pendingUsers = new ArrayList<>();
    private ArrayList<JGGGoClubUserModel> approvedUsers = new ArrayList<>();

    private OnPendingItemClickListener pendingListener;
    private OnApproveItemClickListener approveListener;

    public UserListingOfOwnerAdapter(Context context, ArrayList<JGGGoClubUserModel> users) {
        this.mContext = context;

        sortClubUserByStatus(users);
    }

    private void sortClubUserByStatus(ArrayList<JGGGoClubUserModel> users) {
        if (users.size() > 0) {
            ArrayList<JGGGoClubUserModel> tmpUsers = new ArrayList<>();
            for (JGGGoClubUserModel user : users) {
                switch (user.getUserStatus()) {
                    case requested:
                        pendingUsers.add(user);
                        break;
                    case approved:
                        tmpUsers.add(user);
                        break;
                }
            }
            sortClubUserByUserType(tmpUsers);
        } else {
            sortClubUserByUserType(users);
        }
    }

    // Todo - Sort Admin and General Users
    private void sortClubUserByUserType(ArrayList<JGGGoClubUserModel> users) {
        approvedUsers.clear();
        ArrayList<JGGGoClubUserModel> adminUsers = new ArrayList<>();
        ArrayList<JGGGoClubUserModel> generalUsers = new ArrayList<>();
        for (JGGGoClubUserModel user : users) {
            if (user.getUserType() == Global.EventUserType.admin)
                adminUsers.add(user);
            else
                generalUsers.add(user);
        }
        approvedUsers.addAll(adminUsers);
        approvedUsers.addAll(generalUsers);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            if (pendingUsers.size() > 0) {
                View pendingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pending_approval_member, parent, false);
                PendingApprovalViewHolder pendingViewHolder = new PendingApprovalViewHolder(pendingView);
                return pendingViewHolder;
            } else {
                View clubUserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job_detail_user_name_rating, parent, false);
                UserNameRatingCell clubUserViewHolder = new UserNameRatingCell(mContext, clubUserView);
                return clubUserViewHolder;
            }
        } else {
            View clubUserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job_detail_user_name_rating, parent, false);
            UserNameRatingCell clubUserViewHolder = new UserNameRatingCell(mContext, clubUserView);
            return clubUserViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserNameRatingCell) {
            final UserNameRatingCell viewHolder = (UserNameRatingCell) holder;
            final JGGGoClubUserModel user;
            final int index;
            if (pendingUsers.size() > 0)
                index = position - 1;
            else
                index = position;
            user = approvedUsers.get(index);
            viewHolder.setApprovedUser(user);

            viewHolder.lblDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approveListener.onItemClick(v, user);
                }
            });
            viewHolder.lblReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approveListener.onItemClick(v, user);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (pendingUsers.size() > 0)
            return approvedUsers.size() + 1;
        else
            return approvedUsers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnPendingItemClickListener {
        void onItemClick(View view, String userID);
    }

    public void setPendingItemClickListener(OnPendingItemClickListener listener) {
        this.pendingListener = listener;
    }

    public interface OnApproveItemClickListener {
        void onItemClick(View view, JGGGoClubUserModel user);
    }

    public void setApproveItemClickListener(OnApproveItemClickListener listener) {
        this.approveListener = listener;
    }

    // Todo - Pending Approval RecyclerViewHolder
    public class PendingApprovalViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;

        public PendingApprovalViewHolder(View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.pending_members_recycler_view);

            if (recyclerView != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
            }
            PendingUserListAdapter adapter = new PendingUserListAdapter();
            recyclerView.setAdapter(adapter);
        }
    }

    // Todo - Pending Approval RecyclerView Adapter
    public class PendingUserListAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View clubUserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_job_detail_user_name_rating, parent, false);
            UserNameRatingCell clubUserViewHolder = new UserNameRatingCell(mContext, clubUserView);
            return clubUserViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            UserNameRatingCell viewHolder = (UserNameRatingCell) holder;
            final JGGGoClubUserModel user = pendingUsers.get(position);
            viewHolder.setPendingUser(user);
            viewHolder.lblReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pendingListener.onItemClick(v, user.getUserProfileID());
                }
            });
            viewHolder.lblDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pendingListener.onItemClick(v, user.getUserProfileID());
                }
            });
        }

        @Override
        public int getItemCount() {
            return pendingUsers.size();
        }
    }
}
