package com.easygo.cashier.widget.dialog;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.EquipmentState;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-20
 */
public class EquipmentstateDialog extends MyBaseDialog {
    private TextView dialog_title;
    private ConstraintLayout cl_cancel;
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
    protected int getLayoutWidth() {
        return getResources().getDimensionPixelSize(R.dimen.equipment_list_width);
    }

    @Override
    protected int getLayoutHeight() {
        return data.size() == 1 ? getResources().getDimensionPixelSize(R.dimen.equipment_list_height1)
                : getResources().getDimensionPixelSize(R.dimen.y364);
    }

    @Override
    protected void initView(View rootView) {
        if (getArguments() != null) {
            d_title = getArguments().getString("title");
            data = getArguments().getParcelableArrayList("data");
        }
        dialog_title = rootView.findViewById(R.id.tv_function_list);
        recyclerView = rootView.findViewById(R.id.rv_function);
        cl_cancel = rootView.findViewById(R.id.cl_cancel);

        dialog_title.setText(d_title);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        recyclerView.addItemDecoration(verticalDecoration);
        recyclerView.setAdapter(adapter = new BaseQuickAdapter<EquipmentState, BaseViewHolder>(R.layout.item_dialog_equipment) {
            @Override
            protected void convert(final BaseViewHolder helper, final EquipmentState item) {
                int normal = getResources().getColor(R.color.color_16A1E1);
                int abnormal = getResources().getColor(R.color.color_B02F38);
                String normal_str = getResources().getString(R.string.device_normal);
                String abnormal_str = getResources().getString(R.string.device_abnormal);
                String error_content = item.getEquipment_error_content();

                helper.getView(R.id.loading).setVisibility(item.isEquipment_request() ? View.VISIBLE : View.GONE);
                helper.setText(R.id.tv_equipment_name, item.getEquipment_name())
                        .setText(R.id.tv_equipment_state, item.isEquipment_state() ? normal_str :
                                !TextUtils.isEmpty(error_content)? error_content : abnormal_str)
                        .setTextColor(R.id.tv_equipment_state, item.isEquipment_state() ? normal : abnormal)
                        .getView(R.id.tv_connection).setVisibility(item.isEquipment_state() ? View.GONE : View.VISIBLE);

                helper.getView(R.id.tv_connection).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(helper.getAdapterPosition() == 0 && mCallback != null) {
                            mCallback.onConnectClick();
                        }
                    }
                });
            }
        });
        if (data != null) {
            adapter.setNewData(data);
        }

        cl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "EQUIPMENT_STATE");
    }

    public void setNewData(int position, String name, boolean state) {
        EquipmentState bean = adapter.getData().get(position);
        bean.setEquipment_name(name);
        bean.setEquipment_state(state);
        bean.setEquipment_request(false);
        adapter.notifyDataSetChanged();
    }
    public void setNewData(String printer_sn, boolean state) {
        List<EquipmentState> data = adapter.getData();
        int size = data.size();
        int position = -1;
        for (int i = 0; i < size; i++) {
            EquipmentState equipmentState = data.get(i);
            if(printer_sn.equals(equipmentState.getEquipment_name())) {
                position = i;
                break;
            }
        }

        if(position == -1) {
            return;
        }

        EquipmentState bean = data.get(position);
        bean.setEquipment_name(printer_sn);
        bean.setEquipment_state(state);
        bean.setEquipment_request(false);
        adapter.notifyDataSetChanged();
    }
    public void setErrorData(int position, String name, String content) {
        EquipmentState bean = adapter.getData().get(position);
        bean.setEquipment_name(name);
        bean.setEquipment_state(false);
        bean.setEquipment_request(false);
        bean.setEquipment_error_content(content);
        adapter.notifyDataSetChanged();
    }

    public interface Callback {
        void onConnectClick();
    }
    private Callback mCallback;
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

}