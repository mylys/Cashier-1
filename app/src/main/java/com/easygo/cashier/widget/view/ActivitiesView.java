package com.easygo.cashier.widget.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.easygo.cashier.R;
import com.easygo.cashier.adapter.ActivitiesAdapter;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ActivitiesView extends ConstraintLayout {

    private View mView;
    private RecyclerView mRv;
    private ActivitiesAdapter mAdapter;
    private Button mCancelTempPromotion;


    public ActivitiesView(Context context) {
        super(context);
        init(context, null);
    }

    public ActivitiesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(final Context context, AttributeSet attrs) {

        initView(context);

        initAttr(context, attrs);


        initRv();

    }

    private void initRv() {

        mAdapter = new ActivitiesAdapter();
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRv.setAdapter(mAdapter);

    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_activities_view, this, true);
        mRv = mView.findViewById(R.id.rv);
        mCancelTempPromotion = mView.findViewById(R.id.btn_cancel_temp_promotion);
    }

    private void initAttr(Context context, AttributeSet attrs) {
//        if (attrs == null) {
//            return;
//        }
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MySearchView);
//        if (ta != null) {
//
//
//            ta.recycle();
//        }
    }

    public void setData(List<String> data) {
        if (mAdapter != null) {
            mAdapter.setNewData(data);
        }
    }

    public void showCancelTempPromotionButton(boolean isVisiable) {
        mCancelTempPromotion.setVisibility(isVisiable? VISIBLE: GONE);
    }

    public void enableCancelTempPromotionButton(boolean isEnable) {
        mCancelTempPromotion.setEnabled(isEnable);
    }


}
