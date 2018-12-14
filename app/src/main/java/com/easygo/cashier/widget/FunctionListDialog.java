package com.easygo.cashier.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.easygo.cashier.R;
import com.easygo.cashier.adapter.FunctionListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FunctionListDialog extends DialogFragment {

    private Unbinder unbinder;

    @BindView(R.id.rv_function)
    RecyclerView rvFunction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_FRAME, R.style.CustomDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_function_dialog, container, false);

        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if(window != null) {
            window.setLayout(getResources().getDimensionPixelSize(R.dimen.function_list_width),
                            getResources().getDimensionPixelSize(R.dimen.function_list_height));
        }
        getDialog().setCanceledOnTouchOutside(false);
    }

    public void init() {
        GridLayoutManager glm = new GridLayoutManager(getContext(), 4);

        FunctionListAdapter functionListAdapter = new FunctionListAdapter();

        //分割线
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_horizontal));
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        rvFunction.addItemDecoration(horizontalDecoration);
        rvFunction.addItemDecoration(verticalDecoration);

        rvFunction.setLayoutManager(glm);
        rvFunction.setAdapter(functionListAdapter);

        functionListAdapter.setOnItemClickListener(new FunctionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(mListener != null) {
                    mListener.onItemClickBefore();
                }
                switch (position) {
                    case 0://历史订单
                        orderHistory();
                        break;
                    case 1://退款
                        refund();
                        break;
                    case 2://交接班
                        shift();
                        break;
                    case 3://进入系统
                        enterSystem();
                        break;
                    case 4://语言设置
                        languageSetting();
                        break;
                    case 5://设备状态
                        deviceStatus();
                        break;
                    case 6://系统设置
                        systemSetting();
                        break;
                    default:
                        break;
                }
                onItemClickAfter();
            }
        });
    }

    private void onItemClickAfter() {
        dismiss();

        if(mListener != null) {
            mListener.onItemClickAfter();
        }
    }

    public void orderHistory() {
        if (mListener != null) {
            mListener.orderHistory();
        }
    }

    public void refund() {
        if (mListener != null) {
            mListener.refund();
        }
    }

    public void shift() {
        if (mListener != null) {
            mListener.shift();
        }
    }

    public void enterSystem() {
        if (mListener != null) {
            mListener.enterSystem();
        }
    }

    public void languageSetting() {
        if (mListener != null) {
            mListener.languageSetting();
        }
    }

    public void deviceStatus() {
        if (mListener != null) {
            mListener.deviceStatus();
        }
    }

    public void systemSetting() {
        if (mListener != null) {
            mListener.systemSetting();
        }
    }

    private OnFunctionListItemListener mListener;
    public void setOnFunctionListItemListener(OnFunctionListItemListener listItemListener) {
        this.mListener = listItemListener;
    }
    public interface OnFunctionListItemListener {
        void orderHistory();
        void refund();
        void shift();
        void enterSystem();
        void languageSetting();
        void deviceStatus();
        void systemSetting();

        void onItemClickBefore();
        void onItemClickAfter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null)
            unbinder.unbind();
    }

    @OnClick(R.id.iv_cancel)
    public void onViewClicked() {
        dismiss();
    }
}
