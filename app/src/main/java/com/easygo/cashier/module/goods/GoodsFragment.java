package com.easygo.cashier.module.goods;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.BarcodeUtils;
import com.easygo.cashier.Configs;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.adapter.GoodsMultiItemAdapter;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.easygo.cashier.module.refund.RefundActivity;
import com.easygo.cashier.module.secondary_sreen.UserGoodsScreen;
import com.easygo.cashier.widget.GeneraDialog;
import com.easygo.cashier.widget.MySearchView;
import com.easygo.cashier.widget.NoGoodsDialog;
import com.easygo.cashier.widget.ProcessingChoiceDialog;
import com.easygo.cashier.widget.SearchResultWindow;
import com.niubility.library.base.BaseMvpFragment;
import com.niubility.library.http.exception.HttpExceptionEngine;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描商品
 */
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
    @BindView(R.id.cl_search)
    MySearchView clSearch;
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
    private GoodsMultiItemAdapter mGoodsMultiItemAdapter;

    /**
     * 商品数据
     */
    private List<GoodsEntity<GoodsResponse>> mData;

    /**
     * 商品总数
     */
    private int mGoodsCount;
    /**
     * 应收
     */
    private float mTotalMoney;
    /**
     * 优惠
     */
    private float mCoupon;

    /**
     * 搜索结果弹窗
     */
    private SearchResultWindow mSearchResultWindow;
    /**
     * 加工方式选择弹窗
     */
    private ProcessingChoiceDialog mProcessingChoiceDialog;

    private UserGoodsScreen mUserGoodsScreen;


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
        if (Configs.getRole(Configs.menus[1]) != 1) {
            btnSettlement.setVisibility(View.GONE);
        }
        initView();

        initBarcode();

        initUserGoodsScreen();
        if (mUserGoodsScreen != null) {
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SYSTEM_ALERT_WINDOW)
                    == PackageManager.PERMISSION_GRANTED) {
                mUserGoodsScreen.show();
            } else {
                showToast("悬浮窗权限");

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 0);
            }

