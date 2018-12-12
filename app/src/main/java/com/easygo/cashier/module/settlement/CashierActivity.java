package com.easygo.cashier.module.settlement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppActivity;
import com.easygo.cashier.widget.Keyboard;
import com.easygo.cashier.widget.MyTitleBar;
import com.easygo.cashier.widget.PayWayView;

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
    Keyboard kb;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.et_money)
    EditText etMoney;

    private SettlementView settlementView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        settlementView = SettlementView.create(this);
        ((FrameLayout) findViewById(R.id.framelayout)).addView(settlementView);

        //关联EditText
        kb.attachEditText(etMoney);

        payWayView.setOnPayWayListener(new PayWayView.OnPayWayListener() {
            @Override
            public void onPayWaySelected(boolean isCombinePay, int pay_way) {
                //支付方式选择回调
                if (settlementView != null) {
                    settlementView.setPayType(isCombinePay, pay_way);

                    //支付方式为有现金时 键盘才可用
                    etMoney.setEnabled(isCombinePay || pay_way == PayWayView.WAY_CASH);
                }
            }
        });

    }

    @OnClick({R.id.cl_back, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_back://返回
                finish();
            case R.id.btn_delete://键盘删除按钮
                int selectionStart = etMoney.getSelectionStart();
                Editable editable = etMoney.getText();

                if (editable != null && editable.length() > 0) {
                    if (selectionStart > 0) {
                        editable.delete(selectionStart - 1, selectionStart);
                    }
                }
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
