package com.easygo.cashier.module.goods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.ActivitiesUtils;
import com.easygo.cashier.BarcodeUtils;
import com.easygo.cashier.Configs;
import com.easygo.cashier.Constants;
import com.easygo.cashier.MemberUtils;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.MyApplication;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.adapter.GoodsMultiItemAdapter;
import com.easygo.cashier.base.BaseAppMvpFragment;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.EntryOrders;
import com.easygo.cashier.bean.GiftCardResponse;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.MemberDayInfo;
import com.easygo.cashier.bean.MemberDiscountInfo;
import com.easygo.cashier.bean.MemberInfo;
import com.easygo.cashier.bean.ShopActivityResponse;
import com.easygo.cashier.module.CouponUtils;
import com.easygo.cashier.module.GiftCardUtils;
import com.easygo.cashier.module.login.LoginActivity;
import com.easygo.cashier.module.promotion.goods.BaseGoodsPromotion;
import com.easygo.cashier.module.secondary_sreen.UserGoodsScreen;
import com.easygo.cashier.printer.local.PrinterUtils;
import com.easygo.cashier.widget.ActivitiesView;
import com.easygo.cashier.widget.ChooseCouponsDialog;
import com.easygo.cashier.widget.ChooseMembersDialog;
import com.easygo.cashier.widget.CountTextView;
import com.easygo.cashier.widget.ExtraInfoView;
import com.easygo.cashier.widget.GeneraButton;
import com.easygo.cashier.widget.GeneraDialog;
import com.easygo.cashier.widget.GeneraEditDialog;
import com.easygo.cashier.widget.MySearchView;
import com.easygo.cashier.widget.PettyCashDialog;
import com.easygo.cashier.widget.ProcessingChoiceDialog;
import com.easygo.cashier.widget.ScanCodeDialog;
import com.easygo.cashier.widget.SearchResultWindow;
import com.easygo.cashier.widget.SetCountPopupWindow;
import com.easygo.cashier.widget.TempPromotionDialog;
import com.google.gson.reflect.TypeToken;
import com.niubility.library.base.BaseApplication;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.GsonUtils;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描商品
 */
public class GoodsFragment extends BaseAppMvpFragment<GoodsContract.IView, GoodsPresenter> implements GoodsContract.IView {

    public static final String TAG = "GoodsFragment";

    @BindView(R.id.tv_text_member_price)
    TextView tvMemberText;
    @BindView(R.id.cl_extra_info)
    ExtraInfoView clExtraInfo;

    @BindView(R.id.activities_view)
    ActivitiesView activitiesView;

    @BindView(R.id.cl_member)
    ConstraintLayout clMember;
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
    @BindView(R.id.cl_search)
    MySearchView clSearch;
    @BindView(R.id.btn_no_barcode)
    GeneraButton clNoBarcode;
    @BindView(R.id.et_barcode)
    EditText etBarcode;//监听扫码机

    private String admin_name;
    private GoodsMultiItemAdapter mGoodsMultiItemAdapter;

    private GeneraEditDialog editDialog;
    private ChooseMembersDialog membersDialog;
    private ChooseCouponsDialog couponsDialog;
    private ScanCodeDialog scanCodeDialog;

