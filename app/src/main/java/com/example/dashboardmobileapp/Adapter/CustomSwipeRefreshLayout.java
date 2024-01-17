package com.example.dashboardmobileapp.Adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

    private NestedScrollView nestedScrollView;

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNestedScrollView(NestedScrollView nestedScrollView) {
        this.nestedScrollView = nestedScrollView;
    }

    @Override
    public boolean canChildScrollUp() {
        return nestedScrollView != null && nestedScrollView.canScrollVertically(-1);
    }
}