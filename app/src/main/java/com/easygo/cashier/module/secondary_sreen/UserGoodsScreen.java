package com.easygo.cashier.module.secondary_sreen;

import android.animation.ObjectAnimator;
import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.easygo.cashier.Configs;
import com.easygo.cashier.Events;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.adapter.UserGoodsAdapter;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.widget.view.ActivitiesView;
import com.easygo.cashier.widget.view.MyTitleBar;

import java.text.DecimalFormat;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 显示给用户的商品列表页面
 */
public class UserGoodsScreen extends Presentation {

    @BindView(R.id.title_bar)
    MyTitleBar titleBar;
    @BindView(R.id.tv_barcode)
    TextView tvBarcode;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.activities_view)
    ActivitiesView activitiesView;
    @BindView(R.id.tv_product_num)
    TextView tvProductNum;
    @BindView(R.id.tv_product_total_price)
    TextView tvProductTotalPrice;
    @BindView(R.id.tv_product_preferential)
    TextView tvProductPreferential;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_text_member_price)
    TextView tvTextMember;
    @BindView(R.id.cl_pay_successful)
    ConstraintLayout clPaySuccessful;
    @BindView(R.id.tv_text_goods_name)
    TextView tvTextGoodsName;
    @BindView(R.id.tv_text_price)
    TextView tvTextPrice;
    @BindView(R.id.tv_text_coupon)
    TextView tvTextCoupon;
    @BindView(R.id.tv_text_subtotal)
    TextView tvTextSubtotal;
    @BindView(R.id.tv_text_goods_count)
    TextView tvTextGoodsCount;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_total_cash)
    TextView tvTotalCash;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;
    private UserGoodsAdapter mUserGoodsAdapter;
    private String admin_name;

    public UserGoodsScreen(Context outerContext, Display display, String admin_name) {
        super(outerContext, display);
        this.admin_name = admin_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_user_goods_screen);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        titleBar.setCashierAccount(String.valueOf(Configs.cashier_id));

        ObjectAnimator.ofFloat(clPaySuccessful, "alpha", 0f)
                .setDuration(0)
                .start();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mUserGoodsAdapter = new UserGoodsAdapter();
        recyclerView.setAdapter(mUserGoodsAdapter);

        mUserGoodsAdapter.setOnItemListener(null);

        //分割线
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        recyclerView.addItemDecoration(verticalDecoration);

        setLanguage();
    }

    public void refreshPrice(int goods_count, float total_money, float coupon, float real_pay) {
        DecimalFormat df = new DecimalFormat("#0.00");

        tvProductNum.setText(String.valueOf(goods_count));
        tvProductTotalPrice.setText("￥" + df.format(total_money));
        tvProductPreferential.setText("￥" + df.format(coupon));
        tvTotalPrice.setText("￥" + df.format(real_pay));

        notifyAdapter();
    }

    public void clear() {
        if (mUserGoodsAdapter != null)
            mUserGoodsAdapter.clear();
    }

    //更新位置到最后
    public void toPosition() {
        if (recyclerView != null && mUserGoodsAdapter != null)
            recyclerView.smoothScrollToPosition(mUserGoodsAdapter.getData().size() - 1);
    }

    public void notifyAdapter() {
        mUserGoodsAdapter.notifyDataSetChanged();
    }

    public void setMemberVisiable(boolean visiable) {
        tvTextMember.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    public void setCoupon(String coupon) {
        tvProductPreferential.setText("￥" + coupon);
    }

    public void showCurrentActivities(List<String> data) {
        activitiesView.setVisibility(data != null && data.size() > 0 ? View.VISIBLE : View.GONE);
        activitiesView.setData(data);
    }

    public void refreshGoodsData(List<GoodsEntity<GoodsResponse>> data) {
        mUserGoodsAdapter.refreshGoodsData(data);
        toPosition();
    }

    public void showPaySuccessful() {
        ObjectAnimator.ofFloat(clPaySuccessful, "alpha", 0f, 1f, 1f, 1f, 0f)
                .setDuration(2500)
                .start();
    }

    private void setLanguage() {
        String language = Events.LANGUAGE;
        tvBarcode.setText(language.equals("zh-tw") ? "條碼" : language.equals("en") ? "Barcode" : "条码");
        tvTextGoodsName.setText(language.equals("zh-tw") ? "品名" : language.equals("en") ? "Product Name" : "品名");
        tvTextPrice.setText(language.equals("zh-tw") ? "單價" : language.equals("en") ? "Unit Price" : "单价");
        tvTextMember.setText(language.equals("zh-tw") ? "會員價" : language.equals("en") ? "Member price" : "会员价");
        tvTextCoupon.setText(language.equals("zh-tw") ? "優惠" : language.equals("en") ? "Offer" : "优惠");
        tvTextSubtotal.setText(language.equals("zh-tw") ? "小計" : language.equals("en") ? "Subtotal" : "小计");
        tvTextGoodsCount.setText(language.equals("zh-tw") ? "數量/重量" : language.equals("en") ? "Quantity" : "数量/重量");
        tvDescription.setText(language.equals("zh-tw") ? "支付成功" : language.equals("en") ? "Pay Success" : "支付成功");
        tvNum.setText(language.equals("zh-tw") ? "件數" : language.equals("en") ? "Number" : "件数");
        tvTotalCash.setText(language.equals("zh-tw") ? "總額" : language.equals("en") ? "Lump Sum" : "总额");
        tvCoupon.setText(language.equals("zh-tw") ? "優惠" : language.equals("en") ? "Offer" : "优惠");
        tvTotalNum.setText(language.equals("zh-tw") ? "合計：" : language.equals("en") ? "Total：" : "合计：");
        switch (language) {
            case "zh-tw":
                activitiesView.language("取消臨時折扣", "正在參與活動：");
                break;
            case "en":
                activitiesView.language("Cancel temporary discount", "Participating in the event：");
                break;
        }
    }
}
