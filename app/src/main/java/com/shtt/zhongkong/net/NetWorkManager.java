package com.shtt.zhongkong.net;

import android.content.Context;
import android.util.Log;

import com.shtt.zhongkong.NetBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetWorkManager {


    private static NetWorkManager mInstance;
    private static Retrofit retrofit;

    private static volatile HttpRequest request = null;

    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {

        HashMap map = new HashMap<String, String>();


        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {

                Log.e("NET", "OkHttp====Message:" + message);

            }
        });
        loggingInterceptor.setLevel(level);


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BaseUrlInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();


        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(HttpRequest.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    public static HttpRequest getRequest() {
        if (request == null) {
            synchronized (Request.class) {
                request = retrofit.create(HttpRequest.class);
            }
        }
        return request;
    }

    public class BaseUrlInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            NetBean nb = NetBean.getIntanse();
            Request request = chain.request();
            HttpUrl oldHttpUrl = request.url();

            Request.Builder builder = request.newBuilder();
            List<String> headerValues = request.headers("urlname");
            if (headerValues != null && headerValues.size() > 0) {

                builder.removeHeader("urlname");
                String headerValue = headerValues.get(0);
                Log.e("ssssssss",headerValue);

                HttpUrl newBaseUrl = null;

                switch (headerValue) {
                    case "baseUrl":
                        newBaseUrl = HttpUrl.parse(nb.getBaseUrl());

                        break;

                    case "xhjj":
                        newBaseUrl = HttpUrl.parse(nb.getXhjj());
                        break;

                    case "xhjjcyl":
                        newBaseUrl = HttpUrl.parse(nb.getXhjjcyl());
                        break;

                    case "zss":
                        newBaseUrl = HttpUrl.parse(nb.getZss());
                        break;

                    case "czws":
                        newBaseUrl = HttpUrl.parse(nb.getCzws());
                        break;

                    case "gyws":
                        newBaseUrl = HttpUrl.parse(nb.getGyws());
                        break;

                    case "fgcl":
                        newBaseUrl = HttpUrl.parse(nb.getFgcl());
                        break;

                    case "xhzl":
                        newBaseUrl = HttpUrl.parse(nb.getXhzl());
                        break;

                    default:

                        newBaseUrl = oldHttpUrl;
                        break;

                }

                HttpUrl newFullUrl = oldHttpUrl
                        .newBuilder()
                        .scheme("http")
                        .host(newBaseUrl.host())
                        .port(newBaseUrl.port())

                        .build();

                return chain.proceed(builder.url(newFullUrl).build());


            }
            return chain.proceed(request);

        }
    }


}