package com.easygo.cashier.adapter;

import android.support.v4.util.ArrayMap;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsNum;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.widget.CountTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GoodsAdapter extends BaseQuickAdapter<GoodsNum<GoodsResponse>, BaseViewHolder> {

    public GoodsAdapter() {
        super(R.layout.item_goods);
    }

    private ArrayMap<String, GoodsNum<GoodsResponse>> data;
    private ArrayList<String> barcodeData = new ArrayList<>();

    public void addItem(String code, GoodsResponse t) {
        if (data == null) {
            data = new ArrayMap<>();
            barcodeData = new ArrayList<>();
        }
        if (!data.containsKey(code)) {
            GoodsNum<GoodsResponse> goodsNum = new GoodsNum<>();
            goodsNum.setData(t);
            goodsNum.setCount(1);
            barcodeData.add(code);
            data.put(code, goodsNum);
            mData.add(goodsNum);
            notifyItemInserted(barcodeData.size() - 1);
        } else {
            data.get(code).setCount((data.get(code).getCount()) + 1);
            notifyItemChanged(barcodeData.indexOf(code));
        }
        refreshPrice();
    }

    /**
     * 刷新价格
     */
    private void refreshPrice() {
        if (mListener != null) {
            double price = 0;
            int count = 0;
            double coupon = 0;
            for (GoodsNum<GoodsResponse> good : data.values()) {
                count += good.getCount();
                coupon += Double.valueOf(good.getData().getDiscount_price());
                price += Double.parseDouble(good.getData().getPrice()) * good.getCount();
            }
            mListener.onPriceChange((float) price, count, (float) coupon);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsNum<GoodsResponse> item) {
        int good_count = item.getCount();
        final GoodsResponse good = item.getData();

        final String barcode = good.getBarcode();
        float subtotal = Float.valueOf(good.getPrice()) * good_count;
        DecimalFormat df = new DecimalFormat("#0.00");

        helper.setText(R.id.tv_barcode, barcode)
                .setText(R.id.tv_goods_name, good.getG_sku_name())
                .setText(R.id.tv_price, String.valueOf(good.getPrice()))
                .setText(R.id.tv_coupon, String.valueOf(good.getDiscount_price()))
                .setText(R.id.tv_subtotal, String.valueOf(df.format(subtotal)));

        CountTextView count = ((CountTextView) helper.getView(R.id.count_view));
        count.setCount(String.valueOf(good_count));
        count.setOnCountListener(new CountTextView.OnCountListener() {
            @Override
            public void onCountChanged(int count) {
                if(count == 0) {
                    //清除当前商品
                    barcodeData.remove(barcode);
                    data.remove(barcode);
                    mData.remove(item);
                    notifyItemRemoved(helper.getAdapterPosition());
                } else {
                    item.setCount(count);
//                    double subtotal_new = Double.valueOf(good.getPrice()) * count;
//                    DecimalFormat df = new DecimalFormat("0.00");
//                    helper.setText(R.id.tv_subtotal, "" + String.valueOf(df.format(subtotal_new)));

                    notifyItemChanged(helper.getAdapterPosition());
                }
                //刷新价格
                refreshPrice();
            }
        });
    }

    public void clear() {
        if(getItemCount() <= 0) {
            //不需要清空
            return;
        }
        int count = barcodeData.size();
        data.clear();
        barcodeData.clear();
        mData.clear();
        mListener.onPriceChange(0, 0, 0);
        notifyItemRangeRemoved(0, count);
    }

    private OnPriceListener mListener;
    public void setOnPriceListener(OnPriceListener listener) {
        this.mListener = listener;
    }

    public interface OnPriceListener {
        void onPriceChange(float price, int count, float coupon);
    }

}
