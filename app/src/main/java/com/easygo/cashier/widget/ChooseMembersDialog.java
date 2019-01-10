package com.easygo.cashier.widget;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.Configs;
import com.easygo.cashier.Events;
import com.easygo.cashier.R;
import com.easygo.cashier.SoftKeyboardUtil;
import com.easygo.cashier.bean.MemberInfo;
import com.niubility.library.base.BaseDialog;
import com.niubility.library.utils.EventUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe：选择会员
 * @date：2019-01-08
 */
public class ChooseMembersDialog extends BaseDialog {
    private RecyclerView recyclerView;
    private TextView dialog_title;
    private DialogSearchView searchView;
    private ImageView ivCancel;

    private String title = "";
    private BaseQuickAdapter<MemberInfo, BaseViewHolder> adapter;
    private OnSearchListener listener;

    public interface OnSearchListener {
        void onSearch(String content);
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_choose_members;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected void initView(View rootView) {
        dialog_title = rootView.findViewById(R.id.tv_name);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        searchView = rootView.findViewById(R.id.search_view);
        ivCancel = rootView.findViewById(R.id.iv_cancel);

        searchView.setVisiable(true);
        if (!TextUtils.isEmpty(title)) {
            dialog_title.setText(title);
        }

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(verticalDecoration);
        recyclerView.setAdapter(adapter = new BaseQuickAdapter<MemberInfo, BaseViewHolder>(R.layout.item_members_list) {
            @Override
            protected void convert(BaseViewHolder helper, final MemberInfo item) {
                helper.setText(R.id.tv_telphone, item.getPhone())
                        .setText(R.id.tv_nick, item.getNick_name())
                        .setText(R.id.tv_member_card, item.getCard_no())
                        .setText(R.id.tv_package_balance, "￥" + item.getWallet())
                        .setText(R.id.tv_integral_balance, item.getIntegral() + "")
                        .setText(R.id.tv_process, "选择")
                        .getView(R.id.tv_process).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Configs.memberInfo = item;
                        EventUtils.post(Events.MEMBER_INFO, item);
                        searchView.setContent("");
                        dialogDismiss();
                    }
                });
            }
        });

        setEmpty();
        setListener();
    }

    private void setListener() {
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismiss();
            }
        });

        searchView.setOnFoceChangeListener(new DialogSearchView.OnFocuChangeListener() {
            @Override
            public void onFocuChange(boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(searchView.getContent())) {
                        return;
                    }
                    listener.onSearch(searchView.getContent());
                }
            }

            @Override
            public void onSearchClick(String content) {
                listener.onSearch(searchView.getContent());
                if (searchView.getEditText() != null) {
                    SoftKeyboardUtil.hideSoftKeyboard(getActivity(), searchView.getEditText());
                }
            }
        });
    }

    public boolean isShow() {
        return isShowing();
    }

    private void dialogDismiss() {
        if (searchView.getEditText() != null) {
            SoftKeyboardUtil.hideSoftKeyboard(getActivity(), searchView.getEditText());
        }
        dismiss();
    }

    /* 设置adapter空数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        TextView view = emptyView.findViewById(R.id.text);
        view.setText(getResources().getString(R.string.text_no_telphone));
        adapter.setEmptyView(emptyView);
    }

    public void setNewData(List<MemberInfo> infos) {
        if (adapter != null) {
            adapter.setNewData(infos);
        }
    }

    public void setTitle(String title) {
        this.title = title;
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
        showCenter(activity, "DIALOG_MEMBERS");
    }
}
