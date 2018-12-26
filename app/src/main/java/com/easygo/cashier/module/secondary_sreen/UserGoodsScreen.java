package com.easygo.cashier.module.secondary_sreen;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.adapter.UserGoodsAdapter;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.widget.MyTitleBar;

import java.text.DecimalFormat;
import java.util.List;

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
    @BindView(R.id.tv_product_num)
    TextView tvProductNum;
    @BindView(R.id.tv_product_total_price)
    TextView tvProductTotalPrice;
    @BindView(R.id.tv_product_preferential)
    TextView tvProductPreferential;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
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
        titleBar.setCashierAccount(admin_name);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mUserGoodsAdapter = new UserGoodsAdapter();
        recyclerView.setAdapter(mUserGoodsAdapter);

        //分割线
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        recyclerView.addItemDecoration(verticalDecoration);

    }

    public void addItem(List<GoodsResponse> result, int weight) {
        this.mUserGoodsAdapter.addItem(result, weight);
    }

    public void addNoCodeItem(float price) {
        this.mUserGoodsAdapter.addNoCodeItem(price);
    }

    public void chooseProcessing(int position, GoodsResponse choice) {
        this.mUserGoodsAdapter.chooseProcessing(position, choice);
    }

    public void onCountChanged(int position, int count) {
        if(count == 0) {
            if(position >= 0)
                onItemRemoved(position);
        } else {
            List<GoodsEntity<GoodsResponse>> data = mUserGoodsAdapter.getData();
            GoodsEntity<GoodsResponse> goodsEntity = data.get(position);
            goodsEntity.setCount(count);
            mUserGoodsAdapter.notifyItemChanged(position);
        }
    }

    public void onItemRemoved(int position) {
        mUserGoodsAdapter.remove(position);
    }

    public void refreshPrice(int goods_count, float total_money, float coupon, float real_pay) {
        DecimalFormat df = new DecimalFormat("#0.00");

        tvProductNum.setText(String.valueOf(goods_count));
        tvProductTotalPrice.setText("￥" + df.format(total_money));
        tvProductPreferential.setText("￥" + df.format(coupon));
        tvTotalPrice.setText("￥" + df.format(real_pay));


    }

    public void clear() {
        mUserGoodsAdapter.clear();
    }

    //更新位置到最后
    public void toPosition() {
        recyclerView.smoothScrollToPosition(mUserGoodsAdapter.getItemCount()-1);
    }
}
