package com.easygo.cashier.widget.dialog;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.Events;
import com.easygo.cashier.utils.MemberUtils;
import com.easygo.cashier.R;
import com.easygo.cashier.utils.SoftKeyboardUtil;
import com.easygo.cashier.bean.MemberInfo;
import com.easygo.cashier.widget.view.DialogSearchView;
import com.niubility.library.utils.EventUtils;

import java.text.DecimalFormat;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @Describe：选择会员
 * @date：2019-01-08
 */
public class ChooseMembersDialog extends MyBaseDialog {
    private RecyclerView recyclerView;
    private TextView dialog_title;
    private DialogSearchView searchView;
    private ConstraintLayout clCancel;

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
        dialog_title = rootView.findViewById(R.id.tv_name);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        searchView = rootView.findViewById(R.id.search_view);
        clCancel = rootView.findViewById(R.id.cl_cancel);

        if (!TextUtils.isEmpty(title)) {
            dialog_title.setText(title);
        }

//        searchView.setContent("13048061473");

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(verticalDecoration);
        recyclerView.setAdapter(adapter = new BaseQuickAdapter<MemberInfo, BaseViewHolder>(R.layout.item_members_list) {

            DecimalFormat df = new DecimalFormat("0.00");

            @Override
            protected void convert(BaseViewHolder helper, final MemberInfo item) {
                helper.setText(R.id.tv_telphone, item.getPhone())
                        .setText(R.id.tv_nick, item.getNick_name())
                        .setText(R.id.tv_member_card, item.getCard_no())
                        .setText(R.id.tv_package_balance, "￥" + df.format(item.getWallet()))
                        .setText(R.id.tv_integral_balance, df.format(item.getIntegral()))
                        .setText(R.id.tv_process, "选择")
                        .getView(R.id.tv_process).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MemberUtils.memberInfo = item;
                        EventUtils.post(Events.MEMBER_INFO, item);
                        searchView.setContent("");
                        dialogDismiss();
                    }
                });
            }
        });
        recyclerView.setVisibility(View.INVISIBLE);

        setEmpty();
        setListener();
    }




    private void setListener() {
        clCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismiss();
            }
        });

        searchView.setOnSearchChangeListener(new DialogSearchView.OnSearchChangeListener() {
            @Override
            public void onSearchClick(String content) {
                listener.onSearch(searchView.getContent());
                if (searchView.getEditText() != null) {
                    SoftKeyboardUtil.hideSoftKeyboard(getActivity(), searchView.getEditText());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(searchView != null) {
            searchView.setContent("");
        }
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
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_MEMBERS");
    }
}
