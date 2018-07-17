package com.sunfusheng.github.net.interceptor;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Logger.d("log-api-request: " + request.url().toString());

        Response response = chain.proceed(request);
        if (response == null || response.body() == null) {
            return response;
        }

        MediaType contentType = response.body().contentType();
        String content = response.body().string();
        Logger.d("log-api-response: " + content);

        return response.newBuilder()
                .body(ResponseBody.create(contentType, content))
                .build();
    }
}
