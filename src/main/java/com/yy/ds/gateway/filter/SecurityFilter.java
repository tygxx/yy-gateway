
package com.yy.ds.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yy.ds.gateway.config.IgnoreUrlsConfig;
import com.yy.ds.gateway.utils.IpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.SecureUtil;
import reactor.core.publisher.Mono;

@Component
public class SecurityFilter implements GlobalFilter, Ordered {

    private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 日志记录
        recordLog(exchange);

        // 获取请求的url
        String path = exchange.getRequest().getURI().getPath();
        PathMatcher pathMatcher = new AntPathMatcher();

        // 白名单路径直接放行
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, path)) {
                return chain.filter(exchange);
            }
        }

        return sighCheck(exchange, chain);
    }

    /**
     * 有多个过滤器时，过滤器执行的顺序，数字越小越优先执行
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 签名检查
     */
    private Mono<Void> sighCheck(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String userId = request.getHeaders().getFirst("userId");
        String timestamp = request.getHeaders().getFirst("timestamp");
        String signature = request.getHeaders().getFirst("signature");

        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(signature)) {
            return responseResult(response);
        }

        if (!checkSignature(userId, timestamp, signature)) {
            return responseResult(response);
        }

        return chain.filter(exchange);
    }

    /**
     * 校验签名
     *
     * @param userId
     * @param timestamp
     * @param signature
     * @return
     */
    private Boolean checkSignature(String userId, String timestamp, String signature) {
        String id = Base64Encoder.encode(userId);
        String temp = id + timestamp;
        String md5 = SecureUtil.md5(temp);
        return signature.equals(md5);
    }

    /**
     * 对请求失败结果进行封装
     * 
     * @param response
     * @return
     */
    private Mono<Void> responseResult(ServerHttpResponse response) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", 401);
        data.put("message", "签名校验失败");
        byte[] datas = JSON.toJSONString(data).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(datas);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 请求日志记录
     * 
     * @param exchange
     */
    private void recordLog(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        JSONObject json = new JSONObject();
        json.put("method", request.getMethod());
        json.put("path", request.getPath().toString());
        json.put("methodValue", request.getMethodValue());
        json.put("ip", IpUtil.getIp(request));
        json.put("queryParams", request.getQueryParams().toString());
        json.put("userId", request.getHeaders().getFirst("userId"));
        json.put("timestamp", request.getHeaders().getFirst("timestamp"));
        json.put("signature", request.getHeaders().getFirst("signature"));
        json.put("user-agent", request.getHeaders().getFirst("User-Agent"));
        logger.debug(json.toJSONString());
    }

    public static void main(String[] args) {
        String userId = "1";
        String timestamp = "1556582400000";
        String id = Base64Encoder.encode(userId);
        String temp = id + timestamp;
        String md5 = SecureUtil.md5(temp);
        System.out.println(md5);

    }
}
