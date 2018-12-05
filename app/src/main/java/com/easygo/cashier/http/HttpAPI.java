package com.easygo.cashier.http;

import com.easygo.cashier.BuildConfig;
import com.niubility.library.http.API;
import com.niubility.library.http.base.BaseRetrofit;

public class HttpAPI extends API {

    private HttpService httpService;

    public HttpService httpService() {
        if(httpService == null) {
            httpService = BaseRetrofit.getInstance()
                    .getRetrofit(BuildConfig.API_URL)
                    .create(HttpService.class);
        }
        return httpService;
    }



    public static HttpAPI getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static HttpAPI sInstance = new HttpAPI();
    }

}
