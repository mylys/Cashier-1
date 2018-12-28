package com.easygo.cashier.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FunctionListAdapter extends RecyclerView.Adapter<FunctionListAdapter.FuntionListHolder> {

    private String[] functions = new String[]{
            "历史订单",
            "退款",
            "交接班",
            "进入系统",
            "语言设置",
            "设备状态",
            "系统设置",
    };

    private int[] res = new int[]{
            R.drawable.ic_order_history,
            R.drawable.ic_refund,
            R.drawable.ic_shift,
            R.drawable.ic_enter_system,
            R.drawable.ic_lauage,
            R.drawable.ic_device_status,
            R.drawable.ic_system_setting,
    };

    @NonNull
    @Override
    public FuntionListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_function, viewGroup, false);
        return new FuntionListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuntionListHolder funtionListHolder, final int i) {
        funtionListHolder.iv.setImageResource(res[i]);
        funtionListHolder.tv_funtion.setText(functions[i]);
        funtionListHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(i);
                }
            }
        });

//        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
//
//        if (receptionHallBeanList.size() == 0) {
//            //隐藏item要把高度宽度设为0；
//            linearLayout.setVisibility(View.GONE);
//            layoutParams.height = 0;
//            ayoutParams.width = 0;
//        } else {
//            //显示item要重新设置高度宽度；
//            linearLayout.setVisibility(View.VISIBLE);
//            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        }
//        linearLayout.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return functions != null ? functions.length : 0;
    }

    static class FuntionListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root)
        ConstraintLayout root;
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv_function)
        TextView tv_funtion;

        public FuntionListHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

//            root = ((ConstraintLayout) itemView.findViewById(R.id.root));
//            iv = ((ImageView) itemView.findViewById(R.id.iv));
//            tv_funtion = ((TextView) itemView.findViewById(R.id.tv_function));
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
