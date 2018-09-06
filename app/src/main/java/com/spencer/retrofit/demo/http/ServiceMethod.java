package com.spencer.retrofit.demo.http;

import com.spencer.retrofit.demo.annotation.Field;
import com.spencer.retrofit.demo.annotation.GET;
import com.spencer.retrofit.demo.annotation.POST;
import com.spencer.retrofit.demo.annotation.Query;
import com.spencer.retrofit.demo.retrofit.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @description: 采集数据的缓存类
 * @author: huanghuojun
 * @date: 2018/9/6 16:03
 */
public class ServiceMethod {
    private Call.Factory factory;
    private ParameterHandler[] mParameterHandlers;
    private String httpMethod;
    private String relativeUrl;
    private HttpUrl baseUrl;
    private HttpUrl.Builder urlBuilder;
    private FormBody.Builder mFormBodyBuilder;

    public ServiceMethod(Builder builder) {
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.mParameterHandlers = builder.mParameterHandlers;
        this.baseUrl = builder.retrofit.getBaseUrl();
        this.factory = builder.retrofit.getFactory();
    }

    public Call toCall(Object[] args) {
        //创建Request
        Request.Builder requestBuilder = new Request.Builder();
        //设置地址
        if (urlBuilder == null){
            urlBuilder = baseUrl.newBuilder(relativeUrl);
        }
        //如果Get请求，拼接url地址
        for (int i = 0; i < mParameterHandlers.length; i++) {
            mParameterHandlers[i].apply(this, String.valueOf(args[i]));
        }
        FormBody formBody = null;
        if (mFormBodyBuilder != null)
            formBody = mFormBodyBuilder.build();
        //设置url
        requestBuilder.url(urlBuilder.build());

        Request request = requestBuilder.method(httpMethod, formBody).build();
        //创建Call
        return factory.newCall(request);
    }

    public void addQueryParamnter(String name, String value) {
        urlBuilder.addQueryParameter(name, value);
    }

    public void addFormParamnter(String name, String value) {
        if (mFormBodyBuilder == null)
            mFormBodyBuilder = new FormBody.Builder();
        mFormBodyBuilder.add(name, value);
    }

    public static final class Builder{

        Annotation[] mMethodAnnotations;
        Annotation[][] mParameterAnnotations;
        ParameterHandler[] mParameterHandlers;
        private String httpMethod;
        private String relativeUrl;
        private Retrofit retrofit;

        public Builder(Retrofit retrofit, Method method){
            this.retrofit = retrofit;
            //真正的采集数据
            //获取方法的注解
            mMethodAnnotations = method.getAnnotations();
            //获取参数的注解
            //从语法上来说一个参数可以被多个注解声明
            mParameterAnnotations = method.getParameterAnnotations();
        }

        public ServiceMethod builder(){
            //处理方法注解
            for(Annotation annotation : mMethodAnnotations){
                parseMethodAnnotation(annotation);
            }

            mParameterHandlers = new ParameterHandler[mParameterAnnotations.length];

            for (int i = 0; i < mParameterHandlers.length; i++) {
                Annotation[] annotations = mParameterAnnotations[i];
                //遍历一个参数上的一个注解
                for (Annotation annotation: annotations ) {
                    if(annotation instanceof Query){
                        Query query = (Query) annotation;
                        mParameterHandlers[i] = new ParameterHandler.Query(query.value());
                    }else if (annotation instanceof Field){
                        Field field = (Field) annotation;
                        mParameterHandlers[i] = new ParameterHandler.Field(field.value());
                    }
                }
            }
            return new ServiceMethod(this);
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if(annotation instanceof GET){
                httpMethod = "GET";
                //获取GET注解中的value
                GET get = (GET) annotation;
                relativeUrl = get.value();
            }else if (annotation instanceof POST){
                httpMethod = "POST";
                //获取POST注解中的value
                POST get = (POST) annotation;
                relativeUrl = get.value();
            }
        }
    }
}
