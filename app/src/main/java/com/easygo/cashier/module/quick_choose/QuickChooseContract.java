package com.easygo.cashier.module.quick_choose;

import com.easygo.cashier.bean.QuickInfo;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

/**
 * @Describe：
 * @Date：2019-01-15
 */
public class QuickChooseContract {
    public interface Presenter extends BaseContract.Presenter {
        void getGoodsList();
    }

    public interface View extends BaseView {
        void showGoodsListSuccess(QuickInfo goodsResponses);
        void showGoodsListFailed(Map<String, Object> map);
    }
}
