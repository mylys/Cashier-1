package com.easygo.cashier.adapter;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.BarcodeUtils;
import com.easygo.cashier.Configs;
import com.easygo.cashier.MemberUtils;
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
    DecimalFormat df;

    public GoodsMultiItemAdapter() {
        super(null);
        df = new DecimalFormat("0.00");
        addItemType(GoodsEntity.TYPE_GOODS, R.layout.item_goods);
        addItemType(GoodsEntity.TYPE_WEIGHT, R.layout.item_goods_weight);
        addItemType(GoodsEntity.TYPE_ONLY_PROCESSING, R.layout.item_goods);
        addItemType(GoodsEntity.TYPE_PROCESSING, R.layout.item_goods_processing);
        addItemType(GoodsEntity.TYPE_NO_CODE, R.layout.item_goods);
    }

    protected ArrayMap<String, GoodsEntity<GoodsResponse>> data;
    protected ArrayList<String> barcodeData = new ArrayList<>();

    /**
     * 确保数据集合不为空
     */
    private void ensureNotNull() {
        if (data == null) {
            data = new ArrayMap<>();
            barcodeData = new ArrayList<>();
        }
    }

    //普通商品
    public void addItem(GoodsResponse t) {
        String code = t.getBarcode();
        ensureNotNull();
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
        ensureNotNull();
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
        ensureNotNull();
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
        ensureNotNull();
        GoodsEntity<GoodsResponse> goodsNum = new GoodsEntity<>(GoodsEntity.TYPE_PROCESSING);
        goodsNum.setCount(weight);

        int size = list.size();
        List<GoodsResponse> processing_list = new ArrayList<>(size - 1);
        String code = "";
        for (int i = 0; i < size; i++) {
            GoodsResponse goodsResponse = list.get(i);

            if (goodsResponse.getParent_id() == 0) {//主商品
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
        for (GoodsResponse info : t) {
            if (Double.valueOf(info.getMembership_price()) > 0) {
                info.setMemberPrice(true);
            } else {
                info.setMemberPrice(false);
            }
        }
        //普通商品   可调节数量                                         （同一条码 合并）
        //重量商品   不可调节数量   barcode 以22开头   重量需传进来        （同一条码 合并）

        //纯加工商品   不可调节数量   单个数据                             （同一条码 不合并）
        //可选择的加工商品   不可调节数量   返回多个数据                    （同一条码 不合并）

        int size = t.size();
        //为商品添加时间戳，唯一识别商品以方便后台增减库存
        long timeStamp = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            t.get(i).setIdentity(String.valueOf(timeStamp));
        }
        if (size == 1) {//商品 （重量、普通、纯加工）
            GoodsResponse goodsResponse = t.get(0);
            String barcode = goodsResponse.getBarcode();

            if (goodsResponse.getParent_id() == 0) {//主商品
                if (BarcodeUtils.isWeightCode(barcode)) {
                    //重量商品
                    goodsResponse.setType(GoodsResponse.type_weight);
                    addWeightItem(goodsResponse, weight);
                } else {
                    //普通商品
                    goodsResponse.setType(GoodsResponse.type_normal);
                    addItem(goodsResponse);
                }
            } else if (goodsResponse.getParent_id() != 0) {
                //纯加工商品
//                addOnlyPrcessingItem(goodsResponse, weight);
                goodsResponse.setType(GoodsResponse.type_processing);
                addOnlyPrcessingItem(goodsResponse, 1);
            }

        } else {//加工商品
            GoodsResponse goodsResponse;
            for (int i = 0; i < size; i++) {
                goodsResponse = t.get(i);
                if (goodsResponse.getParent_id() == 0) {//主商品
                    goodsResponse.setType(GoodsResponse.type_weight);
                } else {//加工商品
                    goodsResponse.setType(GoodsResponse.type_processing);
                }
            }
            addPrcessingItem(t, weight);
        }
    }

    //无码商品
    public void addNoCodeItem(float price) {
        String code = String.valueOf(price);

        //为商品添加时间戳，唯一识别商品以方便后台增减库存
        long timeStamp = System.currentTimeMillis();

        GoodsResponse goodsResponse = new GoodsResponse();
        goodsResponse.setCount(1);
        goodsResponse.setBarcode("");
        goodsResponse.setDiscount_price("0.00");
        goodsResponse.setG_sku_id(0);
        goodsResponse.setIdentity(String.valueOf(timeStamp));
        goodsResponse.setType(GoodsResponse.type_no_code);
        goodsResponse.setG_sku_name("无码商品");
        goodsResponse.setPrice(code);

        ensureNotNull();
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
    protected void refreshPrice() {
        if (mListener != null) {
            double price = 0;
            int count = 0;
            double coupon = 0;
            int size = mData.size();
            boolean member;
            double p;
            for (int i = 0; i < size; i++) {
                GoodsEntity<GoodsResponse> good = mData.get(i);

                member = good.getData().isMemberPrice();

                switch (good.getItemType()) {
                    case GoodsEntity.TYPE_GOODS:
                    case GoodsEntity.TYPE_NO_CODE:
                        count += good.getCount();
                        coupon += Double.valueOf(good.getData().getDiscount_price());
                        p = MemberUtils.isMember && member ?
                                Double.valueOf(good.getData().getMembership_price()) : Double.valueOf(good.getData().getPrice());
                        price += p * good.getCount();
                        break;
                    case GoodsEntity.TYPE_WEIGHT:
                        count += 1;
                        coupon += Double.valueOf(good.getData().getDiscount_price());
                        p = MemberUtils.isMember && member ?
                                Double.valueOf(good.getData().getMembership_price()) : Double.valueOf(good.getData().getPrice());
                        price += p * good.getCount();
                        break;
                    case GoodsEntity.TYPE_ONLY_PROCESSING:
                        count += 1;
                        coupon += Double.valueOf(good.getData().getDiscount_price());
//                        price += Double.parseDouble(good.getData().getProcess_price()) * good.getCount();
                        p = MemberUtils.isMember && member ?
                                Double.valueOf(good.getData().getMembership_price()) : Double.valueOf(good.getData().getProcess_price());
                        price += p * good.getCount();
                        break;
                    case GoodsEntity.TYPE_PROCESSING:
                        count += 1;
                        coupon += Double.valueOf(good.getData().getDiscount_price());
                        p = MemberUtils.isMember && member ?
                                Double.valueOf(good.getData().getMembership_price()) : Double.valueOf(good.getData().getPrice());
                        price += p * good.getCount();
                        //选择加工时 更新价格
                        GoodsResponse processing = good.getProcessing();
                        if (processing != null) {//此时 选择了加工
                            count += 1;
                            coupon += Double.valueOf(processing.getDiscount_price());
                            String process_price = good.getData().getProcess_price();
                            p = MemberUtils.isMember && member ?
                                    Double.valueOf(good.getData().getMembership_price()) : Double.valueOf(process_price!=null?process_price: "0.00");
                            price += p * good.getCount();
                        }
                        break;
                }
            }
            mListener.onPriceChange((float) price, count, (float) coupon);
        }
        notifyDataSetChanged();
    }


    @Override
    protected void convert(final BaseViewHolder helper, final GoodsEntity<GoodsResponse> item) {
        final int good_count = item.getCount();
        final GoodsResponse good = item.getData();

        final String barcode = good.getBarcode();
        final String price = good.getPrice();
        float subtotal = Float.valueOf(price) * good_count - Float.valueOf(good.getDiscount_price());

//        good.setDiscount_price("0.00");
        helper.getView(R.id.tv_member_price).setVisibility(MemberUtils.isMember ? View.VISIBLE : View.GONE);
        helper.setText(R.id.tv_barcode, barcode)
                .setText(R.id.tv_goods_name, good.getG_sku_name())
                .setText(R.id.tv_price, String.valueOf(price))
                .setText(R.id.tv_subtotal, String.valueOf(df.format(subtotal)))
                .setText(R.id.tv_coupon, good.getDiscount_price())
                .setText(R.id.tv_member_price, "0.00");

        if (item.getPromotion() == null && MemberUtils.isMember) {
            if (good.isMemberPrice()) {                                              //普通会员价计算
                float coupon = (Float.parseFloat(price) - Float.parseFloat(good.getMembership_price())) * good_count;
                subtotal = Float.parseFloat(good.getMembership_price()) * good_count;
                good.setDiscount_price(df.format(coupon));
                helper.setText(R.id.tv_member_price, good.getMembership_price());
            } else if (!good.isMemberPrice() && MemberUtils.isMemberDay) {           //会员日计算
                if (getFullTotalPrice() >= MemberUtils.full){
                    float coupon = MemberUtils.getCoupon(getFullTotalPrice(), Float.parseFloat(price), good_count);
                    subtotal = (Float.parseFloat(price) * good_count) - coupon;
                    good.setDiscount_price(df.format(coupon));
                }
            } else if (!MemberUtils.isMemberDay && MemberUtils.isMemberDiscount) {   //会员固定折扣计算
                float coupon = (float) (Float.parseFloat(price) - (MemberUtils.discount * Float.parseFloat(price))) * good_count;
                subtotal = (Float.parseFloat(price) * good_count) - coupon;
                good.setDiscount_price(df.format(coupon));
            }

            helper.setText(R.id.tv_subtotal, df.format(subtotal))
                    .setText(R.id.tv_coupon, good.getDiscount_price());
            mListener.onPriceChange(Float.parseFloat(getTotalPrice()), getTotalCount(), Float.parseFloat(getTotalCoupon()));
        }

        switch (helper.getItemViewType()) {
            case GoodsEntity.TYPE_GOODS://普通商品
            case GoodsEntity.TYPE_NO_CODE://无码商品
                final CountTextView countTextView = ((CountTextView) helper.getView(R.id.count_view));
                countTextView.setCount(String.valueOf(good_count));
                countTextView.setOnCountListener(new CountTextView.OnCountListener() {
                    @Override
                    public void onCountChanged(int count) {
                        if (mListener != null) {
                            mListener.onCountChanged(helper.getAdapterPosition(), count);
                        }

                        if (count == 0) {
                            //清除当前商品
                            if (TextUtils.isEmpty(barcode)) {
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
                            if (good.getIs_inventory_limit() == 1 && helper.getItemViewType() == GoodsEntity.TYPE_GOODS
                                    && count > good.getOn_sale_count()) {
                                //数量大于在售数量了
                                count--;
                                countTextView.setCount(count + "");

                                if (mListener != null) {
                                    mListener.onSaleCountNotEnough();
                                }

                            }

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

                String process_price = good.getProcess_price();
                subtotal = Float.valueOf(process_price);
                if (MemberUtils.isMember) {
                    if (good.isMemberPrice()) {
                        float coupon = (Float.parseFloat(process_price) - Float.parseFloat(good.getMembership_price()));
                        subtotal = Float.valueOf(good.getMembership_price());
                        good.setDiscount_price(df.format(coupon));
                    }else if (!good.isMemberPrice() && MemberUtils.isMemberDay){
                        if (getFullTotalPrice() >= MemberUtils.full){
                            float coupon = MemberUtils.getCoupon(getFullTotalPrice(), Float.parseFloat(process_price), good_count);
                            subtotal = (Float.parseFloat(process_price)) - coupon;
                            good.setDiscount_price(df.format(coupon));
                        }
                    }else if (!MemberUtils.isMemberDay && MemberUtils.isMemberDiscount){
                        float coupon = (float) (Float.parseFloat(process_price) - (MemberUtils.discount * Float.parseFloat(process_price))) * good_count;
                        subtotal = (Float.parseFloat(process_price) * good_count) - coupon;
                        good.setDiscount_price(df.format(coupon));
                    }
                    helper.setText(R.id.tv_coupon, good.getDiscount_price());
                    mListener.onPriceChange(Float.parseFloat(getTotalPrice()), getTotalCount(), Float.parseFloat(getTotalCoupon()));
                }

                helper.setText(R.id.tv_price, String.valueOf(process_price))
                        .setText(R.id.tv_subtotal, df.format(subtotal));
                break;
            case GoodsEntity.TYPE_PROCESSING://加工方式

                final CheckBox cb_processing = (CheckBox) helper.getView(R.id.cb_processing);

                GoodsResponse processing = item.getProcessing();
                if (processing != null) {//选择了加工方式
                    cb_processing.setChecked(true);

                    //刷新数据
                    refreshPrcessingData(helper, processing);
                } else {
                    cb_processing.setChecked(false);
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

                        GoodsEntity<GoodsResponse> goodsEntity = mData.get(helper.getAdapterPosition());

                        //选择加工时 设置加工商品，否则置空
                        GoodsResponse default_processing = goodsEntity.getProcessing_list().get(0);
                        goodsEntity.setProcessing(isChecked ? default_processing : null);
                        if (isChecked) {
                            default_processing.setIdentity(String.valueOf(System.currentTimeMillis()));
                            refreshPrcessingData(helper, default_processing);
                        }

                        refreshPrice();

                        if (mListener != null) {
                            mListener.onProcessingCheckedChanged(isChecked, helper.getAdapterPosition(),
                                    goodsEntity.getProcessing());
                        }

                    }
                });
                //加工方式点击监听
                helper.getView(R.id.cl_processing).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onProcessingClicked(helper.getAdapterPosition(),
                                    item.getProcessing(), item.getProcessing_list());
                        }
                    }
                });

                break;
        }


        switch (helper.getItemViewType()) {
            case GoodsEntity.TYPE_WEIGHT://普通商品
            case GoodsEntity.TYPE_PROCESSING://加工商品
            case GoodsEntity.TYPE_ONLY_PROCESSING://加工商品
                View view = helper.getView(R.id.cl_remove);
                view.setVisibility(View.VISIBLE);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemRemoved(helper.getAdapterPosition());
                        }

                        remove(helper.getAdapterPosition());
                        refreshPrice();
                    }
                });

                break;
        }

    }

    /**
     * 设置加工方式相关UI 可见性
     */
    private void setProcessingLayout(BaseViewHolder helper, boolean showProcessing) {
        helper.setVisible(R.id.cl_processing, showProcessing)
                .setVisible(R.id.tv_processing_price, showProcessing)
                .setVisible(R.id.tv_processing_coupon, showProcessing)
                .setVisible(R.id.tv_processing_subtotal, showProcessing)
                .setVisible(R.id.tv_processing_count, showProcessing);
    }

    /**
     * 刷新加工方式相关UI 数据
     */
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
        if (choice.getProcess_id() != current.getProcess_id()) {//选择了新的加工方式
            GoodsEntity<GoodsResponse> goodsEntity = mData.get(position);
            goodsEntity.setProcessing(choice);

            //设置唯一标识 跟主商品区分开
            choice.setIdentity(String.valueOf(System.currentTimeMillis()));

            mData.set(position, goodsEntity);

            current = choice;
            notifyItemChanged(position);

            //刷新价格
            refreshPrice();
        }
    }

    /**
     * 移除
     */
    public void remove(int position) {
        GoodsEntity<GoodsResponse> goodsEntity = mData.get(position);
        GoodsResponse data = goodsEntity.getData();

        mData.remove(position);
        String barcode = data.getBarcode();
        int size = mData.size();
        boolean needDeleteBarcode = true;
        for (int i = 0; i < size; i++) {
            if (barcode.equals(mData.get(i).getData().getBarcode())) {
                needDeleteBarcode = false;
                break;
            }
        }

        if (needDeleteBarcode) {
            barcodeData.remove(barcode);
            this.data.remove(barcode);
        }

        notifyItemRemoved(position);
        refreshPrice();
    }


    public void clear() {
        if (getItemCount() <= 0) {
            //不需要清空
            return;
        }
        int count = mData.size();
        data.clear();
        barcodeData.clear();
        mData.clear();
        if (mListener != null)
            mListener.onPriceChange(0, 0, 0);
        notifyItemRangeRemoved(0, count);
    }

    protected OnItemListener mListener;

    public void setOnItemListener(OnItemListener listener) {
        this.mListener = listener;
    }

    public interface OnItemListener {
        void onPriceChange(float price, int count, float coupon);

        void onProcessingClicked(int position, GoodsResponse cur_processing, List<GoodsResponse> processing_list);

        void onSaleCountNotEnough();


        //用于 用户副屏

        /**
         * 点击加工选项时
         */
        void onProcessingCheckedChanged(boolean isChecked, int position, GoodsResponse processing);

        /**
         * 数量改变时
         */
        void onCountChanged(int position, int count);

        /**
         * item被移除时
         */
        void onItemRemoved(int position);
    }

    public void setOrdersData(List<GoodsEntity<GoodsResponse>> list) {
        ensureNotNull();
        for (GoodsEntity<GoodsResponse> entity : list) {
            data.put(entity.getData().getBarcode(), entity);
            barcodeData.add(entity.getData().getBarcode());
        }
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public void setMemberData() {
        if (data != null) {
            notifyDataSetChanged();
        }
    }

    public String getTotalCoupon() {
        float coupon = 0;
        for (GoodsEntity<GoodsResponse> entity : mData) {
            GoodsResponse good = entity.getData();
            coupon += Float.parseFloat(good.getDiscount_price());
        }
        return df.format(coupon);
    }

    public String getTotalPrice() {
        float total_price = 0;
        for (GoodsEntity<GoodsResponse> entity : mData) {
            GoodsResponse good = entity.getData();
            if (entity.getItemType() == GoodsEntity.TYPE_ONLY_PROCESSING) {
                total_price += Float.parseFloat(good.getProcess_price()) * entity.getCount();
            } else {
                total_price += Float.parseFloat(good.getPrice()) * entity.getCount();
            }
        }
        return df.format(total_price);
    }

    public Float getFullTotalPrice(){
        float total_price = 0;
        for (GoodsEntity<GoodsResponse> entity : mData) {
            GoodsResponse good = entity.getData();
            if (!good.isMemberPrice()) {
                if (entity.getItemType() == GoodsEntity.TYPE_ONLY_PROCESSING) {
                    total_price += Float.parseFloat(good.getProcess_price()) * entity.getCount();
                } else {
                    total_price += Float.parseFloat(good.getPrice()) * entity.getCount();
                }
            }
        }
        return total_price;
    }

    public int getTotalCount() {
        int count = 0;
        for (GoodsEntity<GoodsResponse> entity : mData) {
            count += entity.getCount();
        }
        return count;
    }
}
