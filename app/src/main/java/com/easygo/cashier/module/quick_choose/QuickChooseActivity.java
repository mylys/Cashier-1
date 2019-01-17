package com.easygo.cashier.module.quick_choose;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.Events;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.QuickGoodsAdapter;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.QuickClassifyInfo;
import com.easygo.cashier.widget.MyTitleBar;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.EventUtils;
import com.niubility.library.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Describe：
 * @Date：2019-01-15
 */
@Route(path = ModulePath.quick)
public class QuickChooseActivity extends BaseMvpActivity<QuickChooseContract.View, QuickChoosePresenter> implements QuickChooseContract.View {

    @BindView(R.id.rv_classify_list)
    RecyclerView rvClassifyList;
    @BindView(R.id.rv_goods_list)
    RecyclerView rvGoodsList;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.cl_title)
    MyTitleBar clTitle;

    private List<QuickClassifyInfo> info;
    private List<GoodsResponse> selectBean = new ArrayList<>();
    private BaseQuickAdapter<QuickClassifyInfo, BaseViewHolder> classifyAdapter;
    private QuickGoodsAdapter goodsAdapter;

    /* 获取父级选择的位置 */
    private int classifyPosition = 0;

    @Override
    protected QuickChoosePresenter createPresenter() {
        return new QuickChoosePresenter();
    }

    @Override
    protected QuickChooseContract.View createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_quick;
    }

    @Override
    protected void init() {
        mPresenter.getGoodsList();

        initRvClassify();
        initRvGoodsList();
        setListener();
    }

    private void initRvGoodsList() {
        goodsAdapter = new QuickGoodsAdapter();

        rvGoodsList.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        rvGoodsList.setAdapter(goodsAdapter);
    }

    private void initRvClassify() {
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));

        rvClassifyList.setLayoutManager(new LinearLayoutManager(this));
        rvClassifyList.addItemDecoration(verticalDecoration);

        rvClassifyList.setAdapter(classifyAdapter = new BaseQuickAdapter<QuickClassifyInfo, BaseViewHolder>(R.layout.item_quick_classify) {
            @Override
            protected void convert(final BaseViewHolder helper, final QuickClassifyInfo item) {
                int black = getResources().getColor(R.color.color_505050);
                int white = getResources().getColor(R.color.color_text_white);
                int backcolor = getResources().getColor(R.color.color_51beaf);

                helper.setText(R.id.tv_classify, item.getClass_name())
                        .setTextColor(R.id.tv_classify, item.isSelect() ? white : black)
                        .getView(R.id.constraint).setBackgroundColor(item.isSelect() ? backcolor : white);

                helper.getView(R.id.constraint).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!item.isSelect()) {
                            for (QuickClassifyInfo info : getData()) {
                                info.setSelect(false);
                            }
                            item.setSelect(true);
                            goodsAdapter.setNewData(item.getGoods());
                            classifyPosition = helper.getLayoutPosition();
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    private void setListener() {
        goodsAdapter.setOnItemClickListener(new QuickGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (getSelectAll() != null) {
                    selectBean.clear();
                    selectBean.addAll(getSelectAll());
                    if (selectBean.size() == 0) {
                        btnSure.setText(getResources().getString(R.string.text_dialog_submit));
                    } else {
                        btnSure.setText(getResources().getString(R.string.text_dialog_submit) + "(" + selectBean.size() + ")");
                    }
                }
                Log.i("选择商品列表信息 === ",GsonUtils.getInstance().getGson().toJson(selectBean));
            }
        });
    }

    /* 设置adapter空数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        TextView textView = emptyView.findViewById(R.id.text);
        textView.setText("请到店铺后台设置商品");
        goodsAdapter.setEmptyView(emptyView);
    }

    @Override
    public void showGoodsListSuccess(List<QuickClassifyInfo> goodsResponses) {
        if (goodsResponses != null && goodsResponses.size() > 0) {
            info = goodsResponses;
            goodsResponses.get(0).setSelect(true);
            classifyAdapter.setNewData(goodsResponses);
            goodsAdapter.setNewData(goodsResponses.get(0).getGoods());
            setEmpty();
        }
    }

    @Override
    public void showGoodsListFailed(Map<String, Object> map) {
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_sure, R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                if (selectBean.size() == 0) {
                    showToast("请选择商品");
                    return;
                }
                EventUtils.post(Events.QUICK_CHOOSE, selectBean);
                finish();
                break;
            case R.id.cl_back:
                finish();
                break;
        }
    }

    private List<GoodsResponse> getSelectAll() {
        if (info != null) {
            if (info.size() > 0) {
                List<GoodsResponse> list = new ArrayList<>();
                for (QuickClassifyInfo item : info) {
                    for (GoodsResponse response : item.getGoods()) {
                        if (response.isSelect()) {
                            list.add(response);
                        }
                    }
                }
                return list;
            }
        }
        return null;
    }
}
