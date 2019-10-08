package com.easygo.cashier.module.goods;

import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.GiftCardResponse;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.MemberDayInfo;
import com.easygo.cashier.bean.MemberDiscountInfo;
import com.easygo.cashier.bean.MemberInfo;
import com.easygo.cashier.bean.ShopActivityResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.List;
import java.util.Map;

public class GoodsContract {

    public interface IPresenter extends BaseContract.Presenter {
        /**type 1 精确搜索， 2 模糊搜索*/
        void getGoods(String shop_id, String barcode);
        void searchGoods(String shop_sn, String barcode);

        void popTill(String shop_sn, String printer_sn);

        /**商品促销*/
        void goods_activity(String shop_sn);
        /**店铺促销*/
        void shop_activity(String shop_sn);

        void getMember(String phone,String barcode);//是否为会员
        void getMemberDay();//会员日
        void getMemberDiscount();//会员折扣

        void get_coupon(String coupon);

        void gift_card(String card_no);
        /**心跳*/
        void heartbeat();

        void tillAuth(String type,String till_password,String lock_password,String refund_password,String account,String password);
    }

    public interface IView extends BaseView {
        void getGoodsSuccess(List<GoodsResponse> result);
        void getGoodsFailed(Map<String, Object> map);

        void searchGoodsSuccess(List<GoodsResponse> result);
        void searchGoodsFailed(Map<String, Object> map);

        void popTillSuccess();
        void popTillFailed(Map<String, Object> map);

        void goodsActivitySuccess(GoodsActivityResponse result);
        void goodsActivityFailed(Map<String, Object> map);

        void shopActivitySuccess(ShopActivityResponse result);
        void shopActivityFailed(Map<String, Object> map);

        void getMemberSuccess(MemberInfo memberInfo,String barcode,String phone);
        void getMemberFailed(Map<String, Object> map,String barcode,String phone);

        void getMemberDaySuccess(List<MemberDayInfo> memberDayInfos);
        void getMemberDayFailed(Map<String,Object> map);

        void getMemberDiscountSuccess(List<MemberDiscountInfo> memberDiscountInfos);
        void getMemberDiscountFailed(Map<String,Object> map);

        void couponSuccess(CouponResponse result);
        void couponFailed(Map<String,Object> map);

        void giftCardSuccess(GiftCardResponse result);
        void giftCardFailed(Map<String,Object> map);

        void getTillAuthSuccess(String result);
        void getTillAythFailed(Map<String, Object> map);

        void getLockSuccess(String result);
        void getLockFailed(Map<String, Object> map);
    }
}
