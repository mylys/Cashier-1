package com.easygo.cashier.http;

import android.util.ArrayMap;

import com.easygo.cashier.BuildConfig;
import com.easygo.cashier.Configs;
import com.niubility.library.http.API;
import com.niubility.library.http.base.BaseRetrofit;

public class HttpAPI extends API {


    public final String[] domain = new String[]{
            "https://api.pos.esgao.cn/",
            "http://api.staging.pos.esgao.cn/",
            "http://api.dev.pos.esgao.cn/",
            "http://192.168.31.120:8071/",
    };

    protected ArrayMap<Integer, HttpService> serviceArrayMap = new ArrayMap<>(domain.length);



    private HttpService httpService;

    public HttpService httpService() {

        httpService = serviceArrayMap.get(Configs.environment_index);
        if(httpService == null) {
            httpService = BaseRetrofit.getInstance()
                    .getRetrofit(domain[Configs.environment_index])
                    .create(HttpService.class);
            serviceArrayMap.put(Configs.environment_index, httpService);
        }
        return httpService;

//        if(this.httpService == null) {
//            this.httpService = BaseRetrofit.getInstance()
////                    .getRetrofit(BuildConfig.API_URL)
//                    .getRetrofit("http://192.168.31.120:8071/")
//                    .create(HttpService.class);
//        }
//        return this.httpService;
    }



    public static HttpAPI getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static HttpAPI sInstance = new HttpAPI();
    }

}
