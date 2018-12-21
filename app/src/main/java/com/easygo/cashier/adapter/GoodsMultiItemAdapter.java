package com.easygo.cashier.adapter;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.BarcodeUtils;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.widget.CountTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品扫描结果适配器
 * 1、普通商品
 * 2、重量商品
 * 3、纯加工商品（通过搜索获得）
 * 4、加工商品
 * 5、无码商品
 */
public class GoodsMultiItemAdapter extends BaseMultiItemQuickAdapter<GoodsEntity<GoodsResponse>, BaseViewHolder> {


    public GoodsMultiItemAdapter() {
        super(null);

        addItemType(GoodsEntity.TYPE_GOODS, R.layout.item_goods);
        addItemType(GoodsEntity.TYPE_WEIGHT, R.layout.item_goods_weight);
        addItemType(GoodsEntity.TYPE_ONLY_PROCESSING, R.layout.item_goods);
        addItemType(GoodsEntity.TYPE_PROCESSING, R.layout.item_goods_processing);
        addItemType(GoodsEntity.TYPE_NO_CODE, R.layout.item_goods);
    }

    private ArrayMap<String, GoodsEntity<GoodsResponse>> data;
    private ArrayList<String> barcodeData = new ArrayList<>();


    //普通商品
    public void addItem(GoodsResponse t) {
        String code = t.getBarcode();
        if (data == null) {
            data = new ArrayMap<>();
            barcodeData = new ArrayList<>();
        }
        if (!data.containsKey(code)) {
            GoodsEntity<GoodsResponse> goodsNum = new GoodsEntity<>(GoodsEntity.TYPE_GOODS);
            goodsNum.setData(t);
            goodsNum.setCount(1);
            barcodeData.add(code);
            data.put(code, goodsNum);
            mData.add(goodsNum);
            notifyItemInserted(mData.size() - 1);
        } else {
            data.get(code).setCount((data.get(code).getCount()) + 1);
            notifyItemChanged(barcodeData.indexOf(code));
        }
        refreshPrice();
    }
    //重量商品
    public void addWeightItem(GoodsResponse t, int weight) {
        String code = t.getBarcode();
        if (data == null) {
            data = new ArrayMap<>();
            barcodeData = new ArrayList<>();
        }
        if (!data.containsKey(code)) {
            GoodsEntity<GoodsResponse> goodsNum = new GoodsEntity<>(GoodsEntity.TYPE_WEIGHT);
            goodsNum.setData(t);
            goodsNum.setCount(weight);
            barcodeData.add(code);
            data.put(code, goodsNum);
            mData.add(goodsNum);
            notifyItemInserted(mData.size() - 1);
        } else {
            data.get(code).setCount((data.get(code).getCount()) + weight);
            notifyItemChanged(barcodeData.indexOf(code));
        }
        refreshPrice();
    }
    //纯加工商品
    public void addOnlyPrcessingItem(GoodsResponse t, int weight) {
        String code = t.getBarcode();
        if (data == null) {
            data = new ArrayMap<>();
            barcodeData = new ArrayList<>();
        }
        GoodsEntity<GoodsResponse> goodsNum = new GoodsEntity<>(GoodsEntity.TYPE_ONLY_PROCESSING);
        goodsNum.setData(t);
        goodsNum.setCount(weight);
        barcodeData.add(code);
        mData.add(goodsNum);
        notifyItemInserted(mData.size() - 1);
        refreshPrice();
    }
    //可选择的加工商品
    public void addPrcessingItem(List<GoodsResponse> list, int weight) {
        if (data == null) {
            data = new ArrayMap<>();
            barcodeData = new ArrayList<>();
        }
        GoodsEntity<GoodsResponse> goodsNum = new GoodsEntity<>(GoodsEntity.TYPE_PROCESSING);
        goodsNum.setCount(weight);

        int size = list.size();
        List<GoodsResponse> processing_list = new ArrayList<>(size-1);
        String code = "";
        for (int i = 0; i < size; i++) {
            GoodsResponse goodsResponse = list.get(i);

            if(goodsResponse.getParent_id() == 0) {//主商品
                code = goodsResponse.getBarcode();
                goodsNum.setData(goodsResponse);
            } else {//附带加工方式 商品
                goodsResponse.setCount(1);
                processing_list.add(goodsResponse);
            }
        }
        goodsNum.setProcessing_list(processing_list);

        barcodeData.add(code);
        mData.add(goodsNum);
        notifyItemInserted(mData.size() - 1);
        refreshPrice();
    }

