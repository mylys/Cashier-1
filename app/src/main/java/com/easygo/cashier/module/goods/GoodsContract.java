package com.easygo.cashier.module.goods;

import com.easygo.cashier.bean.CheckPayStatusResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BasePresenter;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

public class GoodsContract {

//    public interface IModel extends BaseModel {
//        void getGoods(String shop_id, String barcode, RequestListener<GoodsResponse> listener);
//        void searchGoods(String shop_id, String barcode, String good_name, String code, RequestListener<GoodsResponse> listener);
//
//        void createOrder(String json, RequestListener<String> listener);
//        void realMoney(String json, RequestListener<RealMoneyResponse> listener);
//    }

    public interface IPresenter extends BaseContract.Presenter {
        void getGoods(String shop_id, String barcode);
        void searchGoods(String shop_id, String barcode, String good_name);

        void createOrder(String json);
        void checkPayStatus(String order_sn);
        void realMoney(String json);

    }

    interface IView extends BaseView {
        void getGoodsSuccess(GoodsResponse result);
        void getGoodsFailed(Map<String, Object> map);

        void searchGoodsSuccess(GoodsResponse result);
        void searchGoodsFailed(Map<String, Object> map);

        void createOrderSuccess(String order_sn);
        void createOrderFailed(Map<String, Object> map);

        void realMoneySuccess(RealMoneyResponse result);
        void realMoneyFailed(Map<String, Object> map);

        void checkPayStatusSuccess(CheckPayStatusResponse result);
        void checkPayStatusFailed(Map<String, Object> map);



    }
}
