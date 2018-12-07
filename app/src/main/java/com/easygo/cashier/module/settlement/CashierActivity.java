package com.easygo.cashier.module.settlement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppActivity;
import com.easygo.cashier.widget.MyTitleBar;
import com.easygo.cashier.widget.PayWayView;
import com.niubility.library.widget.keyboard.NumericKeyboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 结算收银页面
 */
@Route(path = ModulePath.settlement)
public class CashierActivity extends BaseAppActivity {

    @BindView(R.id.cl_title)
    MyTitleBar clTitle;
    @BindView(R.id.pay_way_view)
    PayWayView payWayView;
    @BindView(R.id.kb)
    NumericKeyboard kb;

    private SettlementView settlementView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
//        clTitle.setTitle(getResources().getString(R.string.text_settlement));

        settlementView = SettlementView.create(this);
        ((FrameLayout) findViewById(R.id.framelayout)).addView(settlementView);
        settlementView.post(new Runnable() {
            @Override
            public void run() {
                //关联EditText
                kb.setEditText(settlementView.getEditText());
            }
        });

        payWayView.setOnPayWayListener(new PayWayView.OnPayWayListener() {
            @Override
            public void onPayWaySelected(boolean isCombinePay, int pay_way) {
                //支付方式选择回调
                if (settlementView != null) {
                    settlementView.setPayType(isCombinePay, pay_way);

                    //支付方式为有现金时 键盘才可用
                    settlementView.getEditText().setEnabled(isCombinePay || pay_way == PayWayView.WAY_CASH);
                }
            }
        });

    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settlementView != null) {
            settlementView.release();
        }
    }
}