    /**
     * 添加商品
     */
    public void addItem(List<GoodsResponse> t, int weight) {

        //普通商品   可调节数量                                         （同一条码 合并）
        //重量商品   不可调节数量   barcode 以22开头   重量需传进来        （同一条码 合并）

        //纯加工商品   不可调节数量   单个数据                             （同一条码 不合并）
        //可选择的加工商品   不可调节数量   返回多个数据                    （同一条码 不合并）


        int size = t.size();
        if(size == 1) {//商品 （重量、普通、纯加工）
            GoodsResponse goodsResponse = t.get(0);
            String barcode = goodsResponse.getBarcode();

            if(goodsResponse.getParent_id() == 0) {//主商品
                if(BarcodeUtils.isWeightCode(barcode)) {
                    //重量商品
                    addWeightItem(goodsResponse, weight);
                } else {
                    //普通商品
                    addItem(goodsResponse);
                }
            }
           else if(goodsResponse.getParent_id() != 0){
                //纯加工商品
                addOnlyPrcessingItem(goodsResponse, weight);
            }

        } else {//加工商品
            addPrcessingItem(t, weight);
        }
    }

    //无码商品
    public void addNoCodeItem(float price) {
        String code = String.valueOf(price);

        GoodsResponse goodsResponse = new GoodsResponse();
        goodsResponse.setCount(1);
        goodsResponse.setBarcode("");
        goodsResponse.setDiscount_price("0.00");
        goodsResponse.setG_sku_id(0);
        goodsResponse.setG_sku_name("无码商品");
        goodsResponse.setPrice(code);

        if (data == null) {
            data = new ArrayMap<>();
            barcodeData = new ArrayList<>();
        }
        //以单价为key
        if (!data.containsKey(code)) {
            GoodsEntity<GoodsResponse> goodsNum = new GoodsEntity<>(GoodsEntity.TYPE_NO_CODE);
            goodsNum.setData(goodsResponse);
            goodsNum.setCount(1);
            barcodeData.add(code);
            data.put(code, goodsNum);
            mData.add(goodsNum);
            notifyItemInserted(mData.size() - 1);
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
            int size = mData.size();
            for (int i = 0; i < size; i++) {
                GoodsEntity<GoodsResponse> good = mData.get(i);

                switch (good.getItemType()) {
                    case GoodsEntity.TYPE_GOODS:
                    case GoodsEntity.TYPE_NO_CODE:
                        count += good.getCount();
                        coupon += Double.valueOf(good.getData().getDiscount_price());
                        price += Double.parseDouble(good.getData().getPrice()) * good.getCount();
                        break;
                    case GoodsEntity.TYPE_WEIGHT:
                        count += 1;
                        coupon += Double.valueOf(good.getData().getDiscount_price());
                        price += Double.parseDouble(good.getData().getPrice()) * good.getCount();
                        break;
                    case GoodsEntity.TYPE_ONLY_PROCESSING:
                        count += 1;
                        coupon += Double.valueOf(good.getData().getDiscount_price());
                        price += Double.parseDouble(good.getData().getPrice()) * good.getCount();
                        break;
                    case GoodsEntity.TYPE_PROCESSING:
                        count += 1;
                        coupon += Double.valueOf(good.getData().getDiscount_price());
                        price += Double.parseDouble(good.getData().getPrice()) * good.getCount();
                        //选择加工时 更新价格
                        GoodsResponse processing = good.getProcessing();
                        if(processing != null) {//此时 选择了加工
                            count += 1;
                            coupon += Double.valueOf(processing.getDiscount_price());
                            price += Double.parseDouble(processing.getProcess_price()) * processing.getCount();
                        }
                        break;
                }
            }
            mListener.onPriceChange((float) price, count, (float) coupon);
        }
    }


