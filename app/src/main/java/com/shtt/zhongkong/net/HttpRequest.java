package com.shtt.zhongkong.net;


import com.shtt.zhongkong.StatusBean;
import com.wangce.Base.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface HttpRequest {

    public final static String HOST = "http://192.168.1.12:9090/";




     //开关控制
    @GET("switchVideo")
    Observable<BaseResponse<String>> CloseOrOpen(@Query("isSwitch") boolean open,@Query("code") int code,@Header("urlname") String head);

    //状态获取

    @GET("getVideoStatus")
    Observable<BaseResponse<StatusBean>> getStatus(@Query("code") int code,@Header("urlname") String head);

    // 关机

    @GET("shutdown")
    Observable<BaseResponse<String>> shutdown( @Header("urlname") String head);

    //dengkong
    @GET("lightControl")
    Observable<BaseResponse<String>> lightControl(@Query("isOpen") boolean open, @Header("urlname") String head);


   //deng kong   chaxun

    @GET("getLightStatus")
    Observable<BaseResponse<StatusBean>> getLightStatus( @Header("urlname") String head);


    //废固处理
    @GET("getCurrentVersion")
    Observable<BaseResponse<StatusBean>> getCurrentVersion( @Header("urlname") String head);

    //
    @GET("radarSwitch/version")
    Observable<BaseResponse<StatusBean>> radarSwitch( @Query("flag") boolean flag,@Header("urlname") String head);

}