package com.easygo.cashier.module.weighing;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.WeightAdapter;
import com.easygo.cashier.base.BaseAppFragment;
import com.easygo.cashier.bean.ClassifyInfo;
import com.easygo.cashier.bean.WeightBean;
import com.easygo.cashier.module.order_history.OrderHistoryActivity;
import com.easygo.cashier.widget.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe：
 * @Date：2019-06-14
 */
public class WeighingFragment extends BaseAppFragment {
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    Unbinder unbinder;

    private List<WeightBean.SkuListBean> beans;
    private ArrayMap<Integer, List<WeightBean.SkuListBean>> map = new ArrayMap<>();

    private WeightAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_weight;
    }

    @Override
    protected void initView() {
        adapter = new WeightAdapter();
        rvGoods.setLayoutManager(new GridLayoutManager(getActivity(), 4, LinearLayoutManager.VERTICAL, false));
        rvGoods.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter quickAdapter, View view, int position) {
                if (!adapter.getData().get(position).isSelect()) {
                    for (WeightBean.SkuListBean item : adapter.getData()) {
                        item.setSelect(false);
                    }
                    adapter.getData().get(position).setSelect(true);
                    adapter.notifyDataSetChanged();
                    ((WeighingActivity) getActivity()).setData(
                            adapter.getData().get(position).getPrice(),
                            adapter.getData().get(position).getG_u_name());
                    ((WeighingActivity) getActivity()).setBarcode(
                            adapter.getData().get(position).getBarcode());
                }
            }
        });
    }

    /**
     * 根据分类选择位置，判断是否该分类已存在map中，如果没有则排除分类id，添加新的数据，如果有则显示
     *
     * @param position
     * @param id
     */
    public void putData(int position, int id, List<ClassifyInfo> classifyList) {
        if (!map.keySet().contains(position)) {
            List<WeightBean.SkuListBean> list = new ArrayList<>(beans);
            if (classifyList != null) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    for (ClassifyInfo info : classifyList) {
                        if (list.get(i).getG_c_id() == info.getClass_id()) {
                            list.remove(i);
                            break;
                        }
                    }
                }
            } else {
                for (Iterator<WeightBean.SkuListBean> it = list.iterator(); it.hasNext(); ) {
                    if (it.next().getG_c_id() != id) {
                        it.remove();
                    }
                }
            }
            map.put(position, list);
            adapter.setNewData(list);
        } else {
            List<WeightBean.SkuListBean> listBeans = map.get(position);
            assert listBeans != null;
            for (WeightBean.SkuListBean item : listBeans) {
                item.setSelect(false);
            }
            adapter.setNewData(listBeans);
        }
        setEmpty();
    }

    /**
     * 设置全部数据
     *
     * @param beans
     */
    public void setData(List<WeightBean.SkuListBean> beans) {
        this.beans = beans;
        if (beans.size() > 0) {
            adapter.setNewData(beans);
            map.put(0, beans);//存储全部的list
        } else {
            setEmpty();
        }
    }

    /**
     * 根据选中的分类位置进行搜索，映射key值得到列表
     *
     * @param position
     */
    public void searchData(int position, String content) {
        List<WeightBean.SkuListBean> listBeans = map.get(position);
        if (content.length() == 0) {
            adapter.setNewData(listBeans);
            return;
        }
        List<WeightBean.SkuListBean> list = new ArrayList<>(listBeans);
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!list.get(i).getG_sku_name().contains(content) && !list.get(i).getBarcode().contains(content)) {
                list.remove(i);
            }
        }
        adapter.setNewData(list);
        setEmpty();
    }

    /* 设置adapter空数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        TextView textView = emptyView.findViewById(R.id.text);
        textView.setText("暂无商品");
        adapter.setEmptyView(emptyView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
