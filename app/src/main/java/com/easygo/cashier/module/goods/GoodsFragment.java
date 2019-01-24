package com.easygo.cashier.module.goods;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.ActivitiesUtils;
import com.easygo.cashier.BarcodeUtils;
import com.easygo.cashier.Configs;
import com.easygo.cashier.MemberUtils;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.adapter.GoodsMultiItemAdapter;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.EntryOrders;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.MemberDayInfo;
import com.easygo.cashier.bean.MemberDiscountInfo;
import com.easygo.cashier.bean.MemberInfo;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.easygo.cashier.bean.ShopActivityResponse;
import com.easygo.cashier.module.CouponUtils;
import com.easygo.cashier.module.refund.RefundActivity;
import com.easygo.cashier.module.secondary_sreen.UserGoodsScreen;
import com.easygo.cashier.widget.ActivitiesView;
import com.easygo.cashier.widget.ChooseCouponsDialog;
import com.easygo.cashier.widget.ChooseMembersDialog;
import com.easygo.cashier.widget.GeneraDialog;
import com.easygo.cashier.widget.GeneraEditDialog;
import com.easygo.cashier.widget.MySearchView;
import com.easygo.cashier.widget.PettyCashDialog;
import com.easygo.cashier.widget.ProcessingChoiceDialog;
import com.easygo.cashier.widget.ScanCodeDialog;
import com.easygo.cashier.widget.SearchResultWindow;
import com.google.gson.reflect.TypeToken;
import com.niubility.library.base.BaseApplication;
import com.niubility.library.base.BaseMvpFragment;
import com.niubility.library.constants.Constans;
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
public class GoodsFragment extends BaseMvpFragment<GoodsContract.IView, GoodsPresenter> implements GoodsContract.IView {

    public static final String TAG = "GoodsFragment";

    @BindView(R.id.tv_text_member_price)
    TextView tvMemberText;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_member)
    TextView tvMember;
    @BindView(R.id.cl_extra_info)
    ConstraintLayout clExtraInfo;
    @BindView(R.id.tv_coupon_no)
    TextView tvCouponNo;
    @BindView(R.id.tv_coupon_price)
    TextView tvCouponPrice;
    @BindView(R.id.activities_view)
    ActivitiesView activitiesView;
    @BindView(R.id.btn_choose_member)
    Button btnChooseMember;
    @BindView(R.id.btn_choose_coupon)
    Button btnChooseCoupon;

    @BindView(R.id.cl_member)
    ConstraintLayout clMember;
    @BindView(R.id.cl_coupon)
    ConstraintLayout clCoupon;
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
     * 搜索结果弹窗
     */
    private SearchResultWindow mSearchResultWindow;
    /**
     * 加工方式选择弹窗
     */
    private ProcessingChoiceDialog mProcessingChoiceDialog;

    private UserGoodsScreen mUserGoodsScreen;

    private boolean start = true;


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
        initView();

        initBarcode();

        initUserGoodsScreen();

//        btnChooseCoupon.setVisibility(View.GONE);
//        btnChooseMember.setVisibility(View.GONE);

