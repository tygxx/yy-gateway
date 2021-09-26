package com.yy.ds.gateway.sso;

import java.util.HashMap;
import java.util.Map;

import com.yy.ds.common.api.CommonResult;
import com.yy.ds.common.constant.AuthConstant;
import com.yy.ds.gateway.feign.AuthFeignClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "登录管理")
@RestController
@RequestMapping("/sso")
public class SsoController {

    @Autowired
    private AuthFeignClient authFeignClient;

    @Value("${jks.secret}")
    private String secret;

    @ApiOperation(value = "登录以后返回token")
    @PostMapping(value = "/login")
    public CommonResult login(@ApiParam(value = "用户名", required = true) @RequestParam String username,
            @ApiParam(value = "密码", required = true) @RequestParam String password) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstant.ADMIN_CLIENT_ID);
        params.put("client_secret", secret);
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);
        return authFeignClient.getAccessToken(params);
    }
}