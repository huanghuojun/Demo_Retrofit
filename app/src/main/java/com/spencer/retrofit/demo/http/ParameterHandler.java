package com.spencer.retrofit.demo.http;

/**
 * @description: 参数处理类
 * @author: huanghuojun
 * @date: 2018/9/6 16:15
 */
public abstract class ParameterHandler {

    public abstract void apply(ServiceMethod serviceMethod, String s);

    static class Query extends ParameterHandler{
        private final String name;

        public Query(String value){
            this.name = value;
        }

        @Override
        public void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addQueryParamnter(name, value);
        }
    }

    static class Field extends ParameterHandler{
        private final String name;

        public Field(String value){
            this.name = value;
        }

        @Override
        public void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addQueryParamnter(name, value);
        }
    }
}
