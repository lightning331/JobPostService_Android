package com.kelvin.jacksgogo.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.Appointment.ApptHistoryListCell;
import com.kelvin.jacksgogo.CustomView.RecyclerViewCell.SectionTitleView;
import com.kelvin.jacksgogo.Models.Jobs_Services.JGGAppBaseModel;
import com.kelvin.jacksgogo.Models.Jobs_Services.JGGEventModel;
import com.kelvin.jacksgogo.Models.Jobs_Services.JGGJobModel;
import com.kelvin.jacksgogo.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by PUMA on 10/31/2017.
 * https://rajeshandroiddeveloper.blogspot.jp/2013/05/sectioned-list-view-list-with-headers.html
 */

public class AppointmentMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<JGGAppBaseModel> dataSet;
    public final Map<String, ArrayList<JGGAppBaseModel>> sections = new LinkedHashMap<>();
    public final ArrayAdapter<String> headers;
    public final static int TYPE_SECTION_HEADER = 0;

    Context mContext;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AppointmentMainAdapter(Context context) {
        this.mContext = context;
        headers = new ArrayAdapter<String>(context, R.layout.section_title_view); // this is the header desing page.
    }

    public void addSection(String section, ArrayList<JGGAppBaseModel> arrayList) {
        this.headers.add(section);
        this.sections.put(section, arrayList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SECTION_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_title_view, parent, false);
            return new SectionTitleView(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_history_list_cell, parent, false);
            return new ApptHistoryListCell(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Object itemData = getItem(position);

        if (itemData instanceof String) {
            // RecyclerView Header
            SectionTitleView sectionView = (SectionTitleView) holder;
            sectionView.txtTitle.setTypeface(Typeface.create("mulibold", Typeface.BOLD));
            sectionView.setTitle((String) itemData);
        } else if (itemData instanceof JGGAppBaseModel) {
            // RecyclerView Cell
            ApptHistoryListCell cellView = (ApptHistoryListCell) holder;
            JGGAppBaseModel appointment = (JGGAppBaseModel) itemData;

            cellView.lbl_Title.setText(appointment.getTitle());
            cellView.lbl_Comment.setText(appointment.getComment());
            cellView.lbl_Day.setText(appointment.getAppointmentDay());
            cellView.lbl_Month.setText(appointment.getAppointmentMonth());

            if (appointment.getStatus() == JGGAppBaseModel.AppointmentStatus.CANCELLED) {
                cellView.lbl_Status.setText("Cancelled");
            } else if (appointment.getStatus() == JGGAppBaseModel.AppointmentStatus.WITHDRAWN) {
                cellView.lbl_Status.setText("Withdrawn");
            } else {
                cellView.lbl_Status.setVisibility(View.GONE);
            }
            if (appointment.getBadgeNumber() < 1) {
                // Badge view hide when count is less than 1
                cellView.lbl_BadgeNumber.setVisibility(View.INVISIBLE);
                cellView.mViewStatusBar.setVisibility(View.INVISIBLE);
            } else {
                cellView.lbl_BadgeNumber.setVisibility(View.VISIBLE);
                cellView.mViewStatusBar.setVisibility(View.VISIBLE);
                // Show Badge Count
                Integer badgeCount = appointment.getBadgeNumber();
                cellView.lbl_BadgeNumber.setText(String.valueOf(badgeCount));
            }

            cellView.lbl_Day.setTextColor(ContextCompat.getColor(getContext(), R.color.JGGGreen));
            cellView.lbl_Month.setTextColor(ContextCompat.getColor(getContext(), R.color.JGGGreen));
            if (appointment instanceof JGGJobModel) {
                cellView.lbl_Day.setTextColor(ContextCompat.getColor(getContext(), R.color.JGGCyan));
                cellView.lbl_Month.setTextColor(ContextCompat.getColor(getContext(), R.color.JGGCyan));
            } else if (appointment instanceof JGGEventModel) {
                cellView.lbl_Day.setTextColor(ContextCompat.getColor(getContext(), R.color.JGGPurple));
                cellView.lbl_Month.setTextColor(ContextCompat.getColor(getContext(), R.color.JGGPurple));
            }

            if (cellView != null) {
                cellView.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(position, itemData);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int total = 0;
        for (ArrayList<JGGAppBaseModel> arrayList : this.sections.values())
            total += arrayList.size() + 1;
        return total;
    }

    public Object getItem(int position) {
        for (Object section : this.sections.keySet()) {
            ArrayList<JGGAppBaseModel> arrayList = sections.get(section);
            int size = arrayList.size() + 1;

            // check if position inside this section
            if (position == 0) return section;
            if (position < size) return arrayList.get(position - 1);

            // otherwise jump into next section
            position -= size;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 1;
        for (Object section : this.sections.keySet()) {
            ArrayList<JGGAppBaseModel> arrayList = sections.get(section);
            int size = arrayList.size() + 1;

            // check if position inside this section
            if (position == 0) return TYPE_SECTION_HEADER;
            if (position < size) return 1;

            // otherwise jump into next section
            position -= size;
            type += arrayList.size();
        }
        return -1;
    }

    private Context getContext() {
        return this.mContext;
    }

    // Search Filter
    public void setFilter(ArrayList<JGGAppBaseModel> filteredArray) {
        dataSet = new ArrayList<>();
        dataSet.addAll(filteredArray);
        notifyDataSetChanged();
    }
}