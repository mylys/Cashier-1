package com.easygo.cashier.module.goods;

import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

public class GoodsContract {

    public interface IPresenter extends BaseContract.Presenter {
        void getGoods(String shop_id, String barcode);
        void searchGoods(String shop_id, String barcode, String good_name);

        void realMoney(String json);

        void popTill(String shop_sn, String printer_sn);

    }

    interface IView extends BaseView {
        void getGoodsSuccess(GoodsResponse result);
        void getGoodsFailed(Map<String, Object> map);

        void searchGoodsSuccess(GoodsResponse result);
        void searchGoodsFailed(Map<String, Object> map);

        void realMoneySuccess(RealMoneyResponse result);
        void realMoneyFailed(Map<String, Object> map);

        void popTillSuccess();
        void popTillFailed(Map<String, Object> map);


    }
}
