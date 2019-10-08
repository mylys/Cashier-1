package com.easygo.cashier.widget.dialog;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsResponseAdapter;
import com.easygo.cashier.bean.GoodsResponse;

import java.util.List;

public class ProcessingChoiceDialog extends MyBaseDialog {

    private RecyclerView rvProcessingChoice;
    private GoodsResponseAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_FRAME, R.style.CustomDialogStyle);
    }

    @Override
    protected void initView(View rootView) {
        rvProcessingChoice = rootView.findViewById(R.id.rv_processing_choice);

        adapter = new GoodsResponseAdapter(R.layout.item_processing_choice);

        rvProcessingChoice.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvProcessingChoice.setAdapter(adapter);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        rvProcessingChoice.addItemDecoration(verticalDecoration);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mListener != null) {
                    List data = adapter.getData();
                    GoodsResponse self = (GoodsResponse) data.get(position);
                    mListener.onItemClicked(self);

                    dismiss();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_processing_choice;
    }

    @Override
    protected int getLayoutWidth() {
        return getResources().getDimensionPixelSize(R.dimen.x600);
    }

    @Override
    protected int getLayoutHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public void setData(List<GoodsResponse> data) {
        adapter.setNewData(data);
    }

    public OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClicked(GoodsResponse result);
    }

    @Override
    protected boolean canCanceledOnTouchOutside() {
        return true;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "TAG_PROCESSING_CHOICE");
    }

}
