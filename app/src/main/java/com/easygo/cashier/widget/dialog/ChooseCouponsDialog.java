package com.easygo.cashier.widget.dialog;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.Configs;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.utils.CouponUtils;
import com.easygo.cashier.utils.MemberUtils;
import com.easygo.cashier.R;
import com.easygo.cashier.utils.SoftKeyboardUtil;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.widget.view.DialogSearchView;
import com.niubility.library.base.BaseDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @Describe：选择优惠券
 * @date：2019-01-08
 */
public class ChooseCouponsDialog extends MyBaseDialog {
    private RecyclerView recyclerView;
    private DialogSearchView searchView;
    private TextView dialog_title;
    private ConstraintLayout clCancel;

    private String title;
    private BaseQuickAdapter<CouponResponse, BaseViewHolder> adapter;

    private float price;
    private List<GoodsEntity<GoodsResponse>> goodsData;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_choose_coupons;
    }

    @Override
    protected int getLayoutWidth() {
        return 0;
    }

    @Override
    protected int getLayoutHeight() {
        return 0;
    }

    @Override
    protected boolean isHideNavigationBar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        searchView = rootView.findViewById(R.id.search_view);
        dialog_title = rootView.findViewById(R.id.tv_name);
        clCancel = rootView.findViewById(R.id.cl_cancel);
//        etBarcode = rootView.findViewById(R.id.et_barcode);

        searchView.setHint(getResources().getString(R.string.text_input_coupon));

        //正式服优惠券