    DecimalFormat df = new DecimalFormat("0.00");

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
     * 优惠 (包含全部优惠和优惠券金额）
     */
    private float mCoupon;

    /**
     * 优惠券优惠的价格
     */
    private float mCouponMoney;

    /**
     * 右下角收银金额
     */
    private float mRealPay;


    /**
     * 搜索结果弹窗
     */
    private SearchResultWindow mSearchResultWindow;
    /**
     * 加工方式选择弹窗
     */
    private ProcessingChoiceDialog mProcessingChoiceDialog;
    /**
     * 设置商品数量弹窗
     */
    private SetCountPopupWindow mSetCountPopupWindow;

    private UserGoodsScreen mUserGoodsScreen;


    /**
     * 轮询间隔
     */
    private long interval = 30000;
    public static final int MSG_PROMOTION = 0;

    /**
     * 商品促销接口请求成功
     */
    private boolean goods_promotion_success;
    /**
     * 店铺促销接口请求成功
     */
    private boolean shop_promotion_success;
    /**
     * 是否前台显示
     */
    private boolean isForeground;
    /**
     * 是否正在刷新促销价格
     */
    private boolean refresh_promotion_price;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PROMOTION:
                    removeMessages(MSG_PROMOTION);
                    Log.i(TAG, "handleMessage: 获取促销信息");
                    getPromotion();
                    break;
            }
        }
    };

    /**
     * 调度轮询促销接口
     */
    private void scheduleGetPromotion() {
        if (goods_promotion_success && shop_promotion_success) {
            mHandler.sendEmptyMessageDelayed(MSG_PROMOTION, interval);

            refresh_promotion_price = true;
            computePrice(mTotalMoney, mGoodsCount, 0);
            refresh_promotion_price = false;
        }
    }

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
        if (Configs.getRole(Configs.menus[1]) == 0) {
            btnSettlement.setVisibility(View.GONE);
        }
        if (Configs.getRole(Configs.menus[21]) == 0) {
            clNoBarcode.setVisibility(View.GONE);
        }
        initView();

        initBarcode();

        initUserGoodsScreen();

        //发送心跳
        mPresenter.heartbeat();
        /* 得到是否有会员日或者有会员折扣信息 */
        mPresenter.getMemberDay();
        mPresenter.getMemberDiscount();

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
     * 是否是自编码
     */
    private boolean mIsSelfEncode;
    private int mGoodWeight;
    private int mGoodMoney;

    /**
     * 扫描到的条码 回调
     */
    private void onScanCode(String barcode) {
        Log.i(TAG, "onScanCode: barcode --> " + barcode);

        if (TextUtils.isEmpty(barcode)) {
//            showToast("barcode = null");
        } else if (BarcodeUtils.isWeightCode(barcode)) {//自编码

            mIsSelfEncode = true;
            String weight_barcode = BarcodeUtils.getProductCode(barcode);
            mGoodMoney = BarcodeUtils.getProductTotalMoney(barcode);
            mGoodWeight = BarcodeUtils.getProductWeight(barcode);

            //获取称重商品信息
            mPresenter.getGoods(Configs.shop_sn, weight_barcode);

        } else if (BarcodeUtils.isMember(barcode)) {
            mPresenter.getMember(null, barcode);
        } else {
            //获取商品信息
            mPresenter.getGoods(Configs.shop_sn, barcode);
        }
    }


    private void initView() {

        rvGoods.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mGoodsMultiItemAdapter = new GoodsMultiItemAdapter();
        mGoodsMultiItemAdapter.bindToRecyclerView(rvGoods);
        mData = mGoodsMultiItemAdapter.getData();

        mGoodsMultiItemAdapter.setOnItemListener(new GoodsMultiItemAdapter.OnItemListener() {
            @Override
            public void onPriceChange(float price, int count, float coupon) {

                computePrice(price, count, coupon);
            }

            @Override
            public void onProcessingClicked(int position, GoodsResponse current, List<GoodsResponse> processing_list) {
                showProcessingDialog(position, current, processing_list);
                for (GoodsResponse goodsResponse : processing_list) {
                    Log.i(TAG, "onProcessingClicked: name - " + goodsResponse.getG_sku_name());
                }
            }

            @Override
            public void onSaleCountNotEnough() {
                showToast("商品库存不足");
            }

            @Override
            public void onCountClick(CountTextView countTextView, int position, int count) {
                showSetCountDialog(countTextView, position, count);
            }

            @Override
            public boolean onPromotionTouch(View v, MotionEvent event, String name) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "onPromotionTouch按下: 促销活动" + name);
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "onPromotionTouch抬起: 促销活动" + name);
                        return true;
                }
                return false;
            }

            @Override
            public void onProcessingCheckedChanged(boolean isChecked, int position, GoodsResponse processing) {
            }

            @Override
            public void onCountChanged(int position, int count) {
            }

            @Override
            public void onItemRemoved(int position) {
                GoodsEntity<GoodsResponse> goodsEntity = mData.get(position);
                BaseGoodsPromotion promotion = goodsEntity.getPromotion();
                if(promotion != null && promotion.isTempGoodsPromotion()) {
                    //取消临时促销
                    ActivitiesUtils.getInstance().cancelTempGoodsPromotion(
                            goodsEntity.getData().getBarcode() + "_" + goodsEntity.getData().getPrice());
                }
            }

        });
        //条目点击事件监听
        mGoodsMultiItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List data = adapter.getData();
                GoodsEntity goodsEntity = (GoodsEntity) data.get(position);
                goodsEntity.setSelected(!goodsEntity.isSelected());

                view.setSelected(!view.isSelected());

                //判断选中的条目是否有临时促销， 更新取消按钮UI
                boolean enable = false;
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    goodsEntity = (GoodsEntity) data.get(i);
                    if (!goodsEntity.isSelected()) {
                        continue;
                    }
                    BaseGoodsPromotion promotion = goodsEntity.getPromotion();
                    if(promotion != null && promotion.isTempGoodsPromotion()) {
                        enable = true;
                        break;
                    }
                }
                activitiesView.enableCancelTempPromotionButton(enable);
            }
        });


        //分割线
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        rvGoods.addItemDecoration(verticalDecoration);

        setIntentData();

        clSearch.setOnSearchListenerClick(new MySearchView.OnSearhListenerClick() {
            @Override
            public void onSearch(String content) {
                if (!TextUtils.isEmpty(content.trim())) {
                    clSearch.startLoading();
                    mPresenter.searchGoods(Configs.shop_sn, content.trim());
                }
            }
        });

        clExtraInfo.setCallback(new ExtraInfoView.Callback() {
            @Override
            public void onCancelMember() {
                updateMemberInfo(null);
            }

            @Override
            public void onCancelCoupon() {
                cancelCouponWithRefresh();
            }

            @Override
            public void onCancelGiftCard() {
                cancelGiftCard();
                refreshPrice(mTotalMoney, mGoodsCount, mCoupon);
            }
        });


    }

    /**
     * 显示设置数字弹框
     */
    private void showSetCountDialog(final CountTextView countTextView, final int position, int count) {
        mSetCountPopupWindow = new SetCountPopupWindow(getContext(), countTextView, count);
        mSetCountPopupWindow.setOutsideTouchable(true);
        mSetCountPopupWindow.setFocusable(true);
        mSetCountPopupWindow.setWidth(-2);
        mSetCountPopupWindow.setHeight(-2);
        mSetCountPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mSetCountPopupWindow.setOnCountClickListener(new SetCountPopupWindow.OnClickListener() {
            @Override
            public void onCount(String content) {
                mGoodsMultiItemAdapter.setGoodsCount(position, Integer.parseInt(content));
            }
        });
        int[] location = new int[2];
        countTextView.getLocationOnScreen(location);
        mSetCountPopupWindow.showCenter();
    }

    /**
     * 商品价格变动时  计算促销、会员、优惠券
     *
     * @param price
     * @param count
     * @param coupon
     */
    private void computePrice(float price, int count, float coupon) {
        if (price == 0 && count == 0 && coupon == 0) {
            //清空数据时
            refreshPrice(price, count, coupon);
            showCurrentActivities(null);

            if(mUserGoodsScreen != null) {
                mUserGoodsScreen.clear();
            }
            return;
        }

        //是否可以跟优惠券共用
        boolean with_coupon = true;

        ActivitiesUtils.getInstance().promotion(mData);
        ActivitiesUtils.getInstance().getCurrentGoodsPromotions(mData);

        //-----------------------------------------------------

        coupon = 0f;
        float goods_coupon = 0f;
        float member_coupon = 0f;
        float shop_coupon = 0f;

        //判断是否有商品促销
        if (ActivitiesUtils.getInstance().hasGoodsPromotion()) {
            goods_coupon = ActivitiesUtils.getInstance().getGoodsPromotionMoney();
            coupon = goods_coupon;

            with_coupon = ActivitiesUtils.getInstance().isWith_coupon();
        }
        //判断是否有会员
        if (MemberUtils.isMember) {

            //计算会员价、日、折扣
            MemberUtils.computeMember(mData);

            coupon = Float.valueOf(mGoodsMultiItemAdapter.getTotalCoupon());
            member_coupon = coupon - goods_coupon;

            //没有商品促销时
            if (goods_coupon == 0) {
                with_coupon = true;
            }
        }
        //判断是否有店铺促销
        shop_coupon = ActivitiesUtils.getInstance().promotion(mData,
                mGoodsMultiItemAdapter.getShopTotal(ActivitiesUtils.getInstance().getShopExcludeBarcodeList()));
        if (shop_coupon > 0) {
            coupon += shop_coupon;

            with_coupon = ActivitiesUtils.getInstance().isWith_coupon();
        }

        //判断是否有优惠券可用
        if (with_coupon && CouponUtils.getInstance().getCouponInfo() != null) {
            float couponMoney = CouponUtils.getInstance().getCouponMoney(mData, Configs.shop_sn, price - coupon);

            Log.i(TAG, " 优惠券 优惠价格 --> " + couponMoney);
            mCouponMoney = couponMoney;

            coupon += mCouponMoney;

        } else {
            //取消优惠券
            cancelCoupon();
        }

        //判断是否有礼品卡
        if(GiftCardUtils.getInstance().getGiftCardInfo() == null) {
            cancelGiftCard();
        }

        //判断是否有临时订单促销
        if(ActivitiesUtils.getInstance().hasTempOrderPromotion()) {
            float tempOrderPromotionMoney = ActivitiesUtils.getInstance().getTempOrderPromotionMoney();
            coupon += tempOrderPromotionMoney;
        }

      //显示正在参与活动
        if (goods_coupon == 0 && member_coupon == 0 && shop_coupon == 0) {

            //没有任何促销、会员优惠
            //清除正在参与活动
            showCurrentActivities(null);
        } else {
            List<String> currentPromotionNames = new ArrayList<>();
            if (ActivitiesUtils.getInstance().hasGoodsPromotion()) {
                currentPromotionNames.addAll(ActivitiesUtils.getInstance().getCurrentPromotionNames());
            }
            if (member_coupon != 0) {
                currentPromotionNames.addAll(MemberUtils.currentNames);
            }
            if (ActivitiesUtils.getInstance().hasShopPromotion()) {
                currentPromotionNames.add(ActivitiesUtils.getInstance().getCurrentShopPromotion().getName());
            }
            //刷新正在参加的活动
            showCurrentActivities(currentPromotionNames);
        }


        //刷新价格
        refreshPrice(price, count, coupon);
        if (!rvGoods.isComputingLayout()) {
            //刷新界面显示促销
            mGoodsMultiItemAdapter.notifyDataSetChanged();
        }

        //刷新用户副屏商品优惠显示
        if(mUserGoodsScreen != null) {
            Log.i(TAG, "computePrice: 刷新用户副屏");
            mUserGoodsScreen.refreshGoodsData(mData);
        }
    }


    private void showCurrentActivities(List<String> data) {
        activitiesView.setVisibility(data != null && data.size() > 0 ? View.VISIBLE : View.GONE);
        activitiesView.setData(data);

        if (mUserGoodsScreen != null) {
            mUserGoodsScreen.showCurrentActivities(data);
        }

        if(ActivitiesUtils.getInstance().hasTempGoodsPromotion()) {
            activitiesView.showCancelTempPromotionButton(true);
        } else {
            activitiesView.showCancelTempPromotionButton(false);
        }

        //设置数量的弹窗如果弹出时 更新位置
        if(mSetCountPopupWindow != null && mSetCountPopupWindow.isShowing()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSetCountPopupWindow.showCenter();
                }
            }, 500);
        }

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
            mUserGoodsScreen = new UserGoodsScreen(MyApplication.sApplication,
                    displays[displays.length - 1], String.valueOf(Configs.cashier_id));// displays[1]是副屏
            mUserGoodsScreen.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mUserGoodsScreen.show();
        }
    }

    public void getPromotion() {
        goods_promotion_success = false;
        shop_promotion_success = false;

        mPresenter.goods_activity(Configs.shop_sn);
        mPresenter.shop_activity(Configs.shop_sn);
    }

    @Override
    public void onResume() {
        super.onResume();

        isForeground = true;
        mHandler.removeMessages(MSG_PROMOTION);
        getPromotion();
    }

    @Override
    public void onPause() {
        super.onPause();

        isForeground = false;
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 显示加工方式选择框
     */
    private void showProcessingDialog(final int position, final GoodsResponse current, List<GoodsResponse> processing_list) {
        if (mProcessingChoiceDialog == null) {
            mProcessingChoiceDialog = new ProcessingChoiceDialog(getContext());
            mProcessingChoiceDialog.getWindow().setGravity(Gravity.CENTER);
            mProcessingChoiceDialog.setCanceledOnTouchOutside(true);

            mProcessingChoiceDialog.create();
        }
        mProcessingChoiceDialog.setOnItemClickListener(new ProcessingChoiceDialog.OnItemClickListener() {
            @Override
            public void onItemClicked(GoodsResponse result) {
                mGoodsMultiItemAdapter.chooseProcessing(position, current, result);
            }
        });
        mProcessingChoiceDialog.setData(processing_list);
        mProcessingChoiceDialog.show();

    }

    private void setIntentData() {
        Bundle data = getArguments();
        if (data != null) {
            admin_name = data.getString(Constants.KEY_ADMIN_NAME);
        }
        btnSettlement.setText(" 收银：  ￥0.00 ");

    }

    /**
     * 刷新界面显示
     */
    private void refreshPrice(float price, int count, float coupon) {
        mGoodsCount = count;
        mTotalMoney = price;
        mCoupon = coupon;
        float real_pay = price - coupon;

        if (real_pay < 0) {
            real_pay = 0;
        } else {
            if (GiftCardUtils.getInstance().getGiftCardInfo() != null) {
                float giftCardBalance = GiftCardUtils.getInstance().getGiftCardInfo().getBalance_amount();
                if(giftCardBalance >= real_pay) {
                    real_pay = 0f;
                } else {
                    real_pay -= giftCardBalance;
                }
            }
        }
        mRealPay = real_pay;

        tvTotalMoney.setText("￥" + df.format(price));
        tvCoupon.setText("￥" + df.format(coupon));
        btnSettlement.setText(" 收银：  ￥" + df.format(real_pay) + " ");
        tvGoodsCount.setText(String.valueOf(count));


        //刷新用户副屏界面 价格
        if (mUserGoodsScreen != null) {
            mUserGoodsScreen.refreshPrice(mGoodsCount, mTotalMoney, mCoupon, real_pay);
        }
    }


    @OnClick({R.id.btn_no_barcode,
            R.id.btn_clear,
            R.id.btn_settlement,
            R.id.btn_orders,
            R.id.btn_temp_promotion,
            R.id.btn_cancel_temp_promotion,
            R.id.btn_gift_card,
            R.id.btn_choose_member,
            R.id.btn_choose_coupon,
            R.id.btn_quick_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_no_barcode://无码商品

                final PettyCashDialog dialog = new PettyCashDialog();
                dialog.showCenter(getActivity());
                dialog.setNoCode();
                dialog.setCanInputDecimal(true);
                dialog.setOnDialogClickListener(new PettyCashDialog.OnDialogClickListener() {
                    @Override
                    public void onClick(String content) {
                        float price = Float.valueOf(content);
                        if (price == 0) {
                            showToast("金额不能等于0");
                            return;
                        }
                        dialog.dismiss();

                        //添加无码商品
                        mGoodsMultiItemAdapter.addNoCodeItem(price);
                        rvGoods.smoothScrollToPosition(mGoodsMultiItemAdapter.getData().size() - 1);
                    }
                });

                break;
            case R.id.btn_clear://清空
                if (mGoodsMultiItemAdapter.getItemCount() <= 0) {
                    return;
                }
                GeneraDialog generaDialog = GeneraDialog.getInstance("确认清空商品？", "取消", "确定");
                generaDialog.showCenter(getActivity());
                generaDialog.setOnDialogClickListener(new GeneraDialog.OnDialogClickListener() {
                    @Override
                    public void onSubmit() {
                        mGoodsMultiItemAdapter.clear();

                        if (mUserGoodsScreen != null) {
                            mUserGoodsScreen.clear();
                        }
                        //清除临时促销
                        ActivitiesUtils.getInstance().clearTempPromotion();
                        //请求一次促销
                        mHandler.sendEmptyMessage(MSG_PROMOTION);

                    }
                });
                break;
            case R.id.btn_settlement://收银

                if (refresh_promotion_price) {
                    Log.i(TAG, "onViewClicked: 正在刷新促销价格");
                    return;
                }

                if (mTotalMoney <= 0 && mData.size() == 0) {
                    showToast("订单金额不能小于0！");
                    return;
                }

                String balance = clExtraInfo.getMemberBalance();
                ARouter.getInstance().build(ModulePath.settlement)
                        .withString("admin_name", admin_name)
                        .withInt("goods_count", mGoodsCount)
                        .withFloat("coupon", mCoupon - mCouponMoney)
                        .withFloat("coupon_money", mCouponMoney)
                        .withFloat("total_money", mTotalMoney)
                        .withString("member_balance", balance)
                        .withSerializable("goods_data", (Serializable) mData)
                        .navigation();
                break;
            case R.id.btn_orders:
                if (mGoodsMultiItemAdapter.getItemCount() <= 0) {
                    showToast("请先扫描商品");
                    return;
                }
                if (MemberUtils.isMember) {
                    showToast("当前存在会员，请先删除会员信息");
                    return;
                }
                if (editDialog == null) {
                    editDialog = new GeneraEditDialog();
                }
                editDialog.setVisiable(true);
                editDialog.showCenter(getActivity());
                editDialog.setType(GeneraEditDialog.ENTRY_ORDER);
                editDialog.setTitle(getResources().getString(R.string.text_entry_orders));
                editDialog.setOnDialogClickListener(new GeneraEditDialog.OnDialogClickListener() {
                    @Override
                    public void onContent(int type, String account, String password) {
                        SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication).edit();
                        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication);
                        String json = sp.getString(Constants.KEY_ENTRY_ORDERS_LIST, "");

                        List<EntryOrders> entryOrders;
                        if (!TextUtils.isEmpty(json)) {
                            entryOrders = GsonUtils.getInstance().getGson().fromJson(
                                    json, new TypeToken<List<EntryOrders>>() {
                                    }.getType());
                        } else {
                            entryOrders = new ArrayList<>();
                        }

                        EntryOrders orders = new EntryOrders();
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        orders.setEntry_orders_note(account);
                        orders.setEntry_orders_time(sdf.format(date));
                        orders.setEntry_orders_total_price(String.valueOf(mTotalMoney));
                        orders.setEntry_orders_total_number(String.valueOf(mGoodsCount));
                        orders.setGoodsEntityList(mGoodsMultiItemAdapter.getData());
                        orders.setSelect(false);
                        if (entryOrders != null) {
                            entryOrders.add(orders);
                        }

                        editor.putString(Constants.KEY_ENTRY_ORDERS_LIST, GsonUtils.getInstance().getGson().toJson(entryOrders)).apply();

                        mGoodsMultiItemAdapter.clear();

                        if (mUserGoodsScreen != null) {
                            mUserGoodsScreen.clear();
                        }
                        //清除临时促销
                        ActivitiesUtils.getInstance().clearTempPromotion();

                        editDialog.dialogDismiss();
                        editDialog.dismiss();
                    }
                });
                break;
            case R.id.btn_temp_promotion:
                List<GoodsEntity<GoodsResponse>> selected = mGoodsMultiItemAdapter.getSelected();
                if(selected == null) {
                    showToast("请选择商品");
                    return;
                }

                final TempPromotionDialog tempPromotionDialog = new TempPromotionDialog();
                tempPromotionDialog.setSelected(selected);
                tempPromotionDialog.setOnDialogClickListener(new TempPromotionDialog.OnDialogClickListener() {
                    @Override
                    public void onClick(List<GoodsEntity<GoodsResponse>> selectGoods, int mode, boolean isFreeOrder, float value) {
                        tempPromotionDialog.dismiss();

                        if(isFreeOrder) {
                            cancelCoupon();
                            cancelGiftCard();
                        }

                        ActivitiesUtils.getInstance().createTempGoodsPromotion(selectGoods, mode, isFreeOrder, value);

                        mGoodsMultiItemAdapter.cancelAllSelected();

                        computePrice(mTotalMoney, mGoodsCount, 0);

                    }
                });
                tempPromotionDialog.showCenter(getActivity());
                break;
            case R.id.btn_cancel_temp_promotion:
                List<GoodsEntity<GoodsResponse>> cancelSelected = mGoodsMultiItemAdapter.getSelected();
                if(cancelSelected == null) {
                    showToast("请选择商品");
                    return;
                }

                int size = cancelSelected.size();
                for (int i = 0; i < size; i++) {
                    GoodsEntity<GoodsResponse> goodsEntity = cancelSelected.get(i);
                    BaseGoodsPromotion promotion = goodsEntity.getPromotion();

                    if(promotion == null || !promotion.isTempGoodsPromotion()) {
                        continue;
                    }

                    //取消临时促销
                    ActivitiesUtils.getInstance().cancelTempGoodsPromotion(
                            goodsEntity.getData().getBarcode() + "_" + goodsEntity.getData().getPrice());

                }

                mGoodsMultiItemAdapter.cancelAllSelected();
                view.setEnabled(false);
                computePrice(mTotalMoney, mGoodsCount, 0);


                break;
            case R.id.btn_gift_card://礼品卡
                if(mRealPay <= 0 && mGoodsCount > 0) {
                    return;
                }

                showScanCodeDialog();
                if(scanCodeDialog != null) {
                    scanCodeDialog.setOnScanCodeListener(new ScanCodeDialog.OnScanCodeListener() {
                        @Override
                        public void onScanCode(String barcode) {
                            if (scanCodeDialog != null) {
                                scanCodeDialog.setStatus(ScanCodeDialog.STATUS_SCANNING);
                            }
                            scanCodeDialog.setStopScan(true);
                            mPresenter.gift_card(barcode);
                        }
                    });
                }
                setScanCodeDialogStatus(ScanCodeDialog.STATUS_SCAN_GIFT_CARD);
                break;
            case R.id.btn_choose_member://会员
                if (membersDialog == null) {
                    membersDialog = new ChooseMembersDialog();
                }
                membersDialog.showCenter(getActivity());
                membersDialog.setTitle(getResources().getString(R.string.text_choose_member));
                membersDialog.setOnSearchListener(new ChooseMembersDialog.OnSearchListener() {
                    @Override
                    public void onSearch(String content) {

                        if (BarcodeUtils.isMember(content)) {
                            mPresenter.getMember(null, content);
                        } else if (content.length() == 11 && TextUtils.isDigitsOnly(content)) {
                            mPresenter.getMember(content, null);
                        } else {
                            showToast("请输入正确的手机号/会员卡号");
                        }

                    }
                });
                break;
            case R.id.btn_choose_coupon://优惠券

                if(mRealPay <= 0 && mGoodsCount > 0) {
                    return;
                }
                //判断是否有促销
                if (ActivitiesUtils.getInstance().hasGoodsPromotion() || ActivitiesUtils.getInstance().hasShopPromotion()) {

                    if (!ActivitiesUtils.getInstance().isWith_coupon()) {
                        showToast("参与的促销活动不可与优惠券共用");
                        return;
                    }
                }

                if (couponsDialog == null) {
                    couponsDialog = new ChooseCouponsDialog();
                }
                couponsDialog.showCenter(getActivity());
                couponsDialog.setTitle(getResources().getString(R.string.text_coupon_coupon));
                couponsDialog.setOnSearchListener(new ChooseCouponsDialog.OnSearchListener() {
                    @Override
                    public void onSearch(String content) {
                        if (content.length() < 32) {
                            showToast("优惠券编号至少32位");
                            return;
                        }
                        mPresenter.get_coupon(content);
                    }

                    @Override
                    public void onItemClick(CouponResponse result) {
                        //设置优惠券信息
                        if (!MemberUtils.isMember) {
                            clMember.setVisibility(View.GONE);
                        }
                        clExtraInfo.setCouponData(result);
                        CouponUtils.getInstance().setCouponInfo(result);

                        computePrice(mTotalMoney, mGoodsCount, mCoupon);
                    }
                });
                break;
            case R.id.btn_quick_choose://快速选择
                ARouter.getInstance().build(ModulePath.quick).navigation();
                break;
        }
    }

    /**
     * 点击了弹出钱箱
     */
    public void clickPopTill() {
        if (Configs.till_auth == 0) {
            realPopTill();
            return;
        }
        if (editDialog == null) {
            editDialog = new GeneraEditDialog();
        }
        editDialog.setVisiable(true);
        editDialog.showCenter(getActivity());
        editDialog.setTitle(getResources().getString(R.string.text_cashbox_accredit));
        editDialog.setType(Configs.till_auth == 1 ? GeneraEditDialog.USER_ACCREDIT : GeneraEditDialog.USER_ACCOUNT);
        editDialog.setOnDialogClickListener(new GeneraEditDialog.OnDialogClickListener() {
            @Override
            public void onContent(int type, String account, String password) {
                if (type == GeneraEditDialog.USER_ACCREDIT) {
                    mPresenter.tillAuth("till", account, null, null, null, null);
                } else if (type == GeneraEditDialog.USER_ACCOUNT) {
                    mPresenter.tillAuth("till", null, null, null, account, password);
                }
            }
        });
    }

    /**
     * 弹出钱箱
     */
    public void realPopTill() {
        mPresenter.popTill(Configs.shop_sn, Configs.printer_sn);
        PrinterUtils.getInstance().popTill();
    }

    /**
     * 取消优惠券
     */
    private void cancelCoupon() {
        clExtraInfo.setCouponData(null);
        CouponUtils.getInstance().setCouponInfo(null);

        mCoupon -= mCouponMoney;

        mCouponMoney = 0f;

    }
    /**
     * 取消优惠券并刷新数据源
     */
    private void cancelCouponWithRefresh() {
        cancelCoupon();

        refreshPrice(mTotalMoney, mGoodsCount, mCoupon);
        mGoodsMultiItemAdapter.notifyDataSetChanged();
    }

    /**
     *  取消礼品卡
     */
    private void cancelGiftCard() {
        GiftCardUtils.getInstance().setGiftCardInfo(null);
        clExtraInfo.setGiftCardData(null);
    }


    @Override
    public void getGoodsSuccess(List<GoodsResponse> result) {
        if (result == null || result.size() == 0) {
            showToast("商品不存在");
            return;
        }

        float price;
        float count = 1f;
        int size = result.size();

        if (mIsSelfEncode) {//自编码
            if (mGoodWeight == 0) {//以个为单位的商品
                for (int i = 0; i < size; i++) {

                    GoodsResponse goodsResponse = result.get(i);
                    goodsResponse.setCount_disable(true);
                    price = Float.valueOf(goodsResponse.getPrice());

                    count = (mGoodMoney / 100f) / price;
                }
            } else {
                count = mGoodWeight;
            }
        }

        boolean add_success = mGoodsMultiItemAdapter.addItem(result, count);
        if (!add_success) {
            showToast("商品库存不足");
            return;
        }
        rvGoods.smoothScrollToPosition(mGoodsMultiItemAdapter.getData().size() - 1);

        if (mUserGoodsScreen != null) {
            //没有显示时 显示用户商品列表副屏页面
            if (!mUserGoodsScreen.isShowing())
                mUserGoodsScreen.show();
        }

        mIsSelfEncode = false;
        mGoodMoney = 0;
        mGoodWeight = 0;

    }

    @Override
    public void getGoodsFailed(Map<String, Object> map) {
        int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);
        String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
        showToast(err_msg);
        mIsSelfEncode = false;
        mGoodMoney = 0;
        mGoodWeight = 0;
    }

    @Override
    public void searchGoodsSuccess(List<GoodsResponse> result) {

        if (getActivity() != null) {
            clSearch.stopLoading();
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
            mSearchResultWindow.setElevation(getResources().getDimensionPixelSize(R.dimen.x20));
            mSearchResultWindow.setWidth(clSearch.getSearchResultWidth());
            mSearchResultWindow.setHeight(-2);
            mSearchResultWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_search_result_frame));


            mSearchResultWindow.setOnSearchResultClickListener(new SearchResultWindow.OnSearchResultClickListener() {
                @Override
                public void onSearchResultClicked(List<GoodsResponse> list) {

                    int size = list.size();
                    float count = 1;
                    for (int i = 0; i < size; i++) {
                        GoodsResponse goodsResponse = list.get(i);
                        if (goodsResponse.getIs_weigh() != 1 || goodsResponse.getParent_id() != 0) {
                            continue;
                        }
                        if(goodsResponse.isJin()) {
                            count = 500;//500g 等于 1斤
                        } else {
                            count = 1000;//1000g 等于 1kg
                        }
                    }
                    //添加
                    boolean add_success = mGoodsMultiItemAdapter.addItem(list, count);
                    if (!add_success) {
                        showToast("商品库存不足");
                        clSearch.clearText();
                        mSearchResultWindow.dismiss();
                        return;
                    }
                    rvGoods.smoothScrollToPosition(mGoodsMultiItemAdapter.getData().size() - 1);

                    clSearch.clearText();
                    mSearchResultWindow.dismiss();
                }
            });
        }
        mSearchResultWindow.setData(result);
        if (!mSearchResultWindow.isShowing()) {
            mSearchResultWindow.showAsDropDown(clSearch);
        }
    }

    @Override
    public void searchGoodsFailed(Map<String, Object> map) {
        clSearch.stopLoading();
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    public void popTillSuccess() {
//        showToast("弹出钱箱成功");
    }

    @Override
    public void popTillFailed(Map<String, Object> map) {
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    public void goodsActivitySuccess(GoodsActivityResponse result) {
        Log.i(TAG, "goodsActivitySuccess: 商品促销成功");

        if (!isForeground) {
            Log.i(TAG, "goodsActivitySuccess: 取消解析商品促销");
            return;
        }

        goods_promotion_success = true;
        ActivitiesUtils.getInstance().parseGoods(result);

        scheduleGetPromotion();

    }

    @Override
    public void goodsActivityFailed(Map<String, Object> map) {
        Log.i(TAG, "goodsActivityFailed: 商品促销失败");
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));

        goods_promotion_success = false;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.goods_activity(Configs.shop_sn);
            }
        }, 1000);

    }

    @Override
    public void shopActivitySuccess(ShopActivityResponse result) {
        Log.i(TAG, "shopActivitySuccess: 店铺促销成功");

        if (!isForeground) {
            Log.i(TAG, "shopActivitySuccess: 取消解析店铺促销");
            return;
        }

        shop_promotion_success = true;
        ActivitiesUtils.getInstance().parseShop(result);

        scheduleGetPromotion();

    }

    @Override
    public void shopActivityFailed(Map<String, Object> map) {
        Log.i(TAG, "shopActivityFailed: 店铺促销失败");
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));

        shop_promotion_success = false;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.shop_activity(Configs.shop_sn);
            }
        }, 1000);
    }

    @Override
    public void getMemberSuccess(MemberInfo memberInfo, String barcode, String phone) {
        if (membersDialog != null && membersDialog.isShow()) {
            List<MemberInfo> infos = new ArrayList<>();
            infos.add(memberInfo);
            membersDialog.setNewData(infos);
        } else {
            MemberUtils.memberInfo = memberInfo;
            updateMemberInfo(memberInfo);
        }

    }

    @Override
    public void getMemberFailed(Map<String, Object> map, String barcode, String phone) {
        if (!TextUtils.isEmpty(barcode)) {
            showScanCodeDialog();
            setScanCodeDialogStatus(ScanCodeDialog.STATUS_MEMBER_NULL);
        } else if (!TextUtils.isEmpty(phone)) {
            if (membersDialog != null && membersDialog.isShow()) {
                membersDialog.setNewData(new ArrayList<MemberInfo>());
            }
        }
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    public void getMemberDaySuccess(List<MemberDayInfo> memberDayInfos) {
        if (memberDayInfos != null) {
            if (memberDayInfos.size() == 0) {
                return;
            }
            MemberUtils.sMemberDayInfo = memberDayInfos.get(0);
            if (MemberUtils.isDateMemberDay(memberDayInfos.get(0))) {
                MemberUtils.isMemberDay = true;
            }
        }
    }

    @Override
    public void getMemberDayFailed(Map<String, Object> map) {
        Log.e(TAG, "getMemberDayFailed: 无会员日活动");
        MemberUtils.isMemberDay = false;
    }

    @Override
    public void getMemberDiscountSuccess(List<MemberDiscountInfo> memberDiscountInfos) {
        if (memberDiscountInfos != null) {
            if (memberDiscountInfos.size() == 0) {
                return;
            }
            MemberUtils.sMemberDiscountInfo = memberDiscountInfos.get(0);
            Log.i(TAG, "getMemberDiscountSuccess：有会员固定折扣");
            MemberUtils.isMemberDiscount = true;
            MemberUtils.discount = (100 - memberDiscountInfos.get(0).getDiscount_amount()) * 0.01;
            MemberUtils.discount_rc_id = memberDiscountInfos.get(0).getRc_id();

        }
    }

    @Override
    public void getMemberDiscountFailed(Map<String, Object> map) {
        Log.e(TAG, "getMemberDiscountFailed: 无会员固定折扣");
        MemberUtils.isMemberDiscount = false;
    }

    @Override
    public void couponSuccess(CouponResponse result) {
        ArrayList<CouponResponse> couponResponses = new ArrayList<>();
        result.setCoupon_sn(couponsDialog.getSearchView().getContent());
        couponResponses.add(result);
        couponsDialog.setNewData(couponResponses);

    }

    @Override
    public void couponFailed(Map<String, Object> map) {
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    public void giftCardSuccess(GiftCardResponse result) {
        if(result.getBalance_amount() <= 0) {
            if(scanCodeDialog != null && scanCodeDialog.isShowing()) {
                scanCodeDialog.setStatus(ScanCodeDialog.STATUS_GIFT_CARD_NULL);
            }
            return;
        }
        if(scanCodeDialog != null && scanCodeDialog.isShowing()) {
            scanCodeDialog.dismiss();
        }

        GiftCardUtils.getInstance().setGiftCardInfo(result);
        clExtraInfo.setGiftCardData(result);

        computePrice(mTotalMoney, mGoodsCount, mCoupon);
    }

    @Override
    public void giftCardFailed(Map<String, Object> map) {
        if(scanCodeDialog != null && scanCodeDialog.isShowing()) {
            scanCodeDialog.setStatus(ScanCodeDialog.STATUS_GIFT_CARD_NULL);
        }
    }

    @Override
    public void getTillAuthSuccess(String result) {
        Log.i(TAG, "getTillAuthSuccess :校验钱箱权限成功");
        if (editDialog != null && editDialog.isShow()){
            editDialog.dismiss();
        }
        realPopTill();
    }

    @Override
    public void getTillAythFailed(Map<String, Object> map) {
        Log.i(TAG, "getTillAuthSuccess :校验钱箱权限失败");
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    public void getLockSuccess(String result) {
        if (editDialog != null && editDialog.isShow()){
            editDialog.dismiss();
        }
    }

    @Override
    public void getLockFailed(Map<String, Object> map) {
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
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
        if (mSetCountPopupWindow != null && mSetCountPopupWindow.isShowing()) {
            mSetCountPopupWindow.dismiss();
        }
        if (scanCodeDialog != null && scanCodeDialog.isShowing()) {
            scanCodeDialog.dismiss();
        }

        mHandler.removeCallbacksAndMessages(null);
    }

    // 选择挂单添加item
    public void addData(EntryOrders entryOrders) {
        List<GoodsEntity<GoodsResponse>> goodsEntityList = entryOrders.getGoodsEntityList();
        mData.addAll(goodsEntityList);
        mGoodsMultiItemAdapter.addOrdersData(goodsEntityList);
    }

    // 退款或收银 清除界面
    public void clearInfo() {
        if (mGoodsMultiItemAdapter != null)
            mGoodsMultiItemAdapter.clear();
        if (mUserGoodsScreen != null)
            mUserGoodsScreen.clear();
        //清除临时促销
        ActivitiesUtils.getInstance().clearTempPromotion();
        //隐藏 清空会员及 优惠券
        clExtraInfo.setHide(clExtraInfo);
        CouponUtils.getInstance().setCouponInfo(null);
        updateMemberInfo(null);
        GiftCardUtils.getInstance().setGiftCardInfo(null);
    }

    public void showScanCodeDialog() {
        scanCodeDialog = new ScanCodeDialog(getActivity(), R.style.DialogStyle);
        WindowManager.LayoutParams lp = scanCodeDialog.getWindow().getAttributes();
        lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        lp.y = getResources().getDimensionPixelSize(R.dimen.y411);
        scanCodeDialog.getWindow().setAttributes(lp);
        scanCodeDialog.setCanceledOnTouchOutside(false);
        scanCodeDialog.setCancelable(false);
        scanCodeDialog.show();
    }
    public void setScanCodeDialogStatus(int status) {

        if(scanCodeDialog != null && scanCodeDialog.isShowing()) {
            scanCodeDialog.setStatus(status);
        }
    }

    public void updateMemberInfo(MemberInfo info) {
        setMemberVisiable(info != null);
        clExtraInfo.setMemberData(info);

        computePrice(mTotalMoney, mGoodsCount, 0);

    }

    /**
     * 设置会员价一列是否显示
     *
     * @param visiable
     */
    public void setMemberVisiable(boolean visiable) {
        tvMemberText.setVisibility(visiable ? View.VISIBLE : View.GONE);
        if (mUserGoodsScreen != null) {
            mUserGoodsScreen.setMemberVisiable(visiable);
        }
        mGoodsMultiItemAdapter.notifyDataSetChanged();
    }

    /**
     * 快速选择
     * @param goodsResponses
     */
    public void addChooseInfo(List<GoodsResponse> goodsResponses) {
        for (GoodsResponse info : goodsResponses) {
            List<GoodsResponse> infos = new ArrayList<>();
            infos.add(info);
            boolean add_success = mGoodsMultiItemAdapter.addItem(infos, 1);
            if (!add_success) {
                showToast("商品库存不足");
                return;
            }
            rvGoods.smoothScrollToPosition(mGoodsMultiItemAdapter.getData().size() - 1);

            if (mUserGoodsScreen != null) {
                //没有显示时 显示用户商品列表副屏页面
                if (!mUserGoodsScreen.isShowing())
                    mUserGoodsScreen.show();
            }
        }
    }

    /**
     * 刷新商品数据
     */
    public void refreshGoodsData(List<GoodsEntity<GoodsResponse>> data) {
        Log.i(TAG, "refreshGoodsData: ");
        mData = data;
        mGoodsMultiItemAdapter.refreshGoodsData(data);
        mData = mGoodsMultiItemAdapter.getData();

        computePrice(mTotalMoney, mGoodsCount, 0);

        //更新优惠券信息
        CouponResponse couponInfo = CouponUtils.getInstance().getCouponInfo();
        if(couponInfo != null) {
            clExtraInfo.setCouponData(couponInfo);
        }

        //更新礼品卡信息
        GiftCardResponse giftCardInfo = GiftCardUtils.getInstance().getGiftCardInfo();
        if(giftCardInfo != null) {
            clExtraInfo.setGiftCardData(giftCardInfo);

        }

    }

    public void lockCashier() {
        GeneraDialog generaDialog = GeneraDialog.getInstance("确认锁定收银机？", "取消", "确定");
        generaDialog.showCenter(getActivity());
        generaDialog.setOnDialogClickListener(new GeneraDialog.OnDialogClickListener() {
            @Override
            public void onSubmit() {
                realLockCashier();
            }
        });
    }

    /**
     * 锁定收银机
     */
    private void realLockCashier() {
        if (editDialog == null) {
            editDialog = new GeneraEditDialog();
        }
        editDialog.showCenter(getActivity());
        editDialog.setCancelable(false);
        if (Configs.lock_auth == 1) {
            editDialog.setTitle(getResources().getString(R.string.text_lock));
        } else if (Configs.lock_auth == 2) {
            editDialog.setTitle(getResources().getString(R.string.text_lock_user));
        }
        editDialog.setVisiable(false);
        editDialog.setType(Configs.lock_auth == 1 ? GeneraEditDialog.USER_ACCREDIT : GeneraEditDialog.USER_ACCOUNT);
        editDialog.setOnDialogClickListener(new GeneraEditDialog.OnDialogClickListener() {
            @Override
            public void onContent(int type, String account, String password) {
                if (type == GeneraEditDialog.USER_ACCREDIT) {
                    mPresenter.tillAuth("lock", null, account, null, null, null);
                } else if (type == GeneraEditDialog.USER_ACCOUNT) {
                    mPresenter.tillAuth("lock", null, null, null, account, password);
                }
            }
        });
    }

    public void switchMode(final int mode) {

        String text = getString(Configs.mode_offline == mode? R.string.text_switch_mode_offline: R.string.text_switch_mode_online);

        GeneraDialog generaDialog = GeneraDialog.getInstance(text + ", 需要\n重新登录？", "取消", "确定");
        generaDialog.showCenter(getActivity());
        generaDialog.setOnDialogClickListener(new GeneraDialog.OnDialogClickListener() {
            @Override
            public void onSubmit() {
                realSwitchMode(mode);
            }
        });
    }

    public void realSwitchMode(int mode) {
        switch(mode) {
            case Configs.mode_offline:
                Configs.current_mode = Configs.mode_offline;
                showToast("已切换到离线模式");
                break;
            case Configs.mode_online:
                Configs.current_mode = Configs.mode_online;
                showToast("已切换到在线模式");
                break;
        }

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
