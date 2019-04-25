package com.easygo.cashier.module.quick_choose;

import com.easygo.cashier.bean.QuickInfo;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.Map;

/**
 * @Describe：
 * @Date：2019-01-15
 */
public class QuickChoosePresenter extends BasePresenter<QuickChooseContract.View> implements QuickChooseContract.Presenter {

    @Override
    public void getGoodsList() {
        mView.showLoading();
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().showLists(header),
                new BaseResultObserver<QuickInfo>() {
            @Override
            protected void onSuccess(QuickInfo result) {
                mView.hideLoading();
                mView.showGoodsListSuccess(result);
            }

            @Override
            protected void onFailure(Map<String, Object> map) {
                mView.hideLoading();
                mView.showGoodsListFailed(map);
            }
        });
    }
}
