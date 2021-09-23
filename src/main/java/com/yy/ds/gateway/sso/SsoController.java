package com.yy.ds.gateway.sso;

import com.alibaba.fastjson.JSONObject;
import com.yy.ds.gateway.common.CommonResult;
import com.yy.ds.gateway.feign.SystemFeignClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "登录管理")
@RestController
@RequestMapping("/test33")
public class SsoController {

    @Autowired
    private SystemFeignClient systemFeignClient;

    @ApiOperation(value = "测试")
    @GetMapping("{id}")
    public CommonResult<JSONObject> findOne(@PathVariable Long id) {
        return systemFeignClient.getUserById(id);
    }
}