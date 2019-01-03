package com.easygo.cashier.widget;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.Configs;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.EquipmentState;
import com.niubility.library.base.BaseDialog;

import java.util.ArrayList;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-20
 */
public class EquipmentstateDialog extends BaseDialog {
    private TextView dialog_title;
    private ImageView iv_cancel;
    private RecyclerView recyclerView;
    private String d_title = "";

    private BaseQuickAdapter<EquipmentState, BaseViewHolder> adapter;
    private ArrayList<EquipmentState> data = null;

    public static EquipmentstateDialog getInstance(Bundle bundle) {
        EquipmentstateDialog dialog = new EquipmentstateDialog();
        if (bundle != null)
            dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_function_dialog;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(getResources().getDimensionPixelSize(R.dimen.equipment_list_width), data.size() == 1 ? getResources().getDimensionPixelSize(R.dimen.equipment_list_height1) : getResources().getDimensionPixelSize(R.dimen.equipment_list_height));
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(uiOptions);
        }
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView(View rootView) {
        if (getArguments() != null) {
            d_title = getArguments().getString("title");
            data = getArguments().getParcelableArrayList("data");
        }
        dialog_title = rootView.findViewById(R.id.tv_function_list);
        recyclerView = rootView.findViewById(R.id.rv_function);
        iv_cancel = rootView.findViewById(R.id.iv_cancel);

        dialog_title.setText(d_title);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        recyclerView.addItemDecoration(verticalDecoration);
        recyclerView.setAdapter(adapter = new BaseQuickAdapter<EquipmentState, BaseViewHolder>(R.layout.item_dialog_equipment) {
            @Override
            protected void convert(BaseViewHolder helper, final EquipmentState item) {
                int normal = getResources().getColor(R.color.color_16A1E1);
                int abnormal = getResources().getColor(R.color.color_B02F38);
                String normal_str = getResources().getString(R.string.device_normal);
                String abnormal_str = getResources().getString(R.string.device_abnormal);

                helper.getView(R.id.loading).setVisibility(item.isEquipment_request() ? View.VISIBLE : View.GONE);
                helper.setText(R.id.tv_equipment_name, item.getEquipment_name())
                        .setText(R.id.tv_equipment_state, item.isEquipment_state() ? normal_str : abnormal_str)
                        .setTextColor(R.id.tv_equipment_state, item.isEquipment_state() ? normal : abnormal)
                        .getView(R.id.tv_connection).setVisibility(item.isEquipment_state() ? View.GONE : View.VISIBLE);
            }
        });
        if (data != null) {
            adapter.setNewData(data);
        }

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
        showCenter(activity, "EQUIPMENT_STATE");
    }

    public boolean isShow() {
        return isShowing();
    }

    public void setNewData(int position, String name, boolean state) {
        EquipmentState bean = adapter.getData().get(position);
        bean.setEquipment_name(name);
        bean.setEquipment_state(state);
        bean.setEquipment_request(false);
        adapter.notifyDataSetChanged();
    }
}