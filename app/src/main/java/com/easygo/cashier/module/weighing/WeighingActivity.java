package com.easygo.cashier.module.weighing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.Events;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppMvpActivity;
import com.easygo.cashier.bean.ClassifyInfo;
import com.easygo.cashier.bean.WeightBean;
import com.easygo.cashier.printer.ZQPrint.ZQEBUtil;
import com.easygo.cashier.printer.ZQPrint.ZQPrinterUtil;
import com.easygo.cashier.widget.dialog.GeneraCashDialog;
import com.easygo.cashier.widget.dialog.LoadingDialog;
import com.easygo.cashier.widget.view.MyTitleBar;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.mvp.BasePresenter;
import com.niubility.library.mvp.BaseView;
import com.niubility.library.utils.EventUtils;
import com.niubility.library.utils.GsonUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Describe：
 * @Date：2019-06-13
 */
@Route(path = ModulePath.weight)
public class WeighingActivity extends BaseAppMvpActivity<WeighingContract.View, WeighingPresenter> implements WeighingContract.View {
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
    private DecimalFormat df;

    private GeneraCashDialog cashDialog;
    private LoadingDialog dialog;

    private String oldPrice = "";
    private String oldWeight = "";
    private String oldUnit = "";
    private String barcode = "";
    private boolean stable = false;

    @Override
    protected WeighingPresenter createPresenter() {
        return new WeighingPresenter();
    }

    @Override
    protected WeighingContract.View createView() {
        return this;
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

        mPresenter.getWeightSku();
    }

    private void setListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter quickAdapter, View view, int position) {
                ClassifyInfo item = adapter.getData().get(position);
                if (!item.isSelect()) {
                    for (ClassifyInfo info : adapter.getData()) {
                        info.setSelect(false);
                    }
                    item.setSelect(true);
                    if (weighingFragment != null) {
                        if (item.getClass_id() == 0) {
                            weighingFragment.putData(position, item.getClass_id(), adapter.getData());
                        } else {
                            weighingFragment.putData(position, item.getClass_id(), null);
                        }
                    }
                    tvPrice.setText("0/kg");
                    tvTotal.setText("￥0.00");
                    barcode = "";
                    oldPrice = "";
                    oldUnit = "";
                    oldWeight = "";
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
                                String[] strings = ZQEBUtil.getInstance().ReadData();
                                stable = strings[1].equals("稳定");
                                String newWeight = strings[0];
                                tvGoodsWeight.setText(newWeight);
                                if (!newWeight.equals(oldWeight)) {
                                    oldWeight = newWeight;
                                    setData(oldPrice, oldUnit);
                                }
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
                if (TextUtils.isEmpty(barcode)) {
                    showToast(getResources().getString(R.string.text_choose_goods));
                    return;
                }
                float weight = Float.parseFloat(tvGoodsWeight.getText().toString().replace("kg", "")) * 1000;
                if (weight <= 0) {
                    showToast(getResources().getString(R.string.text_weight_smaller_zero));
                    return;
                }
                if (!stable) {
                    showToast(getResources().getString(R.string.text_no_stable));
                    return;
                }
                readThread = false;
                Intent intent = new Intent(Events.ADD_GOODS);
                intent.putExtra("WEIGHT", (int) weight);
                intent.putExtra("BARCODE", barcode);
                sendBroadcast(intent);
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
            if (adapter.getData().size() > 0) {
                //TODO:搜索逻辑
                if (weighingFragment != null) {
                    weighingFragment.searchData(getPosition(), s.toString());
                }
            }
            ivClear.setVisibility(s.toString().length() > 0 ? View.VISIBLE : View.GONE);
        }

    }

    private int getPosition() {
        for (int i = 0, j = adapter.getData().size(); i < j; i++) {
            if (adapter.getData().get(i).isSelect()) {
                return i;
            }
        }
        return 0;
    }

    public void setData(String money, String unit) {
        if (TextUtils.isEmpty(money) || TextUtils.isEmpty(unit)) {
            return;
        }
        oldPrice = money;
        oldUnit = unit;

        if (df == null) {
            df = new DecimalFormat("#0.00");
        }
        tvPrice.setText(money + "/" + unit);

        float weight = Float.parseFloat(tvGoodsWeight.getText().toString().replace("kg", ""));
        float price = Float.parseFloat(money);
        if (unit.equals("斤")) {
            weight = weight * 2;
        }
        float total_price = weight * price;
        if ((weight * price) <= 0) {
            total_price = 0;
        }
        tvTotal.setText("￥" + df.format(total_price));
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public void getWeightSkuSuccess(WeightBean bean) {
        Log.i("TAGGGGG", GsonUtils.getInstance().getGson().toJson(bean));
        if (bean != null) {
            boolean qitaClassify = false;
            if (bean.getCate_list().size() > 0) {
                List<ClassifyInfo> list = new ArrayList<>();
                ClassifyInfo info;
                for (WeightBean.CateListBean item : bean.getCate_list()) {
                    if (item.getG_c_id() == 0) {
                        qitaClassify = true;
                        continue;
                    }
                    info = new ClassifyInfo(item.getG_c_id(), item.getG_c_name(), false);
                    list.add(info);
                }
                info = new ClassifyInfo(1, "全部", true);
                list.add(0, info);
                if (qitaClassify) {
                    info = new ClassifyInfo(0, "其他", false);
                    list.add(info);
                }
                adapter.setNewData(list);
                rvClassifyList.setVisibility(View.VISIBLE);
            } else {
                rvClassifyList.setVisibility(View.GONE);
            }
            if (weighingFragment != null) {
                weighingFragment.setData(bean.getSku_list());
            }
        }
    }

    @Override
    public void getWeightSkuFailed(Map<String, Object> map) {
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        if (dialog == null) {
            dialog = new LoadingDialog(this);
        }
        dialog.show();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
