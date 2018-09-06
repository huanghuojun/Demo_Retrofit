package com.spencer.retrofit.demo;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @description: 动态代理测试
 * @author: huanghuojun
 * @date: 2018/9/6 15:13
 */
public class ProxyTest {

    interface User{
        void login(String username, String password);
    }
    @Test
    public void addition_isCorrect() {
        User user = (User) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{User.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName());
                System.out.println(Arrays.toString(args));
                return null;
            }
        });
        user.login("张三", "123456");

    }
}