package com.easygo.cashier.widget.dialog;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.easygo.cashier.Configs;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.FunctionListAdapter;
import com.easygo.cashier.bean.FunctionListBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FunctionListDialog extends MyBaseDialog {

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
            R.string.text_function_entry,
            R.string.text_function_device,
            R.string.text_function_lock,
            R.string.text_function_handover,
            R.string.text_switch_mode_offline,
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
            R.drawable.ic_entry_orders,
            R.drawable.ic_device_status,
            R.drawable.ic_lock,
            R.drawable.ic_shift,
            R.drawable.ic_switch_mode,
    };
    private ArrayList<Integer> hideList;
    private FunctionListAdapter functionListAdapter;

    public void init() {

        functionListAdapter = new FunctionListAdapter();
        hideList = new ArrayList<>();

        if (Configs.getRole(Configs.menus[10]) == 0) {//设备状态
            hideList.add(2);
        }
        if (Configs.getRole(Configs.menus[7]) == 0) {//交接班
            hideList.add(4);
        }
        if (Configs.getRole(Configs.menus[4]) == 0) {//历史订单
            hideList.add(0);
        }

        if (Configs.lock_auth == 0) {//收银机
            hideList.add(3);
        }

        if(!Configs.open_offline) {
            hideList.add(5);
        }

//        if(Config.isOnlineMode()) {
        if(Configs.isOnlineMode()) {
            functions[5] = R.string.text_switch_mode_offline;
        } else {
            functions[5] = R.string.text_switch_mode_online;
        }

        //分割线
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_horizontal));
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        rvFunction.addItemDecoration(horizontalDecoration);
        rvFunction.addItemDecoration(verticalDecoration);

        rvFunction.setLayoutManager(new GridLayoutManager(getContext(), 4));
        functionListAdapter.bindToRecyclerView(rvFunction);
        for (int i = 0; i < functions.length; i++) {
            if (!hideList.contains(i)) {
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
                            case R.string.text_switch_mode_offline:
                                switchOffline();
                                break;
                            case R.string.text_switch_mode_online:
                                switchOnline();
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

    private void switchOnline() {
        if(mListener != null) {
            mListener.switchMode(Configs.mode_online);
        }
    }
    private void switchOffline() {
        if(mListener != null) {
            mListener.switchMode(Configs.mode_offline);
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

        void handover();

        void enterSystem();

        void entryOrders();

        void languageSetting();

        void deviceStatus();

        void systemSetting();

        void onItemClickBefore();

        void onItemClickAfter();

        void lockCashier();

        void switchMode(int mode);
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
    protected int getLayoutWidth() {
        return getResources().getDimensionPixelSize(R.dimen.function_list_width);
    }

    @Override
    protected int getLayoutHeight() {
        boolean singleLine = functionListAdapter.getItemCount() <= 4;
        return getResources().getDimensionPixelSize(singleLine ? R.dimen.y359 : R.dimen.function_list_height);
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_FUNCTION_LIST");
    }

    @OnClick(R.id.cl_cancel)
    public void onViewClicked() {
        dismiss();
    }
}
