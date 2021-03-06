package com.kelvin.jacksgogo.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.Global.AppointmentType;
import com.kelvin.jacksgogo.Utils.Models.FilterCategoryModel;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGCategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PUMA on 1/17/2018.
 */

public class CategoryMultiSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<JGGCategoryModel> mCategories;
    private ArrayList<FilterCategoryModel> mFilterCategories;
    private AppointmentType mType;

    public CategoryMultiSelectionAdapter(Context context, ArrayList<JGGCategoryModel> data, AppointmentType type, ArrayList<FilterCategoryModel> filterCategories) {
        mContext = context;
        mCategories = data;
        mType = type;
        mFilterCategories = filterCategories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_category, parent, false);
        CategoryViewHolder categoryView = new CategoryViewHolder(mContext, view);
        return categoryView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CategoryViewHolder categoryView = (CategoryViewHolder) holder;
        if (mType == AppointmentType.SERVICES
                || mType == AppointmentType.GOCLUB
                || mType == AppointmentType.EVENTS) {
            final FilterCategoryModel filterCategoryModel = mFilterCategories.get(position);
            if (mCategories != null) {
                String categoryName = mCategories.get(position).getName();
                String url = mCategories.get(position).getImage();
                categoryView.setData(url, categoryName);

                categoryView.setSelected(filterCategoryModel.isSelected(), mType);
            }
            categoryView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterCategoryModel.setSelected(!filterCategoryModel.isSelected());
                    listener.onItemClick(position);
                    notifyItemChanged(position);
                }
            });
        } else if (mType == AppointmentType.JOBS) {
            if (position == 0) {
                categoryView.lblCategory.setText("Quick Jobs");
                categoryView.imgCategory.setImageResource(R.mipmap.icon_cat_quickjob);
            } else {
                if (mCategories != null) {
                    String categoryName = mCategories.get(position - 1).getName();
                    String url = mCategories.get(position - 1).getImage();
                    categoryView.setData(url, categoryName);
                }
            }
            categoryView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position - 1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mType == AppointmentType.SERVICES
                || mType == AppointmentType.GOCLUB
                || mType == AppointmentType.EVENTS) {
            if (mCategories != null)
                return mCategories.size();
        }
        else if (mType == AppointmentType.JOBS) {
            if (mCategories != null)
                return mCategories.size() + 1;
        }
        return 0;
    }

    public void refreshData(ArrayList<JGGCategoryModel> categories) {
        mCategories = categories;
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;
        public LinearLayout backLayout;
        public ImageView imgCategory;
        public TextView lblCategory;

        public CategoryViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;

            backLayout = (LinearLayout) itemView.findViewById(R.id.cell_background);
            imgCategory = (ImageView) itemView.findViewById(R.id.img_search_category);
            lblCategory = (TextView) itemView.findViewById(R.id.lbl_search_category);
        }

        public void setData(String imgUrl, String name) {

            lblCategory.setText(name);
            Picasso.with(mContext)
                    .load(imgUrl)
                    .placeholder(null)
                    .into(imgCategory);
        }

        public void setSelected(boolean isSelected, AppointmentType type) {
            if (isSelected) {
                if (type == AppointmentType.EVENTS) {
                    backLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.JGGGreen10Percent));
                } else if (type == AppointmentType.SERVICES) {
                    backLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.JGGGreen10Percent));
                } else if (type == AppointmentType.GOCLUB) {
                    backLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.JGGPurple10Percent));
                } else if (type == AppointmentType.JOBS) {
                    backLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.JGGCyan10Percent));
                }
            } else {
                backLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.JGGWhite));
            }
        }
    }
}
