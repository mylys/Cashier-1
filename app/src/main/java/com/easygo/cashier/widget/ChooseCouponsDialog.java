package com.easygo.cashier.widget;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.SoftKeyboardUtil;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.MemberInfo;
import com.niubility.library.base.BaseDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @Describe：选择优惠券
 * @date：2019-01-08
 */
public class ChooseCouponsDialog extends BaseDialog {
    private RecyclerView recyclerView;
    private DialogSearchView searchView;
    private TextView dialog_title;
    private ImageView dialog_cancel;
    private EditText etBarcode;

    private String title;
    private BaseQuickAdapter<CouponResponse, BaseViewHolder> adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_choose_coupons;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected void initView(View rootView) {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        searchView = rootView.findViewById(R.id.search_view);
        dialog_title = rootView.findViewById(R.id.tv_name);
        dialog_cancel = rootView.findViewById(R.id.iv_cancel);
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
                int offer_value = item.getOffer_value();
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

    public void setTitle(String title) {
        this.title = title;
    }

    private void setListener() {
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
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
        searchView.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    onScanCode(searchView.getEditText().getText().toString().trim());
                }
                return false;
            }
        });
    }

    private void onScanCode(String code) {
        if(mListener != null) {
            Log.i("扫描优惠券", "onScanCode: " + code );
            mListener.onSearch(code);
        }
    }

    private void dialogDismiss() {
        if (searchView.getEditText() != null) {
            SoftKeyboardUtil.hideSoftKeyboard(getActivity(), searchView.getEditText());
        }
        recyclerView.setVisibility(View.INVISIBLE);
        dismiss();
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

    @Override
    protected boolean shouldHideBackground() {
        return false;
    }

    @Override
    protected boolean canCanceledOnTouchOutside() {
        return false;
    }

    @Override
    protected boolean isWindowWidthMatchParent() {
        return false;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_COUPONS");
    }
}
