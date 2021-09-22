package com.yy.ds.gateway.config;

import java.util.concurrent.TimeUnit;

import com.yy.ds.gateway.interceptor.OkHttpLogInterceptor;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

@Configuration
// Spring工程中引用了Feign的包，才会构建这个bean
@ConditionalOnClass(Feign.class)
// 说明OkHttpConfig会在FeignAutoConfiguration之前加载
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class OkHttpConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                // 设置连接超时
                .connectTimeout(60, TimeUnit.SECONDS)
                // 设置读超时
                .readTimeout(60, TimeUnit.SECONDS)
                // 设置写超时
                .writeTimeout(120, TimeUnit.SECONDS)
                // 是否自动重连
                .retryOnConnectionFailure(true)
                // 指定了连接池的大小，最大保持连接数为 10 ，并且在 5 分钟不活动之后被清除
                .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
                // 自定义日志拦截器
                .addInterceptor(new OkHttpLogInterceptor()).build();
    }
}