package com.kelvin.jacksgogo.CustomView.RecyclerViewCell.GoClub_Events;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.Adapter.Events.EventsListingAdapter;
import com.kelvin.jacksgogo.R;

public class PastEventRecyclerView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context mContext;

    private RecyclerView recyclerView;
    private TextView btnViewPastEvents;

    public PastEventRecyclerView(View itemView, Context context) {
        super(itemView);
        mContext = context;

        recyclerView = itemView.findViewById(R.id.past_event_recycler_view);
        btnViewPastEvents = itemView.findViewById(R.id.btn_past_event);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        }
        EventsListingAdapter adapter = new EventsListingAdapter(mContext);
        adapter.setOnItemClickListener(new EventsListingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

    }
}
