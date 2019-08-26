package com.easygo.cashier.adapter;

import android.support.v4.util.ArrayMap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.utils.MemberUtils;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.goods.BaseGoodsPromotion;
import com.easygo.cashier.widget.view.CountTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
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
    DecimalFormat df_int;
    DecimalFormat df_weight;

    public GoodsMultiItemAdapter() {
        super(null);
        df = new DecimalFormat("0.00");
        df_int = new DecimalFormat("#");
        df_weight = new DecimalFormat("#0.000");
        addItemType(GoodsEntity.TYPE_GOODS, R.layout.item_goods);
        addItemType(GoodsEntity.TYPE_WEIGHT, R.layout.item_goods_weight);
        addItemType(GoodsEntity.TYPE_ONLY_PROCESSING, R.layout.item_goods);
        addItemType(GoodsEntity.TYPE_PROCESSING, R.layout.item_goods_processing);
        addItemType(GoodsEntity.TYPE_NO_CODE, R.layout.item_goods);
    }

    protected ArrayMap<String, GoodsEntity<GoodsResponse>> data;
    protected ArrayList<String> barcodeData = new ArrayList<>();
    protected HashSet<String> limit = new HashSet<>();


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
        addItem(t, 1);
    }
    public void addItem(GoodsResponse t, float count) {
        String code = t.getBarcode();
        ensureNotNull();
        if (!data.containsKey(code)) {
            GoodsEntity<GoodsResponse> goodsNum = new GoodsEntity<>(GoodsEntity.TYPE_GOODS);
            goodsNum.setData(t);
            goodsNum.setCount(count);
            barcodeData.add(code);
            data.put(code, goodsNum);
            mData.add(goodsNum);
            notifyItemInserted(mData.size() - 1);
        } else {
            data.get(code).setCount((data.get(code).getCount()) + count);
            notifyItemChanged(barcodeData.indexOf(code));
        }
        refreshPrice();
    }

    //重量商品
    public void addWeightItem(GoodsResponse t, float weight) {
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
    public void addPrcessingItem(List<GoodsResponse> list, float weight) {
        ensureNotNull();
        GoodsEntity<GoodsResponse> goodsNum = new GoodsEntity<>(GoodsEntity.TYPE_PROCESSING);
        goodsNum.setCount(weight);

        int size = list.size();
        List<GoodsResponse> processing_list = new ArrayList<>(size - 1);
        String code = "";
        for (int i = 0; i < size; i++) {
            GoodsResponse goodsResponse = list.get(i);

            if (goodsResponse.isMainGood()) {//主商品
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
    public boolean addItem(List<GoodsResponse> t, float weight) {
        int size = t.size();
        String barcode;
        GoodsResponse goodsResponse;
        for (int i = 0; i < size; i++) {
            goodsResponse = t.get(i);
            barcode = goodsResponse.getBarcode();
            if(goodsResponse.getIs_inventory_limit() == 0) {//关闭了库存限制
                limit.remove(barcode);
                continue;
            }
            if(!goodsResponse.isMainGood()) {
                //排除加工方式
                continue;
            }

            if (limit.contains(barcode)) {
                return false;
            } else {
                if(data != null && data.containsKey(barcode)) {//已经被添加进来
                    if (goodsResponse.getOn_sale_count() - data.get(barcode).getCount() - weight < 0) {
                        //在售数量 - 已经添加数量 - 即将添加数量 < 0 说明库存不足
                        limit.add(barcode);
                        return false;
                    }
                } else {//还没被添加进来
                    if(goodsResponse.getOn_sale_count() - weight < 0) {
                        //在售数量 - 即将添加数量 < 0 说明库存不足
                        limit.add(barcode);
                        return false;
                    }
                }
            }
        }
        //-------------- 有库存

        for (GoodsResponse info : t) {
            Double membership_price = Double.valueOf(info.getMembership_price());
            if (membership_price > 0 && membership_price < Double.valueOf(info.getPrice())) {
                info.setMemberPrice(true);
            } else {
                info.setMemberPrice(false);
                info.setMembership_price("0.00");
            }
        }
        //普通商品   可调节数量                                         （同一条码 合并）
        //重量商品   不可调节数量   barcode 以22开头   重量需传进来        （同一条码 合并）

        //纯加工商品   不可调节数量   单个数据                             （同一条码 不合并）
        //可选择的加工商品   不可调节数量   返回多个数据                    （同一条码 不合并）


        //为商品添加时间戳，唯一识别商品以方便后台增减库存
        long timeStamp = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            t.get(i).setIdentity(String.valueOf(timeStamp));
        }
        if (size == 1) {//商品 （重量、普通、纯加工）
            goodsResponse = t.get(0);

            if (goodsResponse.isMainGood()) {//主商品
                if (goodsResponse.getIs_weigh() == 1) {
                    //重量商品
                    goodsResponse.setType(GoodsResponse.type_weight);
                    addWeightItem(goodsResponse, weight);
                } else {
                    //普通商品
                    goodsResponse.setType(GoodsResponse.type_normal);
                    addItem(goodsResponse, weight);
                }
            } else if (!goodsResponse.isMainGood()) {
                //纯加工商品
                goodsResponse.setType(GoodsResponse.type_processing);
                addOnlyPrcessingItem(goodsResponse, 1);
            }

        } else {//加工商品
            for (int i = 0; i < size; i++) {
                goodsResponse = t.get(i);
                if (goodsResponse.isMainGood()) {//主商品
                    goodsResponse.setType(GoodsResponse.type_weight);
                } else {//加工商品
                    goodsResponse.setType(GoodsResponse.type_processing);
                }
            }
            addPrcessingItem(t, weight);
        }
        return true;
    }

    //无码商品
    public void addNoCodeItem(float price) {
        String code = df.format(price);

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
        goodsResponse.setMembership_price("0.00");
        goodsResponse.setMemberPrice(false);

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
            double p;
            for (int i = 0; i < size; i++) {
                GoodsEntity<GoodsResponse> good = mData.get(i);

                p = Double.valueOf(good.getData().getPrice());

                coupon += Double.valueOf(good.getData().getDiscount_price());
                price += p * good.getCount();
                switch (good.getItemType()) {
                    case GoodsEntity.TYPE_GOODS:
                    case GoodsEntity.TYPE_NO_CODE:
                        count += good.getCount();
                        break;
                    case GoodsEntity.TYPE_WEIGHT:
                        count += 1;
                        break;
                    case GoodsEntity.TYPE_ONLY_PROCESSING:
                        count += 1;
                        break;
                    case GoodsEntity.TYPE_PROCESSING:
                        count += 1;
                        //选择加工时 更新价格
                        GoodsResponse processing = good.getProcessing();
                        if (processing != null) {//此时 选择了加工
                            count += 1;
                            coupon += Double.valueOf(processing.getDiscount_price());
                            p = Double.valueOf(processing.getPrice());
                            price += p * processing.getCount();
                        }
                        break;
                }
            }
            mListener.onPriceChange((float) price, count, (float) coupon);
        }
    }


    @Override
    protected void convert(final BaseViewHolder helper, final GoodsEntity<GoodsResponse> item) {
        final int position = helper.getAdapterPosition();

        final float good_count = item.getCount();
        final GoodsResponse good = item.getData();

        final String barcode = good.getBarcode();
        final String price = good.getPrice();
        float subtotal = Float.valueOf(price) * good_count - Float.valueOf(good.getDiscount_price());

        if(subtotal < 0) {
            subtotal = 0;
        }

        BaseGoodsPromotion promotion = item.getPromotion();

        helper.getView(R.id.tv_member_price).setVisibility(MemberUtils.isMember ? View.VISIBLE : View.GONE);
        helper.setText(R.id.tv_barcode, barcode)
                .setText(R.id.tv_goods_name, good.getG_sku_name())
                .setText(R.id.tv_price, String.valueOf(price))
                .setText(R.id.tv_subtotal, String.valueOf(df.format(subtotal)))
                .setText(R.id.tv_coupon, df.format(Float.valueOf(good.getDiscount_price())))
                .setText(R.id.tv_member_price, good.getMembership_price())
                .setVisible(R.id.iv_coupon, Float.valueOf(good.getDiscount_price()) > 0);

        switch (helper.getItemViewType()) {
            case GoodsEntity.TYPE_GOODS://普通商品
            case GoodsEntity.TYPE_NO_CODE://无码商品
                final CountTextView countTextView = helper.getView(R.id.count_view);
                if(good.isCount_disable()) {//自编码按数量计的商品 不可加减
                    countTextView.setCountChangeEnable(false);
                } else {
                    countTextView.setCountChangeEnable(true);
                }
                countTextView.setCount(df_int.format(good_count));
                if(!good.isCount_disable()) {
                    countTextView.setOnCountListener(new CountTextView.OnCountListener() {
                        @Override
                        public void onCountChanged(int count) {

                            if (count == 0) {
                                //清除当前商品
                                remove(position);
                                return;

                            } else {
                                float on_sale_count = good.getOn_sale_count();
                                if (good.getIs_inventory_limit() == 1 && helper.getItemViewType() == GoodsEntity.TYPE_GOODS
                                        && on_sale_count - count < 0) {
                                    //数量大于在售数量了
                                    count--;
                                    countTextView.setCount(count + "");

                                    if (mListener != null) {
                                        mListener.onSaleCountNotEnough();
                                    }
                                    //添加到限制集合
                                    limit.add(barcode);

                                }
                                if (on_sale_count - count > 0) {
                                    //移除出 限制集合
                                    limit.remove(barcode);
                                } else {
                                    //添加到 限制集合
                                    limit.add(barcode);
                                }

                                item.setCount(count);
                                notifyItemChanged(position);
                            }
                            //刷新价格
                            refreshPrice();

                        }

                        @Override
                        public void onCountClick() {
                            if (mListener != null) {
                                mListener.onCountClick(countTextView, position, countTextView.getCount());
                            }
                        }
                    });
                }
                break;
            case GoodsEntity.TYPE_WEIGHT://称重商品
                helper.setText(R.id.tv_count, df_weight.format(good_count) + good.getG_u_symbol());
                break;
            case GoodsEntity.TYPE_ONLY_PROCESSING://纯加工方式
                CountTextView view = helper.getView(R.id.count_view);
                view.setCountChangeEnable(false);

                break;
            case GoodsEntity.TYPE_PROCESSING://加工方式

                final CheckBox cb_processing = helper.getView(R.id.cb_processing);

                GoodsResponse processing = item.getProcessing();
                cb_processing.setOnCheckedChangeListener(null);
                if (processing != null) {//选择了加工方式
                    cb_processing.setChecked(true);

                    //刷新数据
                    refreshPrcessingData(helper, processing);
                } else {
                    cb_processing.setChecked(false);
                }

                final boolean is_processing = cb_processing.isChecked();

                //设置加工方式相关控件可见性
                helper.setText(R.id.tv_count,  df_weight.format(good_count) + good.getG_u_symbol());
                setProcessingLayout(helper, is_processing);


                //是否加工监听
                cb_processing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setProcessingLayout(helper, isChecked);

                        GoodsEntity<GoodsResponse> goodsEntity = mData.get(position);

                        //选择加工时 设置加工商品，否则置空
                        GoodsResponse default_processing = goodsEntity.getProcessing_list().get(0);
                        goodsEntity.setProcessing(isChecked ? default_processing : null);
                        if (isChecked) {
                            default_processing.setIdentity(String.valueOf(System.currentTimeMillis()));
                            refreshPrcessingData(helper, default_processing);
                        }

                        refreshPrice();

                        if (mListener != null) {
                            mListener.onProcessingCheckedChanged(isChecked, position,
                                    goodsEntity.getProcessing());
                        }

                    }
                });
                //加工方式点击监听
                helper.getView(R.id.cl_processing).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onProcessingClicked(position,
                                    item.getProcessing(), item.getProcessing_list());
                        }
                    }
                });

                break;
        }

        View view = helper.getView(R.id.cl_remove);
        switch (helper.getItemViewType()) {
            case GoodsEntity.TYPE_GOODS://普通商品
                if(!good.isCount_disable()) {//没有禁止加减按钮 即普通非称重商品
                    view.setVisibility(View.GONE);
                    break;
                }
                //普通下发至称重机 按数量计的商品，禁止加减，显示移除按钮
            case GoodsEntity.TYPE_WEIGHT://称重商品
            case GoodsEntity.TYPE_PROCESSING://加工商品
            case GoodsEntity.TYPE_ONLY_PROCESSING://加工商品
//                View view = helper.getView(R.id.cl_remove);
                view.setVisibility(View.VISIBLE);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemRemoved(position);
                        }

                        remove(position);
                        limit.remove(barcode);
                        refreshPrice();
                    }
                });

                break;
        }

        String promotion_name = "";
        if (promotion != null) {
            promotion_name = promotion.getName();
        }
        final String final_promotion_name = promotion_name;
        helper.getView(R.id.iv_coupon).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mListener != null) {
                    return mListener. onPromotionTouch(v, event, final_promotion_name);
                }
                return false;
            }
        });

        helper.itemView.setSelected(item.isSelected());
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
        float processing_subtotal = Float.valueOf(processing.getPrice()) * processing.getCount()
                - Float.valueOf(processing.getDiscount_price());

        if(processing_subtotal < 0) {
            processing_subtotal = 0;
        }

        helper.setText(R.id.tv_processing_name, processing.getG_sku_name())
                .setText(R.id.tv_processing_price, processing.getPrice())
                .setText(R.id.tv_processing_coupon, df.format(Float.valueOf(processing.getDiscount_price())))
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

        if(position >= mData.size()) {
            return;
        }

        if (mListener != null) {
            mListener.onItemRemoved(position);
        }

        GoodsEntity<GoodsResponse> goodsEntity = mData.get(position);
        GoodsResponse data = goodsEntity.getData();

        mData.remove(position);
        String barcode;

        if(goodsEntity.getItemType() == GoodsEntity.TYPE_NO_CODE) {
            barcode = data.getPrice();
        } else {
            barcode = data.getBarcode();
        }

        int size = mData.size();
        boolean needDeleteBarcode = true;
        for (int i = 0; i < size; i++) {
            if(goodsEntity.getItemType() == GoodsEntity.TYPE_NO_CODE) {
                if (barcode.equals(mData.get(i).getData().getPrice())) {
                    needDeleteBarcode = false;
                    break;
                }
            } else {
                if (barcode.equals(mData.get(i).getData().getBarcode())) {
                    needDeleteBarcode = false;
                    break;
                }
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
        ensureNotNull();
        int count = mData.size();
        data.clear();
        barcodeData.clear();
        mData.clear();
        limit.clear();
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

        void onCountClick(CountTextView countTextView, int position, int count);

        /**
         * 按压促销图标时
         * @param v
         * @param event
         */
        boolean onPromotionTouch(View v, MotionEvent event, String promotion_name);


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

    public void addOrdersData(List<GoodsEntity<GoodsResponse>> list) {
        ensureNotNull();
        for (GoodsEntity<GoodsResponse> entity : list) {
            if(entity.getData().getType() == GoodsResponse.type_no_code) {
                String price = entity.getData().getPrice();
                data.put(df.format(Float.valueOf(price)), entity);
            } else {
                data.put(entity.getData().getBarcode(), entity);
            }
            barcodeData.add(entity.getData().getBarcode());
        }
        notifyDataSetChanged();

        refreshPrice();
    }

    public void refreshGoodsData(List<GoodsEntity<GoodsResponse>> list) {
        ensureNotNull();
        data.clear();
        barcodeData.clear();
        mData.clear();
        limit.clear();

        for (GoodsEntity<GoodsResponse> entity : list) {
            if(entity.getData().getType() == GoodsResponse.type_no_code) {
                String price = entity.getData().getPrice();
                data.put(df.format(Float.valueOf(price)), entity);
            } else {
                data.put(entity.getData().getBarcode(), entity);
            }
            barcodeData.add(entity.getData().getBarcode());
        }
        mData.addAll(list);

        notifyDataSetChanged();
    }

    /**
     * 获取临时商品促销、商品促销、会员 全部优惠金额
     * @return
     */
    public String getTotalCoupon() {
        float coupon = 0;
        for (GoodsEntity<GoodsResponse> entity : mData) {
            GoodsResponse good = entity.getData();
            coupon += good.getTemp_goods_discount() + good.getGoods_activity_discount() + good.getMember_discount();
        }
        return df.format(coupon);
    }

    /**
     * 获取除加工方式商品外的所有商品小计
     * @return
     */
    public float getSubtotal() {
        float temp;
        float count;
        float price;
        float coupon;
        float result = 0;
        for (GoodsEntity<GoodsResponse> entity : mData) {
            GoodsResponse good = entity.getData();

            price = Float.valueOf(good.getPrice());
            count = entity.getCount();
            coupon = Float.valueOf(good.getDiscount_price());
            temp = price * count - coupon;
            if(temp < 0) {
                temp = 0;
            }
            result += temp;
        }
        return result;

    }

    /**
     * 获取除加工方式商品外,没有参与商品促销和会员的所有商品小计
     * @return
     */
    public float getShopTotal() {
        float temp;
        float count;
        float price;
        float coupon;
        float result = 0;
        GoodsResponse good;
        for (GoodsEntity<GoodsResponse> entity : mData) {
            good = entity.getData();
            if(entity.getPromotion() != null) {
                continue;
            }

            coupon = Float.valueOf(good.getDiscount_price());
            if (coupon > 0) {
                continue;
            }

            price = Float.valueOf(good.getPrice());
            count = entity.getCount();
            temp = price * count;
            if(temp < 0) {
                temp = 0;
            }
            result += temp;
        }
        return result;

    }

    /**
     * 获取计算店铺促销时的金额小计
     * @return
     */
    public float getShopTotal(List<String> excludeBarcodeList) {
        float temp;
        float count;
        float price;
        float temp_goods_discount;
        float goods_discount;
        float member_discount;
        float result = 0;
        GoodsResponse good;
        int exclude_size = excludeBarcodeList.size();
        boolean is_exclude = false;
        for (GoodsEntity<GoodsResponse> entity : mData) {
            good = entity.getData();
            if(entity.getPromotion() != null) {
                continue;
            }

            temp_goods_discount = good.getTemp_goods_discount();
            goods_discount = good.getGoods_activity_discount();
            member_discount = good.getMember_discount();
            if (temp_goods_discount != 0 || goods_discount != 0 || member_discount != 0) {
                continue;
            }

            is_exclude = false;
            //判断是否在排除列表里面
            if(exclude_size > 0) {
                for (int i = 0; i < exclude_size; i++) {
                    if(excludeBarcodeList.contains(good.getBarcode())) {
                        //在排除商品列表里面
                        is_exclude = true;
                        entity.setExcludeInShopActivity(true);
                        break;
                    }
                }
            }
            //排除此商品
            if(is_exclude) {
                continue;
            }
            entity.setExcludeInShopActivity(false);

            price = Float.valueOf(good.getPrice());
            count = entity.getCount();
            temp = price * count;
            if(temp < 0) {
                temp = 0;
            }
            result += temp;
        }
        return result;

    }


    /**
     * 获取被选中的数据集合
     * @return
     */
    public List<GoodsEntity<GoodsResponse>> getSelected(){
        List<GoodsEntity<GoodsResponse>> result = null;

        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = mData.get(i);

            if(!goodsEntity.isSelected()) {
                continue;
            }

            if (result == null) {
                result = new ArrayList<>();
            }
            result.add(goodsEntity);
        }

        return result;
    }

    /**
     * 取消所有选中条目
     */
    public void cancelAllSelected() {

        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = mData.get(i);

            if(goodsEntity.isSelected()) {
                goodsEntity.setSelected(false);

                View root = getViewByPosition(i, R.id.root);
                if(root != null) {
                    root.setSelected(false);
                }
            }
        }
    }

    public void setGoodsCount(int position, int count) {
        if(position < 0 || position >= mData.size()) {
            return;
        }
        CountTextView countTextView = (CountTextView) getViewByPosition(position, R.id.count_view);

        GoodsEntity<GoodsResponse> goodsEntity = mData.get(position);
        GoodsResponse good = goodsEntity.getData();
        String barcode = good.getBarcode();
        float on_sale_count = good.getOn_sale_count();

        if (good.getIs_inventory_limit() == 1 && good.getType() == GoodsResponse.type_normal
                && count > on_sale_count) {

            if (mListener != null) {
                mListener.onSaleCountNotEnough();
            }
        }
        if (count == on_sale_count) {
            limit.add(barcode);
        }

        goodsEntity.setCount(count);
        countTextView.setCount(count + "");
        notifyItemChanged(position);
        if (mListener != null) {
            mListener.onCountChanged(position, count);
        }
        //刷新价格
        refreshPrice();
    }


}
