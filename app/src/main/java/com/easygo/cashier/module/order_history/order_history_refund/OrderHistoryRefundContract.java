package com.easygo.cashier.module.order_history.order_history_refund;

import com.easygo.cashier.bean.OrderHistorysInfo;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-21
 */
public class OrderHistoryRefundContract {

    public interface IPresenter extends BaseContract.Presenter {

        void post(String json);

    }

    interface IView extends BaseView {
        void getHistoryRefundSuccess(String message);
        void getHistorfRefundFailed(Map<String, Object> map);
    }
}