//        searchView.setContent("RYH463c3484cb6e4f9f04c6437bda07d69d");
//        searchView.setContent("UYHacdfb070859c7348024c8cd2b6bb5983");
//        searchView.setContent("UYH889b4480567ddec65b1a4f629077588c");

        if (!TextUtils.isEmpty(title)) {
            dialog_title.setText(title);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new BaseQuickAdapter<CouponResponse, BaseViewHolder>(R.layout.item_dialog_coupons) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

            @Override
            protected void convert(BaseViewHolder helper, CouponResponse item) {
                String condition_value = item.getCondition_value();
                float offer_value = item.getOffer_value();
                String effected_at = sdf.format(item.getEffected_at());
                String expired_at = sdf.format(item.getExpired_at());

                int offer_type = item.getOffer_type();
                if(offer_type == 1) {
                    helper.setText(R.id.tv_coupon_price, priceText(String.valueOf(offer_value)));
                } else if(offer_type == 2) {
                    DecimalFormat df = new DecimalFormat("0.0");
                    helper.setText(R.id.tv_coupon_price, discountText(df.format((100 - offer_value) / 10f)));
                }

                helper.setText(R.id.tv_full_reduction, item.getName())
                        .setText(R.id.tv_full_use, "满" + condition_value + "元可用")
                        .setText(R.id.tv_coupon_validity, "有效期：" + effected_at + "至" + expired_at);

                String range = "";
                if(item.isForAll()) {
                    range = "（全店通用）";
                } else if(item.isForGood()) {
                    range = "（指定商品）";
                } else if(item.isForShop()) {
                    range = "（指定店铺）";
                }
                helper.setText(R.id.tv_coupon_state, item.getOffer_type_str() + range);

                //设置是否可用
                boolean isCouponEnable = item.isEnable();
                helper.getView(R.id.root).setEnabled(isCouponEnable);

                int enable_color = getResources().getColor(R.color.color_555555);
                int disable_color = getResources().getColor(R.color.color_text_ababab);
                int discount_color = getResources().getColor(R.color.color_f95e39);

                helper.setTextColor(R.id.tv_coupon_price, isCouponEnable? discount_color: disable_color)
                        .setTextColor(R.id.tv_full_reduction, isCouponEnable? enable_color: disable_color)
                        .setTextColor(R.id.tv_full_use, isCouponEnable? enable_color: disable_color)
                        .setTextColor(R.id.tv_coupon_state, isCouponEnable? enable_color: disable_color);

            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mListener != null) {
                    CouponResponse couponResponse = (CouponResponse) adapter.getData().get(position);
                    mListener.onItemClick(couponResponse);
                }
                dialogDismiss();
            }
        });
        recyclerView.setVisibility(View.INVISIBLE);

        setEmpty();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(searchView != null)
            searchView.setContent("");

        if(MemberUtils.isMember && MemberUtils.memberInfo != null) {
            List<CouponResponse> coupons = MemberUtils.memberInfo.getCoupons();
            int size = coupons.size();
            if(size > 0) {

                List<CouponResponse> data = new ArrayList<>(size);
                List<CouponResponse> disable = new ArrayList<>(size);
                CouponResponse couponResponse;
                for (int i = 0; i < size; i++) {
                    couponResponse = coupons.get(i);
                    if(CouponUtils.getInstance().isCouponEnable(couponResponse,
                            goodsData, Configs.shop_sn, price)) {
                        data.add(couponResponse);
                        couponResponse.setEnable(true);
                    } else {
                        disable.add(couponResponse);
                        couponResponse.setEnable(false);
                    }
                }

                if(disable.size() > 0) {
                    data.addAll(disable);
                }

                adapter.setNewData(data);
                recyclerView.scrollToPosition(0);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                SoftKeyboardUtil.showSoftKeyboard(searchView.getEditText());
            }
        } else {
            adapter.setNewData(null);
            if (searchView.getEditText() != null) {
                searchView.getEditText().requestFocus();
            }
        }
    }

    public void setInfo(List<GoodsEntity<GoodsResponse>> data, float price) {
        this.price = price;
        this.goodsData = data;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void setListener() {
        clCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getEditText() != null) {
                    SoftKeyboardUtil.hideSoftKeyboard(getActivity(), searchView.getEditText());
                }
                dismiss();
            }
        });
        searchView.setOnSearchChangeListener(new DialogSearchView.OnSearchChangeListener() {
            @Override
            public void onSearchClick(String content) {
                if(mListener != null) {
                    mListener.onSearch(content);
                }
                if (searchView.getEditText() != null) {
                    SoftKeyboardUtil.hideSoftKeyboard(getActivity(), searchView.getEditText());
                }
            }
        });
    }

    private void dialogDismiss() {
        if (searchView.getEditText() != null) {
            SoftKeyboardUtil.hideSoftKeyboard(getActivity(), searchView.getEditText());

            searchView.getEditText().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, 100);
        }
        recyclerView.setVisibility(View.INVISIBLE);
        goodsData = null;
    }

    public DialogSearchView getSearchView() {
        return searchView;
    }

    public void setNewData(List<CouponResponse> infos) {
        if (adapter != null) {
            adapter.setNewData(infos);
        }
        recyclerView.setVisibility(View.VISIBLE);
    }

    private OnSearchListener mListener;
    public void setOnSearchListener(OnSearchListener listener) {
        this.mListener = listener;
    }

    public interface OnSearchListener {
        void onSearch(String content);
        void onItemClick(CouponResponse result);
    }


    public SpannableString priceText(String price) {
        SpannableString spannableString = new SpannableString("￥" + price);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.6f);
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        spannableString.setSpan(relativeSizeSpan, 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(superscriptSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    public SpannableString discountText(String discount) {
        SpannableString spannableString = new SpannableString(discount+ "折");
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.6f);
//        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        SubscriptSpan subscriptSpan = new SubscriptSpan();
        spannableString.setSpan(new RelativeSizeSpan(1f), spannableString.length()-1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(relativeSizeSpan, 0, spannableString.length()-1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    /* 设置adapter空数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        TextView view = emptyView.findViewById(R.id.text);
        view.setText(getResources().getString(R.string.text_no_search_coupon));
        adapter.setEmptyView(emptyView);
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_COUPONS");
    }
}