//        mPresenter.goods_activity(Configs.shop_sn);
//        mPresenter.shop_activity(Configs.shop_sn);


        //发送心跳
        mPresenter.heartbeat();

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
//            showToast("barcode = null");
        } else if (BarcodeUtils.isWeightCode(barcode)) {//自编码

            String weight_barcode = BarcodeUtils.getProductCode(barcode);
            mGoodWeight = BarcodeUtils.getProductWeight(barcode);

            //获取称重商品信息
            mPresenter.getGoods(Configs.shop_sn, weight_barcode);

        } else if (BarcodeUtils.isMember(barcode)) {
            mPresenter.getMember(null, barcode);
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
        /* 得到是否有会员日或者有会员折扣信息 */
        mPresenter.getMemberDay();
        mPresenter.getMemberDiscount();

        rvGoods.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mGoodsMultiItemAdapter = new GoodsMultiItemAdapter();
        rvGoods.setAdapter(mGoodsMultiItemAdapter);
        mData = mGoodsMultiItemAdapter.getData();

        mGoodsMultiItemAdapter.setOnItemListener(new GoodsMultiItemAdapter.OnItemListener() {
            @Override
            public void onPriceChange(float price, int count, float coupon) {

//                refreshPrice(price, count, coupon);
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
        if (Configs.getRole(Configs.menus[21]) == 0) {
            clNoBarcode.setVisibility(View.GONE);
        }
        if (Configs.getRole(Configs.menus[13]) == 0) {
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

    /**
     * 商品价格变动时  计算促销、会员、优惠券
     * @param price
     * @param count
     * @param coupon
     */
    private void computePrice(float price, int count, float coupon) {
        if(price == 0 && count == 0 && coupon == 0) {
            //清空数据时
            refreshPrice(price, count, coupon);
            showCurrentActivities(null);
            return;
        }

        //是否可以跟优惠券共用
        boolean with_coupon = false;

        ActivitiesUtils.getInstance().promotion(mData);
        ActivitiesUtils.getInstance().getCurrentGoodsPromotions(mData);
        //判断是否有商品促销
        if(ActivitiesUtils.getInstance().hasGoodsPromotion()) {

            //刷新正在参加的活动
            showCurrentActivities(ActivitiesUtils.getInstance().getCurrentPromotionNames());


            if(!MemberUtils.isMember) {//不是会员时
                coupon = ActivitiesUtils.getInstance().getGoodsPromotionMoney();
            } else {

                //计算会员价、日、折扣
                MemberUtils.computeMember(mData);

                coupon = Float.valueOf(mGoodsMultiItemAdapter.getTotalCoupon());
            }

            with_coupon = ActivitiesUtils.getInstance().isWith_coupon();
        } else {
            //判断是否有店铺促销
            coupon = ActivitiesUtils.getInstance().promotion(mGoodsMultiItemAdapter.getSubtotal());
            if(coupon > 0) {
                //符合店铺促销条件
                showCurrentActivities(ActivitiesUtils.getInstance().getCurrentPromotionNames());

                with_coupon = ActivitiesUtils.getInstance().isWith_coupon();

            }
            //判断是否有会员
            else if(MemberUtils.isMember) {
                //计算会员价、日、折扣
                MemberUtils.computeMember(mData);

                coupon = Float.valueOf(mGoodsMultiItemAdapter.getTotalCoupon());

                with_coupon = true;

                //清除正在参与活动
                showCurrentActivities(MemberUtils.currentNames);
            }
            //没有任何促销优惠
            else {
                //清除正在参与活动
                showCurrentActivities(null);
            }
        }
        //判断是否有优惠券可用
        if(with_coupon) {
            if (CouponUtils.getInstance().getCouponInfo() != null) {
                float couponMoney = CouponUtils.getInstance().getCouponMoney(price);

                Log.i(TAG, " 优惠券 优惠价格 --> " + couponMoney);
                mCouponMoney = couponMoney;

                coupon += mCouponMoney;

            }
        } else {
            //取消优惠券
            cancelCoupon();
        }
        //刷新价格
        refreshPrice(price, count, coupon);
        //刷新界面显示促销
        mGoodsMultiItemAdapter.notifyDataSetChanged();
    }



    private void showCurrentActivities(List<String> data) {
        activitiesView.setVisibility(data != null && data.size() > 0 ? View.VISIBLE: View.GONE);
        activitiesView.setData(data);

        if(mUserGoodsScreen != null) {
            mUserGoodsScreen.showCurrentActivities(data);
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
            mUserGoodsScreen = new UserGoodsScreen(context,
                    displays[displays.length - 1], admin_name);// displays[1]是副屏
            mUserGoodsScreen.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mUserGoodsScreen.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(start) {
            mPresenter.goods_activity(Configs.shop_sn);
            mPresenter.shop_activity(Configs.shop_sn);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (mGoodsMultiItemAdapter != null)
//            mGoodsMultiItemAdapter.clear();
//        if (mUserGoodsScreen != null)
//            mUserGoodsScreen.clear();
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
    private void refreshPrice(float price, int count, float coupon) {
        mGoodsCount = count;
        mTotalMoney = price;
        mCoupon = coupon;
        float real_pay = price - coupon;

        if(real_pay < 0) {
            real_pay = 0;
        }

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
            mUserGoodsScreen.refreshPrice(mGoodsCount, mTotalMoney, mCoupon, real_pay);
        }

    }


    @OnClick({R.id.btn_no_barcode,
            R.id.btn_pop_money_box,
            R.id.btn_clear,
            R.id.btn_settlement,
            R.id.btn_orders,
            R.id.btn_choose_member,
            R.id.btn_choose_coupon,
            R.id.iv_cancel_member,
            R.id.iv_cancel_coupon,
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

                    }
                });
                break;
            case R.id.btn_settlement://收银 or  退款
                switch (mType) {
                    case TYPE_GOODS:

                        if (mTotalMoney <= 0 && mData.size() == 0) {
                            showToast("订单金额不能小于0！");
                            return;
                        }
//                        else if (real_pay < 0) {
//                            showToast("收银金额不能小于0！");
//                            return;
//                        }

                        String balance = MemberUtils.isMember ? tvBalance.getText().toString() : "";
                        ARouter.getInstance().build(ModulePath.settlement)
                                .withString("admin_name", admin_name)
                                .withInt("goods_count", mGoodsCount)
                                .withFloat("coupon", mCoupon-mCouponMoney)
                                .withFloat("coupon_money",  mCouponMoney)
                                .withFloat("total_money", mTotalMoney)
                                .withString("member_balance", balance)
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
                editDialog.showCenter(getActivity());
                editDialog.setTitle(getResources().getString(R.string.text_entry_orders));
                editDialog.setOnDialogClickListener(new GeneraEditDialog.OnDialogClickListener() {
                    @Override
                    public void onContent(String content) {
                        SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication).edit();
                        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication);
                        String json = sp.getString(Constans.KEY_ENTRY_ORDERS_LIST, "");

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

                        orders.setEntry_orders_note(content);
                        orders.setEntry_orders_time(sdf.format(date));
//                        orders.setEntry_orders_total_price(tvTotalMoney.getText().toString());
//                        orders.setEntry_orders_total_number(tvGoodsCount.getText().toString());
                        orders.setEntry_orders_total_price(String.valueOf(mTotalMoney));
                        orders.setEntry_orders_total_number(String.valueOf(mGoodsCount));
                        orders.setGoodsEntityList(mGoodsMultiItemAdapter.getData());
                        orders.setSelect(false);
                        if (entryOrders != null) {
                            entryOrders.add(orders);
                        }

                        editor.putString(Constans.KEY_ENTRY_ORDERS_LIST, GsonUtils.getInstance().getGson().toJson(entryOrders)).apply();

                        mGoodsMultiItemAdapter.clear();

                        if (mUserGoodsScreen != null) {
                            mUserGoodsScreen.clear();
                        }
                        editDialog.clearInfo();
                    }
                });
                break;
            case R.id.btn_choose_member:
                if (membersDialog == null) {
                    membersDialog = new ChooseMembersDialog();
                }
                membersDialog.showCenter(getActivity());
                membersDialog.setTitle(getResources().getString(R.string.text_choose_member));
                membersDialog.setOnSearchListener(new ChooseMembersDialog.OnSearchListener() {
                    @Override
                    public void onSearch(String content) {

                        if(BarcodeUtils.isMember(content)) {
                            mPresenter.getMember(null, content);
                        } else if(content.length() == 11 && TextUtils.isDigitsOnly(content)){
                            mPresenter.getMember(content, null);
                        } else {
                            showToast("请输入正确的手机号");
                        }

                    }
                });
                break;
            case R.id.btn_choose_coupon:
                //判断是否有促销
                if(ActivitiesUtils.getInstance().hasGoodsPromotion() || ActivitiesUtils.getInstance().hasShopPromotion()) {

                    if(!ActivitiesUtils.getInstance().isWith_coupon()) {
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
                        if(content.length() < 32) {
                            showToast("优惠券编号至少32位");
                            return;
                        }
                        mPresenter.get_coupon(content);
                    }

                    @Override
                    public void onItemClick(CouponResponse result) {
                        //设置优惠券信息
                        setShow(clCoupon);
                        if(!MemberUtils.isMember) {
                            clMember.setVisibility(View.GONE);
                        }
                        tvCouponNo.setText(result.getName());
                        int offer_type = result.getOffer_type();
                        int offer_value = result.getOffer_value();
                        if (offer_type == 1) {
                            tvCouponPrice.setText("-" + offer_value);
                        } else if (offer_type == 2) {
                            DecimalFormat df = new DecimalFormat("0.0");
                            tvCouponPrice.setText(df.format((100 - offer_value) / 10f) + "折");
                        }
                        CouponUtils.getInstance().setCouponInfo(result);

                        computePrice(mTotalMoney, mGoodsCount, mCoupon);
                    }
                });
                break;
            case R.id.iv_cancel_member:
                updateMebmerInfo(null);
//                setMemberVisiable(false);
                break;
            case R.id.iv_cancel_coupon:
                cancelCoupon();
                break;
            case R.id.btn_quick_choose:
                ARouter.getInstance().build(ModulePath.quick).navigation();
                break;
        }
    }

    /**
     * 取消优惠券
     */
    private void cancelCoupon() {
        setHide(clCoupon);
        CouponUtils.getInstance().setCouponInfo(null);

        mCoupon -= mCouponMoney;

        mCouponMoney = 0f;

        refreshPrice(mTotalMoney, mGoodsCount, mCoupon);
    }

    private void setHide(ConstraintLayout constraintLayout) {
//        if (constraintLayout == clMember) {
//            if (clCoupon.getVisibility() == View.VISIBLE) {
//                clMember.setVisibility(View.GONE);
//                return;
//            }
//        } else if (constraintLayout == clCoupon) {
//            if (clMember.getVisibility() == View.VISIBLE) {
//                clCoupon.setVisibility(View.GONE);
//                return;
//            }
//        }
//        clExtraInfo.setVisibility(View.GONE);
        if(constraintLayout == clExtraInfo) {
            clExtraInfo.setVisibility(View.GONE);
            clMember.setVisibility(View.GONE);
            clCoupon.setVisibility(View.GONE);
            return;
        }

        constraintLayout.setVisibility(View.GONE);

        boolean needHide = false;
        if (constraintLayout == clMember) {
            needHide = clCoupon.getVisibility() == View.GONE;
        } else if(constraintLayout == clCoupon) {
            needHide = clMember.getVisibility() == View.GONE;
        }

        if(needHide) {
            clExtraInfo.setVisibility(View.GONE);
        }

    }

    public void setShow(ConstraintLayout constraintLayout) {
        constraintLayout.setVisibility(View.VISIBLE);
        clExtraInfo.setVisibility(View.VISIBLE);
        if(MemberUtils.isMember) {
            clMember.setVisibility(View.VISIBLE);
        }
        if(CouponUtils.getInstance().getCouponInfo() != null) {
            clCoupon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getGoodsSuccess(List<GoodsResponse> result) {
        if(result == null || result.size() == 0) {
            showToast("商品不存在");
            return;
        }

        boolean add_success = mGoodsMultiItemAdapter.addItem(result, mGoodWeight != 0 ? mGoodWeight : 1);
        if(!add_success) {
            showToast("商品库存不足");
            return;
        }
        rvGoods.smoothScrollToPosition(mGoodsMultiItemAdapter.getData().size() - 1);

        if (mUserGoodsScreen != null) {
            //没有显示时 显示用户商品列表副屏页面
            if (!mUserGoodsScreen.isShowing())
                mUserGoodsScreen.show();

            //刷新用户商品列表页面数据
            mUserGoodsScreen.addItem(result, mGoodWeight != 0 ? mGoodWeight : 1);
            mUserGoodsScreen.toPosition();
        }

        mGoodWeight = 0;

//        ActivitiesUtils.getInstance().promotion(mData);
    }

    public int getAdapterSize() {
        return mGoodsMultiItemAdapter.getItemCount();
    }

    @Override
    public void getGoodsFailed(Map<String, Object> map) {
//        if (HttpExceptionEngine.isBussinessError(map)) {
        int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);
        String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
//            if (20001 == err_code) {
        showToast(err_msg);
//            } else {
//                showToast(err_msg);
//            }
//        }
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
            mSearchResultWindow.setElevation(getResources().getDimensionPixelSize(R.dimen.x20));
            mSearchResultWindow.setWidth(clSearch.getSearchResultWidth());
            mSearchResultWindow.setHeight(-2);
            mSearchResultWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_search_result_frame));

            mSearchResultWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                //在dismiss中恢复透明度
                public void onDismiss() {
//                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    getActivity().getWindow().setAttributes(lp);
                }
            });

            mSearchResultWindow.setOnSearchResultClickListener(new SearchResultWindow.OnSearchResultClickListener() {
                @Override
                public void onSearchResultClicked(List<GoodsResponse> list) {
                    //添加
                    boolean add_success = mGoodsMultiItemAdapter.addItem(list, 1);
                    if(!add_success) {
                        showToast("商品库存不足");
                        clSearch.clearText();
                        mSearchResultWindow.dismiss();
                        return;
                    }
                    rvGoods.smoothScrollToPosition(mGoodsMultiItemAdapter.getData().size() - 1);
                    //刷新副屏
                    if (mUserGoodsScreen != null) {
                        mUserGoodsScreen.addItem(list, 1);
                        mUserGoodsScreen.toPosition();
                    }

                    clSearch.clearText();
                    mSearchResultWindow.dismiss();
                }
            });
        }
        mSearchResultWindow.setData(result);
        if (!mSearchResultWindow.isShowing()) {
//            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//            lp.alpha = 0.4f;
//            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            getActivity().getWindow().setAttributes(lp);
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
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    public void goodsActivitySuccess(GoodsActivityResponse result) {
        Log.i(TAG, "goodsActivitySuccess: 商品促销成功");

        if(start)
            ActivitiesUtils.getInstance().parseGoods(result);

    }

    @Override
    public void goodsActivityFailed(Map<String, Object> map) {
        Log.i(TAG, "goodsActivityFailed: 商品促销失败");
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    public void shopActivitySuccess(ShopActivityResponse result) {
        Log.i(TAG, "shopActivitySuccess: 店铺促销成功");

        if(start)
            ActivitiesUtils.getInstance().parseShop(result);

    }

    @Override
    public void shopActivityFailed(Map<String, Object> map) {
        Log.i(TAG, "shopActivityFailed: 店铺促销失败");
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    public void getMemberSuccess(MemberInfo memberInfo, String barcode, String phone) {
        MemberUtils.isMember = true;
        if (membersDialog != null && membersDialog.isShow()) {
            List<MemberInfo> infos = new ArrayList<>();
            infos.add(memberInfo);
            membersDialog.setNewData(infos);
        } else {
            MemberUtils.memberInfo = memberInfo;
            updateMebmerInfo(memberInfo);
        }

    }

    @Override
    public void getMemberFailed(Map<String, Object> map, String barcode, String phone) {
        if (!TextUtils.isEmpty(barcode)) {
            showScanCodeDialog();
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

    // 选择挂单添加item
    public void addData(EntryOrders entryOrders) {
//        tvTotalMoney.setText(entryOrders.getEntry_orders_total_price());
//        tvGoodsCount.setText(entryOrders.getEntry_orders_total_number());
//        tvCoupon.setText("0.00");

        computePrice(Float.valueOf(entryOrders.getEntry_orders_total_price())
                , (Integer.valueOf(entryOrders.getEntry_orders_total_number()))
                , 0);
        mGoodsMultiItemAdapter.setOrdersData(entryOrders.getGoodsEntityList());

        if (mUserGoodsScreen != null) {
            mUserGoodsScreen.setOrdersData(entryOrders.getGoodsEntityList());
        }
    }

    // 退款或收银 清除界面
    public void clearInfo() {
        if (mGoodsMultiItemAdapter != null)
            mGoodsMultiItemAdapter.clear();
        if (mUserGoodsScreen != null)
            mUserGoodsScreen.clear();
        //隐藏 清空会员及 优惠券
        setHide(clExtraInfo);
        CouponUtils.getInstance().setCouponInfo(null);
        updateMebmerInfo(null);
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
        scanCodeDialog.setStatus(ScanCodeDialog.STATUS_MEMBER_NULL);
    }

    public void updateMebmerInfo(MemberInfo info) {

        if (info == null) {//重置清空会员
            MemberUtils.isMember = false;
            MemberUtils.reset();
            setHide(clMember);
            setMemberVisiable(false);
        } else {
            MemberUtils.isMember = true;

            setShow(clMember);
            tvMember.setText(info.getNick_name());
            tvBalance.setText("￥" + df.format(info.getWallet()));
            tvIntegral.setText(info.getIntegral() + "");

            setMemberVisiable(true);
        }
        mGoodsMultiItemAdapter.setMemberData();

        computePrice(mTotalMoney, mGoodsCount, 0);

    }

    /**
     * 设置会员价一列是否显示
     * @param visiable
     */
    public void setMemberVisiable(boolean visiable) {
        tvMemberText.setVisibility(visiable ? View.VISIBLE : View.GONE);
        if (mUserGoodsScreen != null) {
            mUserGoodsScreen.setMemberVisiable(visiable);
        }
    }

    public void addChooseInfo(List<GoodsResponse> goodsResponses) {
        for (GoodsResponse info : goodsResponses) {
            List<GoodsResponse> infos = new ArrayList<>();
            infos.add(info);
            boolean add_success = mGoodsMultiItemAdapter.addItem(infos, 1);
            if(!add_success) {
                showToast("商品库存不足");
                return;
            }
            rvGoods.smoothScrollToPosition(mGoodsMultiItemAdapter.getData().size() - 1);

            if (mUserGoodsScreen != null) {
                //没有显示时 显示用户商品列表副屏页面
                if (!mUserGoodsScreen.isShowing())
                    mUserGoodsScreen.show();

                //刷新用户商品列表页面数据
                mUserGoodsScreen.addItem(infos, 1);
                mUserGoodsScreen.toPosition();
            }
        }

        computePrice(mTotalMoney, mGoodsCount, 0);
    }
}
