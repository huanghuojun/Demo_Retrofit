package com.spencer.retrofit.demo.api;

import com.spencer.retrofit.demo.annotation.Field;
import com.spencer.retrofit.demo.annotation.GET;
import com.spencer.retrofit.demo.annotation.POST;
import com.spencer.retrofit.demo.annotation.Query;

import okhttp3.Call;

/**
 * @description:
 * @author: huanghuojun
 * @date: 2018/9/6 15:56
 */
public interface Person {

    @GET("/weather/index")
    Call get(@Query("cityName") String cityName, @Query("key") String key);

    @POST("/weather/index")
    Call post(@Field("cityName") String cityName, @Field("key") String key);
}
