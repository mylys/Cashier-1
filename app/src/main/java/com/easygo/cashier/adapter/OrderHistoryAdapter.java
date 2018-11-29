package com.easygo.cashier.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.bean.OrderHistoryInfo;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private List<OrderHistoryInfo> data;


    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_history_list, viewGroup, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderHistoryViewHolder orderHistoryViewHolder, int i) {
        final OrderHistoryInfo orderHistoryInfo = data.get(i);
        if(orderHistoryInfo.getRefund() != 0) {
            orderHistoryViewHolder.clRefund.setVisibility(View.VISIBLE);
        } else {
            orderHistoryViewHolder.clRefund.setVisibility(View.GONE);
        }
        orderHistoryViewHolder.tvOrderNo.setText(orderHistoryInfo.getOrder_no());
        orderHistoryViewHolder.tvMoney.setText("￥" + orderHistoryInfo.getTotal_money());
        orderHistoryViewHolder.tvTime.setText(orderHistoryInfo.getTime());

        orderHistoryViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(orderHistoryInfo, orderHistoryViewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null? data.size(): 0;
    }

    public void setData(List<OrderHistoryInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root)
        ConstraintLayout root;
        @BindView(R.id.cl_refund)
        ConstraintLayout clRefund;
        @BindView(R.id.tv_order_no)
        TextView tvOrderNo;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_money)
        TextView tvMoney;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener  = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrderHistoryInfo orderHistoryInfo, int position);
    }
}
