package com.yy.ds.gateway.feign;

import com.yy.ds.gateway.common.CommonResult;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "system")
public interface SystemFeignClient {

    @GetMapping(value = "/user/{id}")
    CommonResult getUserById(@PathVariable Long id);

}
