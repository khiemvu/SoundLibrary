package com.oman.allinone.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class NetworkUtils {
    private static OkHttpClient client;

    public static void init() {
        client = new OkHttpClient();
        client.setConnectTimeout(50000, TimeUnit.SECONDS);
    }

    public static OkHttpClient getHttpClientInstance() {
        if (client == null) {
            init();
        }
        return client;
    }

    public static Response executeGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return getHttpClientInstance().newCall(request).execute();
    }
}