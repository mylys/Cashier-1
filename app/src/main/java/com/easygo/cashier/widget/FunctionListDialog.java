package com.easygo.cashier.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easygo.cashier.R;
import com.easygo.cashier.activity.MainActivity;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    public void init() {
        GridLayoutManager glm = new GridLayoutManager(getContext(), 4);

        FunctionListAdapter functionListAdapter = new FunctionListAdapter();

        rvFunction.setLayoutManager(glm);
        rvFunction.setAdapter(functionListAdapter);

        functionListAdapter.setOnItemClickListener(new FunctionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
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
    }

    public void orderHistory() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.toOrderHistoryActivity();
        }
    }

    public void refund() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.toRefundActivity();
        }
    }

    public void shift() {

    }

    public void enterSystem() {

    }

    public void languageSetting() {

    }

    public void deviceStatus() {

    }

    public void systemSetting() {

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
