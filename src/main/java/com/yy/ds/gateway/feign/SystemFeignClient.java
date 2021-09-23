package com.yy.ds.gateway.feign;

import com.alibaba.fastjson.JSONObject;
import com.yy.ds.common.api.CommonResult;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "system")
public interface SystemFeignClient {

    @GetMapping(value = "/user/{id}")
    CommonResult<JSONObject> getUserById(@PathVariable Long id);

}
