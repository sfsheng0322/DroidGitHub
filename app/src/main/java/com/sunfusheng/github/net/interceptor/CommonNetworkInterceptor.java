package com.sunfusheng.github.net.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.response.ResponseData;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class CommonNetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        @FetchMode int fetchMode = CommonInterceptor.getFetchMode(request);
        Response response = chain.proceed(request);
        Log.w("sfs", "BaseNetworkInterceptor url: " + request.url().toString() + " fetchMode: " + ResponseData.getFetchModeString(fetchMode));

        Log.d("sfs", "CommonInterceptor.getCacheControl(fetchMode): " + CommonInterceptor.getCacheControl(fetchMode));
        return response.newBuilder()
                .header("Cache-Control", CommonInterceptor.getCacheControl(fetchMode))
                .removeHeader("Pragma")
                .build();
    }
}