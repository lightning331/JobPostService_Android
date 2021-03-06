package com.kelvin.jacksgogo.CustomView.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelvin.jacksgogo.R;

/**
 * Created by PUMA on 11/13/2017.
 */

public class SectionTitleView extends LinearLayout {

    private Context mContext;
    public TextView txtTitle;
    public LinearLayout background;

    public SectionTitleView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SectionTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SectionTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_section_title, this);
        this.txtTitle = findViewById(R.id.lbl_detail_type_header);
        background = findViewById(R.id.section_background);
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setTitle(CharSequence title) {
        txtTitle.setText(title);
    }

    public void setTitle(int resid) {
        txtTitle.setText(resid);
    }

    public void setBackgroundColor(int color) {
        txtTitle.setBackgroundColor(color);
    }
}
