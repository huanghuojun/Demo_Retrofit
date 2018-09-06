package com.spencer.retrofit.demo.retrofit;

import com.spencer.retrofit.demo.http.ServiceMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * @description: 自己动手写Retrofit实现
 * @author: huanghuojun
 * @date: 2018/8/30 20:43
 */
public class Retrofit {

    private HttpUrl baseUrl;
    private Call.Factory factory;
    private Map<Method, ServiceMethod> mMethodServiceMethodMap = new HashMap<>();

    public Retrofit(Bundler bundler) {
        this.baseUrl = bundler.baseUrl;
        this.factory = bundler.factory;
    }

    public HttpUrl getBaseUrl() {
        return baseUrl;
    }

    public Call.Factory getFactory() {
        return factory;
    }

    /**
     * 构建者模式
     */
    public static final class Bundler{

        private HttpUrl baseUrl;
        private Call.Factory factory;

        public Bundler baseUrl(String baseUrl){
            this.baseUrl = HttpUrl.parse(baseUrl);
            return this;
        }
        
        public Bundler callFctory(Call.Factory factory){
            this.factory = factory;
            return this;
        }

        public Retrofit builder(){
            if(baseUrl == null)
                throw new IllegalArgumentException("The baseUrl cannot be empty");
            if (factory == null)
                factory = new OkHttpClient();

            return new Retrofit(this);
        }
    }


    public <T> T create(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                /**
                 * 实现网络请求
                 * 1.采集数据（考虑缓存）
                 * 1.1 查找缓存
                 */
                ServiceMethod serviceMethod = loadServiceMethod(method);
                Call call = serviceMethod.toCall(args);
                return call;
            }
        });
    }

    /**
     * 查找缓存Map，不存在通过构建者模式创建
     * @param method
     * @return
     */
    private ServiceMethod loadServiceMethod(Method method){
        ServiceMethod serviceMethod = mMethodServiceMethodMap.get(method);
        if(serviceMethod == null){
            serviceMethod = new ServiceMethod.Builder(this, method).builder();
            mMethodServiceMethodMap.put(method, serviceMethod);
        }

        return serviceMethod;
    }
}
