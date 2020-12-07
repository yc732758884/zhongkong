package com.shtt.zhongkong.net;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class CustomException {

    /**
     * 成功
     */
    public static final int SUCCESS = 0;


    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 协议错误
     */
    public static final int HTTP_ERROR = 1003;


    /**
     * w未打开网络
     */
    public static final int No_NETWORK = 1004;


    public  static  final int TOKEN_OUTTIME=95598;

    public   static  final int TOKEN=95598;











    public static ApiException handleException(Throwable e) {


        ApiException ex;
        if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException ||e instanceof MalformedJsonException) {
            //解析错误
            ex = new ApiException(PARSE_ERROR, e.getMessage());

        } else if (e instanceof ConnectException) {
            //网络错误
            ex = new ApiException(NETWORK_ERROR, e.getMessage());

        }
        else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            //连接错误
            ex = new ApiException(NETWORK_ERROR, e.getMessage());

        } else if (e instanceof NoNetworkException){
            ex = new ApiException(No_NETWORK, e.getMessage());
        } else {
            //未知错误
            ex = new ApiException(UNKNOWN, e.getMessage());

        }



        return ex;

    }
}