package com.easygo.cashier.http;

import android.util.Log;

import com.easygo.cashier.Configs;
import com.niubility.library.common.config.BaseConfig;
import com.niubility.library.http.base.BaseHttpAPI;
import com.niubility.library.http.base.BaseRetrofit;

import java.util.ArrayList;
import java.util.List;

public class HttpAPI extends BaseHttpAPI {

    public static HttpAPI getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        static final HttpAPI sInstance = new HttpAPI();
    }

    public void reset() {
        init();
        if (httpServiceCashierList != null) {
            httpServiceCashierList.clear();
            httpServiceCashierList = null;
        }
        if (httpServiceOfflineList != null) {
            httpServiceOfflineList.clear();
            httpServiceOfflineList = null;
        }
    }


    private List<HttpService> httpServiceCashierList;
    private List<HttpService> httpServiceOfflineList;
    private HttpService httpService;

    public HttpService httpService() {

        if(Configs.isOnlineMode()) {
            if (httpServiceCashierList == null) {
                httpServiceCashierList = new ArrayList<>();
                for (int i = 0; i < BaseConfig.environment_count; i++) {
                    httpServiceCashierList.add(i, null);
                }
            }
            httpService = httpServiceCashierList.get(BaseConfig.environment_index);
            Log.i("test", "httpService: 在线模式- " + httpService);
            if (httpService == null) {
                httpService = BaseRetrofit.getInstance()
                        .getCurrentEnvRetrofit(array_key_url[0])
                        .create(HttpService.class);

                httpServiceCashierList.set(BaseConfig.environment_index, httpService);
            }

        } else {
            if (httpServiceOfflineList == null) {
                httpServiceOfflineList = new ArrayList<>();
                for (int i = 0; i < BaseConfig.environment_count; i++) {
                    httpServiceOfflineList.add(i, null);
                }
            }
            httpService = httpServiceOfflineList.get(BaseConfig.environment_index);
            Log.i("test", "httpService: 离线模式- " + httpService);
            if (httpService == null) {
                httpService = BaseRetrofit.getInstance()
                        .getCurrentEnvRetrofit(array_key_url[1])
                        .create(HttpService.class);

                httpServiceOfflineList.set(BaseConfig.environment_index, httpService);
            }
        }

        return httpService;

    }


    @Override
    protected String[] getUrlKeyArray() {
        return new String[] {
                "KEY_URL_CASHIER",
                "KEY_URL_OFFLINE"
        };
    }

    @Override
    protected String[][] getUrlArray() {

        String[] array_url_cashier = new String[] {
                "https://api.pos.esgao.cn/", //正式
                "http://api.staging.pos.esgao.cn/", //预发布
                "http://api.dev.pos.esgao.cn/", //测试
        };

        String[] array_url_offline = new String[] {
                "http://" + BaseConfig.environment_ip + "/",
                "http://" + BaseConfig.environment_ip + "/",
                "http://" + BaseConfig.environment_ip + "/",
        };

        return new String[][]{
                array_url_cashier,
                array_url_offline
        };
    }

}
