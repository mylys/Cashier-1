package com.easygo.cashier.module.goods;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.Configs;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.Test;
import com.easygo.cashier.adapter.GoodsAdapter;
import com.easygo.cashier.bean.CheckPayStatusResponse;
import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.bean.GoodsNum;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.easygo.cashier.module.refund.RefundActivity;
import com.easygo.cashier.widget.NoGoodsDialog;
import com.niubility.library.base.BaseMvpFragment;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.GsonUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.crypto.KeyAgreement;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsFragment extends BaseMvpFragment<GoodsContract.IView, GoodsPresenter> implements GoodsContract.IView {

    public static final String TAG = "GoodsFragment";

    @BindView(R.id.tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.btn_settlement)
    Button btnSettlement;
    @BindView(R.id.btn_pop_money_box)
    Button clPopMoneyBox;
    @BindView(R.id.btn_no_barcode)
    Button clNoBarcode;
    @BindView(R.id.et_barcode)
    EditText etBarcode;//监听扫码机

    public static final int TYPE_GOODS = 0;
    public static final int TYPE_REFUND = 1;
    public static final String KEY_TYPE = "key_type";
    private int mType = TYPE_GOODS;
    public static final String KEY_ADMIN_NAME = "key_admin_name";
    private String admin_name;
    private GoodsAdapter mGoodsAdapter;

    /**商品数据*/
    private List<GoodsNum<GoodsResponse>> mdata;

    /**商品总数*/
    private int mGoodsCount;
    /**应收*/
    private float mTotalMoney;
    /**优惠*/
    private float mCoupon;


    public static GoodsFragment newInstance() {
        return newInstance(null);
    }

    public static GoodsFragment newInstance(Bundle bundle) {
        GoodsFragment goodsFragment = new GoodsFragment();
        if (bundle != null)
            goodsFragment.setArguments(bundle);
        return goodsFragment;
    }

    @Override
    protected GoodsPresenter createPresenter() {
        return new GoodsPresenter();
    }

    @Override
    protected GoodsContract.IView createView() {
        return this;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void init() {
        initView();

        initBarcode();
    }

    private void initBarcode() {

        etBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    onScanCode(etBarcode.getText().toString().trim());
                    etBarcode.setText("");
                }

                return false;
            }
        });

        etBarcode.setFocusable(true);
        etBarcode.setFocusableInTouchMode(true);
        etBarcode.requestFocus();

    }

    /**
     * 扫描到的条码 回调
     */
    private void onScanCode(String barcode) {
        Log.i(TAG, "onScanCode: barcode --> " + barcode);

        if("2210000000019".equals(barcode)) {
            //测试
            showToast("测试数据");
            mPresenter.getGoods(Configs.shop_sn, "096619438839");
            return;
        }

        if(TextUtils.isEmpty(barcode)) {
            showToast("barcode = null");
        } else {
            //获取商品信息
            mPresenter.getGoods(Configs.shop_sn, barcode);
        }

//        mPresenter.getGoods(Test.shop_sn, "096619438839");

//        mPresenter.getGoods(Test.shop_sn, Test.barcode);
//        mPresenter.getGoods(Test.shop_sn, "8885012290555");
//        mPresenter.getGoods(Test.shop_sn, "3179730010041");
//        mPresenter.getGoods(Test.shop_sn, "4891028170969");
//        mPresenter.getGoods(Test.shop_sn, "4891028705635");
//        mPresenter.getGoods(Test.shop_sn, "8996006856890");

    }


    private void initView() {
        rvGoods.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mGoodsAdapter = new GoodsAdapter();
        rvGoods.setAdapter(mGoodsAdapter);

        mdata = new ArrayList<>();
        mGoodsAdapter.setNewData(mdata);

        mGoodsAdapter.setOnPriceListener(new GoodsAdapter.OnPriceListener() {
            @Override
            public void onPriceChange(float price, int count, float coupon) {
                refreshPrce(price, count, coupon);
            }

        });

        setIntentData();
    }

    private void setIntentData() {
        Bundle data = getArguments();
        if (data != null) {
            mType = data.getInt(KEY_TYPE, TYPE_GOODS);
            admin_name = data.getString(KEY_ADMIN_NAME);

        }
        switch (mType) {
            case TYPE_GOODS:
                clPopMoneyBox.setVisibility(View.VISIBLE);
                clNoBarcode.setVisibility(View.GONE);
                btnSettlement.setText(" 收银：  ￥0.00 ");
                break;
            case TYPE_REFUND:
                clPopMoneyBox.setVisibility(View.GONE);
                clNoBarcode.setVisibility(View.VISIBLE);
                btnSettlement.setText(" 退款：  ￥0.00 ");
                break;
        }

    }

    /**
     * 刷新界面显示
     */
    private void refreshPrce(float price, int count, float coupon) {
        mGoodsCount = count;
        mTotalMoney = price;
        mCoupon = coupon;

        DecimalFormat df = new DecimalFormat("#0.00");

        tvTotalMoney.setText("￥" + df.format(price));
        tvCoupon.setText("￥" + df.format(coupon));
        if(mType == TYPE_GOODS) {
            btnSettlement.setText(" 收银：  ￥" + df.format(price) + " ");
        } else {
            btnSettlement.setText(" 退款：  ￥" + df.format(price) + " ");
        }
        tvGoodsCount.setText(String.valueOf(count));
    }


    @OnClick({R.id.btn_no_barcode, R.id.btn_pop_money_box, R.id.btn_clear, R.id.btn_settlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_no_barcode://无码商品
                final NoGoodsDialog dialog = new NoGoodsDialog();
                dialog.showCenter(getActivity());
                dialog.setOnDialogClickListener(new NoGoodsDialog.OnDialogClickListener() {
                    @Override
                    public void onDialogClick(String content) {
                        if (TextUtils.isEmpty(content)) {
                            showToast("请输入金额");
                            return;
                        }
                        showToast(content);
                        dialog.dismiss();
                    }
                });


                break;
            case R.id.btn_pop_money_box://弹出钱箱
//                RealMoneyRequestBody requestBody = new RealMoneyRequestBody();
//                requestBody.setShop_id("16");
//                requestBody.setOpenid("上海");
//
//                RealMoneyRequestBody.GoodsBean goodsBean = new RealMoneyRequestBody.GoodsBean();
//                goodsBean.setBarcode("6923450605226");
//                goodsBean.setGoods_count(1);
//                List<RealMoneyRequestBody.GoodsBean> list = new ArrayList<>();
//                list.add(goodsBean);
//                requestBody.setGoods(list);
//
//                String json = GsonUtils.getInstance().getGson().toJson(requestBody);
//                mPresenter.realMoney(json);



                break;
            case R.id.btn_clear://清空
                mGoodsAdapter.clear();

                break;
            case R.id.btn_settlement://收银 or  退款
                switch (mType) {
                    case TYPE_GOODS:

//                        if(mTotalMoney <= 0) {
//                            showToast("金额不能小于等于0！");
//                            return;
//                        }

                        ARouter.getInstance().build(ModulePath.settlement)
                                .withString("admin_name", admin_name)
                                .withInt("goods_count", mGoodsCount)
                                .withFloat("coupon", mCoupon)
                                .withFloat("total_money", mTotalMoney)
                                .withString("string_goods_data", GsonUtils.getInstance().getGson().toJson(mdata))
                                .withSerializable("goods_data", (Serializable) mdata)
                                .navigation();
                        break;
                    case TYPE_REFUND:
                        RefundActivity refundActivity = (RefundActivity) getActivity();
                        if (refundActivity != null)
                            refundActivity.toRefundCashFragment();
                        break;
                }

                break;
        }
    }

    @Override
    public void getGoodsSuccess(GoodsResponse goodsResponse) {
        String barcode = goodsResponse.getBarcode();
        mGoodsAdapter.addItem(barcode, goodsResponse);
    }

    @Override
    public void getGoodsFailed(Map<String, Object> map) {
        if(HttpExceptionEngine.isBussinessError(map)) {
            int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);
            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
            if(20001 == err_code) {
                showToast(err_msg);
            } else {
                showToast(err_msg);
            }
        }
    }

    @Override
    public void searchGoodsSuccess(GoodsResponse goodsResponse) {

    }

    @Override
    public void searchGoodsFailed(Map<String, Object> map) {

    }


    @Override
    public void realMoneySuccess(RealMoneyResponse result) {

    }

    @Override
    public void realMoneyFailed(Map<String, Object> map) {
        Log.i("test", "realMoneyFailed: " + (String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    public void checkPayStatusSuccess(CheckPayStatusResponse result) {

    }

    @Override
    public void checkPayStatusFailed(Map<String, Object> map) {

    }

}
