package com.easygo.cashier.module.refund;

import com.easygo.cashier.bean.CheckPayStatusResponse;
import com.easygo.cashier.bean.PayResponse;
import com.niubility.library.http.RequestListener;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseModel;
import com.niubility.library.mvp.BaseRxView;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

public class RefundCashContract {

    public interface IModel extends BaseModel {

    }

    public interface IPresenter extends BaseContract.Presenter {

    }

    public interface IView extends BaseRxView {

    }
}
