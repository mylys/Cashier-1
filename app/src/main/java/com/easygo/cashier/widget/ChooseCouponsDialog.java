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
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.SoftKeyboardUtil;
import com.niubility.library.base.BaseDialog;

import java.util.ArrayList;

/**
 * @Describe：选择优惠券
 * @date：2019-01-08
 */
public class ChooseCouponsDialog extends BaseDialog {
    private RecyclerView recyclerView;
    private DialogSearchView searchView;
    private TextView dialog_title;
    private ImageView dialog_cancel;

    private String title;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

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

        searchView.setHint(getResources().getString(R.string.text_input_coupon));

        if (!TextUtils.isEmpty(title)) {
            dialog_title.setText(title);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dialog_coupons) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_coupon_price, priceText(item));
            }
        });
        for (int i = 1; i <= 5; i++) {
            adapter.addData(i + "0");
        }

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


    }

    public SpannableString priceText(String price) {
        SpannableString spannableString = new SpannableString("￥" + price);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.6f);
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        spannableString.setSpan(relativeSizeSpan, 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(superscriptSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
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
