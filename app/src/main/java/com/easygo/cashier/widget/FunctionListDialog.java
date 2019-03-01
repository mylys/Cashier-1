package com.easygo.cashier.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.easygo.cashier.Configs;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.FunctionListAdapter;
import com.easygo.cashier.bean.FunctionListBean;
import com.niubility.library.base.BaseDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FunctionListDialog extends BaseDialog {

    private Unbinder unbinder;

    @BindView(R.id.rv_function)
    RecyclerView rvFunction;

//    private int[] functions = new int[]{
//            R.string.text_function_history,
//            R.string.text_function_refund,
//            R.string.text_function_handover,
//            R.string.text_function_entry,
//            R.string.text_function_language,
//            R.string.text_function_device,
//            R.string.text_function_setting,
//            R.string.text_function_system,
//    };

    private int[] functions = new int[]{
            R.string.text_function_history,
            R.string.text_function_handover,
            R.string.text_function_entry,
            R.string.text_function_device,
            R.string.text_function_lock,
    };

//    private int[] res = new int[]{
//            R.drawable.ic_order_history,
//            R.drawable.ic_refund,
//            R.drawable.ic_shift,
//            R.drawable.ic_entry_orders,
//            R.drawable.ic_lauage,
//            R.drawable.ic_device_status,
//            R.drawable.ic_system_setting,
//            R.drawable.ic_enter_system,
//    };

    private int[] res = new int[]{
            R.drawable.ic_order_history,
//            R.drawable.ic_refund,
            R.drawable.ic_shift,
            R.drawable.ic_entry_orders,
//            R.drawable.ic_lauage,
            R.drawable.ic_device_status,
            R.drawable.ic_lock,
//            R.drawable.ic_system_setting,
//            R.drawable.ic_enter_system,
    };


    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(getResources().getDimensionPixelSize(R.dimen.function_list_width),
                    getResources().getDimensionPixelSize(Configs.lock_auth == 0 ? R.dimen.y359 : R.dimen.function_list_height));
//                    getResources().getDimensionPixelSize(R.dimen.y359));
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

            window.setGravity(Gravity.CENTER);

            window.getDecorView().setSystemUiVisibility(uiOptions);
        }
        getDialog().setCanceledOnTouchOutside(false);
    }

    public void init() {

        FunctionListAdapter functionListAdapter = new FunctionListAdapter();
        ArrayList<Integer> integers = new ArrayList<>();

        if (Configs.getRole(Configs.menus[10]) == 0) {
//            integers.add(5);
            integers.add(3);
        }
        if (Configs.getRole(Configs.menus[7]) == 0) {
//            integers.add(2);
            integers.add(1);
        }
        if (Configs.getRole(Configs.menus[4]) == 0) {
            integers.add(0);
        }

        if (Configs.lock_auth == 0) {
            integers.add(4);
        }

        //分割线
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_horizontal));
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        rvFunction.addItemDecoration(horizontalDecoration);
        rvFunction.addItemDecoration(verticalDecoration);

        rvFunction.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvFunction.setAdapter(functionListAdapter);
        for (int i = 0; i < functions.length; i++) {
            if (!integers.contains(i)) {
                functionListAdapter.addData(new FunctionListBean(functions[i], res[i]));
            }
        }

        functionListAdapter.setOnItemClickListener(new FunctionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                if (mListener != null) {
                    mListener.onItemClickBefore();
                }
                rvFunction.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        switch (position) {
                            case R.string.text_function_history:
                                orderHistory();
                                break;
                            case R.string.text_function_refund:
                                refund();
                                break;
                            case R.string.text_function_handover:
                                handover();
                                break;
                            case R.string.text_function_system:
                                enterSystem();
                                break;
                            case R.string.text_function_language:
                                languageSetting();
                                break;
                            case R.string.text_function_device:
                                deviceStatus();
                                break;
                            case R.string.text_function_setting:
                                systemSetting();
                                break;
                            case R.string.text_function_entry:
                                entryOrders();
                                break;
                            case R.string.text_function_lock:
                                lockCashier();
                                break;
                        }

                        dismiss();
                    }
                }, 200);


                onItemClickAfter();
            }
        });
    }

    private void onItemClickAfter() {
//        dismiss();

        if (mListener != null) {
            mListener.onItemClickAfter();
        }
    }

    private void lockCashier() {
        if (mListener != null) {
            mListener.lockCashier();
        }
    }

    public void orderHistory() {
        if (mListener != null) {
            mListener.orderHistory();
        }
    }

    public void entryOrders() {
        if (mListener != null) {
            mListener.entryOrders();
        }
    }

    public void refund() {
        if (mListener != null) {
            mListener.refund();
        }
    }

    public void handover() {
        if (mListener != null) {
            mListener.handover();
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

        void handover();

        void enterSystem();

        void entryOrders();

        void languageSetting();

        void deviceStatus();

        void systemSetting();

        void onItemClickBefore();

        void onItemClickAfter();

        void lockCashier();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    protected void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);

        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_function_dialog;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected boolean shouldHideBackground() {
        return false;
    }

    @Override
    protected boolean canCanceledOnTouchOutside() {
        return false;
    }

    @Override
    protected boolean isWindowWidthMatchParent() {
        return false;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_FUNCTION_LIST");
    }

    @OnClick(R.id.iv_cancel)
    public void onViewClicked() {
        dismiss();
    }
}
