package com.easygo.cashier.widget;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsResponseAdapter;
import com.easygo.cashier.bean.GoodsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果显示弹窗
 */
public class SearchResultWindow extends PopupWindow {

    private RecyclerView rvSearchResult;
    private GoodsResponseAdapter searchResultAdapter;

    public SearchResultWindow(Context context) {
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_search_result, null, false);
        setContentView(view);


        rvSearchResult = ((RecyclerView) view.findViewById(R.id.rv_search_result));

        searchResultAdapter = new GoodsResponseAdapter();

        rvSearchResult.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                int count = state.getItemCount();

                if(searchResultAdapter.getEmptyViewCount() == 1) {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                    return;
                }

                if (count > 0) {
                    if(count > 4){
                        count = 4;
                    }
                    int realHeight = 0;
                    int realWidth = 0;
                    for(int i = 0;i < count; i++){
                        View view = recycler.getViewForPosition(0);
                        if (view != null) {
                            measureChild(view, widthSpec, heightSpec);
                            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                            int measuredHeight = view.getMeasuredHeight();
                            realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
                            realHeight += measuredHeight;
                        }
                        setMeasuredDimension(realWidth, realHeight);
                    }
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });


        searchResultAdapter.bindToRecyclerView(rvSearchResult);
//        rvSearchResult.setAdapter(searchResultAdapter);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(context.getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        rvSearchResult.addItemDecoration(verticalDecoration);

        searchResultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mListener != null) {
                    List data = adapter.getData();
                    GoodsResponse self = (GoodsResponse) data.get(position);
                    List<GoodsResponse> result = new ArrayList<>();
                    result.add(self);

                    GoodsResponse goodsResponse;
                    if(self.getParent_id() == 0) {//加工商品
                        //遍历寻找此商品的加工商品
                        int size = data.size();
                        for (int i = 0; i < size; i++) {
                            if(i == position)
                                continue;

                            goodsResponse = (GoodsResponse) data.get(i);
                            if(goodsResponse.getParent_id() != 0
                                    && goodsResponse.getG_sku_name().startsWith(self.getG_sku_name())) {
                                //商品开头一样，则判断为 此商品的加工商品
                                result.add(goodsResponse);
                            }
                        }
                    }

                    mListener.onSearchResultClicked(result);
                }
            }
        });

        setEmpty(context);


    }

    /* 设置adapter空数据 */
    private void setEmpty(Context context) {
        View emptyView = LayoutInflater.from(context).inflate(R.layout.item_empty_view, null, false);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        TextView textView = emptyView.findViewById(R.id.text);
        textView.setText(context.getResources().getString(R.string.search_null));
        searchResultAdapter.setEmptyView(emptyView);
    }

    public void setData(List<GoodsResponse> data) {
        searchResultAdapter.setNewData(data);
    }

    public OnSearchResultClickListener mListener;
    public void setOnSearchResultClickListener(OnSearchResultClickListener listener) {
        this.mListener = listener;
    }

    public interface OnSearchResultClickListener {
        void onSearchResultClicked(List<GoodsResponse> list);
    }


}
