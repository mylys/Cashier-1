package com.easygo.cashier.module.refund;

import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseModel;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

public class RefundCashContract {

    public interface IModel extends BaseModel {

    }

    public interface IPresenter extends BaseContract.Presenter {
        void cashRefund(String json);
        void print_info(String shop_sn, String printer_sn, String info);
    }

    public interface IView extends BaseView {
        void cashRefundSuccess(String result);
        void cashRefundFailed(Map<String, Object> map);

        void printSuccess(String result);
        void printFailed(Map<String, Object> map);
    }
}
