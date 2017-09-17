package com.teamgrid.fashhub.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.teamgrid.fashhub.R;

public class NewSimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public NewSimpleDividerItemDecoration(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.new_line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + 10;
        int right = parent.getWidth() - parent.getPaddingRight() - 10 ;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}