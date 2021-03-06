package com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.Global.JGGProposalStatus;
import com.kelvin.jacksgogo.Utils.Models.Proposal.JGGProposalModel;
import com.kelvin.jacksgogo.Utils.Models.User.JGGUserProfileModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by PUMA on 11/13/2017.
 */

public class ServiceProviderCell extends RecyclerView.ViewHolder {

    protected Context mContext;

    public LinearLayout cellBackground;
    public RoundedImageView avatar;
    public TextView lblUserName;
    public TextView lblPrice;
    public TextView lblStatus;
    public MaterialRatingBar ratingBar;
    public ImageView imgProposal;
    public LinearLayout btnProposal;
    public ImageView imgChat;
    public TextView lblMessageCount;

    public ServiceProviderCell(Context context, View itemView) {
        super(itemView);
        mContext = context;

        cellBackground = itemView.findViewById(R.id.background_layout);
        avatar = itemView.findViewById(R.id.img_bidding_provider_avatar);
        lblUserName = itemView.findViewById(R.id.lbl_bidding_provider_username);
        lblPrice = itemView.findViewById(R.id.lbl_bidding_provider_price);
        lblStatus = itemView.findViewById(R.id.lbl_bidding_provider_status);
        ratingBar = itemView.findViewById(R.id.bidding_provider_ratingBar);
        imgProposal = itemView.findViewById(R.id.img_proposal);
        btnProposal = itemView.findViewById(R.id.btn_proposal);
        imgChat = itemView.findViewById(R.id.chat_icon);
        lblMessageCount = itemView.findViewById(R.id.lblBadgeCount);
    }

    public void setData(JGGProposalModel proposal) {

        JGGUserProfileModel provider = proposal.getUserProfile();
        JGGProposalStatus status = proposal.getStatus();

        lblStatus.setVisibility(View.GONE);
        lblPrice.setVisibility(View.GONE);
        imgProposal.setVisibility(View.GONE);

        switch (status) {
            case open:
                if (proposal.isInvited()) {
                    if (proposal.getAcceptedInvite() == null) {

                    } else {
                        lblPrice.setVisibility(View.VISIBLE);
                        imgProposal.setVisibility(View.VISIBLE);
                        lblPrice.setText("$" + String.valueOf(proposal.getBudget()));
                    }
                } else {
                    lblPrice.setVisibility(View.VISIBLE);
                    imgProposal.setVisibility(View.VISIBLE);
                    lblPrice.setText("$" + String.valueOf(proposal.getBudget()));
                }
                break;
            case award:
                lblStatus.setVisibility(View.VISIBLE);
                lblStatus.setText("Awarded");
                imgProposal.setVisibility(View.VISIBLE);
                lblPrice.setVisibility(View.VISIBLE);
                lblPrice.setText("$" + String.valueOf(proposal.getBudget()));
                break;
            case rejected:
                lblStatus.setVisibility(View.VISIBLE);
                lblStatus.setText("Rejected");
                imgProposal.setVisibility(View.VISIBLE);
                lblPrice.setVisibility(View.VISIBLE);
                lblPrice.setText("$" + String.valueOf(proposal.getBudget()));

                itemView.setAlpha(.5f);
                break;
            case confirmed:
                lblStatus.setVisibility(View.VISIBLE);
                lblStatus.setText("Confirmed");
                imgProposal.setVisibility(View.VISIBLE);
                lblPrice.setVisibility(View.VISIBLE);
                lblPrice.setText("$" + String.valueOf(proposal.getBudget()));
                break;
            case withdrawn:
                break;
        }

        // Need to fix in API field
        if (proposal.getMessageCount() == 0) {
            imgChat.setImageResource(R.mipmap.chat_green);
            lblMessageCount.setVisibility(View.GONE);
        } else if (proposal.getMessageCount() > 0) {
            imgChat.setImageResource(R.mipmap.chat_filled_green);
            lblMessageCount.setText(String.valueOf(proposal.getMessageCount()));
        }
        // User avatar
        Picasso.with(mContext)
                .load(provider.getUser().getPhotoURL())
                .placeholder(R.mipmap.icon_profile)
                .into(avatar);
        // User name
        if (provider.getUser().getSurname() == null)
            lblUserName.setText(provider.getUser().getUserName());
        else
            lblUserName.setText(provider.getUser().getFullName());
        // User rating
        if (provider.getUser().getRate() == null || provider.getUser().getRate() == 0)
            ratingBar.setRating(0.0f);
        else
            ratingBar.setRating(provider.getUser().getRate().floatValue());
    }
}
