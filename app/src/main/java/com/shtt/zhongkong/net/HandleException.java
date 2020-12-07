package com.shtt.zhongkong.net;

import android.app.Activity;
import android.util.Log;


public class HandleException {



    public   static   void  handle(Activity context, Object e){
//        if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException ||e instanceof MalformedJsonException) {
//            //解析错误
//
//
//        } else if (e instanceof ConnectException) {
//            //网络错误
//            ToastUtil.toast("网络错误，请稍后再试");
//
//        }
//        else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
//            //连接错误
//
//        } else if (e instanceof NoNetworkException){
//
//        }

        if (e instanceof ApiException){
           Log.e        ("code",((ApiException) e).getCode()+"~~~~~~~~~~~~");
            switch (((ApiException) e).getCode()){







            }
        }


    }





}
