package com.easygo.cashier.widget.dialog;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.niubility.library.base.BaseDialog;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Describe：通用List弹窗
 * @date：2019-01-03
 */
public class GeneraListDialog extends MyBaseDialog {

    private int type = -1;
    private String name = "";
    private String title = "";
    public static final int CURRENCY_SYMBOL = 1001;             //货币符号
    public static final int NETWORK_TYPE = 1002;                //网络类型
    public static final int RECEIPTS_PRINT_NUMBER = 1003;       //小票机打印数量
    public static final int RECEIPTS_PRINT_WIDTH = 1004;        //小票机打印宽度
    public static final int PHOTO_INTERVAL_TIME = 1005;         //图片切换时间
    public static final int LANGUAGE_SETTING = 10006;           //语言设置

    private TextView dialog_title;
    private ImageView dialog_cancel;
    private RecyclerView recyclerView;

    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private int lastPosition = -1;
    private SparseBooleanArray mBooleanArray;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_genera_list_dialog;
    }

    @Override
    protected int getLayoutWidth() {
        return getResources().getDimensionPixelSize(R.dimen.genera_list_item_width);
    }

    @Override
    protected int getLayoutHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected void initView(View rootView) {
        dialog_title = rootView.findViewById(R.id.tv_function_list);
        dialog_cancel = rootView.findViewById(R.id.iv_cancel);
        recyclerView = rootView.findViewById(R.id.rv_function);

        if (!TextUtils.isEmpty(title)) {
            dialog_title.setText(title);
        }

        setAdapter();
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        recyclerView.addItemDecoration(verticalDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        getList(type != -1 ? type : CURRENCY_SYMBOL);

        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setAdapter() {
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dialog_generalist) {
            @Override
            protected void convert(final BaseViewHolder helper, final String item) {
                helper.getView(R.id.image_select).setVisibility(item.equals(name) ? View.VISIBLE : View.GONE);
                helper.setText(R.id.tv_list_name, item)
                        .getView(R.id.constraint_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        setItemChecked(helper.getLayoutPosition());
                        listener.onItemClick(helper.getLayoutPosition());
                        dismiss();
                    }
                });
            }
        };
    }

    public void setType(int type, String name, String title) {
        this.type = type;
        this.name = name;
        this.title = title;
    }

    /* Item单选，因点击隐藏，故不需要（防止需求改动） */
    private void setItemChecked(int position) {
        if (lastPosition == position)
            return;
        mBooleanArray.put(position, true);

        if (lastPosition > -1) {
            mBooleanArray.put(lastPosition, false);
            adapter.notifyItemChanged(lastPosition);
        }
        adapter.notifyDataSetChanged();
        lastPosition = position;
    }

    //根据选择弹窗类型载入列表数据
    private void getList(int type) {
        ArrayList<String> list = new ArrayList<>();
        String[] type_list = new String[0];

        switch (type) {
            case CURRENCY_SYMBOL:
                type_list = new String[]{getString(R.string.text_genera_list_follow_system), getString(R.string.text_genera_money), getString(R.string.text_genera_nt), getString(R.string.text_genera_hk), getString(R.string.text_genera_pat)};
                break;
            case NETWORK_TYPE:
                type_list = new String[]{getString(R.string.text_genera_wifi), getString(R.string.text_genera_3g_mobile), getString(R.string.text_genera_3g_unicom_telecom), getString(R.string.text_genera_4g)};
                break;
            case RECEIPTS_PRINT_NUMBER:
                type_list = new String[]{getString(R.string.text_genera_number_1), getString(R.string.text_genera_number_2), getString(R.string.text_genera_number_3)};
                break;
            case RECEIPTS_PRINT_WIDTH:
                type_list = new String[]{getString(R.string.text_genera_width_58), getString(R.string.text_genera_width_88)};
                break;
            case PHOTO_INTERVAL_TIME:
                type_list = new String[]{getString(R.string.text_genera_seconds_5), getString(R.string.text_genera_seconds_10), getString(R.string.text_genera_seconds_15), getString(R.string.text_genera_seconds_30), getString(R.string.text_genera_seconds_60)};
                break;
            case LANGUAGE_SETTING:
                type_list = new String[]{getString(R.string.text_simplified), getString(R.string.text_traditional), getString(R.string.text_english)};
                break;
        }
        Collections.addAll(list, type_list);
        if (adapter != null) {
            adapter.setNewData(list);
//            mBooleanArray = new SparseBooleanArray(list.size());
//            setItemChecked(0);
        }
    }

    @Override
    protected boolean canCanceledOnTouchOutside() {
        return true;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_GENERALIST");
    }
}
