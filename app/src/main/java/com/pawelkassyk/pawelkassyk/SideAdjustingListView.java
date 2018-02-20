package com.pawelkassyk.pawelkassyk;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SideAdjustingListView extends ListView {
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public SideAdjustingListView(Context context) {
        super(context);
    }

    public SideAdjustingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideAdjustingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
