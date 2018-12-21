package com.easygo.cashier.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsResponseAdapter;
import com.easygo.cashier.bean.GoodsResponse;

import java.util.ArrayList;
import java.util.List;

public class ProcessingChoiceDialog extends Dialog {

    private RecyclerView rvProcessingChoice;
    private GoodsResponseAdapter adapter;

    public ProcessingChoiceDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }


    private void init() {
        setContentView(R.layout.layout_processing_choice);



        rvProcessingChoice = ((RecyclerView) findViewById(R.id.rv_processing_choice));

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


}
