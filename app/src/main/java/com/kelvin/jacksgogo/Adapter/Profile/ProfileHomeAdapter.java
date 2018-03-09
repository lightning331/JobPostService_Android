package com.kelvin.jacksgogo.Adapter.Profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Profile.ProfileHomeCell;
import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Profile.ProfileHomeHeaderCell;
import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Profile.ProfileHomeSignOutCell;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.API.JGGAppManager;
import com.kelvin.jacksgogo.Utils.Models.User.JGGUserProfileModel;

/**
 * Created by PUMA on 1/27/2018.
 */

public class ProfileHomeAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private JGGUserProfileModel user;

    public static int HEADER_TYPE = 0;
    public static int JOINED_GOCLUB_TYPE = 1;
    public static int SETTINGS_TYPE = 2;
    public static int ABOUT_TYPE = 3;
    public static int SIGNOUT_TYPE = 4;
    private int ITEM_COUNT = 5;

    public ProfileHomeAdapter(Context context) {
        mContext = context;
        user = JGGAppManager.getInstance(mContext).currentUser;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_profile_home_header, parent, false);
            ProfileHomeHeaderCell header = new ProfileHomeHeaderCell(view);
            return header;
        } else if (viewType == JOINED_GOCLUB_TYPE
                || viewType == SETTINGS_TYPE
                || viewType == ABOUT_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_profile_home, parent, false);
            ProfileHomeCell cell = new ProfileHomeCell(view);
            return cell;
        } else if (viewType == SIGNOUT_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_profile_home_signout, parent, false);
            ProfileHomeSignOutCell cell = new ProfileHomeSignOutCell(view);
            return cell;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ProfileHomeHeaderCell) {
            ProfileHomeHeaderCell header = (ProfileHomeHeaderCell) holder;
            header.lblUserName.setText(user.getUser().getFullName());
        } else if (holder instanceof ProfileHomeCell) {
            ProfileHomeCell cell = (ProfileHomeCell) holder;
            if (position == JOINED_GOCLUB_TYPE) {
                cell.title1.setText("Joined GoClubs");
                cell.title2.setText("Service Listing");
            } else if (position == SETTINGS_TYPE) {
                cell.title1.setText("Payment Method");
                cell.title2.setText("Settings");
            } else if (position == ABOUT_TYPE) {
                cell.title1.setText("Talk To Us");
                cell.title2.setText("About JacksGoGo");
            }
        } else if (holder instanceof ProfileHomeSignOutCell) {
            ProfileHomeSignOutCell cell = (ProfileHomeSignOutCell) holder;
            cell.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}