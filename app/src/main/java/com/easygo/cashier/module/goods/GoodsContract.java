package com.easygo.cashier.module.goods;

import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.List;
import java.util.Map;

public class GoodsContract {

    public interface IPresenter extends BaseContract.Presenter {
        /**type 1 精确搜索， 2 模糊搜索*/
        void getGoods(String shop_id, String barcode);
        void searchGoods(String shop_sn, String barcode);

        void realMoney(String json);

        void popTill(String shop_sn, String printer_sn);

    }

    public interface IView extends BaseView {
        void getGoodsSuccess(List<GoodsResponse> result);
        void getGoodsFailed(Map<String, Object> map);

        void searchGoodsSuccess(List<GoodsResponse> result);
        void searchGoodsFailed(Map<String, Object> map);

        void realMoneySuccess(RealMoneyResponse result);
        void realMoneyFailed(Map<String, Object> map);

        void popTillSuccess();
        void popTillFailed(Map<String, Object> map);


    }
}
