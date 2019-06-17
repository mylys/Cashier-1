package com.easygo.cashier.module.weighing;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppMvpActivity;
import com.easygo.cashier.bean.ClassifyInfo;
import com.easygo.cashier.printer.ZQPrint.ZQEBUtil;
import com.easygo.cashier.printer.ZQPrint.ZQPrinterUtil;
import com.easygo.cashier.widget.dialog.GeneraCashDialog;
import com.easygo.cashier.widget.view.MyTitleBar;
import com.niubility.library.mvp.BasePresenter;
import com.niubility.library.mvp.BaseView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Describe：
 * @Date：2019-06-13
 */
@Route(path = ModulePath.weight)
public class WeighingActivity extends BaseAppMvpActivity<BaseView, BasePresenter<BaseView>> implements BaseView {
    @BindView(R.id.cl_title)
    MyTitleBar clTitle;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.rv_classify_list)
    RecyclerView rvClassifyList;
    @BindView(R.id.tv_goods_weight)
    TextView tvGoodsWeight;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    private boolean readThread = false;//是否进行读取线程
    private BaseQuickAdapter<ClassifyInfo, BaseViewHolder> adapter;
    private WeighingFragment weighingFragment;
    private GeneraCashDialog cashDialog;

    @Override
    protected BasePresenter<BaseView> createPresenter() {
        return null;
    }

    @Override
    protected BaseView createView() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weight;
    }

    @Override
    protected void init() {
        if (weighingFragment == null) {
            weighingFragment = new WeighingFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.frame_layout, weighingFragment, "tag_weight").commit();
        }

        etSearch.addTextChangedListener(new MTextWatcher());
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                ivClear.setVisibility(View.GONE);
            }
        });

        readData();

        rvClassifyList.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical));
        rvClassifyList.addItemDecoration(itemDecoration);
        rvClassifyList.setAdapter(adapter = new BaseQuickAdapter<ClassifyInfo, BaseViewHolder>(R.layout.item_quick_classify) {
            @Override
            protected void convert(BaseViewHolder helper, ClassifyInfo item) {
                helper.setText(R.id.tv_classify, item.getClass_name())
                        .setBackgroundColor(R.id.constraint, item.isSelect() ?
                                getResources().getColor(R.color.color_51beaf) :
                                getResources().getColor(R.color.color_text_white))
                        .setTextColor(R.id.tv_classify, item.isSelect() ?
                                getResources().getColor(R.color.color_text_white) :
                                getResources().getColor(R.color.color_505050));
            }
        });
        setListener();
        rvClassifyList.setVisibility(View.GONE);
    }

    private void setListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter quickAdapter, View view, int position) {
                if (!adapter.getData().get(position).isSelect()) {
                    for (ClassifyInfo info : adapter.getData()) {
                        info.setSelect(false);
                    }
                    adapter.getData().get(position).setSelect(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void readData() {
        if (ZQEBUtil.getInstance().isConnect()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    readThread = true;
                    while (readThread) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvGoodsWeight.setText(ZQEBUtil.getInstance().ReadData());
                            }
                        });
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    @OnClick({R.id.btn_set_zero, R.id.btn_set_tare, R.id.btn_clear, R.id.btn_net_weight, R.id.btn_sure, R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set_zero:
                ZQEBUtil.getInstance().zqebSetZero();
                break;
            case R.id.btn_set_tare:
                if (cashDialog == null) {
                    cashDialog = new GeneraCashDialog();
                }
                cashDialog.showCenter(this, "DIALOG_CASH");
                cashDialog.setWeight();
                cashDialog.setLimit(3);
                cashDialog.setCanInputDecimal(true);
                cashDialog.setOnDialogClickListener(new GeneraCashDialog.OnDialogClickListener() {
                    @Override
                    public void onClick(String content) {
                        if (content.length() == 0) {
                            showToast(getResources().getString(R.string.input_set_tare));
                            return;
                        }
                        ZQEBUtil.getInstance().zqebSetTare(Double.parseDouble(content));
                        cashDialog.dismiss();
                        cashDialog.setEditText("");
                    }
                });
                break;
            case R.id.btn_clear:
                ZQEBUtil.getInstance().zqebClearTare();
                break;
            case R.id.btn_net_weight:
                ZQEBUtil.getInstance().zqebNetWeight();
                break;
            case R.id.btn_sure:
                readThread = false;
                finish();
                break;
            case R.id.cl_back:
                readThread = false;
                finish();
                break;
        }
    }

    private class MTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 0 && adapter.getData().size() > 0) {
                //TODO:搜索逻辑
                ivClear.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
