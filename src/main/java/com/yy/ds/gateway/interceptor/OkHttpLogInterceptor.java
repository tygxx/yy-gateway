package com.yy.ds.gateway.interceptor;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Slf4j
public class OkHttpLogInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        // 这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();

        long t1 = System.currentTimeMillis();// 请求发起的时间
        log.info("okHttpRequest:{}", request.toString());

        Response response = chain.proceed(request);

        long t2 = System.currentTimeMillis();// 收到响应的时间

        // 这里不能直接使用response.body().string()的方式输出日志
        // 因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        // 个新的response给应用层处理,从响应正文中查看最多1024 * 1024个字节并将它们作为新响应返回
        ResponseBody responseBody = response.peekBody(1024 * 1024);

        log.info("okHttpResponse:{},耗时:{}毫秒", responseBody.string(), t2 - t1);

        return response;
    }
}