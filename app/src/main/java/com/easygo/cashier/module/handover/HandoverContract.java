package com.easygo.cashier.module.handover;

import com.easygo.cashier.bean.HandoverResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.List;
import java.util.Map;


public class HandoverContract {

    public interface IPresenter extends BaseContract.Presenter {

        /**type 0：登出 1：获取交接页数据*/
        void handover(int handover_id);
        /**type 0：登出 1：获取交接页数据*/
        void loginout(int handover_id);

        void sale_list(int handover_id);

        void print_info(String shop_sn, String printer_sn, String info);

    }

    interface IView extends BaseView {
        void handoverSuccess(HandoverResponse result);
        void handoverFailed(Map<String, Object> map);

        void loginoutSuccess(String result);
        void loginoutFailed(Map<String, Object> map);

        void saleListSuccess(List<HandoverSaleResponse> result);
        void saleListFailed(Map<String, Object> map);

        void printSuccess(String result);
        void printFailed(Map<String, Object> map);

    }
}
