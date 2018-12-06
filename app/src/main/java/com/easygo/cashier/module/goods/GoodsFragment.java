package com.easygo.cashier.module.goods;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.R;
import com.easygo.cashier.Test;
import com.easygo.cashier.adapter.GoodsAdapter;
import com.easygo.cashier.bean.CheckPayStatusResponse;
import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.easygo.cashier.bean.request.CreateOrderRequestBody;
import com.easygo.cashier.bean.request.RealMoneyRequestBody;
import com.easygo.cashier.module.refund.RefundActivity;
import com.easygo.cashier.widget.NoGoodsDialog;
import com.niubility.library.base.BaseRxFragment;
import com.niubility.library.http.HttpExceptionEngine;
import com.niubility.library.utils.GsonUtils;
import com.niubility.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class GoodsFragment extends BaseRxFragment<GoodsContract.IView, GoodsPresenter> implements GoodsContract.IView{


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
    @BindView(R.id.cl_pop_money_box)
    ConstraintLayout clPopMoneyBox;
    @BindView(R.id.line1)
    View line1;

    private GoodsPresenter mGoodsPresenter;

    public static final int TYPE_GOODS = 0;
    public static final int TYPE_REFUND = 1;
    public static final String KEY_TYPE = "key_type";
    private int mType = TYPE_GOODS;


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
    }

    private void initView() {
        rvGoods.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        final GoodsAdapter goodsAdapter = new GoodsAdapter();
        rvGoods.setAdapter(goodsAdapter);

        goodsAdapter.setNewData(Test.getGoodsInfos());

        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo goodsInfo = (GoodsInfo) (adapter.getData().get(position));
                Toast.makeText(getContext(), goodsInfo.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        Bundle data = getArguments();
        if (data != null) {
            mType = data.getInt(KEY_TYPE, TYPE_GOODS);

        }
        switch (mType) {
            case TYPE_GOODS:
                clPopMoneyBox.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                btnSettlement.setText("收银：  ￥6.00");
                break;
            case TYPE_REFUND:
                clPopMoneyBox.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);
                btnSettlement.setText("退款：  ￥6.00");
                break;
        }

        btnSettlement.setText(mType == TYPE_GOODS ? "收银：  ￥6.00" : "退款：  ￥6.00");
    }


    @OnClick({R.id.cl_no_barcode_goods, R.id.cl_pop_money_box, R.id.btn_clear, R.id.btn_settlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_no_barcode_goods://无码商品
                final NoGoodsDialog dialog = new NoGoodsDialog();
                dialog.showCenter(getActivity());
                dialog.setOnDialogClickListener(new NoGoodsDialog.OnDialogClickListener() {
                    @Override
                    public void onDialogClick(String content) {
                        if (TextUtils.isEmpty(content)) {
                            ToastUtils.showToast(getActivity(), "请输入金额");
                            return;
                        }
                        ToastUtils.showToast(getActivity(), content);
                        dialog.dismiss();
                    }
                });


                break;
            case R.id.cl_pop_money_box://弹出钱箱
                RealMoneyRequestBody requestBody = new RealMoneyRequestBody();
                requestBody.setShop_id("16");
                requestBody.setOpenid("上海");

                RealMoneyRequestBody.GoodsBean goodsBean = new RealMoneyRequestBody.GoodsBean();
                goodsBean.setBarcode("6923450605226");
                goodsBean.setGoods_count(1);
                List<RealMoneyRequestBody.GoodsBean> list = new ArrayList<>();
                list.add(goodsBean);
                requestBody.setGoods(list);

                String json = GsonUtils.getInstance().getGson().toJson(requestBody);
                mPresenter.realMoney(json);


                break;
            case R.id.btn_clear://清空


                break;
            case R.id.btn_settlement://收银 or  退款
                switch (mType) {
                    case TYPE_GOODS:
                        MainActivity mainActivity = (MainActivity) getActivity();
                        if (mainActivity != null)
                            mainActivity.toCashierActivity();
                        break;
                    case TYPE_REFUND:
                        RefundActivity refundActivity = (RefundActivity) getActivity();
                        if (refundActivity != null)
                            refundActivity.toRefundFragment();
                        break;
                }

                break;
        }
    }

    public void createOrder() {
        CreateOrderRequestBody requestBody1 = new CreateOrderRequestBody();
        requestBody1.setShop_id("308");
        requestBody1.setOpenid("shanghai");
        requestBody1.setTotal_money("1");
        requestBody1.setReal_pay("1");
        requestBody1.setAct_sn("");
        requestBody1.setCoun_sn("");
        requestBody1.setIntegral(1);
        requestBody1.setWallet_pay(0);
        requestBody1.setUnion_id("");
        requestBody1.setCard_no("");
        requestBody1.setMember_money(1);
        CreateOrderRequestBody.GoodsBean goodsBean1 = new CreateOrderRequestBody.GoodsBean();
        goodsBean1.setBarcode("6921168550111");
        goodsBean1.setGoods_count(1);
        List<CreateOrderRequestBody.GoodsBean> list1 = new ArrayList<>();
        list1.add(goodsBean1);
        requestBody1.setGoods(list1);

        mPresenter.createOrder(GsonUtils.getInstance().getGson().toJson(requestBody1));
    }

//    mPresenter.checkPayStatus("201812055c07c454c6faa845457");
//    mPresenter.getGoods("308","6921168550111");

    @Override
    public void getGoodsSuccess(GoodsResponse goodsResponse) {

    }

    @Override
    public void getGoodsFailed(Map<String, Object> map) {

    }

    @Override
    public void searchGoodsSuccess(GoodsResponse goodsResponse) {

    }

    @Override
    public void searchGoodsFailed(Map<String, Object> map) {

    }

    @Override
    public void createOrderSuccess(String order_sn) {

    }

    @Override
    public void createOrderFailed(Map<String, Object> map) {

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