    @Override
    protected void convert(final BaseViewHolder helper, final GoodsEntity<GoodsResponse> item) {
        final int good_count = item.getCount();
        final GoodsResponse good = item.getData();

        final String barcode = good.getBarcode();
        final String price = good.getPrice();
        float subtotal = Float.valueOf(price) * good_count;
        DecimalFormat df = new DecimalFormat("#0.00");

        helper.setText(R.id.tv_barcode, barcode)
                .setText(R.id.tv_goods_name, good.getG_sku_name())
                .setText(R.id.tv_price, String.valueOf(price))
                .setText(R.id.tv_coupon, String.valueOf(good.getDiscount_price()))
                .setText(R.id.tv_subtotal, String.valueOf(df.format(subtotal)));


        switch (helper.getItemViewType()) {
            case GoodsEntity.TYPE_GOODS://普通商品
            case GoodsEntity.TYPE_NO_CODE://无码商品
                CountTextView count = ((CountTextView) helper.getView(R.id.count_view));
                count.setCount(String.valueOf(good_count));
                count.setOnCountListener(new CountTextView.OnCountListener() {
                    @Override
                    public void onCountChanged(int count) {
                        if(count == 0) {
                            //清除当前商品
                            if(TextUtils.isEmpty(barcode)) {
                                //无码商品
                                barcodeData.remove(price);
                                data.remove(price);
                            } else {
                                barcodeData.remove(barcode);
                                data.remove(barcode);
                            }
                            mData.remove(item);
                            notifyItemRemoved(helper.getAdapterPosition());
                        } else {
                            item.setCount(count);
                            notifyItemChanged(helper.getAdapterPosition());
                        }
                        //刷新价格
                        refreshPrice();
                    }
                });
                break;
            case GoodsEntity.TYPE_WEIGHT://称重商品
                helper.setText(R.id.tv_count, good_count + "g");
                break;
            case GoodsEntity.TYPE_ONLY_PROCESSING://纯加工方式
                CountTextView view = (CountTextView) helper.getView(R.id.count_view);
                view.setCountChangeEnable(false);
                break;
            case GoodsEntity.TYPE_PROCESSING://加工方式

                final CheckBox cb_processing = (CheckBox) helper.getView(R.id.cb_processing);

                GoodsResponse processing = item.getProcessing();
                if(processing != null) {//选择了加工方式
                    cb_processing.setChecked(true);

                    //刷新数据
                    refreshPrcessingData(helper, processing);
                }

                final boolean is_processing = cb_processing.isChecked();

                //设置加工方式相关控件可见性
                helper.setText(R.id.tv_count, good_count + "g");
                setProcessingLayout(helper, is_processing);


                //是否加工监听
                cb_processing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setProcessingLayout(helper, isChecked);

                        //选择加工时 设置加工商品，否则置空
                        GoodsResponse default_processing = item.getProcessing_list().get(0);
                        item.setProcessing(isChecked? default_processing : null);
                        if(isChecked) {
                            refreshPrcessingData(helper, default_processing);
                        }

                        refreshPrice();
                    }
                });
                //加工方式点击监听
                helper.getView(R.id.cl_processing).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null) {
                            mListener.onProcessingClicked(helper.getAdapterPosition(),
                                    item.getProcessing(), item.getProcessing_list());
                        }
                    }
                });

                break;
        }
    }
    /**设置加工方式相关UI 可见性*/
    private void setProcessingLayout(BaseViewHolder helper, boolean showProcessing) {
        helper.setVisible(R.id.cl_processing, showProcessing)
                .setVisible(R.id.tv_processing_price, showProcessing)
                .setVisible(R.id.tv_processing_coupon, showProcessing)
                .setVisible(R.id.tv_processing_subtotal, showProcessing)
                .setVisible(R.id.tv_processing_count, showProcessing);
    }
    /**刷新加工方式相关UI 数据*/
    private void refreshPrcessingData(BaseViewHolder helper, GoodsResponse processing) {
        DecimalFormat df = new DecimalFormat("#0.00");
        float processing_subtotal = Float.valueOf(processing.getProcess_price()) * processing.getCount();

        helper.setText(R.id.tv_processing_name, processing.getG_sku_name())
                .setText(R.id.tv_processing_price, processing.getProcess_price())
                .setText(R.id.tv_processing_coupon, processing.getDiscount_price())
                .setText(R.id.tv_processing_subtotal, String.valueOf(df.format(processing_subtotal)))
                .setText(R.id.tv_processing_count, String.valueOf(processing.getCount()));
    }

    /**
     * 选择了加工方式, 刷新
     */
    public void chooseProcessing(int position, GoodsResponse current, GoodsResponse choice) {
        if(choice.getProcess_id() != current.getProcess_id()) {//选择了新的加工方式
            GoodsEntity<GoodsResponse> goodsEntity = mData.get(position);
            goodsEntity.setProcessing(choice);
            current = choice;
            notifyItemChanged(position);
        }
    }

    /**移除*/
    public void remove(int position) {
        GoodsEntity<GoodsResponse> goodsEntity = mData.get(position);
        GoodsResponse data = goodsEntity.getData();

        barcodeData.remove(data.getBarcode());
        this.data.remove(data.getBarcode());
        mData.remove(position);

        notifyItemRemoved(position);
        refreshPrice();
    }


    public void clear() {
        if(getItemCount() <= 0) {
            //不需要清空
            return;
        }
        int count = mData.size();
        data.clear();
        barcodeData.clear();
        mData.clear();
        mListener.onPriceChange(0, 0, 0);
        notifyItemRangeRemoved(0, count);
    }

    private OnItemListener mListener;
    public void setOnItemListener(OnItemListener listener) {
        this.mListener = listener;
    }

    public interface OnItemListener {
        void onPriceChange(float price, int count, float coupon);
        void onProcessingClicked(int position, GoodsResponse cur_processing, List<GoodsResponse> processing_list);
    }

}
