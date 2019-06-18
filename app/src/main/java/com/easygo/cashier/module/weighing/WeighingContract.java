package com.easygo.cashier.module.weighing;

import com.easygo.cashier.bean.WeightBean;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

/**
 * @Describe：
 * @Date：2019-06-18
 */
public class WeighingContract {

    interface View extends BaseView {
        void getWeightSkuSuccess(WeightBean bean);
        void getWeightSkuFailed(Map<String, Object> map);
    }

    interface Presenter extends BaseContract.Presenter {
        void getWeightSku();
    }
}