//            mUserGoodsScreen.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mUserGoodsScreen.show();
            } else {
                showToast("权限没有通过");
            }
        }
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

    private int mGoodWeight;

    /**
     * 扫描到的条码 回调
     */
    private void onScanCode(String barcode) {
        Log.i(TAG, "onScanCode: barcode --> " + barcode);

        if ("2210000000019".equals(barcode)) {
            //测试
            showToast("测试数据");
//            mPresenter.getGoods(Configs.shop_sn, "096619438839");
            mPresenter.getGoods(Configs.shop_sn, "2212345");
//            mPresenter.getGoods(Configs.shop_sn, "发", 2);
            return;
        }

        if (TextUtils.isEmpty(barcode)) {
            showToast("barcode = null");
        } else if (BarcodeUtils.isWeightCode(barcode)) {//自编码

            String weight_barcode = BarcodeUtils.getProductCode(barcode);
            mGoodWeight = BarcodeUtils.getProductWeight(barcode);

            //获取称重商品信息
            mPresenter.getGoods(Configs.shop_sn, weight_barcode);

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

        mGoodsMultiItemAdapter = new GoodsMultiItemAdapter();
        rvGoods.setAdapter(mGoodsMultiItemAdapter);
        mData = mGoodsMultiItemAdapter.getData();

        mGoodsMultiItemAdapter.setOnItemListener(new GoodsMultiItemAdapter.OnItemListener() {
            @Override
            public void onPriceChange(float price, int count, float coupon) {
                refreshPrce(price, count, coupon);
            }

            @Override
            public void onProcessingClicked(int position, GoodsResponse current, List<GoodsResponse> processing_list) {
                showProcessingDialog(position, current, processing_list);
                for (GoodsResponse goodsResponse : processing_list) {
                    Log.i(TAG, "onProcessingClicked: name - " + goodsResponse.getG_sku_name());
                }
            }

            @Override
            public void onProcessingCheckedChanged(boolean isChecked, int position, GoodsResponse processing) {
                if (mUserGoodsScreen != null) {
                    mUserGoodsScreen.chooseProcessing(position, processing);
                }
            }

            @Override
            public void onCountChanged(int position, int count) {
                if (mUserGoodsScreen != null) {
                    mUserGoodsScreen.onCountChanged(position, count);
                }
            }

            @Override
            public void onItemRemoved(int position) {
                if (mUserGoodsScreen != null) {
                    mUserGoodsScreen.onItemRemoved(position);
                }
            }

        });
        //条目点击事件监听
        mGoodsMultiItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List data = adapter.getData();
                GoodsEntity goodsEntity = (GoodsEntity) data.get(position);
                switch (goodsEntity.getItemType()) {
                    case GoodsEntity.TYPE_WEIGHT:
                    case GoodsEntity.TYPE_ONLY_PROCESSING:
                    case GoodsEntity.TYPE_PROCESSING:
//                        mGoodsMultiItemAdapter.remove(position);
                        break;
                }
            }
        });


        //分割线
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        rvGoods.addItemDecoration(verticalDecoration);

        setIntentData();
        if (Configs.getRole(Configs.menus[21]) != 1) {
            clNoBarcode.setVisibility(View.GONE);
        }
        if (Configs.getRole(Configs.menus[13]) != 1){
            clPopMoneyBox.setVisibility(View.GONE);
        }

        clSearch.setOnSearchListenerClick(new MySearchView.OnSearhListenerClick() {
            @Override
            public void onSearch(String content) {
                if (!TextUtils.isEmpty(content.trim())) {
                    mPresenter.searchGoods(Configs.shop_sn, content.trim());
                }
            }
        });


    }

    public void initUserGoodsScreen() {
        Context context = getContext();
        if (context == null) {
            Log.i(TAG, "initUserGoodsScreen: context = null");
            return;
        }
        DisplayManager mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();

        if (mUserGoodsScreen == null && displays.length == 2) {
            mUserGoodsScreen = new UserGoodsScreen(context,
                    displays[displays.length - 1], admin_name);// displays[1]是副屏
            mUserGoodsScreen.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoodsMultiItemAdapter != null)
            mGoodsMultiItemAdapter.clear();
        if (mUserGoodsScreen != null)
            mUserGoodsScreen.clear();
    }

    /**
     * 显示加工方式选择框
     */
    private void showProcessingDialog(final int position, final GoodsResponse current, List<GoodsResponse> processing_list) {
        if (mProcessingChoiceDialog == null) {
            mProcessingChoiceDialog = new ProcessingChoiceDialog(getContext());
            mProcessingChoiceDialog.setCanceledOnTouchOutside(true);

            mProcessingChoiceDialog.create();
        }
        mProcessingChoiceDialog.setOnItemClickListener(new ProcessingChoiceDialog.OnItemClickListener() {
            @Override
            public void onItemClicked(GoodsResponse result) {
                mGoodsMultiItemAdapter.chooseProcessing(position, current, result);
                //刷新副屏
                if (mUserGoodsScreen != null)
                    mUserGoodsScreen.chooseProcessing(position, result);
            }
        });
        mProcessingChoiceDialog.setData(processing_list);
        mProcessingChoiceDialog.show();

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
                clNoBarcode.setVisibility(View.VISIBLE);
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
        float real_pay = price - coupon;

        DecimalFormat df = new DecimalFormat("#0.00");

        tvTotalMoney.setText("￥" + df.format(price));
        tvCoupon.setText("￥" + df.format(coupon));
        if (mType == TYPE_GOODS) {
            btnSettlement.setText(" 收银：  ￥" + df.format(real_pay) + " ");
        } else {
            btnSettlement.setText(" 退款：  ￥" + df.format(real_pay) + " ");
        }
        tvGoodsCount.setText(String.valueOf(count));


        //刷新用户副屏界面 价格
        if (mUserGoodsScreen != null) {
            mUserGoodsScreen.refreshPrice(mGoodsCount, mTotalMoney, mCoupon, mTotalMoney - mCoupon);
        }

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
                        } else if (content.startsWith(".") || content.startsWith("00")) {
                            showToast("请重新输入");
                            return;
                        }
                        float price = Float.valueOf(content);
                        if (price == 0) {
                            showToast("金额不能等于0");
                            return;
                        }
                        dialog.dismiss();

                        //添加无码商品
                        mGoodsMultiItemAdapter.addNoCodeItem(price);
                        rvGoods.smoothScrollToPosition(mGoodsMultiItemAdapter.getItemCount() - 1);
                        //刷新副屏
                        if (mUserGoodsScreen != null) {
                            mUserGoodsScreen.addNoCodeItem(price);
                            mUserGoodsScreen.toPosition();
                        }
                    }
                });


                break;
            case R.id.btn_pop_money_box://弹出钱箱

                mPresenter.popTill(Configs.shop_sn, Configs.printer_sn);

                break;
            case R.id.btn_clear://清空
                GeneraDialog generaDialog = GeneraDialog.getInstance("确认清空商品？", "取消", "确定");
                generaDialog.showCenter(getActivity());
                generaDialog.setOnDialogClickListener(new GeneraDialog.OnDialogClickListener() {
                    @Override
                    public void onSubmit() {
                        mGoodsMultiItemAdapter.clear();

                        if (mUserGoodsScreen != null) {
                            mUserGoodsScreen.clear();
                        }

                    }
                });
                break;
            case R.id.btn_settlement://收银 or  退款
                switch (mType) {
                    case TYPE_GOODS:

                        if (mTotalMoney <= 0) {
                            showToast("金额不能小于等于0！");
                            return;
                        }

                        ARouter.getInstance().build(ModulePath.settlement)
                                .withString("admin_name", admin_name)
                                .withInt("goods_count", mGoodsCount)
                                .withFloat("coupon", mCoupon)
                                .withFloat("total_money", mTotalMoney)
                                .withSerializable("goods_data", (Serializable) mData)
                                .navigation();
                        break;
                    case TYPE_REFUND:
                        RefundActivity refundActivity = (RefundActivity) getActivity();
                        if (refundActivity != null) {

                            Bundle bundle = new Bundle();
                            bundle.putInt("goods_count", mGoodsCount);
                            bundle.putFloat("total_money", mTotalMoney);
                            bundle.putSerializable("goods_data", (Serializable) mData);

                            refundActivity.toRefundCashFragment(bundle);
                        }
                        break;
                }

                break;
        }
    }

    @Override
    public void getGoodsSuccess(List<GoodsResponse> result) {

        mGoodsMultiItemAdapter.addItem(result, mGoodWeight != 0 ? mGoodWeight : 1);
        rvGoods.smoothScrollToPosition(mGoodsMultiItemAdapter.getItemCount() - 1);

        if (mUserGoodsScreen != null) {
            //没有显示时 显示用户商品列表副屏页面
            if (!mUserGoodsScreen.isShowing())
                mUserGoodsScreen.show();

            //刷新用户商品列表页面数据
            mUserGoodsScreen.addItem(result, mGoodWeight != 0 ? mGoodWeight : 1);
            mUserGoodsScreen.toPosition();
        }

        mGoodWeight = 0;
    }

    public int getAdapterSize() {
        return mGoodsMultiItemAdapter.getData().size();
    }

    @Override
    public void getGoodsFailed(Map<String, Object> map) {
        if (HttpExceptionEngine.isBussinessError(map)) {
            int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);
            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
            if (20001 == err_code) {
                showToast(err_msg);
            } else {
                showToast(err_msg);
            }
        }
        mGoodWeight = 0;
    }

    @Override
    public void searchGoodsSuccess(List<GoodsResponse> result) {

        if (getActivity() != null) {
            showSearchResultWindow(result);
        }

    }

    /**
     * 显示搜索结果弹窗
     */
    private void showSearchResultWindow(List<GoodsResponse> result) {
        if (mSearchResultWindow == null) {
            mSearchResultWindow = new SearchResultWindow(getContext());
            mSearchResultWindow.setOutsideTouchable(true);
            mSearchResultWindow.setWidth(clSearch.getSearchResultWidth());
            mSearchResultWindow.setHeight(-2);
            mSearchResultWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_search_result_frame));

            mSearchResultWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                //在dismiss中恢复透明度
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
            });

            mSearchResultWindow.setOnSearchResultClickListener(new SearchResultWindow.OnSearchResultClickListener() {
                @Override
                public void onSearchResultClicked(List<GoodsResponse> list) {
                    //添加
                    mGoodsMultiItemAdapter.addItem(list, 1);
                    //刷新副屏
                    if (mUserGoodsScreen != null)
                        mUserGoodsScreen.addItem(list, 1);

                    clSearch.clearText();
                    mSearchResultWindow.dismiss();
                }
            });
        }
        mSearchResultWindow.setData(result);
        if (!mSearchResultWindow.isShowing()) {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.4f;
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getActivity().getWindow().setAttributes(lp);
            mSearchResultWindow.showAsDropDown(clSearch);
        }
    }

    @Override
    public void searchGoodsFailed(Map<String, Object> map) {
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }


    @Override
    public void realMoneySuccess(RealMoneyResponse result) {

    }

    @Override
    public void realMoneyFailed(Map<String, Object> map) {
        Log.i("test", "realMoneyFailed: " + (String) map.get(HttpExceptionEngine.ErrorMsg));
    }


    @Override
    public void popTillSuccess() {
        showToast("弹出钱箱成功");
    }

    @Override
    public void popTillFailed(Map<String, Object> map) {
        showToast("弹出钱箱失败");

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mUserGoodsScreen != null && mUserGoodsScreen.isShowing()) {
            mUserGoodsScreen.dismiss();
        }
        if (mSearchResultWindow != null && mSearchResultWindow.isShowing()) {
            mSearchResultWindow.dismiss();
        }
        if (mProcessingChoiceDialog != null && mProcessingChoiceDialog.isShowing()) {
            mProcessingChoiceDialog.dismiss();
        }
    }
}
