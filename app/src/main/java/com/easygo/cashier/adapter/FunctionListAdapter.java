package com.easygo.cashier.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.FunctionListBean;

public class FunctionListAdapter extends BaseQuickAdapter<FunctionListBean,BaseViewHolder> {

    public FunctionListAdapter() {
        super(R.layout.item_function);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FunctionListBean item) {
        helper.setImageResource(R.id.iv,item.getRes())
                .setText(R.id.tv_function,item.getFunction_name())
                .getView(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(item.getFunction_name());
                }
            }
        });
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int function);
    }
}
