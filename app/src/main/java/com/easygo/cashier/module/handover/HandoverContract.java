package com.easygo.cashier.module.handover;

import com.easygo.cashier.bean.HandoverResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.Map;


public class HandoverContract {

    public interface IPresenter extends BaseContract.Presenter {

        /**type 0：登出 1：获取交接页数据*/
        void handover(int handover_id);
        /**type 0：登出 1：获取交接页数据*/
        void loginout(int handover_id);

        void sale_list(int handover_id);

    }

    interface IView extends BaseView {
        void handoverSuccess(HandoverResponse result);
        void handoverFailed(Map<String, Object> map);

        void loginoutSuccess(String result);
        void loginoutFailed(Map<String, Object> map);

        void saleListSuccess(HandoverSaleResponse result);
        void saleListFailed(Map<String, Object> map);

    }
}
