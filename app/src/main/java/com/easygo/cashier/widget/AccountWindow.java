package com.easygo.cashier.widget;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.AccountAdapter;

import java.util.List;

/**
 * 登录过的账号显示弹窗
 */
public class AccountWindow extends PopupWindow {

    private RecyclerView mRecycleView;
    private AccountAdapter accountAdapter;

    public AccountWindow(Context context) {
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_search_result, null, false);
        setContentView(view);


        mRecycleView = view.findViewById(R.id.rv_search_result);
        mRecycleView.setVerticalScrollBarEnabled(true);

        accountAdapter = new AccountAdapter();

        mRecycleView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                int count = state.getItemCount();

                if (count > 0) {
                    if(count > 3){
                        count = 3;
                    }
                    int realHeight = 0;
                    int realWidth = 0;
                    for(int i = 0;i < count; i++){
                        View view = recycler.getViewForPosition(0);
                        if (view != null) {
                            measureChild(view, widthSpec, heightSpec);
                            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                            int measuredHeight = view.getMeasuredHeight();
                            realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
                            realHeight += measuredHeight;
                        }
                        setMeasuredDimension(realWidth, realHeight);
                    }
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });


        mRecycleView.setAdapter(accountAdapter);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(context.getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        mRecycleView.addItemDecoration(verticalDecoration);

        accountAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mListener != null) {
                    mListener.onItemClicked((String) adapter.getData().get(position));
                }
            }
        });

    }

    public void setData(List<String> data) {
        accountAdapter.setNewData(data);
    }

    public OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClicked(String account);
    }


}
