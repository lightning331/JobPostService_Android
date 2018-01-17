package com.kelvin.jacksgogo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGCategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PUMA on 1/17/2018.
 */

public class CategoryCellAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<JGGCategoryModel> mCategories;
    private String mType;

    public CategoryCellAdapter(Context context, ArrayList<JGGCategoryModel> data, String type) {
        mContext = context;
        mCategories = data;
        mType = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_search_category, parent, false);
        CategoryViewHolder categoryView = new CategoryViewHolder(mContext, view);
        return categoryView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryViewHolder categoryView = (CategoryViewHolder) holder;
        if (mCategories != null) {
            String categoryName = mCategories.get(position).getName();
            String url = mCategories.get(position).getImage();
            categoryView.setData(url, categoryName);

            categoryView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mCategories != null) return mCategories.size();
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
        public ImageView imgCategory;
        public TextView lblCategory;

        public CategoryViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;

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
    }
}
