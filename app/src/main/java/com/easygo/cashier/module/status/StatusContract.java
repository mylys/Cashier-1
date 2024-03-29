package com.easygo.cashier.module.status;

import com.easygo.cashier.bean.PrinterStatusResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

public class StatusContract {


    public interface IPresenter extends BaseContract.Presenter {
        void printerStatus(String shop_sn, String printer_sn);
    }

    public interface IView extends BaseView {

        void printerStatusSuccess(PrinterStatusResponse result);
        void printerStatusFailed(Map<String, Object> map);
    }
}
