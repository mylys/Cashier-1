package com.easygo.cashier.module.quick_choose;

import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.QuickClassifyInfo;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.List;
import java.util.Map;

/**
 * @Describe：
 * @Date：2019-01-15
 */
public class QuickChoosePresenter extends BasePresenter<QuickChooseContract.View> implements QuickChooseContract.Presenter {

    @Override
    public void getGoodsList() {
        Map<String, String> header = HttpClient.getInstance().getHeaders();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().showLists(header),
                new BaseResultObserver<List<QuickClassifyInfo>>() {
            @Override
            protected void onSuccess(List<QuickClassifyInfo> result) {
                mView.showGoodsListSuccess(result);
            }

            @Override
            protected void onFailure(Map<String, Object> map) {
                mView.showGoodsListFailed(map);
            }
        });
    }
}
