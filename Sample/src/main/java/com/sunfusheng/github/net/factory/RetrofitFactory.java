package com.sunfusheng.github.net.factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class RetrofitFactory {

    public static Retrofit create(OkHttpClient client, String baseUrl, boolean isJson) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(client);
        builder.baseUrl(baseUrl);
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        if (isJson) {
            builder.addConverterFactory(GsonConverterFactory.create());
        } else {
            builder.addConverterFactory(SimpleXmlConverterFactory.create());
        }
        return builder.build();
    }
}
