package com.easygo.cashier.module.status;

import com.easygo.cashier.bean.PrinterStatusResponse;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.Map;

public class StatusPresenter extends BasePresenter<StatusContract.IView> implements StatusContract.IPresenter{

    @Override
    public void printerStatus(String shop_sn, final String printer_sn) {

        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().printer_status(header, shop_sn, printer_sn),
                new BaseResultObserver<PrinterStatusResponse>() {

                    @Override
                    protected void onSuccess(PrinterStatusResponse result) {
                        result.setPrinter_sn(printer_sn);
                        mView.printerStatusSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.printerStatusFailed(map);
                    }
                });
    }
}
