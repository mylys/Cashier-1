package com.easygo.cashier.module.order_history;

import com.easygo.cashier.bean.OrderHistorysInfo;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.List;
import java.util.Map;

public class OrderHistoryContract {

//    interface IModel extends BaseModel {
//
//    }

    public interface IPresenter extends BaseContract.Presenter {

        void post(int handover_id, String keyword, int page, int count);
        void print_info(String shop_sn, String printer_sn, String info, int type);
    }

    interface IView extends BaseView {
        void getHistoryInfo(List<OrderHistorysInfo> orderHistorysInfo);
        void getHistorfFailed(Map<String, Object> map);

        void printSuccess(String result);
        void printFailed(Map<String, Object> map);
    }
}
